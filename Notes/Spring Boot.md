# Spring Boot Notes

## Overview

Spring Boot is an opinionated layer over the Spring Framework. Spring provides dependency injection, web, data access, transactions, security, and other infrastructure. Boot adds curated dependency versions, conditional auto-configuration, externalized configuration, executable packaging, and production integrations. It reduces setup; it does not dictate your architecture.

At startup, `SpringApplication.run` creates an `ApplicationContext`. The context registers bean definitions, creates and wires beans, applies post-processors/proxies, and starts the application. Boot auto-configuration contributes defaults based on classpath, properties, and existing beans. Defaults commonly back off when you provide your own bean.

## Project and startup

Use the Boot Maven parent/BOM or Gradle plugin to manage compatible dependency versions. Prefer starters (such as web, validation, data JPA, security, actuator, test), and avoid overriding managed library versions without a concrete reason.

```java
@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
```

`@SpringBootApplication` combines `@Configuration`, `@EnableAutoConfiguration`, and `@ComponentScan`. Put it in a top-level package above application components so scanning has a deliberate boundary; avoid the default package.

## Dependency injection and beans

A bean is an object managed by Spring. Register application components with `@Component`, `@Service`, `@Repository`, `@Controller`, or `@RestController`; use `@Bean` methods for third-party objects or explicit construction.

Prefer constructor injection: dependencies are visible, can be final, and are easy to supply in unit tests.

```java
@Service
class OrderService {
	private final OrderRepository orders;
	OrderService(OrderRepository orders) { this.orders = orders; }
}
```

One constructor needs no `@Autowired`. Avoid field injection and routine calls to `ApplicationContext.getBean()`. Use `@Qualifier` when multiple candidates exist; use `@Primary` only when one implementation is intentionally the default.

Singleton is the default bean scope and means one instance per application context, not per JVM. Keep singleton services stateless/thread-safe. Avoid slow external work during bean construction. Lifecycle hooks (`@PostConstruct`, `@PreDestroy`) should not obscure resource ownership.

### Proxies and AOP

Transactions, method security, and caching commonly use proxies. Advice applies when a call enters through the Spring-managed proxy. Self-invocation (`this.otherMethod()`), manually created objects (`new`), and some final/private method patterns can bypass advice. Put advised boundaries on externally invoked methods or refactor them into another bean.

## Auto-configuration and diagnosis

Auto-configuration is conditional configuration, not magic. If a bean is missing or unexpected, check package scanning, dependencies, active profiles, property values, conditions, and existing beans. Run with `--debug` for the condition evaluation report; inspect startup logs, dependency trees, or the context. Consult the auto-configuration conditions for the exact version. Avoid fighting defaults before understanding why they activated.

`ApplicationRunner` and `CommandLineRunner` run during startup. Keep their work short; long tasks delay readiness. They are not durable job schedulers.

## Configuration

Boot resolves properties from config files and external sources such as environment variables, system properties, and command-line arguments. More specific sources generally override packaged defaults; check the version reference for exact precedence and config-file discovery.

```yaml
server:
  port: ${PORT:8080}
spring:
  application:
	name: orders
  datasource:
	url: ${DB_URL}
	username: ${DB_USER}
	password: ${DB_PASSWORD}
```

Use `@ConfigurationProperties` for related, typed settings; add validation to fail fast on invalid values. Keep safe defaults in source control and provide deployment-specific values externally. Never commit secrets. Use a secret manager/platform integration and restrict endpoints that can expose configuration. Profiles are useful for materially different bean/configuration behavior, not as a replacement for ordinary environment-specific values.

## Web and REST

`spring-boot-starter-web` normally configures Spring MVC and an embedded servlet container. `@RestController` writes returned values as response bodies.

```java
@RestController
@RequestMapping("/api/orders")
class OrderController {
	private final OrderService service;
	OrderController(OrderService service) { this.service = service; }

	@GetMapping("/{id}")
	OrderResponse get(@PathVariable UUID id) { return service.get(id); }
}
```

Design around HTTP semantics and stable contracts. Use DTOs rather than serializing persistence entities. Validate request input with Jakarta Bean Validation (`@Valid`, constraints); validation does not replace domain rules or authorization. Use appropriate status codes, pagination limits, and headers. Centralize API error mapping with `@RestControllerAdvice`; return consistent safe errors and never leak stack traces or SQL details. Document compatibility and test contracts. CORS is not authentication; configure it narrowly.

MVC is usually the natural choice with blocking JDBC/JPA. WebFlux is for non-blocking I/O and streaming; reactive types do not make blocking calls non-blocking. Avoid blocking work on event-loop threads.

## Persistence and transactions

Spring Data can implement repository interfaces such as:

```java
interface OrderRepository extends JpaRepository<Order, UUID> {
	Optional<Order> findByExternalReference(String reference);
}
```

Use Flyway, Liquibase, or another reviewed migration process for production schema changes; `ddl-auto: update` is not a migration strategy. Keep entities out of API responses. Understand lazy loading, relationships, cascades, and entity equality. Watch for N+1 queries; use projections, fetch plans, and query inspection rather than loading everything eagerly. Prefer fetching required data within clear transaction boundaries over relying on Open Session in View.

Use database constraints as well as application validation. Index measured query patterns, inspect query plans, parameterize SQL, and size connection pools against database capacity and replica count.

`@Transactional` is usually proxy-based and applies to calls through the proxy. Default rollback is for unchecked exceptions and `Error`, not all checked exceptions. Set rollback rules deliberately. Put transactions around coherent service/use-case operations; keep them short and avoid network calls while holding database locks. `readOnly = true` is generally a hint, not a universal write prohibition. Transactions do not automatically cross threads or remote calls. For cross-system consistency, consider an outbox, idempotent consumers, and compensating actions rather than assuming a database transaction covers a broker or HTTP service.

## Security

Adding Spring Security changes application behavior; define the intended policy rather than relying on generated development credentials or assumptions. Modern configuration uses a `SecurityFilterChain` bean.

```java
@Bean
SecurityFilterChain security(HttpSecurity http) throws Exception {
	return http.authorizeHttpRequests(auth -> auth
		.requestMatchers("/actuator/health").permitAll()
		.anyRequest().authenticated()).build();
}
```

This is only a sketch: configure authentication, authorization, CSRF, CORS, sessions, and token validation for the actual threat model. Prefer established identity providers and OAuth 2.0/OIDC integrations to custom token schemes. Authentication is not authorization. Do not disable CSRF indiscriminately; consider browser credentials and threat model. Validate JWT issuer, signature, audience, expiry, and claims. Use password encoders, least privilege, patched dependencies, and safe logging. Restrict actuator endpoints; operational data can disclose sensitive information.

## Testing

Use the smallest test scope that proves the behavior:

- Unit tests: plain JUnit for isolated domain/business logic.
- `@WebMvcTest`: MVC mappings, validation, serialization, and error behavior.
- `@DataJpaTest`: repository behavior; understand embedded database substitution and transaction rollback.
- `@SpringBootTest`: full wiring/integration when required.
- Testcontainers or equivalent: realistic database/broker integration.

Assert observable behavior, important failures, and access control. Keep tests deterministic; control clocks, external calls, and shared state. Avoid mocking every collaborator or relying on test order. Test configuration is not production configuration; specify profiles and properties deliberately.

## Operations and observability

Actuator and Micrometer support health, metrics, and operational integration. Expose only required endpoints to trusted networks and secure them.

- Liveness indicates whether the process should be restarted; readiness indicates whether it should receive traffic. A temporary downstream failure should not automatically make liveness fail.
- Monitor request rate, errors, latency, saturation, JVM, connection pools, and dependencies. Avoid high-cardinality metric labels such as user IDs or full URLs.
- Use structured logs and distributed tracing/correlation context. Redact tokens, credentials, and personal data.
- Set timeouts for inbound requests and all downstream clients. Configure bounded thread/connection pools and queues.

For `@Async` and `@Scheduled`, remember proxy behavior and explicitly configure bounded executors, errors, shutdown, and context propagation. They are not durable job systems. For messaging, assume redelivery/duplicates unless proven otherwise; design idempotent consumers and handle ordering, retries, and dead letters.

## HTTP clients and resilience

Configure connection and response timeouts, TLS, pooling, and observability. Every remote call needs a timeout. Retries can amplify outages: keep them bounded, retry only transient failures, use backoff/jitter, and require idempotency for unsafe operations. Circuit breakers and bulkheads isolate failures but do not fix them; define fallback behavior carefully. Keep an end-to-end latency budget across service calls.

## Packaging and deployment

Boot commonly packages an executable JAR runnable with `java -jar app.jar`; executable WARs are also possible. Build reproducibly, promote the same artifact, scan dependencies/images, and run containers as non-root with minimal permissions. Keep secrets out of images. Size CPU, memory, and JVM settings from measurements. Configure graceful shutdown and readiness/liveness probes. Make migrations compatible with rolling deployments; use expand/migrate/contract where needed. Avoid relying on in-memory state when running multiple instances.

## Troubleshooting checklist

1. Identify the failing request/path and correlate logs, traces, and metrics.
2. Check conditions/configuration, profiles, package scan, and bean candidates for startup/wiring issues.
3. Check timeouts, pool exhaustion, queue depth, CPU/memory/GC, and downstream latency.
4. Inspect SQL count, N+1 behavior, lock waits, and query plans.
5. For ignored transactions/security/caching, check proxies, self-invocation, and filter/method rules.
6. For unexpected test behavior, inspect test slices, replacement databases, active properties, and context caching.

Measure before optimizing; make one targeted change and compare under representative load. Avoid indiscriminate production debug logging.

## Architecture guidance

The usual engineering fundamentals still apply: cohesion, explicit boundaries, failure handling, data ownership, and operational simplicity. Keep controllers as HTTP adapters, services coordinating use cases, and repositories focused on persistence. Framework-independent domain code can improve testability, but avoid needless abstraction layers and interfaces. Use DTOs at external boundaries; make transaction ownership visible. Treat integration events as versioned contracts. Prefer a modular monolith until independent deployment, ownership, or scaling needs justify service boundaries.

## Learning path

1. Build an MVC API with constructor injection and typed configuration.
2. Add validation, consistent error responses, and MVC tests.
3. Add persistence, migrations, and real-database integration tests.
4. Add transaction and failure-case tests.
5. Add authentication/authorization and test denied as well as allowed access.
6. Add Actuator, metrics, logs, and tracing; secure operational endpoints.
7. Package and run with external configuration, probes, and graceful shutdown.
8. Practice diagnosing auto-configuration, proxies, and database performance.

## Version notes

Spring Boot 3 requires Java 17+ and uses Jakarta APIs (`jakarta.*`); Boot 2 applications commonly use `javax.*`. APIs and configuration can vary between Boot releases. Consult the documentation and migration guide for the project's exact version, and let Boot manage compatible dependency versions.
