# TypeScript Notes

## 1. What is TypeScript?

TypeScript is a superset of JavaScript created by Microsoft. It adds static typing, tooling, and better editor support while keeping JavaScript runtime behavior intact.

Key benefits:

- catches errors before runtime
- improves code readability and maintainability
- helps with large codebases
- provides better IntelliSense in editors
- supports advanced patterns like generics and interfaces

Compile TypeScript to JavaScript:

```bash
tsc app.ts
```

This produces `app.js`.

## 2. TypeScript Configuration

A `tsconfig.json` file controls compiler behavior.

```json
{
  "compilerOptions": {
    "target": "ES2020",
    "module": "CommonJS",
    "strict": true,
    "esModuleInterop": true,
    "skipLibCheck": true,
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

## 3. Basic Types

TypeScript has primitive and reference types.

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

## 4. Type Inference

TypeScript often infers variable types automatically.

```ts
let message = "Hello"; // inferred as string
let count = 10; // inferred as number

const status = "success"; // literal type inferred as "success"
```

This reduces the need for explicit type annotations in many cases.

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

Interfaces define the shape of an object.

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
  return (pet as Fish).swim !== undefined;
}

const pet: Pet = { swim: () => console.log("Swimming") };

if (isFish(pet)) {
  pet.swim();
}
```

## 15. Classes

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

## 19. Nullish Coalescing and Optional Chaining

### Optional chaining

```ts
const user = {
  profile: {
    name: "Aisha"
  }
};

console.log(user.profile?.name);
console.log(user.settings?.theme);
```

If `profile` or `settings` is missing, no error is thrown.

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

Async functions must return `Promise<T>` or `Promise<void>`.

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

## 27. Summary

TypeScript is JavaScript with static typing and stronger tooling. It helps you catch mistakes earlier, model data more clearly, and write more maintainable programs.

Core ideas to remember:

- types describe values
- unions and intersections model complex data
- interfaces and type aliases define shapes
- generics make code reusable
- narrowing improves safety
- strict mode keeps code robust

With practice, TypeScript makes large systems easier to understand, extend, and debug.
