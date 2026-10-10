## Table of Contents

- [1. Fundamentals](#1-fundamentals)
- [2. Object-Oriented Programming](#2-object-oriented-programming)
- [3. Keywords and Essentials](#3-keywords-and-essentials)
- [4. Memory and Strings](#4-memory-and-strings)
- [5. Exception Handling](#5-exception-handling)
- [6. Collections Framework](#6-collections-framework)
- [7. Generics](#7-generics)
- [8. Multithreading and Concurrency](#8-multithreading-and-concurrency)
- [9. Java 8+ Features](#9-java-8-features)
- [10. JVM Internals](#10-jvm-internals)
- [11. Advanced Core Java](#11-advanced-core-java)
- [12. Java Platform Module System](#12-java-platform-module-system)
- [13. Modern Java Features](#13-modern-java-features)

# 1. Fundamentals

## 1.1 Java Platform Components

### JVM - Java Virtual Machine

- Executes Java bytecode and provides runtime services such as class loading, memory management, and garbage collection.
- The interpreter can run bytecode immediately; the JIT compiler turns frequently executed code into optimized native machine code.
- A JVM implementation is platform-specific, while compatible class files can run on any platform with a suitable JVM.
- "Write once, run anywhere" still depends on compatible Java versions, available libraries, and platform-specific behavior such as file paths.

### JRE - Java Runtime Environment

- Conceptually, a runtime consists of a JVM plus the libraries and supporting files needed to run applications.
- Since Java 11, many vendors distribute JDKs rather than a separate general-purpose JRE; deployments may also use custom runtime images.

### JDK - Java Development Kit

- Provides development tools such as `javac`, `javadoc`, and diagnostic utilities, along with a runtime in common distributions.
- Use a JDK to compile and develop; a runtime image may be enough to run an already-built application.

## 1.2 Variables and Data Types

- A variable has a declared type and scope. Fields receive default values; local variables must be definitely assigned before use.
  ```java
  int age;              // declaration
  age = 25;             // assignment
  final int MAX = 100;  // cannot be reassigned
  ```
- Primitive data types (8 total):

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

- A reference variable holds either `null` or a reference to an object; it is not a directly usable memory address.
  ```java
  String name = "Stitch";
  int[] scores = new int[5];
  ```
- `==` compares primitive values or reference identity. `.equals()` compares logical value only when the class implements it that way.
- Do not infer physical placement from source syntax: JVM implementations may optimize storage. The language guarantees default values for fields and array elements, not for local variables.

## 1.3 Operators and Type Casting

- Unary: `++a` increments before its value is used, while `a++` uses the current value and increments afterward. For example, `int b = a++;` assigns the old value of `a` to `b`. The `--` operator decrements; `!` reverses a boolean; `~` flips every bit in an integer.
- Arithmetic: `+`, `-`, `*`, `/`, and `%` perform addition, subtraction, multiplication, division, and remainder. With integers, `/` truncates toward zero (`7 / 2` is `3`), while `%` gives the remainder (`7 % 2` is `1`). Integer division by zero throws `ArithmeticException`.
- Relational: `==`, `!=`, `<`, `>`, `<=`, and `>=` compare values and produce a `boolean`. For primitives, `==` compares values; for object references, it checks whether both references point to the same object. Use `.equals()` to compare object content, such as strings.
- Logical: `&&` (AND) and `||` (OR) combine boolean expressions and short-circuit once the result is known. For example, `obj != null && obj.isReady()` avoids calling `isReady()` when `obj` is null. `!` negates a boolean expression.
- Ternary: `String res = marks > 40 ? "pass" : "fail";` evaluates the condition and returns the expression after `?` when true, or after `:` when false. The two result expressions must have compatible types; use this operator for concise choices rather than complex branching.
- `instanceof`: checks whether a non-null object is compatible with a type, e.g. `if (obj instanceof String)`. It returns `false` for `null`. Since Java 16, pattern matching can also declare a typed variable: `if (obj instanceof String text) { System.out.println(text.length()); }`.

- Type Casting:
    - Widening conversions are implicit, but can lose precision (for example, `long` to `float`).
    byte -> short -> int -> long -> float -> double
    - Narrowing conversions require a cast and may lose range or fractional information.
    double d = 100.99;
    int i = (int) d; // 100; fractional part discarded
    int big = 130;
    byte b = (byte) big; // -126 - overflow
- Upcasting is implicit; downcasting requires a runtime type check or may throw `ClassCastException`:
  ```java
  Parent parent = new Child();
  if (parent instanceof Child child) {
    child.childOnlyMethod();
  }
  ```

## 1.4 Control Flow

- **`if` / `else`:** Choose this when conditions express ranges or more complex boolean logic. Conditions are checked top-to-bottom and only the first matching branch runs, so test the most specific or highest-priority condition first. Use braces even for one-line bodies to make control flow clear.
  ```java
  char grade;
  if (score >= 90) {
    grade = 'A';
  } else if (score >= 75) {
    grade = 'B';
  } else {
    grade = 'C';
  }
  ```

- **`switch` statement:** Choose this for selecting among discrete values such as integral types except `long`, `String`, and enums. Colon-style cases fall through unless execution stops with `break`, `return`, or another control-flow statement; grouped labels can share a body. Include `default` when unmatched values need handling.
  ```java
  int month = 2;
  switch (month) {
    case 1:
      System.out.println("January");
      break;
    case 2:
      System.out.println("February");
      break;
    default:
      System.out.println("Other month");
  }
  ```

- **Switch expression (Java 14+):** Produces a value, and arrow cases do not fall through. It must be exhaustive; use `default` unless the compiler can establish that the cases cover all possible values (for example, all constants of an enum).
  ```java
  String monthName = switch (month) {
    case 1 -> "January";
    case 2 -> "February";
    default -> "Other month";
  };
  ```

- **Loops:** Repeat while a condition is true. Ensure the loop can make progress toward termination—for example, update its counter or consume input—or it may run indefinitely.
  ```java
  // for: initialization, test, and update are together; useful for counted repetition
  for (int i = 0; i < 10; i++) {
    if (i == 5) continue;
    System.out.println(i);
  }

  // while: tests before each iteration; useful when the count is not known in advance
  while (scanner.hasNext()) {
    process(scanner.next());
  }

  // do-while: tests after the body, so the body runs at least once
  do {
    update();
  } while (needsMoreWork());

  // for-each: visits every array element or Iterable element
  for (String item : items) {
    System.out.println(item);
  }
  ```
  Use an indexed loop when you need an element's position or must update array/list elements by index. A for-each loop over a collection does not provide the index.

- **Control-transfer statements:** `break` exits the nearest loop or `switch`; `continue` skips to the next iteration of the nearest loop; `return` exits the current method and may provide its result. A labeled `break` can exit an enclosing loop when searching nested structures.
  ```java
  outer:
  for (int i = 0; i < 3; i++) {
    for (int j = 0; j < 3; j++) {
      if (i == 1 && j == 1) {
        break outer; // exits both loops
      }
    }
  }
  ```

- **Short-circuit boolean operators:** `&&` skips its right operand when the left side is false; `||` skips it when the left side is true. This is useful for guarding operations such as dereferencing a possibly null value. `&` and `|` evaluate both operands when used with booleans.
  ```java
  if (text != null && !text.isEmpty()) {
    System.out.println(text);
  }
  ```

## 1.5 Input and Output

- **Output:** `print` stays on the current line; `println` appends the platform line separator. `printf` uses format specifiers and does not add a newline automatically.
  ```java
  String name = "Ava";
  int age = 28;
  double salary = 72500.5;

  System.out.print("Hello ");
  System.out.println(name);
  System.out.printf("Name: %s, age: %d, salary: %.2f%n", name, age, salary);
  System.err.println("Error messages go to the error stream.");
  ```

  Common format specifiers include `%s` (string), `%d` (integer), `%f` (floating-point), and `%%` (literal percent sign). Precision such as `%.2f` sets the number of digits after the decimal point; `%n` emits a platform-independent newline.

- **Input with `Scanner`:** convenient for small, interactive programs. `next()` reads one token; `nextLine()` reads the rest of the current line, including spaces.
  ```java
  import java.util.Scanner;

  Scanner sc = new Scanner(System.in);
  System.out.print("Age: ");
  int age = sc.nextInt();
  sc.nextLine(); // consume the rest of the line after nextInt()
  System.out.print("Name: ");
  String name = sc.nextLine();
  ```

  `nextInt()` and other token-reading methods leave the line separator in the input. If `nextLine()` follows one of them, consume the remainder first when you intend to read a new line. Do not close a `Scanner` wrapping `System.in` while other code still needs standard input; closing it also closes the underlying stream.

- **Buffered input:** `BufferedReader` is a good choice for larger input. It reads lines as strings, so convert values explicitly. `StringTokenizer` can split a line into whitespace-separated tokens.
  ```java
  import java.io.BufferedReader;
  import java.io.InputStreamReader;
  import java.util.StringTokenizer;

  BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
  String line = br.readLine(); // null when the input stream reaches EOF
  int value = Integer.parseInt(line.trim());

  StringTokenizer tokens = new StringTokenizer(br.readLine());
  int first = Integer.parseInt(tokens.nextToken());
  String second = tokens.nextToken();
  ```

  `Integer.parseInt` and similar parsing methods throw `NumberFormatException` for invalid input. Handle that exception when input is not guaranteed to be valid. As with `Scanner`, avoid closing a reader over `System.in` if the stream must remain available.

- **Password input:** `Console.readPassword()` avoids displaying typed characters, but `System.console()` may return `null` in an IDE, test runner, or redirected process.
  ```java
  import java.io.Console;
  import java.util.Arrays;

  Console console = System.console();
  if (console == null) {
      throw new IllegalStateException("No interactive console is available.");
  }
  char[] password = console.readPassword("Password: ");
  if (password == null) {
      throw new IllegalStateException("No password was entered.");
  }
  try {
      // Use the password without converting it to a String.
  } finally {
      Arrays.fill(password, '\0');
  }
  ```


## 1.6 Compilation, Execution, and Classpath

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

## 1.7 Primitive Details and Numeric Accuracy

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

## 1.8 Scope, Lifetime, and Parameter Passing

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

## 1.9 Arrays

- An array is a fixed-length object whose elements all have the same component type. Its length is available through the `length` field, and valid indexes run from `0` to `length - 1`.
- Declare an array reference with `type[]`; create the array with `new` or an initializer. The reference itself can be reassigned, but the array's length cannot change.
  ```java
  int[] scores = new int[3];       // elements default to 0
  scores[0] = 95;
  int[] ages = { 18, 21, 30 };     // declare and initialize
  String[] names = new String[2];  // elements default to null

  System.out.println(ages.length); // 3
  System.out.println(ages[0]);     // 18
  ```

- Array elements receive default values (`0`, `false`, or `null`, depending on the component type); a local array reference does not and must be assigned before use. Accessing an invalid index throws `ArrayIndexOutOfBoundsException`.
- Use an indexed loop when you need the position or want to update elements; use a for-each loop when you only need to visit each element.
  ```java
  for (int i = 0; i < scores.length; i++) {
    scores[i] += 5;
  }
  for (int score : scores) {
    System.out.println(score);
  }
  ```

- A multidimensional array is an array of arrays. Rows can have different lengths, or even be `null`.
  ```java
  int[][] grid = new int[2][3];  // two rows, three columns each
  int[][] triangle = { { 1 }, { 2, 3 }, { 4, 5, 6 } }; // jagged
  ```

- Arrays are reference types. Assigning an array variable copies its reference, not its elements; changes through either reference affect the same array. Use `Arrays.copyOf` or `clone()` for a shallow copy. For nested arrays, a shallow copy still shares the inner arrays.
- Arrays are covariant, so `Number[] values = new Integer[2]` compiles, but storing a non-`Integer` value through `values` throws `ArrayStoreException` at runtime. Prefer invariant generic collections when this runtime restriction is undesirable.
- `Arrays.equals` compares one-dimensional contents; use `Arrays.deepEquals` for nested arrays. `Arrays.sort` sorts in place. `Arrays.binarySearch` requires the array to already be sorted; otherwise its result is not meaningful.
  ```java
  int[] values = { 4, 1, 3 };
  int[] copy = java.util.Arrays.copyOf(values, values.length);
  java.util.Arrays.sort(copy);
  int index = java.util.Arrays.binarySearch(copy, 3); // index 1
  boolean sameContents = java.util.Arrays.equals(values, copy); // false
  ```

- For a resizable sequence, prefer `ArrayList`; for bulk operations and array utilities, see `java.util.Arrays`.

## 1.10 Literals and Compile-Time Constants

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

## 1.11 Expressions and Promotion Rules

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

## 1.12 Command-Line Arguments and Environment

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

## 1.13 Packages, JARs, and Manifests

A JAR is a ZIP archive containing classes, resources, and metadata.

```text
jar --create --file app.jar --main-class com.example.Main -C out .
java -jar app.jar
```

`META-INF/MANIFEST.MF` can declare `Main-Class`, implementation version, automatic module name, and other metadata. A normal executable JAR does not automatically include dependency JARs; use an application layout, module path, or deliberately built executable/fat JAR.

# 2. Object-Oriented Programming

Object-oriented programming organizes code around objects that combine state and behavior. The core ideas are encapsulation, abstraction, inheritance, and polymorphism. Use them to model clear responsibilities and contracts; a class is not automatically better just because it has more getters, setters, or inheritance.

## 2.1 Classes, Objects, and Constructors

- A class declares a reference type, its fields, constructors, and methods. An object is an instance with identity and state. Instance members belong to each object; static members belong to the class.
  ```java
  class Employee {
    private final String name;
    static String company = "Stitch";

    Employee(String name) {
      this.name = java.util.Objects.requireNonNull(name);
    }
  }
  ```
- Assigning an object variable copies the reference, not the object. Both variables below refer to the same `Employee`.
  ```java
  Employee first = new Employee("Ali");
  Employee second = first;
  ```
- A constructor initializes a new instance. It has the class name and no return type. If no constructor is declared, the compiler provides a no-argument constructor; declaring any constructor suppresses that default.
  ```java
  class Employee {
    private final String name;

    Employee() {
      this("Unknown"); // constructor delegation must be first
    }

    Employee(String name) {
      this.name = java.util.Objects.requireNonNull(name);
    }

    // Copy constructors are a convention, not a Java-generated feature.
    Employee(Employee other) {
      this(java.util.Objects.requireNonNull(other).name);
    }
  }
  ```
- `this(...)` delegates to another constructor in the same class and must be the first constructor statement. Constructor chaining centralizes validation and initialization.

## 2.2 Encapsulation and Accessors

- Encapsulation hides representation and exposes operations that preserve an object's invariants. It is not simply making fields private and generating a getter and setter for every field.
  ```java
  class BankAccount {
    private java.math.BigDecimal balance = java.math.BigDecimal.ZERO;

    public java.math.BigDecimal balance() {
      return balance;
    }

    public void deposit(java.math.BigDecimal amount) {
      if (amount == null || amount.signum() <= 0) {
        throw new IllegalArgumentException("amount must be positive");
      }
      balance = balance.add(amount);
    }
  }
  ```
- Prefer domain operations such as `deposit` and `withdraw` over unrestricted setters that let callers violate valid-state rules. For financial calculations, use `BigDecimal` with a documented scale and rounding policy instead of `double`.
- Abstraction presents a useful contract while hiding implementation details; encapsulation controls access to state and behavior.

## 2.3 Inheritance

- Inheritance forms an IS-A relationship: a subclass inherits accessible members and can specialize its superclass. Model substitutable types, not just opportunities to reuse code.
  ```java
  class Vehicle {
    void start() {
      System.out.println("Starting");
    }
  }

  class ElectricCar extends Vehicle {
    @Override
    void start() {
      System.out.println("Starting electric motor");
    }
  }
  ```
- `super(...)` invokes a superclass constructor and, when written explicitly, must be the first constructor statement. If omitted, Java inserts `super()`; compilation fails if the superclass has no accessible no-argument constructor. `super.field` and `super.method()` refer to inherited members.
- Common class hierarchies are single, multilevel, and hierarchical. A class can extend only one class but can implement multiple interfaces.
- Every class without an explicit superclass extends `Object`; interfaces do not extend `Object`.

## 2.4 Polymorphism

- **Overloading** gives methods the same name with different parameter lists. The compiler selects an overload using compile-time argument types and applicable conversions; return type alone cannot distinguish overloads.
  ```java
  class Calculator {
    int add(int a, int b) { return a + b; }
    int add(int a, int b, int c) { return a + b + c; }
    double add(double a, double b) { return a + b; }
  }
  ```
- **Overriding** lets a subtype provide an implementation of an inherited instance method. The runtime object's type selects the implementation.
  ```java
  class Bank {
    double getRate() { return 5.0; }
  }

  class SavingsBank extends Bank {
    @Override
    double getRate() { return 7.5; }
  }

  Bank account = new SavingsBank();
  System.out.println(account.getRate()); // 7.5
  ```
- Rules for overriding: the method must be inherited and have a subsignature; private methods are not inherited, static methods are hidden, and final methods cannot be overridden. An override cannot reduce visibility or broaden checked exceptions.

## 2.5 Abstraction: Abstract Classes and Interfaces

- Abstraction defines the behavior clients may rely on while hiding implementation choices. Abstract classes are useful for a related family that shares state or implementation; interfaces define contracts or capabilities that unrelated classes can implement.
  ```java
  abstract class Payment {
    abstract void pay();

    void printReceipt() {
      System.out.println("Receipt printed");
    }
  }

  interface Refundable {
    void refund();
  }

  class CardPayment extends Payment implements Refundable {
    @Override
    void pay() {
      System.out.println("Pay by card");
    }

    @Override
    public void refund() {
      System.out.println("Refund to card");
    }
  }
  ```
- An abstract class cannot be instantiated directly, but it can have constructors, instance state, and concrete methods. An interface cannot be instantiated and has no per-instance state; its fields are implicitly `public static final` constants and its abstract methods are implicitly `public`.
- Interfaces may also define `default` and `static` methods (Java 8+) and private helper methods (Java 9+). A class implementing an interface must provide public implementations of its abstract methods.

| Abstract class | Interface |
| --- | --- |
| A class extends one superclass | A class can implement multiple interfaces |
| Can have constructors and instance state | No constructors or per-instance state |
| Can provide shared state and implementation | Defines a contract; can also provide default and static methods |
| Useful for a closely related type hierarchy | Useful for capabilities and decoupled contracts |

Use the narrowest useful abstraction: consumers should depend on the operations they need rather than a specific implementation.

## 2.6 Access Modifiers

Controls visibility:
| Modifier | Same Class | Same Package | Child (diff pkg) | World |
| --- | --- | --- | --- | --- |
| private | YES | NO | NO | NO |
| default (no keyword) | YES | YES | NO | NO |
| protected | YES | YES | YES, through subclass access rules | NO |
| public | YES | YES | YES | YES |

public class A {
  private int a = 1; // only inside A
  int b = 2; // default - package only
  protected int c = 3; // package + child outside pkg
  public int d = 4; // anywhere
}

## 2.7 Composition, Association, and Aggregation

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

## 2.8 Initialization Order

For a newly created instance, class initialization happens first if needed; instance initialization then follows this order:

1. Memory allocation with instance fields set to default values.
2. Parent instance field initializers and initializer blocks.
3. Parent constructor body.
4. Child instance field initializers and initializer blocks.
5. Child constructor body.

Each superclass constructor runs after that class's instance initializers and before the subclass's initializers. Calling an overridable method from a constructor is dangerous because subclass fields may not yet be initialized.

## 2.9 Method Dispatch and Covariant Returns

- Instance methods are dynamically dispatched from the runtime object type.
- Fields, static methods, and private methods are resolved from the reference or declaring type and are not polymorphic.
- An overriding method may return a subtype of the parent's return type.
- It cannot throw broader checked exceptions than the overridden method.
- It may widen access, such as `protected` to `public`, but cannot narrow access.

## 2.10 Immutability

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

Immutability simplifies equality, caching, and thread safety, although copying large mutable inputs may have a cost. A `final` reference only prevents reassignment; it does not freeze the referenced object.

## 2.11 Nested Classes

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

## 2.12 Enums as Full Classes

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

## 2.13 Object Methods

Important methods inherited from `Object`:

- `toString`: human-readable representation; avoid including secrets.
- `equals` and `hashCode`: logical identity and hash-based collection behavior.
- `getClass`: exact runtime class.
- `clone`: protected shallow-copy mechanism; usually prefer constructors/factories.
- `wait`, `notify`, `notifyAll`: intrinsic-monitor coordination.

When inheritance is allowed, decide whether equality uses `instanceof` or exact `getClass()` checks. Exact-class equality avoids many symmetry problems; value-based hierarchies require particularly careful design.

## 2.14 Tell, Do Not Ask

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

# 3. Keywords and Essentials

This section summarizes language features that recur across Java code. The keyword's effect depends on where it is used: for example, `static` changes member ownership, while `final` prevents reassignment or overriding but does not automatically make an object immutable.

## 3.1 `this` Keyword

`this` refers to the current object in an instance context. It is commonly used to distinguish a field from a parameter, call another instance member, pass the current object, or delegate to another constructor.

```java
class Employee {
  private final String name;

  Employee() {
    this("Unknown"); // delegates to another constructor; must be first
  }

  Employee(String name) {
    this.name = java.util.Objects.requireNonNull(name);
  }

  String name() {
    return this.name;
  }
}
```

`this(...)` is constructor delegation and must be the first constructor statement. `this` cannot be used in a static context because a static member is not invoked on a particular instance. Avoid letting `this` escape during construction, such as by registering it with another object before initialization is complete.

## 3.2 `super` Keyword

`super` accesses an inherited superclass member or explicitly invokes a superclass constructor. It is not a separate reference that can be saved or passed around.

```java
class Parent {
  int value = 10;

  Parent(String label) {
    System.out.println(label);
  }

  void show() {
    System.out.println("Parent");
  }
}

class Child extends Parent {
  int value = 20;

  Child() {
    super("creating child"); // must be the first constructor statement
  }

  @Override
  void show() {
    System.out.println(value);       // 20
    System.out.println(super.value); // 10
    super.show();                    // invoke the superclass implementation
  }
}
```

If a constructor does not explicitly start with `this(...)` or `super(...)`, Java inserts `super()`. That implicit call only compiles when the superclass has an accessible no-argument constructor. `this(...)` and `super(...)` cannot both be written in the same constructor because either call must be first.

## 3.3 `final` Keyword

`final` has different effects depending on the declaration:

- **Variable:** can be assigned only once. A blank `final` field may be assigned in a constructor. A `final` reference cannot be reassigned, but the referenced object may still be mutable.
- **Method:** cannot be overridden by subclasses.
- **Class:** cannot be subclassed.

```java
final int max = 100;
// max = 200; // compile-time error

class Parent {
  final void pay() {}
}

final class Token {}
// class Child extends Token {} // compile-time error
```

`final` can help express invariants and prevent accidental extension, but it is not by itself a security boundary or a guarantee of deep immutability.

## 3.4 `static` Keyword

Static fields and methods belong to the class rather than an individual instance. They are associated with the class as defined by a particular class loader.

```java
class Employee {
  private final String name;        // each instance has its own name
  static String company = "Stitch"; // shared by instances of this class

  Employee(String name) {
    this.name = name;
  }

  static void changeCompany(String newName) {
    company = newName;
    // An instance field such as name cannot be accessed without an Employee object.
  }

  void display() {
    System.out.println(name + " " + company);
  }

  static {
    System.out.println("Runs during class initialization");
  }
}

Employee.changeCompany("Example"); // qualify static access with the class name
```

- A static method has no current instance, so it cannot use `this` or `super` and cannot directly access instance members.
- A static initializer runs as part of class initialization on first active use, not necessarily before `main` in every program.
- Static methods are hidden, not overridden. For a call through a reference, the compile-time type determines which hidden static method is selected; prefer calling static methods through the class name.
- Mutable static state is shared and can require synchronization when accessed by multiple threads.

## 3.5 Packages and Imports

Packages organize related types and help avoid simple-name collisions. The package declaration identifies the type's package; source directories conventionally mirror that package name, but the declaration determines the package.

```java
package com.example.payment; // must precede imports and type declarations

import java.util.Scanner;
import static java.lang.Math.sqrt;

public class Payment {
  Scanner scanner;
  double root = sqrt(25);
  java.util.Date date; // fully qualified name needs no import
}
```

- `java.lang` types such as `String`, `System`, and `Math` are available without an explicit import.
- `import java.util.*` imports accessible types in `java.util`, not its subpackages. Imports do not load classes or add dependencies; they only make source names shorter.
- Static imports make static members available without qualifying them; use them selectively so the origin of a name remains clear.
- Compile with `javac -d out ...` to write class files into package-structured directories under `out`. For named modules, see section 12.

## 3.6 Wrapper Classes and Autoboxing

Wrapper classes represent primitive values as objects. They are needed where an object type is required, such as a generic collection or a generic type argument; Java collections do not store primitive values directly.

| Primitive | Wrapper (in java.lang) |
| --- | --- |
| `byte` | `Byte` |
| `short` | `Short` |
| `int` | `Integer` |
| `long` | `Long` |
| `float` | `Float` |
| `double` | `Double` |
| `char` | `Character` |
| `boolean` | `Boolean` |

```java
int primitive = 10;
Integer boxed = Integer.valueOf(primitive); // boxing
Integer automatic = primitive;              // autoboxing
int unboxed = boxed;                        // auto-unboxing

List<Integer> values = new ArrayList<>(); // List<int> is not legal
values.add(10);                           // boxing
int first = values.get(0);                // unboxing
```

- Unboxing `null` throws `NullPointerException`; check for null or use an appropriate default before unboxing.
- Prefer `Integer.parseInt("123")` when a primitive `int` is needed, and `Integer.valueOf("123")` when an `Integer` object is needed. `toString` and constants such as `Integer.MAX_VALUE` are also commonly used.
- `Integer.valueOf` is guaranteed to reuse instances for at least values from -128 through 127; implementations may cache more. Never use `==` to compare wrapper values—use primitive comparison after null validation or use `equals()` for object values.
- Autoboxing may allocate objects and can hide null-unboxing or performance costs in tight loops. Use primitive-specialized APIs where measurement shows boxing is significant.

## 3.7 Important Modifiers

- `abstract`: declares an incomplete class or method.
- `synchronized`: acquires an intrinsic monitor for mutual exclusion and visibility.
- `volatile`: provides visibility and ordering for one field, not compound-operation atomicity.
- `transient`: excludes an instance field from default Java serialization.
- `native`: declares a method implemented outside Java through JNI.
- `strictfp`: historically enforced strict floating-point behavior; since Java 17, floating-point operations are always strict.

## 3.8 `final` vs Immutability

`final` prevents reassignment; it does not make a referenced object immutable:

```java
final List<String> names = new ArrayList<>();
names.add("Ali");              // allowed
// names = new ArrayList<>();  // not allowed
```

Correctly constructed `final` fields also have safe-publication guarantees, provided `this` does not escape during construction.

## 3.9 Static Initialization

- A class initializes on first active use, such as construction, static method invocation, or access to a non-constant static field.
- Compile-time constants may be inlined and may not trigger initialization.
- If initialization throws, the first access receives `ExceptionInInitializerError`; later access commonly receives `NoClassDefFoundError`.
- Avoid heavy I/O, networking, or recoverable configuration work in static initializers.

## 3.10 Imports and Name Resolution

- Imports affect source-name resolution only; they do not load classes or add dependencies.
- Wildcard imports do not include subpackages.
- Use static imports sparingly, where they improve readability, such as test assertions.
- If imported classes share a simple name, use a fully qualified name for at least one.

## 3.11 `this`, `super`, and Dispatch During Construction

- `this(...)` delegates to another constructor in the same class.
- `super(...)` delegates to a parent constructor.
- One of them may be the first constructor statement; if neither is written, the compiler inserts a no-argument `super()`.
- Constructor delegation must eventually reach a superclass constructor.
- Dynamic dispatch still applies inside constructors, which is why invoking overridable methods there is unsafe.

## 3.12 Access Across Packages

`protected` has two distinct forms of access:

1. Any class in the same package can access the member.
2. A subclass in another package can access it through inheritance, subject to reference-type restrictions.

An out-of-package subclass cannot use an arbitrary parent instance to access the parent's protected member. Prefer protected methods over protected mutable fields.

## 3.13 Annotation Basics

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

## 3.14 Initialization-on-Demand Holder

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

# 4. Memory and Strings

## 4.1 Heap, Stack, and Metaspace

- **Heap:** stores objects and arrays during their lifetime. The garbage collector can reclaim an object when it is no longer reachable from live code; collection timing is not guaranteed. JIT optimizations may eliminate or transform some allocations, so source-level `new` does not always imply a physical heap allocation.
- **JVM stack:** each thread has its own stack of method frames. Frames track method execution, including local variables and intermediate computation state. The JVM specification does not require every local value or reference to physically reside on a native stack.
- **Metaspace:** HotSpot commonly stores class metadata in native memory. This is distinct from the heap and may be limited with JVM options such as `-XX:MaxMetaspaceSize`.
- Static fields are associated with their class and are not themselves ordinary objects stored in Metaspace; an object referenced by a static field is still an object. The string intern pool is on the heap in modern Java.
- `StackOverflowError` commonly results from excessive recursion. `OutOfMemoryError` may result from heap or native-memory exhaustion, including Metaspace exhaustion. These are different resource failures; increasing one memory limit does not necessarily address the other.

## 4.2 String Pool

- String literals and compile-time constant string expressions are interned. Equal literals in the same runtime can therefore refer to the same canonical object. Use `equals()` for content comparisons; do not rely on pooling for correctness.
  ```java
  String first = "Java";
  String second = "Java";
  System.out.println(first == second); // true: both are the same interned literal
  System.out.println(first.equals(second)); // true: contents are equal
  ```
- A string created at runtime is not guaranteed to be interned. `intern()` returns the canonical pooled reference for the same contents; compare strings by content unless identity is specifically required.
  ```java
  String created = new String("Java");
  String canonical = created.intern();
  System.out.println(created == first);   // false: explicitly created distinct object
  System.out.println(canonical == first); // true: canonical pooled reference
  ```
- Pooling can reduce duplicate string storage in suitable cases, but interning large amounts of unbounded or user-generated text can increase memory pressure. It is not a replacement for a cache with an explicit eviction policy.

## 4.3 String, StringBuilder, and StringBuffer

| Feature | String | StringBuilder | StringBuffer |
| --- | --- | --- | --- |
| Property | `String` | `StringBuilder` | `StringBuffer` |
| --- | --- | --- | --- |
| Mutable? | No; immutable | Yes | Yes |
| Synchronization | Safe to share as an immutable value | Not synchronized | Most methods are synchronized |
| Typical use | General text values, map keys | Building text locally, especially in loops | Legacy APIs requiring synchronized mutable text |

- Immutability makes strings safe to share and gives them stable hash codes. It does not make `String` a secure password container: secrets may remain in memory until the strings are collected, and copies may be made.
- `+` creates a new string value. Simple concatenation expressions are commonly optimized by compilers; repeated concatenation in a loop can repeatedly copy the growing result. Use `StringBuilder` when incrementally building text.
  ```java
  StringBuilder builder = new StringBuilder("Java");
  builder.append(" ").append("notes");
  builder.reverse();
  String result = builder.toString();
  ```
- A builder has a mutable capacity and may resize as text is appended. If the approximate output size is known, an initial capacity can reduce resizing:
  ```java
  StringBuilder output = new StringBuilder(256);
  for (String part : parts) {
    output.append(part);
  }
  ```
- `StringBuffer` synchronizes its individual methods, but that does not automatically make a sequence of multiple calls an atomic operation. Prefer `StringBuilder` for thread-confined use; use explicit coordination when multiple threads must safely share a mutable text buffer.

## 4.4 `equals()` and `==`

- For primitives, `==` compares values. For references, `==` tests identity: whether both references point to the same object. It does not compare object contents or expose a memory address.
- `equals(Object)` defines logical equality. The default implementation inherited from `Object` is identity-based; value types such as `String` override it to compare contents. A class should override it when the domain defines instances with equal values.
  ```java
  String first = "Java";
  String second = "Java";
  String constructed = new String("Java");

  System.out.println(first == second);       // true here: interned literals
  System.out.println(first == constructed);  // false: distinct objects
  System.out.println(first.equals(constructed)); // true: equal contents

  int left = 10;
  int right = 10;
  System.out.println(left == right); // true: primitive values are equal

  Employee one = new Employee("Ava");
  Employee two = new Employee("Ava");
  System.out.println(one == two);      // false: distinct references
  System.out.println(one.equals(two)); // depends on Employee's equals implementation
  ```
- Never use `==` for `String` content comparison; pooling can make it appear to work for some strings. Wrapper types also represent objects, so `==` may compare identity and wrapper caching can make results surprising. Unboxing a null wrapper throws `NullPointerException`.
- `equals()` must be reflexive, symmetric, transitive, and consistent, and must return false for null. Whenever overriding `equals`, override `hashCode` as well: equal objects must have equal hash codes. Unequal objects may share a hash code.
- A value-based implementation should compare the same significant fields in both `equals` and `hashCode`, and should account for nulls. For example:
  ```java
  final class Employee {
    private final String name;

    Employee(String name) {
      this.name = java.util.Objects.requireNonNull(name);
    }

    @Override
    public boolean equals(Object other) {
      if (this == other) return true;
      if (other == null || getClass() != other.getClass()) return false;
      Employee employee = (Employee) other;
      return name.equals(employee.name);
    }

    @Override
    public int hashCode() {
      return name.hashCode();
    }
  }
  ```
- `Objects.equals(a, b)` is a null-safe way to compare two references: it returns true when both are null and otherwise delegates to `a.equals(b)`. It does not make an object's `equals` implementation correct.
- Array equality is also a common pitfall: arrays inherit identity-based `equals`; use `Arrays.equals` for one-dimensional contents and `Arrays.deepEquals` for nested arrays.
- Do not use mutable fields in the equality/hash calculation of keys stored in `HashMap` or elements in `HashSet`. Mutating such a field can make an entry unreachable through normal lookup.
- For inheritance hierarchies, equality must preserve symmetry and transitivity. Exact-class checks avoid common subclass asymmetry problems; value-based hierarchies need a deliberate equality design. Prefer immutable value types when practical.

## 4.5 Unicode and String Operations

Java `String` uses UTF-16 code units. String indexes and methods such as `length()`, `charAt()`, and `substring()` operate on code-unit positions, not user-perceived characters:

```java
String value = "A😀";
System.out.println(value.length()); // 3 UTF-16 code units
System.out.println(value.codePointCount(0, value.length())); // 2 Unicode code points
System.out.println(value.charAt(1)); // first half of the emoji's surrogate pair
```

Use code-point APIs when processing Unicode code points, especially for supplementary characters outside the Basic Multilingual Plane:
```java
int codePoint = value.codePointAt(1);
String fromCodePoints = new String(Character.toChars(codePoint));
value.codePoints().forEach(System.out::println);
```

Code points are not always the same as user-perceived characters (grapheme clusters): a displayed character can consist of multiple code points, such as a base letter plus a combining mark. Use a Unicode-aware text or grapheme segmentation library when user-visible character boundaries matter.

Unicode strings may have canonically equivalent representations. Normalize when the application needs consistent comparison or storage, while choosing the normalization form according to the data's requirements:
```java
String normalized = java.text.Normalizer.normalize(
    input, java.text.Normalizer.Form.NFC);
```

Locale-sensitive case conversion should specify a locale. Use `Locale.ROOT` for machine-readable identifiers and a user locale for display text:

```java
String key = input.toLowerCase(java.util.Locale.ROOT);
```

Use `equalsIgnoreCase()` only when its locale-independent semantics fit the domain; language-aware sorting and comparison may require `Collator`.

## 4.6 Concatenation and Formatting

- Use `+` for short, readable expressions. The compiler commonly optimizes concatenation within one expression; use `StringBuilder` for incrementally assembling text, particularly in loops.
- Use `String.join` or `Collectors.joining` to combine values with a delimiter without manually handling separators:
  ```java
  String csv = String.join(", ", names);
  ```
- `String.formatted(...)` (Java 15+) and `String.format(...)` use format specifiers such as `%s`, `%d`, and `%.2f`. They do not append a newline unless the format includes `%n`; use `printf` when writing formatted output directly.
  ```java
  String message = "User: %s, score: %.1f".formatted(name, score);
  ```
- Formatting is useful for presentation but generally more expensive than direct appends in hot paths. Specify a locale when numeric or date formatting must be predictable; default-locale formatting can vary between machines.
- Formatting does not escape data for another language or protocol. Never construct SQL by concatenating or formatting values; use prepared statements with parameters. Apply the appropriate contextual encoding for HTML, shell commands, URLs, or other output formats.

## 4.7 Defensive String Handling

- Validate and normalize input at the boundary, then keep the canonical form consistent throughout the application. Decide explicitly whether leading/trailing whitespace is allowed; do not silently normalize identifiers if doing so could change their meaning.
- `isBlank()` treats a string containing only Java whitespace characters as blank. `strip()` trims according to `Character.isWhitespace`; `trim()` removes only characters up to U+0020. None of these methods removes every invisible Unicode format character.
  ```java
  String candidate = java.util.Objects.requireNonNull(input, "input").strip();
  if (candidate.isEmpty()) {
    throw new IllegalArgumentException("Value must not be blank");
  }
  ```
- Choose case handling according to the domain. `toLowerCase(Locale.ROOT)` can help canonicalize machine identifiers, but case folding and `equalsIgnoreCase()` are not substitutes for locale-aware human-language comparison or a documented identifier policy.
- Escape or encode untrusted text for the specific output context (HTML, SQL, shell, URL, and so on); trimming or validating a string does not make it safe for every context. Use parameterized APIs such as prepared statements for SQL.
- Prefer APIs that accept `char[]` for secrets and clear the array when finished, but this only reduces exposure: input handling, libraries, logging, garbage collection, and conversions may create copies. Avoid turning secrets into `String` where possible, and never log them.

## 4.8 Reference Strengths and Cleanup

- **Strong references** are the normal kind: an object remains reachable while live code can reach it through strong references. Garbage collection does not provide a deterministic cleanup schedule.
- **Soft references** may be cleared under memory pressure. Their timing is unpredictable, so they are generally a poor basis for application caches; use a cache with explicit size, expiry, and eviction policy.
- **Weak references** do not keep their referents alive. They are useful for certain associations whose values should not extend key/object lifetimes, but must be designed around the possibility that the referent disappears at any time. `WeakHashMap` uses weak keys, not weak values; a map value that strongly refers back to its key can prevent that key from being collected.
- **Phantom references** allow cleanup coordination after an object becomes phantom reachable. Their referent cannot be retrieved; use a `ReferenceQueue` and keep the cleanup state separate from, and not strongly referencing, the object being collected.
- Reference objects do not replace deterministic resource management. Close files, sockets, and similar resources with try-with-resources:
  ```java
  try (var reader = java.nio.file.Files.newBufferedReader(path)) {
    // Read from the file.
  }
  ```
- Finalization is deprecated for removal and has unpredictable timing. `Cleaner` can be a last-resort safety net for native or other resources, but its action is not prompt or guaranteed to run before process exit; explicit `close()` remains the primary lifecycle mechanism.

## 4.9 String Internals and Compact Strings

In modern HotSpot JDKs, compact strings can store string contents using a byte array and a coder that selects a compact Latin-1 representation or UTF-16 when needed. This is an implementation detail, not an API guarantee.

- Use the public `String` APIs; never depend on its backing representation, object layout, or memory footprint.
- Current common JDK implementations copy the relevant contents for `substring` rather than retaining the original full backing array. Do not assume this storage behavior across all Java implementations or use it as an API contract.
- `String` hash codes can be cached because strings are immutable. Application code should rely on the documented `hashCode()` result, not whether or when it is cached.
- Interning unbounded dynamic input may keep many canonical strings reachable. It is not a general-purpose cache; use an explicitly bounded cache when caching is needed.

## 4.10 Regular Expressions

Java regular expressions are described by `Pattern` and applied with a `Matcher`. Regex syntax is embedded in a Java string literal, so backslashes usually need to be doubled:

```java
import java.util.regex.Matcher;
import java.util.regex.Pattern;

private static final Pattern POSTAL_CODE =
    Pattern.compile("\\d{5}");

boolean valid = POSTAL_CODE.matcher(input).matches();
```

Common building blocks:

| Syntax | Meaning | Example |
| --- | --- | --- |
| `.` | Any character (except line terminators by default) | `a.c` |
| `\d`, `\w`, `\s` | Digit, word character, whitespace | `\d+` |
| `[abc]`, `[^abc]` | Character set, or its negation | `[A-F0-9]+` |
| `[a-z]` | Character range | `[a-z]+` |
| `*`, `+`, `?`, `{n,m}` | Zero-or-more, one-or-more, optional, bounded repetitions | `\\d{1,3}` |
| `^`, `$` | Line/input boundaries, depending on flags | `^start` |
| `(...)`, `(?:...)` | Capturing and non-capturing groups | `(ab)+` |
| `|` | Alternation | `cat|dog` |

`Pattern` supports more than basic syntax, including lookarounds, named groups, and flags. Use `Pattern.CASE_INSENSITIVE` or inline flags such as `(?i)` when case-insensitive matching is needed; Unicode-aware case behavior can require `Pattern.UNICODE_CASE` as well.

Choose the `Matcher` operation based on whether the pattern should cover all input or find part of it:
```java
Pattern wordPattern = Pattern.compile("[A-Za-z]+");
Matcher matcher = wordPattern.matcher("Java 21");

boolean wholeInputIsWord = matcher.matches(); // false: requires the whole region
boolean containsWord = matcher.find();        // true: finds "Java"
String firstWord = matcher.group();           // "Java", after a successful find()
```

Capture groups retrieve parts of a match with `group(1)`, `group(2)`, and so on; group zero is the complete match. Check that a match succeeded before calling `group()`, or `IllegalStateException` is thrown. `Matcher.find()` can be called repeatedly to find successive matches.

For replacement and splitting, use the dedicated APIs. In replacement strings, `$1` refers to a captured group and a backslash quotes the next replacement character; use `Matcher.quoteReplacement` if replacement text is literal.
```java
String masked = Pattern.compile("\\d")
    .matcher("Order 123")
    .replaceAll("*"); // "Order ***"

String[] fields = Pattern.compile(",")
    .split("one,two,three");
```

- Compile reused patterns once, for example as `static final Pattern` constants; `Pattern` instances are immutable and safe to share. A `Matcher` holds per-input state, so create one per operation or thread.
- Keep Java string escaping and regex escaping distinct: regex `\d+` must be written as `"\\d+"` in a Java string. A character class such as `[.]` matches a literal period without escaping; outside a character class, use `\\.`.
- Anchors and character classes have details: `matches()` already requires a full-region match, while `^` and `$` are affected by multiline mode and line terminators. By default, `\d` and `\w` have ASCII-oriented behavior; enable Unicode character classes with `Pattern.UNICODE_CHARACTER_CLASS` when appropriate.
- Avoid catastrophic backtracking on attacker-controlled input. Bound input length, prefer clear and constrained patterns, and consider possessive quantifiers or atomic groups when they preserve the intended match. Do not assume a regex is safe merely because it is short.
- Regex can validate syntax, not necessarily business meaning. For example, a simple email-shaped pattern does not establish that an address exists or satisfies every valid email-address rule; use domain-specific parsing or verification where required.

## 4.11 Character Encoding

Characters in a Java `String` are not bytes. When text crosses a file, network, or other byte-oriented boundary, encode and decode it with an explicit charset:

```java
byte[] bytes = text.getBytes(java.nio.charset.StandardCharsets.UTF_8);
String restored = new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
```

- UTF-8 is variable-length and widely interoperable. Use `StandardCharsets.UTF_8` for portable data formats and protocols unless their specification requires another charset.
- Avoid default-charset overloads for persistent or network data: their behavior can depend on the Java version, runtime configuration, or platform. State the charset explicitly in readers, writers, and file APIs too.
- Some text files begin with a byte-order mark (BOM). BOM handling is format-specific; decide whether to preserve, remove, or reject it rather than assuming every decoder removes it.
- Convenience decoding methods can replace malformed input. If invalid byte sequences must be rejected or reported, configure a decoder explicitly:
  ```java
  java.nio.charset.CharsetDecoder decoder =
      java.nio.charset.StandardCharsets.UTF_8.newDecoder()
          .onMalformedInput(java.nio.charset.CodingErrorAction.REPORT)
          .onUnmappableCharacter(java.nio.charset.CodingErrorAction.REPORT);
  String strict = decoder.decode(java.nio.ByteBuffer.wrap(bytes)).toString();
  ```
- Encoding and decoding are inverse operations only when the same charset is used and the input bytes are valid for that charset. A charset does not define application-level normalization or remove a BOM automatically.

## 4.12 String Comparison and Collation

- `equals()` compares exact string contents; `compareTo()` provides a lexicographic ordering based on UTF-16 code units. Neither performs locale-aware dictionary comparison, Unicode normalization, or general linguistic case folding.
- Use `Collator` to sort human-readable text according to a locale. Collation behavior varies by locale and configuration; set strength or decomposition when the product's comparison rules require it.
  ```java
  java.text.Collator collator =
      java.text.Collator.getInstance(userLocale);
  names.sort(collator);
  ```
- Collation can consider distinct strings equivalent at a given strength. If a total, deterministic order is needed for ties (for example in pagination), add an explicit tie-breaker such as the original string's `compareTo`.
- Canonically equivalent Unicode sequences can have different code-unit representations. Normalize both values to the same form when the domain requires canonical equivalence:

```java
String left = java.text.Normalizer.normalize(
    input, java.text.Normalizer.Form.NFC);
String right = java.text.Normalizer.normalize(
    other, java.text.Normalizer.Form.NFC);
boolean equalAfterNormalization = left.equals(right);
```

Normalization and case handling have domain-specific security implications. For security-sensitive identifiers, define which characters and equivalences are allowed, normalize consistently at boundaries, and avoid treating visual similarity as equality.

# 5. Exception Handling

## 5.1 Exception Hierarchy

Every throwable extends `Throwable`, whose two main branches are `Error` and `Exception`:

```text
Throwable
├── Error                  unchecked
└── Exception
    ├── checked exceptions (except RuntimeException subclasses)
    └── RuntimeException   unchecked
```

Checked or unchecked describes the compiler's handling requirement, not whether a failure occurs at compile time or runtime. Java requires checked exceptions to be caught or declared; unchecked exceptions do not have that requirement. Applications normally do not catch `Error`, which usually indicates a serious JVM or environment problem, but recoverability depends on the specific failure and process boundary.

## 5.2 Checked and Unchecked Exceptions

| Checked | Unchecked |
| --- | --- |
| Must be caught or declared with `throws` | No catch-or-declare requirement |
| Often represents an external condition callers may be able to handle | Often represents invalid arguments, invalid state, or programming defects |
| Examples: `IOException`, `SQLException`, `ClassNotFoundException` | Examples: `NullPointerException`, `ArithmeticException`, `IndexOutOfBoundsException`, `NumberFormatException` |

Checked exceptions extend `Exception` but not `RuntimeException`; unchecked exceptions include all `RuntimeException` subclasses and all `Error` subclasses. Choose checked versus unchecked based on the API contract and whether callers can reasonably take meaningful action, not on a blanket rule.

```java
// Checked: handle the failure or declare it.
try (var reader = java.nio.file.Files.newBufferedReader(path)) {
  return reader.readLine();
} catch (java.io.IOException e) {
  throw new IllegalStateException("Could not read input", e);
}

// Unchecked: compiles without catch-or-declare, but can fail at runtime.
int quotient = 10 / divisor; // ArithmeticException if divisor is zero
```

## 5.3 `try`, `catch`, `finally`, `throw`, and `throws`

`try` contains code that may fail. A matching `catch` handles a thrown exception; order handlers from more specific to more general because a superclass catch would make later subtype handlers unreachable.

```java
try {
  int result = numerator / denominator;
  System.out.println(result);
} catch (ArithmeticException e) {
  System.err.println("Cannot divide by zero");
} catch (RuntimeException e) {
  System.err.println("Unexpected runtime failure");
}
```

- `throw` raises a particular throwable from a statement; `throws` in a method declaration documents exceptions that may propagate. Checked exceptions must be caught or declared by the calling code.
  ```java
  static void requireAdult(int age) {
    if (age < 18) {
      throw new IllegalArgumentException("age must be at least 18");
    }
  }

  static String readFirstLine(java.nio.file.Path path)
      throws java.io.IOException {
    try (var reader = java.nio.file.Files.newBufferedReader(path)) {
      return reader.readLine();
    }
  }
  ```
- `finally` runs when ordinary control flow leaves the associated `try`/`catch`, including when an exception propagates or a `return` is executed. It is not guaranteed if the process or JVM terminates abruptly. Avoid `return` or throwing from `finally`: doing so can hide an earlier result or replace the original exception.
- Use try-with-resources for `AutoCloseable` resources. Resources initialize left-to-right and close in reverse order; if both the body and a close operation fail, close failures are attached as suppressed exceptions to the primary throwable.
  ```java
  try (var reader = java.nio.file.Files.newBufferedReader(path)) {
    return reader.readLine();
  }
  ```
- A `try` may have `catch` blocks, a `finally` block, or both. A try-with-resources statement may stand alone or be followed by `catch`/`finally`.

## 5.4 Exception Flow Examples

- **A matching handler exists:** control transfers from the throwing point to the first matching `catch`; after it completes, `finally` runs if present, then execution continues after the statement.
- **No exception occurs:** `catch` blocks are skipped; `finally` runs if present, then execution continues.
- **No matching handler exists:** `finally` runs if present, then the original exception propagates up the call stack. An exception thrown while running `finally` can replace the original, which is one reason to keep cleanup code simple.
- **A method returns inside `try`:** `finally` runs before the method returns. A return from `finally` overrides the pending return and should be avoided.

## 5.5 Custom Exceptions

Create a custom exception when it gives callers a meaningful, stable way to distinguish a domain or application failure. Extend `Exception` for a checked exception or `RuntimeException` for an unchecked one; retain a cause when translating another failure.

```java
class InsufficientFundsException extends Exception {
  InsufficientFundsException(String message) {
    super(message);
  }

  InsufficientFundsException(String message, Throwable cause) {
    super(message, cause);
  }
}

class BankAccount {
  private java.math.BigDecimal balance;

  BankAccount(java.math.BigDecimal openingBalance) {
    this.balance = openingBalance;
  }

  void withdraw(java.math.BigDecimal amount)
      throws InsufficientFundsException {
    if (amount.signum() <= 0) {
      throw new IllegalArgumentException("amount must be positive");
    }
    if (amount.compareTo(balance) > 0) {
      throw new InsufficientFundsException("Insufficient funds");
    }
    balance = balance.subtract(amount);
  }
}
```

Handle the exception at a boundary where a useful recovery or response is possible. Avoid exposing sensitive details in messages. See sections 5.6 and 5.10 for exception translation and additional design guidance.

### Common questions

1. **Can `try` exist without `catch`?** Yes. A `try` can use `finally` without `catch`; try-with-resources can also be used without either.
2. **Can `finally` contain `return`?** Yes, but it overrides a pending return or exception. Avoid returning or throwing from `finally`.
3. **Are `Error`s always unrecoverable and exceptions always recoverable?** No. `Error` usually signals a serious failure that applications should not try to handle routinely; whether an exception can be handled depends on its type and context.
4. **Are checked exceptions unsuitable for services?** Not categorically. Choose checked or unchecked exceptions based on the contract and whether callers can reasonably recover; framework conventions do not establish a universal rule.

## 5.6 Exception Hierarchy and Boundaries

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

## 5.7 Multi-Catch and Suppressed Exceptions

```java
try {
  readConfiguration();
} catch (IOException | ParseException e) {
  throw new ConfigurationException("Invalid configuration", e);
}
```

Multi-catch alternatives cannot be parent and child types. In try-with-resources, resources initialize left-to-right and close right-to-left. If the body and `close()` both fail, close failures are available through `getSuppressed()`.

## 5.8 Exception Design Guidelines

- Catch only where code can recover, add context, or translate abstractions.
- Preserve causes when wrapping.
- Do not catch `Throwable` for ordinary application handling.
- Do not use exceptions for expected control flow.
- Log once at the boundary that handles the error.
- Never expose secrets or full sensitive payloads in exception messages.
- Assertions are disabled by default and must not validate public input or required business rules.

## 5.9 Common Anti-Patterns

- Empty catch blocks hide failures.
- Broad catches may accidentally swallow cancellation or programming defects.
- Logging and rethrowing unchanged exceptions produces duplicate logs.
- Returning `null`, zero, or empty data after unexpected failure creates success-shaped errors.
- Throwing from `finally` can replace the original failure.
- Failing to restore interrupted status can prevent task cancellation.

## 5.10 Designing Custom Exceptions

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

## 5.11 Stack Traces

A stack trace captures the call path when the throwable is created. Creating many exceptions can therefore be expensive.

- The top frame is normally closest to the throw site.
- `Caused by` preserves lower-level failure context.
- `Suppressed` lists secondary failures.
- Async boundaries may split logical operations across different stacks; attach correlation context.
- Do not call `fillInStackTrace` or remove stack information merely to hide performance issues without measurement.

## 5.12 Exception Transparency in Lambdas

Standard functional interfaces do not declare checked exceptions:

```java
// files.stream().map(Files::readString) does not compile because readString throws IOException.
```

Options include handling inside the lambda, extracting a method that translates the exception, using a loop, or defining a domain-specific throwing interface. Avoid generic "sneaky throw" helpers that hide the API contract.

## 5.13 Failure Atomicity

An operation is failure-atomic when a failed attempt leaves the object or system in its previous valid state.

Techniques include:

- Validate before mutation.
- Compute a new immutable value, then replace the old value.
- Use database transactions.
- Write to a temporary file and atomically move it.
- Roll back partial external changes where possible.

Document partial-success behavior when atomicity cannot be guaranteed.

# 6. Collections Framework

The Collections Framework provides interfaces, implementations, and algorithms for groups of objects. Program to the interface when possible, choosing an implementation based on ordering, uniqueness, access patterns, and concurrency needs.

```text
Iterable
└── Collection
    ├── List
    ├── Set
    │   └── SortedSet / NavigableSet
    └── Queue
        └── Deque

Map is a separate hierarchy: it maps keys to values and is not a subtype of Collection.
```

Collections store object references, so primitive values use wrapper types such as `Integer`. Generics provide compile-time type safety and avoid most casts:

```java
List<String> names = new ArrayList<>();
names.add("Ava");
String first = names.get(0);
```

## 6.1 List

`List` is an ordered sequence that permits duplicates and supports positional access. `ArrayList` is the usual general-purpose implementation.

| Operation / property | `ArrayList` | `LinkedList` |
| --- | --- | --- |
| Indexed get/set | O(1) | O(n) |
| Append | Amortized O(1) | O(1) |
| Insert/remove at an index | O(n), due to shifting | O(n) to find position, then O(1) link changes |
| Typical trade-off | Compact storage and fast traversal | Also implements `Deque`; extra node allocation and pointer traversal |

```java
List<String> names = new ArrayList<>();
names.add("Ava");
names.add(0, "Mina");        // insert at index
String first = names.get(0);
names.set(0, "Nina");        // replace at index
names.remove(0);             // remove by index
names.sort(String.CASE_INSENSITIVE_ORDER);
```

`LinkedList` is usually not faster for edits by index because locating the node is O(n). Use a `Deque` interface when double-ended queue operations are the goal. `Vector` is a legacy synchronized list; prefer modern alternatives unless an API specifically requires it.

## 6.2 Set

`Set` holds unique elements; adding an element already present does not change the set. Equality and hash behavior determine uniqueness for hash-based sets, while sorting and uniqueness in a `TreeSet` are determined by its comparator (or natural ordering).

| Implementation | Iteration order | Typical add/contains | Use when |
| --- | --- | --- | --- |
| `HashSet` | Unspecified | Expected O(1) | Uniqueness and fast membership checks |
| `LinkedHashSet` | Insertion order | Expected O(1) | Uniqueness with predictable iteration |
| `TreeSet` | Sorted order | O(log n) | Sorted values and range/nearest-element operations |

```java
Set<String> unique = new HashSet<>();
unique.add("A");
unique.add("A"); // duplicate; size remains 1

Set<Integer> sorted = new TreeSet<>();
sorted.addAll(List.of(10, 2, 1)); // iteration: 1, 2, 10
```

`HashSet` and `LinkedHashSet` allow one `null`; `TreeSet` null behavior depends on its ordering, and natural ordering normally rejects null. A comparator that permits null can define a different policy.

## 6.3 Map

`Map<K,V>` associates each key with at most one value; it is not a `Collection`. Putting a value for an existing key replaces the previous mapping.

| Implementation | Iteration order | Null policy | Typical use |
| --- | --- | --- | --- |
| `HashMap` | Unspecified | One null key and null values allowed | General-purpose lookup |
| `LinkedHashMap` | Insertion order by default; can be configured for access order | One null key and null values allowed | Predictable iteration or access-order maps |
| `TreeMap` | Sorted by key | Null-key support depends on comparator; null values allowed | Sorted keys and range queries |
| `ConcurrentHashMap` | Unspecified | Null keys and values rejected | Concurrent access and atomic map operations |

```java
Map<Integer, String> users = new HashMap<>();
users.put(1, "Ali");
users.put(1, "Khan"); // replaces the value for key 1

String name = users.get(1);
boolean hasKey = users.containsKey(1);
for (Map.Entry<Integer, String> entry : users.entrySet()) {
  System.out.println(entry.getKey() + ": " + entry.getValue());
}
```

Hash-based maps use `hashCode()` and `equals()` to find keys; custom key classes must implement them consistently. Do not mutate fields used by a key's equality or hash calculation while it is stored in a map. `Hashtable` is a legacy synchronized map; prefer `HashMap` for non-concurrent use or a suitable `java.util.concurrent` implementation for concurrency.

## 6.4 Queue, Deque, and Stack

- A FIFO `Queue` typically adds at the tail and removes from the head. `offer`, `poll`, and `peek` return a failure value (`false` or `null`) when the operation cannot be performed; `add`, `remove`, and `element` instead throw an exception in those cases.
  ```java
  Queue<Integer> queue = new ArrayDeque<>();
  queue.offer(10);
  queue.offer(20);
  Integer next = queue.poll(); // 10; null if empty
  Integer head = queue.peek(); // 20; null if empty
  ```
- `PriorityQueue` removes elements according to natural ordering or a comparator, not insertion order. Its iterator does not promise sorted traversal; repeatedly `poll()` to consume elements in priority order.
- `Deque` supports operations at both ends and can be used as either a queue or a stack. `ArrayDeque` is often the preferred general-purpose implementation; it does not permit null elements.
  ```java
  Deque<Integer> deque = new ArrayDeque<>();
  deque.offerFirst(10);
  deque.offerLast(20);
  deque.pollFirst();
  deque.pollLast();

  Deque<Integer> stack = new ArrayDeque<>();
  stack.push(10);
  stack.push(20);
  int top = stack.pop(); // 20
  ```
- Prefer `Deque` over legacy `Stack`; unlike `Stack`, `ArrayDeque` is not synchronized. Coordinate access if a deque is shared concurrently.

## 6.5 Comparable and Comparator

`Comparable<T>` defines a type's natural ordering with `compareTo`; `Comparator<T>` supplies an ordering externally and allows multiple sort orders. A comparison should return a negative value, zero, or a positive value—not necessarily exactly `-1`, `0`, or `1`.

```java
record Employee(int id, String name, int salary)
    implements Comparable<Employee> {
  @Override
  public int compareTo(Employee other) {
    return Integer.compare(id, other.id); // avoids overflow
  }
}

List<Employee> employees = new ArrayList<>();
employees.sort(Comparator.naturalOrder()); // by id
employees.sort(Comparator.comparing(Employee::name));
employees.sort(Comparator.comparingInt(Employee::salary)
    .thenComparing(Employee::name));
```

| `Comparable` | `Comparator` |
| --- | --- |
| Implemented by the element type | Separate strategy, often a lambda |
| `compareTo(other)` | `compare(left, right)` |
| Usually one natural order | Can define many alternative orders |
| In `java.lang` | In `java.util` |

Comparators used by `TreeSet` or `TreeMap` determine which elements/keys count as equivalent: if `compare(a, b) == 0`, the sorted set/map treats them as the same position even when `a.equals(b)` is false. Prefer orderings consistent with `equals()` when the collection's semantics require it.

## 6.6 Iteration and Concurrent Modification

Use an `Iterator` when removing elements during traversal. Its `remove()` method removes the last element returned by `next()`; call it at most once per `next()` and only if supported by the collection.

```java
List<String> names = new ArrayList<>(List.of("Ava", "Mina", "Ali"));
Iterator<String> iterator = names.iterator();
while (iterator.hasNext()) {
  String name = iterator.next();
  if (name.startsWith("A")) {
    iterator.remove(); // supported removal through this iterator
  }
}
```

For a simple predicate-based removal, `removeIf` is usually clearer:
```java
names.removeIf(name -> name.startsWith("A"));
```

`ListIterator` supports traversal in both directions and controlled edits to a list while iterating:
```java
ListIterator<String> cursor = names.listIterator();
while (cursor.hasNext()) {
  String name = cursor.next();
  if (name.equals("Mina")) {
    cursor.set("Mina K."); // replace the last element returned
    cursor.add("Ali");     // insert at the cursor position
  }
}
```

`set` and `add` have iterator-state requirements; for example, `set` requires a preceding `next()` or `previous()` not followed by `remove()` or `add()`. Use the iterator's own mutation methods rather than structurally changing the backing collection through another reference during iteration.

For read-only traversal, use an enhanced `for` loop or `forEach`:
```java
for (String name : names) {
  System.out.println(name);
}
names.forEach(System.out::println);
```

Do not add or remove elements from an `ArrayList` or `HashSet` directly inside these traversals. Structural modification outside the iterator's supported methods may cause a `ConcurrentModificationException`.

- **Structural modification** changes the collection's size or structure, such as adding or removing elements. Replacing an existing list element with `set` is not structural.
- Fail-fast iterators may throw `ConcurrentModificationException` when they detect unsupported structural modification. This is best-effort bug detection, not synchronization, and not a guarantee that every concurrent modification will be detected. Do not catch the exception as a concurrency-control strategy.
- A fail-fast iterator is not safe for concurrent access. For shared mutable collections, use external synchronization or a collection designed for concurrency.
- `Collections.synchronizedList` synchronizes individual methods, but iteration across multiple operations must also synchronize on the wrapper:
  ```java
  List<String> synchronizedNames =
      Collections.synchronizedList(new ArrayList<>());
  synchronized (synchronizedNames) {
    for (String name : synchronizedNames) {
      System.out.println(name);
    }
  }
  ```
- `CopyOnWriteArrayList` iterators traverse a snapshot captured when the iterator was created; later changes are not seen by that iterator. This is useful for small, read-mostly collections but makes writes expensive.
- `ConcurrentHashMap` iterators are weakly consistent: they do not throw `ConcurrentModificationException` and may reflect some updates made during traversal. They are not snapshots.

## 6.7 Choosing a Collection

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

## 6.8 Complexity Guide

- `ArrayList.get`: O(1); middle insertion/removal: O(n).
- `HashMap.get/put`: expected O(1), depending on hashing and resizing.
- `TreeMap.get/put`: O(log n).
- `HashSet.contains`: expected O(1).
- `TreeSet.contains`: O(log n).
- `PriorityQueue.offer/poll`: O(log n); `peek`: O(1).

Big-O does not capture allocation, cache locality, hash quality, concurrency, or small-data constants. Measure critical paths.

## 6.9 Immutable and Unmodifiable Collections

```java
List<String> fixed = List.of("A", "B");
List<String> snapshot = List.copyOf(existing);
List<String> view = Collections.unmodifiableList(existing);
```

- Factory collections reject mutation and generally reject null.
- `copyOf` creates an immutable snapshot unless the source is already suitable.
- `unmodifiableList` is a view; backing-list changes remain visible.
- `Arrays.asList` is fixed-size but permits replacement with `set`.

## 6.10 Map Operations and Contracts

```java
counts.merge(word, 1, Integer::sum);
users.computeIfAbsent(teamId, ignored -> new ArrayList<>()).add(user);
cache.computeIfPresent(key, (key, value) -> refresh(value));
```

- Mapping functions should be short and avoid recursively modifying the same map.
- `HashMap` permits one null key and null values; `ConcurrentHashMap` permits neither.
- Comparator subtraction can overflow; use `Integer.compare`.
- Mutating fields used by hashing or ordering while an element is stored can make it logically unreachable.

## 6.11 HashMap Internal Behavior

A `HashMap` spreads a key's hash to choose a bucket. Within a bucket, it uses equality to find the exact key.

1. Compute `hashCode()` and spread high bits.
2. Select a bucket from the current table size.
3. Compare hash, then `equals()`.
4. Insert, replace, or return the matching entry.

When size exceeds `capacity * loadFactor`, the table resizes. Since Java 8, sufficiently large, heavily collided buckets may become balanced trees when table and bucket thresholds are met. This protects worst-case lookup behavior but does not excuse poor hash functions.

## 6.12 Views and Backing Collections

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

## 6.13 Navigable Collections

`NavigableSet` and `NavigableMap` support nearest-match and range operations:

- `lower`: greatest element strictly less than key.
- `floor`: greatest element less than or equal.
- `ceiling`: least element greater than or equal.
- `higher`: least element strictly greater.
- `pollFirst` / `pollLast`: retrieve and remove an endpoint.

These are useful for scheduling, time ranges, leaderboards, and version lookup.

## 6.14 Spliterator

A `Spliterator` traverses and partitions elements for sequential or parallel processing. Characteristics such as `ORDERED`, `DISTINCT`, `SORTED`, `SIZED`, `IMMUTABLE`, and `CONCURRENT` help stream implementations optimize safely.

Custom spliterators must partition without losing or duplicating elements and report only truthful characteristics.

## 6.15 Concurrent Collection Semantics

- `ConcurrentHashMap` supports concurrent reads and updates without one global map lock.
- Its iterators are weakly consistent: they do not throw `ConcurrentModificationException` and may reflect some concurrent changes.
- `CopyOnWriteArrayList` makes every mutation copy the backing array; excellent for tiny, read-mostly listener lists, poor for write-heavy or large lists.
- `ConcurrentLinkedQueue` is non-blocking and unbounded.
- `BlockingQueue` can enforce producer backpressure when bounded.

Compound actions still need atomic methods such as `compute`, `merge`, `putIfAbsent`, or external coordination.

# 7. Generics

Generics let classes, interfaces, and methods operate on types while preserving compile-time checks. They reduce casts and make APIs reusable without giving up type safety. Prefer parameterized types over raw types; unchecked warnings often indicate that the compiler can no longer verify the type relationship.

Type arguments must be reference types, so use wrapper classes such as `Integer` rather than primitives such as `int`. Java does not have reified generic types: `List<String>` and `List<Integer>` are different compile-time types, but their ordinary runtime class is `List`.

## 7.1 Generic Classes

`T` is a type parameter; `Box<String>` supplies `String` as its type argument. The compiler checks values at the boundary, so callers can use the result without casting:

```java
final class Box<T> {
  private T value;

  Box(T value) {
    this.value = value;
  }

  T getValue() {
    return value;
  }

  void setValue(T value) {
    this.value = value;
  }
}

Box<String> message = new Box<>("Hello"); // diamond operator infers String
String value = message.getValue();         // type checked; no cast
```

A class can declare multiple type parameters, as in `Pair<K, V>`. Common conventions are `T` (type), `E` (element), `K` (key), `V` (value), and `N` (number). Type parameters are scoped to their declaration; a generic method can declare its own parameters independently of the containing class.

Parameterized types are invariant: a `List<Integer>` is not a subtype of `List<Number>`, even though `Integer` is a subtype of `Number`. Wildcards, covered below, express safe covariance or contravariance at an API boundary.

## 7.2 Generic Methods

The method's type parameters appear before its return type. The compiler usually infers them from the arguments; an explicit type witness is available when inference needs help.

```java
final class Util {
  static <T> void printArray(T[] values) {
    for (T value : values) {
      System.out.println(value);
    }
  }

  static <T> Optional<T> first(T[] values) {
    return values.length == 0 ? Optional.empty() : Optional.ofNullable(values[0]);
  }
}

Integer[] integers = { 1, 2, 3 };
String[] strings = { "A", "B" };
Util.<Integer>printArray(integers); // explicit type witness
Util.printArray(strings);           // compiler infers T as String
Optional<String> first = Util.first(strings);
```

## 7.3 Bounded Types and Wildcards

Bounds restrict which types a type parameter can represent. For example, `<T extends Number>` lets a method use members of `Number`; it does not mean the argument is a wildcard:

```java
static <T extends Number> double toDouble(T value) {
  return value.doubleValue();
}
```

An upper-bounded wildcard accepts a producer of some unknown subtype. Values can be read as the bound, but a non-null value generally cannot be added because the exact element type is unknown:

```java
static double sum(List<? extends Number> numbers) {
  double total = 0;
  for (Number number : numbers) total += number.doubleValue();
  return total;
}

List<Integer> integers = List.of(1, 2, 3);
double total = sum(integers); // accepts List<Integer>, List<Double>, or List<Number>
```

An unbounded wildcard means a list of one unknown element type. Reading yields `Object`; only `null` can be added through `List<?>`. Use it when the operation does not depend on the element type:
```java
static void printAll(List<?> values) {
  for (Object value : values) {
    System.out.println(value);
  }
}
```

A lower-bounded wildcard accepts a consumer of a type or one of its supertypes:

```java
static void addDefaults(List<? super Integer> destination) {
  destination.add(10);
  destination.add(20);
}

List<Number> numbers = new ArrayList<>();
addDefaults(numbers); // also accepts List<Integer> and List<Object>
```

**PECS** (“Producer Extends, Consumer Super”) is a useful API-design heuristic: use `extends` when a parameter produces values for you to read, and `super` when it consumes values you provide. A wildcard is not write-only: a `List<? super Integer>` can also be read, but the only statically safe result type is `Object`. Prefer a named type parameter instead of a wildcard when multiple arguments or a return value must share the same type.

## 7.4 Type Erasure

- Generic type arguments are erased from ordinary runtime object types; a type variable erases to its leftmost bound or `Object` if unbounded.
- Class files retain generic-signature metadata for reflection, but ordinary runtime checks cannot distinguish `List<String>` from `List<Integer>`. Reflection can inspect declared generic signatures; that metadata does not verify the actual contents of an object at runtime.
- Because of erasure:
  ```java
  // These have the same erased signature and cannot be overloaded:
  // void process(List<String> values) {}
  // void process(List<Integer> values) {}

  // Cannot create an array of a non-reifiable parameterized type:
  // T[] values = new T[10];

  // Cannot test a specific type argument at runtime:
  // if (value instanceof ArrayList<String>) { }
  if (value instanceof ArrayList<?> list) { // reifiable wildcard form
    System.out.println(list.size());
  }
  ```

Avoid raw types such as `List` except when interoperating with legacy APIs. Raw types bypass generic checks and can cause heap pollution or a `ClassCastException` later.

## 7.5 Generic Interfaces

```java
interface Repository<T, ID> {
  void save(T entity);
  Optional<T> findById(ID id);
}

final class EmployeeRepository implements Repository<Employee, Long> {
  @Override
  public void save(Employee entity) {
    // Persist the entity.
  }

  @Override
  public Optional<Employee> findById(Long id) {
    return Optional.empty(); // Replace with the lookup result.
  }
}
```

The implementation fixes `T` as `Employee` and `ID` as `Long`, so callers cannot accidentally save a different entity type or pass an unrelated identifier type.

## 7.6 Wildcard Capture

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

## 7.7 Generic API Design

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

## 7.8 Heap Pollution and Varargs

Heap pollution occurs when a parameterized variable refers to an incompatible value, often through raw types, unchecked casts, or generic varargs.

```java
@SafeVarargs
static <T> List<T> combine(List<? extends T>... lists) {
  List<T> result = new ArrayList<>();
  for (List<? extends T> list : lists) result.addAll(list);
  return result;
}
```

`@SafeVarargs` suppresses a specific warning; use it only when callers cannot observe heap pollution caused by the method. A method can still be unsafe even if it never assigns directly into the array—for example, exposing the array through an alias can allow incompatible values to be stored.

## 7.9 Reifiable Types

Reifiable types retain enough runtime information for operations such as `instanceof`. Examples include primitives, non-generic classes, raw types, and unbounded wildcard types:

```java
if (value instanceof List<?> list) {
  System.out.println(list.size());
}
```

`List<String>` is non-reifiable because its element type is erased.

## 7.10 Recursive Bounds

Recursive bounds express relationships involving the type itself:

```java
static <T extends Comparable<? super T>> T max(List<? extends T> values) {
  return values.stream().max(Comparator.naturalOrder()).orElseThrow();
}
```

`Comparable<? super T>` permits comparison logic inherited from a supertype and is more flexible than `Comparable<T>`.

## 7.11 Multiple Bounds

A type parameter can require one class and multiple interfaces:

```java
static <T extends Number & Comparable<T> & Serializable>
T choose(T left, T right) {
  return left.compareTo(right) >= 0 ? left : right;
}
```

The class bound, if any, must appear first. Erasure uses the leftmost bound, which can affect generated casts and binary compatibility.

## 7.12 Bridge Methods

Type erasure can change an overriding method's erased signature. The compiler creates a synthetic bridge method to preserve polymorphism:

```java
class StringBox implements Comparable<StringBox> {
  public int compareTo(StringBox other) {
    return 0;
  }
}
```

Reflection and stack traces may expose bridge methods. `Method.isBridge()` identifies them.

## 7.13 Generic Factories

Static factories can infer type arguments more cleanly than constructors:

```java
static <K, V> Map<K, V> newMap() {
  return new HashMap<>();
}

Map<String, Integer> counts = newMap();
```

The diamond operator can infer constructor types from the target context. Anonymous classes have supported the diamond operator since Java 9, with restrictions based on inferred non-denotable types.

## 7.14 Variance Summary

- Java generic types are invariant: `List<Integer>` is not a subtype of `List<Number>`.
- `? extends Number` provides a covariant read view.
- `? super Integer` provides a contravariant write view.
- Arrays are covariant and reified, shifting some errors from compile time to runtime.
- Function inputs often use `? super T`; function outputs often use `? extends R`.

Example from `Stream.map` conceptually: `Function<? super T, ? extends R>`.

# 8. Multithreading and Concurrency

**Concurrency** is structuring work so multiple tasks can make progress during overlapping periods; **parallelism** is executing multiple tasks at the same instant. Java threads share a process's heap, so shared mutable state needs an explicit coordination strategy. Prefer immutable data, ownership confinement, or high-level concurrency utilities before introducing manual locking.

Choose a concurrency tool based on the problem:

| Need | Typical tool |
| --- | --- |
| Run a bounded set of application tasks | `ExecutorService` |
| Return a result from submitted work | `Callable` and `Future` |
| Compose asynchronous stages | `CompletableFuture` |
| Protect a short shared-state invariant | `synchronized`, `Lock`, or an atomic type |
| Transfer work between producers and consumers | `BlockingQueue` |
| Coordinate task completion or resource access | `CountDownLatch`, `Phaser`, or `Semaphore` |

Thread safety is a property of behavior under concurrent use, not a label that makes every operation on an object atomic. A collection may make individual methods safe while a multi-step check-then-act sequence still needs coordination.

## 8.1 Threads, Runnable, and Lifecycle

`Thread` represents a path of execution. Calling `start()` schedules a new thread to execute `run()`; calling `run()` directly is an ordinary method call on the current thread. Prefer separating task from thread management with `Runnable` or `Callable`; most application work should be submitted to an executor rather than creating an unbounded number of threads manually.

```java
Runnable task = () ->
    System.out.println("Running on " + Thread.currentThread().getName());

Thread worker = new Thread(task);
worker.start(); // starts a separate thread
worker.join();  // wait for that thread to finish
```

`Runnable.run()` does not return a result or declare checked exceptions. `Callable<V>.call()` returns a value and may throw checked exceptions; submitting one to an executor gives a `Future<V>`.

`Thread.State` has six values; there is no separate `RUNNING` state in the API. A thread executing or eligible for CPU time is reported as `RUNNABLE`.

1. `NEW`: created but not started.
2. `RUNNABLE`: executing or ready to execute.
3. `BLOCKED`: waiting to acquire an intrinsic monitor.
4. `WAITING`: waiting indefinitely for another action, such as `join()` or `Object.wait()`.
5. `TIMED_WAITING`: waiting with a deadline, such as `sleep()` or timed `join()`.
6. `TERMINATED`: `run()` has completed.

- `Thread.sleep(duration)` pauses the current thread and does not release any monitor it holds. Handle or propagate `InterruptedException`.
- `join()` waits for a thread to terminate; use a timed join if waiting indefinitely is not acceptable.
- `yield()` and thread priority are scheduler hints, not synchronization or correctness mechanisms.
- A daemon thread does not keep the JVM alive. The JVM may exit when only daemon threads remain, so do not rely on daemon threads to finish required work or cleanup.
- Threads generally cannot be restarted after termination. Design cancellation cooperatively, usually with interruption or an explicit cancellation signal.

## 8.2 `synchronized` and `volatile`

A **race condition** occurs when the result depends on unsynchronized timing between operations on shared state. For example, `count++` is a read-modify-write sequence, not one atomic operation; simultaneous increments can be lost.

`synchronized` uses an object's intrinsic monitor to provide mutual exclusion and a visibility guarantee. An instance synchronized method locks `this`; a static synchronized method locks the class's `Class` object. A synchronized block lets code protect a smaller critical section. Prefer a private lock object when callers should not be able to contend on or interfere with the lock.

```java
final class Counter {
  private final Object lock = new Object();
  private int count;

  void increment() {
    synchronized (lock) {
      count++;
    }
  }

  int value() {
    synchronized (lock) {
      return count;
    }
  }
}
```

`volatile` makes reads and writes of a field visible across threads and establishes ordering for those accesses; it does not make compound operations such as `count++` atomic. Use it for independent state such as a stop flag when the protocol is simple enough; use a lock or atomic class for read-modify-write or multi-field invariants.

| `synchronized` | `volatile` |
| --- | --- |
| Mutual exclusion plus visibility/order guarantees | Visibility and ordering for that field; no mutual exclusion |
| Can protect compound actions and invariants | Does not make compound actions atomic |
| Waiting to acquire a monitor may block | Access itself does not acquire a monitor |

## 8.3 ExecutorService, Future, and CompletableFuture

An `ExecutorService` separates task submission from thread creation, reuses worker threads, and provides a lifecycle for shutdown. Choose pool and queue capacity based on workload; unbounded task submission can consume unbounded memory.

```java
static void runTask() throws InterruptedException, ExecutionException {
ExecutorService executor = Executors.newFixedThreadPool(4);
try {
  Future<Integer> result = executor.submit(() -> 10 + 20);
  int value = result.get(); // blocks; may throw InterruptedException or ExecutionException
} finally {
  executor.shutdown(); // stop accepting new work; submitted tasks can finish
  try {
    if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
      executor.shutdownNow(); // requests interruption; tasks must cooperate
    }
  } catch (InterruptedException e) {
    executor.shutdownNow();
    Thread.currentThread().interrupt();
  }
}
}
```

`Future.get()` blocks until completion. `cancel(true)` requests interruption but cannot force a task to stop. If interrupted while waiting, restore the interrupt status when the method cannot propagate the exception.

Use `CompletableFuture` to compose dependent or independent asynchronous stages without blocking between every stage:
```java
CompletableFuture<String> name =
    CompletableFuture.supplyAsync(() -> "Ava", executor);

CompletableFuture<String> greeting = name
    .thenApply(value -> "Hello, " + value)
    .thenApply(String::toUpperCase);
```

- `thenApply` transforms a result; `thenCompose` chains a function that itself returns a future; `thenCombine` combines independent results.
- `allOf` completes when all supplied futures complete but does not collect their values; `anyOf` completes with the first completed future's result.
- `exceptionally` recovers by supplying a replacement result; `handle` processes success or failure; `whenComplete` observes completion without replacing its outcome. Do not turn failures into plausible success values unless that fallback is intentional.
- Async methods without an explicit executor commonly use the common ForkJoinPool. Non-async continuations may run on the thread that completes the prior stage. Supply an appropriate executor when execution placement or blocking behavior matters.

## 8.4 Concurrent Utilities and Locks

- `java.util.concurrent` provides concurrent collections and coordination utilities. Pick one whose semantics match the task: for example, `ConcurrentHashMap` for concurrent key/value operations, `CopyOnWriteArrayList` for small read-mostly lists, and `BlockingQueue` for producer-consumer handoff.
  ```java
  BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);
  queue.put(10); // waits if full
  Integer item = queue.take(); // waits if empty
  ```
- `Lock` offers capabilities beyond intrinsic monitors, such as timed or interruptible acquisition and multiple `Condition`s. Always release a successfully acquired lock in `finally`:
  ```java
  Lock lock = new ReentrantLock();
  lock.lock();
  try {
    count++;
  } finally {
    lock.unlock();
  }
  ```
- `tryLock(timeout, unit)` can bound a wait but does not alone prevent deadlock or guarantee fairness. A fair `ReentrantLock` can reduce starvation in some workloads but may reduce throughput.
- `ReadWriteLock` permits multiple readers or one writer; its extra coordination is useful only when the workload benefits. `AtomicInteger` provides atomic operations such as `incrementAndGet`, but does not make a larger multi-variable invariant atomic. Prefer the simplest correct mechanism and measure before optimizing.

## 8.5 Race Conditions, Deadlock, and Liveness

- A **race condition** is a correctness bug caused by unsynchronized access or ordering of concurrent operations. A **data race** is the narrower case of conflicting accesses to the same variable without a happens-before relationship, with at least one write.
- **Deadlock** occurs when tasks wait forever for resources held by one another. The classic Coffman conditions are mutual exclusion, hold-and-wait, no preemption, and circular wait. Prevent common lock deadlocks by acquiring locks in a consistent global order, reducing nested locking, or redesigning shared ownership.
- Liveness failures also include **starvation** (a task cannot obtain needed progress/resources) and **livelock** (tasks remain active but make no useful progress). Timeouts may improve responsiveness but do not guarantee correctness or eliminate all deadlocks.
- Use immutable state, thread confinement, synchronized/locked access, atomic operations, or concurrent collections according to the invariant being protected. `ThreadLocal` isolates a value per thread, but pooled threads are reused; remove request-specific values in `finally` to avoid retaining or leaking data across tasks.
- Capture a thread dump (for example with `jcmd` or `jstack`) to diagnose blocked threads and deadlocks. Logging, timeouts, and monitoring help identify stalls but are not substitutes for correct synchronization.

### Common questions

1. **`start()` vs `run()`?** `start()` arranges for a separate thread to execute `run()`; a direct `run()` call executes on the calling thread.
2. **Why are `wait()` and `notify()` methods on `Object`?** Intrinsic monitor coordination is associated with an object's monitor; the calling thread must own that same monitor.
3. **`wait()` vs `sleep()`?** `wait()` releases the monitor on which it is called while waiting and must be called while owning that monitor. `sleep()` pauses the current thread without releasing monitors it holds.
4. **Does `CompletableFuture` always use the ForkJoin common pool?** No. Async stages without an explicit executor commonly use it; stages can use a supplied executor, while non-async stages may run on the thread completing the prior stage.

## 8.6 Java Memory Model

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

## 8.7 Safe Publication

An object is safely published when other threads cannot observe a partially constructed state. Common mechanisms:

- Store it in a properly locked field.
- Store it in a volatile field.
- Publish through a thread-safe collection.
- Initialize it in a static initializer.
- Share it before starting a new thread.

Do not allow `this` to escape from a constructor by registering listeners, starting threads, or calling external code.

## 8.8 Executor Sizing and Backpressure

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

## 8.9 Cancellation and Timeouts

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

## 8.10 Concurrent Utilities

- `CountDownLatch`: wait until a fixed number of events complete; one-shot.
- `CyclicBarrier`: repeatedly wait until a group reaches a point.
- `Semaphore`: limit concurrent access to a scarce resource.
- `Phaser`: flexible multi-phase coordination.
- `BlockingQueue`: producer-consumer handoff with optional capacity.
- `StampedLock`: optimistic reads for specialized workloads; not reentrant.
- `LongAdder`: scalable counters under heavy contention; `sum()` is not an atomic snapshot.

Prefer high-level utilities over manual `wait()`/`notify()`. If using conditions, always wait in a loop because wakeups may be spurious.

## 8.11 Intrinsic Locks and Reentrancy

Every object has an intrinsic monitor. A synchronized instance method locks `this`; a synchronized static method locks the `Class` object.

Locks are reentrant: a thread holding a monitor can acquire it again. Reentrancy supports synchronized methods calling one another but does not make a class automatically thread-safe.

Keep critical sections small, avoid calling unknown external code while locked, and never lock publicly accessible objects such as string literals.

## 8.12 Lock Ordering

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

## 8.13 ThreadLocal

`ThreadLocal` gives each thread a separate value:

```java
private static final ThreadLocal<DateTimeFormatter> FORMATTER =
    ThreadLocal.withInitial(() -> DateTimeFormatter.ISO_DATE_TIME);
```

Modern `DateTimeFormatter` is already thread-safe, so this example does not need `ThreadLocal`; it illustrates syntax only.

In thread pools, always call `remove()` in a `finally` block for request-scoped values. Otherwise values can leak across requests and retain objects as long as the worker thread lives.

## 8.14 CompletableFuture Error Flow

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

## 8.15 Atomic Classes

Atomic variables support lock-free compare-and-set loops:

```java
AtomicReference<State> state = new AtomicReference<>(initial);
state.updateAndGet(current -> current.next());
```

The update function may run more than once due to retries, so it must be side-effect free. Multiple independent atomic fields do not make a multi-field invariant atomic; use one immutable state object or a lock.

## 8.16 False Sharing and Contention

Independent frequently written fields can occupy the same cache line, causing cores to invalidate each other's cache entries. This is false sharing.

Do not attempt manual padding without profiling and JVM-specific evidence. Often the better fix is reducing shared mutation, partitioning state, batching updates, or using contention-friendly utilities.

## 8.17 `wait()`, `notify()`, and `notifyAll()`

These methods coordinate threads through an object's intrinsic monitor. A thread must own that object's monitor to call `wait`, `notify`, or `notifyAll`; `wait()` releases the monitor while waiting and reacquires it before returning. Prefer `BlockingQueue`, latches, or other higher-level utilities for most application coordination.

```java
final class Gate {
  private final Object monitor = new Object();
  private boolean open;

  void awaitOpen() throws InterruptedException {
    synchronized (monitor) {
      while (!open) {
        monitor.wait();
      }
    }
  }

  void open() {
    synchronized (monitor) {
      open = true;
      monitor.notifyAll();
    }
  }
}
```

- Always test the condition in a `while` loop, not an `if`: wakeups can be spurious, and another thread may consume or change the condition before the awakened thread reacquires the monitor.
- Update and inspect the condition while holding the same monitor. Calling `notifyAll()` wakes waiters but does not release the monitor immediately; they can proceed only after the notifying thread exits the synchronized region.
- `notify()` wakes one arbitrary waiter; `notifyAll()` wakes all waiters. When waiters may be waiting for different conditions, `notifyAll()` is generally safer, though it can cause extra wakeups.
- `wait()` may throw `InterruptedException`. Propagate it or perform appropriate cleanup and restore the thread's interrupt status.

For virtual threads, their use cases and constraints, see section 13.6.

# 9. Java 8+ Features

## 9.1 Functional Interfaces, Lambdas, and Method References

A **functional interface** has exactly one abstract method (SAM, or single abstract method), ignoring public methods corresponding to `Object`. It may still have default and static methods. `@FunctionalInterface` is optional, but makes the compiler verify the interface's intended contract.

```java
@FunctionalInterface
interface IntCombiner {
  int combine(int left, int right);

  default int combineWithDefault(int value) {
    return combine(value, 0);
  }
}
```

The lambda's **target type** is the functional-interface type expected by the assignment, argument, or return context. That type determines the parameter and result types; a lambda does not have a useful standalone type without such a target.

```java
IntCombiner add = (left, right) -> left + right;
IntCombiner multiply = (left, right) -> {
  int product = left * right;
  return product;
};

int result = add.combine(2, 3); // 5
```

For a single parameter, parentheses and the parameter type can usually be omitted when inferred. A block-bodied lambda that returns a value must use `return`; an expression-bodied lambda returns its expression. A `void`-compatible expression lambda may be a statement expression, such as a method call.

Common `java.util.function` interfaces:

| Interface | Abstract method | Typical use |
| --- | --- | --- |
| `Predicate<T>` | `boolean test(T)` | Test or filter a value |
| `Function<T, R>` | `R apply(T)` | Transform a value |
| `Consumer<T>` | `void accept(T)` | Perform an action with a value |
| `Supplier<T>` | `T get()` | Produce a value without input |
| `BiPredicate<T, U>` | `boolean test(T, U)` | Test two values |
| `BiFunction<T, U, R>` | `R apply(T, U)` | Transform two values |
| `UnaryOperator<T>` | `T apply(T)` | Transform a value to the same type |
| `BinaryOperator<T>` | `T apply(T, T)` | Combine two values of the same type |

Primitive-specialized forms such as `IntPredicate`, `IntFunction<R>`, `ToIntFunction<T>`, and `IntUnaryOperator` can avoid boxing and unboxing.

```java
Predicate<Integer> isEven = number -> number % 2 == 0;
Function<String, Integer> length = text -> text.length();
Consumer<String> print = System.out::println;
Supplier<List<String>> newList = ArrayList::new;
IntUnaryOperator doubled = number -> number * 2;

boolean even = isEven.test(10);
int size = length.apply("Java");
```

Lambdas may capture local variables only when they are final or effectively final. A lambda's `this` refers to the enclosing instance; unlike an anonymous inner class, the lambda does not introduce a new `this`. Avoid using side effects on shared mutable state, especially in deferred or parallel operations.

Standard functional interfaces generally do not declare checked exceptions. A method reference to a checked-exception-throwing method therefore may not fit directly; catch and translate the exception at an appropriate boundary, or define a domain-specific functional interface that declares it. Avoid hiding failures with generic unchecked-wrapper tricks.

Overloaded methods can make lambda target typing ambiguous. If the compiler cannot determine which functional-interface overload is intended, use an explicitly typed lambda or a local variable with the desired functional-interface type.

**Method references** are concise forms for invoking an existing method or constructor when its signature is compatible with the target functional interface. The four common forms are:

```java
// 1. Static method reference
Function<String, Integer> parse = Integer::parseInt;

// 2. Bound instance method: receiver is a particular object
Consumer<String> output = System.out::println;

// 3. Unbound instance method: the first argument supplies the receiver
Function<String, String> trimmed = String::trim;

// 4. Constructor reference
Supplier<List<String>> listFactory = ArrayList::new;
```

Method references must still match the target's argument and return types; overload resolution selects a compatible overload from that target context. Prefer a lambda when it makes adaptation, validation, or control flow clearer.

## 9.2 Stream API

A stream is a one-use pipeline for processing elements from a source; it does not store elements. A typical pipeline consists of a source, zero or more intermediate operations, and one terminal operation:

```text
source -> intermediate operations (lazy) -> terminal operation (traverses the pipeline)
```

Use a stream when it makes a transformation or aggregation clearer than a loop. A stream is not automatically faster or more memory-efficient than an imperative loop.

```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);

// Imperative: explicitly describe how to build the result.
List<Integer> evenDoubled = new ArrayList<>();
for (int number : numbers) {
  if (number % 2 == 0) {
    evenDoubled.add(number * 2);
  }
}

// Stream: compose filtering, transformation, and collection.
List<Integer> evenDoubledWithStream = numbers.stream()
    .filter(number -> number % 2 == 0)
    .map(number -> number * 2)
    .toList(); // unmodifiable result, Java 16+
```

### Sources and intermediate operations

Common sources include `Collection.stream()`, `Collection.parallelStream()`, `Arrays.stream(array)`, and `Stream.of(...)`. A stream should generally not be reused after a terminal operation; create a new stream from the source for another traversal.

Intermediate operations return another stream and are lazy: they run only when a terminal operation needs elements. Common operations include:

| Operation | Purpose |
| --- | --- |
| `filter(predicate)` | Keep elements matching a condition |
| `map(function)` | Transform each element one-to-one |
| `flatMap(function)` | Map each element to a stream, then flatten the streams |
| `distinct()` | Remove duplicate elements using equality |
| `sorted()` / `sorted(comparator)` | Sort elements |
| `limit(n)` / `skip(n)` | Bound or skip elements |
| `takeWhile` / `dropWhile` | Process a prefix based on a predicate (Java 9+) |

```java
// Flatten a list of lists, e.g. [["A", "B"], ["C"]] -> ["A", "B", "C"]
List<String> allSkills = employees.stream()
    .flatMap(employee -> employee.skills().stream())
    .toList();
```

`peek()` is mainly useful for diagnostics; do not rely on it for required side effects because pipeline optimizations or short-circuiting may mean an element is not visited.

### Terminal operations

A terminal operation traverses the pipeline and produces a result, a side effect, or a short-circuiting answer:

- `toList()`, `toArray()`, and `collect(...)` create a result.
- `count()`, `min(...)`, `max(...)`, and `reduce(...)` aggregate values.
- `anyMatch(...)`, `allMatch(...)`, `noneMatch(...)`, `findFirst()`, and `findAny()` may short-circuit.
- `forEach(...)` and `forEachOrdered(...)` perform an action; for ordered parallel streams, `forEachOrdered` preserves encounter order.

An empty stream has no minimum, maximum, or first element, so these operations return `Optional` or `OptionalInt`/`OptionalLong`/`OptionalDouble`.

### Common pipelines

```java
// Filter and map
List<String> highEarners = employees.stream()
    .filter(employee -> employee.salary() > 50_000)
    .map(employee -> employee.name().toUpperCase(Locale.ROOT))
    .toList();

// Numeric aggregation: use primitive streams to avoid wrapper boxing.
int sum = numbers.stream().mapToInt(Integer::intValue).sum();
OptionalInt largest = numbers.stream().mapToInt(Integer::intValue).max();

// Group elements by a key.
Map<String, List<Employee>> byDepartment = employees.stream()
    .collect(Collectors.groupingBy(Employee::department));

// Partition into exactly two groups according to a predicate.
Map<Boolean, List<Employee>> partition =
    employees.stream().collect(
        Collectors.partitioningBy(employee -> employee.salary() > 50_000));

String names = employees.stream()
    .map(Employee::name)
    .collect(Collectors.joining(", "));

// Top three salaries; sort before limiting when the source is not already ordered.
List<Employee> topThree = employees.stream()
    .sorted(Comparator.comparingInt(Employee::salary).reversed())
    .limit(3)
    .toList();
```

`reduce` combines elements into a single value and should use an associative accumulator, especially for parallel streams. Prefer specialized operations such as `sum()` for numeric streams and `collect` for mutable reductions:

```java
int total = numbers.stream().reduce(0, Integer::sum);
Optional<Integer> maximum =
    numbers.stream().reduce(Integer::max); // empty if there are no elements
```

When using `Collectors.toMap`, provide a merge function if duplicate keys are possible; otherwise a duplicate key causes `IllegalStateException`:
```java
Map<String, Integer> counts = words.stream()
    .collect(Collectors.toMap(
        Function.identity(),
        word -> 1,
        Integer::sum));
```

- `Stream.toList()` (Java 16+) returns an unmodifiable list. `Collectors.toList()` does not promise a particular implementation or mutability; choose `Collectors.toCollection(ArrayList::new)` when a mutable list is required.
- Avoid modifying the stream's source while it is being traversed or mutating shared state from intermediate operations. Prefer stateless transformations and collectors.
- Parallel streams can use shared execution resources and may change ordering and execution context. Consider them only after measuring a sufficiently large, CPU-bound, independent workload; avoid blocking I/O and shared side effects in parallel pipelines. See section 9.10 for additional cautions.

## 9.3 Optional

`Optional<T>` represents a result that may be present or absent. It makes absence explicit in an API instead of returning `null`; it does not make the contained value non-null-safe in every other context or replace validation of required inputs.

```java
String supplied = readOptionalText();
Optional<String> maybeText = Optional.ofNullable(supplied); // null becomes empty
Optional<String> knownText = Optional.of("Java");           // rejects null
Optional<String> empty = Optional.empty();
```

Use `ofNullable` when adapting a value that may be null. Use `of` when null is invalid and should fail immediately. Avoid constructing `Optional` just to pass it through; use it at API boundaries where absence is a meaningful result.

```java
Optional<String> normalized = maybeText
    .filter(text -> !text.isBlank())
    .map(String::strip)
    .map(String::toUpperCase);
```

- `filter(predicate)` keeps the value only when it matches.
- `map(function)` transforms a present value; if the mapping function returns `null`, the result is empty.
- `flatMap(function)` is for a mapping that already returns an `Optional`, avoiding nested `Optional<Optional<T>>`. The function should itself return a non-null `Optional`.
- `ifPresent(action)` runs only when present. `ifPresentOrElse(valueAction, emptyAction)` is available since Java 9.
- `isPresent()` and `isEmpty()` (Java 11+) inspect presence, but prefer transformations or fallback operations when they express the logic directly.

Choose a fallback according to its evaluation behavior:
```java
String display = maybeText.orElse("Unknown"); // fallback expression is evaluated eagerly
String displayFromCall = maybeText.orElseGet(() -> loadDefault()); // supplier runs only if empty
String required = maybeText.orElseThrow(); // throws NoSuchElementException if empty
String user = findUser(id)
    .orElseThrow(() -> new UserNotFoundException(id));
```

`get()` also throws `NoSuchElementException` when empty, and usually obscures the intended absence behavior; use it only when presence has already been established and that invariant is clear. `orElseThrow()` without arguments is available since Java 10.

For primitive results, `OptionalInt`, `OptionalLong`, and `OptionalDouble` avoid boxing. Optional values are generally intended as method return types, not fields, parameters, or collection elements; use an empty collection to represent no elements.

```java
Optional<Employee> findById(int id) {
  return Optional.ofNullable(database.get(id));
}

Optional<String> city = findById(id)
    .flatMap(Employee::address)
    .map(Address::city);
```

`Optional.stream()` (Java 9+) converts a present value to a single-element stream and an empty optional to an empty stream, which is useful when flattening collections of optionals.

## 9.4 Default and Static Interface Methods

An interface can provide behavior as well as declare abstract methods. A **default method** is inherited by implementing classes and can be overridden; a **static interface method** belongs to the interface and is called through its name, not inherited as an instance method by implementing classes.

```java
interface Payment {
  void pay();

  default void logPayment() {
    System.out.println("Payment processed");
    audit();
  }

  static String category() {
    return "payment";
  }

  private void audit() { // Java 9+: helper for interface methods
    System.out.println("Audit event");
  }
}

class CardPayment implements Payment {
  @Override
  public void pay() {
    System.out.println("Pay by card");
  }

  @Override
  public void logPayment() {
    System.out.println("Card payment processed");
  }
}

Payment payment = new CardPayment();
payment.logPayment();            // invokes the class's override
String category = Payment.category(); // static method called on the interface
```

- Default methods were added in Java 8 to evolve interfaces while preserving compatibility for existing implementations. They are instance methods and can call other interface methods.
- Static interface methods are not inherited by implementing classes and cannot be overridden as instance methods. Call them as `Payment.category()`.
- Private interface methods (Java 9+) can share implementation among default or static methods; they are not part of the implementing class's API.
- If a class inherits competing defaults with the same signature from unrelated interfaces, it must resolve the conflict by overriding the method. It can explicitly select one implementation with `InterfaceName.super.method()`:
  ```java
  interface Auditable {
    default void log() { System.out.println("Audit"); }
  }

  interface Traceable {
    default void log() { System.out.println("Trace"); }
  }

  class Service implements Auditable, Traceable {
    @Override
    public void log() {
      Auditable.super.log();
      Traceable.super.log();
    }
  }
  ```
- A class method takes precedence over an interface default with the same signature. When extending interfaces, a more-specific subinterface's default takes precedence over a less-specific parent interface's default.

## 9.5 Date and Time API

The `java.time` API (Java 8+) provides immutable, thread-safe date/time types. Select a type that represents the domain value rather than attaching a time zone where none is intended:

| Type | Represents | Typical use |
| --- | --- | --- |
| `LocalDate` | Calendar date without time or zone | Birthday, due date |
| `LocalTime` | Wall-clock time without date or zone | Store opening time |
| `LocalDateTime` | Local date and time without offset or zone | Local appointment before assigning a region |
| `Instant` | Point on the UTC timeline | Event timestamp, machine time |
| `OffsetDateTime` | Date/time with a fixed UTC offset | Interchange where the supplied offset matters |
| `ZonedDateTime` | Date/time with a region zone and its rules | Scheduling or displaying in a named region |

```java
LocalDate date = LocalDate.of(2026, 10, 11);
LocalTime time = LocalTime.of(9, 30);
LocalDateTime localAppointment = LocalDateTime.of(date, time);

Instant eventTime = Instant.now();
ZoneId zone = ZoneId.of("Asia/Kolkata");
ZonedDateTime regionalTime = eventTime.atZone(zone);
```

`LocalDateTime` has no offset or time zone and therefore does not identify a unique instant. Do not use it alone to represent an event that must be ordered globally. A `ZoneId` such as `"Asia/Kolkata"` carries regional time-zone rules; `ZoneOffset` such as `UTC` or `+05:30` is a fixed offset and does not encode future or historical daylight-saving rules.

Convert a known instant for display with `atZone` or `withZoneSameInstant`. `withZoneSameLocal` keeps the wall-clock fields and changes the represented instant, so use it only when preserving local clock time is intended:
```java
ZonedDateTime inParis = eventTime.atZone(ZoneId.of("Europe/Paris"));
ZonedDateTime inTokyo = inParis.withZoneSameInstant(ZoneId.of("Asia/Tokyo"));
```

When mapping a local date-time into a region, daylight-saving transitions can make the local time invalid (a gap) or ambiguous (an overlap). Use explicit zone rules and a documented policy where those cases matter; don't assume every local date-time maps to exactly one instant.

Date/time objects are immutable; arithmetic returns a new value:
```java
LocalDateTime later = localAppointment.plusDays(5).minusMonths(1);
```

Use `Period` for calendar-based date differences (years, months, days), and `Duration` for elapsed time (seconds/nanoseconds). Their meanings differ around variable-length months and daylight-saving changes:
```java
Period calendarAge = Period.between(
    LocalDate.of(2000, 1, 15),
    LocalDate.of(2026, 10, 11));

Duration elapsed = Duration.between(
    Instant.parse("2026-01-01T00:00:00Z"),
    Instant.parse("2026-01-01T01:30:00Z"));
```

Use `DateTimeFormatter` for parsing and formatting. Prefer ISO formatters for interoperable data; use an explicit locale and zone when formatting human-facing values. For strict date parsing, `uuuu` represents the proleptic year and is generally preferable to `yyyy` (year-of-era):
```java
DateTimeFormatter format =
    DateTimeFormatter.ofPattern("uuuu-MM-dd", Locale.ROOT);
LocalDate parsed = LocalDate.parse("2026-10-11", format);
String display = regionalTime.format(
    DateTimeFormatter.ofPattern("dd MMM uuuu HH:mm z", Locale.UK));
```

Inject a `Clock` rather than calling `now()` throughout business logic; this makes tests deterministic and allows the application's time source and zone to be controlled:
```java
Clock fixedClock = Clock.fixed(
    Instant.parse("2026-01-01T00:00:00Z"), ZoneOffset.UTC);
LocalDate testDate = LocalDate.now(fixedClock);
```

The legacy `Date` and `Calendar` APIs have different mutability and time-zone semantics. Use them only when integrating with APIs that require them, converting at the boundary with `Date.from(instant)` or `date.toInstant()`.

## 9.6 Records, Sealed Classes, and Pattern Matching

- **Records (final in Java 16):** concise, transparent data-carrier classes. The compiler derives a canonical constructor, component accessors, `equals`, `hashCode`, and `toString`.
  ```java
  record Employee(int id, String name) {
    Employee {
      if (id <= 0) {
        throw new IllegalArgumentException("id must be positive");
      }
      name = Objects.requireNonNull(name).strip();
    }

    String displayName() {
      return id + ": " + name;
    }
  }

  Employee employee = new Employee(1, "Ava");
  String name = employee.name(); // component accessor, not getName()
  ```
  A compact constructor can validate or normalize its parameters; Java assigns the final component fields after the constructor body. Components are final references, so a record is only shallowly immutable. Defensively copy mutable inputs and outputs when required. Records can implement interfaces and declare methods or static members, but are implicitly final, cannot extend another class, and cannot declare additional instance fields.

- **Sealed classes and interfaces (final in Java 17):** restrict which direct types may extend or implement a hierarchy. Each permitted direct subtype must be `final`, `sealed`, or `non-sealed`.
  ```java
  sealed interface PaymentResult permits Paid, Declined {}
  record Paid(String receiptId) implements PaymentResult {}
  record Declined(String reason) implements PaymentResult {}
  ```
  Permitted subtypes must be in the same named module, or in the same package when the types are in the unnamed module. Use `final` to close a branch, `sealed` to constrain its children further, or `non-sealed` to allow unrestricted extension.

- **Pattern matching for `instanceof` (final in Java 16):** combines a type check and a cast, with the pattern variable available where the match is known to be true.
  ```java
  if (value instanceof String text && !text.isBlank()) {
    System.out.println(text.strip());
  }
  ```
  The pattern variable is flow-scoped; it is not available where the type check might have failed. This avoids an unsafe cast while keeping the checked value's type explicit.

- **Pattern matching for `switch` (final in Java 21):** selects behavior based on the selector's runtime type and can bind a pattern variable in each case. `when` adds a guard; order guarded or more-specific cases before broader patterns that would dominate them.
  ```java
  static String describe(Object value) {
    return switch (value) {
      case null -> "null";
      case Integer number when number > 0 -> "positive integer";
      case Integer number -> "integer: " + number;
      case String text -> "text: " + text;
      default -> "other";
    };
  }
  ```
  A switch over a sealed hierarchy can be exhaustive without a `default`; adding a new permitted subtype then prompts callers to handle it. Handle `null` explicitly when valid; without a `case null`, a pattern switch on a null selector throws `NullPointerException`.

## 9.7 Stream Semantics and Laziness

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

## 9.8 Primitive Streams

Use `IntStream`, `LongStream`, or `DoubleStream` to avoid boxing overhead and access numeric operations:

```java
IntSummaryStatistics stats = employees.stream()
    .mapToInt(Employee::age)
    .summaryStatistics();

double average = stats.getAverage();
int maximum = stats.getMax();
```

Use `mapToObj` to return to object streams and `boxed()` when a collection of wrappers is required.

## 9.9 Collector Details

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

## 9.10 Parallel Stream Cautions

- Parallel streams normally share the common `ForkJoinPool`.
- Blocking operations can starve unrelated work using the same pool.
- Parallelism adds splitting, coordination, and merging overhead.
- Ordered pipelines and stateful operations may reduce benefits.
- Results must be independent of scheduling; do not mutate shared non-thread-safe state.
- Benchmark with realistic data before using `parallelStream`.

## 9.11 Optional Design

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

## 9.12 Time-Zone and Clock Guidance

- Use `Instant` for a point on the UTC timeline and convert it to a region with `atZone` for display.
- `LocalDateTime` has no zone or offset. Mapping it to a regional zone can encounter daylight-saving gaps or overlaps; define how to resolve those cases.
- `ZonedDateTime` combines a local date/time with a region's evolving time-zone rules. `OffsetDateTime` carries a fixed offset but not the region's complete rules.
- Persist machine timestamps as `Instant` or an offset-aware database value. Store a region `ZoneId` as well when future appointments must retain local civil-time intent.
- Inject `Clock` for deterministic tests and explicit control of the time source.

## 9.13 Functional Composition

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

## 9.14 Stream Reduction Laws

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

## 9.15 Date/Time Edge Cases

Local times can be invalid or ambiguous during daylight-saving transitions:

- A gap skips local times when clocks move forward.
- An overlap repeats local times when clocks move backward.

Construct with a `ZoneId` and decide how ambiguity should be resolved. Time-zone database rules change, so retain the original zone when future local scheduling matters.

## 9.16 Resource Streams

Some streams wrap resources and must be closed:

```java
try (Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8)) {
  long errors = lines.filter(line -> line.startsWith("ERROR")).count();
}
```

Collection streams do not normally need closing. A terminal operation does not automatically close an I/O-backed stream.

## 9.17 Collector Composition

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

# 10. JVM Internals

## 10.1 JVM Architecture

The Java Virtual Machine (JVM) is the runtime that loads and executes class files. `javac` compiles source code to platform-independent bytecode; each operating system and processor needs a compatible JVM implementation to execute it.

```text
Source files (.java)
        | javac
        v
Class files (.class) and resources
        | class loading, verification, linking, initialization
        v
JVM execution: interpreter + JIT-compiled native code
        | JVM and native libraries
        v
Operating system and hardware
```

The JVM specification defines the class-file format and runtime behavior. Details such as object layout, garbage collector algorithms, JIT tiers, and exact memory organization vary by JVM vendor, version, architecture, and configuration.

### JVM Components

1. **Class-loader subsystem:** loads class definitions and performs verification, linking, and initialization.
2. **Runtime data areas:** include per-thread program counters and stacks, a shared heap, and the specification's method area; implementations also use native memory.
3. **Execution engine:** interprets bytecode, compiles frequently executed code with a JIT compiler, and coordinates garbage collection.
4. **Native interface and libraries:** let Java code interoperate with native code and platform services.

At runtime, class loading and execution interact: loading can happen lazily as types are first needed, and the execution engine may optimize hot code while the program continues running.

## 10.2 Class Loading

- The standard delegation chain is bootstrap, platform, then application class loader; custom loaders can define additional delegation policies.
- The bootstrap loader is implemented by the JVM, so it is not necessarily a C++ object visible to Java code. Java 9 replaced the extension loader with the platform loader.
- Loading a class is distinct from initializing it. The JVM may load and link a type before its initialization is triggered.

The lifecycle is commonly described in three stages:

1. Loading creates the runtime representation of a class.
2. Linking verifies class-file structure and bytecode, prepares static fields with default values, and resolves symbolic references as needed.
3. Initialization executes class initialization code and explicit static field initializers and static blocks in textual order.

```java
class Example {
  static int value = initialize();

  static {
    System.out.println("Class initialized");
  }

  private static int initialize() {
    return 10;
  }
}

Class.forName("Example"); // initializes by default
```

- Active use, such as creating an instance, invoking a static method, or reading a non-constant static field, can trigger initialization. Compile-time constant fields may be inlined and do not necessarily trigger initialization when read.
- Initialization of a class is synchronized by the JVM and occurs once per defining class loader. If initialization fails, the class can become erroneous for that loader; later uses may fail with `NoClassDefFoundError`.
- `Class.forName(name)` initializes by default; its overload with `initialize = false` can load the class without requesting initialization.
- Parent delegation helps preserve platform type identity, but custom class loaders can define classes with the same binary name. A class's identity includes its defining loader.

## 10.3 Runtime Memory Areas

- **Per-thread areas:** each thread has its own program counter and JVM stack of frames for active method calls. Native method execution may also use a native-method stack.
- **Heap:** shared by threads; objects and arrays are allocated here conceptually. Garbage collectors and JIT optimizations determine the physical allocation and reclamation details.
- **Method area:** a JVM-specification runtime area for per-class structures such as runtime constant pools, field and method data, and method code. It is a conceptual area, not a required physical memory layout.
- **Metaspace:** HotSpot's native-memory implementation for class metadata since Java 8, replacing PermGen. Metaspace is not the same thing as the specification's method area, even though it stores much of the corresponding class metadata.
- The string intern pool and ordinary Java objects are on the heap in modern HotSpot. Static fields are associated with classes; an object referenced from a static field remains an object.
- Heap, thread stacks, Metaspace, direct buffers, and JIT code cache have different purposes and limits. A process can run out of native memory even when its Java heap is not full.

## 10.4 Garbage Collection

- Garbage collection automatically reclaims memory occupied by objects that are no longer reachable; it is not a general-purpose cleanup mechanism for files, sockets, or other external resources. `System.gc()` is only a request/hint and may be ignored.
- An object is reachable if it can be followed from a GC root, such as a live thread's references, static references, or JNI references. Cycles do not prevent collection if the entire cycle is unreachable.
  ```java
  Object value = new Object();
  value = null; // the object may now be eligible for collection
  ```
- Eligibility does not mean collection happens immediately or at all before process exit. Avoid relying on finalizers or collection timing for program behavior.
- Collectors use implementation-specific algorithms to reclaim unreachable objects. Some phases pause application threads; others perform work concurrently. A pause's duration and frequency depend on the collector, heap, allocation rate, object graph, and workload.
- Generational collection is common but not universal. Promotion rules, region layouts, and collection triggers vary by collector and JDK; tuning numbers are not language guarantees.
- A full-GC event is collector-specific and does not mean every memory area is reclaimed or that every application thread pauses for the same duration.

- GC Types - choose based on measured latency, throughput, and memory constraints:
| GC | How | For |
| --- | --- | --- |
| Serial GC | Stop-the-world collection on one GC thread | Small heaps or constrained environments |
| Parallel GC | Parallel collection focused on throughput | Throughput-oriented workloads |
| G1 GC | Region-based collector with configurable pause-time goals | General-purpose server workloads; default in many current HotSpot configurations |
| ZGC, Shenandoah | Concurrent low-latency collectors | Workloads prioritizing pause times; verify support and configuration for the target JDK |

- Tuning flags:
-Xms512m -Xmx1024m // min and max heap
-XX:+UseG1GC // use G1
-XX:MaxGCPauseMillis=200 // a tuning goal, not a pause-time guarantee

Use GC logs, JFR, and workload measurements before tuning. Heap sizing must leave room for Metaspace, thread stacks, direct buffers, and other native memory.

## 10.5 `equals()` and `hashCode()` Contract

`equals()` defines logical equality; `hashCode()` supports hash-based collections such as `HashMap` and `HashSet`. Their contract must agree for these collections to find, replace, and deduplicate equal objects correctly.

### `equals()` contract

For non-null references `x`, `y`, and `z`, an equality implementation must be:

1. **Reflexive:** `x.equals(x)` is true.
2. **Symmetric:** if `x.equals(y)` is true, then `y.equals(x)` is true.
3. **Transitive:** if `x.equals(y)` and `y.equals(z)`, then `x.equals(z)`.
4. **Consistent:** repeated calls return the same result while the equality-relevant state is unchanged.
5. **Null-safe:** `x.equals(null)` is false.

The default `Object.equals()` compares identity. Override it only when the type has a meaningful value-equality definition.

### `hashCode()` contract

- If `x.equals(y)` is true, `x.hashCode()` and `y.hashCode()` must be equal.
- Unequal objects may have the same hash code; collisions are legal. A good implementation distributes likely values well to reduce collisions.
- A hash code must remain consistent while fields used by `equals()` remain unchanged. It need not remain stable across separate program runs.
- When overriding `equals()`, override `hashCode()` using the same equality-relevant fields.

```java
final class Employee {
  private final int id;
  private final String name;

  Employee(int id, String name) {
    this.id = id;
    this.name = Objects.requireNonNull(name);
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (other == null || getClass() != other.getClass()) return false;
    Employee employee = (Employee) other;
    return id == employee.id && name.equals(employee.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }
}
```

`Objects.hash(...)` is concise; for a very small performance-sensitive implementation, a hand-written hash may avoid varargs/array overhead. Correctness and field consistency matter more than choosing a particular formula.

### Why the contract matters

Hash-based collections first use the hash to locate a bucket, then use equality to identify a key. If equal objects have different hashes, a lookup can search a different bucket and fail to find the existing equal key. If a key's equality-relevant state changes after insertion, it may likewise become unreachable through normal lookup.

```java
Map<Employee, String> employees = new HashMap<>();
Employee first = new Employee(1, "Ava");
Employee second = new Employee(1, "Ava");

employees.put(first, "first");
employees.put(second, "second"); // replaces the value when equality/hash agree
```

Avoid mutable fields in equality and hashing for objects used as map keys or set members. For inheritance hierarchies, equality is difficult to extend safely: exact-class checks avoid many symmetry problems, while `instanceof`-based equality requires a deliberate design that preserves symmetry and transitivity.

Records automatically derive `equals()` and `hashCode()` from their components, which is useful for value objects; mutable objects stored in a component still require care.

## 10.6 Bytecode Execution and JIT Compilation

The interpreter starts bytecode quickly. As methods become hot, tiered compilation uses C1 and C2 compilers to produce optimized native code.

Common optimizations include:

- Method inlining.
- Escape analysis and scalar replacement.
- Lock elimination.
- Loop optimizations.
- Devirtualization when runtime types are predictable.

If an assumption becomes false, the JVM can deoptimize compiled code and return execution to a less optimized tier. Warmup is why short ad-hoc benchmarks are misleading.

## 10.7 Object Layout and Allocation

An object generally contains a header, instance fields, and alignment padding. Exact layout depends on JVM options and architecture.

- Thread-local allocation buffers make most small object allocations inexpensive.
- Large objects or exhausted buffers may take slower allocation paths.
- Escape analysis can eliminate some allocations, but code must not depend on that optimization.
- Compressed ordinary object pointers can reduce memory use for suitable heap sizes.

Use JOL or a profiler when exact layout matters; do not estimate from field sizes alone.

## 10.8 Native Memory

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

## 10.9 Class-Loader Identity and Leaks

A class is identified by both its binary name and defining class loader. The same class file loaded by different class loaders produces incompatible runtime types.

Application servers and plugin systems can leak class loaders when long-lived objects retain application classes through:

- Static collections.
- `ThreadLocal` values.
- Running threads or executors.
- JDBC drivers or callbacks not deregistered.
- Framework caches and listeners.

## 10.10 GC Terminology and Selection

- **Live set:** objects reachable after collection.
- **Allocation rate:** bytes allocated per unit time.
- **Pause:** application threads stop at a safepoint.
- **Concurrent phase:** GC work overlaps application execution.
- **Throughput:** application time relative to total elapsed time.

Minor, major, and full-GC terminology is collector-specific; always interpret actual GC logs for the selected collector. Enable unified logging on modern JDKs:

```text
-Xlog:gc*,safepoint:file=gc.log:time,uptime,level,tags
```

## 10.11 Common JVM Errors

- `OutOfMemoryError: Java heap space`: heap cannot satisfy allocation after GC.
- `OutOfMemoryError: Metaspace`: class metadata limit reached, often from excessive classes or loader leaks.
- `OutOfMemoryError: unable to create native thread`: OS/thread or native-memory limit reached.
- `StackOverflowError`: thread stack exhausted, usually by deep or infinite recursion.
- `LinkageError`: incompatible or duplicate class definitions, versions, or loader constraints.

## 10.12 Verification, Resolution, and Initialization

Verification checks bytecode structure, type safety, stack usage, and control flow before execution. Resolution converts symbolic references in the constant pool into direct runtime references and may occur lazily.

Initialization executes static field assignments and static blocks in textual order after parent initialization. Interfaces initialize differently: initializing an interface does not automatically initialize all parent interfaces.

## 10.13 Safepoints and Stop-the-World Pauses

At safepoints, JVM threads reach states where the runtime can safely inspect or modify shared VM structures. GC is a common reason, but deoptimization, biased-lock revocation in older JDKs, class redefinition, and some diagnostics may also require safepoints.

Pause time can include time for threads to reach a safepoint plus the operation itself. Unified safepoint logging helps distinguish these costs.

## 10.14 Escape Analysis

The JIT may determine that an object:

- Does not escape a method.
- Escapes only to the current thread.
- Escapes globally.

This information can enable scalar replacement, stack-like optimization, and lock elimination. The Java specification still models normal heap objects; these are runtime optimizations and not guaranteed.

## 10.15 Code Cache

JIT-compiled native methods reside in the code cache. If it fills, compilation may stop and application performance can degrade.

```text
jcmd <pid> Compiler.codecache
jcmd <pid> Compiler.queue
```

Investigate unusual compiler pressure, excessive generated classes, and JVM logs before changing code-cache flags.

## 10.16 CDS and Startup

Class Data Sharing stores preprocessed class metadata in an archive to improve startup and memory sharing:

- The JDK ships with a default archive for core classes.
- Application CDS can include application and library classes.
- Dynamic CDS can create an archive after a training run.

CDS mainly targets startup and footprint; validate archive compatibility when application classes or JDK versions change.

## 10.17 Container Awareness

Modern JVMs detect container CPU and memory limits, but deployment settings still require care:

- Leave headroom beyond heap for native memory.
- CPU limits influence GC and compiler thread ergonomics.
- Percentage-based heap flags can adapt across environments.
- Container OOM termination may occur before Java can write a heap dump.
- Monitor process resident memory as well as heap usage.

# 11. Advanced Core Java

This chapter covers APIs that interact with runtime metadata, external data, and platform resources. Treat those boundaries as explicit contracts: validate data, choose stable formats, preserve resource ownership, and account for failures.

## 11.1 Serialization and Cloning

Java object serialization converts an object graph to a byte stream; deserialization reconstructs it. It is a Java-specific format with compatibility and security concerns, not a general-purpose data interchange format.

```java
final class Employee implements Serializable {
  private static final long serialVersionUID = 1L;

  private final int id;
  private final String name;
  private transient String sessionToken;

  Employee(int id, String name) {
    this.id = id;
    this.name = name;
  }

  String getName() {
    return name;
  }
}
```

- A class must implement `Serializable`, and every non-transient object reachable through its serialized fields must also be serializable or serialization fails with `NotSerializableException`. Static fields belong to the class, so they are not serialized as instance state.
- `transient` omits a field from the default serialized form; it does not clear the field from memory. After deserialization, transient fields have default values unless restored explicitly.
- `serialVersionUID` is a compatibility identifier, not a migration mechanism. Matching identifiers do not guarantee that the old and new classes have equivalent meaning or valid state.
- Deserialization of serializable classes does not run their constructors or instance field initializers. Validate invariants in `readObject` or use a serialization proxy for carefully designed serializable value types.
- Never deserialize untrusted Java object streams without a restrictive `ObjectInputFilter` allowlist and resource limits. Prefer a schema-based format such as JSON or Protocol Buffers for long-lived files and service boundaries.

```java
try (ObjectOutputStream out = new ObjectOutputStream(
    Files.newOutputStream(Path.of("employee.ser")))) {
  out.writeObject(employee);
}

try (ObjectInputStream in = new ObjectInputStream(
    Files.newInputStream(Path.of("employee.ser")))) {
  in.setObjectInputFilter(ObjectInputFilter.Config.createFilter(
      "maxdepth=20;maxrefs=10000;maxbytes=1000000;Employee;!*"));
  Employee restored = (Employee) in.readObject();
}
```

An `ObjectInputFilter` is available since Java 9. Replace `Employee` in the allowlist with the actual fully qualified class names and include the required object graph types; a filter is not a substitute for treating serialized input as untrusted.

`Externalizable` gives a class explicit control through `writeExternal` and `readExternal`, but requires a public no-argument constructor and places more compatibility responsibility on the class. Use it only when that control is necessary.

Cloning uses `Object.clone()` only when the class implements `Cloneable`; otherwise the default implementation throws `CloneNotSupportedException`. The default operation is shallow: references to nested mutable objects are copied, not those objects themselves.

```java
final class Address {
  private final String city;

  Address(String city) {
    this.city = city;
  }
}

final class Employee {
  private final int id;
  private final String name;
  private final Address address;

  Employee(Employee other) {
    this.id = other.id;
    this.name = other.name;
    this.address = other.address; // safe to share because Address is immutable
  }
}
```

Prefer copy constructors or factories that clearly define shallow versus deep-copy behavior. A true deep copy must decide how every reachable mutable object should be copied, including cycles and shared references; blindly cloning the whole graph is rarely the right default.

## 11.2 Reflection and Annotations

Reflection inspects classes, fields, methods, constructors, and annotations at runtime. Frameworks use it for discovery and integration; ordinary, statically typed calls are simpler and safer when the type is already known.

```java
Class<?> type = Employee.class; // also available through instance.getClass()
Method method = type.getDeclaredMethod("getName");
Object result = method.invoke(employee);
```

- `getDeclaredFields()` and related `getDeclared*` methods include members declared on that class, including non-public members, but not inherited members. `getFields()` returns public fields including inherited ones; similar distinctions apply to methods and constructors.
- Reflection moves some compile-time checks to runtime. Lookup and invocation can fail with exceptions such as `NoSuchMethodException`, `IllegalAccessException`, and `InvocationTargetException`; inspect the latter's cause for an exception thrown by the invoked method.
- `setAccessible`/`trySetAccessible` remain subject to module encapsulation and access checks. Do not assume private JDK or application members can always be opened.
- Cache validated reflective metadata when it is repeatedly needed, and avoid exposing unrestricted reflective access to untrusted callers.

Annotations attach metadata to declarations. Retention determines how long metadata is kept: `SOURCE` is discarded by compilation, `CLASS` is stored in the class file but need not be visible at runtime, and `RUNTIME` is available through reflection.

```java
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@interface Endpoint {
  String value();
}

@Endpoint("/payments")
class PaymentService {
  @Endpoint("/charge")
  void charge() {}
}

Endpoint endpoint = PaymentService.class.getAnnotation(Endpoint.class);
```

`@Target` restricts where an annotation may appear. `@Inherited` applies only to class annotations obtained from superclasses; it does not propagate annotations on methods, fields, or implemented interfaces. Annotation presence and defaults are part of an API contract, so document custom annotations used by frameworks.

## 11.3 I/O, NIO, and File Handling

Java I/O APIs move bytes, characters, or structured data between a program and an external source or sink. Use byte streams for binary data and readers/writers for text; choose a charset explicitly for text.

| API style | Main abstractions | Typical use |
| --- | --- | --- |
| `java.io` | Streams, readers, writers | Straightforward blocking input/output |
| NIO.2 (`java.nio.file`) | `Path`, `Files`, file-system providers | Portable file and directory operations |
| Channels and buffers | `Channel`, `Buffer`, `Selector` | Large transfers or supported non-blocking I/O |

For common file work, `Files` and `Path` are usually the simplest choice. The following examples use APIs available since Java 11:

```java
Path path = Path.of("a.txt");

try (BufferedReader reader =
         Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
  String line;
  while ((line = reader.readLine()) != null) {
    System.out.println(line);
  }
}

Files.writeString(path, "hello", StandardCharsets.UTF_8);
```

- Streams handle bytes. For large files, stream or buffer data rather than loading the entire file into memory. `InputStream.transferTo` is available since Java 9.
- Try-with-resources closes streams, readers, writers, and channels on both success and failure. Use it whenever the API transfers ownership of a closeable resource to the current scope.
- `Files.exists` can return `false` when existence cannot be determined. If correctness depends on the operation, perform it and handle `IOException` rather than treating a prior existence check as a guarantee; the file can change between check and use.
- Use `Path.resolve` and normalization for path composition. For security-sensitive access, validate against an intended root and consider symbolic links and race conditions; lexical normalization alone does not prove a path stays under a directory.

Channels read and write through buffers. A buffer switches between write mode and read mode with `flip()`; after consuming the data, `clear()` prepares it for writing again. A single channel read or write may process fewer bytes than requested:
```java
try (FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {
  ByteBuffer buffer = ByteBuffer.allocate(1024);
  while (channel.read(buffer) != -1) {
    buffer.flip();
    while (buffer.hasRemaining()) {
      consume(buffer.get());
    }
    buffer.clear();
  }
}
```

NIO is not automatically faster than stream-based I/O. Choose based on the required semantics and benchmark realistic workloads.

## 11.4 JDBC

JDBC provides a vendor-neutral API for relational database access; a JDBC driver implements communication with a specific database. Obtain connections from a `DataSource`, usually managed by the application or a connection pool, rather than opening a new physical connection for each query.

Use `PreparedStatement` to bind data values separately from SQL syntax and try-with-resources to close connections, statements, and result sets on every exit path:

```java
String sql = "SELECT id, name FROM emp WHERE id = ?";
try (Connection connection = dataSource.getConnection();
     PreparedStatement statement = connection.prepareStatement(sql)) {
  statement.setInt(1, 100);
  try (ResultSet rows = statement.executeQuery()) {
    while (rows.next()) {
      int id = rows.getInt("id");
      String name = rows.getString("name");
      System.out.println(id + ": " + name);
    }
  }
}
```

Modern JDBC drivers normally load through service-provider discovery; explicit `Class.forName` is generally only needed for legacy setups. Parameter placeholders bind values, not table or column names; allowlist any dynamic SQL identifiers.

| API | Purpose |
| --- | --- |
| `Statement` | Execute fixed SQL with no external values; never concatenate untrusted input into the SQL text |
| `PreparedStatement` | Bind values separately from SQL syntax; the usual choice for parameters and repeated execution |
| `CallableStatement` | Invoke stored procedures, for example `{call proc(?, ?)}` |

Prepared statements help prevent SQL injection for bound values, but do not validate authorization, business rules, or dynamic identifiers. Performance benefits from server-side preparation depend on the driver and database.

Read `ResultSet` values while its statement and connection remain open. JDBC column indexes are one-based; `getXXX` may return a primitive default for SQL `NULL`, so use `wasNull()` immediately after reading a primitive or use nullable object accessors when appropriate.

```java
try (PreparedStatement statement =
         connection.prepareStatement("SELECT age FROM employee WHERE id = ?")) {
  statement.setLong(1, employeeId);
  try (ResultSet rows = statement.executeQuery()) {
    if (rows.next()) {
      int age = rows.getInt("age");
      Integer nullableAge = rows.wasNull() ? null : age;
    }
  }
}
```

For multi-statement work that must succeed or fail as a unit, use a transaction and handle rollback failures without discarding the original exception. See sections 11.8 and 11.13 for pooling, transactions, and isolation.

## 11.5 Serialization Safety and Versioning

- `serialVersionUID` controls compatibility checks but does not guarantee semantic compatibility.
- Adding fields is often compatible because missing fields receive defaults; changing field types or hierarchy can break compatibility.
- Validate invariants in `readObject`; constructors and field initializers are not called for normal serializable classes. `readObject` should call `defaultReadObject()` when using default field handling, then validate or normalize the restored state.
- For classes with invariants or evolving internal representation, the serialization-proxy pattern can serialize a small validated proxy and reconstruct the real object through its constructor.
- Treat compatibility as a schema-evolution problem: test old serialized fixtures against new code and define migration behavior instead of relying only on a matching UID.
- Prefer a stable schema format for long-lived storage and inter-service communication.
- Never deserialize untrusted native Java streams without a strict object filter.

## 11.6 NIO Buffers and Channels

A buffer has `capacity`, `position`, and `limit`. In write mode, `position` is where the next value is stored and `limit` is usually the capacity; after `flip()`, `limit` marks the written data and `position` resets for reading:

```java
ByteBuffer buffer = ByteBuffer.allocate(1024);
int read = channel.read(buffer); // may read fewer bytes than requested
if (read == -1) {
  // End of stream.
} else {
  buffer.flip();                 // switch from writing into buffer to reading it
  while (buffer.hasRemaining()) {
    consume(buffer.get());
  }
  buffer.clear();                // reset for another write
}
```

`clear()` does not erase bytes; it resets position and limit. `compact()` preserves unread bytes by moving them to the beginning and is useful when a message spans multiple reads. A channel write may also be partial, so keep writing while the buffer has remaining bytes when the full buffer must be sent. Direct buffers can reduce copying for some native I/O paths but use native memory and are more expensive to allocate; benchmark before using them.

## 11.7 File-System Correctness

- Specify charsets explicitly, usually `StandardCharsets.UTF_8`.
- For replace-style writes, write to a temporary file in the target directory, flush/close it, then use `Files.move` with `ATOMIC_MOVE` and (when required) `REPLACE_EXISTING`. Atomic moves are filesystem-dependent and may be unsupported; handle that case according to the durability and consistency requirements.
- Do not assume a single `read` or `write` processes the entire buffer.
- Close directory streams and file channels.
- Decide how symbolic links should be handled for security-sensitive operations. `normalize()` is lexical and does not resolve symlinks or prevent time-of-check/time-of-use races.
- Use streaming APIs for large files rather than `readAllBytes`.
- File attributes and permissions vary by filesystem and operating system; avoid assuming POSIX permissions or case-sensitivity on every platform.

## 11.8 JDBC Transactions and Pooling

- Obtain connections from a `DataSource`, normally backed by a connection pool.
- Keep transactions short; never wait for user input or remote network calls while holding one open.
- Return pooled connections by closing them.
- Choose isolation based on required consistency and database behavior.
- Batch repeated updates and inspect partial failures.
- Set query and transaction timeouts.
- A transaction usually belongs to one connection. Do not assume separate pooled connections share transaction state, and do not return a connection until commit or rollback has completed.
- If rollback itself fails, preserve that failure as suppressed (as in the example) and report the original transaction failure. Closing a pooled connection normally returns it to the pool; the pool should reset connection state, but application code should still follow its pool's documented contract.
- A database commit can fail after the server has committed but before the client receives confirmation. For retryable operations, design idempotency or reconciliation rather than assuming a failed `commit()` means nothing changed.

```java
try (Connection connection = dataSource.getConnection()) {
  connection.setAutoCommit(false);
  try {
    updateInventory(connection);
    createOrder(connection);
    connection.commit();
  } catch (SQLException e) {
    try {
      connection.rollback();
    } catch (SQLException rollbackFailure) {
      e.addSuppressed(rollbackFailure);
    }
    throw e;
  }
}
```

In framework-managed transactions, let the framework own commit, rollback, and connection lifecycle; do not manually commit a connection whose transaction is controlled by the framework.

## 11.9 Reflection and Method Handles

Reflection is flexible but shifts errors to runtime and can conflict with module encapsulation. Cache validated metadata when repeatedly used.

`MethodHandle` and `VarHandle` provide typed, JVM-supported dynamic access:

- `MethodHandle`: invoke methods, constructors, and fields through a typed signature.
- `VarHandle`: access fields or array elements with defined memory-ordering modes.

Use ordinary calls when types are known statically. Method handles are created through a `Lookup`, whose access rights control what can be found; a method handle's `MethodType` must match the call site's expected types. `invokeExact` requires an exact type match, while `invoke` permits adaptations.

VarHandles provide access modes such as plain, opaque, acquire/release, and volatile. Select a mode that satisfies the required memory-ordering contract; using a VarHandle does not make a multi-field invariant atomic.

## 11.10 ServiceLoader

`ServiceLoader` supports provider discovery without hard-coding implementations:

```java
ServiceLoader<PaymentProvider> providers =
    ServiceLoader.load(PaymentProvider.class);

for (PaymentProvider provider : providers) {
  provider.initialize();
}
```

Classpath providers use `META-INF/services/<fully-qualified-service-name>`; named modules declare `uses` and `provides ... with ...`. Provider configuration is discovered lazily during iteration, so missing or malformed providers can raise `ServiceConfigurationError` then, not necessarily at `load()`.

Use `ServiceLoader.stream()` when provider metadata or provider types are needed before instantiation. Decide what to do when no provider exists, multiple providers are found, or initialization fails; do not silently choose an arbitrary implementation when ordering is not part of the contract.

## 11.11 Memory-Mapped Files

`FileChannel.map` maps a file region into memory:

```java
try (FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {
  MappedByteBuffer buffer =
      channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
  consume(buffer);
}
```

Memory mapping can help random access and large-file workloads, but page faults, address-space use, file locking behavior, and unmapping timing are platform-sensitive. Benchmark against buffered I/O.

  - A mapping's size and mode must match the intended access; writable mappings can modify the underlying file. `MappedByteBuffer.force()` can request that updates be written to storage, but durability guarantees still depend on the operating system and filesystem.
  - Closing the `FileChannel` does not necessarily unmap an existing mapping immediately. Avoid relying on prompt unmapping to release file locks or address space; structure mapping lifetime carefully and account for platform behavior.
  - Mapping very large files can exceed address-space or implementation limits. Map manageable regions when needed and handle file truncation or concurrent modification according to the application's contract.

  ## 11.12 Asynchronous and Non-Blocking I/O

  - `AsynchronousFileChannel` completes file operations through futures or callbacks; asynchronous completion does not imply that the operating system performs every operation without worker threads.
  - A `Selector` multiplexes readiness events from supported non-blocking channels on a thread. A selected key indicates an operation may make progress, not that a complete message was read or written.
  - Non-blocking reads and writes can be partial or return zero. Maintain per-connection buffers and protocol state, and continue writes when the output buffer still has remaining bytes.
  - Network protocols still require message framing, partial-read handling, backpressure, cancellation, and timeouts. A selector loop must remove processed selected keys and handle channel closure and errors.

  Frameworks such as Netty encapsulate much of this complexity. Do not build a custom event loop unless requirements justify it.

## 11.13 JDBC Isolation Levels

Standard JDBC levels include:

- `READ_UNCOMMITTED`: may allow dirty reads.
- `READ_COMMITTED`: prevents dirty reads.
- `REPEATABLE_READ`: also protects repeated reads, with database-specific phantom behavior.
- `SERIALIZABLE`: strongest isolation, lowest concurrency.

Databases implement multiversioning and locking differently. Verify actual semantics, deadlock behavior, and retry requirements for the chosen database.

The level requested through JDBC is not necessarily supported exactly as requested; drivers/databases may reject it or provide documented alternatives. Isolation level alone does not prevent every application-level race: use constraints, locking reads, optimistic version columns, or retry logic where the invariant requires them.

## 11.14 JDBC Batching and Generated Keys

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

Execute batches inside an explicit transaction when the group must be atomic. Inspect update counts and driver behavior; `SUCCESS_NO_INFO` and `EXECUTE_FAILED` are special count values, and a batch exception may report that some earlier commands succeeded.

## 11.15 Annotation Processing

Annotation processors run during compilation and can validate code or generate source/resources. Examples include mapper generators and immutable-value tools.

- Register processors through the service-provider mechanism or build configuration.
- Generated source should be deterministic.
- Separate annotation-processor dependencies from runtime dependencies.
- Incremental builds depend on processors accurately declaring their behavior.
- Generated code should remain inspectable and testable.
- Processors run in rounds as generated source can introduce new annotated elements. Use `Filer` to create generated files and `Messager` to report diagnostics instead of writing into source directories directly.
- A processor's classpath is a build-time concern; avoid making consumers depend on it at runtime unless generated code actually references its runtime API.

## 11.16 Dynamic Proxies

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

- JDK dynamic proxies implement interfaces; they do not proxy arbitrary concrete classes. Every proxy call goes through the handler, including `equals`, `hashCode`, and `toString`, so decide how those methods should behave.
- `Method.invoke` wraps a target exception in `InvocationTargetException`; unwrap and rethrow its cause when preserving the target's contract. For interface default methods, use the supported default-method invocation mechanism rather than assuming `method.invoke(proxy, ...)` will call the default implementation.
- Proxy handlers should account for `arguments == null` on no-argument methods, preserve return types and declared exceptions, and avoid leaking sensitive method arguments into logs.

# 12. Java Platform Module System

The Java Platform Module System (JPMS), introduced in Java 9, groups packages into modules with explicit dependencies and access boundaries. A module descriptor, `module-info.java`, declares which other modules are required and which of its packages are part of its supported API.

```text
module descriptor -> dependency graph -> module resolution -> access checks
```

JPMS improves configuration and encapsulation; it is not a sandbox and does not replace application-level authorization or operating-system security.

## 12.1 Named, Automatic, and Unnamed Modules

- A **named module** has a `module-info.class` descriptor, normally authored as `module-info.java` at the module source root. Module names are case-sensitive identifiers; reverse-DNS names are a convention, not a requirement.
- An **automatic module** is a non-modular JAR placed on the module path. Its name comes from the manifest's `Automatic-Module-Name`, if present, or is derived from the JAR filename. It reads other resolved modules and exports all its packages, so it provides weaker encapsulation than an explicit descriptor.
- The **unnamed module** contains classpath code. It reads observable named modules, but named modules cannot declare a `requires` dependency on the unnamed module.

Automatic modules ease migration but expose all packages and have less reliable names unless the manifest defines a stable one. Prefer explicit module descriptors for libraries intended for modular applications. Named modules in one resolved configuration cannot freely split packages across modules; avoid placing the same package in multiple named modules.

```java
module com.example.orders {
  requires java.sql;
  requires transitive com.example.money;

  exports com.example.orders.api;
  opens com.example.orders.dto to com.fasterxml.jackson.databind;
}
```

## 12.2 Strong Encapsulation

`exports` makes public types in a package accessible to code in other modules at compile time and runtime. It does not export subpackages automatically. `opens` permits deep reflection at runtime and does not by itself make the package available for ordinary compile-time access. They solve different problems:

```java
module com.example.orders {
  exports com.example.orders.api;
  opens com.example.orders.dto to com.fasterxml.jackson.databind;
}
```

Qualified exports or opens grant access only to listed modules. Avoid opening every package merely to silence reflective-access failures.

`open module com.example.app { ... }` opens all of the module's packages for deep reflection; use targeted `opens` where possible. `requires` declares a readability dependency; it does not export the requiring module's packages. Consumers also need the target package exported, and the referenced type/member must be accessible:

```java
module com.example.web {
  requires com.example.orders;
}
```

Use `requires transitive` only when a dependency's types are part of your module's public API and downstream modules need readability to use them.

`requires static name;` makes a dependency required at compile time but optional at runtime. Use it only when code can operate without that module at runtime, for example when the dependency is used solely for annotations.

## 12.3 Compilation and Execution

With a conventional source layout:

```text
src/
  com.example.app/
    module-info.java
    com/example/app/Main.java
  com.example.orders/
    module-info.java
    com/example/orders/api/Order.java
```

The module source-path form compiles a module and its source dependencies:

```text
javac -d out --module-source-path src -m com.example.app
java --module-path out -m com.example.app/com.example.app.Main
```

For an already compiled modular JAR, place dependencies on the module path and launch by module and main class:

```text
javac --module-path mods -d out src/com.example.app/module-info.java src/com.example.app/com/example/app/Main.java
java --module-path "mods;out" -m com.example.app/com.example.app.Main
```

Use the platform-appropriate path separator when listing multiple module-path entries (`;` on Windows, `:` on Unix-like systems).

Useful analysis tools:

```text
jdeps --recursive app.jar
jar --describe-module --file library.jar
jlink --module-path out --add-modules com.example.app --output runtime
```

`jlink` creates a custom runtime image containing selected modules and dependencies. It is useful for controlled deployments but must be rebuilt for security updates.

Classpath and module-path launch modes have different resolution and encapsulation rules. Test the exact packaged artifact and launch command; a successful IDE classpath run does not prove the module graph is valid.

`--add-modules` can add root modules to resolution; `--add-reads`, `--add-exports`, and `--add-opens` can temporarily adjust access during migration. Keep such overrides explicit and remove them when descriptors and dependencies are corrected.

## 12.4 Migration Strategy

1. Remove dependencies on JDK internals.
2. Give published JARs stable automatic module names.
3. Resolve split packages and cyclic dependencies.
4. Add descriptors to libraries from the leaves upward.
5. Open only packages that frameworks need for reflection.
6. Test both modular packaging and runtime launch commands.

Do not modularize solely to add a descriptor if the dependency graph, package layout, or frameworks still require broad reflective access. Test both classpath and module-path consumers when publishing a library intended to support both.

## 12.5 Services Across Modules

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

## 12.6 Reflection and Modules

Named modules strongly encapsulate non-exported packages. Reflective frameworks may require:

- Targeted `opens` in `module-info.java`.
- Command-line `--add-opens` during migration.
- Framework support that avoids deep reflection.

`--add-opens` and `--add-exports` are deployment escape hatches, not ideal permanent library contracts.

## 12.7 Module Layers

A `ModuleLayer` can load additional module configurations at runtime, useful for plugin systems. Each layer can use distinct class loaders and service providers.

This flexibility adds class-identity, lifecycle, and unloading complexity. Define strict plugin APIs and prevent plugins from depending on application internals.

## 12.8 Modular JARs and Multi-Release JARs

- A modular JAR contains `module-info.class`.
- A multi-release JAR can provide version-specific classes under `META-INF/versions/<n>`.
- The base classes must support the minimum runtime.
- Versioned implementations should preserve the same public API.

Test every supported runtime because only that runtime selects its relevant entries.

## 12.9 JPMS Limitations and Decisions

JPMS provides reliable configuration and strong encapsulation, but it is not a security sandbox. It does not replace process isolation, authorization, or OS permissions.

Libraries should consider module compatibility even when applications remain on the classpath. Applications should adopt modules when encapsulation, custom runtime images, or explicit dependency graphs justify migration cost.

# 13. Modern Java Features

Modern Java releases add language features and library APIs incrementally. Check both the feature's first permanent release and the application's target runtime before using it; some features were previewed before becoming final.

| Feature | Permanent release |
| --- | --- |
| Local-variable type inference (`var`) | Java 10 |
| `var` in lambda parameters | Java 11 |
| Helpful NullPointerExceptions | Java 14 |
| Switch expressions | Java 14 |
| Text blocks | Java 15 |
| Pattern matching for `instanceof` | Java 16 |
| Records | Java 16 |
| Sealed classes and interfaces | Java 17 |
| Pattern matching for `switch` | Java 21 |
| Virtual threads | Java 21 |
| Sequenced collections | Java 21 |
| Record patterns | Java 21 |
| Unnamed variables and patterns | Java 22 |
| Foreign Function and Memory API | Java 22 |
| Scoped values | Java 25 |

Use `javac --release N` (or the equivalent build-tool setting) to verify code against a specific Java release's language level and documented APIs. Preview features are tied to a particular JDK release and require that JDK's `--enable-preview` option at compilation and runtime; they are not a way to target a different release.

## 13.1 `var` for Local Variables (Java 10)

```java
var names = new ArrayList<String>(); // inferred as ArrayList<String>
var total = calculateTotal();        // inferred from return type
```

`var` is not dynamic typing. The compiler infers one static type from the initializer; the declared variable cannot later hold a value of an unrelated type. It works for initialized local variables and enhanced-for variables starting in Java 10. Starting in Java 11, lambda parameters may also use `var`, but it must be used consistently for all parameters in that lambda.

```java
var count = 3;                    // int
var names = new ArrayList<String>(); // ArrayList<String>
for (var name : names) {
  System.out.println(name);
}
Function<String, Integer> length = (var text) -> text.length();
```

It cannot be used for fields, method parameters, return types, variables without an initializer, or a `null`-only initializer. Avoid it when the inferred type is obscure, exposes an implementation detail, or makes the code harder to read.

## 13.2 Helpful NullPointerExceptions (Java 14)

The JVM can identify which part of a chained expression was null. This improves diagnostics but does not replace input validation or null-safe design.

## 13.3 Records (Final in Java 16)

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

The compiler generates a canonical constructor, component accessors, `equals`, `hashCode`, and `toString`. A compact constructor validates or normalizes parameters; assigning to a component name updates the value that will be assigned to the component field. Records are implicitly final, cannot extend another class, and may implement interfaces or declare static members and instance methods, but cannot declare additional instance fields.

## 13.4 Sealed Types (Final in Java 17)

```java
sealed interface Result permits Success, Failure {}
record Success(String value) implements Result {}
record Failure(String message) implements Result {}
```

Permitted implementations must be `final`, `sealed`, or `non-sealed`. Sealed hierarchies work well with exhaustive pattern matching.

Direct permitted subclasses must be in the same named module, or in the same package when the types are in the unnamed module. Use `final` to close a branch, `sealed` to constrain its next level, and `non-sealed` to reopen that branch to unrestricted extension.

## 13.5 Pattern Matching for `switch` (Final in Java 21)

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

Pattern switch supports type patterns and guarded patterns; a `when` guard adds a condition to a case. Put narrower or guarded cases before broader cases that would dominate them. Handle `null` explicitly when it is a valid input, since a type-pattern switch otherwise throws `NullPointerException` for a null selector.

## 13.6 Virtual Threads (Final in Java 21)

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

## 13.7 Sequenced Collections (Java 21)

`SequencedCollection`, `SequencedSet`, and `SequencedMap` provide a uniform API for ordered collections:

```java
SequencedCollection<String> names = new ArrayList<>();
names.addFirst("A");
names.addLast("B");
String first = names.getFirst();
SequencedCollection<String> reversed = names.reversed();
```

## 13.8 Switch Expressions and Text Blocks

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

Switch expressions must produce a value for every possible path. Use `yield` from a multi-statement case block:

Arrow cases do not fall through. A colon-style switch expression may group labels, but each path must produce a value with `yield` or complete abruptly. For example, this case performs work before yielding its result:
```java
String label = switch (status) {
  case ACTIVE -> {
    audit(status);
    yield "Active";
  }
  default -> "Other";
};
```

Text blocks are `String` literals. Their incidental indentation is removed according to the closing delimiter's position; they do not automatically escape or validate JSON, SQL, HTML, or another embedded language.

## 13.9 Feature Lifecycle and Compatibility

Java features may be permanent, preview, incubating, or experimental:

- Preview language/API features require `--enable-preview` at compile and run time for the corresponding JDK release and may change or be removed before finalization.
- Incubator modules are non-final APIs that must be added explicitly.
- Experimental JVM features may require flags and are not compatibility commitments.
- A source/bytecode target alone is not enough to validate dependencies: third-party libraries and runtime features must also support the deployment JDK.

Compile with the correct release target:

```text
javac --release 17 Main.java
```

`--release` constrains language features, bytecode level, and documented JDK APIs together. Setting only `-source` and `-target` does not prevent accidental use of newer library APIs.

## 13.10 Pattern Matching Design

Patterns improve data-oriented branching but should not replace polymorphism automatically.

- Use polymorphism when behavior naturally belongs to each subtype.
- Use a pattern switch when an operation belongs to the consumer and the hierarchy is closed.
- Guarded cases should appear before broader cases.
- Exhaustive sealed-type switches make new subtype additions visible as compile errors.

## 13.11 Virtual Threads vs Reactive Programming

Virtual threads simplify high-concurrency blocking code and stack traces. Reactive APIs remain useful when:

- End-to-end libraries are already non-blocking.
- Streaming backpressure is central.
- The application composes event streams rather than request-per-task workflows.

Do not mix models casually. Blocking inside an event-loop thread can stall many requests, while wrapping every trivial call in a virtual thread adds complexity without benefit.

## 13.12 New Collection and Stream Conveniences

Modern JDKs include useful additions such as:

- `List.of`, `Set.of`, `Map.of` for compact immutable collections.
- `Stream.toList()` for an unmodifiable encounter-ordered list.
- `Collectors.teeing` to combine two downstream reductions.
- `Stream.mapMulti` for one-to-many mapping without creating a stream for each element.
- `Optional.stream` to integrate optional values into pipelines.

Check the exact minimum JDK version before adopting an API in a shared library.

## 13.13 Record Patterns

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

## 13.14 Unnamed Variables and Patterns

Modern Java permits `_` in selected declarations where a value is intentionally unused:

```java
try {
  perform();
} catch (ExpectedException _) {
  recover();
}
```

Unnamed variables and patterns became permanent in Java 22. They document intentional non-use and prevent accidental access; compile using a release that supports the syntax.

## 13.15 Foreign Function and Memory API

The Foreign Function and Memory (FFM) API became a permanent feature in Java 22. It provides supported access to native libraries and off-heap memory without much of JNI's boilerplate.

Core concepts include:

- `Arena` for memory-segment lifetime.
- `MemorySegment` for bounded memory access.
- `Linker` and function descriptors for native calls.
- Layouts and variable handles for structured data.

Native interaction remains unsafe at the system boundary: signatures, ownership, thread rules, and library compatibility must be exact.

## 13.16 Scoped Values

Scoped values became a permanent feature in Java 25. They provide immutable context inherited through a bounded dynamic scope and are designed as a safer alternative to many `ThreadLocal` use cases, especially with virtual threads.

They are useful for request metadata such as trace identity, not for mutable global state:

```java
static final ScopedValue<String> REQUEST_ID = ScopedValue.newInstance();

ScopedValue.where(REQUEST_ID, "req-123").run(() -> {
  System.out.println(REQUEST_ID.get());
});
```

Compile against a JDK that includes the feature; preview status can differ across earlier releases.

## 13.17 API Evolution Awareness

When using modern APIs:

- Check the minimum JDK release.
- Check whether a feature requires preview flags.
- Avoid exposing preview types in stable public APIs.
- Consider runtime vendors and deployment tooling.
- Use multi-release JARs only when one artifact truly needs optimized per-JDK implementations.
- Document fallback behavior for older supported runtimes.