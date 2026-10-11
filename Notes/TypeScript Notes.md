# TypeScript Notes

## 1. What is TypeScript?

TypeScript is a superset of JavaScript created by Microsoft. It adds a static type system and tooling, then compiles (transpiles) to JavaScript. The program still runs with JavaScript semantics; TypeScript types do not change how values behave at runtime.

Key benefits:

- catches errors before runtime
- improves code readability and maintainability
- helps with large codebases
- provides better IntelliSense in editors
- supports advanced patterns like generics and interfaces

Compile a project configured with `tsconfig.json`:

```bash
tsc
```

For a one-file experiment, `tsc app.ts` emits `app.js`, but passing source files on the command line makes `tsc` ignore `tsconfig.json`. In an existing project, use its package scripts or project configuration instead.

## 2. TypeScript Configuration

A `tsconfig.json` file controls compiler behavior.

```json
{
  "compilerOptions": {
    "target": "ES2020",
    "module": "CommonJS",
    "strict": true,
    "esModuleInterop": true,
    "outDir": "dist"
  },
  "include": ["src/**/*.ts"]
}
```

Important compiler options:

- `strict`: enables strict type checking
- `noImplicitAny`: disallows implicit `any`
- `strictNullChecks`: helps avoid null-related bugs
- `target`: sets the JavaScript version output
- `module`: controls module system (`CommonJS`, `ESNext`, etc.)
- `outDir`: folder where compiled JS is emitted

`strict: true` enables a family of checks, including `noImplicitAny` and `strictNullChecks`. Keep it enabled for new code. Module and target settings depend on the runtime and build tool; use the project’s recommended settings rather than copying one configuration blindly. To type-check without emitting JavaScript, run `tsc --noEmit`.

## 3. Basic Types

JavaScript has primitive values and objects. TypeScript describes them with types; for example, `number` covers integers and floating-point values (there is no separate `int` type).

```ts
let firstName: string = "Ali";
let age: number = 28;
let isActive: boolean = true;
let empty: null = null;
let missing: undefined = undefined;
```

### `any`, `unknown`, `never`, `void`

```ts
let data: any = "hello";
data = 42;

let value: unknown = "test";
if (typeof value === "string") {
  console.log(value.toUpperCase());
}

function throwError(message: string): never {
  throw new Error(message);
}

function logMessage(message: string): void {
  console.log(message);
}
```

- `any`: disables type checking; avoid using it unless necessary.
- `unknown`: safer than `any`; you must narrow it before use.
- `never`: represents values that never occur, such as a function that always throws.
- `void`: used for functions returning no value.

With `strictNullChecks`, `null` and `undefined` are distinct values and are not assignable to ordinary types unless included in a union. A JavaScript property lookup can still produce `undefined` at runtime, so model optionality explicitly and handle it.

## 4. Type Inference

TypeScript often infers variable types automatically.

```ts
let message = "Hello"; // inferred as string
let count = 10; // inferred as number

const status = "success"; // literal type inferred as "success"
```

For a `let`, TypeScript normally infers a type that permits reassignment (here, `string`); a `const` can retain the narrower literal type. Prefer inference when the type is clear from the initializer, and annotate function parameters and public APIs where it improves clarity.

`const` prevents rebinding a variable, not mutating the object it refers to:

```ts
const options = { retries: 2 };
options.retries = 3; // allowed
// options = { retries: 4 }; // Error
```

## 5. Type Annotation and Literal Types

```ts
let userName: string = "Hitesh";
let total: number = 100;

let direction: "left" | "right" | "center" = "left";
```

Literal types restrict values to specific strings, numbers, or booleans.

```ts
type Role = "admin" | "user" | "guest";
let role: Role = "admin";
```

## 6. Arrays

### Basic arrays

```ts
let numbers: number[] = [1, 2, 3, 4];
let names: Array<string> = ["A", "B", "C"];
```

### Mixed arrays

```ts
let mixed: (number | string)[] = [1, "two", 3];
```

### Readonly arrays

```ts
const nums: readonly number[] = [1, 2, 3];
// nums.push(4); // Error
```

### Nested arrays

```ts
let matrix: number[][] = [
  [1, 2],
  [3, 4],
  [5, 6]
];
```

### Array methods

```ts
const fruits = ["apple", "banana", "mango"];

const upper = fruits.map(f => f.toUpperCase());
const longNames = fruits.filter(f => f.length > 5);
```

## 7. Union Types

A variable can hold more than one type.

```ts
let value: string | number;

value = "abc";
value = 123;
```

Use narrowing with `typeof`:

```ts
function printValue(value: string | number) {
  if (typeof value === "string") {
    console.log(value.toUpperCase());
  } else {
    console.log(value.toFixed(2));
  }
}
```

## 8. Intersection Types

Use `&` to combine types.

```ts
type Employee = {
  id: number;
  name: string;
};

type Manager = {
  department: string;
};

type TeamLead = Employee & Manager;

const lead: TeamLead = {
  id: 1,
  name: "Alice",
  department: "Engineering"
};
```

Intersection types are useful when an object must satisfy multiple shapes at once.

## 9. Type Aliases

Type aliases create custom names for types.

```ts
type User = {
  id: number;
  name: string;
  email: string;
  isActive: boolean;
};

function createUser(user: User) {
  console.log(user.name);
}
```

### Readonly and optional properties

```ts
type Product = {
  readonly id: string;
  name: string;
  price?: number;
};
```

`readonly` prevents reassignment after object creation. `?` makes a property optional.

## 10. Objects and Interfaces

Interfaces define the shape of an object. TypeScript is structurally typed: compatibility is generally based on the members a value has, even if it was not declared as implementing that interface. This differs from Java/C++ nominal typing.

```ts
interface User {
  id: number;
  email: string;
  userId: number;
  googleId?: string;
}

const user1: User = {
  id: 1,
  email: "h@h.co",
  userId: 12
};
```

```ts
interface Point {
  x: number;
  y: number;
}

function distanceFromOrigin(point: Point): number {
  return Math.hypot(point.x, point.y);
}

const point3d = { x: 3, y: 4, z: 12 };
distanceFromOrigin(point3d); // allowed: it has the required x and y members
```

Fresh object literals receive an additional excess-property check, so assigning an inline literal with misspelled or unexpected keys may fail even though a previously-created variable with extra keys is assignable.

### Functions inside interfaces

```ts
interface User {
  readonly dbId: number;
  email: string;
  startTrail: () => string;
}

const user2: User = {
  dbId: 101,
  email: "abc@gmail.com",
  startTrail: () => "trail started"
};
```

### Extending interfaces

```ts
interface Admin extends User {
  role: "admin" | "manager" | "learner";
}
```

### Merging interfaces

```ts
interface User {
  githubToken: string;
}
```

This merges with an earlier `User` declaration.

Interface declaration merging is a TypeScript feature, not a runtime class feature. `interface` and object-shaped `type` aliases overlap substantially; interfaces can be extended and merged, while aliases can name unions, primitives, tuples, and other types. Choose based on the shape and project conventions.

## 11. Tuples

A tuple is an array with a fixed number of elements and known types in each position.

```ts
let user: [string, number] = ["hitesh", 25];

// order matters
// user = [25, "hitesh"]; // Error
```

### Named tuples

```ts
type UserTuple = [name: string, age: number];

const u1: UserTuple = ["Anurag", 30];
```

### Optional tuple elements

```ts
let rgb: [number, number, number?] = [255, 0];
let rgb2: [number, number, number?] = [255, 0, 128];
```

### Rest elements

```ts
type StringNumberBooleans = [string, number, ...boolean[]];

const values: StringNumberBooleans = ["hi", 1, true, false, true];
```

### Readonly tuples

```ts
const point: readonly [number, number] = [10, 20];
// point[0] = 30; // Error
```

## 12. Enums

Enums allow named constants.

### Numeric enum

```ts
enum SeatChoice {
  AISLE,
  MIDDLE,
  WINDOW
}

const seat = SeatChoice.AISLE;
```

### Custom numeric values

```ts
enum SeatChoice {
  AISLE = 10,
  MIDDLE = 22,
  WINDOW = 30
}
```

### String enum

```ts
enum SeatChoice {
  AISLE = "aisle",
  MIDDLE = "middle",
  WINDOW = "window"
}
```

Enums are convenient for fixed sets of values, but plain string unions are often preferred when possible because they are simpler and more predictable.

Unlike a string union, an `enum` also emits a JavaScript runtime object. Use one when that runtime object is useful or required by a library; otherwise a union is often enough:

```ts
type Seat = "aisle" | "middle" | "window";
```

## 13. Functions

### Basic function

```ts
function addTwo(num: number): number {
  return num + 2;
}
```

### Default parameters

```ts
function loginUser(name: string, email: string, isPaid: boolean = false) {
  // logic
}
```

An optional parameter (`email?: string`) may be omitted and is treated as possibly `undefined` inside the function. A default parameter also may be omitted, but receives its default value. Optional parameters should follow required parameters; default parameters should generally do so too unless passing `undefined` positionally is intentional.

### Rest parameters

```ts
function sum(...numbers: number[]): number {
  return numbers.reduce((total, n) => total + n, 0);
}

console.log(sum(1, 2, 3, 4));
```

### Function overloads

```ts
function formatValue(value: string): string;
function formatValue(value: number): string;
function formatValue(value: string | number): string {
  return String(value);
}
```

This is useful when the same function accepts multiple input shapes and has different type guarantees.

### `this` typing

```ts
const person = {
  name: "John",
  greet(this: { name: string }) {
    return `Hello ${this.name}`;
  }
};
```

## 14. Type Narrowing

TypeScript narrows unions to more specific types.

### `typeof`

```ts
function printValue(value: string | number) {
  if (typeof value === "string") {
    console.log(value.toUpperCase());
  } else {
    console.log(value.toFixed(2));
  }
}
```

### `in` operator

```ts
type Fish = { kind: "fish"; swim: () => void };
type Bird = { kind: "bird"; fly: () => void };
type Pet = Fish | Bird;

function move(pet: Pet) {
  if ("swim" in pet) {
    pet.swim();
  } else {
    pet.fly();
  }
}
```

### `instanceof`

```ts
function logValue(value: Date | string) {
  if (value instanceof Date) {
    console.log(value.toISOString());
  } else {
    console.log(value.toUpperCase());
  }
}
```

### Custom type guards

```ts
type Fish = { swim: () => void };
type Bird = { fly: () => void };

type Pet = Fish | Bird;

function isFish(pet: Pet): pet is Fish {
  return "swim" in pet;
}

const pet: Pet = { swim: () => console.log("Swimming") };

if (isFish(pet)) {
  pet.swim();
}
```

## 15. Classes

TypeScript classes are JavaScript classes with compile-time type checking. They use JavaScript's prototype and inheritance model and do not provide C++-style deterministic destruction or Java/C++ runtime type enforcement from TypeScript annotations.

```ts
class User {
  email: string;
  name: string;

  constructor(email: string, name: string) {
    this.email = email;
    this.name = name;
  }
}

const user1 = new User("h@h.com", "user1");
```

### Access modifiers

```ts
class User {
  public email: string;
  private password: string;
  protected role: string;

  constructor(email: string, password: string, role: string) {
    this.email = email;
    this.password = password;
    this.role = role;
  }
}
```

- `public`: accessible everywhere
- `private`: accessible only inside the class
- `protected`: accessible in the class and subclasses

TypeScript's `private` and `protected` are primarily compile-time restrictions; they do not make a property private at runtime. JavaScript's `#password` syntax provides runtime-enforced private fields. Use it when runtime privacy is required and supported by the target environment.

### Parameter properties

```ts
class User {
  constructor(
    public email: string,
    public name: string,
    private readonly id: number
  ) {}
}
```

### Getters and setters

```ts
class User {
  private _email: string = "";

  get email(): string {
    return this._email;
  }

  set email(value: string) {
    this._email = value;
  }
}
```

### Inheritance

```ts
class Animal {
  move() {
    console.log("Moving");
  }
}

class Dog extends Animal {
  bark() {
    console.log("Woof");
  }
}
```

### `implements`

```ts
interface TakePhoto {
  cameraMode: string;
  filter: string;
}

class Instagram implements TakePhoto {
  cameraMode = "portrait";
  filter = "vivid";
}
```

`implements` checks a class against an interface at compile time. It does not create runtime interface metadata, change object layout, or make an instance pass a runtime interface check.

### Abstract classes

```ts
abstract class Shape {
  abstract area(): number;
}

class Circle extends Shape {
  constructor(private radius: number) {
    super();
  }

  area(): number {
    return Math.PI * this.radius * this.radius;
  }
}
```

## 16. `readonly` and `static`

```ts
class Counter {
  static count = 0;

  readonly id = 1;

  constructor() {
    Counter.count++;
  }
}
```

- `readonly`: property cannot be reassigned after initialization
- `static`: property belongs to the class itself, not each instance

`readonly` is a compile-time restriction and is shallow: a readonly property that refers to an object does not automatically make that object's nested properties readonly. It is also not the same as a runtime-frozen object.

## 17. Generics

Generics allow one piece of code to work with many types.

```ts
function identity<T>(value: T): T {
  return value;
}

const a = identity<number>(10);
const b = identity("hello");
```

### Generic interfaces

```ts
interface Box<T> {
  value: T;
}

const box: Box<string> = { value: "abc" };
```

### Generic classes

```ts
class Storage<T> {
  private items: T[] = [];

  add(item: T) {
    this.items.push(item);
  }

  getAll(): T[] {
    return this.items;
  }
}
```

### Constraining generics

```ts
function printLength<T extends { length: number }>(value: T): number {
  return value.length;
}
```

This ensures `T` has a `length` property.

Generic type parameters are erased from emitted JavaScript. Unlike C++ templates, TypeScript does not generate a separate runtime implementation per type argument; unlike Java, generic arguments are not available for runtime inspection. To create a value of a generic type, pass a factory or constructor explicitly.

Prefer preserving the relationship between input and output types over widening to `any`:

```ts
function first<T>(items: readonly T[]): T | undefined {
  return items[0];
}
```

## 18. Type Assertions

Type assertions tell the compiler: "trust me, this is the type I want".

```ts
const input = document.getElementById("user") as HTMLInputElement;
```

You can also use angle-bracket syntax, but `as` is preferred in modern TypeScript.

```ts
const value = someValue as string;
```

Use assertions carefully; they do not change runtime behavior.

An assertion is not a cast that converts or validates a value. It only tells the compiler to trust the programmer, so an incorrect assertion can still fail at runtime. Prefer narrowing or runtime validation. The `satisfies` operator (TypeScript 4.9+) can check an expression against a type while retaining its more specific inferred type:

```ts
const defaults = {
  mode: "dark",
  retries: 3
} satisfies { mode: string; retries: number };
```

## 19. Nullish Coalescing and Optional Chaining

### Optional chaining

```ts
type UserProfile = {
  profile?: { name: string };
  settings?: { theme: string };
};

const user: UserProfile = {
  profile: {
    name: "Aisha"
  }
};

console.log(user.profile?.name);
console.log(user.settings?.theme);
```

The optional chain returns `undefined` when the value immediately to its left is `null` or `undefined`. For example, `user.settings?.theme` is safe if `settings` is absent, but it still throws if `user` itself is nullish; use `user?.settings?.theme` when both may be missing.

### Nullish coalescing

```ts
const username = input ?? "guest";
```

This uses a default only when the value is `null` or `undefined`.

Compare with `||`:

```ts
const count = 0 || 10; // 10
const safeCount = 0 ?? 10; // 0
```

## 20. `as const`

`as const` makes values read-only and preserves literal types.

```ts
const settings = {
  mode: "dark",
  premium: true
} as const;
```

This gives:

- `mode` is exactly `"dark"`
- `premium` is exactly `true`

This is a compile-time restriction, not a runtime freeze; it does not make the object immutable to JavaScript or external code.

## 21. Utility Types

TypeScript includes helpful built-in utility types.

```ts
interface Person {
  id: number;
  name: string;
  email: string;
}

type PartialPerson = Partial<Person>;
type RequiredPerson = Required<Person>;
type ReadonlyPerson = Readonly<Person>;
type PersonName = Pick<Person, "name">;
type PersonWithoutId = Omit<Person, "id">;
```

Common utility types:

- `Partial<T>`: makes all properties optional
- `Required<T>`: makes all properties required
- `Readonly<T>`: makes properties read-only
- `Pick<T, K>`: picks selected keys
- `Omit<T, K>`: removes selected keys
- `Record<K, T>`: maps keys to values
- `ReturnType<T>`: gets the return type of a function
- `Parameters<T>`: gets function parameter types

## 22. Modules and Imports

TypeScript supports ES modules.

```ts
// math.ts
export function add(a: number, b: number): number {
  return a + b;
}
```

```ts
// app.ts
import { add } from "./math";

console.log(add(2, 3));
```

### Default export

```ts
// logger.ts
export default function log(message: string) {
  console.log(message);
}
```

```ts
import log from "./logger";
log("Hello");
```

Imports and exports are resolved by the runtime and build configuration, not by the type system alone. A module import is also a module-scope boundary; files without imports or exports may be treated as scripts with shared global declarations. Use `import type` and `export type` when an import or export is needed only for types.

## 23. Async/Await and Promises

```ts
function fetchUser(): Promise<string> {
  return Promise.resolve("Alice");
}

async function loadUser() {
  const user = await fetchUser();
  console.log(user);
}
```

An `async` function always returns a `Promise`; returning a plain `T` is automatically wrapped as `Promise<T>`. `await` pauses the current async function until the promise settles, not the entire JavaScript thread. It does not by itself make CPU-bound work parallel.

Rejected promises throw at the `await` point and should be handled with `try`/`catch` or allowed to propagate to a caller that handles them. A TypeScript return annotation does not guarantee that a promise will resolve successfully.

## 24. `never` and Exhaustive Checks

```ts
type Shape =
  | { kind: "circle"; radius: number }
  | { kind: "square"; side: number };

function getArea(shape: Shape): number {
  switch (shape.kind) {
    case "circle":
      return Math.PI * shape.radius ** 2;
    case "square":
      return shape.side ** 2;
    default:
      const _exhaustive: never = shape;
      return _exhaustive;
  }
}
```

This pattern ensures all union variants are handled.

## 25. Strict Mode and Good Practices

Always enable strict mode in TypeScript projects:

```json
{
  "compilerOptions": {
    "strict": true
  }
}
```

Good practices:

- prefer `unknown` over `any`
- use literal unions instead of broad `string` when possible
- keep interfaces focused and minimal
- avoid unsafe type assertions unless necessary
- use `readonly` for values that should not change
- prefer small, explicit types over generic “catch-all” objects

## 26. Common TypeScript Patterns

### Conditional type

```ts
type IsString<T> = T extends string ? true : false;

type Test1 = IsString<string>; // true
type Test2 = IsString<number>; // false
```

### Mapped types

```ts
type ReadonlyKeys<T> = {
  readonly [K in keyof T]: T[K];
};
```

Mapped types transform existing object types in a reusable way.

## 27. JavaScript Runtime Semantics to Keep in Mind

These are JavaScript behaviors, not TypeScript-specific rules:

- Prefer `===` and `!==` for equality. `==` performs coercions that can make comparisons surprising.
- Conditions use truthiness: `0`, `""`, `false`, `null`, `undefined`, and `NaN` are falsy. Use explicit checks when zero or an empty string is valid data.
- Objects and arrays are reference values. Assigning or passing one copies the reference, not the object; two variables can refer to the same mutable object.
- `number` is IEEE 754 double precision. It does not distinguish integers from floating-point values, and very large integers may lose precision. Use `bigint` when integer precision beyond the safe integer range is required.
- JavaScript functions are values and can be passed around. Arrow functions capture the surrounding `this`; ordinary functions get `this` from how they are called. TypeScript can check a `this` type, but does not change that runtime rule.
- JavaScript can throw any value, not only `Error`. In a `catch` block, treat the caught value as unknown and narrow it before using error properties.

```ts
async function doWork(): Promise<void> {
  throw new Error("Operation failed");
}

async function run() {
  try {
    await doWork();
  } catch (error: unknown) {
    if (error instanceof Error) {
      console.error(error.message);
    } else {
      console.error("A non-Error value was thrown");
    }
  }
}
```

## 28. Runtime Validation at External Boundaries

Type annotations do not validate data returned from `JSON.parse`, HTTP responses, files, environment variables, or user input. Treat such values as `unknown`, check their shape, and only then use them as a trusted type.

```ts
type UserRecord = { id: number; name: string };

function isUserRecord(value: unknown): value is UserRecord {
  return (
    typeof value === "object" &&
    value !== null &&
    "id" in value &&
    typeof value.id === "number" &&
    "name" in value &&
    typeof value.name === "string"
  );
}

const parsed: unknown = JSON.parse('{"id":1,"name":"Aisha"}');

if (!isUserRecord(parsed)) {
  throw new Error("Invalid user payload");
}

console.log(parsed.name);
```

For larger schemas, use a runtime validation library or a dedicated parser rather than scattering assertions throughout the code. Keep validation at system boundaries so the rest of the application can work with trusted, well-defined types.

## 29. A Practical Development Workflow

1. Read the repository's `package.json`, `tsconfig.json`, and existing patterns before choosing compiler or module settings.
2. Run the project's type-check/build script. `tsc --noEmit` is useful when the project uses `tsc` directly and you only want type checking.
3. Add types to inputs and public boundaries; let the compiler infer obvious local values.
4. Represent state that has a small set of valid cases with discriminated unions, and handle every case exhaustively.
5. Keep runtime checks for untrusted data; a type assertion is not validation.
6. Test runtime behavior too. Type checking cannot prove correctness of algorithms, I/O, side effects, or external data.

The examples in these notes are standalone illustrations. Reusing declarations with the same name in one source file may merge interfaces or cause duplicate-identifier errors.

## 30. Summary

TypeScript is JavaScript with static typing and stronger tooling. It helps you catch mistakes earlier, model data more clearly, and write more maintainable programs.

Core ideas to remember:

- types describe values
- unions and intersections model complex data
- interfaces and type aliases define shapes
- generics make code reusable
- narrowing improves safety
- strict mode keeps code robust
- types are compile-time descriptions, not runtime validation
- JavaScript semantics still apply after compilation

With practice, TypeScript makes large systems easier to understand, extend, and debug.
