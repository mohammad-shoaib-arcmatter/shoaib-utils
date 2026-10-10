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

- `if`-`else if`-`else` ladder: Use this to choose one path based on conditions, especially for ranges. Java checks conditions from top to bottom and runs only the first matching branch, so order cases from most specific to least specific. A final `else` handles any value not matched above.
  if (score >= 90) grade='A';
  else if (score >= 75) grade='B';
  else grade='C';

- `switch`: Use this to select among discrete values rather than ranges. Traditional `switch` supports integral types except `long`, `char`, `String`, and enums. In colon-style cases, execution continues into the next case unless stopped with `break`, `return`, or another control-flow statement; unintended fall-through is a common bug.
  int month=2;
  switch(month){
    case 1: System.out.println("Jan"); break;
    case 2: System.out.println("Feb"); break;
    default: System.out.println("Invalid");
  }

  // Java 14+ switch expression: arrow cases do not fall through and produce a value
  String res = switch(month){
    case 1 -> "Jan";
    case 2 -> "Feb";
    default -> "Invalid";
  };
  // Every possible input must produce a value; include a default unless all values are covered.

- Loops repeat a block while their condition is satisfied. Ensure each loop can eventually stop—for example, update a counter or consume input—or it may run indefinitely.
  // for - keeps initialization, condition, and update together; useful for counted repetition
  for(int i=0; i<10; i++){ if(i==5) continue; }

  // while - tests before each iteration; useful when the number of repetitions is not known in advance
  while(scanner.hasNext()){ }

  // do-while - tests after the body, so the body always runs at least once
  do{ } while(x<10);

  // for-each - visits every element of an array or iterable collection; use an indexed loop if you need the position
  for(String s: list){ System.out.println(s); }

- `break`, `continue`, and `return` have different scopes: `break` exits the nearest loop or `switch`; `continue` skips the rest of the current loop iteration and starts the next one; `return` exits the current method (and supplies a value when required). A labeled `break` exits the named enclosing loop, which is useful for leaving nested loops.
  outer: for(int i=0; i<3; i++) {
    for(int j=0; j<3; j++) {
      if(i == 1 && j == 1) break outer; // exits both loops
    }
  }

## 1.5 Input and Output

- Output:
  System.out.println(); // with newline
  System.out.print(); // without
  System.err.println("error"); // for error stream
  System.out.printf("Name %s, age %d, sal %.2f", name, age, sal);

- Input:
  // Method 1: Scanner - convenient for interactive input
  import java.util.Scanner;
  Scanner sc = new Scanner(System.in);
  int a = sc.nextInt(); // leaves \n
  sc.nextLine(); // consume leftover
  String name = sc.nextLine(); // full line
  String word = sc.next(); // single word
  // Avoid closing a Scanner wrapping System.in if the rest of the app still needs stdin.

  // Method 2: BufferedReader - fast, for large input
  BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
  String line = br.readLine();
  int num = Integer.parseInt(line);

  // Method 3: System.console - for password
  Console c = System.console();
  char[] pwd = c == null ? null : c.readPassword(); // console may be unavailable in an IDE or redirected process


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

- Arrays are fixed-size objects with zero-based indexes.
- Array elements receive defaults; a local array reference does not.
- Arrays are covariant, so `Number[] values = new Integer[2]` compiles but can throw `ArrayStoreException`.
- Common utilities include `Arrays.copyOf`, `sort`, `binarySearch`, `equals`, and `deepEquals`.
- For resizable sequences, prefer `ArrayList`.

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

## 2.1 Classes, Objects, and Constructors

- A class declares a type; creating an instance gives that type object identity and state.
  public class Employee {
    String name; // instance variable
    static String company = "Stitch"; // static - shared by all
  }
- An assignment copies a reference, not the object:
  Employee e1 = new Employee(); // new creates object
  e1.name = "Ali";
  Employee e2 = e1; // both references designate the same object
- A constructor initializes a new instance. It has the class name and no return type.
  public class Employee {
    String name;
    // The compiler supplies a no-argument constructor only if no constructor is declared.
    Employee() { this("Unknown"); }

    // Parameterized
    Employee(String name) { this.name = java.util.Objects.requireNonNull(name); }

    // Copy constructors are a convention, not a Java-generated feature.
    Employee(Employee other) { this(other.name); }
  }
- `this(...)` delegates to another constructor in the same class and must be the first constructor statement. Constructor chaining centralizes validation and initialization.

## 2.2 Encapsulation and Accessors

- Encapsulation: Hide data using private, expose via methods. For data security + validation.
  public class BankAccount {
    private double balance; // cannot access directly from outside

    public double getBalance() { return balance; } // getter

    public void setBalance(double bal) {
      if (bal < 0) throw new IllegalArgumentException("balance must be non-negative");
      this.balance = bal;
    }
  }
- Encapsulation protects invariants; a setter that accepts every value can still expose invalid state. For financial calculations, prefer `BigDecimal` with a documented scale and rounding policy over `double`.
- Abstraction presents a useful contract while hiding implementation details; encapsulation controls access to state and behavior.

## 2.3 Inheritance

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
- Common class hierarchies are single, multilevel, and hierarchical. A class can extend only one class but can implement multiple interfaces.
- Every class without an explicit superclass extends `Object`; interfaces do not extend `Object`.

## 2.4 Polymorphism

- Overloading: methods share a name but have different parameter signatures; overload resolution uses compile-time argument types and applicable conversions. Return type alone does not distinguish overloads.
  class Calculator {
    int add(int a, int b) { return a+b; }
    int add(int a, int b, int c) { return a+b+c; } // diff count
    double add(double a, double b) { return a+b; } // diff type
  }
  // Rules: Return type alone not enough to overload, must change params
- Overriding: a subtype supplies an implementation of an inherited instance method. The runtime receiver type selects the implementation; fields and static methods are not dynamically overridden.
  class Bank { double getRate() { return 5.0; } }
  class SBI extends Bank {
    @Override
    double getRate() { return 7.5; } // overrides
  }
  Bank b = new SBI(); // Parent ref, Child object
  b.getRate(); // 7.5 - Child's method runs - Runtime Polymorphism
- Rules for overriding: the method must be inherited and have a subsignature; private methods are not inherited, static methods are hidden, and final methods cannot be overridden. An override cannot reduce visibility or broaden checked exceptions.

## 2.5 Abstraction: Abstract Classes and Interfaces

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
| Can have constructors and instance state | No constructors or per-instance state; fields are constants |
| Single inheritance | Multiple interfaces can be implemented |
| Use for shared base state/implementation | Use to define capabilities and decoupled contracts |

Interfaces may define `default` and `static` methods (Java 8+) and private helper methods (Java 9+). An implementing class must still provide public implementations of abstract interface methods.

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

### Interview Notes

- Outside the package, a subclass can access a protected instance member through `this`, or through a reference whose compile-time type is that subclass (or its subtype); it cannot freely access the member through an arbitrary parent-typed reference.
- Encapsulation uses private + public getters/setters.

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

## 3.1 `this` Keyword

`this` refers to the current instance in an instance context. It can disambiguate fields from parameters, delegate constructors, and be passed or returned as a reference.
class Employee {
  String name;
  Employee(String name) {
    this.name = name; // this.name = instance var, name = local param
  }
  void display() {
    System.out.println(this); // calls toString(); Object's default form is not a memory address
  }
  void methodA() { this.methodB(); } // calls current class method
  Employee getObj() { return this; } // return current object

  Employee() { this("Unknown"); } // this() calls same class constructor, must be first line
}
- Interview: this cannot be used in static context because static has no object.

## 3.2 `super` Keyword

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

## 3.3 `final` Keyword

1.  final variable: Constant, cannot reassign. Must init once.
    final int MAX = 100;
    // MAX = 200; // ERROR
    final int x; x = 10; // blank final - allowed if init in constructor
2.  final method: Cannot be overridden.
    class Parent { final void pay(){} }
    class Child extends Parent { // void pay(){} ERROR
    }
3.  final class: Cannot be inherited. Useful to prevent subclassing, but not by itself a security boundary.
    final class SBI {} // class Child extends SBI ERROR
    // String, Integer are final classes in Java

## 3.4 `static` Keyword

Static members belong to a class rather than an instance; their lifetime and identity follow the defining class loader.
class Employee {
  String name; // instance - each object has own copy
  static String company = "Stitch"; // class variable shared by instances of this loaded class
  static int count = 0;

  Employee() { count++; } // counts objects

  static void changeCompany() { 
    company = "Stitch.sa"; 
    // System.out.println(name); ERROR - static cannot access non-static directly
  }
  void display() {
    System.out.println(name + " " + company); // non-static can access static
  }
  static { System.out.println("Static block runs once when this class is initialized"); }
}
Employee.changeCompany(); // call without object - ClassName.method
System.out.println(Employee.company);
- Static method cannot use this/super.
- A static initializer runs as part of class initialization on first active use, not necessarily before `main` in every program.
- Interview trick: Can we override static method? No, it's method hiding not overriding. Parent p = new Child(); p.staticMethod() calls Parent's, not Child's.

## 3.5 Packages and Imports

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

## 3.6 Wrapper Classes and Autoboxing

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
- Caching: `Integer.valueOf` is guaranteed to reuse instances for at least -128 through 127. Implementations may cache more values, so never use `==` to compare wrapper values.

Interview note: use `.equals()` rather than `==` to compare wrapper values.

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

- Each thread has a JVM stack for method frames and execution state. The JVM specification does not require every local value or reference to physically reside on a native stack.
- Objects and arrays are logically allocated from the heap and are garbage-collected when unreachable. JIT optimizations may eliminate some allocations.
- A `StackOverflowError` can result from excessive recursion; `OutOfMemoryError` can result from heap exhaustion, native-memory exhaustion, or other resource limits.
- Class metadata is commonly stored in native-memory Metaspace in HotSpot. The string intern pool is on the heap; static fields are not generally stored in Metaspace as ordinary object state.

## 4.2 String Pool

- String literals and compile-time constant strings are interned; equal literals in the same runtime can refer to the same pooled object.
String s1 = "Stitch";
String s2 = "Stitch";
System.out.println(s1 == s2); // true: both refer to the interned literal

- intern() method:
String s3 = new String("Stitch"); // explicitly creates a distinct String object
String s4 = s3.intern();          // returns the canonical pooled reference
// s1 == s4 is true; s1 == s3 is false

## 4.3 String, StringBuilder, and StringBuffer

| Feature | String | StringBuilder | StringBuffer |
| --- | --- | --- | --- |
| Mutable? | NO - Immutable | YES - Mutable | YES - Mutable |
| Thread Safe? | Yes (immutable) | No - faster | Yes - synchronized, slower |
| When to use | General text values | Repeated mutation in one thread | Legacy shared mutable text |
| Memory | New object each change | Same object modified | Same object modified |

- Immutability allows safe sharing, pooling, and stable hash keys. It does not make `String` a secure password container; secrets may remain in memory until collected.
// Concatenation produces a new String value; repeated concatenation in a loop can recopy growing text.
String s = "a";
s = s + "b"; // s now refers to "ab"; the old value may be reclaimed when unreachable

// StringBuilder - good
StringBuilder sb = new StringBuilder("a");
sb.append("b"); // same object modified, no new object
sb.append("c").reverse().toString();

StringBuffer sbf = new StringBuffer("a");
sbf.append("b"); // synchronized - thread safe
- For concatenation in a loop, `StringBuilder` is usually clearer and avoids repeatedly copying the growing result. Simple `+` expressions are optimized by modern compilers and should not be mechanically replaced.
- Interview code:
String s = "a"; for(int i=0;i<1000;i++) s+= "b"; // repeatedly copies the growing result
StringBuilder sb = new StringBuilder(); for(int i=0;i<1000;i++) sb.append("b"); // reuses a mutable buffer; it may resize

## 4.4 `equals()` and `==`

- `==`: For references, checks identity (whether both variables refer to the same object), not a raw address.
- .equals() : Checks content equality. Method defined in Object class, String class overrides it to check characters.
String s1 = "Stitch";
String s2 = "Stitch";
String s3 = new String("Stitch");

System.out.println(s1 == s2); // true - same interned object
System.out.println(s1 == s3); // false - s3 is a distinct object
System.out.println(s1.equals(s3)); // true - content same "Stitch"

int a = 10, b = 10;
System.out.println(a == b); // true - for primitives, == checks value

Employee e1 = new Employee("Ali");
Employee e2 = new Employee("Ali");
System.out.println(e1 == e2); // false - different objects
System.out.println(e1.equals(e2)); // false by default! Because Object's equals() also uses ==
// To make content check, you must OVERRIDE equals() in Employee class
- Override `equals` and `hashCode` together when instances have value-based equality. A safe implementation checks identity, null/type compatibility, and null-safe component equality:
  ```java
  final class Employee {
    private final String name;

    Employee(String name) {
      this.name = java.util.Objects.requireNonNull(name);
    }

    @Override
    public boolean equals(Object other) {
      if (this == other) return true;
      if (!(other instanceof Employee)) return false;
      Employee employee = (Employee) other;
      return name.equals(employee.name);
    }

    @Override
    public int hashCode() {
      return name.hashCode();
    }
  }
  ```
- Do not include mutable fields in equality/hash calculations if instances will be used as map keys or set elements; changing a key after insertion can make it unfindable.
- Rule for interview: 
    - For primitives always ==
    - For String content always .equals()
    - Never use == for String content - bug!

- Bonus: equals() contract - reflexive, symmetric, transitive, consistent.

## 4.5 Unicode and String Operations

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

## 4.6 Concatenation and Formatting

- The compiler usually optimizes simple concatenation.
- Repeated concatenation inside loops should use `StringBuilder`.
- `String.join` and `Collectors.joining` handle delimiters cleanly.
- `String.formatted` and `Formatter` improve readability but are slower in hot paths.
- Never build SQL by concatenating values; formatting does not make SQL safe.

## 4.7 Defensive String Handling

- Use `isBlank()` when whitespace-only input is invalid.
- `strip()` is Unicode-aware; `trim()` removes only characters up to U+0020.
- Use `equalsIgnoreCase()` only when its locale-independent semantics fit the domain.
- Prefer short-lived `char[]` for secrets where APIs support it, though copies may still exist.

## 4.8 Reference Strengths and Cleanup

- Strong references keep objects alive normally.
- `SoftReference` may be cleared under memory pressure and is unsuitable for predictable cache policy.
- `WeakReference` does not prevent collection and can support carefully designed canonical mappings.
- `PhantomReference` plus `ReferenceQueue` supports post-mortem cleanup coordination.

Finalization is deprecated for removal and has unpredictable timing. Use try-with-resources; use `Cleaner` only as a last-resort safety net.

## 4.9 String Internals and Compact Strings

Modern JDK implementations may store strings internally as Latin-1 or UTF-16 bytes using compact strings. This is an implementation detail, not an API guarantee.

- Never depend on a particular backing representation.
- `substring` in modern JDKs creates independent storage rather than retaining the original full array.
- String hash codes may be cached because strings are immutable.
- Interning unbounded dynamic input can retain large numbers of strings and should not be used as a general cache.

## 4.10 Regular Expressions

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

## 4.11 Character Encoding

Text becomes bytes only through a charset:

```java
byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
String restored = new String(bytes, StandardCharsets.UTF_8);
```

- Never rely on the platform default for persisted or network data.
- UTF-8 is variable-length and widely interoperable.
- A byte-order mark may appear in some files and may need explicit handling.
- Configure malformed/unmappable input behavior with `CharsetDecoder` when silent replacement is unacceptable.

## 4.12 String Comparison and Collation

- `String.compareTo` compares UTF-16 values lexicographically, not natural-language dictionary order.
- Use `Collator` for locale-sensitive user-facing sorting.
- Normalize Unicode when canonically equivalent sequences must compare consistently.

```java
String normalized = Normalizer.normalize(input, Normalizer.Form.NFC);
Collator collator = Collator.getInstance(userLocale);
names.sort(collator);
```

Normalization and case folding have domain-specific security implications; identifiers should follow a documented policy.

# 5. Exception Handling

## 5.1 Exception Hierarchy

Every throwable extends `Throwable`, whose two major branches are `Error` and `Exception`. Exceptions include checked exceptions and unchecked `RuntimeException` subclasses. Applications usually do not catch `Error`, but recovery policy depends on the failure and process boundary rather than a blanket rule.

## 5.2 Checked and Unchecked Exceptions

| Checked (Compile-time) | Unchecked (Runtime) |
| --- | --- |
| Compiler forces you to handle | Compiler doesn't force |
| Often represents an external or recoverable condition | Often represents invalid state or a programming error |
| Must use try-catch or throws else compile error | No need, but you can |
| Eg: IOException, SQLException, ClassNotFoundException, FileNotFoundException | Eg: NullPointerException, ArithmeticException, ArrayIndexOutOfBounds, NumberFormatException |
| Extends Exception directly | Extends RuntimeException |

// Checked - compile error if not handled
FileReader fr = new FileReader("file.txt"); // Must surround with try-catch

// Unchecked - compiles fine, fails at runtime
int a = 10/0; // ArithmeticException at runtime
String s = null; s.length(); // NullPointerException

## 5.3 `try`, `catch`, `finally`, `throw`, and `throws`

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
finally { // runs during ordinary control flow when try/catch exits
  System.out.println("Runs when ordinary control flow exits try/catch");
}

// Order matters - child to parent in catch blocks
- finally details:
    - Runs during ordinary control flow, including a return or exception from try. It is not guaranteed after process termination, JVM failure, or a non-terminating try block.
    - Use try-with-resources for AutoCloseable resources; closing failures are preserved as suppressed exceptions when another failure is already propagating.
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
- throws - declares exceptions that may propagate; checked exceptions must be caught or declared by callers.
void readFile() throws IOException {
  try (FileReader reader = new FileReader("file.txt")) {
    // read from reader
  }
}

void readFile2() throws IOException, SQLException { // multiple
}
- Difference: throw is inside method body to throw object, throws is in method signature to declare.

## 5.4 Exception Flow Examples

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

## 5.5 Custom Exceptions

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

## 6.1 List

| Feature | ArrayList | LinkedList | Vector |
| --- | --- | --- | --- |
| Internal | Dynamic array Object[] | Doubly Linked List (Node prev, data, next) | Dynamic array - legacy |
| Get by index | O(1) - fast | O(n) - slow, must traverse | O(1) |
| Insert/delete middle | O(n) - shift elements | O(1) after locating node; locating by index is O(n) | O(n) |
| Thread safe? | No | No | Synchronized methods; compound workflows still need coordination |
| When to use | General-purpose list | Deque operations or edits at a known node | Legacy APIs |

List<String> list = new ArrayList<>();
list.add("Stitch"); list.add(0,"Pay"); // add at index
list.get(0); list.set(0,"X"); list.remove(0);
Collections.sort(list);

// LinkedList can work as both List and Deque
LinkedList<String> ll = new LinkedList<>();
ll.addFirst("A"); ll.addLast("Z");

## 6.2 Set

| HashSet | LinkedHashSet | TreeSet |
| --- | --- | --- |
| HashMap internally | LinkedHashMap internally | TreeMap (Red-Black Tree) |
| No order | Insertion order maintained | Sorted order - natural sorting |
| Expected O(1) add/search | Expected O(1) | O(log n) |
| Allows 1 null | Allows 1 null | Natural ordering rejects null; a custom comparator may define null ordering |
| Use when fast check | When you need order + uniqueness | When sorted set needed |

Set<String> set = new HashSet<>();
set.add("A"); set.add("A"); // second ignored -> size 1

Set<Integer> sorted = new TreeSet<>(); // sorted: 1,2,10
sorted.add(10); sorted.add(2); sorted.add(1);

LinkedHashSet maintains insertion order

## 6.3 Map

| HashMap | LinkedHashMap | TreeMap | ConcurrentHashMap |
| --- | --- | --- | --- |
| Unspecified iteration order | Insertion or access order | Sorted by key comparator | No guaranteed order |
| One null key and null values | One null key and null values | Null-key support depends on comparator | Rejects null keys and values |
| Not thread safe | Not thread safe | Not thread safe | Supports concurrent operations without one global map lock |
| Expected O(1) | Expected O(1) | O(log n) | Expected O(1) |

Map<Integer, String> map = new HashMap<>();
map.put(1, "Ali"); map.put(1, "Khan"); // overwrites - key 1 now Khan
map.get(1); // Khan
map.containsKey(1); map.containsValue("Ali");
for(Map.Entry<Integer,String> e : map.entrySet()){ e.getKey(); e.getValue(); }

Map<Integer,String> lmap = new LinkedHashMap<>(); // order same as you inserted

Map<Integer,String> tmap = new TreeMap<>(); // keys sorted 1,2,3

// HashMap uses hashCode() to select a bucket and equals() to identify a matching key.
// Java 8+ may treeify a heavily collided bucket when capacity thresholds are also met.
// Custom key types need consistent equals() and hashCode() implementations.

Map<String,String> cmap = new ConcurrentHashMap<>(); // for thread safe without Hashtable
- HashMap vs HashTable: Hashtable legacy, synchronized slow, no null. HashMap new, fast, allows null.

## 6.4 Queue, Deque, and Stack

// Queue - FIFO
Queue<Integer> q = new ArrayDeque<>(); // FIFO queue
q.offer(10); q.offer(20); // add
q.poll(); // remove head -> 10
q.peek(); // see head -> 20

Queue<Integer> pq = new PriorityQueue<>(); // priority queue - smallest first by natural order
pq.offer(10); pq.offer(2); pq.peek(); // 2

// Deque - double-ended queue; also preferred over legacy Stack for LIFO use
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

## 6.5 Comparable and Comparator

class Employee implements Comparable<Employee> { // Comparable - natural sorting, 1 way
  int id; String name;
  @Override
  public int compareTo(Employee other) {
    return Integer.compare(this.id, other.id); // avoids subtraction overflow
    // return this.name.compareTo(other.name); // by name
  }
}
Collections.sort(empList); // uses compareTo

// Comparator - multiple ways, external logic
class NameComparator implements Comparator<Employee> {
  public int compare(Employee e1, Employee e2){ return e1.name.compareTo(e2.name); }
}
class SalaryComparator implements Comparator<Employee> {
  public int compare(Employee e1, Employee e2){ return Integer.compare(e1.salary, e2.salary); }
}
Collections.sort(empList, new NameComparator());
// Java 8 lambda
empList.sort(Comparator.comparingInt(e -> e.id));
empList.sort(Comparator.comparing(e -> e.name));
| Comparable | Comparator |
| --- | --- |
| In same class - implements Comparable | Separate class |
| compareTo() 1 param | compare() 2 params |
| Natural ordering - single logic | Multiple logics |
| java.lang package | java.util package |

## 6.6 Iteration and Concurrent Modification

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
- Fail-fast iterators may throw `ConcurrentModificationException` after unsupported structural modification. This is best-effort bug detection, not synchronization or a guarantee that every concurrent change will be detected.
for(String s: list){ list.add("X"); } // throws exception
- Some iterators are snapshot-based (`CopyOnWriteArrayList`); others are weakly consistent (`ConcurrentHashMap`). Neither behavior should be generalized as "fail-safe."
ConcurrentHashMap<Integer,String> cmap = new ConcurrentHashMap<>();
// iterator won't throw even if you modify
Interview Must-Know:
1. ArrayList capacity growth is implementation-specific; current OpenJDK implementations typically allocate on first insertion and grow geometrically.
2. HashMap internal working - hashcode, equals, bucket, treeify.
3. When to use which Map/List - they give scenario.
4. How to make ArrayList thread safe? Collections.synchronizedList(list) or CopyOnWriteArrayList

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

## 7.1 Generic Classes

`T` is a compile-time type parameter. Parameterizing a class lets the compiler check values at the boundary instead of requiring callers to cast:

```java
final class Box<T> {
  private T value;

  Box(T value) { this.value = value; }
  T getValue() { return value; }
  void setValue(T value) { this.value = value; }
}

Box<String> message = new Box<>("Hello");
String value = message.getValue(); // no cast; type is checked by the compiler
```

A class can declare multiple independent type parameters, as in `Pair<K, V>`. Common conventions are `T` (type), `E` (element), `K` (key), `V` (value), and `N` (number).

## 7.2 Generic Methods

class Util {
  // Method with its own generic type <T>
  public static <T> void printArray(T[] values) {
    for (T value : values) System.out.println(value);
  }
  public static <T> Optional<T> first(T[] values) {
    return values.length == 0 ? Optional.empty() : Optional.ofNullable(values[0]);
  }
}

Integer[] intArr = {1,2,3};
String[] strArr = {"A","B"};
Util.<Integer>printArray(intArr); // explicit type witness
Util.printArray(strArr);           // compiler infers T as String

## 7.3 Bounded Types and Wildcards

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

An unbounded wildcard means a list of one unknown element type. Reading yields `Object`; only `null` can be added through `List<?>`.

A lower-bounded wildcard accepts a consumer of a type or one of its supertypes:

```java
static void addDefaults(List<? super Integer> destination) {
  destination.add(10);
  destination.add(20);
}

List<Number> numbers = new ArrayList<>();
addDefaults(numbers); // also accepts List<Integer> and List<Object>
```

**PECS** is a useful API-design heuristic: use `extends` when a parameter produces values for you to read, and `super` when it consumes values you provide. A wildcard is not write-only: a `List<? super Integer>` can also be read, but the only statically safe result type is `Object`.

## 7.4 Type Erasure

- Generic type arguments are erased from ordinary runtime object types; the erased form uses the first bound or `Object`.
- Class files retain some generic-signature metadata for reflection, but runtime checks cannot distinguish ordinary `List<String>` from `List<Integer>`.
- Because of erasure:
// These are same after erasure - cannot overload
void method(List<String> list){}
void method(List<Integer> list){} // COMPILE ERROR - same after erasure

// Cannot create an array of a non-reifiable parameterized type:
// T[] values = new T[10]; // ERROR
// Runtime checks cannot test a specific type argument:
// if (value instanceof ArrayList<String>) { } // ERROR
if (value instanceof ArrayList<?> list) { // allowed: unbounded wildcard is reifiable
  System.out.println(list.size());
}

## 7.5 Generic Interfaces

interface Repository<T, ID> {
  void save(T entity);
  Optional<T> findById(ID id);
}
final class EmployeeRepository implements Repository<Employee, Long> {
  public void save(Employee entity) { /* persist entity */ }
  public Optional<Employee> findById(Long id) {
    return Optional.empty(); // replace with the actual lookup result
  }
}

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

## 8.1 Threads, Runnable, and Lifecycle

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
`Thread.State` has six values; there is no separate `RUNNING` state in the API. A thread executing or eligible for CPU time is reported as `RUNNABLE`.

1. `NEW`: created but not started.
2. `RUNNABLE`: executing or ready to execute.
3. `BLOCKED`: waiting to acquire an intrinsic monitor.
4. `WAITING`: waiting indefinitely for another action, such as `join()` or `Object.wait()`.
5. `TIMED_WAITING`: waiting with a deadline, such as `sleep()` or timed `join()`.
6. `TERMINATED`: `run()` has completed.

Methods:
t.start(); // start
Thread.sleep(1000); // pauses current thread; interruption must be handled
t.join(); // wait till t finishes - main waits
Thread.yield(); // hint to scheduler - give chance to other thread
t.setPriority(1-10); // priority is only a scheduler hint; do not rely on it for correctness
t.setDaemon(true); // JVM may exit when only daemon threads remain; their work is not guaranteed to finish

## 8.2 `synchronized` and `volatile`

- A race condition occurs when correctness depends on unsynchronized timing between operations on shared state.
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
- `volatile` establishes visibility and ordering for reads/writes of that field; it does not mean every access goes directly to main memory.
boolean flag = false; // without a happens-before edge, another thread is not guaranteed to observe an update

volatile boolean flag = false; // a write happens-before a later read that observes it
// BUT volatile does NOT make count++ atomic - for atomic use AtomicInteger or synchronized
| synchronized | volatile |
| --- | --- |
| Locks - mutual exclusion | No lock - only visibility |
| Makes operation atomic | Does NOT make atomic |
| Blocks threads | Doesn't block |
| Use for compound actions | Use for flags - volatile boolean stop |

## 8.3 ExecutorService, Future, and CompletableFuture

// ExecutorService - thread pool - reuse threads

// 1. Single thread
ExecutorService ex = Executors.newSingleThreadExecutor();

// Use a bounded queue and an explicit rejection policy rather than allowing
// overload to accumulate in memory.
ExecutorService ex = new ThreadPoolExecutor(
    5, 5, 0L, TimeUnit.MILLISECONDS,
    new ArrayBlockingQueue<>(100),
    new ThreadPoolExecutor.AbortPolicy());

// 3. Cached pool - creates new if needed, reuse idle
ExecutorService ex = Executors.newCachedThreadPool();

// 4. Scheduled - for recurring or delayed in-process tasks, not a durable cron service
ScheduledExecutorService sched = Executors.newScheduledThreadPool(2);
sched.schedule(() -> System.out.println("After 5 sec"), 5, TimeUnit.SECONDS);
sched.scheduleAtFixedRate(() -> {}, 0, 1, TimeUnit.SECONDS);

ex.submit(() -> System.out.println("Task")); // submit Runnable
Future<Integer> f = ex.submit(() -> 10+20); // submit Callable
Integer result = f.get(); // blocks; may throw InterruptedException or ExecutionException
f.isDone(); f.cancel(true);

ex.shutdown(); // stop accepting new, completes running tasks
// shutdownNow() requests interruption and returns tasks that never started.
if (!ex.awaitTermination(5, TimeUnit.SECONDS)) {
  ex.shutdownNow();
}
- Future problem: get() blocks - cannot chain.

- CompletableFuture - Java 8 - compose stages without blocking between each stage
CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> {
  // uses the common ForkJoinPool unless an Executor is supplied
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

// Observe failure without converting it into a plausible success value.
cf.whenComplete((value, error) -> {
  if (error != null) logger.log(Level.SEVERE, "Async operation failed", error);
});

// All of, any of
CompletableFuture.allOf(cf1, cf2).join(); // completes when all finish; does not collect their values
CompletableFuture.anyOf(cf1, cf2).join(); // completes with the first completed result

Non-async continuations such as `thenApply` may run on the thread that completes the prior stage. Use an `*Async` method with an explicit executor when execution placement matters.

## 8.4 Concurrent Utilities and Locks

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

// Timed acquisition can avoid waiting forever; it does not by itself prevent deadlocks.
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
atomicCount.incrementAndGet(); // atomic read-modify-write; benchmark against locking for the actual workload

## 8.5 Race Conditions, Deadlock, and Liveness

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
  try {
    tl.set(10);
    tl.get();
  } finally {
    tl.remove(); // important when reusing pooled threads
  }
5. Use concurrent collections

Interview Must for Stitch Guindy:
1.  start() vs run()?
2.  Why wait(), notify() in Object not Thread? Because lock is on object.
3.  wait() vs sleep()? wait releases lock, sleep doesn't.
4.  How CompletableFuture works internally? ForkJoinPool.

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

# 9. Java 8+ Features

## 9.1 Functional Interfaces, Lambdas, and Method References

- A functional interface has one abstract method (excluding public methods corresponding to `Object`). It may also have default and static methods; `@FunctionalInterface` asks the compiler to verify the contract.
@FunctionalInterface
interface MyFunc { int add(int a, int b); } // 1 abstract method

Common `java.util.function` interfaces:

- `Predicate<T>` tests a value and returns `boolean`.
- `Function<T, R>` transforms a `T` into an `R`.
- `Consumer<T>` accepts a value and returns no result.
- `Supplier<T>` provides a value without an input.
- `BiFunction`, `BiPredicate`, `UnaryOperator`, and `BinaryOperator` cover common two-input or same-type cases.

A lambda provides an implementation of the target functional interface. The target type supplies the parameter and return types:
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

Captured local variables must be final or effectively final. Keep lambdas short and side-effect-light so behavior remains clear when APIs defer or parallelize execution.

Method references are a concise form when an existing method already matches the functional interface's signature:
// Types:
// 1. Static method
Function<String,Integer> f = s -> Integer.parseInt(s);
Function<String,Integer> f2 = Integer::parseInt; // same

// 2. Instance method of object
Consumer<String> c1 = s -> System.out.println(s);
Consumer<String> c2 = System.out::println;

// 3. Instance method of arbitrary object
Function<String,String> f3 = s -> s.toUpperCase();
Function<String,String> f4 = String::toUpperCase; // unbound instance method; input supplies the receiver

// 4. Constructor
Supplier<List<String>> s1 = () -> new ArrayList<>();
Supplier<List<String>> s2 = ArrayList::new;

## 9.2 Stream API

Stream = a one-use pipeline over a source; it does not store elements. Intermediate operations are lazy, and a terminal operation starts traversal. Parallelism is optional and does not automatically make a pipeline faster.
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
  // [["A", "B"], ["C", "D"]] -> ["A", "B", "C", "D"]
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

// Parallel streams commonly use the shared ForkJoinPool.
list.parallelStream().filter(...).collect(toList());
// Measure first; side effects, ordering requirements, small inputs, and blocking work can make parallelism slower or incorrect.
- `Stream.toList()` returns an unmodifiable list; `Collectors.toList()` does not promise a particular implementation or mutability. Choose an explicit collector such as `toCollection(ArrayList::new)` when a mutable result is required.
- Avoid modifying a stream's source or shared mutable state from intermediate operations. Prefer stateless transformations and collectors.

## 9.3 Optional

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
// orElse evaluates its argument even when opt has a value; use orElseGet for expensive fallback work.

opt.filter(v -> v.length()>3).map(String::toUpperCase).orElse("NA");

// Real use in service
public Optional<Employee> findById(int id){ return Optional.ofNullable(db.get(id)); }

## 9.4 Default and Static Interface Methods

Default methods can evolve an interface without requiring every existing implementation to add a method, but adding one may still create conflicts with inherited methods or another interface's default.
interface Payment {
  void pay(); // abstract
  
  default void log(){ System.out.println("Logging"); } // default - can be overridden
  static void info(){ System.out.println("Payment gateway"); } // static - cannot override, call via InterfaceName

  private void helper(){} // Java 9 - private method in interface for code reuse inside default methods
}

Payment.info(); // call static
- Diamond problem with default methods? If class implements 2 interfaces with same default method, must override.

## 9.5 Date and Time API

// Old Date is mutable, not thread safe - don't use
Date d = new Date(); Calendar c = Calendar.getInstance();

// New API - immutable, thread safe
LocalDate date = LocalDate.now(); // 2026-10-03 - only date
LocalTime time = LocalTime.now(); // 10:30:15 - only time
LocalDateTime dt = LocalDateTime.now(); // both - most used
ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
// LocalDateTime has no time zone or offset; use Instant or ZonedDateTime for unambiguous moments.
// Inject a Clock so "now" can be fixed in tests.
Clock clock = Clock.fixed(Instant.parse("2026-01-01T00:00:00Z"), ZoneOffset.UTC);
LocalDate testDate = LocalDate.now(clock);

dt.plusDays(5).minusMonths(1);
dt.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

LocalDate dob = LocalDate.of(2000,1,15);
Period age = Period.between(dob, LocalDate.now(clock)); // deterministic in tests

Duration dur = Duration.between(time1, time2); // time diff

Instant instant = Instant.now(); // timestamp for machine - UTC

## 9.6 Records, Sealed Classes, and Pattern Matching

- Record - transparent data carrier with final component references; auto-generates a canonical constructor, accessors, equals, hashCode, and toString
// Old - 50 lines
class Employee { private final int id; private final String name; constructor, getters, equals... }

// New - 1 line
record Employee(int id, String name) {} // immutable, final
Employee e = new Employee(1, "Ali");
e.id(); e.name(); // component accessors, not JavaBean getId()/getName() methods
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

### Common Interview Exercises

// Find duplicate numbers using stream
list.stream().collect(groupingBy(Function.identity(), counting()))
    .entrySet().stream().filter(e->e.getValue()>1).map(Map.Entry::getKey).collect(toList())

// Second distinct-highest salary; duplicates otherwise affect the result.
employees.stream().map(Employee::getSalary).distinct()
    .sorted(Comparator.reverseOrder()).skip(1).findFirst()

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

- `Instant` is a point on the UTC timeline.
- `LocalDateTime` has no zone and is ambiguous during daylight-saving transitions.
- `ZonedDateTime` combines local date/time with time-zone rules.
- `OffsetDateTime` has a fixed offset but not complete regional rules.
- Persist timestamps as `Instant` or an offset-aware database type.
- Inject `Clock` for deterministic tests.

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

Java Code (.java) -> javac -> Bytecode (.class) -> JVM -> OS -> Hardware

### JVM Components

1. ClassLoader Subsystem
2. Runtime Memory Areas (Heap, Stack, Method Area, PC Register, Native Stack)
3. Execution Engine (Interpreter, JIT Compiler, GC)

## 10.2 Class Loading

- The standard delegation chain is bootstrap, platform, then application class loader; custom loaders can define additional delegation policies.
- The bootstrap loader is implemented by the JVM, so it is not necessarily a C++ object visible to Java code. Java 9 replaced the extension loader with the platform loader.
- Loading Steps:

1. Loading creates the runtime representation of a class.
2. Linking verifies class-file structure and bytecode, prepares static fields with default values, and resolves symbolic references as needed.
3. Initialization runs class initialization code and assigns explicit static initializers in textual order.
class A {
  static int x = 10;
  static { System.out.println("Static block"); } // runs in initialization
}
Class.forName("A"); // initializes by default; an overload can request loading without initialization
- Parent delegation helps preserve platform type identity, but custom class loaders can define classes with the same binary name. A class's identity includes its defining loader.

## 10.3 Runtime Memory Areas

- Each thread has its own JVM stack and program counter; native calls may use a native-method stack.
- The heap is shared and holds objects and arrays. HotSpot collectors may divide it into regions or generations, but the exact layout is collector-specific.
- HotSpot stores class metadata in native-memory Metaspace (PermGen was removed in Java 8). The string intern pool is on the heap; do not treat static fields as a separate Metaspace store.
- The JVM specification describes a method area conceptually; Metaspace is a HotSpot implementation detail.

## 10.4 Garbage Collection

- What is GC? Automatic memory cleanup - deletes unreachable objects. You cannot force GC - System.gc() is only hint.

- An object is eligible for collection when it is no longer reachable from any GC root; unreachable cycles can also be collected.
Employee e = new Employee(); e = null; // now object eligible for GC
- Collectors reclaim objects unreachable from GC roots using collector-specific algorithms. Some collections pause application threads; others do substantial work concurrently.
- Generational collection is common but not universal. Promotion and collection triggers vary by collector and JDK; age thresholds are tuning details, not fixed language guarantees.
- A full-GC event does not necessarily mean Metaspace is collected or every application thread pauses for the same duration.

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

- Contract from Object class - MUST follow, else collections break.
class Employee {
  int id; String name;
  
  // Default from Object class - checks == - reference equality - WRONG for content
}

### Contract Rules

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

### Interview Questions

1.  Why String Pool possible? Because String immutable + hashCode cached.
2.  What is GC Root? Stack refs, static, JNI.
3.  Can you call GC? System.gc() hint, not guarantee.
4.  OutOfMemory vs StackOverflow?
5.  What happens if hashCode not overridden?

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

## 11.1 Serialization and Cloning

- Serialization: Converting object to byte stream to save to file / send over network. Deserialization reverse.
// Must implement Serializable - marker interface - no methods
class Employee implements Serializable {
  private static final long serialVersionUID = 1L; // compatibility identifier; class changes do not always imply incompatibility
  int id; String name;
  String getName() { return name; }
  transient String password; // omitted by default serialization; transient does not erase the in-memory value
  static String company = "Stitch"; // static not serialized - belongs to class not object
}

// Serialization is suitable only for trusted, compatible data; prefer an explicit
// schema format for files or data crossing service boundaries.
Employee e = new Employee();
e.id = 1;
e.name = "Ali";
try (ObjectOutputStream out =
         new ObjectOutputStream(new FileOutputStream("emp.ser"))) {
  out.writeObject(e);
}

// Deserialization bypasses constructors of serializable classes;
// the no-argument constructor of the first non-serializable superclass runs.
try (ObjectInputStream in =
         new ObjectInputStream(new FileInputStream("emp.ser"))) {
  // ObjectInputFilter is available since Java 9. This sample class is in the
  // default package; use its fully qualified name when it belongs to a package.
  in.setObjectInputFilter(ObjectInputFilter.Config.createFilter(
      "maxdepth=20;maxrefs=10000;maxbytes=1000000;Employee;!*"));
  Employee e2 = (Employee) in.readObject();
}

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

// A deep copy must copy every mutable reachable object that should be independent.
// Prefer explicit copy constructors; this sketch assumes addr is non-null and cloneable.
@Override protected Object clone() throws CloneNotSupportedException {
  Employee cloned = (Employee) super.clone();
  cloned.addr = (Address) this.addr.clone(); // deep - new Address object
  return cloned;
}
Use copy constructor instead of clone - better practice.

## 11.2 Reflection and Annotations

- Reflection inspects or invokes runtime types; frameworks use it for discovery and integration.
Class<?> clazz = Employee.class; // or Class.forName("com.stitch.Employee") or e.getClass()

clazz.getName(); clazz.getDeclaredFields(); clazz.getDeclaredMethods(); clazz.getConstructors();

// Reflection access remains subject to module boundaries and access checks.
Constructor<?> cons = clazz.getDeclaredConstructor();
if (!cons.trySetAccessible()) {
  throw new IllegalAccessException("Constructor is not accessible");
}
Object obj = cons.newInstance();

// Call method
Method m = clazz.getDeclaredMethod("getName");
Object name = m.invoke(obj); // unwrap InvocationTargetException to inspect target failures

// getDeclared* includes non-public members declared on this class, not inherited members.
// Prefer ordinary APIs; reflection moves access/type failures to runtime.
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
if (ann != null) {
  ann.value(); // Payment
}

## 11.3 I/O, NIO, and File Handling

| IO (`java.io`) | NIO (`java.nio`, introduced in Java 1.4) |
| --- | --- |
| Includes blocking streams | Adds buffers/channels; non-blocking channels and selectors are available for supported channel types |
| Stream-oriented byte/character APIs | Buffer- and channel-oriented APIs |
| No selector in classic stream APIs | Selectors can multiplex non-blocking channels |
| Still useful for many tasks | Choose based on workload; NIO is not automatically faster |

// Text I/O with an explicit charset and deterministic resource cleanup.
// This example uses Java 11 APIs (Path.of, Files.writeString); transferTo is Java 9+.
Path path = Path.of("a.txt");
try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
  String line;
  while ((line = reader.readLine()) != null) {
    System.out.println(line);
  }
}

// Streams handle bytes. Buffer large copies instead of loading huge files into memory.
try (InputStream input = Files.newInputStream(Path.of("a.jpg"));
     OutputStream output = Files.newOutputStream(Path.of("b.jpg"))) {
  input.transferTo(output);
}

// NIO file APIs
Files.writeString(path, "hello", StandardCharsets.UTF_8);
// Files.exists can return false when existence cannot be determined; perform the
// operation and handle IOException when correctness depends on the result.

try (FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {
  ByteBuffer buffer = ByteBuffer.allocate(1024);
  while (channel.read(buffer) != -1) {
    buffer.flip(); // switch from writing into the buffer to reading from it
    while (buffer.hasRemaining()) {
      consume(buffer.get());
    }
    buffer.clear();
  }
}

## 11.4 JDBC

Use a `DataSource` (usually managed by the application or a connection pool) and try-with-resources so connections, statements, and result sets close on every exit path:

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

Modern JDBC drivers normally load through service-provider discovery; explicit `Class.forName` is only needed for legacy driver setups. Parameter placeholders bind values, not table or column names; validate dynamic identifiers against an allowlist.
- Statement vs PreparedStatement vs CallableStatement:
    - `Statement`: appropriate for fixed SQL without user-provided values; concatenating untrusted input can enable SQL injection.
    - `PreparedStatement`: binds values separately from SQL syntax and is usually the right choice for input values or repeated execution. Server-side preparation and performance depend on the driver/database.
    - `CallableStatement`: invokes stored procedures, for example `{call proc(?, ?)}`.

## 11.5 Serialization Safety and Versioning

- `serialVersionUID` controls compatibility checks but does not guarantee semantic compatibility.
- Adding fields is often compatible because missing fields receive defaults; changing field types or hierarchy can break compatibility.
- Validate invariants in `readObject`; constructors are not called for normal serializable classes.
- Prefer a stable schema format for long-lived storage and inter-service communication.
- Never deserialize untrusted native Java streams without a strict object filter.

## 11.6 NIO Buffers and Channels

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

## 11.7 File-System Correctness

- Specify charsets explicitly, usually `StandardCharsets.UTF_8`.
- Use atomic move where supported for replace-style writes.
- Do not assume a single `read` or `write` processes the entire buffer.
- Close directory streams and file channels.
- Decide how symbolic links should be handled for security-sensitive operations.
- Use streaming APIs for large files rather than `readAllBytes`.

## 11.8 JDBC Transactions and Pooling

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
    try {
      connection.rollback();
    } catch (SQLException rollbackFailure) {
      e.addSuppressed(rollbackFailure);
    }
    throw e;
  }
}
```

## 11.9 Reflection and Method Handles

Reflection is flexible but shifts errors to runtime and can conflict with module encapsulation. Cache validated metadata when repeatedly used.

`MethodHandle` and `VarHandle` provide typed, JVM-supported dynamic access:

- `MethodHandle`: invoke methods, constructors, and fields through a typed signature.
- `VarHandle`: access fields or array elements with defined memory-ordering modes.

Use ordinary calls when types are known statically.

## 11.10 ServiceLoader

`ServiceLoader` supports provider discovery without hard-coding implementations:

```java
ServiceLoader<PaymentProvider> providers =
    ServiceLoader.load(PaymentProvider.class);

for (PaymentProvider provider : providers) {
  provider.initialize();
}
```

Classpath providers use `META-INF/services/<interface-name>`; named modules use `uses` and `provides`.

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

## 11.12 Asynchronous and Non-Blocking I/O

- `AsynchronousFileChannel` completes file operations through futures or callbacks.
- `Selector` multiplexes many non-blocking channels on one thread.
- A channel's readiness means an operation can make progress, not necessarily finish completely.
- Network protocols still require framing, partial-read handling, backpressure, and timeout logic.

Frameworks such as Netty encapsulate much of this complexity. Do not build a custom event loop unless requirements justify it.

## 11.13 JDBC Isolation Levels

Standard JDBC levels include:

- `READ_UNCOMMITTED`: may allow dirty reads.
- `READ_COMMITTED`: prevents dirty reads.
- `REPEATABLE_READ`: also protects repeated reads, with database-specific phantom behavior.
- `SERIALIZABLE`: strongest isolation, lowest concurrency.

Databases implement multiversioning and locking differently. Verify actual semantics, deadlock behavior, and retry requirements for the chosen database.

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

## 11.15 Annotation Processing

Annotation processors run during compilation and can validate code or generate source/resources. Examples include mapper generators and immutable-value tools.

- Register processors through the service-provider mechanism or build configuration.
- Generated source should be deterministic.
- Separate annotation-processor dependencies from runtime dependencies.
- Incremental builds depend on processors accurately declaring their behavior.
- Generated code should remain inspectable and testable.

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

# 12. Java Platform Module System

## 12.1 Named, Automatic, and Unnamed Modules

- A **named module** contains `module-info.class`.
- An **automatic module** is a non-modular JAR placed on the module path; its name comes from `Automatic-Module-Name` or the JAR file.
- The **unnamed module** contains classpath code and reads all observable modules.

Automatic modules ease migration but expose all packages and have less reliable naming unless the manifest defines it.

## 12.2 Strong Encapsulation

`exports` allows normal compiled access to public types. `opens` allows deep reflection. They solve different problems:

```java
module com.example.orders {
  exports com.example.orders.api;
  opens com.example.orders.dto to com.fasterxml.jackson.databind;
}
```

Qualified exports or opens grant access only to listed modules. Avoid opening every package merely to silence reflective-access failures.

`requires` declares a readability dependency; it does not export the requiring module's packages. Consumers also need the target package exported, and the referenced type/member must be accessible:

```java
module com.example.web {
  requires com.example.orders;
}
```

Use `requires transitive` only when a dependency's types are part of your module's public API and downstream modules need readability to use them.

## 12.3 Compilation and Execution

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

Classpath and module-path launch modes have different resolution and encapsulation rules. Test the exact packaged artifact and launch command; a successful IDE classpath run does not prove the module graph is valid.

## 12.4 Migration Strategy

1. Remove dependencies on JDK internals.
2. Give published JARs stable automatic module names.
3. Resolve split packages and cyclic dependencies.
4. Add descriptors to libraries from the leaves upward.
5. Open only packages that frameworks need for reflection.
6. Test both modular packaging and runtime launch commands.

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

## 13.1 `var` for Local Variables (Java 10)

```java
var names = new ArrayList<String>(); // inferred as ArrayList<String>
var total = calculateTotal();        // inferred from return type
```

`var` is not dynamic typing. The compiler still assigns one static type. It works only for local variables with an initializer, enhanced-for variables, and lambda parameters. Avoid it when the inferred type is unclear.

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

## 13.4 Sealed Types (Final in Java 17)

```java
sealed interface Result permits Success, Failure {}
record Success(String value) implements Result {}
record Failure(String message) implements Result {}
```

Permitted implementations must be `final`, `sealed`, or `non-sealed`. Sealed hierarchies work well with exhaustive pattern matching.

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

Switch expressions must produce a value for every possible path. Use `yield` from a multi-statement case block.

## 13.9 Feature Lifecycle and Compatibility

Java features may be permanent, preview, incubating, or experimental:

- Preview language/API features require `--enable-preview` at compile and run time and may change between releases.
- Incubator modules are non-final APIs that must be added explicitly.
- Experimental JVM features may require flags and are not compatibility commitments.

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