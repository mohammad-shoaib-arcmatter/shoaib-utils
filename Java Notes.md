# Java Notes

A practical reference for Core Java, modern language features, JVM internals, testing, build tools, and production best practices.

## Contents

- [How to Use These Notes](#how-to-use-these-notes)
- [Part I: Java Language Foundations](#part-i-java-language-foundations)
  - [1. Fundamentals](#1-fundamentals)
  - [2. Object-Oriented Programming](#2-object-oriented-programming)
  - [3. Keywords and Essentials](#3-keywords-and-essentials)
  - [4. Memory and Strings](#4-memory-and-strings)
  - [5. Exception Handling](#5-exception-handling)
- [Part II: Collections, Functional Java, and Concurrency](#part-ii-collections-functional-java-and-concurrency)
  - [6. Collections Framework](#6-collections-framework)
  - [7. Generics](#7-generics)
  - [8. Multithreading and Concurrency](#8-multithreading-and-concurrency)
  - [9. Java 8+ Features](#9-java-8-features)
- [Part III: JVM and Advanced Java](#part-iii-jvm-and-advanced-java)
  - [10. JVM Internals](#10-jvm-internals)
  - [11. Advanced Core Java](#11-advanced-core-java)
- [Part IV: Design, Build, Test, and Platform Evolution](#part-iv-design-build-test-and-platform-evolution)
  - [12. SOLID Principles and Design Patterns](#12-solid-principles-and-design-patterns)
  - [13. Maven and Gradle](#13-maven-and-gradle)
  - [14. Testing with JUnit and Mockito](#14-testing-with-junit-and-mockito)
  - [15. Java Platform Module System](#15-java-platform-module-system)
  - [16. Modern Java Features](#16-modern-java-features)
- [Part V: Production, Security, and Operations](#part-v-production-security-and-operations)
  - [17. Production Java Best Practices](#17-production-java-best-practices)
  - [18. Security Essentials](#18-security-essentials)
  - [19. Performance, Monitoring, and Troubleshooting](#19-performance-monitoring-and-troubleshooting)
  - [20. Quick Revision Checklist](#20-quick-revision-checklist)

## How to Use These Notes

- Read Parts I and II in order when learning Java fundamentals.
- Use Parts III and IV to understand runtime behavior and engineering practices.
- Use Part V as a production-readiness and interview-revision reference.
- Each chapter moves from core concepts to advanced behavior, then ends with review notes and common pitfalls.
- Code samples are illustrative; verify imports, Java-version requirements, and error handling before using them in production.


## Part I: Java Language Foundations

Core syntax, type rules, object-oriented design, memory concepts, text handling, and exceptions.

### 1. Fundamentals

#### 1.1 Java Platform Components

##### JVM - Java Virtual Machine

- The engine that actually runs your code
- Reads .class bytecode and converts it to machine code for your OS
- Handles memory, garbage collection, security
- You never download JVM alone - it comes inside JRE/JDK
- One JVM per platform: Windows JVM, Linux JVM, etc. That's why Java is "write once, run anywhere" - same bytecode, different JVM translates it.

##### JRE - Java Runtime Environment

- JVM + libraries needed to _run_ Java apps
- Contains: JVM + core class libraries (java.lang, java.util etc) + other supporting files
- If you only want to RUN a Java app (like Minecraft), you need JRE
- Cannot develop - no compiler inside

##### JDK - Java Development Kit

- JRE + tools needed to _develop_ Java apps
- Contains: JRE (so includes JVM) + javac compiler + debugger + javadoc + other dev tools
- If you want to WRITE and compile Java code, you need JDK

#### 1.2 Variables and Data Types

**Explanation:** A variable is a named location whose declared type limits the values and operations the compiler permits. Primitive variables contain simple values, while reference variables can point to objects or be null.

**Why it matters:** These rules are enforced by the compiler or runtime and become assumptions used by every Java API.

- Variable: Name for memory location. Must declare type before use.
  int age; // declaration
  age = 25; // initialization
  final int MAX = 100; // constant, cannot change
- Primitive - 8 types:

| Type | Size | Range / Values | Default |
| --- | --- | --- | --- |
| `byte` | 1 byte | -128 to 127 | `0` |
| `short` | 2 bytes | -32,768 to 32,767 | `0` |
| `int` | 4 bytes | -2³¹ to 2³¹ - 1 | `0` |
| `long` | 8 bytes | -2⁶³ to 2⁶³ - 1 | `0L` |
| `float` | 4 bytes | IEEE 754 single precision | `0.0f` |
| `double` | 8 bytes | IEEE 754 double precision | `0.0d` |
| `char` | 2 bytes | UTF-16 code unit | `'\u0000'` |
| `boolean` | JVM-dependent storage | `true` or `false` | `false` |

- Reference: Stores address pointing to heap.
  String name = "Stitch"; // String pool
  int[] arr = new int[5]; // array is object in Java
  // Reference comparison: == checks address,.equals() checks content
- Memory: Primitives on stack (fast), objects on heap (GC cleans). Local variables no default - you must init.

#### 1.3 Operators and Type Casting

**Explanation:** Operators build expressions from values. Casting asks Java to view or convert a value as another type; widening usually preserves information, while narrowing can overflow, truncate, or fail at runtime.

**Why it matters:** These rules are enforced by the compiler or runtime and become assumptions used by every Java API.

- Unary: ++a pre-increment, a++ post, --, !, ~
- Arithmetic: + - * / %.
- Relational: ==!= > < >= <=
- Logical: &&, ||
- Ternary: String res = (marks>40)? "pass" : "fail";
- instanceof: checks object type - if(obj instanceof String)

- Type Casting:
    - Widening (Implicit) - safe: JVM does automatically.
    byte -> short -> int -> long -> float -> double
    - Narrowing (Explicit) - risky: You must cast, may lose data.
    double d = 100.99;
    int i = (int) d; // 100 -.99 lost
    int big = 130;
    byte b = (byte) big; // -126 - overflow
- Upcasting/Downcasting for objects: Parent p = new Child(); // upcast auto and Child c = (Child) p; // downcast

#### 1.4 Control Flow

**Explanation:** Control-flow statements decide which instructions execute and how often. Each branch and loop should have a clear condition, termination rule, and behavior for boundary inputs.

**Why it matters:** These rules are enforced by the compiler or runtime and become assumptions used by every Java API.

- if-else ladder: For ranges. Only first true executes.
  if (score >= 90) grade='A';
  else if (score >= 75) grade='B';
  else grade='C';

- switch: Works with int, char, String, enum. Break needed else fall-through.
  int month=2;
  switch(month){
    case 1: System.out.println("Jan"); break;
    case 2: System.out.println("Feb"); break;
    default: System.out.println("Invalid");
  }

  // Java 14+ switch expression - no break needed
  String res = switch(month){
    case 1 -> "Jan";
    case 2 -> "Feb";
    default -> "Invalid";
  };

- Loops:
  // for - when you know count
  for(int i=0; i<10; i++){ if(i==5) continue; }

  // while - when cond is unknown
  while(scanner.hasNext()){ }

  // do-while - executes atleast once
  do{ } while(x<10);

  // for-each - for array/collection
  for(String s: list){ System.out.println(s); }
- break vs continue vs return:
    - break; - breaks out of loop/switch fully.
    - continue; - skips rest of current iteration, goes to next.
    - Labeled break: outer: for() { for() { break outer; } } - breaks outer loop.

#### 1.5 Input and Output

**Explanation:** Input converts external text or bytes into program values, while output converts values into a representation for a console, file, or another system. Parsing and formatting are boundary operations and need validation.

**Why it matters:** These rules are enforced by the compiler or runtime and become assumptions used by every Java API.

- Output:
  System.out.println(); // with newline
  System.out.print(); // without
  System.err.println("error"); // for error stream
  System.out.printf("Name %s, age %d, sal %.2f", name, age, sal);

- Input:
  // Method 1: Scanner - easy but slow, use for interviews
  import java.util.Scanner;
  Scanner sc = new Scanner(System.in);
  int a = sc.nextInt(); // leaves \n
  sc.nextLine(); // consume leftover
  String name = sc.nextLine(); // full line
  String word = sc.next(); // single word
  sc.close();

  // Method 2: BufferedReader - fast, for large input
  BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
  String line = br.readLine();
  int num = Integer.parseInt(line);

  // Method 3: System.console - for password
  Console c = System.console();
  char[] pwd = c.readPassword();


#### 1.6 Compilation, Execution, and Classpath

The normal lifecycle is:

```text
Source.java --javac--> Source.class --JVM verification/loading--> execution
```

```text
javac -d out src/com/example/Main.java
java -cp out com.example.Main
jar --create --file app.jar -C out .
java -cp app.jar com.example.Main
```

- `javac` checks syntax and types, then produces platform-neutral bytecode.
- `java` starts a JVM, locates the requested class, loads dependencies, and calls `public static void main(String[] args)`.
- The classpath is an ordered list of directories and JAR files used to locate classes.
- A package name must match the logical directory structure used by the compiler/class loader.
- `NoClassDefFoundError` means a class available during compilation could not be loaded at runtime or failed initialization.
- `ClassNotFoundException` is a checked exception produced by explicit dynamic-loading APIs such as `Class.forName`.

#### 1.7 Primitive Details and Numeric Accuracy

**Explanation:** Integer types have fixed ranges and wrap on ordinary overflow. Floating-point types approximate real numbers in binary, so exact decimal domains such as money require `BigDecimal` and explicit rounding.

**Why it matters:** These rules are enforced by the compiler or runtime and become assumptions used by every Java API.

- `byte`, `short`, `int`, and `long` are signed two's-complement integers.
- `char` is an unsigned UTF-16 code unit, not necessarily a complete Unicode character.
- Integer arithmetic with `byte`, `short`, or `char` is promoted to at least `int`.
- Integer division discards the fractional part: `7 / 2` is `3`.
- Floating-point values follow IEEE 754 and cannot exactly represent many decimal fractions.
- Use `BigInteger` for integers beyond `long` and `BigDecimal` for exact decimal arithmetic.

```java
System.out.println(0.1 + 0.2); // approximately 0.30000000000000004

BigDecimal left = new BigDecimal("0.1");
BigDecimal right = new BigDecimal("0.2");
System.out.println(left.add(right)); // exactly 0.3
```

Numeric overflow does not throw by default:

```java
int value = Integer.MAX_VALUE;
System.out.println(value + 1); // wraps to Integer.MIN_VALUE
Math.addExact(value, 1);       // throws ArithmeticException
```

#### 1.8 Scope, Lifetime, and Parameter Passing

**Explanation:** Scope determines where a name is visible; lifetime determines how long its value or object remains reachable. Java passes every argument by value, including a copied value of an object reference.

**Why it matters:** These rules are enforced by the compiler or runtime and become assumptions used by every Java API.

- Local variables exist within their declaring block and must be definitely assigned before use.
- Instance fields belong to an object and receive default values.
- Static fields belong to the class and are shared by instances loaded by that class loader.
- Java is always pass-by-value. A method receives a copy of a primitive value or a copy of an object reference.

```java
static void rename(StringBuilder name) {
  name.append(" Khan");          // mutates the referenced object
  name = new StringBuilder("X"); // changes only the local reference copy
}
```

#### 1.9 Arrays

**Explanation:** An array is a fixed-length object containing elements of one component type. It provides fast indexed access but no automatic growth, so collection classes are usually easier for changing data sets.

**Why it matters:** These rules are enforced by the compiler or runtime and become assumptions used by every Java API.

- Arrays are fixed-size objects with zero-based indexes.
- Array elements receive defaults; a local array reference does not.
- Arrays are covariant, so `Number[] values = new Integer[2]` compiles but can throw `ArrayStoreException`.
- Common utilities include `Arrays.copyOf`, `sort`, `binarySearch`, `equals`, and `deepEquals`.
- For resizable sequences, prefer `ArrayList`.

#### 1.10 Literals and Compile-Time Constants

Java supports decimal, hexadecimal, octal, and binary integer literals:

```java
int decimal = 255;
int hexadecimal = 0xFF;
int binary = 0b1111_1111;
int readable = 1_000_000;
```

- Underscores can separate digits but cannot appear at the beginning, end, or next to a decimal point or type suffix.
- An integer literal is `int` unless it requires or explicitly uses the `L` suffix.
- A floating-point literal is `double` unless it uses `F`.
- A compile-time constant is a primitive or `String` `final` variable initialized with a constant expression.
- Compile-time constants may be inlined into client bytecode. Changing a public constant may therefore require recompiling clients.

#### 1.11 Expressions and Promotion Rules

Binary numeric promotion converts operands before arithmetic:

1. If either operand is `double`, both become `double`.
2. Otherwise, if either is `float`, both become `float`.
3. Otherwise, if either is `long`, both become `long`.
4. Otherwise, both become `int`.

```java
byte left = 10;
byte right = 20;
// byte sum = left + right; // error: result is int
byte sum = (byte) (left + right);
```

Compound assignment includes an implicit narrowing conversion:

```java
short value = 1;
value += 2;        // compiles
// value = value + 2; // does not compile without a cast
```

Evaluate operands before applying operators. `&&` and `||` short-circuit; `&` and `|` always evaluate both boolean operands.

#### 1.12 Command-Line Arguments and Environment

**Explanation:** Arguments, environment variables, and system properties provide external configuration as text. They must be parsed into typed values, validated, and handled without exposing secrets.

**Why it matters:** These rules are enforced by the compiler or runtime and become assumptions used by every Java API.

```java
public static void main(String[] args) {
  for (String argument : args) {
    System.out.println(argument);
  }
}
```

- Arguments are strings and require explicit parsing.
- Validate missing, duplicate, and malformed options.
- `System.getenv` reads environment variables; `System.getProperty` reads JVM system properties.
- Set a system property with `-Dapp.mode=production`.
- Environment and system properties are global process state; wrap access behind typed configuration for testability.

#### 1.13 Packages, JARs, and Manifests

A JAR is a ZIP archive containing classes, resources, and metadata.

```text
jar --create --file app.jar --main-class com.example.Main -C out .
java -jar app.jar
```

`META-INF/MANIFEST.MF` can declare `Main-Class`, implementation version, automatic module name, and other metadata. A normal executable JAR does not automatically include dependency JARs; use an application layout, module path, or deliberately built executable/fat JAR.

#### 1.14 Chapter Review and Common Pitfalls

##### JVM, JRE, and JDK

- A JDK distribution includes development tools and a runtime, but since Java 11 most vendors no longer ship a separate consumer JRE.
- Java SE defines specifications; OpenJDK provides the reference implementation. Vendors may package different collectors, diagnostics, support periods, and licenses while preserving Java compatibility.
- `java -version`, `javac -version`, and the runtime actually used by a service can differ. Record both compiler and runtime versions during diagnosis.
- Bytecode compatibility flows toward newer runtimes: a newer JVM can usually load older class files, while an older JVM rejects newer class-file versions with `UnsupportedClassVersionError`.

##### Variables, primitives, and references

- Field defaults do not apply to local variables. Definite-assignment analysis proves local initialization along every reachable path.
- A reference value is either `null` or identifies an object; Java does not expose pointer arithmetic.
- Wrapper objects can be null, so auto-unboxing can throw `NullPointerException`.
- Primitive comparison is value-based. Floating-point `NaN` is unequal to every value, including itself; use `Double.isNaN`.
- Use `Integer.compare`, `Long.compare`, and `Double.compare` rather than subtraction in comparators.

##### Operators and expressions

- Operator precedence controls grouping, not evaluation order. Java evaluates operands left-to-right.
- `a && b` skips `b` when `a` is false; `a || b` skips `b` when `a` is true.
- Shift distances are masked: `int` uses the low 5 bits and `long` the low 6 bits.
- `>>` preserves the sign bit; `>>>` shifts in zeros.
- Parentheses should clarify mixed arithmetic, logical, and bitwise expressions even when precedence is known.

##### Control flow

- A `switch` expression must be exhaustive. Enums and sealed hierarchies enable compile-time exhaustiveness checks.
- `break`, `continue`, and `return` still execute enclosing `finally` blocks.
- Prefer early return for invalid conditions when it reduces nesting, but keep cleanup centralized through try-with-resources.
- Labeled flow is legal but often signals logic that should be extracted into a method.

##### Input and output

- `Scanner` performs tokenization and regex parsing and is unsuitable for high-volume parsing without measurement.
- `BufferedReader.readLine()` removes line terminators; preserve separators explicitly if exact file reproduction matters.
- `System.console()` commonly returns null inside IDEs, redirected processes, and CI.
- Specify `Locale` for parsing formatted numbers and `Charset` for text bytes.
- Closing a wrapper stream normally closes its underlying stream; do not close process-wide `System.in`, `System.out`, or `System.err` from library code.

##### Compilation and classpath

- Classpath order can determine which duplicate class is loaded, producing environment-specific failures.
- Package-private access is enforced by both package name and runtime module/class-loader context.
- Annotation processing happens during compilation before generated sources are compiled.
- `javap -c -p ClassName` inspects bytecode and helps explain compiler transformations.
- Compile and run from a clean output directory to avoid stale class files masking source changes.

##### Numeric precision and arrays

- `BigDecimal.equals` compares value and scale; `1.0` and `1.00` are unequal. `compareTo` treats them as numerically equal.
- Specify a `MathContext` or rounding mode for non-terminating decimal operations.
- Multidimensional Java arrays are arrays of arrays and can be jagged.
- `System.arraycopy` handles overlapping ranges and performs runtime type checks for reference arrays.
- An array's `clone()` makes a shallow copy; nested arrays or mutable elements remain shared.

##### CLI, environment, and packaging

- Treat command-line and environment values as untrusted text: parse, validate, and report the exact option without exposing secrets.
- Environment variable names and case sensitivity vary by operating system.
- A JAR manifest line has formatting rules and continuation behavior; prefer build tooling over manual editing.
- Signed JARs protect artifact integrity, not application authorization.
- Shaded JARs can break service descriptors, signatures, resources, and reflection unless transformers are configured correctly.

### 2. Object-Oriented Programming

#### 2.1 Classes, Objects, and Constructors

**Explanation:** A class defines state and behavior; an object is one instance with its own instance state. A constructor should create a complete valid object rather than leave callers to finish initialization.

**Why it matters:** Clear object boundaries make later changes and tests safer because state can change only through known contracts.

- Class: Blueprint / template. No memory.
  public class Employee {
    String name; // instance variable
    static String company = "Stitch"; // static - shared by all
  }
- Object: Real instance of class. Memory on heap.
  Employee e1 = new Employee(); // new creates object
  e1.name = "Ali";
  Employee e2 = e1; // e2 points to same object, not new object
- Constructor: Special method to initialize object. Name same as class, no return type. Called automatically when new.
  public class Employee {
    String name;
    // Default - if you don't write, Java gives this
    Employee() { this.name = "Unknown"; }

    // Parameterized
    Employee(String name) { this.name = name; }

    // Copy constructor - Java doesn't have default, you write
    Employee(Employee other) { this.name = other.name; }
  }
- Constructor chaining: this() calls another constructor of same class, must be first line.
  Employee() { this("Unknown"); } // calls parameterized

#### 2.2 Encapsulation and Accessors

**Explanation:** Encapsulation keeps representation details private and exposes operations that preserve invariants. Getters and setters are useful only when they do not bypass the rules the class is responsible for enforcing.

**Why it matters:** Clear object boundaries make later changes and tests safer because state can change only through known contracts.

- Encapsulation: Hide data using private, expose via methods. For data security + validation.
  public class BankAccount {
    private double balance; // cannot access directly from outside

    public double getBalance() { return balance; } // getter

    public void setBalance(double bal) { // setter with validation
      if(bal >= 0) this.balance = bal;
      else System.out.println("Invalid");
    }
  }
- Why? If variable public, anyone can set balance = -10000. With setter, you control.
    - Interview: Encapsulation is data hiding, Abstraction is implementation hiding.

#### 2.3 Inheritance

**Explanation:** Inheritance creates an IS-A relationship in which a child receives accessible parent behavior. It should be used only when every child can safely substitute for the parent contract.

**Why it matters:** Clear object boundaries make later changes and tests safer because state can change only through known contracts.

- One class gets properties of another. For code reuse. IS-A relationship.
  class Parent { String surname = "Khan"; }
  class Child extends Parent { String name = "Ali"; }
  // Child now has surname + name
- super keyword:
    - super() - calls parent constructor, must be first line in child constructor
    - super.var - parent variable
    - super.method() - parent method
  class Child extends Parent {
    Child() {
      super(); // calls Parent()
      System.out.println(super.surname);
    }
  }
- Types: Single, Multilevel (A->B->C), Hierarchical (B and C extends A). Java doesn't support Multiple inheritance with classes (diamond problem) - use interfaces.
- Object class is parent of all classes in Java.

#### 2.4 Polymorphism

**Explanation:** Polymorphism lets code depend on a shared type while runtime dispatch selects the object's overriding implementation. Overloading is different: the compiler selects an overload from declared argument types.

**Why it matters:** Clear object boundaries make later changes and tests safer because state can change only through known contracts.

- A. Compile-time / Static - Overloading: Same method name, different params in SAME class. Compiler decides which to call.
  class Calculator {
    int add(int a, int b) { return a+b; }
    int add(int a, int b, int c) { return a+b+c; } // diff count
    double add(double a, double b) { return a+b; } // diff type
  }
  // Rules: Return type alone not enough to overload, must change params
- B. Runtime / Dynamic - Overriding: Same method name + same params, in CHILD class. JVM decides at runtime which to call based on object.
  class Bank { double getRate() { return 5.0; } }
  class SBI extends Bank {
    @Override
    double getRate() { return 7.5; } // overrides
  }
  Bank b = new SBI(); // Parent ref, Child object
  b.getRate(); // 7.5 - Child's method runs - Runtime Polymorphism
- Rules for overriding: Need inheritance, same signature, cannot override private/static/final, access cannot be more restrictive.

#### 2.5 Abstraction: Abstract Classes and Interfaces

**Explanation:** Abstraction exposes what a type promises while hiding how it performs the work. Interfaces emphasize capabilities; abstract classes can additionally share state, construction, and implementation.

**Why it matters:** Clear object boundaries make later changes and tests safer because state can change only through known contracts.

- Show WHAT to do, hide HOW.
- Abstract Class (0-100% abstraction):
  abstract class Payment {
    abstract void pay(); // no body - child must implement
    void receipt() { System.out.println("Receipt printed"); } // can have concrete method
    Payment() { } // can have constructor
  }
  // Payment p = new Payment(); // ERROR - cannot create object
  class UPI extends Payment {
    void pay() { System.out.println("Pay via UPI"); }
  }
- Interface (100% abstraction - till Java 7, now can have default/static from Java 8):
  interface PaymentGateway {
    int VERSION = 1; // public static final by default
    void pay(); // public abstract by default
    default void log() { System.out.println("Logging"); } // Java 8
  }
  class Razorpay implements PaymentGateway {
    public void pay() { System.out.println("Pay"); }
  }
- Interview Difference Table:
| Abstract Class | Interface |
| --- | --- |
| extends | implements |
| Can have constructor, variables | No constructor, only constants |
| Single inheritance | Multiple interfaces can be implemented |
| Use when IS-A + some common code | Use when capability - can do |

Java 8+: Interface can have default and static methods to avoid breaking old code.

#### 2.6 Access Modifiers

Controls visibility:
| Modifier | Same Class | Same Package | Child (diff pkg) | World |
| --- | --- | --- | --- | --- |
| private | YES | NO | NO | NO |
| default (no keyword) | YES | YES | NO | NO |
| protected | YES | YES | YES | NO |
| public | YES | YES | YES | YES |

public class A {
  private int a = 1; // only inside A
  int b = 2; // default - package only
  protected int c = 3; // package + child outside pkg
  public int d = 4; // anywhere
}

##### Interview Notes

- Why protected needed? To allow child outside package to access parent.
- Encapsulation uses private + public getters/setters.

#### 2.7 Composition, Association, and Aggregation

**Explanation:** These relationships describe objects using or owning other objects. Composition is usually more flexible than inheritance because collaborators can be replaced without changing the containing type hierarchy.

**Why it matters:** Clear object boundaries make later changes and tests safer because state can change only through known contracts.

- **Association:** one object knows or uses another, such as `Order` using `PaymentService`.
- **Aggregation:** a whole references parts that can exist independently, such as `Department` and `Employee`.
- **Composition:** the whole owns the part's lifecycle, such as `House` creating and owning its `Room` objects.

Prefer composition over inheritance when the relationship is HAS-A rather than IS-A. Composition reduces coupling and permits behavior to be replaced at runtime.

```java
class ReportService {
  private final Formatter formatter;

  ReportService(Formatter formatter) {
    this.formatter = Objects.requireNonNull(formatter);
  }
}
```

#### 2.8 Initialization Order

Object initialization follows this general order:

1. Parent class initialization, then child class initialization, once per class.
2. Memory allocation with instance fields set to default values.
3. Parent instance field initializers and initializer blocks.
4. Parent constructor.
5. Child instance field initializers and initializer blocks.
6. Child constructor.

Calling an overridable method from a constructor is dangerous because child fields may not yet be initialized.

#### 2.9 Method Dispatch and Covariant Returns

**Explanation:** Overridden instance methods dispatch from the runtime receiver type. An override may return a more specific type, but it must preserve access, exception, and behavioral promises.

**Why it matters:** Clear object boundaries make later changes and tests safer because state can change only through known contracts.

- Instance methods are dynamically dispatched from the runtime object type.
- Fields, static methods, and private methods are resolved from the reference or declaring type and are not polymorphic.
- An overriding method may return a subtype of the parent's return type.
- It cannot throw broader checked exceptions than the overridden method.
- It may widen access, such as `protected` to `public`, but cannot narrow access.

#### 2.10 Immutability

An immutable class should initialize all state during construction, prevent mutation, avoid leaking mutable internals, and prevent unsafe subclassing.

```java
final class Schedule {
  private final List<LocalDate> dates;

  Schedule(List<LocalDate> dates) {
    this.dates = List.copyOf(dates);
  }

  List<LocalDate> dates() {
    return dates;
  }
}
```

Immutability simplifies equality, caching, and thread safety, although copying large mutable inputs may have a cost.

#### 2.11 Nested Classes

**Explanation:** Nested types keep helper concepts near their owner. Static nested classes do not retain an outer instance, whereas inner classes do and can access that outer object's members.

**Why it matters:** Clear object boundaries make later changes and tests safer because state can change only through known contracts.

- A static nested class has no implicit outer-object reference.
- An inner class is tied to an enclosing instance and can access its members.
- A local class is declared inside a block.
- An anonymous class creates a one-off subclass or interface implementation.

```java
class Outer {
  private int value = 10;

  static class Nested {}

  class Inner {
    int read() {
      return value;
    }
  }
}
```

Prefer static nested classes unless access to an enclosing instance is required. A non-static inner instance can unintentionally retain its outer object.

#### 2.12 Enums as Full Classes

Enums provide a fixed set of instances and can have fields, methods, constructors, and per-constant behavior:

```java
enum Operation {
  ADD {
    double apply(double a, double b) { return a + b; }
  },
  MULTIPLY {
    double apply(double a, double b) { return a * b; }
  };

  abstract double apply(double a, double b);
}
```

- Enum constructors are implicitly private.
- Compare enums with `==`.
- Persist a stable external code rather than `ordinal()`, because reordering constants changes ordinals.
- `EnumSet` and `EnumMap` are compact, efficient collections for enum keys.

#### 2.13 Object Methods

Important methods inherited from `Object`:

- `toString`: human-readable representation; avoid including secrets.
- `equals` and `hashCode`: logical identity and hash-based collection behavior.
- `getClass`: exact runtime class.
- `clone`: protected shallow-copy mechanism; usually prefer constructors/factories.
- `wait`, `notify`, `notifyAll`: intrinsic-monitor coordination.

When inheritance is allowed, decide whether equality uses `instanceof` or exact `getClass()` checks. Exact-class equality avoids many symmetry problems; value-based hierarchies require particularly careful design.

#### 2.14 Tell, Do Not Ask

Objects should generally protect their own invariants:

```java
// Weak: caller reads state and decides how to mutate it.
if (account.balance().compareTo(amount) >= 0) {
  account.setBalance(account.balance().subtract(amount));
}

// Better: account performs and validates the operation atomically.
account.withdraw(amount);
```

This is not a ban on getters. The goal is to avoid moving domain rules into unrelated callers and duplicating invariants.

#### 2.15 Chapter Review and Common Pitfalls

##### Classes, objects, and constructors

- Object identity is distinct from logical equality. Two separate objects may represent the same value.
- Constructor overloads should delegate to one canonical constructor so validation is not duplicated.
- A constructor should establish all invariants before publishing the object.
- Static factories can name creation modes, cache instances, return subtypes, and avoid constructing invalid objects.
- Avoid doing remote calls or starting threads inside constructors.

##### Encapsulation

- Encapsulation protects invariants, not merely fields. A class with private fields but unrestricted setters may still be poorly encapsulated.
- Expose operations such as `deposit` or `reschedule` instead of generic mutation when rules accompany the change.
- Defensive copying must happen on both input and output boundaries when mutable values are involved.
- Package-private types and methods are useful implementation boundaries that remain testable within the package.

##### Inheritance

- Inheritance couples child code to protected behavior and superclass construction rules.
- Favor shallow hierarchies and stable abstract contracts.
- A subclass must not strengthen preconditions, weaken postconditions, or violate invariants expected through the parent type.
- Constructors are not inherited. A child constructor always invokes a parent constructor.
- Private members exist in the parent portion of the object but are not directly accessible by the child.

##### Polymorphism

- Overload resolution occurs at compile time from declared argument types; overriding dispatch occurs at runtime from the receiver object.
- Passing `null` to overloaded reference parameters can be ambiguous.
- Varargs participate late in overload resolution and can create surprising calls.
- Static method hiding should be avoided because behavior depends on the reference type.
- Use `@Override` so the compiler catches accidental signature mismatches.

##### Abstract classes and interfaces

- Abstract classes can own instance state and protected construction; interfaces define capabilities and support multiple inheritance of type.
- Interface fields are implicitly `public static final`.
- Interface abstract methods are implicitly public and cannot be implemented with weaker visibility.
- Default-method conflicts must be resolved explicitly; class methods take precedence over interface defaults.
- Prefer small interfaces defined near the consumer rather than broad provider-shaped interfaces.

##### Nested types, enums, and Object methods

- Local and anonymous classes can capture only final or effectively final local variables.
- Enum constants are initialized during class initialization; avoid circular static dependencies.
- `toString` should be concise, stable enough for diagnostics, and free of credentials.
- Equality across mutable inheritance hierarchies is difficult to make symmetric and transitive; composition or final value classes are safer.
- `System.identityHashCode` exposes identity hashing even when `hashCode` is overridden, but it is not a memory address.

##### Composition and modeling

- Constructor injection establishes required dependencies; method injection fits per-operation collaborators.
- Avoid bidirectional associations unless both navigation directions are required and consistency is maintained.
- Model aggregate operations atomically so callers cannot observe intermediate invalid states.
- Value objects should validate at construction and usually be immutable.
- Domain objects should not depend directly on transport or persistence frameworks unless the trade-off is intentional.

### 3. Keywords and Essentials

#### 3.1 `this` Keyword

Current object reference. Used to remove ambiguity.
class Employee {
  String name;
  Employee(String name) {
    this.name = name; // this.name = instance var, name = local param
  }
  void display() {
    System.out.println(this); // prints object address
  }
  void methodA() { this.methodB(); } // calls current class method
  Employee getObj() { return this; } // return current object

  Employee() { this("Unknown"); } // this() calls same class constructor, must be first line
}
- Interview: this cannot be used in static context because static has no object.

#### 3.2 `super` Keyword

Parent object reference.
class Parent { int x=10; void show(){ System.out.println("Parent"); } Parent(){ System.out.println("Parent cons"); } }
class Child extends Parent {
  int x=20;
  Child() { super(); } // super() calls parent constructor, must be first line. If you don't write, Java adds super() automatically.
  void show() { 
    System.out.println(x); // 20 child
    System.out.println(super.x); // 10 parent
    super.show(); // calls parent show()
  }
}
- this() vs super(): Both must be first line, so you cannot use both in same constructor.

#### 3.3 `final` Keyword

1.  final variable: Constant, cannot reassign. Must init once.
    final int MAX = 100;
    // MAX = 200; // ERROR
    final int x; x = 10; // blank final - allowed if init in constructor
2.  final method: Cannot be overridden.
    class Parent { final void pay(){} }
    class Child extends Parent { // void pay(){} ERROR
    }
3.  final class: Cannot be inherited. Used for security.
    final class SBI {} // class Child extends SBI ERROR
    // String, Integer are final classes in Java

#### 3.4 `static` Keyword

Memory once in Method Area, shared by all objects.
class Employee {
  String name; // instance - each object has own copy
  static String company = "Stitch"; // class variable - one copy for all
  static int count = 0;

  Employee() { count++; } // counts objects

  static void changeCompany() { 
    company = "Stitch.sa"; 
    // System.out.println(name); ERROR - static cannot access non-static directly
  }
  void display() {
    System.out.println(name + " " + company); // non-static can access static
  }
  static { System.out.println("Static block runs once when class loads"); }
}
Employee.changeCompany(); // call without object - ClassName.method
System.out.println(Employee.company);
- Static method cannot use this/super.
- Static block executes when class loads, before main, used to init static vars.
- Interview trick: Can we override static method? No, it's method hiding not overriding. Parent p = new Child(); p.staticMethod() calls Parent's, not Child's.

#### 3.5 Packages and Imports

**Explanation:** Packages organize types and provide an access boundary. Imports only shorten source names; they neither install a library nor load a class.

**Why it matters:** Misunderstanding declaration modifiers and resolution rules often creates subtle initialization, equality, or visibility bugs.

Package = folder to avoid name clash, organize code.
package com.stitch.payment; // first line, defines package

import java.util.Scanner; // import single class
import java.util.*; // import all classes in util, but not sub-packages
import static java.lang.Math.*; // static import - use sqrt() directly without Math.

public class UPI {
  java.util.Date d = new java.util.Date(); // fully qualified - no import needed
}
- java.lang is auto imported (String, System, Math).
- To create package: javac -d . File.java creates folder structure.

#### 3.6 Wrapper Classes and Autoboxing

Primitives have object version - needed for Collections (Collections cannot store primitive).
| Primitive | Wrapper (in java.lang) |
| --- | --- |
| int | Integer |
| char | Character |
| byte, short, long, float, double, boolean | Same with capital - Byte etc |

// Boxing - primitive to object
int a = 10;
Integer obj1 = Integer.valueOf(a); // manual boxing
Integer obj2 = a; // autoboxing - auto done by compiler (Java 5+)

// Unboxing - object to primitive
Integer obj = 20;
int b = obj.intValue(); // manual unboxing
int c = obj; // auto-unboxing

// Why needed?
ArrayList<int> list; // ERROR
ArrayList<Integer> list2 = new ArrayList<>(); // OK
list2.add(10); // autoboxing int -> Integer
int val = list2.get(0); // unboxing

// Useful methods
Integer.parseInt("123"); // String to int
Integer.toString(123); // int to String
Integer.MAX_VALUE; // constants
- Autoboxing vs Unboxing: Compiler does automatically.
- Caching: Integer caches -128 to 127. Integer a=100, b=100 -> a==b true, but a=200,b=200 -> a==b false because new objects. Use .equals() always.

Interview note: use `.equals()` rather than `==` to compare wrapper values.

#### 3.7 Important Modifiers

**Explanation:** Modifiers change declaration behavior, such as whether a member is abstract, synchronized, volatile, transient, or implemented natively. Each modifier affects a different compiler, runtime, or serialization rule.

**Why it matters:** Misunderstanding declaration modifiers and resolution rules often creates subtle initialization, equality, or visibility bugs.

- `abstract`: declares an incomplete class or method.
- `synchronized`: acquires an intrinsic monitor for mutual exclusion and visibility.
- `volatile`: provides visibility and ordering for one field, not compound-operation atomicity.
- `transient`: excludes an instance field from default Java serialization.
- `native`: declares a method implemented outside Java through JNI.
- `strictfp`: historically enforced strict floating-point behavior; since Java 17, floating-point operations are always strict.

#### 3.8 `final` vs Immutability

**Explanation:** `final` prevents one reference from being reassigned, but it does not stop the referenced object from changing. Immutability requires an API and implementation that expose no state-changing path.

**Why it matters:** Misunderstanding declaration modifiers and resolution rules often creates subtle initialization, equality, or visibility bugs.

`final` prevents reassignment; it does not make a referenced object immutable:

```java
final List<String> names = new ArrayList<>();
names.add("Ali");              // allowed
// names = new ArrayList<>();  // not allowed
```

Correctly constructed `final` fields also have safe-publication guarantees, provided `this` does not escape during construction.

#### 3.9 Static Initialization

**Explanation:** Static fields and blocks initialize once for each defining class loader when the class is first actively used. Heavy or failure-prone work here can make the entire class unusable.

**Why it matters:** Misunderstanding declaration modifiers and resolution rules often creates subtle initialization, equality, or visibility bugs.

- A class initializes on first active use, such as construction, static method invocation, or access to a non-constant static field.
- Compile-time constants may be inlined and may not trigger initialization.
- If initialization throws, the first access receives `ExceptionInInitializerError`; later access commonly receives `NoClassDefFoundError`.
- Avoid heavy I/O, networking, or recoverable configuration work in static initializers.

#### 3.10 Imports and Name Resolution

**Explanation:** The compiler resolves simple names through the current package, explicit imports, wildcard imports, and `java.lang`. Ambiguous names require a fully qualified class name.

**Why it matters:** Misunderstanding declaration modifiers and resolution rules often creates subtle initialization, equality, or visibility bugs.

- Imports affect source-name resolution only; they do not load classes or add dependencies.
- Wildcard imports do not include subpackages.
- Use static imports sparingly, where they improve readability, such as test assertions.
- If imported classes share a simple name, use a fully qualified name for at least one.

#### 3.11 `this`, `super`, and Dispatch During Construction

**Explanation:** `this` selects the current object and `super` selects parent behavior. Constructor execution still uses dynamic dispatch, so overridable calls can observe child state before initialization is complete.

**Why it matters:** Misunderstanding declaration modifiers and resolution rules often creates subtle initialization, equality, or visibility bugs.

- `this(...)` delegates to another constructor in the same class.
- `super(...)` delegates to a parent constructor.
- One of them may be the first constructor statement; if neither is written, the compiler inserts a no-argument `super()`.
- Constructor delegation must eventually reach a superclass constructor.
- Dynamic dispatch still applies inside constructors, which is why invoking overridable methods there is unsafe.

#### 3.12 Access Across Packages

**Explanation:** Package and protected access depend on both package membership and inheritance. Protected access from an out-of-package subclass is narrower than unrestricted access to every parent instance.

**Why it matters:** Misunderstanding declaration modifiers and resolution rules often creates subtle initialization, equality, or visibility bugs.

`protected` has two distinct forms of access:

1. Any class in the same package can access the member.
2. A subclass in another package can access it through inheritance, subject to reference-type restrictions.

An out-of-package subclass cannot use an arbitrary parent instance to access the parent's protected member. Prefer protected methods over protected mutable fields.

#### 3.13 Annotation Basics

Annotations can target declarations or type uses and can have different retention:

- `SOURCE`: discarded by the compiler.
- `CLASS`: stored in class files but not necessarily visible at runtime.
- `RUNTIME`: available through reflection.

```java
@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
@interface NonEmpty {}

List<@NonEmpty String> names;
```

Annotations contain metadata, not executable behavior. A compiler, annotation processor, framework, or application code must interpret them.

#### 3.14 Initialization-on-Demand Holder

A nested static holder provides lazy, thread-safe initialization using class-initialization guarantees:

```java
final class ConfigRegistry {
  private ConfigRegistry() {}

  private static class Holder {
    static final ConfigRegistry INSTANCE = new ConfigRegistry();
  }

  static ConfigRegistry instance() {
    return Holder.INSTANCE;
  }
}
```

Use it only when one process-wide instance is genuinely appropriate. Dependency injection is usually clearer for application services.

#### 3.15 Chapter Review and Common Pitfalls

##### `this` and `super`

- `this` cannot be referenced before the superclass constructor has completed.
- A qualified expression such as `Outer.this` accesses an enclosing instance from an inner class.
- `InterfaceName.super.method()` can select a particular inherited default method.
- Constructor arguments are evaluated before the delegated constructor executes.

##### `final`

- A blank final instance field must be assigned on every constructor path.
- A blank static final field must be assigned during declaration or static initialization.
- A final method prevents overriding but can still call overridable methods.
- A final reference can point to mutable state; immutability is a property of the object's API and implementation.

##### `static`

- Static state is scoped to a defining class loader, so plugin or application-server environments may have multiple copies.
- Static mutable fields create hidden process-wide coupling and complicate parallel tests.
- Static methods are appropriate for pure operations and factories that require no replaceable dependency.
- Initialization cycles can expose default values before all static assignments complete.

##### Packages and access

- The unnamed/default package should not be used for maintainable applications and cannot be imported by named-package code.
- Package naming convention uses reversed domain ownership and lowercase segments.
- `public` exposes a type only if its enclosing type and module/package are also accessible.
- Package-private constructors can force callers through validated factories.

##### Wrappers and boxing

- Boxing in tight loops or streams can increase allocation and GC pressure; primitive streams avoid it.
- Wrapper constructors such as `new Integer(...)` are deprecated; use `valueOf` or autoboxing.
- `Boolean`, numeric wrappers, and `Character` are immutable.
- Parsing methods throw `NumberFormatException`; validate or translate at the input boundary.
- Unsigned helper methods exist for integer comparison, division, parsing, and formatting, but storage remains signed.

##### Modifiers and annotations

- `volatile` is suitable for independent state publication, flags, and immutable snapshots, not compound invariants.
- `synchronized` applies to a monitor and provides both exclusion and happens-before visibility.
- Repeatable annotations are represented through a compiler-generated container annotation.
- Inherited annotations work only for class annotations marked `@Inherited`, not methods or interfaces.
- Type-use annotations enable nullness and other static-analysis systems but require tooling to enforce meaning.

### 4. Memory and Strings

#### 4.1 Heap, Stack, and Metaspace

**Explanation:** The heap holds ordinary objects, each thread has execution stacks, and metaspace holds class metadata in native memory. These are conceptual/runtime areas with different ownership and failure modes.

**Why it matters:** Text and memory assumptions affect correctness, performance, internationalization, and resource safety.

- Stack:
    - Stores: Method calls, local variables (primitive + reference address), execution order LIFO
    - Each thread has own stack
    - Fast, small, auto cleanup when method ends
    - StackOverflowError if infinite recursion
  void method() {
    int x = 10; // x in stack
    Employee e; // reference e in stack
  } // x, e removed automatically
- Heap:
    - Stores: All objects (new), instance variables, arrays
    - Shared by all threads
    - Bigger, slower, cleaned by GC (Garbage Collector)
    - Divided into Young Gen (Eden + Survivor) + Old Gen
  Employee e = new Employee(); // e reference in stack, new Employee() object in heap
- OutOfMemoryError if heap full

- Method Area / Metaspace (part of heap in Java 8+):
    - Stores: Class metadata, static variables, String Pool

#### 4.2 String Pool

**Explanation:** The string pool reuses interned string values, especially literals, to reduce duplication. Pool reuse is an optimization and must never replace content comparison with `.equals()`.

**Why it matters:** Text and memory assumptions affect correctness, performance, internationalization, and resource safety.

- String Pool is special area inside heap (Method Area) to save memory.
String s1 = "Stitch"; // literal - goes to String Pool. If "Stitch" already exists, reuse it
String s2 = "Stitch"; // s1 and s2 point to SAME object in pool

String s3 = new String("Stitch"); // new - creates 2 objects: 1 in heap, 1 in pool if not exists
String s4 = new String("Stitch"); // new object in heap again - s3 != s4
- Why pool? String used a lot, reuse saves memory.

- intern() method:
String s3 = new String("Stitch").intern(); // force to use from pool
// Now s1 == s3 true

#### 4.3 String, StringBuilder, and StringBuffer

**Explanation:** `String` is immutable, while builders modify an internal sequence. Use `StringBuilder` for repeated single-threaded construction and `StringBuffer` only when its synchronized operations are specifically needed.

**Why it matters:** Text and memory assumptions affect correctness, performance, internationalization, and resource safety.

| Feature | String | StringBuilder | StringBuffer |
| --- | --- | --- | --- |
| Mutable? | NO - Immutable | YES - Mutable | YES - Mutable |
| Thread Safe? | Yes (immutable) | No - faster | Yes - synchronized, slower |
| When to use | Less changes | Single thread, many changes | Multi-thread |
| Memory | New object each change | Same object modified | Same object modified |

- Why String immutable? Security (password), String Pool possible, Thread safe, HashMap key.
// String - each + creates new object - BAD for loops
String s = "a";
s = s + "b"; // "a" discarded, new "ab" object. Old "a" stays till GC

// StringBuilder - good
StringBuilder sb = new StringBuilder("a");
sb.append("b"); // same object modified, no new object
sb.append("c").reverse().toString();

StringBuffer sbf = new StringBuffer("a");
sbf.append("b"); // synchronized - thread safe
- Performance: StringBuilder > StringBuffer > String (for concatenation in loop)
- Interview code:
String s = "a"; for(int i=0;i<1000;i++) s+= "b"; // creates 1000 objects - slow
StringBuilder sb = new StringBuilder(); for(int i=0;i<1000;i++) sb.append("b"); // 1 object

#### 4.4 `equals()` and `==`

**Explanation:** `==` compares primitive values or reference identity. `equals()` can implement logical value equality, and classes used in hash collections must implement a matching `hashCode()`.

**Why it matters:** Text and memory assumptions affect correctness, performance, internationalization, and resource safety.

- == : Checks address / reference equality. Are both pointing to same memory location?
- .equals() : Checks content equality. Method defined in Object class, String class overrides it to check characters.
String s1 = "Stitch";
String s2 = "Stitch";
String s3 = new String("Stitch");

System.out.println(s1 == s2); // true - same pool address
System.out.println(s1 == s3); // false - s3 in heap
System.out.println(s1.equals(s3)); // true - content same "Stitch"

int a = 10, b = 10;
System.out.println(a == b); // true - for primitives, == checks value

Employee e1 = new Employee("Ali");
Employee e2 = new Employee("Ali");
System.out.println(e1 == e2); // false - different objects
System.out.println(e1.equals(e2)); // false by default! Because Object's equals() also uses ==
// To make content check, you must OVERRIDE equals() in Employee class
- For custom class you must override equals() and hashCode():
@Override
public boolean equals(Object o) {
  Employee other = (Employee) o;
  return this.name.equals(other.name);
}
@Override
public int hashCode() { return name.hashCode(); }
- Rule for interview: 
    - For primitives always ==
    - For String content always .equals()
    - Never use == for String content - bug!

- Bonus: equals() contract - reflexive, symmetric, transitive, consistent.

#### 4.5 Unicode and String Operations

**Explanation:** Java strings contain UTF-16 code units, which are not always complete characters as users perceive them. Correct international text handling may require code-point, normalization, locale, or grapheme-aware operations.

**Why it matters:** Text and memory assumptions affect correctness, performance, internationalization, and resource safety.

`String.length()` counts UTF-16 code units, not user-perceived characters:

```java
String value = "A😀";
System.out.println(value.length()); // 3 code units
System.out.println(value.codePointCount(0, value.length())); // 2 code points
```

Use code-point APIs when processing arbitrary Unicode. Locale-sensitive transformations should specify a locale:

```java
String key = input.toLowerCase(Locale.ROOT);
```

Use `Locale.ROOT` for machine-readable identifiers and a user locale for display text.

#### 4.6 Concatenation and Formatting

**Explanation:** Concatenation joins text values, while formatting applies a declared representation. Repeated construction should avoid creating unnecessary intermediate strings, especially inside large loops.

**Why it matters:** Text and memory assumptions affect correctness, performance, internationalization, and resource safety.

- The compiler usually optimizes simple concatenation.
- Repeated concatenation inside loops should use `StringBuilder`.
- `String.join` and `Collectors.joining` handle delimiters cleanly.
- `String.formatted` and `Formatter` improve readability but are slower in hot paths.
- Never build SQL by concatenating values; formatting does not make SQL safe.

#### 4.7 Defensive String Handling

**Explanation:** Text arriving at a boundary may be null, blank, incorrectly normalized, too long, or sensitive. Handle those states explicitly rather than assuming every non-null string is usable.

**Why it matters:** Text and memory assumptions affect correctness, performance, internationalization, and resource safety.

- Use `isBlank()` when whitespace-only input is invalid.
- `strip()` is Unicode-aware; `trim()` removes only characters up to U+0020.
- Use `equalsIgnoreCase()` only when its locale-independent semantics fit the domain.
- Prefer short-lived `char[]` for secrets where APIs support it, though copies may still exist.

#### 4.8 Reference Strengths and Cleanup

**Explanation:** Reference strength affects whether a reference keeps an object reachable for garbage collection. It does not replace explicit resource closure for files, sockets, or native handles.

**Why it matters:** Text and memory assumptions affect correctness, performance, internationalization, and resource safety.

- Strong references keep objects alive normally.
- `SoftReference` may be cleared under memory pressure and is unsuitable for predictable cache policy.
- `WeakReference` does not prevent collection and can support carefully designed canonical mappings.
- `PhantomReference` plus `ReferenceQueue` supports post-mortem cleanup coordination.

Finalization is deprecated for removal and has unpredictable timing. Use try-with-resources; use `Cleaner` only as a last-resort safety net.

#### 4.9 String Internals and Compact Strings

Modern JDK implementations may store strings internally as Latin-1 or UTF-16 bytes using compact strings. This is an implementation detail, not an API guarantee.

- Never depend on a particular backing representation.
- `substring` in modern JDKs creates independent storage rather than retaining the original full array.
- String hash codes may be cached because strings are immutable.
- Interning unbounded dynamic input can retain large numbers of strings and should not be used as a general cache.

#### 4.10 Regular Expressions

**Explanation:** A regular expression describes a text pattern for searching, extraction, or shape validation. Complex expressions can be slow or unsafe on untrusted input, so patterns and input sizes need bounds.

**Why it matters:** Text and memory assumptions affect correctness, performance, internationalization, and resource safety.

```java
private static final Pattern EMAIL_SHAPE =
    Pattern.compile("[^@\\s]+@[^@\\s]+");

boolean matches = EMAIL_SHAPE.matcher(input).matches();
```

- Compile reused expressions once.
- `matches()` requires the entire input to match; `find()` searches for a matching region.
- Java string escaping and regex escaping both apply, so a regex backslash often appears as `\\`.
- Avoid catastrophic backtracking on attacker-controlled input; use bounded input, simpler expressions, or possessive quantifiers where appropriate.
- Regex validates syntax, not necessarily business meaning.

#### 4.11 Character Encoding

Text becomes bytes only through a charset:

```java
byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
String restored = new String(bytes, StandardCharsets.UTF_8);
```

- Never rely on the platform default for persisted or network data.
- UTF-8 is variable-length and widely interoperable.
- A byte-order mark may appear in some files and may need explicit handling.
- Configure malformed/unmappable input behavior with `CharsetDecoder` when silent replacement is unacceptable.

#### 4.12 String Comparison and Collation

**Explanation:** Lexicographic UTF-16 comparison is suitable for machine ordering, not every human language. Locale-aware display sorting uses collation rules and may require Unicode normalization.

**Why it matters:** Text and memory assumptions affect correctness, performance, internationalization, and resource safety.

- `String.compareTo` compares UTF-16 values lexicographically, not natural-language dictionary order.
- Use `Collator` for locale-sensitive user-facing sorting.
- Normalize Unicode when canonically equivalent sequences must compare consistently.

```java
String normalized = Normalizer.normalize(input, Normalizer.Form.NFC);
Collator collator = Collator.getInstance(userLocale);
names.sort(collator);
```

Normalization and case folding have domain-specific security implications; identifiers should follow a documented policy.

#### 4.13 Chapter Review and Common Pitfalls

##### Stack, heap, and metaspace

- The specification defines observable behavior, not that every local primitive physically lives on a native stack.
- JIT optimization may scalar-replace objects or keep values in registers.
- Each thread's stack size affects recursion depth and native-memory use.
- Static fields are associated with class metadata but referenced objects still reside in the heap.
- Metaspace uses native memory and grows according to loaded class metadata.

##### String pool and interning

- String literals and constant string expressions are interned.
- Runtime concatenation is generally not interned unless `intern()` is called.
- Interned strings use a global table associated with the runtime and can outlive short operations.
- Reference equality between strings is an implementation-sensitive optimization observation, never content-comparison logic.

##### StringBuilder and StringBuffer

- Builders grow internal capacity and may copy their backing storage; pre-size when the approximate output length is known and important.
- Builders are not value types and do not override `equals` for content comparison.
- `StringBuffer` synchronizes individual methods, but a multi-call sequence may still require external coordination.
- Compiler-generated concatenation may use `invokedynamic` on modern JDKs rather than a visible `StringBuilder`.

##### Equality

- `Objects.equals(a, b)` handles null safely.
- `Arrays.equals` compares one-dimensional contents; `Arrays.deepEquals` recursively handles nested arrays.
- Floating-point equality needs a domain-specific tolerance only for approximate computations; exact identifiers should not use floating point.
- Compare `BigDecimal` using the rule appropriate to the domain: numeric order or scale-sensitive representation.

##### Unicode and encoding

- A grapheme visible to a user may contain multiple code points, such as combining marks or emoji sequences.
- Code-point iteration still does not equal grapheme-cluster iteration.
- Never decode arbitrary byte chunks independently when a multibyte character may cross chunk boundaries; use a decoder preserving state.
- Mojibake occurs when bytes encoded with one charset are decoded with another.
- Validate text normalization policy for identifiers to reduce confusing visually equivalent forms.

##### Regular expressions

- Use `Pattern.quote` for literal text inserted into a regex and `Matcher.quoteReplacement` for replacement text.
- `Matcher` is mutable and not thread-safe; `Pattern` is immutable and safe to reuse.
- Named capture groups improve maintainability.
- Anchors such as `^` and `$` can be affected by multiline mode; `\A` and `\z` target absolute input boundaries.

##### References and cleanup

- Reachability includes strong paths from GC roots, not only local variables visible in source.
- Weak-reference processing is nondeterministic and unsuitable for correctness-sensitive cleanup.
- `Cleaner` actions must not strongly retain the object they clean.
- Native resources need explicit ownership, idempotent close behavior, and try-with-resources.

### 5. Exception Handling

#### 5.1 Exception Hierarchy

Abnormal event that breaks normal flow. Object of Throwable class.
Object -> Throwable -> 2 childs
                                      1. Exception -> Checked + Unchecked (RuntimeException)
                                      2. Error -> OutOfMemoryError, StackOverflowError - don't handle

#### 5.2 Checked and Unchecked Exceptions

**Explanation:** Checked exceptions force a compile-time handling decision; unchecked exceptions do not. The choice should communicate whether callers can reasonably recover, not merely how serious a failure sounds.

**Why it matters:** Failure behavior is part of an API contract and determines whether callers can recover without corrupting state.

| Checked (Compile-time) | Unchecked (Runtime) |
| --- | --- |
| Compiler forces you to handle | Compiler doesn't force |
| Outside program control | Programming mistake |
| Must use try-catch or throws else compile error | No need, but you can |
| Eg: IOException, SQLException, ClassNotFoundException, FileNotFoundException | Eg: NullPointerException, ArithmeticException, ArrayIndexOutOfBounds, NumberFormatException |
| Extends Exception directly | Extends RuntimeException |

// Checked - compile error if not handled
FileReader fr = new FileReader("file.txt"); // Must surround with try-catch

// Unchecked - compiles fine, fails at runtime
int a = 10/0; // ArithmeticException at runtime
String s = null; s.length(); // NullPointerException

#### 5.3 `try`, `catch`, `finally`, `throw`, and `throws`

try {
  int a = 10/0; // exception object created and thrown to catch
  System.out.println("This line never runs");
} 
catch (ArithmeticException e) { // catches specific
  System.out.println("Divide by zero: " + e.getMessage());
  e.printStackTrace(); // full log
} 
catch (Exception e) { // generic - must be LAST, else compile error
  System.out.println("Any exception");
}
finally { // ALWAYS runs - cleanup
  System.out.println("Finally always runs even if exception or return");
}

// Order matters - child to parent in catch blocks
- finally details:
    - Runs even if return in try. Only case it doesn't run: System.exit(0) or JVM crash or infinite loop.
    - Used to close resources: sc.close(), conn.close()
    - From Java 7: try-with-resources auto closes - no need finally.
  try (Scanner sc = new Scanner(System.in); FileReader fr = new FileReader("a.txt")) {
    // auto close
  }
- throw - Actually throwing exception manually
void checkAge(int age) {
  if(age < 18) {
    throw new ArithmeticException("Not eligible"); // create and throw
  }
}
- throws - Declaring that method may throw - warning to caller
void readFile() throws IOException { // caller must handle
  FileReader fr = new FileReader("file.txt");
}

void readFile2() throws IOException, SQLException { // multiple
}
- Difference: throw is inside method body to throw object, throws is in method signature to declare.

#### 5.4 Exception Flow Examples

// Case 1: Exception handled
try { riskyCode(); } 
catch(Exception e) { handle; } 
finally { cleanup; } // try->catch->finally

// Case 2: No exception
try { safeCode; } 
catch(Exception e) { } 
finally { cleanup; } // try->finally

// Case 3: Exception not caught
try { riskyCode(); } // throws NullPointer
catch(ArithmeticException e) { } // not matched
finally { cleanup; } // finally runs, then exception goes up

#### 5.5 Custom Exceptions

Why? Business logic - InsufficientBalanceException is more meaningful than Exception.
// Step 1: Create class extends Exception for checked, RuntimeException for unchecked
class InsufficientBalanceException extends Exception { // checked
  InsufficientBalanceException(String msg) {
    super(msg); // calls parent Exception constructor
  }
}

class InvalidAgeException extends RuntimeException { // unchecked
  InvalidAgeException(String msg) { super(msg); }
}

// Step 2: Use it
class Bank {
  double balance = 1000;
  void withdraw(double amt) throws InsufficientBalanceException {
    if(amt > balance) {
      throw new InsufficientBalanceException("Balance low: " + balance);
    }
    balance -= amt;
  }
}

// Step 3: Caller handles
public class Main {
  public static void main(String[] args) {
    Bank b = new Bank();
    try {
      b.withdraw(2000);
    } catch(InsufficientBalanceException e) {
      System.out.println(e.getMessage());
    }
  }
}
- Interview Qs asked in Stitch:
    1.  Can we have try without catch? Yes, try-finally allowed.
    2.  Can finally have return? Yes, but it overrides try's return - bad practice.
    3.  Difference Error vs Exception? Error cannot be recovered, Exception can.
    4.  Why checked exception not good for microservices? Forces handling everywhere - so Spring uses unchecked.

#### 5.6 Exception Hierarchy and Boundaries

**Explanation:** Exception types form a hierarchy that allows specific or broad handling. Translate failures where one abstraction ends and another begins, while preserving the original cause.

**Why it matters:** Failure behavior is part of an API contract and determines whether callers can recover without corrupting state.

```text
Throwable
  Error
  Exception
    RuntimeException
```

- `Error` represents serious JVM or environment failures applications generally should not recover from.
- Checked exceptions can document recoverable external failures but may make APIs noisy if overused.
- Runtime exceptions commonly represent programming errors, invalid state, or invalid arguments.
- Translate low-level exceptions at architectural boundaries while retaining the cause.

```java
try {
  return repository.load(id);
} catch (SQLException e) {
  throw new OrderDataAccessException("Unable to load order " + id, e);
}
```

#### 5.7 Multi-Catch and Suppressed Exceptions

**Explanation:** Multi-catch handles unrelated failures with one response. Suppressed exceptions retain secondary cleanup failures without replacing the primary exception raised by the operation.

**Why it matters:** Failure behavior is part of an API contract and determines whether callers can recover without corrupting state.

```java
try {
  readConfiguration();
} catch (IOException | ParseException e) {
  throw new ConfigurationException("Invalid configuration", e);
}
```

Multi-catch alternatives cannot be parent and child types. In try-with-resources, resources initialize left-to-right and close right-to-left. If the body and `close()` both fail, close failures are available through `getSuppressed()`.

#### 5.8 Exception Design Guidelines

**Explanation:** An exception API should preserve context, distinguish meaningful recovery cases, and avoid leaking secrets. Catch only where code can recover, translate, or add actionable information.

**Why it matters:** Failure behavior is part of an API contract and determines whether callers can recover without corrupting state.

- Catch only where code can recover, add context, or translate abstractions.
- Preserve causes when wrapping.
- Do not catch `Throwable` for ordinary application handling.
- Do not use exceptions for expected control flow.
- Log once at the boundary that handles the error.
- Never expose secrets or full sensitive payloads in exception messages.
- Assertions are disabled by default and must not validate public input or required business rules.

#### 5.9 Common Anti-Patterns

**Explanation:** Exception anti-patterns make failures invisible or misleading, such as empty catches, broad swallowing, duplicate logging, and success-shaped fallback values after unexpected errors.

**Why it matters:** Failure behavior is part of an API contract and determines whether callers can recover without corrupting state.

- Empty catch blocks hide failures.
- Broad catches may accidentally swallow cancellation or programming defects.
- Logging and rethrowing unchanged exceptions produces duplicate logs.
- Returning `null`, zero, or empty data after unexpected failure creates success-shaped errors.
- Throwing from `finally` can replace the original failure.
- Failing to restore interrupted status can prevent task cancellation.

#### 5.10 Designing Custom Exceptions

**Explanation:** A custom exception gives a domain failure a meaningful type and structured context. Keep the hierarchy small and choose checked or unchecked behavior based on caller recovery expectations.

**Why it matters:** Failure behavior is part of an API contract and determines whether callers can recover without corrupting state.

```java
class InsufficientFundsException extends RuntimeException {
  private final BigDecimal shortfall;

  InsufficientFundsException(BigDecimal shortfall) {
    super("Insufficient funds; shortfall=" + shortfall);
    this.shortfall = shortfall;
  }

  BigDecimal shortfall() {
    return shortfall;
  }
}
```

- Name exceptions after the failed condition or operation.
- Include structured context needed by handlers, but no secrets.
- Keep the hierarchy small and useful.
- Choose checked exceptions when callers can reasonably recover and the API benefits from forcing a decision.
- Document whether operations are safe to retry.

#### 5.11 Stack Traces

A stack trace captures the call path when the throwable is created. Creating many exceptions can therefore be expensive.

- The top frame is normally closest to the throw site.
- `Caused by` preserves lower-level failure context.
- `Suppressed` lists secondary failures.
- Async boundaries may split logical operations across different stacks; attach correlation context.
- Do not call `fillInStackTrace` or remove stack information merely to hide performance issues without measurement.

#### 5.12 Exception Transparency in Lambdas

Standard functional interfaces do not declare checked exceptions:

```java
// files.stream().map(Files::readString) does not compile because readString throws IOException.
```

Options include handling inside the lambda, extracting a method that translates the exception, using a loop, or defining a domain-specific throwing interface. Avoid generic "sneaky throw" helpers that hide the API contract.

#### 5.13 Failure Atomicity

An operation is failure-atomic when a failed attempt leaves the object or system in its previous valid state.

Techniques include:

- Validate before mutation.
- Compute a new immutable value, then replace the old value.
- Use database transactions.
- Write to a temporary file and atomically move it.
- Roll back partial external changes where possible.

Document partial-success behavior when atomicity cannot be guaranteed.

#### 5.14 Chapter Review and Common Pitfalls

##### Checked and unchecked exceptions

- Checked status is determined by inheritance, not by how severe or recoverable a failure is.
- `RuntimeException` and its subclasses are unchecked; other `Exception` subclasses are checked.
- Public APIs should document significant unchecked exceptions when callers can prevent them.
- Library code should avoid converting every failure into one generic unchecked exception.

##### Try, catch, and finally

- Catch blocks are tested from most specific to least specific; unreachable broader/smaller ordering is rejected.
- A return expression is evaluated before `finally`, but a return from `finally` replaces it and should be avoided.
- A try statement may have resources and `finally` without a catch.
- Since Java 9, an effectively final variable can be used directly as a try-with-resources resource.

##### Throwing and declaring

- `throw` requires a `Throwable` instance; `throws` declares potential propagation.
- Overridden methods may omit checked exceptions or declare narrower checked types.
- Generic methods can propagate a type-parameterized checked exception, though such APIs can be difficult to use.
- Exception translation should occur at a boundary where the lower-level type no longer has useful meaning.

##### Custom exceptions

- Include machine-readable fields when callers need structured recovery.
- Avoid enormous exception hierarchies that force callers to catch many equivalent types.
- Make exception objects effectively immutable.
- Messages should describe the failed operation and relevant safe identifiers.

##### Resource failure

- Closing multiple resources continues after one close fails; later failures become suppressed.
- If resource construction fails, already constructed earlier resources are closed.
- A resource's `close()` should be idempotent when practical.
- Do not reuse a resource after it has been transferred to an owner responsible for closing it.

##### Interruption and cancellation

- `InterruptedException` clears the interrupted flag when thrown.
- Restore it with `Thread.currentThread().interrupt()` when not rethrowing.
- Methods should document cancellation behavior and whether partial work remains.
- Do not treat interruption as an ordinary error to log repeatedly.

##### Failure atomicity and retries

- Retrying non-idempotent operations can duplicate side effects.
- A timeout does not prove the remote operation failed; its result may be unknown.
- Compensating actions are business operations and can themselves fail.
- Store durable operation identity when exactly-once business effect is required over at-least-once delivery.


## Part II: Collections, Functional Java, and Concurrency

Data structures, generic type safety, asynchronous execution, streams, and modern functional APIs.

### 6. Collections Framework

#### 6.1 List

**Explanation:** A list is an ordered sequence that allows duplicates and positional access. Implementation choice affects indexing, insertion cost, memory locality, synchronization, and supported operations.

**Why it matters:** The wrong collection can silently change ordering, uniqueness, complexity, concurrency, or memory behavior.

| Feature | ArrayList | LinkedList | Vector |
| --- | --- | --- | --- |
| Internal | Dynamic array Object[] | Doubly Linked List (Node prev, data, next) | Dynamic array - legacy |
| Get by index | O(1) - fast | O(n) - slow, must traverse | O(1) |
| Insert/delete middle | O(n) - shift | O(1) - just change pointers | O(n) |
| Thread safe? | No | No | Yes - synchronized, slow |
| When to use | 90% cases - read heavy | Many inserts in middle | Don't use - use ArrayList |

List<String> list = new ArrayList<>();
list.add("Stitch"); list.add(0,"Pay"); // add at index
list.get(0); list.set(0,"X"); list.remove(0);
Collections.sort(list);

// LinkedList can work as both List and Deque
LinkedList<String> ll = new LinkedList<>();
ll.addFirst("A"); ll.addLast("Z");

#### 6.2 Set

**Explanation:** A set stores unique elements according to equality or comparator semantics. Choose hash-based, insertion-ordered, or sorted behavior based on lookup and traversal requirements.

**Why it matters:** The wrong collection can silently change ordering, uniqueness, complexity, concurrency, or memory behavior.

| HashSet | LinkedHashSet | TreeSet |
| --- | --- | --- |
| HashMap internally | LinkedHashMap internally | TreeMap (Red-Black Tree) |
| No order | Insertion order maintained | Sorted order - natural sorting |
| O(1) add/search | O(1) | O(log n) |
| Allows 1 null | Allows 1 null | No null - throws NPE |
| Use when fast check | When you need order + uniqueness | When sorted set needed |

Set<String> set = new HashSet<>();
set.add("A"); set.add("A"); // second ignored -> size 1

Set<Integer> sorted = new TreeSet<>(); // sorted: 1,2,10
sorted.add(10); sorted.add(2); sorted.add(1);

LinkedHashSet maintains insertion order

#### 6.3 Map

**Explanation:** A map associates unique keys with values. Key equality, mutability, ordering, null policy, and concurrency behavior determine whether a particular implementation is safe.

**Why it matters:** The wrong collection can silently change ordering, uniqueness, complexity, concurrency, or memory behavior.

| HashMap | LinkedHashMap | TreeMap | ConcurrentHashMap |
| --- | --- | --- | --- |
| No order | Insertion order | Sorted by key | No order |
| 1 null key, many null values | 1 null key | No null key | No null key/value - throws NPE |
| Not thread safe - fast | Not thread safe | Not thread safe | Thread safe - segment lock, fast - use in multithreading |
| O(1) | O(1) | O(log n) | O(1) |

Map<Integer, String> map = new HashMap<>();
map.put(1, "Ali"); map.put(1, "Khan"); // overwrites - key 1 now Khan
map.get(1); // Khan
map.containsKey(1); map.containsValue("Ali");
for(Map.Entry<Integer,String> e : map.entrySet()){ e.getKey(); e.getValue(); }

Map<Integer,String> lmap = new LinkedHashMap<>(); // order same as you inserted

Map<Integer,String> tmap = new TreeMap<>(); // keys sorted 1,2,3

// HashMap working internal - MOST ASKED:
// put -> hashCode() of key -> bucket index -> if collision -> linked list -> Java 8+ if list >8 -> tree
// If you don't override hashCode() and equals() - map won't work for custom objects

Map<String,String> cmap = new ConcurrentHashMap<>(); // for thread safe without Hashtable
- HashMap vs HashTable: Hashtable legacy, synchronized slow, no null. HashMap new, fast, allows null.

#### 6.4 Queue, Deque, and Stack

// Queue - FIFO
Queue<Integer> q = new LinkedList<>(); // or PriorityQueue
q.offer(10); q.offer(20); // add
q.poll(); // remove head -> 10
q.peek(); // see head -> 20

Queue<Integer> pq = new PriorityQueue<>(); // min-heap - smallest first
pq.offer(10); pq.offer(2); pq.peek(); // 2

// Deque - Double ended - can add/remove both sides - faster than Stack
Deque<Integer> dq = new ArrayDeque<>();
dq.offerFirst(10); dq.offerLast(20);
dq.pollFirst(); dq.pollLast();

// Stack - LIFO - legacy, use Deque instead
Stack<Integer> st = new Stack<>();
st.push(10); st.push(20);
st.pop(); // 20
st.peek();

// For interview: Use Deque for stack
Deque<Integer> stack = new ArrayDeque<>();
stack.push(10); stack.pop();

#### 6.5 Comparable and Comparator

class Employee implements Comparable<Employee> { // Comparable - natural sorting, 1 way
  int id; String name;
  @Override
  public int compareTo(Employee other) {
    return this.id - other.id; // sort by id - ascending
    // return this.name.compareTo(other.name); // by name
  }
}
Collections.sort(empList); // uses compareTo

// Comparator - multiple ways, external logic
class NameComparator implements Comparator<Employee> {
  public int compare(Employee e1, Employee e2){ return e1.name.compareTo(e2.name); }
}
class SalaryComparator implements Comparator<Employee> {
  public int compare(Employee e1, Employee e2){ return e1.salary - e2.salary; }
}
Collections.sort(empList, new NameComparator());
// Java 8 lambda
empList.sort((e1,e2) -> e1.id - e2.id);
empList.sort(Comparator.comparing(e -> e.name));
| Comparable | Comparator |
| --- | --- |
| In same class - implements Comparable | Separate class |
| compareTo() 1 param | compare() 2 params |
| Natural ordering - single logic | Multiple logics |
| java.lang package | java.util package |

#### 6.6 Iteration and Concurrent Modification

List<String> list = new ArrayList<>();
list.add("A"); list.add("B");

// 1. Iterator
Iterator<String> it = list.iterator();
while(it.hasNext()){ String s = it.next(); if(s.equals("A")) it.remove(); } // safe remove

// 2. ListIterator - bidirectional - only for List
ListIterator<String> lit = list.listIterator();
lit.next(); lit.previous(); lit.add("C"); lit.set("D");

// 3. For-each
for(String s : list){ System.out.println(s); }

// 4. forEach + lambda Java 8
list.forEach(s -> System.out.println(s));
list.forEach(System.out::println);
- fail-fast: Throws ConcurrentModificationException if you modify while iterating. Uses original collection. Eg: ArrayList, HashMap, HashSet iterators - all collection iterators.
for(String s: list){ list.add("X"); } // throws exception
- fail-safe: Works on copy of collection, no exception. Eg: ConcurrentHashMap, CopyOnWriteArrayList iterators.
ConcurrentHashMap<Integer,String> cmap = new ConcurrentHashMap<>();
// iterator won't throw even if you modify
Interview Must-Know:
1. Why ArrayList default size 10? Grows 1.5x.
2. HashMap internal working - hashcode, equals, bucket, treeify.
3. When to use which Map/List - they give scenario.
4. How to make ArrayList thread safe? Collections.synchronizedList(list) or CopyOnWriteArrayList

#### 6.7 Choosing a Collection

**Explanation:** Collection selection begins with required semantics—ordering, uniqueness, lookup, sorting, queueing, or concurrency—then considers complexity and memory behavior.

**Why it matters:** The wrong collection can silently change ordering, uniqueness, complexity, concurrency, or memory behavior.

| Requirement | Typical choice |
| --- | --- |
| Indexed access and append | `ArrayList` |
| Unique values | `HashSet` |
| Unique values in insertion order | `LinkedHashSet` |
| Sorted unique values | `TreeSet` |
| General key/value lookup | `HashMap` |
| Predictable map iteration order | `LinkedHashMap` |
| Sorted keys and range queries | `TreeMap` |
| FIFO queue or stack | `ArrayDeque` |
| Priority-based removal | `PriorityQueue` |
| Concurrent key/value access | `ConcurrentHashMap` |
| Read-heavy, rarely modified list | `CopyOnWriteArrayList` |

`LinkedList` is rarely the best default: indexed access is O(n), nodes add allocation overhead, and traversal has poor memory locality.

#### 6.8 Complexity Guide

**Explanation:** Big-O notation describes how operation cost grows with data size. It is a comparison tool, not a complete performance prediction, because constants, locality, allocation, and contention also matter.

**Why it matters:** The wrong collection can silently change ordering, uniqueness, complexity, concurrency, or memory behavior.

- `ArrayList.get`: O(1); middle insertion/removal: O(n).
- `HashMap.get/put`: expected O(1), depending on hashing and resizing.
- `TreeMap.get/put`: O(log n).
- `HashSet.contains`: expected O(1).
- `TreeSet.contains`: O(log n).
- `PriorityQueue.offer/poll`: O(log n); `peek`: O(1).

Big-O does not capture allocation, cache locality, hash quality, concurrency, or small-data constants. Measure critical paths.

#### 6.9 Immutable and Unmodifiable Collections

**Explanation:** An immutable collection cannot change, while an unmodifiable view can still reflect changes made through its backing collection. Choose a snapshot when isolation is required.

**Why it matters:** The wrong collection can silently change ordering, uniqueness, complexity, concurrency, or memory behavior.

```java
List<String> fixed = List.of("A", "B");
List<String> snapshot = List.copyOf(existing);
List<String> view = Collections.unmodifiableList(existing);
```

- Factory collections reject mutation and generally reject null.
- `copyOf` creates an immutable snapshot unless the source is already suitable.
- `unmodifiableList` is a view; backing-list changes remain visible.
- `Arrays.asList` is fixed-size but permits replacement with `set`.

#### 6.10 Map Operations and Contracts

**Explanation:** Modern map operations can atomically initialize, merge, or update values. Their mapping functions must follow the map's null, reentrancy, and concurrency contracts.

**Why it matters:** The wrong collection can silently change ordering, uniqueness, complexity, concurrency, or memory behavior.

```java
counts.merge(word, 1, Integer::sum);
users.computeIfAbsent(teamId, ignored -> new ArrayList<>()).add(user);
cache.computeIfPresent(key, (key, value) -> refresh(value));
```

- Mapping functions should be short and avoid recursively modifying the same map.
- `HashMap` permits one null key and null values; `ConcurrentHashMap` permits neither.
- Comparator subtraction can overflow; use `Integer.compare`.
- Mutating fields used by hashing or ordering while an element is stored can make it logically unreachable.

#### 6.11 HashMap Internal Behavior

A `HashMap` spreads a key's hash to choose a bucket. Within a bucket, it uses equality to find the exact key.

1. Compute `hashCode()` and spread high bits.
2. Select a bucket from the current table size.
3. Compare hash, then `equals()`.
4. Insert, replace, or return the matching entry.

When size exceeds `capacity * loadFactor`, the table resizes. Since Java 8, sufficiently large, heavily collided buckets may become balanced trees when table and bucket thresholds are met. This protects worst-case lookup behavior but does not excuse poor hash functions.

#### 6.12 Views and Backing Collections

Many collection-returning methods create views:

```java
Map<String, Integer> scores = new HashMap<>();
Set<String> keys = scores.keySet();
keys.remove("Ali"); // removes the mapping from scores
```

- `keySet`, `values`, and `entrySet` are backed by the map.
- `subList` is backed by its source list and can become invalid after unrelated structural changes.
- `NavigableMap.subMap` and `headMap` expose bounded views.
- Copy when an independent snapshot is required.

#### 6.13 Navigable Collections

**Explanation:** Navigable sets and maps provide nearest-key and bounded-range operations over sorted data. Their comparator defines both order and effective key uniqueness.

**Why it matters:** The wrong collection can silently change ordering, uniqueness, complexity, concurrency, or memory behavior.

`NavigableSet` and `NavigableMap` support nearest-match and range operations:

- `lower`: greatest element strictly less than key.
- `floor`: greatest element less than or equal.
- `ceiling`: least element greater than or equal.
- `higher`: least element strictly greater.
- `pollFirst` / `pollLast`: retrieve and remove an endpoint.

These are useful for scheduling, time ranges, leaderboards, and version lookup.

#### 6.14 Spliterator

A `Spliterator` traverses and partitions elements for sequential or parallel processing. Characteristics such as `ORDERED`, `DISTINCT`, `SORTED`, `SIZED`, `IMMUTABLE`, and `CONCURRENT` help stream implementations optimize safely.

Custom spliterators must partition without losing or duplicating elements and report only truthful characteristics.

#### 6.15 Concurrent Collection Semantics

**Explanation:** Concurrent collections permit defined operations during multithreaded access, but they do not make an arbitrary sequence of calls atomic. Use compound atomic methods or external coordination.

**Why it matters:** The wrong collection can silently change ordering, uniqueness, complexity, concurrency, or memory behavior.

- `ConcurrentHashMap` supports concurrent reads and updates without one global map lock.
- Its iterators are weakly consistent: they do not throw `ConcurrentModificationException` and may reflect some concurrent changes.
- `CopyOnWriteArrayList` makes every mutation copy the backing array; excellent for tiny, read-mostly listener lists, poor for write-heavy or large lists.
- `ConcurrentLinkedQueue` is non-blocking and unbounded.
- `BlockingQueue` can enforce producer backpressure when bounded.

Compound actions still need atomic methods such as `compute`, `merge`, `putIfAbsent`, or external coordination.

#### 6.16 Chapter Review and Common Pitfalls

##### List implementations

- `ArrayList` grows geometrically; capacity is an implementation detail and should not be treated as a contract.
- `ensureCapacity` can reduce resizing when a large final size is known.
- `LinkedList` implements both `List` and `Deque`, but its per-node overhead and traversal cost make it a specialized choice.
- `Vector` and `Stack` are legacy synchronized collections; prefer modern alternatives.

##### Sets and maps

- A set delegates uniqueness to equality or comparator semantics.
- `LinkedHashMap` can maintain insertion order or access order, enabling simple LRU-like policies through `removeEldestEntry`.
- `IdentityHashMap` compares keys by identity and is suitable only for identity-based algorithms such as graph traversal.
- `WeakHashMap` weakly references keys, but values can accidentally retain their own keys.
- `EnumMap` requires one enum key type and stores entries compactly.

##### Sorted collections

- A comparator should be antisymmetric, transitive, and consistent across repeated calls.
- A comparator inconsistent with `equals` can cause a sorted set to treat unequal objects as duplicates.
- Never use mutable ordering fields while an element remains in a tree-based collection.
- Range views enforce their bounds and reject out-of-range insertions.

##### Queues and deques

- Queue pairs differ in failure behavior: `add/remove/element` throw, while `offer/poll/peek` return a special result.
- Since null is often used to mean “no element,” most queue implementations disallow null.
- `PriorityQueue` iteration is not sorted; repeatedly `poll` to observe priority order.
- Equal-priority elements have no guaranteed FIFO order unless the comparator includes a sequence tie-breaker.

##### Iteration

- Fail-fast behavior is best-effort bug detection, not a concurrency guarantee.
- `Iterator.remove` may be unsupported for immutable or specialized collections.
- Structural modification usually means size or topology change, not replacing one existing list value.
- Prefer collection bulk operations such as `removeIf`, `replaceAll`, and `sort` when they express the intent.

##### Hashing and capacity

- Good hash distribution reduces collisions but equality still determines key identity.
- Resizing is expensive because the table structure changes; initial capacity can help known bulk loads.
- Load factor trades memory for collision frequency; the default is appropriate for most uses.
- Hash-based iteration order is unspecified and can change across JDKs, capacity changes, or process runs.

##### Concurrent collections

- Thread-safe collection methods do not make multi-step workflows atomic.
- `Collections.synchronizedList` requires synchronizing on the returned list during iteration.
- `ConcurrentHashMap.size()` under concurrent updates is an observation, not a transactional snapshot.
- Bounded blocking queues are useful load-shedding boundaries; unbounded queues merely defer overload.

##### Collection API design

- Accept the most general useful interface, such as `Collection` or `Iterable`.
- Return an immutable snapshot when callers must not observe future mutation.
- Document iteration order, null policy, mutability, thread safety, and ownership.
- Avoid returning `Stream` from APIs when the lifecycle of an underlying I/O resource would be unclear.

### 7. Generics

#### 7.1 Generic Classes

class Box<T> { // T = Type placeholder
  T value;
  Box(T value){ this.value = value; }
  T getValue(){ return value; }
  void setValue(T value){ this.value = value; }
}

Box<String> b1 = new Box<>("Hello");
Box<Integer> b2 = new Box<>(100);
Box<Employee> b3 = new Box<>(new Employee());

// Multiple type params
class Pair<K,V> {
  K key; V value;
  Pair(K k, V v){ this.key=k; this.value=v; }
}
Pair<Integer, String> p = new Pair<>(1, "Stitch");
Common naming: T - Type, E - Element, K - Key, V - Value, N - Number

#### 7.2 Generic Methods

class Util {
  // Method with its own generic type <T>
  public static <T> void printArray(T[] arr){
    for(T t: arr) System.out.println(t);
  }
  public static <T> T getFirst(T[] arr){ return arr[0]; }
}

Integer[] intArr = {1,2,3};
String[] strArr = {"A","B"};
Util.<Integer>printArray(intArr); // explicit
Util.printArray(strArr); // type inference - auto detects String

#### 7.3 Bounded Types and Wildcards

1. Upper Bounded ? extends Type - You can READ but not write (except null)
// T must be Number or child of Number (Integer, Double etc)
class NumberBox<T extends Number> { // bounded class
  T num;
}

NumberBox<Integer> ok = new NumberBox<>(); // Integer extends Number - ok
// NumberBox<String> error - String not extends Number

// In method - wildcard extends
void sum(List<? extends Number> list){ // accepts List<Integer>, List<Double>, List<Number>
  Number n = list.get(0); // can read as Number - safe
  // list.add(10); // ERROR - cannot add, because you don't know exact type - could be Double list
  // list.add(null) allowed
  double total=0; for(Number x: list) total+= x.doubleValue();
}
2. Lower Bounded ? super Type - You can WRITE but read as Object
void addNumbers(List<? super Integer> list){ // accepts List<Integer>, List<Number>, List<Object>
  list.add(10); // can add Integer - safe, Integer is child of all these
  list.add(20);
  // Integer i = list.get(0); // ERROR - could be Object list
  Object o = list.get(0); // only Object safe
}
3. Unbounded ? - Unknown type
void print(List<?> list){ // accepts any type
  Object o = list.get(0); // can only read as Object
  // list.add("A"); // ERROR - cannot add
  for(Object x: list) System.out.println(x);
}
PECS Rule - Remember for interview: Producer Extends, Consumer Super
- If list PRODUCES data (you read from it) -> use extends
- If list CONSUMES data (you write to it) -> use super

#### 7.4 Type Erasure

**Explanation:** Java implements most generics by erasing type arguments after compile-time checks. This preserves old binary compatibility but prevents operations that need the exact runtime type parameter.

**Why it matters:** Accurate generic types move errors from runtime to compilation and make APIs usable without unsafe casts.

- Java keeps generics only at compile time for type check. At runtime, JVM removes generic info and replaces with Object / bounded type.
// Your code
ArrayList<String> list = new ArrayList<>();
// After compilation - type erased
ArrayList list = new ArrayList(); // String removed -> Object
- Because of erasure:
// These are same after erasure - cannot overload
void method(List<String> list){}
void method(List<Integer> list){} // COMPILE ERROR - same after erasure

// You cannot create generic array
T[] arr = new T[10]; // ERROR
// instanceof with generics not allowed
if(list instanceof ArrayList<String>) // ERROR

#### 7.5 Generic Interfaces

interface Repository<T> {
  void save(T t);
  T findById(int id);
}
class EmployeeRepo implements Repository<Employee> {
  public void save(Employee e){}
  public Employee findById(int id){ return new Employee(); }
}

#### 7.6 Common Generic Pitfalls

List<Object>!= List<String> // List<Object> cannot hold List<String>
List<?> list = new ArrayList<String>(); // OK - wildcard allows
List<Object> list2 = new ArrayList<String>(); // ERROR

// Why generics invariant?
List<Integer> intList = new ArrayList<>();
// List<Number> numList = intList; // ERROR - if allowed, you could add Double to Integer list - unsafe

// But arrays are covariant
Integer[] intArr = new Integer[10];
Number[] numArr = intArr; // OK in arrays - but can cause ArrayStoreException at runtime

##### Quick Summary

- Always use generics - type safe, no casting
- Use extends when you need to read numbers, super when you need to add
- All generic info erased at runtime

#### 7.7 Wildcard Capture

A helper can capture an unknown wildcard:

```java
static void reverse(List<?> list) {
  reverseCaptured(list);
}

private static <T> void reverseCaptured(List<T> list) {
  Collections.reverse(list);
}
```

Use a type parameter when arguments or return values must share a type. Use a wildcard when the exact type is irrelevant.

#### 7.8 Generic API Design

**Explanation:** A generic API should express relationships among input and output types while accepting the broadest safe callers. Wildcards are most useful at boundaries; named parameters connect exact types.

**Why it matters:** Accurate generic types move errors from runtime to compilation and make APIs usable without unsafe casts.

```java
static <T> void copy(
    List<? super T> destination,
    List<? extends T> source) {
  for (T item : source) destination.add(item);
}
```

- Accept the least restrictive safe input type.
- Return useful concrete generic information instead of wildcard-heavy output.
- Avoid raw types; use `List<?>` when the element type is unknown.
- Do not expose implementation-specific collection types unless their behavior is part of the contract.

#### 7.9 Heap Pollution and Varargs

Heap pollution occurs when a parameterized variable refers to an incompatible value, often through raw types, unchecked casts, or generic varargs.

```java
@SafeVarargs
static <T> List<T> combine(List<? extends T>... lists) {
  List<T> result = new ArrayList<>();
  for (List<? extends T> list : lists) result.addAll(list);
  return result;
}
```

Use `@SafeVarargs` only when the method performs no unsafe operation on the varargs array.

#### 7.10 Reifiable Types

Reifiable types retain enough runtime information for operations such as `instanceof`. Examples include primitives, non-generic classes, raw types, and unbounded wildcard types:

```java
if (value instanceof List<?> list) {
  System.out.println(list.size());
}
```

`List<String>` is non-reifiable because its element type is erased.

#### 7.11 Recursive Bounds

Recursive bounds express relationships involving the type itself:

```java
static <T extends Comparable<? super T>> T max(List<? extends T> values) {
  return values.stream().max(Comparator.naturalOrder()).orElseThrow();
}
```

`Comparable<? super T>` permits comparison logic inherited from a supertype and is more flexible than `Comparable<T>`.

#### 7.12 Multiple Bounds

A type parameter can require one class and multiple interfaces:

```java
static <T extends Number & Comparable<T> & Serializable>
T choose(T left, T right) {
  return left.compareTo(right) >= 0 ? left : right;
}
```

The class bound, if any, must appear first. Erasure uses the leftmost bound, which can affect generated casts and binary compatibility.

#### 7.13 Bridge Methods

Type erasure can change an overriding method's erased signature. The compiler creates a synthetic bridge method to preserve polymorphism:

```java
class StringBox implements Comparable<StringBox> {
  public int compareTo(StringBox other) {
    return 0;
  }
}
```

Reflection and stack traces may expose bridge methods. `Method.isBridge()` identifies them.

#### 7.14 Generic Factories

Static factories can infer type arguments more cleanly than constructors:

```java
static <K, V> Map<K, V> newMap() {
  return new HashMap<>();
}

Map<String, Integer> counts = newMap();
```

The diamond operator can infer constructor types from the target context. Anonymous classes have supported the diamond operator since Java 9, with restrictions based on inferred non-denotable types.

#### 7.15 Variance Summary

**Explanation:** Variance describes how subtype relationships transfer through generic containers. Java generics are invariant, with `extends` and `super` wildcards providing controlled read or write views.

**Why it matters:** Accurate generic types move errors from runtime to compilation and make APIs usable without unsafe casts.

- Java generic types are invariant: `List<Integer>` is not a subtype of `List<Number>`.
- `? extends Number` provides a covariant read view.
- `? super Integer` provides a contravariant write view.
- Arrays are covariant and reified, shifting some errors from compile time to runtime.
- Function inputs often use `? super T`; function outputs often use `? extends R`.

Example from `Stream.map` conceptually: `Function<? super T, ? extends R>`.

#### 7.16 Chapter Review and Common Pitfalls

##### Generic classes and methods

- Type parameters belong either to a class/interface or independently to a method/constructor.
- A static member cannot use its class's type parameter because it belongs to the raw class, not an instance.
- Type inference uses arguments, target types, bounds, and invocation context.
- Explicit type witnesses such as `Collections.<String>emptyList()` can resolve rare inference ambiguities.

##### Bounds and PECS

- Upper bounds define capabilities available when reading a type.
- Lower-bounded wildcards are particularly useful for callbacks and destination collections.
- PECS is a guideline, not a substitute for understanding both reads and writes.
- If a parameter both consumes and produces the exact same type, a named type parameter is often clearer than a wildcard.

##### Erasure

- Generic type arguments generally do not exist as ordinary runtime class objects.
- `new T()`, `T.class`, and `new T[]` are unavailable because runtime representation is unknown.
- Reflection can inspect generic signatures recorded in class metadata, but actual runtime values may still violate them due to unchecked operations.
- Erasure preserves migration compatibility with pre-generics Java but limits reification.

##### Raw types and unchecked operations

- Raw types disable part of the compiler's type safety and should be confined to legacy boundaries.
- An unchecked warning identifies a point where the compiler cannot prove safety.
- Suppress warnings on the narrowest declaration and explain the invariant that makes the operation safe.
- Validate elements when adapting genuinely untyped external data.

##### Wildcards

- `List<?>` means a list of one unknown type, not a list whose elements can independently be any type.
- The only universally safe value to add through most unbounded wildcard references is null.
- Capture conversion gives the unknown type a temporary compiler identity.
- Avoid wildcard return types when they force every caller to perform capture work.

##### Generic varargs

- Varargs are arrays, and arrays are reified while generic element types are erased.
- The generated array can be polluted through an `Object[]` alias.
- A safe generic-varargs method neither stores incompatible values nor exposes the array.
- Prefer a collection parameter where varargs add little usability.

##### Recursive and multiple bounds

- F-bounded polymorphism expresses “a type comparable to itself” and fluent self types.
- Fluent base classes using unchecked self casts require a carefully sealed or documented hierarchy.
- Intersection types can appear through inference even when they cannot be written as ordinary variable types.
- Bound order affects erasure and therefore binary signatures.

##### API compatibility

- Changing generic signatures can remain binary compatible while breaking source compilation.
- Adding a bound can reject previously valid callers.
- Returning a narrower generic type may expose implementation decisions.
- Published libraries should test source, binary, and behavioral compatibility separately.

### 8. Multithreading and Concurrency

#### 8.1 Threads, Runnable, and Lifecycle

**Explanation:** A thread is an execution path; a task is the work it performs. Separating tasks from thread creation allows executors to control scheduling, reuse, limits, and shutdown.

**Why it matters:** Concurrency defects may disappear during testing and reappear under load, so guarantees must come from design rather than timing.

Thread = lightweight process, shares memory.
// Way 1: extends Thread - not recommended, you cannot extend other class
class MyThread extends Thread {
  public void run(){ System.out.println("Thread: " + Thread.currentThread().getName()); }
}
MyThread t = new MyThread(); t.start(); // start() creates new thread, calls run(). Don't call run() directly - it runs in same thread.

// Way 2: implements Runnable - BEST - composition
class MyTask implements Runnable {
  public void run(){ System.out.println("Runnable"); }
}
Thread t = new Thread(new MyTask()); t.start();

// Way 3: Java 8 lambda
Runnable r = () -> System.out.println("Lambda thread");
new Thread(r).start();

// Way 4: Callable - can return value + throw checked exception
Callable<Integer> c = () -> { return 10+20; };
Thread Lifecycle - 5 states:
NEW -> RUNNABLE -> RUNNING -> BLOCKED/WAITING/TIMED_WAITING -> TERMINATED

1. NEW: Thread t = new Thread();
2. RUNNABLE: t.start() - ready to run, waiting for CPU
3. RUNNING: CPU scheduled, run() executing
4. BLOCKED: waiting for lock (synchronized)
   WAITING: wait() / join() - waits forever until notify()
   TIMED_WAITING: sleep(1000), wait(1000), join(1000)
5. TERMINATED: run() completed
Methods:
t.start(); // start
Thread.sleep(1000); // pause current thread - TIMED_WAITING
t.join(); // wait till t finishes - main waits
Thread.yield(); // hint to scheduler - give chance to other thread
t.setPriority(1-10); // 10 max
t.setDaemon(true); // background thread - JVM exits if only daemon left - GC thread is daemon

#### 8.2 `synchronized` and `volatile`

**Explanation:** `synchronized` provides mutual exclusion and visibility around a monitor. `volatile` provides visibility and ordering for one field but does not make compound updates atomic.

**Why it matters:** Concurrency defects may disappear during testing and reappear under load, so guarantees must come from design rather than timing.

- Race Condition: 2 threads access same variable same time -> wrong result.
int count=0;
count++; // not atomic - 3 steps: read, increment, write. If 2 threads do together, lost update
- synchronized: Lock - only 1 thread enters at a time. Uses monitor lock of object.
// 1. Synchronized method - locks on this object
public synchronized void increment(){ count++; }

// 2. Synchronized block - better - lock only needed part, faster
public void increment(){
  synchronized(this){ count++; }
}

// 3. Static synchronized - locks on Class object, not instance
public static synchronized void method(){}

// 4. Lock on custom object
private final Object lock = new Object();
public void method(){ synchronized(lock){ } }

// How it works: Every object has monitor. When thread enters synchronized, it acquires lock, other threads go BLOCKED
- volatile: Visibility guarantee, not atomicity.
// Problem: Thread caches variable in CPU cache, other thread may not see update
boolean flag = false; // Thread1 writes flag=true, Thread2 may still see false from cache

volatile boolean flag = false; // now always reads from main memory, visible to all threads - fixes visibility
// BUT volatile does NOT make count++ atomic - for atomic use AtomicInteger or synchronized
| synchronized | volatile |
| --- | --- |
| Locks - mutual exclusion | No lock - only visibility |
| Makes operation atomic | Does NOT make atomic |
| Blocks threads | Doesn't block |
| Use for compound actions | Use for flags - volatile boolean stop |

#### 8.3 ExecutorService, Future, and CompletableFuture

// ExecutorService - thread pool - reuse threads

// 1. Single thread
ExecutorService ex = Executors.newSingleThreadExecutor();

// 2. Fixed pool - n threads
ExecutorService ex = Executors.newFixedThreadPool(5); // 5 threads reused

// 3. Cached pool - creates new if needed, reuse idle
ExecutorService ex = Executors.newCachedThreadPool();

// 4. Scheduled - for cron jobs
ScheduledExecutorService sched = Executors.newScheduledThreadPool(2);
sched.schedule(() -> System.out.println("After 5 sec"), 5, TimeUnit.SECONDS);
sched.scheduleAtFixedRate(() -> {}, 0, 1, TimeUnit.SECONDS);

ex.submit(() -> System.out.println("Task")); // submit Runnable
Future<Integer> f = ex.submit(() -> 10+20); // submit Callable
Integer result = f.get(); // blocking call - waits for result
f.isDone(); f.cancel(true);

ex.shutdown(); // stop accepting new, completes running tasks
ex.shutdownNow(); // tries to stop running tasks
ex.awaitTermination(5, TimeUnit.SECONDS);
- Future problem: get() blocks - cannot chain.

- CompletableFuture - Java 8 - non-blocking, async chain - MOST ASKED IN STITCH
CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> {
  // runs in ForkJoinPool
  return "Stitch";
});

cf.thenApply(s -> s + " Pay") // transform
  .thenApply(String::toUpperCase)
  .thenAccept(System.out::println) // consume
  .thenRun(() -> System.out.println("Done"));

CompletableFuture<Integer> cf1 = CompletableFuture.supplyAsync(() -> 10);
CompletableFuture<Integer> cf2 = CompletableFuture.supplyAsync(() -> 20);
cf1.thenCombine(cf2, (a,b) -> a+b) // combine 2 futures
   .thenAccept(System.out::println); // 30

// Exception handling
cf.exceptionally(ex -> { System.out.println(ex); return "default"; });

// All of, any of
CompletableFuture.allOf(cf1, cf2).join(); // wait all
CompletableFuture.anyOf(cf1, cf2).join(); // wait any

#### 8.4 Concurrent Utilities and Locks

**Explanation:** The concurrency package offers locks, atomics, queues, latches, semaphores, and maps with explicit guarantees. Prefer the highest-level utility that directly models the coordination need.

**Why it matters:** Concurrency defects may disappear during testing and reappear under load, so guarantees must come from design rather than timing.

- java.util.concurrent - thread safe collections + utils:
ConcurrentHashMap - instead of synchronized HashMap
CopyOnWriteArrayList - write creates copy - good for many reads
BlockingQueue - queue that blocks if empty/full - used in producer-consumer
BlockingQueue<Integer> q = new ArrayBlockingQueue<>(10);
q.put(10); // blocks if full
q.take(); // blocks if empty
- Locks - More powerful than synchronized
Lock lock = new ReentrantLock();

lock.lock();
try {
  count++;
} finally {
  lock.unlock(); // must unlock in finally else deadlock
}

// Try lock - avoids deadlock
if(lock.tryLock(1, TimeUnit.SECONDS)){
  try { } finally { lock.unlock(); }
}

// ReentrantLock vs synchronized:
// 1. Can tryLock with timeout
// 2. Can have fairness - new ReentrantLock(true) - threads get lock in order
// 3. Can have multiple conditions

ReadWriteLock rwLock = new ReentrantReadWriteLock();
rwLock.readLock().lock(); // many threads can read together
rwLock.writeLock().lock(); // only one write, blocks reads

AtomicInteger atomicCount = new AtomicInteger(0);
atomicCount.incrementAndGet(); // atomic - uses CAS - no lock needed - faster

#### 8.5 Race Conditions, Deadlock, and Liveness

**Explanation:** A race makes correctness depend on timing; deadlock creates a cycle of waiting; starvation and livelock also prevent progress. Correctness requires both safe state and guaranteed progress assumptions.

**Why it matters:** Concurrency defects may disappear during testing and reappear under load, so guarantees must come from design rather than timing.

- Deadlock: 2 threads wait for each other's lock forever.
// Thread1: lock A -> needs B, Thread2: lock B -> needs A -> DEADLOCK
synchronized(lockA){ 
  Thread.sleep(100);
  synchronized(lockB){ } // waits for B
}
// Thread2 opposite
synchronized(lockB){
  synchronized(lockA){ }
}

// 4 conditions for deadlock: Mutual Exclusion, Hold and Wait, No Preemption, Circular Wait
// Fix: Always acquire locks in same order globally, use tryLock with timeout, avoid nested locks
- How to detect deadlock? jstack thread dump, shows Found deadlock

- Race Condition: Result depends on timing of threads.

- How to avoid:
    1. Immutable objects - no state change - inherently thread safe (String)
    2. synchronized / Lock
    3. Atomic classes - AtomicInteger, AtomicReference
    4. ThreadLocal - each thread has own copy
  ThreadLocal<Integer> tl = ThreadLocal.withInitial(() -> 0);
  tl.set(10); tl.get();
5. Use concurrent collections

Interview Must for Stitch Guindy:
1.  start() vs run()?
2.  Why wait(), notify() in Object not Thread? Because lock is on object.
3.  wait() vs sleep()? wait releases lock, sleep doesn't.
4.  How CompletableFuture works internally? ForkJoinPool.

#### 8.6 Java Memory Model

The Java Memory Model defines when one thread's writes become visible to another and which reorderings are legal.

A **happens-before** relationship provides visibility and ordering. Important examples:

- Unlocking a monitor happens-before a later lock of the same monitor.
- Writing a volatile field happens-before a later read of that field.
- Actions before `Thread.start()` happen-before actions in the started thread.
- Actions in a thread happen-before another thread successfully returns from `join()`.
- Class initialization happens-before use of that class.

Atomicity, visibility, and ordering are different:

- `count++` is a read-modify-write sequence and is not atomic.
- `volatile int count` makes values visible but does not make `count++` atomic.
- Use locking, `AtomicInteger`, or `LongAdder` depending on the operation and contention.

#### 8.7 Safe Publication

An object is safely published when other threads cannot observe a partially constructed state. Common mechanisms:

- Store it in a properly locked field.
- Store it in a volatile field.
- Publish through a thread-safe collection.
- Initialize it in a static initializer.
- Share it before starting a new thread.

Do not allow `this` to escape from a constructor by registering listeners, starting threads, or calling external code.

#### 8.8 Executor Sizing and Backpressure

**Explanation:** Executor size limits simultaneous work, while queue capacity controls waiting work. Backpressure or rejection is necessary when incoming demand can exceed sustainable processing capacity.

**Why it matters:** Concurrency defects may disappear during testing and reappear under load, so guarantees must come from design rather than timing.

- CPU-bound pools are commonly near the number of available processors.
- I/O-bound workloads may use more threads, but downstream capacity remains the real limit.
- An unbounded work queue can turn overload into memory exhaustion and extreme latency.
- Use bounded queues and an explicit rejection policy.
- Separate workloads with different latency or blocking characteristics when they would otherwise starve each other.

```java
ExecutorService executor = new ThreadPoolExecutor(
    4, 8,
    30, TimeUnit.SECONDS,
    new ArrayBlockingQueue<>(100),
    new ThreadPoolExecutor.CallerRunsPolicy());
```

Always shut down owned executors and await termination with a deadline.

#### 8.9 Cancellation and Timeouts

Interruption is cooperative cancellation:

```java
while (!Thread.currentThread().isInterrupted()) {
  processNextItem();
}
```

- Blocking JDK methods often throw `InterruptedException`.
- Cleanup resources and restore the interrupt when code cannot propagate it.
- `Future.cancel(true)` requests interruption; it cannot guarantee task termination.
- Apply timeouts at every blocking boundary and use an overall request deadline when possible.
- Avoid `CompletableFuture.join()` on threads that must remain responsive unless completion is guaranteed.

#### 8.10 Concurrent Utilities

**Explanation:** Coordination utilities encode common synchronization patterns more safely than manual `wait` and `notify`. Each has a lifecycle, ownership model, and behavior under interruption.

**Why it matters:** Concurrency defects may disappear during testing and reappear under load, so guarantees must come from design rather than timing.

- `CountDownLatch`: wait until a fixed number of events complete; one-shot.
- `CyclicBarrier`: repeatedly wait until a group reaches a point.
- `Semaphore`: limit concurrent access to a scarce resource.
- `Phaser`: flexible multi-phase coordination.
- `BlockingQueue`: producer-consumer handoff with optional capacity.
- `StampedLock`: optimistic reads for specialized workloads; not reentrant.
- `LongAdder`: scalable counters under heavy contention; `sum()` is not an atomic snapshot.

Prefer high-level utilities over manual `wait()`/`notify()`. If using conditions, always wait in a loop because wakeups may be spurious.

#### 8.11 Intrinsic Locks and Reentrancy

Every object has an intrinsic monitor. A synchronized instance method locks `this`; a synchronized static method locks the `Class` object.

Locks are reentrant: a thread holding a monitor can acquire it again. Reentrancy supports synchronized methods calling one another but does not make a class automatically thread-safe.

Keep critical sections small, avoid calling unknown external code while locked, and never lock publicly accessible objects such as string literals.

#### 8.12 Lock Ordering

Deadlock prevention commonly uses a global order:

```java
void transfer(Account left, Account right, Money amount) {
  Account first = left.id() < right.id() ? left : right;
  Account second = first == left ? right : left;

  synchronized (first) {
    synchronized (second) {
      left.transferTo(right, amount);
    }
  }
}
```

Real code must also handle equal ordering keys. Alternatives include a tie lock, `tryLock` with timeout, or redesigning ownership to avoid multiple locks.

#### 8.13 ThreadLocal

**Explanation:** `ThreadLocal` associates a value with a thread instead of passing it explicitly. Pooled threads outlive requests, so values must be removed to prevent leaks and cross-request contamination.

**Why it matters:** Concurrency defects may disappear during testing and reappear under load, so guarantees must come from design rather than timing.

`ThreadLocal` gives each thread a separate value:

```java
private static final ThreadLocal<DateTimeFormatter> FORMATTER =
    ThreadLocal.withInitial(() -> DateTimeFormatter.ISO_DATE_TIME);
```

Modern `DateTimeFormatter` is already thread-safe, so this example does not need `ThreadLocal`; it illustrates syntax only.

In thread pools, always call `remove()` in a `finally` block for request-scoped values. Otherwise values can leak across requests and retain objects as long as the worker thread lives.

#### 8.14 CompletableFuture Error Flow

**Explanation:** A completion stage can transform, combine, observe, or recover from asynchronous outcomes. Error-handling stages affect whether the resulting future remains failed or becomes successful.

**Why it matters:** Concurrency defects may disappear during testing and reappear under load, so guarantees must come from design rather than timing.

```java
CompletableFuture<Result> result = loadUser(id)
    .thenCompose(this::loadOrders)
    .thenCombine(loadPreferences(id), this::combine)
    .orTimeout(2, TimeUnit.SECONDS)
    .exceptionally(error -> fallback(error));
```

- `thenApply`: transform a completed value.
- `thenCompose`: flatten an asynchronous dependent operation.
- `thenCombine`: combine independent operations.
- `exceptionally`: recover from failure.
- `handle`: process success or failure and produce a value.
- `whenComplete`: observe completion without changing its result.

Unless an executor is supplied, async stages commonly use the common pool. Choose executors based on blocking behavior and lifecycle ownership.

#### 8.15 Atomic Classes

Atomic variables support lock-free compare-and-set loops:

```java
AtomicReference<State> state = new AtomicReference<>(initial);
state.updateAndGet(current -> current.next());
```

The update function may run more than once due to retries, so it must be side-effect free. Multiple independent atomic fields do not make a multi-field invariant atomic; use one immutable state object or a lock.

#### 8.16 False Sharing and Contention

Independent frequently written fields can occupy the same cache line, causing cores to invalidate each other's cache entries. This is false sharing.

Do not attempt manual padding without profiling and JVM-specific evidence. Often the better fix is reducing shared mutation, partitioning state, batching updates, or using contention-friendly utilities.

#### 8.17 Chapter Review and Common Pitfalls

##### Thread lifecycle

- `Thread.State` exposes `NEW`, `RUNNABLE`, `BLOCKED`, `WAITING`, `TIMED_WAITING`, and `TERMINATED`; the JVM does not expose a separate `RUNNING` state.
- A Java `RUNNABLE` thread may be executing or waiting in native operating-system activity.
- A thread cannot be restarted after termination.
- Uncaught exceptions terminate the thread and are sent to its uncaught-exception handler.

##### Visibility and atomicity

- Reads/writes of references and most primitives are atomic, but a correct algorithm also needs visibility and ordering.
- Volatile publication works only when readers obtain all related state through the appropriate happens-before path.
- Immutable objects with final fields are easier to publish safely.
- Data races make behavior timing-dependent even when individual reads and writes do not tear.

##### Synchronization

- Intrinsic locks are released automatically when control exits the synchronized block, including by exception.
- `wait` releases the monitor; `sleep` does not.
- `notify` chooses one arbitrary waiter; `notifyAll` lets all waiters recheck their conditions.
- Wait conditions belong in `while` loops and all condition state must be protected by the same monitor.

##### Executors

- Separate task submission from execution policy.
- A fixed pool with an unbounded queue has no effective maximum beyond its core size.
- `CallerRunsPolicy` slows submitters but can be dangerous on event-loop or request threads.
- Name threads and attach uncaught-exception handling for diagnostics.
- Periodic scheduled tasks differ: fixed rate targets a schedule, while fixed delay waits after completion.

##### Futures

- `Future.get` wraps task failure in `ExecutionException`.
- `CompletableFuture.join` wraps failure in unchecked `CompletionException`.
- Cancellation and timeout should propagate through the whole operation graph where possible.
- `allOf` does not return component values; retain the original futures and inspect them after completion.
- A recovery stage can accidentally turn failure into success; make that policy explicit.

##### Locks and atomics

- `ReentrantLock` supports interruptible acquisition, timed acquisition, fairness, and multiple conditions.
- Fair locks can reduce starvation but often reduce throughput.
- Always unlock in `finally` after successful acquisition.
- Compare-and-set loops can encounter the ABA problem when a value changes away and back; stamped/versioned references can help.

##### Deadlock and liveness

- Livelock means threads keep reacting but make no progress.
- Starvation means a thread repeatedly loses access to needed resources.
- Lock ordering prevents only cycles involving correctly ordered locks; callbacks and external resources can introduce hidden edges.
- Capture thread dumps during the incident, not only after recovery.

##### Virtual threads

- Virtual threads improve concurrency for blocking workloads, not single-request speed.
- Thread-local-heavy designs can consume significant memory when millions of virtual threads are created.
- Avoid using thread pools to ration virtual threads; use semaphores to limit the scarce dependency.
- Profile pinning, carrier utilization, downstream pools, and total memory under realistic load.

##### Structured concurrency

- Structured concurrency treats related child tasks as one lifetime-bounded operation.
- Failure or cancellation can propagate to sibling tasks instead of leaving orphan work.
- Scoped task results should not escape their enclosing scope.
- Check the target JDK's preview/final status before adopting the API.

### 9. Java 8+ Features

#### 9.1 Functional Interfaces, Lambdas, and Method References

**Explanation:** A functional interface describes one abstract operation. Lambdas and method references create implementations that can be passed to APIs as behavior.

**Why it matters:** These APIs reduce boilerplate only when their lazy, immutable, optional, and temporal semantics are understood.

- Functional Interface: Only 1 abstract method. Can have default/static. Marked with @FunctionalInterface
@FunctionalInterface
interface MyFunc { int add(int a, int b); } // 1 abstract method

// Built-in FIs - you must know:
1. Predicate<T> -> boolean test(T t) - for filter condition
2. Function<T,R> -> R apply(T t) - transform
3. Consumer<T> -> void accept(T t) - consume, no return
4. Supplier<T> -> T get() - supply value
5. BiFunction, BiPredicate, UnaryOperator, BinaryOperator
- Lambda: Anonymous function - short form of FI implementation (params) -> {body}
// Old way
MyFunc f = new MyFunc(){ public int add(int a,int b){ return a+b; } };

// Lambda way
MyFunc f = (a,b) -> a+b;
MyFunc f2 = (a,b) -> { int c=a+b; return c; };

Predicate<Integer> isEven = n -> n%2==0;
Function<String,Integer> len = s -> s.length();
Consumer<String> print = s -> System.out.println(s);
Supplier<Integer> random = () -> new Random().nextInt();

// Usage
isEven.test(10); // true
len.apply("Stitch"); // 6
- Method Reference: Shortcut for lambda Class::method - even shorter
// Types:
// 1. Static method
Function<String,Integer> f = s -> Integer.parseInt(s);
Function<String,Integer> f2 = Integer::parseInt; // same

// 2. Instance method of object
Consumer<String> c1 = s -> System.out.println(s);
Consumer<String> c2 = System.out::println;

// 3. Instance method of arbitrary object
Function<String,String> f3 = s -> s.toUpperCase();
Function<String,String> f4 = String::toUpperCase;

// 4. Constructor
Supplier<List<String>> s1 = () -> new ArrayList<>();
Supplier<List<String>> s2 = ArrayList::new;

#### 9.2 Stream API

**Explanation:** A stream is a lazy, single-use pipeline over elements. Intermediate operations describe transformations, and a terminal operation triggers traversal and produces a result or side effect.

**Why it matters:** These APIs reduce boilerplate only when their lazy, immutable, optional, and temporal semantics are understood.

Stream = pipeline to process collection - no storage, lazy, can be parallel.
List<Integer> list = Arrays.asList(1,2,3,4,5,6);

// Old imperative - tell HOW
List<Integer> even = new ArrayList<>();
for(int n: list){ if(n%2==0) even.add(n*2); }

// Stream declarative - tell WHAT
List<Integer> even2 = list.stream()
  .filter(n -> n%2==0) // intermediate - keeps even
  .map(n -> n*2) // intermediate - transform
  .collect(Collectors.toList()); // terminal

// Flow: Source -> intermediate ops (lazy, can be many) -> terminal op (eager, triggers execution)
Intermediate Ops (return Stream - lazy):
filter(Predicate) - keep matching
map(Function) - 1 to 1 transform
flatMap - 1 to many - flattens
  List<List<String>> list = [[A,B],[C,D]] -> flatMap -> [A,B,C,D]
  .flatMap(Collection::stream)

distinct() - removes duplicates
sorted() - natural sort, sorted(comparator)
limit(n), skip(n), peek() - for debugging
Terminal Ops (return result - executes):
collect(Collectors.toList() / toSet() / toMap() / joining() / groupingBy() / partitioningBy())
forEach(), forEachOrdered()
count(), min(), max(), reduce(), toArray()
anyMatch(), allMatch(), noneMatch(), findFirst(), findAny()
Full examples - Stitch asks to write these:
// 1. Filter employees salary > 50000
employees.stream().filter(e -> e.salary>50000).collect(toList())

// 2. Map to names uppercase
employees.stream().map(e -> e.name.toUpperCase()).collect(toList())

// 3. flatMap - get all skills from employees
List<String> allSkills = employees.stream()
  .flatMap(e -> e.skills.stream()) // e.skills is List<String>
  .collect(toList())

// 4. reduce - sum
int sum = list.stream().reduce(0, (a,b) -> a+b); // 0 identity
int sum2 = list.stream().reduce(0, Integer::sum);
Optional<Integer> max = list.stream().reduce((a,b) -> a>b?a:b);

// 5. Collect - groupingBy
Map<String, List<Employee>> byDept = employees.stream()
  .collect(Collectors.groupingBy(e -> e.dept));

Map<Boolean, List<Employee>> partition = employees.stream()
  .collect(Collectors.partitioningBy(e -> e.salary>50000));

String names = employees.stream().map(e->e.name).collect(Collectors.joining(", "));

// 6. Sort + limit
employees.stream()
  .sorted(Comparator.comparing(Employee::getSalary).reversed())
  .limit(3) // top 3 highest paid
  .collect(toList());

// Parallel stream - uses ForkJoinPool - for large data
list.parallelStream().filter(...).collect(toList());
// Don't use parallel if order matters or small data

#### 9.3 Optional

// Problem
String s = null; s.length(); // NPE

// Solution Optional - box that may or may not have value
Optional<String> opt = Optional.ofNullable(s); // ofNullable allows null, of() throws if null

opt.isPresent(); // check
opt.isEmpty(); // Java 11+
opt.get(); // get value - risky, throws if empty

// Best ways:
opt.ifPresent(val -> System.out.println(val));
String res = opt.orElse("Default"); // if null return default
String res2 = opt.orElseGet(() -> getDefault()); // lazy supplier
String res3 = opt.orElseThrow(() -> new RuntimeException("Not found"));

opt.filter(v -> v.length()>3).map(String::toUpperCase).orElse("NA");

// Real use in service
public Optional<Employee> findById(int id){ return Optional.ofNullable(db.get(id)); }

#### 9.4 Default and Static Interface Methods

Why? To add new methods to interface without breaking old implementations.
interface Payment {
  void pay(); // abstract
  
  default void log(){ System.out.println("Logging"); } // default - can be overridden
  static void info(){ System.out.println("Payment gateway"); } // static - cannot override, call via InterfaceName

  private void helper(){} // Java 9 - private method in interface for code reuse inside default methods
}

Payment.info(); // call static
- Diamond problem with default methods? If class implements 2 interfaces with same default method, must override.

#### 9.5 Date and Time API

// Old Date is mutable, not thread safe - don't use
Date d = new Date(); Calendar c = Calendar.getInstance();

// New API - immutable, thread safe
LocalDate date = LocalDate.now(); // 2026-10-03 - only date
LocalTime time = LocalTime.now(); // 10:30:15 - only time
LocalDateTime dt = LocalDateTime.now(); // both - most used
ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));

dt.plusDays(5).minusMonths(1);
dt.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

LocalDate dob = LocalDate.of(2000,1,15);
Period age = Period.between(dob, LocalDate.now()); // age.getYears()

Duration dur = Duration.between(time1, time2); // time diff

Instant instant = Instant.now(); // timestamp for machine - UTC

#### 9.6 Records, Sealed Classes, and Pattern Matching

**Explanation:** Records model transparent data, sealed types define a closed family, and pattern matching safely extracts subtype data. Together they support concise data-oriented designs.

**Why it matters:** These APIs reduce boilerplate only when their lazy, immutable, optional, and temporal semantics are understood.

- Record - immutable data carrier - auto generates constructor, getters, equals, hashCode, toString
// Old - 50 lines
class Employee { private final int id; private final String name; constructor, getters, equals... }

// New - 1 line
record Employee(int id, String name) {} // immutable, final
Employee e = new Employee(1, "Ali");
e.id(); e.name(); // getters - same name as field
// Can add custom constructor, methods but cannot extend
- Sealed Classes - control inheritance
sealed class Payment permits UPI, Card, NetBanking {} // only these 3 can extend
final class UPI extends Payment {}
final class Card extends Payment {}
final class NetBanking extends Payment {}
// non-sealed if you want to allow further extension
- Pattern Matching
// Old instanceof + cast
if(obj instanceof String){ String s = (String)obj; System.out.println(s.toUpperCase()); }

// New - Java 16+
if(obj instanceof String s){ System.out.println(s.toUpperCase()); } // pattern var s auto created

// Switch pattern matching Java 21+
String result = switch(obj){
  case Integer i -> "Integer " + i;
  case String s -> "String " + s.toUpperCase();
  case null -> "null";
  default -> "Unknown";
};

// Text blocks Java 15+
String json = """
  {
    "name": "Stitch",
    "id": 1
  }
  """;

##### Common Interview Exercises

// Find duplicate numbers using stream
list.stream().collect(groupingBy(Function.identity(), counting()))
    .entrySet().stream().filter(e->e.getValue()>1).map(Map.Entry::getKey).collect(toList())

// 2nd highest salary
employees.stream().map(e->e.salary).sorted(Comparator.reverseOrder()).skip(1).findFirst()

#### 9.7 Stream Semantics and Laziness

A stream can be consumed only once. Intermediate operations run only when a terminal operation requests elements.

```java
Optional<String> first = names.stream()
    .filter(name -> {
      System.out.println("checking " + name);
      return name.startsWith("A");
    })
    .findFirst();
```

Because `findFirst` short-circuits, later elements may never be inspected. Avoid side effects in `map`, `filter`, and `peek`; they make behavior dependent on pipeline optimization and parallel execution.

#### 9.8 Primitive Streams

Use `IntStream`, `LongStream`, or `DoubleStream` to avoid boxing overhead and access numeric operations:

```java
IntSummaryStatistics stats = employees.stream()
    .mapToInt(Employee::age)
    .summaryStatistics();

double average = stats.getAverage();
int maximum = stats.getMax();
```

Use `mapToObj` to return to object streams and `boxed()` when a collection of wrappers is required.

#### 9.9 Collector Details

**Explanation:** A collector defines how stream elements create, update, combine, and finish a result container. Duplicate keys, mutability, ordering, and parallel compatibility must be handled explicitly.

**Why it matters:** These APIs reduce boilerplate only when their lazy, immutable, optional, and temporal semantics are understood.

```java
Map<String, Long> countByDepartment = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::department,
        Collectors.counting()));

Map<Integer, Employee> byId = employees.stream()
    .collect(Collectors.toMap(
        Employee::id,
        Function.identity(),
        (existing, replacement) -> existing));
```

`toMap` needs a merge function when duplicate keys are possible. For parallel collection, a collector's supplier, accumulator, combiner, and characteristics must obey associativity and isolation rules.

#### 9.10 Parallel Stream Cautions

**Explanation:** Parallel streams divide work across shared worker threads. Coordination overhead, blocking, encounter order, and shared state can make them slower or incorrect.

**Why it matters:** These APIs reduce boilerplate only when their lazy, immutable, optional, and temporal semantics are understood.

- Parallel streams normally share the common `ForkJoinPool`.
- Blocking operations can starve unrelated work using the same pool.
- Parallelism adds splitting, coordination, and merging overhead.
- Ordered pipelines and stateful operations may reduce benefits.
- Results must be independent of scheduling; do not mutate shared non-thread-safe state.
- Benchmark with realistic data before using `parallelStream`.

#### 9.11 Optional Design

**Explanation:** `Optional` represents an intentionally absent return value and supports transformation without immediate null checks. It does not eliminate the need to validate required inputs.

**Why it matters:** These APIs reduce boilerplate only when their lazy, immutable, optional, and temporal semantics are understood.

- Use `map` when the function returns a plain value and `flatMap` when it returns another `Optional`.
- `orElse` evaluates its argument eagerly; `orElseGet` invokes its supplier only when empty.
- Avoid calling `get()` without a proven presence.
- Do not use `Optional` merely to avoid validating required parameters.
- Collections should usually be empty rather than wrapped in `Optional`.

```java
String city = findUser(id)
    .flatMap(User::address)
    .map(Address::city)
    .orElse("Unknown");
```

#### 9.12 Time-Zone and Clock Guidance

**Explanation:** A timestamp, local wall time, offset, and region time zone represent different concepts. Selecting the correct type prevents daylight-saving and testability errors.

**Why it matters:** These APIs reduce boilerplate only when their lazy, immutable, optional, and temporal semantics are understood.

- `Instant` is a point on the UTC timeline.
- `LocalDateTime` has no zone and is ambiguous during daylight-saving transitions.
- `ZonedDateTime` combines local date/time with time-zone rules.
- `OffsetDateTime` has a fixed offset but not complete regional rules.
- Persist timestamps as `Instant` or an offset-aware database type.
- Inject `Clock` for deterministic tests.

#### 9.13 Functional Composition

**Explanation:** Functional composition combines small predicates, functions, and comparators into larger behavior. Composition is useful when each function remains understandable and side-effect free.

**Why it matters:** These APIs reduce boilerplate only when their lazy, immutable, optional, and temporal semantics are understood.

```java
Predicate<Employee> active = Employee::active;
Predicate<Employee> senior = employee -> employee.years() >= 5;
Predicate<Employee> selected = active.and(senior);

Function<String, String> trim = String::trim;
Function<String, String> normalize = trim.andThen(String::toLowerCase);
```

- Predicates support `and`, `or`, and `negate`.
- Functions support `compose` and `andThen`.
- Comparators support `thenComparing`, `reversed`, and null ordering.
- Unary and binary operators model operations whose result has the same type as operands.

#### 9.14 Stream Reduction Laws

For correct parallel reduction:

- The identity must be neutral: combining it with any element leaves the element unchanged.
- The accumulator and combiner must be associative.
- The combiner must be compatible with the accumulator.
- Mutable reduction should use `collect`, not mutate one identity object in `reduce`.

```java
List<String> result = stream.collect(
    ArrayList::new,
    List::add,
    List::addAll);
```

#### 9.15 Date/Time Edge Cases

Local times can be invalid or ambiguous during daylight-saving transitions:

- A gap skips local times when clocks move forward.
- An overlap repeats local times when clocks move backward.

Construct with a `ZoneId` and decide how ambiguity should be resolved. Time-zone database rules change, so retain the original zone when future local scheduling matters.

#### 9.16 Resource Streams

Some streams wrap resources and must be closed:

```java
try (Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8)) {
  long errors = lines.filter(line -> line.startsWith("ERROR")).count();
}
```

Collection streams do not normally need closing. A terminal operation does not automatically close an I/O-backed stream.

#### 9.17 Collector Composition

Useful downstream collectors include:

```java
Map<String, Set<String>> namesByDepartment = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::department,
        Collectors.mapping(Employee::name, Collectors.toSet())));

Map<Boolean, Long> activeCounts = employees.stream()
    .collect(Collectors.partitioningBy(
        Employee::active,
        Collectors.counting()));
```

Modern collectors also include `filtering`, `flatMapping`, and `teeing`. Prefer readable intermediate steps when deeply nested collectors become difficult to maintain.

#### 9.18 Chapter Review and Common Pitfalls

##### Functional interfaces and lambdas

- A functional interface has one abstract method after accounting for inherited `Object` methods.
- Lambda `this` refers to the enclosing instance; anonymous-class `this` refers to the anonymous object.
- Captured local variables must be final or effectively final.
- Lambda objects have no specified identity; do not synchronize on or compare them by reference.
- Serializable lambdas are fragile across code changes and should not be used as durable data.

##### Method references

- Method references still undergo overload resolution and type inference from the target functional interface.
- `Type::instanceMethod` can mean an unbound receiver where the first function argument becomes the receiver.
- Bound receiver expressions are evaluated when the method reference is created.
- Prefer the lambda form when it communicates argument mapping more clearly.

##### Streams

- Streams describe computation, not stored data.
- Encounter order comes from the source and operations; `HashSet` does not supply stable encounter order.
- Stateful operations such as `sorted` and `distinct` may buffer elements.
- Short-circuiting may stop traversal early but is not guaranteed to inspect the minimum possible elements in every parallel pipeline.
- Use `unordered()` only when semantics allow it and parallel optimization may benefit.

##### Collectors

- `groupingByConcurrent` is useful only when downstream accumulation and ordering requirements permit concurrent collection.
- `toUnmodifiableList` rejects null and guarantees an unmodifiable result.
- `collectingAndThen` applies a finishing transformation.
- A custom collector must not reuse mutable containers across independent accumulation paths.

##### Optional

- `Optional` itself should never be null.
- `map` converts a null mapping result to empty; `flatMap` requires a non-null `Optional`.
- `or` lazily supplies an alternative `Optional`.
- Avoid storing `Optional` in entities or serializing it unless the framework explicitly supports the intended representation.

##### Date and time

- `Period` is date-based; `Duration` is time-based.
- Adding one calendar day across a daylight-saving boundary may differ from adding 24 hours.
- `YearMonth` and `MonthDay` model partial dates without invented day/year values.
- Formatters are immutable and thread-safe.
- Locale, chronology, and zone are distinct concerns.

##### Records and sealed types

- A record's generated accessors return component values directly; copy mutable components if needed.
- Records cannot declare additional instance fields.
- Serialization of records uses component-based reconstruction semantics.
- Sealed hierarchies define permitted direct subtypes, which need not all be nested.
- A `non-sealed` subtype reopens extension beneath that point.

##### Parallel streams

- Splitting quality depends on the source; array-backed data partitions better than linked structures.
- Stateful lambdas violate stream non-interference even when protected by synchronization.
- Parallel collection may increase memory usage.
- Server code should be cautious about shared common-pool interference and latency variability.


## Part III: JVM and Advanced Java

Runtime architecture, garbage collection, reflection, serialization, I/O, and database connectivity.

### 10. JVM Internals

#### 10.1 JVM Architecture

Java Code (.java) -> javac -> Bytecode (.class) -> JVM -> OS -> Hardware

##### JVM Components

1. ClassLoader Subsystem
2. Runtime Memory Areas (Heap, Stack, Method Area, PC Register, Native Stack)
3. Execution Engine (Interpreter, JIT Compiler, GC)

#### 10.2 Class Loading

3 types in hierarchy - Parent Delegation Model:
Bootstrap ClassLoader - C++ - loads java.lang.*, rt.jar - top parent - null
   |
Platform ClassLoader (Java 9+ was Extension) - loads platform libs
   |
Application/ClassPath ClassLoader - loads your classes from classpath - your code
   |
Custom ClassLoader - you create for hot deploy
- Loading Steps:

1.  Loading: Find .class file and read binary to create Class object in Method Area
2.  Linking: a) Verification - bytecode is valid? no stack overflow? b) Preparation - static variables get default values (0, null) c) Resolution - symbolic references replaced with direct references
3.  Initialization: Static blocks executed, static variables assigned actual values - static int x=10; Now x=10
class A {
  static int x = 10;
  static { System.out.println("Static block"); } // runs in initialization
}
Class.forName("A"); // triggers loading + initialization
- Parent Delegation: Child asks parent first to load - avoids duplicate loading and security - you cannot load your own java.lang.String

#### 10.3 Runtime Memory Areas

Stack: Per thread - methods, local vars, LIFO - fast
Heap: All objects - Shared
    - Young Gen: New objects
        - Eden: Where new objects first created - 80% of Young
        - Survivor S0, S1: Objects survived 1 GC - 10% each
    - Old Gen: Long living objects - objects survived many Young GCs - big
    - Before Java 8, there was PermGen inside heap for class metadata - caused OutOfMemory
    - Metaspace: From Java 8 - outside heap - in Native Memory - stores class metadata, String Pool - auto grows

Method Area (Metaspace): Class metadata, static vars, String Pool - shared
PC Register: Per thread - address of current executing instruction
Native Method Stack: For native (C++) methods
Employee e = new Employee(); 
// e reference in Stack
// new Employee() object in Eden (Young Gen)
// Employee class metadata in Metaspace

#### 10.4 Garbage Collection

**Explanation:** Garbage collection finds objects unreachable from GC roots and reclaims their memory. Collector algorithms balance pause time, throughput, footprint, and CPU rather than eliminating all pauses.

**Why it matters:** A correct JVM model helps distinguish application bugs from heap, native-memory, class-loading, compilation, or collector behavior.

- What is GC? Automatic memory cleanup - deletes unreachable objects. You cannot force GC - System.gc() is only hint.

- How GC knows object is garbage? No reference pointing to it.
Employee e = new Employee(); e = null; // now object eligible for GC
- Mark & Sweep Algorithm - Core:

1.  Mark: GC traverses object graph from GC Roots (static vars, stack refs, running threads) and marks all reachable objects as alive.
2.  Sweep: Deletes unmarked objects - reclaims memory.
3.  Compact (Mark-Sweep-Compact): After sweep, memory fragmented. Compact moves alive objects together to make contiguous space.

- Generational GC - Why Young/Old? Because most objects die young.
Minor GC (Young GC): When Eden full
- Marks alive in Eden + S0, moves survivors to S1, clears Eden
- Objects age increment each survival
- After 15 ages (default -XX:MaxTenuringThreshold), moves to Old Gen

Major GC (Old GC): When Old Gen full - slow - STW (Stop The World) - all app threads paused

Full GC: Young + Old + Metaspace
- GC Types - Interview asks which you use:
| GC | How | For |
| --- | --- | --- |
| Serial GC | Single thread, STW | Small apps |
| Parallel GC | Multi-thread Young | Default Java 8 - throughput |
| CMS (old) | Concurrent - less pause | Deprecated |
| G1 GC | Region based - splits heap into regions, predicts pause time - Default Java 9+ | Large heap, low pause - Stitch uses G1 |
| ZGC, Shenandoah | Ultra low pause <10ms even for TB heap | Java 11+ - huge apps |

- Tuning flags:
-Xms512m -Xmx1024m // min and max heap
-XX:+UseG1GC // use G1
-XX:MaxGCPauseMillis=200

#### 10.5 `equals()` and `hashCode()` Contract

**Explanation:** Logical equality and hashing must agree so equal keys reach the same hash bucket. Fields used by both methods should normally remain stable while objects are stored as keys.

**Why it matters:** A correct JVM model helps distinguish application bugs from heap, native-memory, class-loading, compilation, or collector behavior.

- Contract from Object class - MUST follow, else collections break.
class Employee {
  int id; String name;
  
  // Default from Object class - checks == - reference equality - WRONG for content
}

##### Contract Rules

1.  If a.equals(b) is true, then a.hashCode() == b.hashCode() MUST be true.
2.  If a.hashCode() == b.hashCode(), a.equals(b) can be false (collision allowed) but should try to minimize.
3.  If you override equals(), you MUST override hashCode().
4.  equals() must be reflexive, symmetric, transitive, consistent, and x.equals(null) false.
5.  hashCode() must be consistent - same object returns same hashCode until modified.

Why contract matters - HashMap fails if broken:
// WRONG - only equals overridden, not hashCode
@Override public boolean equals(Object o){ return id==((Employee)o).id; }
// Now e1.equals(e2) true, but hashCode different -> HashMap puts in different buckets -> duplicate keys!

Map<Employee,String> map = new HashMap<>();
map.put(e1, "A"); map.put(e2, "B"); // Should overwrite, but creates 2 entries - memory leak + bug

// CORRECT
@Override
public boolean equals(Object o){
  if(this==o) return true;
  if(o==null || getClass()!=o.getClass()) return false;
  Employee e = (Employee)o;
  return id==e.id && Objects.equals(name,e.name);
}
@Override
public int hashCode(){
  return Objects.hash(id, name); // must use same fields as equals
}
- Ideal hashCode: Use same fields, distribute well, use Objects.hash() or 31 * result + field.hashCode() - 31 is prime and fast with shift.

##### Interview Questions

1.  Why String Pool possible? Because String immutable + hashCode cached.
2.  What is GC Root? Stack refs, static, JNI.
3.  Can you call GC? System.gc() hint, not guarantee.
4.  OutOfMemory vs StackOverflow?
5.  What happens if hashCode not overridden?

#### 10.6 Bytecode Execution and JIT Compilation

The interpreter starts bytecode quickly. As methods become hot, tiered compilation uses C1 and C2 compilers to produce optimized native code.

Common optimizations include:

- Method inlining.
- Escape analysis and scalar replacement.
- Lock elimination.
- Loop optimizations.
- Devirtualization when runtime types are predictable.

If an assumption becomes false, the JVM can deoptimize compiled code and return execution to a less optimized tier. Warmup is why short ad-hoc benchmarks are misleading.

#### 10.7 Object Layout and Allocation

An object generally contains a header, instance fields, and alignment padding. Exact layout depends on JVM options and architecture.

- Thread-local allocation buffers make most small object allocations inexpensive.
- Large objects or exhausted buffers may take slower allocation paths.
- Escape analysis can eliminate some allocations, but code must not depend on that optimization.
- Compressed ordinary object pointers can reduce memory use for suitable heap sizes.

Use JOL or a profiler when exact layout matters; do not estimate from field sizes alone.

#### 10.8 Native Memory

Process memory includes more than the Java heap:

- Metaspace and compressed class space.
- Thread stacks.
- JIT code cache.
- Direct byte buffers.
- GC structures.
- JNI/native libraries.

Therefore, `-Xmx` must be lower than the process or container memory limit. Native Memory Tracking can help diagnose non-heap growth:

```text
java -XX:NativeMemoryTracking=summary ...
jcmd <pid> VM.native_memory summary
```

#### 10.9 Class-Loader Identity and Leaks

A class is identified by both its binary name and defining class loader. The same class file loaded by different class loaders produces incompatible runtime types.

Application servers and plugin systems can leak class loaders when long-lived objects retain application classes through:

- Static collections.
- `ThreadLocal` values.
- Running threads or executors.
- JDBC drivers or callbacks not deregistered.
- Framework caches and listeners.

#### 10.10 GC Terminology and Selection

**Explanation:** Collector terms describe live data, allocation, pauses, and concurrent work. Select and tune a collector from measured latency and throughput goals on the actual JDK.

**Why it matters:** A correct JVM model helps distinguish application bugs from heap, native-memory, class-loading, compilation, or collector behavior.

- **Live set:** objects reachable after collection.
- **Allocation rate:** bytes allocated per unit time.
- **Pause:** application threads stop at a safepoint.
- **Concurrent phase:** GC work overlaps application execution.
- **Throughput:** application time relative to total elapsed time.

Minor, major, and full-GC terminology is collector-specific; always interpret actual GC logs for the selected collector. Enable unified logging on modern JDKs:

```text
-Xlog:gc*,safepoint:file=gc.log:time,uptime,level,tags
```

#### 10.11 Common JVM Errors

**Explanation:** JVM errors identify different exhausted resources or linkage failures. The exact error text directs whether to inspect heap retention, metaspace, native threads, stacks, or class compatibility.

**Why it matters:** A correct JVM model helps distinguish application bugs from heap, native-memory, class-loading, compilation, or collector behavior.

- `OutOfMemoryError: Java heap space`: heap cannot satisfy allocation after GC.
- `OutOfMemoryError: Metaspace`: class metadata limit reached, often from excessive classes or loader leaks.
- `OutOfMemoryError: unable to create native thread`: OS/thread or native-memory limit reached.
- `StackOverflowError`: thread stack exhausted, usually by deep or infinite recursion.
- `LinkageError`: incompatible or duplicate class definitions, versions, or loader constraints.

#### 10.12 Verification, Resolution, and Initialization

Verification checks bytecode structure, type safety, stack usage, and control flow before execution. Resolution converts symbolic references in the constant pool into direct runtime references and may occur lazily.

Initialization executes static field assignments and static blocks in textual order after parent initialization. Interfaces initialize differently: initializing an interface does not automatically initialize all parent interfaces.

#### 10.13 Safepoints and Stop-the-World Pauses

At safepoints, JVM threads reach states where the runtime can safely inspect or modify shared VM structures. GC is a common reason, but deoptimization, biased-lock revocation in older JDKs, class redefinition, and some diagnostics may also require safepoints.

Pause time can include time for threads to reach a safepoint plus the operation itself. Unified safepoint logging helps distinguish these costs.

#### 10.14 Escape Analysis

The JIT may determine that an object:

- Does not escape a method.
- Escapes only to the current thread.
- Escapes globally.

This information can enable scalar replacement, stack-like optimization, and lock elimination. The Java specification still models normal heap objects; these are runtime optimizations and not guaranteed.

#### 10.15 Code Cache

JIT-compiled native methods reside in the code cache. If it fills, compilation may stop and application performance can degrade.

```text
jcmd <pid> Compiler.codecache
jcmd <pid> Compiler.queue
```

Investigate unusual compiler pressure, excessive generated classes, and JVM logs before changing code-cache flags.

#### 10.16 CDS and Startup

Class Data Sharing stores preprocessed class metadata in an archive to improve startup and memory sharing:

- The JDK ships with a default archive for core classes.
- Application CDS can include application and library classes.
- Dynamic CDS can create an archive after a training run.

CDS mainly targets startup and footprint; validate archive compatibility when application classes or JDK versions change.

#### 10.17 Container Awareness

Modern JVMs detect container CPU and memory limits, but deployment settings still require care:

- Leave headroom beyond heap for native memory.
- CPU limits influence GC and compiler thread ergonomics.
- Percentage-based heap flags can adapt across environments.
- Container OOM termination may occur before Java can write a heap dump.
- Monitor process resident memory as well as heap usage.

#### 10.18 Chapter Review and Common Pitfalls

##### Class loading

- Loading creates the runtime representation; linking verifies, prepares, and resolves; initialization executes class initialization logic.
- Parent delegation is a convention implemented by standard loaders, not an unbreakable VM rule.
- The thread context class loader lets container code discover application-provided services.
- `Class.forName(name, false, loader)` can load without initialization.
- Loader leaks retain every class and static field defined by that loader.

##### Runtime data areas

- Each thread has a PC register and JVM stack; the heap and method area are shared.
- Stack frames contain locals, an operand stack, and method linkage data.
- Native stacks support JNI and runtime implementation work.
- Direct buffers and native libraries consume memory outside the Java heap.

##### Execution engine

- Hotness counters and profiling data guide tiered compilation.
- Inlining enables many later optimizations but is limited by method size and polymorphism.
- Deoptimization preserves correctness when speculative assumptions fail.
- On-stack replacement can compile and enter optimized code in the middle of a long-running loop.

##### Garbage collection

- Reachability, not reference counting, determines liveness.
- Cross-generation or cross-region references require remembered metadata.
- Concurrent collectors still have brief stop-the-world phases.
- Humongous/large-object allocation can follow collector-specific paths.
- GC cannot reclaim objects still reachable through accidental caches, listeners, or thread locals.

##### Collectors

- Serial favors simplicity and small heaps.
- Parallel GC favors throughput.
- G1 divides the heap into regions and targets pause goals rather than guaranteeing them.
- ZGC and Shenandoah perform most relocation work concurrently to reduce pauses.
- Collector choice depends on latency targets, heap size, allocation rate, CPU budget, and JDK version.

##### JIT and allocation

- Allocation can be cheap while initialization, retention, and later collection remain costly.
- Escape analysis is sensitive to code shape and may change between runs or JDK versions.
- Benchmark warmup should allow relevant methods to reach stable compilation tiers.
- Debugging flags can alter optimization and timing.

##### Native memory and containers

- Resident set size includes committed pages across heap and native areas.
- A heap dump covers heap objects, not all native allocations.
- Direct-buffer limits, stack size, metaspace, and code cache contribute to container pressure.
- Kubernetes memory requests/limits and CPU throttling influence JVM ergonomics and latency.

##### Diagnostics

- Thread dumps show stack state but not necessarily which code consumed CPU over time.
- Heap histograms show shallow instance counts/sizes, not retained ownership.
- JFR correlates allocation, CPU, locks, I/O, exceptions, and GC with relatively low overhead.
- Diagnostic commands may pause or load the process; assess production impact.
- Preserve the exact JDK version and flags with collected evidence.

### 11. Advanced Core Java

#### 11.1 Serialization and Cloning

**Explanation:** Serialization converts object state into a storable or transferable form; cloning creates another object graph. Both require an explicit decision about identity, mutability, versioning, and copy depth.

**Why it matters:** Boundary APIs interact with files, databases, native memory, and dynamic code, where leaks and unsafe input have lasting impact.

- Serialization: Converting object to byte stream to save to file / send over network. Deserialization reverse.
// Must implement Serializable - marker interface - no methods
class Employee implements Serializable {
  private static final long serialVersionUID = 1L; // version - if you don't give, JVM generates. If class changes, ID mismatch -> InvalidClassException
  int id; String name;
  transient String password; // transient = skip during serialization - not saved - password becomes null after deser
  static String company = "Stitch"; // static not serialized - belongs to class not object
}

 // Serialization
Employee e = new Employee(1,"Ali");
ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("emp.ser"));
oos.writeObject(e); oos.close(); // object to file

// Deserialization - no constructor called
ObjectInputStream ois = new ObjectInputStream(new FileInputStream("emp.ser"));
Employee e2 = (Employee) ois.readObject();

// Externalizable vs Serializable: Externalizable you control writeExternal/readExternal, Serializable auto
- Cloning: Copy of object. Cloneable marker interface, else CloneNotSupportedException
class Employee implements Cloneable {
  int id; String name; Address addr; // Address is object
  
  @Override protected Object clone() throws CloneNotSupportedException {
    return super.clone(); // shallow copy
  }
}

// Shallow vs Deep Clone
Employee e1 = new Employee(1,"Ali", new Address("Chennai"));
Employee e2 = (Employee) e1.clone(); // shallow: e1.addr and e2.addr SAME object - if e2 changes address, e1 also changes - BUG

// Deep clone - you clone inner objects also
@Override protected Object clone() throws CloneNotSupportedException {
  Employee cloned = (Employee) super.clone();
  cloned.addr = (Address) this.addr.clone(); // deep - new Address object
  return cloned;
}
Use copy constructor instead of clone - better practice.

#### 11.2 Reflection and Annotations

**Explanation:** Reflection inspects or invokes runtime members, while annotations attach metadata for tools and frameworks. Both move checks from ordinary compilation toward dynamic processing.

**Why it matters:** Boundary APIs interact with files, databases, native memory, and dynamic code, where leaks and unsafe input have lasting impact.

- Reflection: Inspecting class at runtime - framework uses (Spring, Hibernate).
Class<?> clazz = Employee.class; // or Class.forName("com.stitch.Employee") or e.getClass()

clazz.getName(); clazz.getDeclaredFields(); clazz.getDeclaredMethods(); clazz.getConstructors();

// Create object via reflection - even private constructor
Constructor<?> cons = clazz.getDeclaredConstructor(); cons.setAccessible(true); Object obj = cons.newInstance();

// Call method
Method m = clazz.getDeclaredMethod("getName"); m.setAccessible(true); m.invoke(obj);
Field f = clazz.getDeclaredField("id"); f.setAccessible(true); f.set(obj, 100);

// Use case: Spring @Autowired uses reflection to inject, JUnit uses to run @Test methods
// Disadvantage: Slow, breaks encapsulation, security issue
- Annotations: Metadata - give info to compiler/framework.
// Built-in: @Override, @Deprecated, @FunctionalInterface, @SuppressWarnings

// Custom annotation
@Retention(RetentionPolicy.RUNTIME) // when available: SOURCE, CLASS, RUNTIME - RUNTIME needed for reflection
@Target({ElementType.METHOD, ElementType.TYPE}) // where can use
@interface MyAnnotation {
  String value() default "Stitch";
  int count() default 1;
}

@MyAnnotation(value="Payment", count=5)
class Service {
  @MyAnnotation(count=2)
  void pay(){}
}

// Read annotation via reflection
MyAnnotation ann = Service.class.getAnnotation(MyAnnotation.class);
ann.value(); // Payment

#### 11.3 I/O, NIO, and File Handling

**Explanation:** I/O APIs move bytes or characters through streams; NIO adds paths, buffers, channels, and multiplexing. Correct code defines encoding, ownership, size limits, and partial-operation handling.

**Why it matters:** Boundary APIs interact with files, databases, native memory, and dynamic code, where leaks and unsafe input have lasting impact.

| IO (java.io) | NIO (java.nio - New IO - Java 4) |
| --- | --- |
| Blocking - thread waits till read completes | Non-blocking - thread can do other work |
| Stream oriented - byte/char stream | Channel + Buffer oriented - faster |
| No selector | Selector - 1 thread handles many channels - Netty uses for 10k connections |
| Old | New - for high performance file/network |

// IO - File handling
File f = new File("a.txt"); f.exists(); f.createNewFile(); f.delete(); f.isDirectory();

FileReader fr = new FileReader("a.txt"); // char stream - text file
BufferedReader br = new BufferedReader(fr); // buffered - fast
String line; while((line=br.readLine())!=null){}

FileInputStream fis = new FileInputStream("a.jpg"); // byte stream - image, pdf
FileOutputStream fos = new FileOutputStream("b.jpg");

// Java 7+ best way - auto close
try(BufferedReader br = new BufferedReader(new FileReader("a.txt"))){
  br.lines().forEach(System.out::println);
}

// NIO - fast file
Path path = Paths.get("a.txt");
Files.readAllLines(path); Files.write(path, "hello".getBytes());
Files.exists(path); Files.createFile(path); Files.delete(path);

FileChannel channel = FileChannel.open(path, StandardOpenOption.READ);
ByteBuffer buffer = ByteBuffer.allocate(1024);
channel.read(buffer);

#### 11.4 JDBC

5 steps - must memorize:
// Step 1: Load Driver (Java 8+ auto loads, optional)
Class.forName("com.mysql.cj.jdbc.Driver");

// Step 2: Get Connection
String url = "jdbc:mysql://localhost:3306/stitchdb";
Connection con = DriverManager.getConnection(url, "root", "password");

// Step 3: Create Statement
Statement stmt = con.createStatement(); // simple, SQL injection risk
// Better - PreparedStatement - precompiled, prevents SQL injection
PreparedStatement ps = con.prepareStatement("SELECT * FROM emp WHERE id=?");
ps.setInt(1, 100);

// Step 4: Execute Query
ResultSet rs = ps.executeQuery(); // for SELECT
int count = stmt.executeUpdate("INSERT INTO emp VALUES(1,'Ali')"); // for INSERT/UPDATE/DELETE returns rows affected

while(rs.next()){
  int id = rs.getInt("id"); // or rs.getInt(1)
  String name = rs.getString("name");
}

// Step 5: Close - reverse order
rs.close(); ps.close(); con.close();

// Try-with-resources - auto close
try(Connection con = DriverManager.getConnection(url,user,pass);
    PreparedStatement ps = con.prepareStatement("SELECT...")){
    ResultSet rs = ps.executeQuery();
}
- Statement vs PreparedStatement vs CallableStatement:
    - Statement: Simple SQL, vulnerable to SQL injection, slow each time compiles.
    - PreparedStatement: ? placeholder, precompiled, safe, fast for repeated queries - USE THIS 99%.
    - CallableStatement: For calling stored procedures {call proc(?,?)}

- Transactions:
con.setAutoCommit(false); // start transaction
try{ 
  ps1.executeUpdate(); ps2.executeUpdate(); 
  con.commit(); 
} catch(Exception e){ con.rollback(); }

#### 11.5 Serialization Safety and Versioning

**Explanation:** Serialized data can outlive the class version that wrote it. Stable formats need compatibility rules, invariant validation, and protection against unsafe type construction.

**Why it matters:** Boundary APIs interact with files, databases, native memory, and dynamic code, where leaks and unsafe input have lasting impact.

- `serialVersionUID` controls compatibility checks but does not guarantee semantic compatibility.
- Adding fields is often compatible because missing fields receive defaults; changing field types or hierarchy can break compatibility.
- Validate invariants in `readObject`; constructors are not called for normal serializable classes.
- Prefer a stable schema format for long-lived storage and inter-service communication.
- Never deserialize untrusted native Java streams without a strict object filter.

#### 11.6 NIO Buffers and Channels

A buffer has `capacity`, `position`, and `limit`:

```java
ByteBuffer buffer = ByteBuffer.allocate(1024);
int read = channel.read(buffer); // position advances
buffer.flip();                   // limit = position; position = 0
while (buffer.hasRemaining()) {
  consume(buffer.get());
}
buffer.clear();                  // ready for another write
```

`clear()` does not erase bytes. `compact()` preserves unread data and moves it to the beginning. Direct buffers can improve native I/O but use native memory and are more expensive to allocate.

#### 11.7 File-System Correctness

**Explanation:** File operations depend on path normalization, links, permissions, atomicity, and filesystem capabilities. Code must not assume every platform offers identical behavior.

**Why it matters:** Boundary APIs interact with files, databases, native memory, and dynamic code, where leaks and unsafe input have lasting impact.

- Specify charsets explicitly, usually `StandardCharsets.UTF_8`.
- Use atomic move where supported for replace-style writes.
- Do not assume a single `read` or `write` processes the entire buffer.
- Close directory streams and file channels.
- Decide how symbolic links should be handled for security-sensitive operations.
- Use streaming APIs for large files rather than `readAllBytes`.

#### 11.8 JDBC Transactions and Pooling

**Explanation:** A transaction groups database changes into one commit or rollback. A connection pool reuses connections, but closing a pooled connection is still required to return it.

**Why it matters:** Boundary APIs interact with files, databases, native memory, and dynamic code, where leaks and unsafe input have lasting impact.

- Obtain connections from a `DataSource`, normally backed by a connection pool.
- Keep transactions short; never wait for user input or remote network calls while holding one open.
- Return pooled connections by closing them.
- Choose isolation based on required consistency and database behavior.
- Batch repeated updates and inspect partial failures.
- Set query and transaction timeouts.

```java
try (Connection connection = dataSource.getConnection()) {
  connection.setAutoCommit(false);
  try {
    updateInventory(connection);
    createOrder(connection);
    connection.commit();
  } catch (SQLException e) {
    connection.rollback();
    throw e;
  }
}
```

#### 11.9 Reflection and Method Handles

Reflection is flexible but shifts errors to runtime and can conflict with module encapsulation. Cache validated metadata when repeatedly used.

`MethodHandle` and `VarHandle` provide typed, JVM-supported dynamic access:

- `MethodHandle`: invoke methods, constructors, and fields through a typed signature.
- `VarHandle`: access fields or array elements with defined memory-ordering modes.

Use ordinary calls when types are known statically.

#### 11.10 ServiceLoader

**Explanation:** `ServiceLoader` discovers implementations of a known interface from classpath or module metadata. Discovery does not define provider selection, error recovery, or lifecycle.

**Why it matters:** Boundary APIs interact with files, databases, native memory, and dynamic code, where leaks and unsafe input have lasting impact.

`ServiceLoader` supports provider discovery without hard-coding implementations:

```java
ServiceLoader<PaymentProvider> providers =
    ServiceLoader.load(PaymentProvider.class);

for (PaymentProvider provider : providers) {
  provider.initialize();
}
```

Classpath providers use `META-INF/services/<interface-name>`; named modules use `uses` and `provides`.

#### 11.11 Memory-Mapped Files

**Explanation:** Memory mapping exposes file regions as memory buffers and can improve certain random-access workloads. It also introduces platform-sensitive paging, locking, and cleanup behavior.

**Why it matters:** Boundary APIs interact with files, databases, native memory, and dynamic code, where leaks and unsafe input have lasting impact.

`FileChannel.map` maps a file region into memory:

```java
try (FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {
  MappedByteBuffer buffer =
      channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
  consume(buffer);
}
```

Memory mapping can help random access and large-file workloads, but page faults, address-space use, file locking behavior, and unmapping timing are platform-sensitive. Benchmark against buffered I/O.

#### 11.12 Asynchronous and Non-Blocking I/O

**Explanation:** Non-blocking I/O lets a small number of threads manage many channels by reacting to readiness. The application must still handle framing, partial progress, backpressure, and timeouts.

**Why it matters:** Boundary APIs interact with files, databases, native memory, and dynamic code, where leaks and unsafe input have lasting impact.

- `AsynchronousFileChannel` completes file operations through futures or callbacks.
- `Selector` multiplexes many non-blocking channels on one thread.
- A channel's readiness means an operation can make progress, not necessarily finish completely.
- Network protocols still require framing, partial-read handling, backpressure, and timeout logic.

Frameworks such as Netty encapsulate much of this complexity. Do not build a custom event loop unless requirements justify it.

#### 11.13 JDBC Isolation Levels

Standard JDBC levels include:

- `READ_UNCOMMITTED`: may allow dirty reads.
- `READ_COMMITTED`: prevents dirty reads.
- `REPEATABLE_READ`: also protects repeated reads, with database-specific phantom behavior.
- `SERIALIZABLE`: strongest isolation, lowest concurrency.

Databases implement multiversioning and locking differently. Verify actual semantics, deadlock behavior, and retry requirements for the chosen database.

#### 11.14 JDBC Batching and Generated Keys

**Explanation:** Batching reduces database round trips by sending repeated statements together. Partial failure, driver limits, transaction size, and key retrieval remain important correctness concerns.

**Why it matters:** Boundary APIs interact with files, databases, native memory, and dynamic code, where leaks and unsafe input have lasting impact.

```java
try (PreparedStatement statement = connection.prepareStatement(
    "INSERT INTO item(name) VALUES (?)",
    Statement.RETURN_GENERATED_KEYS)) {
  for (String name : names) {
    statement.setString(1, name);
    statement.addBatch();
  }
  int[] counts = statement.executeBatch();
}
```

Batch size affects memory, round trips, transaction duration, and database limits. `BatchUpdateException` can expose partial update counts. Generated-key support and batching behavior vary by driver.

#### 11.15 Annotation Processing

Annotation processors run during compilation and can validate code or generate source/resources. Examples include mapper generators and immutable-value tools.

- Register processors through the service-provider mechanism or build configuration.
- Generated source should be deterministic.
- Separate annotation-processor dependencies from runtime dependencies.
- Incremental builds depend on processors accurately declaring their behavior.
- Generated code should remain inspectable and testable.

#### 11.16 Dynamic Proxies

JDK proxies implement one or more interfaces and route calls through an `InvocationHandler`:

```java
PaymentService proxy = (PaymentService) Proxy.newProxyInstance(
    PaymentService.class.getClassLoader(),
    new Class<?>[] {PaymentService.class},
    (instance, method, arguments) -> {
      log.debug("Calling {}", method.getName());
      return method.invoke(target, arguments);
    });
```

Frameworks use proxies for transactions, security, and interception. Self-invocation may bypass proxy behavior, and checked exceptions from reflection require careful unwrapping.

#### 11.17 Chapter Review and Common Pitfalls

##### Serialization and cloning

- Java native serialization captures an object graph, following non-transient instance references.
- `transient` prevents default field serialization but custom methods can still write the value.
- Deserialization bypasses serializable-class constructors but initializes the first non-serializable superclass.
- `readResolve` and `writeReplace` alter serialized identity and require careful security review.
- Copy constructors and factories communicate copy depth more clearly than `Cloneable`.

##### Reflection

- `getMethods` includes inherited public methods; `getDeclaredMethods` returns members declared directly regardless of visibility.
- Reflective access can trigger class initialization depending on the operation.
- `setAccessible` is constrained by module encapsulation and runtime policy.
- Repeated reflective lookup should be cached only with class-loader lifecycle in mind.
- Invocation wraps target exceptions in `InvocationTargetException`.

##### Annotations

- Annotation elements are limited to primitives, strings, class literals, enums, annotations, and arrays of these.
- Defaults are resolved when read, so changing a default can affect already compiled annotated code.
- Runtime retention increases metadata available to frameworks but does not itself enforce semantics.
- Annotation processors cannot modify existing source syntax directly; they normally validate or generate companion code.

##### Classic I/O

- Byte streams handle binary data; readers/writers handle characters through an encoding.
- Buffering reduces expensive system calls but requires flush/close policy.
- `DataInputStream` and `DataOutputStream` define binary primitive formats, not self-describing schemas.
- `ObjectInputStream` must never be treated as a safe general-purpose parser for untrusted bytes.

##### NIO

- `Path` represents a filesystem-specific path and may be relative.
- `toRealPath` resolves existence and symbolic links; `normalize` is purely lexical.
- File attributes and atomic operations vary by filesystem.
- `WatchService` events can coalesce or overflow; rescan when correctness matters.
- Channels can support position, transfer, gathering, scattering, and memory mapping.

##### JDBC

- JDBC indexes parameters and result columns from 1.
- Prefer column labels over ordinal indexes when query shape changes often.
- A `ResultSet` is tied to its statement/connection lifecycle unless explicitly materialized.
- `setObject` can be driver-dependent; use specific setters for important types.
- Map SQL null with `wasNull` after primitive getters or use suitable object getters.

##### Transactions

- Autocommit commits each statement and can break multi-step invariants.
- Rollback itself can fail; retain the primary exception and attach rollback failure.
- Savepoints permit partial rollback within a transaction.
- Deadlock victims and serialization failures often require bounded whole-transaction retry.
- Never return a connection to the pool with unexpected transaction state.

##### Native and dynamic APIs

- Native calls can crash the process and bypass JVM memory safety.
- Method-handle lookup objects encode access authority; do not expose privileged lookups.
- Dynamic proxies need deliberate handling for `equals`, `hashCode`, and `toString`.
- Service providers should avoid expensive work during discovery; initialize explicitly.


## Part IV: Design, Build, Test, and Platform Evolution

Design principles, build systems, automated testing, modules, and newer Java language/runtime features.

### 12. SOLID Principles and Design Patterns

#### 12.1 Single Responsibility Principle (SRP)

A class should have one reason to change. Keep business rules, persistence, formatting, and transport logic separate.

```java
class InvoiceCalculator {
  BigDecimal total(Invoice invoice) {
    return invoice.items().stream()
        .map(Item::price)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}

class InvoiceRepository {
  void save(Invoice invoice) {
    // persistence logic
  }
}
```

Do not create a single `InvoiceManager` that calculates totals, writes files, sends email, and updates the database.

#### 12.2 Open/Closed Principle (OCP)

Software entities should be open for extension but closed for modification. Prefer adding a new implementation over repeatedly changing a large `if/else` or `switch`.

```java
interface DiscountPolicy {
  BigDecimal apply(BigDecimal amount);
}

class RegularDiscount implements DiscountPolicy {
  public BigDecimal apply(BigDecimal amount) {
    return amount;
  }
}

class PremiumDiscount implements DiscountPolicy {
  public BigDecimal apply(BigDecimal amount) {
    return amount.multiply(new BigDecimal("0.90"));
  }
}
```

#### 12.3 Liskov Substitution Principle (LSP)

A subtype must be usable wherever its parent type is expected without breaking behavior. An override must preserve the parent's contract, including accepted inputs, outputs, and exceptions.

Bad example: a `ReadOnlyFile` extending `File` but throwing `UnsupportedOperationException` from a required `write()` method. Better: use separate `Readable` and `Writable` interfaces.

#### 12.4 Interface Segregation Principle (ISP)

Clients should not depend on methods they do not use. Prefer small capability-based interfaces.

```java
interface Printable { void print(); }
interface Scannable { void scan(); }
interface Faxable { void fax(); }
```

A basic printer can implement only `Printable`; it does not need fake `scan()` or `fax()` methods.

#### 12.5 Dependency Inversion Principle (DIP)

High-level business logic should depend on abstractions, not concrete infrastructure.

```java
interface PaymentGateway {
  PaymentResult charge(Money amount);
}

class CheckoutService {
  private final PaymentGateway gateway;

  CheckoutService(PaymentGateway gateway) {
    this.gateway = gateway;
  }
}
```

Constructor injection makes dependencies explicit and simplifies testing.

#### 12.6 Common Design Patterns

**Explanation:** Design patterns name recurring collaboration structures. Their value is shared vocabulary and a proven trade-off, not the number of patterns present in a codebase.

**Why it matters:** Design techniques are valuable when they reduce a concrete source of coupling or change cost without hiding simple behavior.

- **Strategy:** Select interchangeable behavior at runtime, such as payment or discount policies.
- **Factory Method:** Centralize object creation when construction varies by type.
- **Builder:** Construct complex objects with readable optional parameters.
- **Adapter:** Make an incompatible external API implement an application interface.
- **Decorator:** Add behavior by wrapping an object instead of subclassing it.
- **Observer:** Notify subscribers when state changes; avoid hidden or uncontrolled event chains.
- **Template Method:** Define an algorithm skeleton and let subclasses customize selected steps.
- **Singleton:** One instance per class loader, not necessarily one per process or distributed system. Prefer dependency injection; global mutable state harms testability.

Example of a thread-safe singleton using an enum:

```java
enum ApplicationRegistry {
  INSTANCE;

  private final Map<String, String> values = new ConcurrentHashMap<>();

  void put(String key, String value) {
    values.put(key, value);
  }
}
```

#### 12.7 Pattern Selection and Trade-Offs

Patterns are vocabulary for recurring designs, not goals by themselves.

- Start with the simplest direct design.
- Introduce a pattern when variation, lifecycle, or collaboration has become concrete.
- Prefer explicit dependencies over service locators and hidden global access.
- A pattern that adds more indirection than useful flexibility is over-engineering.
- Document ownership, thread-safety, error behavior, and extension points, not merely the pattern name.

#### 12.8 Additional Useful Patterns

**Explanation:** Command, state, chain, facade, proxy, repository, and unit-of-work patterns solve different variation or coordination problems. Select one only when its forces match the requirement.

**Why it matters:** Design techniques are valuable when they reduce a concrete source of coupling or change cost without hiding simple behavior.

- **Command:** represent an operation as an object; useful for queues, retries, and undo.
- **State:** move state-specific behavior out of large conditionals.
- **Chain of Responsibility:** pass a request through ordered handlers such as filters.
- **Facade:** provide a stable, simplified boundary over a complex subsystem.
- **Proxy:** control access for caching, security, transactions, or remote calls.
- **Repository:** isolate domain logic from persistence queries.
- **Unit of Work:** coordinate a set of persistence changes in one transaction.

#### 12.9 Domain Modeling Guidelines

**Explanation:** Domain modeling represents business concepts, rules, identity, and value directly in code. Good models make invalid states difficult to construct and keep behavior near protected data.

**Why it matters:** Design techniques are valuable when they reduce a concrete source of coupling or change cost without hiding simple behavior.

- Model behavior with the data it protects instead of creating only anemic getters/setters.
- Use value objects for concepts such as `Money`, `EmailAddress`, and `DateRange`.
- Define aggregate boundaries around consistency requirements.
- Keep transport DTOs separate from domain objects when their change cycles differ.
- Make invalid states difficult or impossible to construct.

```java
record Money(BigDecimal amount, Currency currency) {
  Money {
    Objects.requireNonNull(amount);
    Objects.requireNonNull(currency);
    if (amount.scale() > currency.getDefaultFractionDigits()) {
      throw new IllegalArgumentException("Invalid scale");
    }
  }
}
```

#### 12.10 Refactoring Toward SOLID

1. Protect current behavior with tests.
2. Identify the reason a class changes.
3. Extract one responsibility or boundary at a time.
4. Pass dependencies through constructors.
5. Move condition-specific behavior behind a small interface only when multiple implementations are real.
6. Re-run tests and evaluate whether coupling and readability improved.

#### 12.11 Coupling and Cohesion

**Explanation:** Cohesion measures whether responsibilities belong together; coupling measures dependency between modules. Maintainable designs aim for cohesive modules with explicit, stable dependencies.

**Why it matters:** Design techniques are valuable when they reduce a concrete source of coupling or change cost without hiding simple behavior.

- **Cohesion** measures how strongly a module's responsibilities belong together.
- **Coupling** measures how strongly modules depend on one another.

Aim for high cohesion and low, explicit coupling. Instability increases when many modules depend on concrete implementation details. Stable boundaries usually expose domain concepts and hide infrastructure.

#### 12.12 Ports and Adapters

Hexagonal architecture separates application policy from external mechanisms:

- Inbound ports describe use cases.
- Inbound adapters translate HTTP, messaging, or CLI input.
- Outbound ports describe required capabilities such as persistence or payment.
- Outbound adapters implement those capabilities with databases or remote services.

The domain should not need to know which framework invokes it. This improves tests and replaceability but can be excessive for small CRUD applications.

#### 12.13 CQRS and Event Sourcing

**Explanation:** CQRS separates write and read models, while event sourcing persists changes as events. Both add consistency, evolution, replay, and operational complexity that must solve a real need.

**Why it matters:** Design techniques are valuable when they reduce a concrete source of coupling or change cost without hiding simple behavior.

- CQRS separates command models from query models when their needs differ significantly.
- Event sourcing stores state changes as an append-only event history and rebuilds current state by replay.

Benefits can include auditability and independent read optimization. Costs include eventual consistency, schema evolution, replay complexity, idempotency, ordering, and operational tooling. They should address concrete requirements, not fashion.

#### 12.14 Idempotency

An idempotent operation can be repeated without changing the intended result beyond the first successful application.

```java
PaymentResult charge(String idempotencyKey, PaymentRequest request);
```

Store the key, request identity, status, and result atomically. Detect a reused key with different request data. Idempotency is essential when clients may retry after uncertain network failures.

#### 12.15 Anti-Patterns

**Explanation:** An anti-pattern is a commonly repeated design that creates predictable problems. Recognizing one suggests a direction for investigation, not an automatic rewrite.

**Why it matters:** Design techniques are valuable when they reduce a concrete source of coupling or change cost without hiding simple behavior.

- **God object:** owns unrelated responsibilities and excessive state.
- **Service locator:** hides dependencies behind global lookup.
- **Primitive obsession:** represents domain concepts as unrelated strings/numbers.
- **Shotgun surgery:** one logical change requires edits across many modules.
- **Feature envy:** behavior lives away from the data it primarily uses.
- **Inheritance for reuse:** subclasses a type without a valid substitutable relationship.

Refactor only with tests and a clear expected improvement.

#### 12.16 Chapter Review and Common Pitfalls

##### SOLID

- SRP concerns reasons to change, not an arbitrary limit of one method per class.
- OCP is achieved through stable abstractions at actual variation points, not an interface for every class.
- LSP includes behavioral contracts, not only method signatures.
- ISP keeps clients from depending on irrelevant operations.
- DIP directs policy toward abstractions owned near high-level needs.

##### Strategy and factory

- Strategy separates a family of algorithms from the client choosing them.
- Selection logic may live in configuration, a factory, or dependency injection.
- Factories should validate construction and hide unstable implementation names.
- Do not use factories where a direct constructor is clearer and no variation exists.

##### Builder

- Builders are useful for many optional values, staged construction, and immutable results.
- Validate cross-field invariants in `build`, while validating obviously invalid individual values early.
- Reusing a mutable builder can accidentally carry state between builds.
- A builder is not automatically thread-safe.

##### Adapter, decorator, facade, and proxy

- Adapter changes an interface.
- Decorator preserves an interface while adding behavior.
- Facade simplifies access to a subsystem.
- Proxy controls access to another object.
- A wrapper may combine roles, but naming and tests should clarify its contract.

##### Observer and eventing

- Define event ordering, delivery guarantees, error isolation, and unsubscription.
- Synchronous observers extend the publisher's latency and transaction scope.
- Asynchronous observers introduce eventual consistency and retry concerns.
- Listener registration is a common source of memory leaks.

##### Repository and unit of work

- Repositories express domain-oriented retrieval and persistence, not every possible database query.
- Query-specific read models may bypass aggregate repositories when appropriate.
- A unit of work tracks and commits related changes atomically.
- Do not expose lazy persistence proxies beyond the owning session without a clear lifecycle.

##### Architecture

- Boundaries should follow business capability and change patterns, not diagrams alone.
- Dependency direction can be enforced with modules, package rules, and architecture tests.
- Distributed services add network failure, deployment, observability, and consistency costs.
- A modular monolith often provides strong boundaries with lower operational complexity.

##### Idempotency and messaging

- Consumer idempotency requires durable deduplication in the same consistency boundary as the side effect.
- Message acknowledgement should occur only after required durable effects.
- Ordering is usually scoped to a partition/key, not an entire system.
- An outbox pattern can atomically record domain changes and messages in one database transaction.

##### Design review

- State the invariant, owner, lifecycle, and failure model.
- Identify what may vary and what must remain stable.
- Evaluate concurrency, testability, and operational visibility.
- Prefer reversible decisions when requirements are uncertain.
- Remove abstractions that no longer pay for their complexity.

### 13. Maven and Gradle

#### 13.1 Standard Project Layout

**Explanation:** A conventional source layout lets build tools and developers find production code, tests, and resources without custom configuration.

**Why it matters:** A declarative, reproducible build is the foundation for reliable local development, CI, packaging, and releases.

```text
project/
  pom.xml or build.gradle
  src/
    main/
      java/
      resources/
    test/
      java/
      resources/
```

#### 13.2 Maven

Maven uses `pom.xml` and a lifecycle:

- `validate`: check project structure.
- `compile`: compile production code.
- `test`: run unit tests.
- `package`: create JAR/WAR.
- `verify`: run integration checks.
- `install`: place artifact in the local repository.
- `deploy`: publish artifact to a remote repository.
- `clean`: remove generated build output.

Common commands:

```text
mvn clean verify
mvn test
mvn -DskipTests package
mvn dependency:tree
```

Dependency scopes:

- `compile`: available everywhere; default scope.
- `provided`: required to compile but supplied by the runtime, such as a servlet container.
- `runtime`: not required to compile but required at runtime, such as a JDBC driver.
- `test`: only for test compilation and execution.

Use `<dependencyManagement>` to control versions shared by child modules. It declares versions but does not add dependencies by itself.

#### 13.3 Gradle

Gradle uses Groovy or Kotlin build scripts and a task graph.

```groovy
plugins {
  id 'java'
}

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
}

repositories {
  mavenCentral()
}

dependencies {
  testImplementation platform('org.junit:junit-bom:5.11.0')
  testImplementation 'org.junit.jupiter:junit-jupiter'
}

test {
  useJUnitPlatform()
}
```

Common commands:

```text
gradlew clean build
gradlew test
gradlew dependencies
gradlew tasks
```

Always use the Maven Wrapper (`mvnw`) or Gradle Wrapper (`gradlew`) in a project so developers and CI use the intended tool version.

#### 13.4 Dependency Guidance

**Explanation:** Dependencies add APIs, transitive libraries, licenses, vulnerabilities, and upgrade work. Add them deliberately, control versions, and keep credentials outside build files.

**Why it matters:** A declarative, reproducible build is the foundation for reliable local development, CI, packaging, and releases.

- Pin or centrally manage dependency and plugin versions.
- Inspect transitive dependencies before exclusions.
- Keep lockfiles or dependency verification metadata when the build tool supports them.
- Never place credentials directly in build files.
- Run vulnerability scanning in CI and update dependencies deliberately.
- Produce reproducible builds: the same source and inputs should create equivalent output.

#### 13.5 Maven Project Details

The effective POM combines the project POM, parent POMs, active profiles, and Maven defaults:

```text
mvn help:effective-pom
mvn help:active-profiles
```

- Prefer plugin configuration in the build rather than relying on local defaults.
- Use the Maven Enforcer Plugin to require Java/Maven versions and dependency rules.
- Keep release dependencies free of `SNAPSHOT` versions.
- Multi-module reactors build modules in dependency order.
- A BOM imported in `dependencyManagement` aligns a family of library versions.

#### 13.6 Gradle Project Details

**Explanation:** Gradle separates build configuration from task execution and uses lazy providers for efficient builds. Correct input/output declarations enable incremental execution and caching.

**Why it matters:** A declarative, reproducible build is the foundation for reliable local development, CI, packaging, and releases.

- The configuration phase creates the task graph; the execution phase runs selected tasks.
- Avoid doing network or expensive file work during configuration.
- Use lazy `Provider` APIs so values are calculated only when required.
- Version catalogs centralize dependency aliases and versions.
- The build cache reuses outputs when task inputs match.
- The configuration cache reuses configuration state when plugins and scripts are compatible.

#### 13.7 Dependency Resolution

When versions conflict, understand the build tool's selection strategy rather than adding exclusions blindly.

```text
mvn dependency:tree -Dverbose
gradlew dependencyInsight --dependency jackson-databind
```

- Direct dependencies should describe APIs the source actually uses.
- Transitive dependencies are implementation details of another dependency and can change.
- Maven's nearest-definition behavior and Gradle's conflict resolution differ.
- Test the packaged artifact, not only IDE execution, to catch missing runtime dependencies.

#### 13.8 Build Reproducibility and CI

**Explanation:** A reproducible build produces equivalent output from declared inputs. CI should use wrappers, pinned plugins, controlled repositories, and clean verification before publishing.

**Why it matters:** A declarative, reproducible build is the foundation for reliable local development, CI, packaging, and releases.

- Use toolchains to select the compiler independently of the JVM running the build.
- Pin plugin versions.
- Normalize archive timestamps where byte-for-byte output matters.
- Separate unit and integration-test phases.
- Cache immutable dependency downloads, not mutable build outputs without correct keys.
- Publish checksums and software bills of materials for released artifacts.
- Run clean builds periodically so stale output cannot hide missing generated files.

#### 13.9 Maven Lifecycle Customization

Plugins bind goals to lifecycle phases:

```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-compiler-plugin</artifactId>
  <version>...</version>
  <configuration>
    <release>21</release>
  </configuration>
</plugin>
```

- Surefire conventionally runs unit tests in `test`.
- Failsafe conventionally runs integration tests in `integration-test` and checks results in `verify`.
- Put shared plugin versions under `pluginManagement`.
- Profiles should model genuine environment variation, not make ordinary builds unpredictable.

#### 13.10 Gradle Task Modeling

A well-modeled task declares:

- Input files, properties, and classpath.
- Output files or directories.
- External services and environment inputs that affect results.

Correct declarations enable incremental execution and caching. Avoid reading undeclared environment state inside task actions.

#### 13.11 Multi-Module Design

**Explanation:** Modules should represent coherent responsibilities with acyclic dependency direction. Splitting too finely increases build and versioning overhead; splitting too little weakens boundaries.

**Why it matters:** A declarative, reproducible build is the foundation for reliable local development, CI, packaging, and releases.

- Split modules by independently understandable responsibility, not arbitrary technical layers.
- Keep dependency direction acyclic.
- Avoid one giant shared module that every component depends on.
- Publish stable interfaces separately only when versioning or reuse requires it.
- Use composite builds or included builds when independent projects need coordinated local development.

#### 13.12 Dependency Hygiene

**Explanation:** Dependency hygiene means declaring direct use accurately, limiting exposure, removing unused libraries, and reviewing provenance, licenses, and security.

**Why it matters:** A declarative, reproducible build is the foundation for reliable local development, CI, packaging, and releases.

- Declare a dependency where source directly uses it.
- In Gradle, distinguish `api` from `implementation` to control transitive compile exposure.
- Avoid dynamic versions such as `1.+` in reproducible builds.
- Verify repository order and prevent accidental dependency substitution from untrusted repositories.
- Review licenses and provenance in addition to vulnerabilities.
- Remove dependencies that duplicate small JDK capabilities only after considering compatibility and maintenance.

#### 13.13 Release Artifacts

A release process may produce:

- Main JAR.
- Source and Javadoc JARs.
- Checksums and signatures.
- SBOM and provenance metadata.
- Container image or runtime image.

Verify the exact artifact by launching it in a clean environment. Tag source only after build inputs and artifact identity are known.

#### 13.14 Chapter Review and Common Pitfalls

##### Build lifecycle

- A clean build should derive outputs solely from declared source, configuration, dependencies, and tools.
- Build phases/tasks should fail immediately on compiler, test, static-analysis, or packaging errors.
- Generated code and resources need explicit inputs and outputs.
- Local IDE builds must not be the only way to produce a release artifact.

##### Maven

- Maven coordinates are `groupId:artifactId:packaging:classifier:version`.
- Parent inheritance and dependency management solve different problems.
- Optional dependencies are not automatically propagated to consumers.
- Exclusions are per dependency path and can hide runtime requirements.
- `mvn verify` is generally a stronger CI target than `package`.

##### Gradle

- `implementation` hides a dependency from consumers' compile classpaths; `api` exposes it.
- Task avoidance APIs such as `register` prevent eager configuration.
- Daemons improve repeated build speed but should not hide undeclared environmental inputs.
- Dependency locking records selected versions, while verification checks artifact identity.
- Custom tasks should use typed properties and lazy providers.

##### Repositories

- Repository order can affect which artifact is selected.
- Avoid broad content access to plugin or snapshot repositories.
- Internal mirrors can improve control and availability but need integrity and retention policies.
- Never silently fall back to an unexpected public repository for private coordinates.

##### Versioning

- Semantic versioning communicates intent but does not automatically ensure compatibility.
- Snapshot/dynamic versions make historical builds difficult to reproduce.
- Dependency convergence prevents incompatible versions of shared libraries from entering one runtime.
- A BOM aligns versions but does not prove those versions are compatible with application usage.

##### Plugins

- Build plugins execute trusted code with developer/CI permissions.
- Pin versions and review configuration changes.
- Keep plugin dependencies separate from application runtime dependencies.
- Generated reports should not leak environment secrets.

##### Toolchains

- Toolchains allow compilation/testing on designated JDKs independent of the launcher JDK.
- Cross-compilation needs `--release`, not only a matching compiler.
- Test on every supported runtime where behavior or linkage can differ.
- Record vendor and architecture for platform-specific failures.

##### Multi-module builds

- Avoid cyclic module dependencies.
- A module should publish a coherent API and hide internals.
- Shared test fixtures should be intentional dependencies, not copied source.
- Build only affected modules when optimization is correct, but retain full clean verification before release.

##### CI and releases

- CI should start from a controlled environment and preserve test reports and diagnostics.
- Release artifacts should be immutable once published.
- Sign and checksum artifacts where distribution requires trust verification.
- Releasing should be automated, auditable, and repeatable without a developer workstation.

### 14. Testing with JUnit and Mockito

#### 14.1 Testing Pyramid

**Explanation:** The testing pyramid favors many fast unit tests, fewer integration tests, and a small number of end-to-end tests because speed and failure localization decrease toward the top.

**Why it matters:** Tests are useful only when they provide trustworthy, maintainable evidence about behavior and integration.

- **Unit tests:** Fast and isolated; test one unit of behavior.
- **Integration tests:** Verify database, network, filesystem, framework, or multiple components together.
- **End-to-end tests:** Exercise the deployed system through its public interface; fewer because they are slower and more fragile.

Good tests follow Arrange-Act-Assert and describe behavior rather than implementation.

#### 14.2 JUnit 5

**Explanation:** JUnit provides test discovery, lifecycle callbacks, assertions, parameterized tests, and extensions. A test should describe observable behavior and remain independent of execution order.

**Why it matters:** Tests are useful only when they provide trustworthy, maintainable evidence about behavior and integration.

```java
class CalculatorTest {
  private Calculator calculator;

  @BeforeEach
  void setUp() {
    calculator = new Calculator();
  }

  @Test
  @DisplayName("adds two positive numbers")
  void addsPositiveNumbers() {
    int result = calculator.add(2, 3);

    assertEquals(5, result);
  }

  @ParameterizedTest
  @CsvSource({"2, 3, 5", "-1, 1, 0", "0, 0, 0"})
  void addsValues(int left, int right, int expected) {
    assertEquals(expected, calculator.add(left, right));
  }
}
```

Useful assertions:

```java
assertEquals(expected, actual);
assertTrue(condition);
assertNull(value);
assertAll(
    () -> assertEquals("Ali", employee.name()),
    () -> assertEquals(10, employee.id())
);
IllegalArgumentException error =
    assertThrows(IllegalArgumentException.class, () -> service.find(-1));
assertTimeout(Duration.ofMillis(100), () -> service.calculate());
```

Test lifecycle:

- `@BeforeEach` / `@AfterEach`: run around every test.
- `@BeforeAll` / `@AfterAll`: run once per class; normally static.
- `@Nested`: organize related scenarios.
- `@Disabled`: temporarily skip a test; include a reason.

#### 14.3 Mockito

Mock external collaborators, not simple value objects or the class under test.

```java
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
  @Mock PaymentGateway gateway;
  @Mock OrderRepository repository;
  @InjectMocks OrderService service;

  @Test
  void savesOrderAfterSuccessfulPayment() {
    Order order = new Order(100);
    when(gateway.charge(order.total())).thenReturn(PaymentResult.success());

    service.place(order);

    verify(repository).save(order);
    verifyNoMoreInteractions(repository);
  }
}
```

Key methods:

- `when(...).thenReturn(...)`: stub a result.
- `when(...).thenThrow(...)`: stub an error.
- `verify(...)`: check an interaction.
- `ArgumentCaptor<T>`: inspect an argument sent to a collaborator.
- `doThrow(...)`: useful for stubbing void methods.

Avoid over-mocking, verifying every private detail, sleeping in tests, shared mutable fixtures, and tests that depend on execution order.

#### 14.4 Test Quality

**Explanation:** A valuable test is deterministic, readable, behavior-focused, and sensitive to meaningful regressions. Coverage alone cannot show whether assertions verify the right outcome.

**Why it matters:** Tests are useful only when they provide trustworthy, maintainable evidence about behavior and integration.

- Test normal cases, boundaries, invalid input, and failure paths.
- Keep tests deterministic: control clocks, randomness, threads, and external services.
- Inject `Clock` instead of calling `LocalDateTime.now()` directly when time affects behavior.
- Use temporary directories for file tests.
- Prefer realistic integration tests for SQL queries and serialization contracts.
- Treat coverage as a signal, not the goal; assertions must verify meaningful behavior.

#### 14.5 Test Doubles

**Explanation:** Test doubles replace collaborators for a test purpose: stubs return data, mocks verify interactions, fakes implement simplified behavior, and spies observe real behavior.

**Why it matters:** Tests are useful only when they provide trustworthy, maintainable evidence about behavior and integration.

- **Dummy:** passed but never used.
- **Stub:** returns prepared data.
- **Spy:** records calls and may wrap real behavior.
- **Mock:** verifies expected interactions.
- **Fake:** lightweight working implementation, such as an in-memory repository.

Prefer state-based assertions when observable output is enough. Interaction verification is valuable at true boundaries, but excessive verification tightly couples tests to implementation.

#### 14.6 Test Data and Fixtures

**Explanation:** Fixtures create the state needed for a scenario. Good fixtures use valid defaults, expose relevant differences, and avoid shared mutable state.

**Why it matters:** Tests are useful only when they provide trustworthy, maintainable evidence about behavior and integration.

- Use builders or object mothers for readable valid defaults.
- Override only values relevant to the scenario.
- Do not share mutable fixture instances between tests.
- Give test data domain meaning rather than arbitrary values.
- Seed random generators and report the seed on failure.

```java
Order order = OrderBuilder.anOrder()
    .withCustomer("customer-123")
    .withTotal(new BigDecimal("49.99"))
    .build();
```

#### 14.7 Integration Testing

Integration tests should exercise real boundaries where compatibility matters:

- Database schema, queries, constraints, and transactions.
- HTTP serialization and status/error contracts.
- Messaging acknowledgements and redelivery.
- Filesystem permissions and atomicity assumptions.
- Framework dependency injection and configuration.

Use isolated databases or containers with deterministic setup and cleanup. Do not replace every integration boundary with mocks and then assume integration works.

#### 14.8 Asynchronous and Concurrent Tests

**Explanation:** Concurrent tests should coordinate events explicitly and assert eventual outcomes with deadlines. Sleeping guesses at scheduling and creates slow, flaky tests.

**Why it matters:** Tests are useful only when they provide trustworthy, maintainable evidence about behavior and integration.

- Prefer latches, futures, and await utilities over `Thread.sleep`.
- Set an upper timeout so failures terminate.
- Assert eventual outcomes without depending on one scheduler ordering.
- Repeat stress scenarios when testing race-prone code.
- A test that passes once does not prove absence of a data race; design using happens-before rules.

#### 14.9 Mutation, Property, and Contract Testing

**Explanation:** These techniques test test-suite strength, broad input invariants, or cross-system agreements. They complement focused example tests rather than replacing them.

**Why it matters:** Tests are useful only when they provide trustworthy, maintainable evidence about behavior and integration.

- Mutation testing changes operators or branches to check whether tests detect behavior changes.
- Property-based testing generates many inputs and verifies invariants.
- Contract tests verify that providers and consumers agree on an interface.
- Snapshot tests are useful for stable structured output but require careful review of updates.

Use these techniques to supplement clear example tests, not replace them.

#### 14.10 Test Naming and Structure

Names should communicate scenario and expected behavior:

```java
@Test
void rejectsTransferWhenBalanceIsInsufficient() {}
```

One test may contain multiple assertions about one behavior. Avoid forcing one assertion per test when it fragments the scenario, but use `assertAll` when seeing all mismatches together helps.

#### 14.11 Boundary-Value Analysis

For a range, test:

- Minimum accepted value.
- Just below minimum.
- Maximum accepted value.
- Just above maximum.
- Representative middle value.
- Empty, null, malformed, or duplicate input where applicable.

Equivalence partitioning reduces redundant cases by selecting representatives from inputs expected to behave alike.

#### 14.12 Database Test Transactions

Rolling each test back is convenient but can hide:

- Commit-time constraint failures.
- Transaction synchronization behavior.
- Visibility from other connections.
- Lock duration and isolation issues.

Use rollback tests for speed where appropriate, plus explicit commit-based integration tests for transaction semantics.

#### 14.13 Mockito Strictness

Strict stubbing detects unused setup and argument mismatch. Prefer:

- Exact or meaningful argument matchers.
- Minimal stubbing for the scenario.
- Real objects for simple values.
- Fakes for complex stateful collaboration.

Do not mix raw values and matchers in the same invocation unless the raw values use `eq`.

#### 14.14 Flaky-Test Diagnosis

Common causes:

- Shared mutable state.
- Time-zone or locale dependence.
- Fixed ports and files.
- Uncontrolled clocks or random values.
- Races and arbitrary sleeps.
- External service dependence.
- Test-order assumptions.

Quarantine may temporarily protect the build, but assign ownership and fix or remove the test promptly. Re-running until green conceals reliability problems.

#### 14.15 Testing Exceptions and Logs

Assert exception type, stable message elements, and structured fields rather than full stack traces. Test logs only when logging is contractual, such as audit events; otherwise assert the behavior that caused the log.

#### 14.16 Chapter Review and Common Pitfalls

##### Test scope

- A unit is a behavioral boundary, not necessarily one class.
- Integration tests verify collaboration with real infrastructure or framework behavior.
- End-to-end tests provide confidence in deployment wiring but give slower, less localized feedback.
- Contract and component tests can cover useful middle layers.

##### JUnit lifecycle

- JUnit creates a new test instance per method by default.
- Per-class lifecycle allows non-static `@BeforeAll` but introduces shared mutable-state risk.
- Extension ordering and inheritance can affect setup behavior.
- Test discovery relies on the configured engine and build plugin.

##### Assertions

- Put expected before actual for conventional failure messages.
- Supply assertion-message lambdas for expensive diagnostic construction.
- Compare floating point with an appropriate delta or domain rule.
- Assert collections with order-sensitive or order-insensitive semantics intentionally.
- Avoid assertions that merely repeat the implementation.

##### Parameterized and dynamic tests

- Parameterized tests share one behavior over data cases.
- Give argument sets readable names.
- Method sources can provide complex objects and boundary cases.
- Dynamic tests are generated at runtime but have different lifecycle behavior from ordinary test methods.

##### Mockito

- Stubbing should model collaborator contracts, including relevant failures.
- `verifyNoMoreInteractions` can make harmless implementation changes brittle; use only when extra calls are behaviorally wrong.
- Spies execute real methods unless stubbed with `doReturn`/`doThrow` forms.
- Deep stubs hide design problems and should be exceptional.
- Do not mock value types, collections, or code whose real implementation is simpler.

##### Time and randomness

- Inject `Clock`, ID generators, and random sources.
- Test daylight-saving boundaries when local scheduling matters.
- Avoid freezing global system time through invasive static hooks when a normal dependency works.
- Property tests should persist minimal failing examples and seeds.

##### Concurrency tests

- Coordinate starting points with barriers/latches to increase race exposure.
- Assert both safety properties and eventual completion.
- Use timeouts as upper bounds, not sleep as scheduling.
- Run stress tests separately from fast deterministic unit suites.

##### Integration tests

- Schema migrations should run the same way as production.
- Clean data through isolated schemas, transactions, or disposable environments.
- Stub only systems outside the integration boundary.
- Verify failure modes such as unavailable services, constraint violations, and timeout behavior.

##### Test maintainability

- A failing test should explain what behavior regressed.
- Keep setup close to the relevant scenario.
- Remove obsolete tests when behavior is intentionally removed.
- Review test-code quality with production-code standards.
- Track suite duration and flaky rates as engineering metrics.

### 15. Java Platform Module System

#### 15.1 Named, Automatic, and Unnamed Modules

**Explanation:** Named modules have descriptors, automatic modules adapt ordinary JARs on the module path, and the unnamed module contains classpath code. Their readability and encapsulation differ.

**Why it matters:** Module declarations affect compilation, runtime resolution, reflection, packaging, and compatibility.

- A **named module** contains `module-info.class`.
- An **automatic module** is a non-modular JAR placed on the module path; its name comes from `Automatic-Module-Name` or the JAR file.
- The **unnamed module** contains classpath code and reads all observable modules.

Automatic modules ease migration but expose all packages and have less reliable naming unless the manifest defines it.

#### 15.2 Strong Encapsulation

**Explanation:** A module can expose public APIs while keeping other packages inaccessible even when their classes are public. Reflection requires deliberate openness.

**Why it matters:** Module declarations affect compilation, runtime resolution, reflection, packaging, and compatibility.

`exports` allows normal compiled access to public types. `opens` allows deep reflection. They solve different problems:

```java
module com.example.orders {
  exports com.example.orders.api;
  opens com.example.orders.dto to com.fasterxml.jackson.databind;
}
```

Qualified exports or opens grant access only to listed modules. Avoid opening every package merely to silence reflective-access failures.

#### 15.3 Compilation and Execution

**Explanation:** Modular compilation and launch use a module path and module names rather than only a classpath and main class. The descriptor becomes part of dependency resolution.

**Why it matters:** Module declarations affect compilation, runtime resolution, reflection, packaging, and compatibility.

```text
javac -d out --module-source-path src -m com.example.app
java --module-path out -m com.example.app/com.example.app.Main
```

Useful analysis tools:

```text
jdeps --recursive app.jar
jar --describe-module --file library.jar
jlink --module-path out --add-modules com.example.app --output runtime
```

`jlink` creates a custom runtime image containing selected modules and dependencies. It is useful for controlled deployments but must be rebuilt for security updates.

#### 15.4 Migration Strategy

1. Remove dependencies on JDK internals.
2. Give published JARs stable automatic module names.
3. Resolve split packages and cyclic dependencies.
4. Add descriptors to libraries from the leaves upward.
5. Open only packages that frameworks need for reflection.
6. Test both modular packaging and runtime launch commands.

#### 15.5 Services Across Modules

The service-provider API decouples consumers from implementations:

```java
module com.example.checkout {
  uses com.example.payment.PaymentProvider;
}

module com.example.card {
  requires com.example.payment;
  provides com.example.payment.PaymentProvider
      with com.example.card.CardProvider;
}
```

Providers need an accessible provider constructor or provider method according to service-loading rules. Handle absent, duplicate, or misconfigured providers explicitly.

#### 15.6 Reflection and Modules

Named modules strongly encapsulate non-exported packages. Reflective frameworks may require:

- Targeted `opens` in `module-info.java`.
- Command-line `--add-opens` during migration.
- Framework support that avoids deep reflection.

`--add-opens` and `--add-exports` are deployment escape hatches, not ideal permanent library contracts.

#### 15.7 Module Layers

A `ModuleLayer` can load additional module configurations at runtime, useful for plugin systems. Each layer can use distinct class loaders and service providers.

This flexibility adds class-identity, lifecycle, and unloading complexity. Define strict plugin APIs and prevent plugins from depending on application internals.

#### 15.8 Modular JARs and Multi-Release JARs

**Explanation:** A modular JAR declares a module; a multi-release JAR supplies runtime-specific implementations. Both add packaging compatibility obligations across supported JDKs.

**Why it matters:** Module declarations affect compilation, runtime resolution, reflection, packaging, and compatibility.

- A modular JAR contains `module-info.class`.
- A multi-release JAR can provide version-specific classes under `META-INF/versions/<n>`.
- The base classes must support the minimum runtime.
- Versioned implementations should preserve the same public API.

Test every supported runtime because only that runtime selects its relevant entries.

#### 15.9 JPMS Limitations and Decisions

JPMS provides reliable configuration and strong encapsulation, but it is not a security sandbox. It does not replace process isolation, authorization, or OS permissions.

Libraries should consider module compatibility even when applications remain on the classpath. Applications should adopt modules when encapsulation, custom runtime images, or explicit dependency graphs justify migration cost.

#### 15.10 Chapter Review and Common Pitfalls

##### Descriptors

- `module-info.java` compiles to `module-info.class` at the JAR root.
- A module name should be globally stable and normally follow reverse-domain naming.
- Modules contain packages; the same package cannot be split across named modules in one configuration.
- The descriptor is part of the module's public compatibility surface.

##### Readability and accessibility

- Readability determines whether one module can refer to another.
- Accessibility additionally requires the package to be exported and the member to be public.
- `requires static` is mandatory at compile time but optional at runtime.
- `requires transitive` exposes a dependency through the requiring module's API graph.

##### Exports and opens

- `exports` supports normal access to public members.
- `opens` supports deep reflection into package members.
- `open module` opens all packages and weakens encapsulation broadly.
- Qualified exports/opens reduce exposure to selected friend modules but increase coupling.

##### Services

- Service APIs should be stable, small, and independent of provider implementation.
- Provider discovery is lazy, and instantiation failures can occur during iteration.
- Providers can be reloaded, but lifecycle and duplicate handling remain application responsibilities.
- Module layers allow different provider sets in plugin scenarios.

##### Automatic modules

- A filename-derived automatic module name can change when artifact naming changes.
- Libraries can publish `Automatic-Module-Name` before becoming fully modular.
- Automatic modules read all named modules and export all packages, easing migration but reducing encapsulation.
- Two automatic modules with derived-name collisions cannot coexist.

##### Migration

- `jdeps --jdk-internals` identifies dependencies on unsupported JDK internals.
- Split packages often require package relocation or module restructuring.
- Reflection failures should be fixed with targeted openness rather than broad command-line access.
- Test libraries on both classpath and module path when supporting both deployment styles.

##### Runtime images

- `jlink` resolves modules into a platform-specific runtime image.
- Images can exclude unused modules, man pages, headers, and debug information.
- An image is not portable across operating systems or architectures.
- Rebuild images when the JDK receives security updates.

##### Compatibility

- Removing an exported package or required module can break consumers.
- Adding a `uses` declaration is usually internal; changing provided services can alter discovery.
- Module boundaries do not prevent reflection into explicitly opened packages.
- JPMS strengthens encapsulation but does not enforce semantic versioning.

### 16. Modern Java Features

#### 16.1 `var` for Local Variables (Java 10)

**Explanation:** `var` asks the compiler to infer one static local-variable type from the initializer. It reduces repetition but should not hide an important abstraction.

**Why it matters:** Modern features improve modeling and scalability but can also raise runtime requirements and migration risk.

```java
var names = new ArrayList<String>(); // inferred as ArrayList<String>
var total = calculateTotal();        // inferred from return type
```

`var` is not dynamic typing. The compiler still assigns one static type. It works only for local variables with an initializer, enhanced-for variables, and lambda parameters. Avoid it when the inferred type is unclear.

#### 16.2 Helpful NullPointerExceptions (Java 14)

The JVM can identify which part of a chained expression was null. This improves diagnostics but does not replace input validation or null-safe design.

#### 16.3 Records (Final in Java 16)

Records model transparent, shallowly immutable data:

```java
record User(int id, String name) {
  User {
    if (id <= 0) throw new IllegalArgumentException("id must be positive");
    Objects.requireNonNull(name, "name");
  }
}
```

Record components are final references, but referenced mutable objects are still mutable. Make defensive copies when required:

```java
record Team(List<String> members) {
  Team {
    members = List.copyOf(members);
  }
}
```

#### 16.4 Sealed Types (Final in Java 17)

**Explanation:** A sealed type explicitly controls its direct subtypes. This documents a closed domain and enables exhaustive processing while still allowing selected branches to reopen extension.

**Why it matters:** Modern features improve modeling and scalability but can also raise runtime requirements and migration risk.

```java
sealed interface Result permits Success, Failure {}
record Success(String value) implements Result {}
record Failure(String message) implements Result {}
```

Permitted implementations must be `final`, `sealed`, or `non-sealed`. Sealed hierarchies work well with exhaustive pattern matching.

#### 16.5 Pattern Matching for `switch` (Final in Java 21)

**Explanation:** Pattern switches combine type testing, variable binding, guards, and exhaustive result selection. Case order and dominance determine which compatible pattern is chosen.

**Why it matters:** Modern features improve modeling and scalability but can also raise runtime requirements and migration risk.

```java
static String describe(Object value) {
  return switch (value) {
    case null -> "null";
    case Integer i when i > 0 -> "positive integer";
    case Integer i -> "integer: " + i;
    case String s -> "text: " + s;
    default -> "other";
  };
}
```

Case order matters when one pattern dominates another. Exhaustive switches over enums and sealed hierarchies reduce missing-case bugs.

#### 16.6 Virtual Threads (Final in Java 21)

Virtual threads are lightweight JVM-managed threads suited to high-throughput tasks that spend much of their time blocked on I/O.

```java
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
  Future<String> user = executor.submit(() -> loadUser());
  Future<String> orders = executor.submit(() -> loadOrders());
  System.out.println(user.get() + orders.get());
}
```

Guidance:

- Write straightforward blocking code; do not pool virtual threads to limit concurrency.
- Limit access to scarce resources with semaphores or connection pools.
- They do not make CPU-bound work faster; CPU work is still limited by available cores.
- Avoid long blocking operations while holding `synchronized` monitors, especially on older JDK 21 implementations where pinning can reduce scalability.
- Measure before and after migration.

#### 16.7 Sequenced Collections (Java 21)

**Explanation:** Sequenced collection interfaces provide uniform first, last, and reversed operations for data with a defined encounter order.

**Why it matters:** Modern features improve modeling and scalability but can also raise runtime requirements and migration risk.

`SequencedCollection`, `SequencedSet`, and `SequencedMap` provide a uniform API for ordered collections:

```java
SequencedCollection<String> names = new ArrayList<>();
names.addFirst("A");
names.addLast("B");
String first = names.getFirst();
SequencedCollection<String> reversed = names.reversed();
```

#### 16.8 Switch Expressions and Text Blocks

**Explanation:** Switch expressions return values without accidental fall-through, while text blocks represent multiline text with less escaping. Both improve clarity without changing core type safety.

**Why it matters:** Modern features improve modeling and scalability but can also raise runtime requirements and migration risk.

```java
String label = switch (status) {
  case NEW -> "New";
  case ACTIVE -> "Active";
  case CLOSED -> "Closed";
};

String json = """
    {
      "name": "Ali",
      "active": true
    }
    """;
```

Switch expressions must produce a value for every possible path. Use `yield` from a multi-statement case block.

#### 16.9 Feature Lifecycle and Compatibility

Java features may be permanent, preview, incubating, or experimental:

- Preview language/API features require `--enable-preview` at compile and run time and may change between releases.
- Incubator modules are non-final APIs that must be added explicitly.
- Experimental JVM features may require flags and are not compatibility commitments.

Compile with the correct release target:

```text
javac --release 17 Main.java
```

`--release` constrains language features, bytecode level, and documented JDK APIs together. Setting only `-source` and `-target` does not prevent accidental use of newer library APIs.

#### 16.10 Pattern Matching Design

Patterns improve data-oriented branching but should not replace polymorphism automatically.

- Use polymorphism when behavior naturally belongs to each subtype.
- Use a pattern switch when an operation belongs to the consumer and the hierarchy is closed.
- Guarded cases should appear before broader cases.
- Exhaustive sealed-type switches make new subtype additions visible as compile errors.

#### 16.11 Virtual Threads vs Reactive Programming

Virtual threads simplify high-concurrency blocking code and stack traces. Reactive APIs remain useful when:

- End-to-end libraries are already non-blocking.
- Streaming backpressure is central.
- The application composes event streams rather than request-per-task workflows.

Do not mix models casually. Blocking inside an event-loop thread can stall many requests, while wrapping every trivial call in a virtual thread adds complexity without benefit.

#### 16.12 New Collection and Stream Conveniences

Modern JDKs include useful additions such as:

- `List.of`, `Set.of`, `Map.of` for compact immutable collections.
- `Stream.toList()` for an unmodifiable encounter-ordered list.
- `Collectors.teeing` to combine two downstream reductions.
- `Stream.mapMulti` for one-to-many mapping without creating a stream for each element.
- `Optional.stream` to integrate optional values into pipelines.

Check the exact minimum JDK version before adopting an API in a shared library.

#### 16.13 Record Patterns

Record patterns destructure record values and can nest:

```java
record Point(int x, int y) {}
record Line(Point start, Point end) {}

static int startX(Object value) {
  return switch (value) {
    case Line(Point(int x, int y), Point end) -> x;
    default -> 0;
  };
}
```

They work well with sealed algebraic data models. Keep patterns readable; deeply nested destructuring can obscure intent.

#### 16.14 Unnamed Variables and Patterns

Modern Java permits `_` in selected declarations where a value is intentionally unused:

```java
try {
  perform();
} catch (ExpectedException _) {
  recover();
}
```

This documents intentional non-use and prevents accidental access. Confirm the project's Java version and preview/final status before use.

#### 16.15 Foreign Function and Memory API

The Foreign Function and Memory API provides supported access to native libraries and off-heap memory without much of JNI's boilerplate.

Core concepts include:

- `Arena` for memory-segment lifetime.
- `MemorySegment` for bounded memory access.
- `Linker` and function descriptors for native calls.
- Layouts and variable handles for structured data.

Native interaction remains unsafe at the system boundary: signatures, ownership, thread rules, and library compatibility must be exact.

#### 16.16 Scoped Values

Scoped values provide immutable context inherited through a bounded dynamic scope and are designed as a safer alternative to many `ThreadLocal` use cases, especially with virtual threads.

They are useful for request metadata such as trace identity, not for mutable global state. Check whether the feature is preview or final in the exact target JDK and compile accordingly.

#### 16.17 API Evolution Awareness

When using modern APIs:

- Check the minimum JDK release.
- Check whether a feature requires preview flags.
- Avoid exposing preview types in stable public APIs.
- Consider runtime vendors and deployment tooling.
- Use multi-release JARs only when one artifact truly needs optimized per-JDK implementations.
- Document fallback behavior for older supported runtimes.

#### 16.18 Chapter Review and Common Pitfalls

##### `var`

- `var` infers the static type of an initializer; it does not create a union, dynamic, or structural type.
- It cannot initialize from bare null, omit an initializer, or declare fields/method parameters.
- Use it when the initializer makes the type obvious or the explicit generic type is distracting.
- Avoid it when interface abstraction is important or the initializer hides the meaningful type.

##### Switch expressions and patterns

- Arrow cases do not fall through.
- Colon-style groups remain available and require `yield` when a block returns a value.
- Pattern dominance is checked at compile time.
- Exhaustiveness can be affected when separately compiled sealed hierarchies evolve.
- A `case null` is explicit; otherwise switching on null normally throws `NullPointerException`.

##### Records

- The canonical constructor has parameters corresponding to every component.
- A compact constructor implicitly assigns validated/rebound parameters after its body.
- Record equality requires the same record type and equal components.
- Arrays as components use reference equality unless custom methods override generated behavior.
- Records are best for transparent data, not types whose representation must remain hidden.

##### Sealed types

- Permitted subtypes must be accessible in the same module, or same package in the unnamed module.
- Sealing documents and enforces a closed extension boundary.
- Framework proxying/subclassing may conflict with final or sealed models.
- A sealed interface can model alternatives without forcing shared implementation state.

##### Virtual threads

- Virtual-thread scheduling is managed by the JDK over a smaller set of carrier threads.
- Blocking JDK operations generally unmount the virtual thread when possible.
- Native calls and some monitor-held blocking may pin carriers depending on the JDK.
- Use thread dumps and JFR events designed for large virtual-thread populations.
- Preserve request deadlines and resource limits despite cheap thread creation.

##### Sequenced collections

- Sequenced APIs unify first, last, and reversed views across ordered collection types.
- A reversed view is generally backed by the original collection.
- Mutation support follows the underlying collection.
- Encounter order remains a semantic choice; do not impose it where a set/map intentionally has none.

##### Foreign memory

- An arena defines lifetime and thread-access rules for its segments.
- Bounds and temporal checks improve safety over raw native pointers but cannot validate external native code.
- Downcalls must match ABI layouts and calling conventions.
- Native resources should be scoped as narrowly as possible.

##### Preview features

- Source using preview features must compile with the matching release and `--enable-preview`.
- Runtime execution also requires `--enable-preview`.
- Class files using preview features are intentionally tied to that feature release.
- Do not publish stable libraries whose public contracts depend on preview APIs without a clear compatibility policy.

##### Migration

- Upgrade libraries, build plugins, agents, and observability tools before changing production JDKs.
- Run tests with illegal-access, locale, time-zone, TLS, and GC differences in mind.
- Compare performance after warmup under equivalent resource limits.
- Review removed/deprecated APIs and changed defaults in release notes.


## Part V: Production, Security, and Operations

Reliability, secure coding, observability, performance engineering, troubleshooting, and revision guidance.

### 17. Production Java Best Practices

#### 17.1 API and Object Design

**Explanation:** A production API is a contract covering inputs, outputs, nullability, errors, ownership, mutability, concurrency, and compatibility—not only a method signature.

**Why it matters:** Production systems operate under partial failure, overload, restarts, and mixed versions rather than ideal local conditions.

- Validate constructor and method inputs at boundaries.
- Prefer immutable objects for values shared across threads.
- Use records for data carriers when their semantics fit.
- Prefer composition over inheritance unless there is a genuine substitutable IS-A relationship.
- Return empty collections rather than `null`.
- Use `Optional` primarily as a return type, not for every field or parameter.
- Avoid exposing mutable internal collections; return `List.copyOf(...)` or an unmodifiable view as appropriate.
- Program to interfaces when multiple implementations or test doubles are expected.

#### 17.2 Resource Management

Use try-with-resources for every `AutoCloseable`:

```java
try (InputStream input = Files.newInputStream(path);
     BufferedInputStream buffered = new BufferedInputStream(input)) {
  return buffered.readAllBytes();
}
```

Resources close in reverse declaration order. If both the body and `close()` fail, the close error becomes a suppressed exception accessible through `getSuppressed()`.

#### 17.3 Exception Design

**Explanation:** Production exception handling should preserve root causes, translate at boundaries, avoid duplicate logs, and support cancellation and recovery policies.

**Why it matters:** Production systems operate under partial failure, overload, restarts, and mixed versions rather than ideal local conditions.

- Catch exceptions only where you can recover, add useful context, or translate them at an abstraction boundary.
- Preserve the cause: `throw new OrderException("Cannot load order " + id, cause);`
- Never catch `Throwable` for normal application handling.
- Do not use exceptions for expected control flow.
- Log an exception once at the boundary that handles it; avoid duplicate logs at every layer.
- Never ignore interrupted status:

```java
try {
  queue.take();
} catch (InterruptedException e) {
  Thread.currentThread().interrupt();
  return;
}
```

#### 17.4 Money, Time, and Equality

**Explanation:** Money, timestamps, local dates, and identity have domain-specific semantics. Choosing the right types prevents rounding, time-zone, and collection-key defects.

**Why it matters:** Production systems operate under partial failure, overload, restarts, and mixed versions rather than ideal local conditions.

- Use `BigDecimal` for decimal money calculations, not `double`.
- Construct decimals from strings: `new BigDecimal("0.10")`, not `new BigDecimal(0.10)`.
- Specify rounding explicitly when division may be non-terminating.
- Store machine timestamps as `Instant`; convert to a user time zone at the boundary.
- Use `LocalDate` for date-only values such as birthdays.
- Keep fields used by `equals()` and `hashCode()` stable while an object is a `HashMap` key or `HashSet` member.

#### 17.5 Logging

**Explanation:** Logs are structured diagnostic events for people and systems. They need consistent levels, correlation, bounded volume, and protection of confidential data.

**Why it matters:** Production systems operate under partial failure, overload, restarts, and mixed versions rather than ideal local conditions.

- Use a logging facade and parameterized messages: `log.info("Created order {}", orderId);`
- Choose levels consistently: `ERROR`, `WARN`, `INFO`, `DEBUG`, `TRACE`.
- Include correlation/request IDs for distributed flows.
- Do not log passwords, tokens, session IDs, full payment data, or sensitive personal information.
- Avoid expensive string construction when debug logging is disabled.

#### 17.6 Code Quality

**Explanation:** Code quality comes from correctness, clarity, focused responsibilities, automated checks, and ease of safe change. Style matters less than understandable behavior and contracts.

**Why it matters:** Production systems operate under partial failure, overload, restarts, and mixed versions rather than ideal local conditions.

- Keep methods focused and names intention-revealing.
- Remove dead code instead of commenting it out; version control keeps history.
- Use static analysis, formatting, tests, and compiler warnings in CI.
- Treat unchecked warnings as issues to understand, not noise to suppress broadly.
- Benchmark performance-sensitive alternatives instead of relying on intuition.

#### 17.7 Configuration Management

**Explanation:** Configuration should be typed, validated, observable without revealing secrets, and loaded through a documented precedence. Invalid required settings should fail startup clearly.

**Why it matters:** Production systems operate under partial failure, overload, restarts, and mixed versions rather than ideal local conditions.

- Define configuration precedence explicitly.
- Validate required configuration at startup and fail with actionable messages.
- Separate secrets from ordinary configuration.
- Use typed configuration rather than scattered string lookups.
- Record non-sensitive effective configuration for diagnostics.
- Avoid runtime mutation unless the application has a designed reload mechanism.

#### 17.8 HTTP and Remote Calls

**Explanation:** Remote calls can be slow, duplicated, partially completed, or unavailable. Correct clients define timeouts, deadlines, idempotency, bounded retries, and capacity limits.

**Why it matters:** Production systems operate under partial failure, overload, restarts, and mixed versions rather than ideal local conditions.

- Set connect, request, and read timeouts.
- Propagate deadlines rather than resetting a full timeout at each hop.
- Retry only transient failures and only when the operation is idempotent or has an idempotency key.
- Use exponential backoff with jitter.
- Limit concurrency to protect downstream systems.
- Validate response status, content type, and size before deserialization.
- Add circuit breaking only with clear fallback and recovery semantics.

#### 17.9 Serialization and API Evolution

**Explanation:** Wire formats are long-lived contracts between independently deployed systems. Compatibility requires explicit treatment of missing, unknown, renamed, and differently represented fields.

**Why it matters:** Production systems operate under partial failure, overload, restarts, and mixed versions rather than ideal local conditions.

- Treat wire formats as public contracts.
- Add fields compatibly and define behavior for unknown or missing fields.
- Do not expose internal persistence entities directly.
- Version APIs based on semantic incompatibility, not every implementation change.
- Use explicit date/time, number, enum, and null representations.
- Test backward and forward compatibility with stored examples.

#### 17.10 Database Practices

**Explanation:** Application validation and database constraints work together to protect data. Query plans, indexes, transaction boundaries, pagination, and migration compatibility affect production correctness.

**Why it matters:** Production systems operate under partial failure, overload, restarts, and mixed versions rather than ideal local conditions.

- Enforce critical invariants with database constraints as well as application validation.
- Index according to measured query plans.
- Avoid N+1 query patterns.
- Paginate large result sets with stable ordering.
- Use optimistic locking when concurrent updates must not silently overwrite each other.
- Design migrations to coexist with old and new application versions during rolling deployment.

#### 17.11 Graceful Lifecycle

On shutdown:

1. Stop accepting new work.
2. Mark the instance unready.
3. Allow in-flight requests a bounded grace period.
4. Stop consumers and scheduled tasks.
5. Shut down executors.
6. Flush telemetry where possible.
7. Close pools and other resources.

Shutdown hooks are best-effort and must finish quickly; abrupt process or host failure can bypass them.

#### 17.12 Resilience Patterns

**Explanation:** Timeouts, retries, circuit breakers, bulkheads, and rate limiters control different failure effects. Combining them incorrectly can amplify traffic or hide persistent failure.

**Why it matters:** Production systems operate under partial failure, overload, restarts, and mixed versions rather than ideal local conditions.

- **Timeout:** bounds waiting time.
- **Retry:** repeats selected transient operations.
- **Circuit breaker:** temporarily stops calls after repeated failure.
- **Bulkhead:** isolates capacity between workloads.
- **Rate limiter:** controls admission rate.

These patterns interact. For example, retries multiply downstream load and must fit inside the overall deadline. Monitor attempts separately from logical requests.

#### 17.13 Caching

A cache design must define:

- Key identity and normalization.
- Value ownership and mutability.
- Maximum size or weight.
- Expiry after write/access.
- Refresh behavior.
- Handling of missing values and failures.
- Consistency after source updates.

Prevent cache stampedes with request coalescing, jittered expiry, or controlled refresh. Never use an unbounded map as a production cache.

#### 17.14 Pagination

Offset pagination is simple but can become slow and unstable as data changes. Keyset/cursor pagination uses the last ordered key:

```sql
SELECT id, created_at
FROM orders
WHERE (created_at, id) < (?, ?)
ORDER BY created_at DESC, id DESC
LIMIT ?
```

Ordering must be deterministic and include a unique tie-breaker. Treat cursors as opaque API values and validate them.

#### 17.15 Deployment Compatibility

Rolling deployments temporarily run multiple versions:

- Database changes should follow expand-migrate-contract.
- Message consumers should tolerate old and new event forms.
- New writers should not immediately emit data old readers cannot parse.
- Cache key/version changes need a transition plan.
- Feature flags should have ownership, expiry, and safe defaults.

Backward compatibility is an operational requirement, not only an API concern.

#### 17.16 Health Checks

**Explanation:** Liveness decides restart, readiness decides traffic eligibility, and startup checks allow initialization time. Each signal must reflect a distinct operational decision.

**Why it matters:** Production systems operate under partial failure, overload, restarts, and mixed versions rather than ideal local conditions.

- **Liveness:** whether the process should be restarted.
- **Readiness:** whether it can currently receive traffic.
- **Startup:** whether initialization is still progressing.

Do not make liveness depend on every downstream service, or a remote outage can trigger restart loops. Readiness checks should be fast, bounded, and tied to actual ability to serve.

#### 17.17 Data Ownership and Privacy

**Explanation:** Data ownership defines who may collect, use, change, retain, and delete information. Privacy requires minimization, access control, encryption, retention, and audit.

**Why it matters:** Production systems operate under partial failure, overload, restarts, and mixed versions rather than ideal local conditions.

- Collect only data required for a defined purpose.
- Define retention and deletion behavior.
- Classify data sensitivity.
- Encrypt sensitive data in transit and at rest.
- Restrict access and record appropriate audits.
- Avoid copying production personal data into development and test environments.

#### 17.18 Chapter Review and Common Pitfalls

##### API design

- Define nullability, ownership, thread safety, error behavior, complexity, and versioning expectations.
- Prefer domain-specific parameter objects over long groups of primitive arguments.
- Make invalid states unrepresentable where practical.
- Keep public surfaces small because every exposed type and behavior becomes a compatibility obligation.

##### Resource management

- Ownership should be singular and documented: the creator, receiver, or container closes the resource.
- Set bounds on pools, queues, buffers, and caches.
- A leaked file descriptor or connection can fail the process before heap memory is exhausted.
- Shutdown paths need deadlines and must tolerate already-closed resources.

##### Configuration

- Parse configuration once into immutable typed values.
- Distinguish missing, blank, malformed, and forbidden values.
- Validate combinations, such as a timeout shorter than a retry delay.
- Secrets should be redacted in effective-configuration output and error messages.
- Dynamic reload requires atomic snapshots and explicit behavior for invalid updates.

##### Remote calls

- Connect timeout, response timeout, and total deadline represent different boundaries.
- Connection pools require acquisition timeouts and stale-connection handling.
- Retry budgets prevent one dependency failure from multiplying total traffic.
- Idempotency keys must be scoped to the intended operation and retained long enough for retry windows.
- Propagate correlation and trace context without trusting incoming identity claims.

##### Database access

- Pool size should reflect database concurrency capacity and application transaction duration.
- Always bind values; identifiers require allowlisting because placeholders normally bind data, not SQL syntax.
- Fetch size and streaming behavior are driver-specific.
- Optimistic locking detects lost updates through a version field or equivalent predicate.
- Migration rollback may be impossible after destructive data changes; design forward recovery.

##### Logging

- Structured logging preserves fields for search and aggregation.
- High-cardinality values belong in logs/traces, not metric labels.
- Sample repetitive success logs before dropping failure evidence.
- Log at the layer with sufficient context and responsibility for handling.
- Audit logs need stronger integrity, retention, and access controls than diagnostic logs.

##### Caching

- Decide whether cache failure should fail open, fail closed, or bypass.
- Negative caching can protect a source but may delay visibility of newly created data.
- Distributed caches introduce serialization, network, and consistency failure modes.
- Cache invalidation should be tied to authoritative state changes where possible.
- Measure hit rate, load latency, eviction, and entry weight.

##### Deployment and lifecycle

- Readiness should turn false before draining starts.
- Background work needs ownership during rolling replacement.
- Schema and event compatibility must span the maximum coexistence window.
- Startup should not accept traffic until mandatory initialization completes.
- Crash recovery must not rely on shutdown hooks having run.

##### Reliability

- Define retryable error classes explicitly.
- Use jitter to prevent synchronized retry storms.
- Circuit breakers require enough traffic and observability to transition reliably.
- Bulkheads should align with dependencies or workload classes that can fail independently.
- Test overload and recovery, not only steady successful load.

##### Operations

- Runbooks should state symptoms, dashboards, safe mitigations, escalation, and recovery checks.
- Alerts need an owner and actionable response.
- Changes should be observable through version/build identifiers.
- Backups are incomplete until restoration is tested.
- Post-incident actions should address contributing system conditions, not only individual mistakes.

### 18. Security Essentials

#### 18.1 Input and Output

**Explanation:** Security boundaries must validate incoming shape and meaning, then encode outgoing values for their exact destination context. One generic sanitizer cannot protect every sink.

**Why it matters:** Security failures cross trust boundaries and can expose data or capability even when ordinary functional tests pass.

- Validate input by type, length, range, format, and business rules.
- Prefer allowlists over blocklists.
- Encode output for its destination: HTML, JavaScript, URL, SQL, shell, or log contexts need different handling.
- Do not build SQL with string concatenation. Use `PreparedStatement`.
- Avoid executing OS commands with untrusted input. If unavoidable, pass fixed arguments without invoking a shell.

#### 18.2 Secrets and Cryptography

**Explanation:** Secrets require controlled storage, access, rotation, and redaction. Cryptography should use established high-level constructions with correct keys, nonces, salts, and lifecycle.

**Why it matters:** Security failures cross trust boundaries and can expose data or capability even when ordinary functional tests pass.

- Keep secrets outside source control in a secret manager or protected environment configuration.
- Never invent cryptographic algorithms.
- Use authenticated encryption such as AES-GCM when application-level encryption is required.
- Use `SecureRandom` for security-sensitive randomness.
- Store passwords using a dedicated slow password-hashing algorithm such as Argon2id, bcrypt, scrypt, or PBKDF2 with a unique salt.
- Clear sensitive character arrays when practical; immutable `String` values cannot be cleared.

```java
SecureRandom random = new SecureRandom();
byte[] token = new byte[32];
random.nextBytes(token);
String encoded = Base64.getUrlEncoder().withoutPadding().encodeToString(token);
```

#### 18.3 Deserialization and Reflection

Native Java deserialization of untrusted bytes is dangerous because gadget chains can execute code. Prefer constrained formats such as JSON with explicit target types. If legacy serialization is unavoidable, use JDK object input filters and a strict allowlist.

Reflection can bypass normal access controls and type checks. Restrict reflected classes and members; do not derive class names or method names directly from untrusted input.

#### 18.4 Files and Paths

Prevent path traversal by resolving against an expected root and verifying the normalized result:

```java
Path root = Path.of("/data/uploads").toAbsolutePath().normalize();
Path target = root.resolve(userFileName).normalize();
if (!target.startsWith(root)) {
  throw new SecurityException("Invalid path");
}
```

Also limit upload size, verify content rather than trusting extensions, generate server-side file names, and apply least-privilege file permissions.

#### 18.5 Dependency and Runtime Security

**Explanation:** Secure runtime operation requires supported JDKs, patched dependencies, least privilege, restricted network/filesystem access, and verified TLS.

**Why it matters:** Security failures cross trust boundaries and can expose data or capability even when ordinary functional tests pass.

- Use supported JDK releases and apply security updates.
- Scan direct and transitive dependencies.
- Remove unused dependencies and features.
- Run the process as a non-administrator with minimum filesystem and network access.
- Use TLS certificate validation; never install a trust-all manager in production.
- Set connection, read, request, and transaction timeouts.

#### 18.6 Authentication and Authorization

**Explanation:** Authentication establishes who an actor is; authorization decides what that actor may do to a specific resource. Both checks must use trusted server-side context.

**Why it matters:** Security failures cross trust boundaries and can expose data or capability even when ordinary functional tests pass.

- Authentication establishes identity; authorization decides permitted actions.
- Check authorization on every protected server-side operation.
- Apply deny-by-default and least privilege.
- Avoid trusting role or ownership data supplied by a client.
- Keep session and token lifetimes appropriate to risk.
- Rotate signing and encryption keys through a controlled process.
- Validate token issuer, audience, signature, expiry, and allowed algorithms.

#### 18.7 Denial-of-Service Defenses

Set limits for:

- Request and upload size.
- Decompressed size and archive entry count.
- JSON/XML nesting depth and collection length.
- Regex complexity and input length.
- Query result size and pagination limits.
- Concurrent requests, queued work, and per-client rate.
- Time spent on outbound calls and database operations.

Resource exhaustion is possible even when input is syntactically valid.

#### 18.8 XML, JSON, and Template Safety

**Explanation:** Parsers and template engines can consume excessive resources or instantiate unsafe behavior. Configure allowed features, expected types, depth, size, and output encoding.

**Why it matters:** Security failures cross trust boundaries and can expose data or capability even when ordinary functional tests pass.

- Disable external XML entities and DTD processing unless explicitly required and safely configured.
- Bind JSON to expected types; avoid unrestricted polymorphic type loading.
- Limit parser depth and total tokens.
- Escape untrusted data according to the output context.
- Do not evaluate user-controlled template expressions or scripts.

#### 18.9 Logging and Audit Security

**Explanation:** Diagnostic logs explain behavior, while audit logs record security-relevant actions. Both must resist injection and sensitive-data exposure; audits additionally need integrity and retention.

**Why it matters:** Security failures cross trust boundaries and can expose data or capability even when ordinary functional tests pass.

- Sanitize carriage returns and line feeds when untrusted values enter line-oriented logs.
- Mask secrets and minimize personal data.
- Protect logs from unauthorized read and modification.
- Record security-relevant events with actor, action, target, outcome, and correlation ID.
- Avoid revealing account existence or internal implementation details in user-facing errors.

#### 18.10 Security Review Checklist

1. Identify assets, trust boundaries, entry points, and attacker capabilities.
2. Trace untrusted data to SQL, files, commands, templates, logs, and deserializers.
3. Review identity and authorization decisions separately.
4. Inspect dependency and deployment configuration.
5. Verify failure behavior, rate limits, and resource limits.
6. Test with realistic malicious inputs.
7. Ensure monitoring can detect abuse without exposing sensitive data.

#### 18.11 Threat Modeling with STRIDE

STRIDE prompts review of:

- **Spoofing:** impersonating an identity.
- **Tampering:** unauthorized modification.
- **Repudiation:** denying an action without reliable audit evidence.
- **Information disclosure:** exposing protected data.
- **Denial of service:** exhausting resources.
- **Elevation of privilege:** gaining unauthorized capability.

Apply these questions to each trust boundary and data flow, then prioritize mitigations by likelihood and impact.

#### 18.12 SSRF Protection

Server-side request forgery occurs when attackers influence server-initiated destinations.

- Prefer allowlisted destinations.
- Resolve and validate hosts carefully, including redirects and DNS changes.
- Block loopback, link-local, private, metadata-service, and other protected ranges as required.
- Restrict protocols and ports.
- Use outbound network controls in addition to application validation.
- Limit response size and time.

Simple string prefix checks are insufficient URL validation.

#### 18.13 Session and Cookie Security

**Explanation:** Sessions bind browser requests to authenticated state. Strong identifiers, secure cookie attributes, expiry, regeneration, logout, and CSRF defenses protect that binding.

**Why it matters:** Security failures cross trust boundaries and can expose data or capability even when ordinary functional tests pass.

- Generate session identifiers with cryptographic randomness.
- Regenerate sessions after authentication or privilege changes.
- Use `Secure`, `HttpOnly`, and an appropriate `SameSite` setting.
- Enforce inactivity and absolute expiry.
- Invalidate server-side state on logout where applicable.
- Protect state-changing browser requests against CSRF.

Do not store sensitive authorization state solely in client-modifiable cookies.

#### 18.14 Key and Certificate Management

**Explanation:** Keys and certificates need purpose separation, protected storage, rotation, revocation, expiry monitoring, and recovery procedures.

**Why it matters:** Security failures cross trust boundaries and can expose data or capability even when ordinary functional tests pass.

- Separate encryption keys by purpose and environment.
- Define rotation and revocation procedures.
- Keep old decryption keys only as long as needed for migration.
- Validate hostname and certificate chains for TLS.
- Monitor certificate expiry.
- Protect private keys with dedicated secret or key-management services.

Algorithm choice is only one part of cryptographic security; lifecycle and access control are equally important.

#### 18.15 Supply-Chain Security

**Explanation:** The software supply chain includes repositories, dependencies, plugins, CI, base images, signing, and release credentials. Every build input can affect the delivered artifact.

**Why it matters:** Security failures cross trust boundaries and can expose data or capability even when ordinary functional tests pass.

- Pin trusted repositories and verify artifacts.
- Protect CI credentials and release signing keys.
- Review build plugins because they execute code during builds.
- Generate SBOMs for released artifacts.
- Track vulnerability reachability and exploitability, not only raw scanner counts.
- Rebuild and redeploy after base-image or JDK security updates.
- Review source provenance for newly introduced dependencies.

#### 18.16 Security Testing

Combine:

- Static analysis for dangerous code patterns.
- Dependency scanning for known vulnerable components.
- Dynamic scanning against a running application.
- Fuzzing for parser and boundary robustness.
- Manual review for authorization and business-logic flaws.
- Penetration testing for realistic attack paths.

No single scanner proves an application secure.

#### 18.17 Chapter Review and Common Pitfalls

##### Validation

- Validate at the trust boundary and again where domain invariants require it.
- Canonicalize only when the destination semantics are understood.
- Length limits should apply before expensive parsing or normalization.
- Validation does not replace context-specific output encoding.

##### SQL and command injection

- Prepared statements protect data parameters but not dynamically concatenated table names or sort directions.
- Allowlist dynamic identifiers and map user choices to fixed server-side tokens.
- `ProcessBuilder` avoids shell parsing when given an argument list, but invoked programs may interpret arguments dangerously.
- Apply least-privilege database and OS accounts to reduce impact.

##### Authentication

- Use established identity protocols and libraries.
- Multi-factor authentication reduces risk from stolen passwords.
- Login responses and timing should avoid unnecessary account enumeration.
- Credential recovery is an authentication path and needs equivalent protection.
- Re-authenticate for high-risk actions when appropriate.

##### Authorization

- Enforce object-level access, not only endpoint roles.
- Central policy can improve consistency, but decisions still need complete resource context.
- Tenant identity must come from trusted authentication/session context.
- Cache authorization only with correct invalidation and policy versioning.
- Administrative access should be narrowly scoped and audited.

##### Cryptography

- Encryption without authentication permits undetected modification.
- Nonces/IVs must follow algorithm requirements and often must never repeat under one key.
- Password hashing parameters should be calibrated and upgradeable.
- Compare secrets with constant-time APIs where timing exposure matters.
- Separate key encryption, data encryption, signing, and password-hashing purposes.

##### Web security

- Content Security Policy reduces XSS impact but does not replace encoding.
- CORS controls browser access, not server-to-server authorization.
- CSRF protection is needed when browsers automatically attach credentials.
- Redirect destinations should be allowlisted to prevent open redirects.
- Security headers should be tested with actual application flows.

##### Parsing and deserialization

- Limit bytes before decompression and objects after parsing.
- Reject unexpected fields where strict contracts and security require it.
- Polymorphic deserialization should map explicit safe type identifiers, not arbitrary class names.
- Archive extraction must prevent path traversal, excessive expansion, and special-file creation.
- XML parsers require explicit secure configuration.

##### SSRF and files

- Validate every redirect target, not only the first URL.
- DNS resolution can change between validation and connection; network-level egress control is stronger.
- Path checks must consider symbolic links and platform-specific case behavior.
- Create upload files with restrictive permissions and unpredictable server-generated names.
- Scan or isolate content according to how it will later be processed.

##### Supply chain

- Locking a version does not prove its integrity; use verification/checksums/signatures.
- Typosquatting and dependency confusion exploit naming and repository precedence.
- Minimize build and runtime dependencies.
- Monitor end-of-life libraries and JDKs.
- Keep a rapid rebuild/redeployment path for urgent security fixes.

##### Detection and response

- Security events need synchronized time, actor identity, target, decision, and correlation.
- Rate-limit alerts to avoid flooding while preserving incident visibility.
- Protect forensic evidence and document chain of custody when relevant.
- Rotate potentially exposed credentials before declaring recovery.
- Validate mitigations against the original attack path.

### 19. Performance, Monitoring, and Troubleshooting

#### 19.1 Measure First

Optimize only after measuring a representative workload. Wall-clock timing around a loop is unreliable for microbenchmarks because the JVM warms up, compiles hot code, and may eliminate unused work. Use JMH for microbenchmarks.

Important measures:

- Throughput: operations completed per unit time.
- Latency: duration of one operation; inspect percentiles such as p50, p95, and p99.
- Error rate: failed operations divided by total operations.
- Saturation: CPU, memory, thread, connection-pool, and queue utilization.

#### 19.2 JVM Tools

**Explanation:** JVM diagnostic tools expose complementary snapshots and recordings. Choose a command based on the question and assess its overhead before using it on production.

**Why it matters:** Evidence-based diagnosis prevents expensive changes that treat symptoms, distort measurements, or introduce new bottlenecks.

- `jcmd <pid> VM.flags`: show JVM flags.
- `jcmd <pid> GC.heap_info`: summarize heap configuration.
- `jcmd <pid> Thread.print`: capture a thread dump.
- `jstack <pid>`: inspect thread states and deadlocks.
- `jmap -histo <pid>`: display a heap object histogram.
- `jstat -gc <pid> 1000`: sample garbage-collection statistics.
- Java Flight Recorder (JFR): low-overhead recording of CPU, allocation, locks, I/O, and GC events.
- Java Mission Control (JMC): analyze JFR recordings.

Prefer `jcmd` on modern JDKs when it provides the needed operation.

#### 19.3 Common Failure Patterns

##### High CPU

1. Capture multiple thread dumps several seconds apart.
2. Find threads repeatedly in `RUNNABLE`.
3. Inspect hot stack traces.
4. Confirm with JFR or a profiler.

##### Memory leak or rising heap

1. Check whether usage returns to a stable level after GC.
2. Inspect object histograms over time.
3. Capture a heap dump near failure with `-XX:+HeapDumpOnOutOfMemoryError`.
4. Analyze retained size and GC-root paths.
5. Fix the retaining reference, cache policy, listener, `ThreadLocal`, or unbounded collection.

##### Deadlock

Thread dumps identify Java monitor deadlocks and show which threads own and wait for locks. Enforce a consistent lock order and reduce nested locking.

##### Slow requests

Check downstream latency, timeouts, pool saturation, lock contention, GC pauses, database query plans, and excessive allocation. Average latency alone can hide severe tail latency.

#### 19.4 GC Guidance

**Explanation:** GC tuning begins after measuring allocation, live set, pauses, throughput, and available CPU. Collector defaults are often better than speculative flag changes.

**Why it matters:** Evidence-based diagnosis prevents expensive changes that treat symptoms, distort measurements, or introduce new bottlenecks.

- Set memory limits appropriate to the container or host.
- Avoid choosing a collector or tuning dozens of flags before collecting evidence.
- G1 is a balanced default for many server applications.
- ZGC and Shenandoah target very low pause times for large heaps; verify availability and behavior on the chosen JDK.
- Allocation rate and live-set size often matter more than the number of objects created.
- A larger heap can reduce collection frequency but may increase memory footprint and some pause costs.

#### 19.5 Thread-Dump States

**Explanation:** Thread states show whether a thread can run, waits for a monitor, waits for a signal, or has terminated. Several dumps reveal whether a state is persistent.

**Why it matters:** Evidence-based diagnosis prevents expensive changes that treat symptoms, distort measurements, or introduce new bottlenecks.

- `RUNNABLE`: executing or ready to execute; may also be inside native I/O.
- `BLOCKED`: waiting to enter a synchronized monitor.
- `WAITING`: waiting indefinitely for another action.
- `TIMED_WAITING`: waiting with a deadline, such as `sleep()` or timed `get()`.
- `TERMINATED`: completed.

One thread dump is a snapshot; compare several to distinguish persistent contention from temporary activity.

#### 19.6 Observability

Use complementary signals:

- **Logs:** detailed discrete events.
- **Metrics:** aggregate rates, counts, gauges, and distributions.
- **Traces:** request flow and timing across components.
- **Profiles:** where CPU time or allocation occurs inside a process.

Prefer low-cardinality metric labels. User IDs, request IDs, and raw URLs can create unbounded time series and excessive monitoring cost.

#### 19.7 Service-Level Indicators

Common SLIs include availability, successful request rate, latency percentiles, freshness, and correctness. Define them from the user's perspective.

- An SLO states the target level over a time window.
- An error budget is the allowed unreliability.
- Alerts should indicate actionable user impact or imminent exhaustion, not every internal fluctuation.

#### 19.8 Benchmarking with JMH

**Explanation:** JMH controls warmup, optimization, forking, timing, and result consumption for JVM microbenchmarks. A correct microbenchmark still needs a production-relevant question.

**Why it matters:** Evidence-based diagnosis prevents expensive changes that treat symptoms, distort measurements, or introduce new bottlenecks.

```java
@State(Scope.Thread)
public class LookupBenchmark {
  private Map<Integer, String> values;

  @Setup
  public void setup() {
    values = IntStream.range(0, 10_000)
        .boxed()
        .collect(Collectors.toMap(i -> i, Object::toString));
  }

  @Benchmark
  public String lookup() {
    return values.get(5_000);
  }
}
```

JMH handles warmup, measurement iterations, forking, and result consumption. Still ensure the benchmark represents the production data shape and operation.

#### 19.9 Capacity and Load Testing

**Explanation:** Load tests measure behavior near and beyond expected demand, including saturation and recovery. Realistic arrivals, data, failures, and generator capacity are essential.

**Why it matters:** Evidence-based diagnosis prevents expensive changes that treat symptoms, distort measurements, or introduce new bottlenecks.

- Test expected load, peak load, sudden spikes, and sustained soak conditions.
- Include realistic payloads, database sizes, cache hit rates, and downstream latency.
- Observe queue growth and tail latency, not only throughput.
- Find the saturation point and define safe operating headroom.
- Verify recovery after overload; a system that remains degraded has not passed.

#### 19.10 Diagnostic Data Safety

Heap dumps, thread dumps, JFR recordings, and logs may contain credentials, personal data, and request contents. Restrict access, encrypt storage and transfer, define retention, and remove artifacts after diagnosis.

#### 19.11 Allocation Profiling

High allocation can increase GC frequency even without a leak. Profile:

- Allocation rate by class and stack.
- Temporary collections and boxed primitives.
- Repeated parsing, formatting, and buffer creation.
- Large arrays or payload copies.

Optimize only meaningful contributors. Short-lived allocation is often cheap, and object pooling can increase retention and complexity.

#### 19.12 Lock Profiling

Contention symptoms include blocked threads, low CPU despite queued work, and long tail latency.

Investigate:

- Monitor-blocked time.
- Lock owners and call paths.
- Critical-section duration.
- I/O or callbacks while holding locks.
- One global lock protecting independent data.

Possible fixes include reducing lock scope, partitioning state, immutable snapshots, concurrent collections, or redesigning ownership.

#### 19.13 Database Performance

**Explanation:** Database latency includes pool waiting, locks, planning, execution, transfer, and transaction effects. Query plans and measured workload should drive changes.

**Why it matters:** Evidence-based diagnosis prevents expensive changes that treat symptoms, distort measurements, or introduce new bottlenecks.

- Measure query latency and rows examined/returned.
- Inspect execution plans.
- Avoid fetching unused columns and unbounded results.
- Size pools from database capacity and observed concurrency, not arbitrary large values.
- Monitor connection wait time separately from query time.
- Keep transactions short to reduce lock and version retention.

More application threads cannot compensate for a saturated database.

#### 19.14 Coordinated Omission

Some load generators wait for one response before sending the next request, under-reporting latency while the system is stalled. A realistic test should preserve intended arrival rate or otherwise account for coordinated omission.

Report full latency distributions and request failures; dropping slow samples makes results misleading.

#### 19.15 Troubleshooting Method

1. State the user-visible symptom and time range.
2. Confirm scope, affected versions, and recent changes.
3. Preserve logs, metrics, traces, dumps, and configuration.
4. Build a timeline across systems.
5. Form falsifiable hypotheses.
6. Test the cheapest and safest discriminating evidence first.
7. Mitigate impact before deep root-cause work when necessary.
8. Verify recovery with the original indicators.
9. Document root cause and preventive actions.

#### 19.16 Performance Change Validation

Compare before and after under equivalent conditions:

- Same workload and data distribution.
- Same JDK, flags, hardware limits, and warmup.
- Multiple forks/runs.
- Confidence intervals or variability.
- Correctness checks.
- CPU, memory, GC, and downstream effects.

A local microbenchmark improvement may worsen end-to-end performance.

#### 19.17 Chapter Review and Common Pitfalls

##### Measurement

- Define whether the goal is throughput, latency, footprint, startup, cost, or a trade-off.
- Measure from the user boundary and component boundaries.
- Use percentiles with sample counts and time windows.
- Separate service time from queueing time.
- Correlation does not prove causation; change one controlled factor where possible.

##### CPU profiling

- Sampling profilers have lower distortion than instrumentation for many production investigations.
- Wall-clock profiles expose blocking and waiting; CPU profiles show on-CPU work.
- Native and kernel time may require operating-system-aware profiling.
- Compare multiple intervals to distinguish transient spikes from steady hotspots.
- Optimize hot call paths, not methods that are merely individually slow but rarely called.

##### Memory

- Shallow size measures one object; retained size estimates what becomes collectible with it.
- Dominator trees help identify ownership of retained graphs.
- A leak is unwanted retention, even if memory eventually stabilizes below the limit.
- Allocation pressure and retention need different fixes.
- Capture heap evidence before restarting when operationally safe.

##### Garbage collection

- Analyze pause distribution, allocation rate, promotion, concurrent-cycle progress, and live-set trend together.
- Frequent full collections are symptoms; identify why memory cannot be reclaimed or allocated.
- Explicit `System.gc()` can trigger disruptive collection unless disabled/handled by collector policy.
- Container CPU throttling can lengthen concurrent GC phases.
- Tune after establishing workload, goals, and baseline logs.

##### Threads and locks

- Many threads increase stack/native memory and scheduling overhead.
- Queue length reveals contention or downstream saturation before CPU reaches 100%.
- Compare thread dumps to find persistent blocked stacks.
- Lock contention can come from logging, class loading, pools, and library internals, not only application synchronized blocks.
- Virtual-thread counts require different interpretation from platform-thread counts.

##### I/O and database

- Track pool acquisition, DNS, connect, TLS, server processing, and body transfer separately.
- A slow database query may be waiting on locks rather than executing a bad plan.
- Large payload serialization can dominate request CPU and allocation.
- Batch size trades round trips against memory and lock duration.
- Backpressure prevents fast producers from overwhelming slow consumers.

##### JFR and diagnostics

- Choose event settings appropriate to continuous monitoring or incident detail.
- Add custom JFR events for high-value domain operations when standard events lack context.
- Correlate JFR timestamps with metrics, logs, and traces.
- Heap dumps can briefly pause large processes and require storage roughly related to live heap.
- Practice diagnostic collection before an incident.

##### Load testing

- Warm caches and JIT separately from cold-start tests.
- Model think time, arrival rate, connection reuse, and payload diversity.
- Include failure injection and slow dependencies.
- Observe the generator itself for CPU/network saturation.
- Verify correctness under load; high throughput with lost or duplicated work is failure.

##### Capacity

- Capacity planning should include growth, failover, maintenance, and noisy-neighbor headroom.
- Little's Law relates average concurrency, throughput, and average time in a stable system.
- A queue can smooth brief bursts but cannot solve sustained arrival above service rate.
- Autoscaling reacts after measured signals and must account for startup time.
- Scale tests should verify both expansion and safe contraction.

##### Optimization

- Prefer algorithmic and architectural improvements before micro-optimizations.
- Reduce unnecessary work, data movement, synchronization, and remote calls.
- Preserve readability unless evidence justifies complexity.
- Record benchmark methodology and expected gains.
- Re-measure after deployment because production workload can differ from tests.

### 20. Quick Revision Checklist

#### 20.1 Suggested Learning Order

1. Syntax, types, control flow, methods, arrays, and strings.
2. Classes, interfaces, inheritance, composition, and exceptions.
3. Collections, generics, equality, and ordering.
4. Lambdas, streams, `Optional`, and `java.time`.
5. Testing, build tools, JDBC, and file handling.
6. Thread safety, executors, futures, and the Java Memory Model.
7. JVM memory, class loading, GC, profiling, and diagnostics.
8. Design principles, security, modules, and production operations.

#### 20.2 Coding Exercises

Practice implementing:

- Frequency counting and duplicate detection with maps.
- Stable sorting with multiple comparator keys.
- An immutable value object with validation.
- A bounded producer-consumer queue.
- Asynchronous composition with timeouts and failure recovery.
- Stream grouping, partitioning, flattening, and reduction.
- A transaction with rollback and try-with-resources.
- Unit tests for normal, boundary, and failure cases.
- A file parser that handles malformed input and large files safely.
- A thread-safe cache with a documented eviction policy.

#### 20.3 Code Review Questions

**Explanation:** A review checklist directs attention to correctness, ownership, failure handling, security, compatibility, and tests. Questions should produce evidence, not mechanical approval.

**Why it matters:** A checklist turns passive reading into demonstrable coding, review, debugging, and operational capability.

- Is ownership of mutable state clear?
- Are nullability and error contracts explicit?
- Can resources, threads, or transactions leak?
- Are equality and ordering consistent?
- Is concurrency protected by a documented mechanism?
- Are timeouts and bounds present at external boundaries?
- Could untrusted input reach a dangerous sink?
- Are logs useful without exposing secrets?
- Are tests checking behavior and failure paths?
- Does the code depend on a newer JDK than the build declares?

#### 20.4 Scenario-Based Interview Practice

Be ready to reason through:

- A `HashMap` lookup failing after a key field changes.
- Lost increments despite a volatile counter.
- A thread pool exhausted by blocking downstream calls.
- Memory rising because of an unbounded cache or `ThreadLocal`.
- Duplicate payment after an unsafe retry.
- Incorrect times during daylight-saving transitions.
- A stream pipeline producing race-dependent output.
- A database transaction holding locks during an HTTP call.
- A class found during compilation but missing at runtime.
- A service that is fast on average but has unacceptable p99 latency.

#### 20.5 Final Revision Method

For each topic, verify that you can:

1. Define it in one or two sentences.
2. Write a small correct example without copying.
3. Explain one common mistake.
4. Describe when not to use it.
5. Connect it to production behavior, testing, or diagnostics.

#### 20.6 Topic Self-Assessment Matrix

Rate each topic from 0 to 3:

- **0:** unfamiliar.
- **1:** can define it.
- **2:** can implement and explain trade-offs.
- **3:** can diagnose failures and teach it.

Prioritize topics rated 0 or 1, then revisit them through code rather than passive rereading.

#### 20.7 Thirty-Minute Revision Plan

1. Five minutes: types, strings, equality, and exceptions.
2. Five minutes: collections, generics, and complexity.
3. Five minutes: OOP, SOLID, and design trade-offs.
4. Five minutes: streams, `Optional`, and date/time.
5. Five minutes: concurrency and the Java Memory Model.
6. Five minutes: JVM, GC, testing, security, and diagnostics.

#### 20.8 Two-Hour Practical Revision Plan

1. Implement a small domain model with immutable values.
2. Add collection and stream transformations.
3. Persist through a repository interface.
4. Add unit and integration tests.
5. Introduce concurrent processing with cancellation.
6. Run a profiler or JFR recording.
7. Review resource, error, and security boundaries.

#### 20.9 Interview Answer Framework

For conceptual questions:

1. Give a precise definition.
2. Explain the mechanism.
3. Provide a small example.
4. State a common pitfall.
5. Explain when an alternative is better.

For debugging questions:

1. Clarify the symptom and constraints.
2. Name the evidence to collect.
3. Rank hypotheses.
4. Propose a safe mitigation.
5. Verify and prevent recurrence.

#### 20.10 Final Project Checklist

**Explanation:** A final checklist verifies that code can be built, tested, configured, secured, deployed, observed, and recovered—not merely compiled on one workstation.

**Why it matters:** A checklist turns passive reading into demonstrable coding, review, debugging, and operational capability.

- Build succeeds from a clean checkout with the wrapper.
- Compiler and JDK versions are explicit.
- Tests cover critical success and failure behavior.
- Dependencies and plugins are reviewed.
- Inputs, outputs, timeouts, and resource limits are bounded.
- Secrets are externalized and logs are sanitized.
- Transactions and retries have correct semantics.
- Shutdown and deployment compatibility are tested.
- Metrics, logs, and traces support diagnosis.
- Operational documentation explains launch, configuration, health, backup, and recovery.

#### 20.11 Chapter Review and Common Pitfalls

##### Learning strategy

- Alternate reading with implementation, debugging, and explanation.
- Use spaced repetition for contracts, version facts, and common failure modes.
- Rebuild examples from memory rather than only rereading completed code.
- Connect each language feature to a production scenario.

##### Coding practice

- Start with correctness and tests, then discuss complexity and alternatives.
- State assumptions such as null policy, input size, ordering, and concurrency.
- Use descriptive names and standard library APIs.
- After solving, test empty, singleton, duplicate, maximum, invalid, and concurrent cases as relevant.

##### Interview communication

- Clarify requirements before choosing data structures or architecture.
- Think aloud in a structured way without narrating every keystroke.
- Explain time and space complexity.
- Identify trade-offs rather than presenting one choice as universally best.
- Correct mistakes directly when discovered.

##### System scenarios

- Define consistency, availability, latency, and scale requirements.
- Trace data ownership and failure boundaries.
- Include observability, security, deployment, and recovery.
- Distinguish what must be synchronous from what may be eventual.
- Describe overload behavior explicitly.

##### Code review

- Separate correctness blockers from optional style preferences.
- Give evidence and an actionable recommendation.
- Review tests and operational behavior alongside implementation.
- Check compatibility, migration, and rollback.
- Avoid expanding scope into unrelated refactoring.

##### Self-assessment

- Require a runnable example for “can implement.”
- Require evidence collection and root-cause reasoning for “can diagnose.”
- Reassess after a delay to distinguish recognition from recall.
- Track weak categories, not only total study time.

##### Final readiness

- Be able to navigate official JDK API documentation and release notes.
- Know the target project's JDK and build commands.
- Practice reading stack traces, thread dumps, GC logs, and test failures.
- Review secure defaults and resource limits.
- Explain one real incident or difficult bug using symptom, evidence, root cause, fix, and prevention.
