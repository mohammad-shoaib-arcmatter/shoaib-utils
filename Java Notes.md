# Java Notes

 A practical reference for Core Java, modern language features, JVM internals, testing, build tools, and production best practices.

## Contents

 1. Fundamentals
 2. Object-Oriented Programming
 3. Keywords and Essentials
 4. Memory and Strings
 5. Exception Handling
 6. Collections Framework
 7. Generics
 8. Multithreading and Concurrency
 9. Java 8+ Features
 10. JVM Internals
 11. Advanced Core Java
 12. SOLID Principles and Design Patterns
 13. Maven and Gradle
 14. Testing with JUnit and Mockito
 15. Java Platform Module System
 16. Modern Java Features
 17. Production Java Best Practices
 18. Security Essentials
 19. Performance, Monitoring, and Troubleshooting
 20. Quick Revision Checklist

## 1. Fundamentals
JVM - Java Virtual Machine
- The engine that actually runs your code
- Reads .class bytecode and converts it to machine code for your OS
- Handles memory, garbage collection, security
- You never download JVM alone - it comes inside JRE/JDK
- One JVM per platform: Windows JVM, Linux JVM, etc. That's why Java is "write once, run anywhere" - same bytecode, different JVM translates it.

JRE - Java Runtime Environment
- JVM + libraries needed to _run_ Java apps
- Contains: JVM + core class libraries (java.lang, java.util etc) + other supporting files
- If you only want to RUN a Java app (like Minecraft), you need JRE
- Cannot develop - no compiler inside

JDK - Java Development Kit
- JRE + tools needed to _develop_ Java apps
- Contains: JRE (so includes JVM) + javac compiler + debugger + javadoc + other dev tools
- If you want to WRITE and compile Java code, you need JDK

2. Variables & Data Types

- Variable: Name for memory location. Must declare type before use.
  int age; // declaration
  age = 25; // initialization
  final int MAX = 100; // constant, cannot change
- Primitive - 8 types:

Type 	 Size 	 Range 	 Default
byte 	1 byte 	 -128 to 127 	0
short 	2	 -32k to 32k 	0
int 	4	 ~ -2B to 2B 	0
long 	8	 huge 	0L - needs L suffix
float 	4	 decimal 	0.0f - needs f
double 	8	 double decimal 	0.0d
char 	2	 unicode 	\u0000 - single quotes 'A'
boolean 	 1 bit 	 true/false 	false

- Reference: Stores address pointing to heap.
  String name = "Stitch"; // String pool
  int[] arr = new int[5]; // array is object in Java
  // Reference comparison: == checks address,.equals() checks content
- Memory: Primitives on stack (fast), objects on heap (GC cleans). Local variables no default - you must init.

3. Operators & Type Casting

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

4. Control Flow

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

5. Input/Output - Detailed

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


### 1.5 Compilation, Execution, and Classpath

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

### 1.6 Primitive Details and Numeric Accuracy

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

### 1.7 Scope, Lifetime, and Parameter Passing

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

### 1.8 Arrays

- Arrays are fixed-size objects with zero-based indexes.
- Array elements receive defaults; a local array reference does not.
- Arrays are covariant, so `Number[] values = new Integer[2]` compiles but can throw `ArrayStoreException`.
- Common utilities include `Arrays.copyOf`, `sort`, `binarySearch`, `equals`, and `deepEquals`.
- For resizable sequences, prefer `ArrayList`.

## 2. Object-Oriented Programming
- Class, Object, Constructor
- Encapsulation + getters/setters
- Inheritance (extends, super)
- Polymorphism - compile-time (overloading) vs runtime (overriding)
- Abstraction - abstract class vs interface
- Access Modifiers - private, default, protected, public

OOP is a major area in Java interviews:

1. Class, Object, Constructor

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
2. Encapsulation + Getters/Setters

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

3. Inheritance (extends, super)

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

4. Polymorphism - Many forms

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

5. Abstraction - abstract class vs interface

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
Abstract Class | Interface
extends | implements
Can have constructor, variables | No constructor, only constants
Single inheritance | Multiple interfaces can be implemented
Use when IS-A + some common code | Use when capability - can do
Java 8+: Interface can have default and static methods to avoid breaking old code.

6. Access Modifiers

Controls visibility:
Modifier | Same Class | Same Package | Child (diff pkg) | World
private | YES | NO | NO | NO
default (no keyword) | YES | YES | NO | NO
protected | YES | YES | YES | NO
public | YES | YES | YES | YES
public class A {
  private int a = 1; // only inside A
  int b = 2; // default - package only
  protected int c = 3; // package + child outside pkg
  public int d = 4; // anywhere
}
Interview notes:
- Why protected needed? To allow child outside package to access parent.
- Encapsulation uses private + public getters/setters.

### 2.7 Composition, Association, and Aggregation

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

### 2.8 Initialization Order

Object initialization follows this general order:

1. Parent class initialization, then child class initialization, once per class.
2. Memory allocation with instance fields set to default values.
3. Parent instance field initializers and initializer blocks.
4. Parent constructor.
5. Child instance field initializers and initializer blocks.
6. Child constructor.

Calling an overridable method from a constructor is dangerous because child fields may not yet be initialized.

### 2.9 Method Dispatch and Covariant Returns

- Instance methods are dynamically dispatched from the runtime object type.
- Fields, static methods, and private methods are resolved from the reference or declaring type and are not polymorphic.
- An overriding method may return a subtype of the parent's return type.
- It cannot throw broader checked exceptions than the overridden method.
- It may widen access, such as `protected` to `public`, but cannot narrow access.

### 2.10 Immutability

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

## 3. Keywords and Essentials
- this, super, final, static
- Packages, import
- Wrapper Classes, Autoboxing

3. Keywords & Essentials - Detailed

A. this keyword
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

B. super keyword
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

C. final keyword - 3 uses

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
D. static keyword - Belongs to class, not object

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

E. Packages, import

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

F. Wrapper Classes, Autoboxing

Primitives have object version - needed for Collections (Collections cannot store primitive).
Primitive | Wrapper (in java.lang)
int | Integer
char | Character
byte, short, long, float, double, boolean | Same with capital - Byte etc
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

### 3.7 Important Modifiers

- `abstract`: declares an incomplete class or method.
- `synchronized`: acquires an intrinsic monitor for mutual exclusion and visibility.
- `volatile`: provides visibility and ordering for one field, not compound-operation atomicity.
- `transient`: excludes an instance field from default Java serialization.
- `native`: declares a method implemented outside Java through JNI.
- `strictfp`: historically enforced strict floating-point behavior; since Java 17, floating-point operations are always strict.

### 3.8 `final` vs Immutability

`final` prevents reassignment; it does not make a referenced object immutable:

```java
final List<String> names = new ArrayList<>();
names.add("Ali");              // allowed
// names = new ArrayList<>();  // not allowed
```

Correctly constructed `final` fields also have safe-publication guarantees, provided `this` does not escape during construction.

### 3.9 Static Initialization

- A class initializes on first active use, such as construction, static method invocation, or access to a non-constant static field.
- Compile-time constants may be inlined and may not trigger initialization.
- If initialization throws, the first access receives `ExceptionInInitializerError`; later access commonly receives `NoClassDefFoundError`.
- Avoid heavy I/O, networking, or recoverable configuration work in static initializers.

### 3.10 Imports and Name Resolution

- Imports affect source-name resolution only; they do not load classes or add dependencies.
- Wildcard imports do not include subpackages.
- Use static imports sparingly, where they improve readability, such as test assertions.
- If imported classes share a simple name, use a fully qualified name for at least one.

## 4. Memory and Strings
- Heap vs Stack, String Pool
- String, StringBuilder, StringBuffer (immutable vs mutable)
- equals() vs ==

4. Memory & String - Most asked in interviews

A. Heap vs Stack - Full

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

B. String Pool - Important

- String Pool is special area inside heap (Method Area) to save memory.
String s1 = "Stitch"; // literal - goes to String Pool. If "Stitch" already exists, reuse it
String s2 = "Stitch"; // s1 and s2 point to SAME object in pool

String s3 = new String("Stitch"); // new - creates 2 objects: 1 in heap, 1 in pool if not exists
String s4 = new String("Stitch"); // new object in heap again - s3 != s4
- Why pool? String used a lot, reuse saves memory.

- intern() method:
String s3 = new String("Stitch").intern(); // force to use from pool
// Now s1 == s3 true
C. String vs StringBuilder vs StringBuffer
Feature | String | StringBuilder | StringBuffer
Mutable? | NO - Immutable | YES - Mutable | YES - Mutable
Thread Safe? | Yes (immutable) | No - faster | Yes - synchronized, slower
When to use | Less changes | Single thread, many changes | Multi-thread
Memory | New object each change | Same object modified | Same object modified
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
D. equals() vs == - Super Important

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

### 4.5 Unicode and String Operations

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

### 4.6 Concatenation and Formatting

- The compiler usually optimizes simple concatenation.
- Repeated concatenation inside loops should use `StringBuilder`.
- `String.join` and `Collectors.joining` handle delimiters cleanly.
- `String.formatted` and `Formatter` improve readability but are slower in hot paths.
- Never build SQL by concatenating values; formatting does not make SQL safe.

### 4.7 Defensive String Handling

- Use `isBlank()` when whitespace-only input is invalid.
- `strip()` is Unicode-aware; `trim()` removes only characters up to U+0020.
- Use `equalsIgnoreCase()` only when its locale-independent semantics fit the domain.
- Prefer short-lived `char[]` for secrets where APIs support it, though copies may still exist.

### 4.8 Reference Strengths and Cleanup

- Strong references keep objects alive normally.
- `SoftReference` may be cleared under memory pressure and is unsuitable for predictable cache policy.
- `WeakReference` does not prevent collection and can support carefully designed canonical mappings.
- `PhantomReference` plus `ReferenceQueue` supports post-mortem cleanup coordination.

Finalization is deprecated for removal and has unpredictable timing. Use try-with-resources; use `Cleaner` only as a last-resort safety net.

## 5. Exception Handling
- Checked vs Unchecked Exception
- try-catch-finally, throw, throws
- Custom Exception

5. Exception Handling - Detailed

A. What is Exception?
Abnormal event that breaks normal flow. Object of Throwable class.
Object -> Throwable -> 2 childs
                                      1. Exception -> Checked + Unchecked (RuntimeException)
                                      2. Error -> OutOfMemoryError, StackOverflowError - don't handle
B. Checked vs Unchecked
Checked (Compile-time) | Unchecked (Runtime)
Compiler forces you to handle | Compiler doesn't force
Outside program control | Programming mistake
Must use try-catch or throws else compile error | No need, but you can
Eg: IOException, SQLException, ClassNotFoundException, FileNotFoundException | Eg: NullPointerException, ArithmeticException, ArrayIndexOutOfBounds, NumberFormatException
Extends Exception directly | Extends RuntimeException
// Checked - compile error if not handled
FileReader fr = new FileReader("file.txt"); // Must surround with try-catch

// Unchecked - compiles fine, fails at runtime
int a = 10/0; // ArithmeticException at runtime
String s = null; s.length(); // NullPointerException
C. try-catch-finally, throw, throws - Full with flow
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

D. Flow Examples
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
E. Custom Exception - You create own

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

### 5.6 Exception Hierarchy and Boundaries

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

### 5.7 Multi-Catch and Suppressed Exceptions

```java
try {
  readConfiguration();
} catch (IOException | ParseException e) {
  throw new ConfigurationException("Invalid configuration", e);
}
```

Multi-catch alternatives cannot be parent and child types. In try-with-resources, resources initialize left-to-right and close right-to-left. If the body and `close()` both fail, close failures are available through `getSuppressed()`.

### 5.8 Exception Design Guidelines

- Catch only where code can recover, add context, or translate abstractions.
- Preserve causes when wrapping.
- Do not catch `Throwable` for ordinary application handling.
- Do not use exceptions for expected control flow.
- Log once at the boundary that handles the error.
- Never expose secrets or full sensitive payloads in exception messages.
- Assertions are disabled by default and must not validate public input or required business rules.

### 5.9 Common Anti-Patterns

- Empty catch blocks hide failures.
- Broad catches may accidentally swallow cancellation or programming defects.
- Logging and rethrowing unchanged exceptions produces duplicate logs.
- Returning `null`, zero, or empty data after unexpected failure creates success-shaped errors.
- Throwing from `finally` can replace the original failure.
- Failing to restore interrupted status can prevent task cancellation.

## 6. Collections Framework
- List: ArrayList vs LinkedList vs Vector
- Set: HashSet vs LinkedHashSet vs TreeSet
- Map: HashMap vs LinkedHashMap vs TreeMap vs ConcurrentHashMap
- Queue, Deque, Stack
- Comparable vs Comparator
- Iterators, fail-fast vs fail-safe

6. Collections Framework - This decides your selection

Hierarchy:
Collection -> List, Set, Queue
Map is separate - key-value, not child of Collection

---

A. List: Ordered, allows duplicates, index-based
Feature | ArrayList | LinkedList | Vector
Internal | Dynamic array Object[] | Doubly Linked List (Node prev, data, next) | Dynamic array - legacy
Get by index | O(1) - fast | O(n) - slow, must traverse | O(1)
Insert/delete middle | O(n) - shift | O(1) - just change pointers | O(n)
Thread safe? | No | No | Yes - synchronized, slow
When to use | 90% cases - read heavy | Many inserts in middle | Don't use - use ArrayList
List<String> list = new ArrayList<>();
list.add("Stitch"); list.add(0,"Pay"); // add at index
list.get(0); list.set(0,"X"); list.remove(0);
Collections.sort(list);

// LinkedList can work as both List and Deque
LinkedList<String> ll = new LinkedList<>();
ll.addFirst("A"); ll.addLast("Z");
B. Set: No duplicates, at most 1 null (except TreeSet - no null)
HashSet | LinkedHashSet | TreeSet
HashMap internally | LinkedHashMap internally | TreeMap (Red-Black Tree)
No order | Insertion order maintained | Sorted order - natural sorting
O(1) add/search | O(1) | O(log n)
Allows 1 null | Allows 1 null | No null - throws NPE
Use when fast check | When you need order + uniqueness | When sorted set needed
Set<String> set = new HashSet<>();
set.add("A"); set.add("A"); // second ignored -> size 1

Set<Integer> sorted = new TreeSet<>(); // sorted: 1,2,10
sorted.add(10); sorted.add(2); sorted.add(1);

LinkedHashSet maintains insertion order
C. Map: Key-Value, 1 key -> 1 value, key unique, value can duplicate
HashMap | LinkedHashMap | TreeMap | ConcurrentHashMap
No order | Insertion order | Sorted by key | No order
1 null key, many null values | 1 null key | No null key | No null key/value - throws NPE
Not thread safe - fast | Not thread safe | Not thread safe | Thread safe - segment lock, fast - use in multithreading
O(1) | O(1) | O(log n) | O(1)
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

D. Queue, Deque, Stack
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
E. Comparable vs Comparator - Sorting custom objects
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
Comparable | Comparator
In same class - implements Comparable | Separate class
compareTo() 1 param | compare() 2 params
Natural ordering - single logic | Multiple logics
java.lang package | java.util package
F. Iterators, fail-fast vs fail-safe
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

### 6.7 Choosing a Collection

| Requirement | Typical choice |
|---|---|
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

### 6.8 Complexity Guide

- `ArrayList.get`: O(1); middle insertion/removal: O(n).
- `HashMap.get/put`: expected O(1), depending on hashing and resizing.
- `TreeMap.get/put`: O(log n).
- `HashSet.contains`: expected O(1).
- `TreeSet.contains`: O(log n).
- `PriorityQueue.offer/poll`: O(log n); `peek`: O(1).

Big-O does not capture allocation, cache locality, hash quality, concurrency, or small-data constants. Measure critical paths.

### 6.9 Immutable and Unmodifiable Collections

```java
List<String> fixed = List.of("A", "B");
List<String> snapshot = List.copyOf(existing);
List<String> view = Collections.unmodifiableList(existing);
```

- Factory collections reject mutation and generally reject null.
- `copyOf` creates an immutable snapshot unless the source is already suitable.
- `unmodifiableList` is a view; backing-list changes remain visible.
- `Arrays.asList` is fixed-size but permits replacement with `set`.

### 6.10 Map Operations and Contracts

```java
counts.merge(word, 1, Integer::sum);
users.computeIfAbsent(teamId, ignored -> new ArrayList<>()).add(user);
cache.computeIfPresent(key, (key, value) -> refresh(value));
```

- Mapping functions should be short and avoid recursively modifying the same map.
- `HashMap` permits one null key and null values; `ConcurrentHashMap` permits neither.
- Comparator subtraction can overflow; use `Integer.compare`.
- Mutating fields used by hashing or ordering while an element is stored can make it logically unreachable.

## 7. Generics
7. Generics - Detailed

Generics = type safety at compile time. Write code once, work for any type. Added in Java 5.

Without generics - problem:
ArrayList list = new ArrayList(); // raw - can add anything - no safety
list.add("Stitch");
list.add(10);
list.add(new Employee());
String s = (String) list.get(0); // need cast - ClassCastException risk at runtime
With generics - solution:
ArrayList<String> list = new ArrayList<>(); // only String allowed
list.add("Stitch");
// list.add(10); // COMPILE ERROR - type safe
String s = list.get(0); // no cast needed
A. Generic Class
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

B. Generic Method
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
C. Bounded Generics - extends and super - Most Important for interview

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

D. Type Erasure - How Java implements generics internally

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
E. Generic Interface
interface Repository<T> {
  void save(T t);
  T findById(int id);
}
class EmployeeRepo implements Repository<Employee> {
  public void save(Employee e){}
  public Employee findById(int id){ return new Employee(); }
}
F. Interview traps
List<Object>!= List<String> // List<Object> cannot hold List<String>
List<?> list = new ArrayList<String>(); // OK - wildcard allows
List<Object> list2 = new ArrayList<String>(); // ERROR

// Why generics invariant?
List<Integer> intList = new ArrayList<>();
// List<Number> numList = intList; // ERROR - if allowed, you could add Double to Integer list - unsafe

// But arrays are covariant
Integer[] intArr = new Integer[10];
Number[] numArr = intArr; // OK in arrays - but can cause ArrayStoreException at runtime
Quick summary:
- Always use generics - type safe, no casting
- Use extends when you need to read numbers, super when you need to add
- All generic info erased at runtime

### 7.7 Wildcard Capture

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

### 7.8 Generic API Design

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

### 7.9 Heap Pollution and Varargs

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

### 7.10 Reifiable Types

Reifiable types retain enough runtime information for operations such as `instanceof`. Examples include primitives, non-generic classes, raw types, and unbounded wildcard types:

```java
if (value instanceof List<?> list) {
  System.out.println(list.size());
}
```

`List<String>` is non-reifiable because its element type is erased.

## 8. Multithreading and Concurrency
- Thread, Runnable, Thread lifecycle
- synchronized, volatile
- ExecutorService, Future, CompletableFuture
- Concurrent package, Locks
- Deadlock, Race Condition

8. Multithreading & Concurrency - Detailed - Senior Level

A. Thread, Runnable, Lifecycle

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
B. synchronized, volatile - Heart of concurrency

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
synchronized | volatile
Locks - mutual exclusion | No lock - only visibility
Makes operation atomic | Does NOT make atomic
Blocks threads | Doesn't block
Use for compound actions | Use for flags - volatile boolean stop
C. ExecutorService, Future, CompletableFuture - Modern way - Never create Thread manually in production
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
D. Concurrent Package, Locks

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
E. Deadlock, Race Condition - Critical

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

### 8.6 Java Memory Model

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

### 8.7 Safe Publication

An object is safely published when other threads cannot observe a partially constructed state. Common mechanisms:

- Store it in a properly locked field.
- Store it in a volatile field.
- Publish through a thread-safe collection.
- Initialize it in a static initializer.
- Share it before starting a new thread.

Do not allow `this` to escape from a constructor by registering listeners, starting threads, or calling external code.

### 8.8 Executor Sizing and Backpressure

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

### 8.9 Cancellation and Timeouts

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

### 8.10 Concurrent Utilities

- `CountDownLatch`: wait until a fixed number of events complete; one-shot.
- `CyclicBarrier`: repeatedly wait until a group reaches a point.
- `Semaphore`: limit concurrent access to a scarce resource.
- `Phaser`: flexible multi-phase coordination.
- `BlockingQueue`: producer-consumer handoff with optional capacity.
- `StampedLock`: optimistic reads for specialized workloads; not reentrant.
- `LongAdder`: scalable counters under heavy contention; `sum()` is not an atomic snapshot.

Prefer high-level utilities over manual `wait()`/`notify()`. If using conditions, always wait in a loop because wakeups may be spurious.

## 9. Java 8+ Features
- Functional Interface, Lambda, Method Reference
- Stream API - filter, map, flatMap, reduce, collect
- Optional
- Default & Static methods in Interface
- Date/Time API (java.time)
- Records, Sealed Classes (Java 17+), Pattern Matching

9. Java 8+ - Important practical and interview topic

A. Functional Interface, Lambda, Method Reference

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
B. Stream API - filter, map, flatMap, reduce, collect - HEART of Java 8

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
C. Optional - Avoid NullPointerException
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
D. Default & Static methods in Interface - Java 8

Why? To add new methods to interface without breaking old implementations.
interface Payment {
  void pay(); // abstract
  
  default void log(){ System.out.println("Logging"); } // default - can be overridden
  static void info(){ System.out.println("Payment gateway"); } // static - cannot override, call via InterfaceName

  private void helper(){} // Java 9 - private method in interface for code reuse inside default methods
}

Payment.info(); // call static
- Diamond problem with default methods? If class implements 2 interfaces with same default method, must override.

E. Date/Time API (java.time) - Java 8 - Thread safe, replaces Date/Calendar
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
F. Records, Sealed Classes (Java 17+), Pattern Matching - Java 16+

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
Common interview exercises:
// Find duplicate numbers using stream
list.stream().collect(groupingBy(Function.identity(), counting()))
    .entrySet().stream().filter(e->e.getValue()>1).map(Map.Entry::getKey).collect(toList())

// 2nd highest salary
employees.stream().map(e->e.salary).sorted(Comparator.reverseOrder()).skip(1).findFirst()
### 9.7 Stream Semantics and Laziness

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

### 9.8 Primitive Streams

Use `IntStream`, `LongStream`, or `DoubleStream` to avoid boxing overhead and access numeric operations:

```java
IntSummaryStatistics stats = employees.stream()
    .mapToInt(Employee::age)
    .summaryStatistics();

double average = stats.getAverage();
int maximum = stats.getMax();
```

Use `mapToObj` to return to object streams and `boxed()` when a collection of wrappers is required.

### 9.9 Collector Details

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

### 9.10 Parallel Stream Cautions

- Parallel streams normally share the common `ForkJoinPool`.
- Blocking operations can starve unrelated work using the same pool.
- Parallelism adds splitting, coordination, and merging overhead.
- Ordered pipelines and stateful operations may reduce benefits.
- Results must be independent of scheduling; do not mutate shared non-thread-safe state.
- Benchmark with realistic data before using `parallelStream`.

### 9.11 Optional Design

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

### 9.12 Time-Zone and Clock Guidance

- `Instant` is a point on the UTC timeline.
- `LocalDateTime` has no zone and is ambiguous during daylight-saving transitions.
- `ZonedDateTime` combines local date/time with time-zone rules.
- `OffsetDateTime` has a fixed offset but not complete regional rules.
- Persist timestamps as `Instant` or an offset-aware database type.
- Inject `Clock` for deterministic tests.

## 10. JVM Internals
- ClassLoader, Garbage Collection, Mark & Sweep
- Memory Areas - Young/Old Gen, Metaspace
- equals() & hashCode() contract

10. JVM Internals - Deep Topic

A. JVM Architecture - Full
Java Code (.java) -> javac -> Bytecode (.class) -> JVM -> OS -> Hardware

JVM Components:
1. ClassLoader Subsystem
2. Runtime Memory Areas (Heap, Stack, Method Area, PC Register, Native Stack)
3. Execution Engine (Interpreter, JIT Compiler, GC)
B. ClassLoader - How class loading happens

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

C. Memory Areas - Young/Old Gen, Metaspace
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
D. Garbage Collection, Mark & Sweep

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
GC | How | For
Serial GC | Single thread, STW | Small apps
Parallel GC | Multi-thread Young | Default Java 8 - throughput
CMS (old) | Concurrent - less pause | Deprecated
G1 GC | Region based - splits heap into regions, predicts pause time - Default Java 9+ | Large heap, low pause - Stitch uses G1
ZGC, Shenandoah | Ultra low pause <10ms even for TB heap | Java 11+ - huge apps
- Tuning flags:
-Xms512m -Xmx1024m // min and max heap
-XX:+UseG1GC // use G1
-XX:MaxGCPauseMillis=200
E. equals() & hashCode() Contract - VERY IMPORTANT for HashMap/HashSet

- Contract from Object class - MUST follow, else collections break.
class Employee {
  int id; String name;
  
  // Default from Object class - checks == - reference equality - WRONG for content
}
Contract Rules:

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

Interview questions:
1.  Why String Pool possible? Because String immutable + hashCode cached.
2.  What is GC Root? Stack refs, static, JNI.
3.  Can you call GC? System.gc() hint, not guarantee.
4.  OutOfMemory vs StackOverflow?
5.  What happens if hashCode not overridden?

### 10.6 Bytecode Execution and JIT Compilation

The interpreter starts bytecode quickly. As methods become hot, tiered compilation uses C1 and C2 compilers to produce optimized native code.

Common optimizations include:

- Method inlining.
- Escape analysis and scalar replacement.
- Lock elimination.
- Loop optimizations.
- Devirtualization when runtime types are predictable.

If an assumption becomes false, the JVM can deoptimize compiled code and return execution to a less optimized tier. Warmup is why short ad-hoc benchmarks are misleading.

### 10.7 Object Layout and Allocation

An object generally contains a header, instance fields, and alignment padding. Exact layout depends on JVM options and architecture.

- Thread-local allocation buffers make most small object allocations inexpensive.
- Large objects or exhausted buffers may take slower allocation paths.
- Escape analysis can eliminate some allocations, but code must not depend on that optimization.
- Compressed ordinary object pointers can reduce memory use for suitable heap sizes.

Use JOL or a profiler when exact layout matters; do not estimate from field sizes alone.

### 10.8 Native Memory

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

### 10.9 Class-Loader Identity and Leaks

A class is identified by both its binary name and defining class loader. The same class file loaded by different class loaders produces incompatible runtime types.

Application servers and plugin systems can leak class loaders when long-lived objects retain application classes through:

- Static collections.
- `ThreadLocal` values.
- Running threads or executors.
- JDBC drivers or callbacks not deregistered.
- Framework caches and listeners.

### 10.10 GC Terminology and Selection

- **Live set:** objects reachable after collection.
- **Allocation rate:** bytes allocated per unit time.
- **Pause:** application threads stop at a safepoint.
- **Concurrent phase:** GC work overlaps application execution.
- **Throughput:** application time relative to total elapsed time.

Minor, major, and full-GC terminology is collector-specific; always interpret actual GC logs for the selected collector. Enable unified logging on modern JDKs:

```text
-Xlog:gc*,safepoint:file=gc.log:time,uptime,level,tags
```

### 10.11 Common JVM Errors

- `OutOfMemoryError: Java heap space`: heap cannot satisfy allocation after GC.
- `OutOfMemoryError: Metaspace`: class metadata limit reached, often from excessive classes or loader leaks.
- `OutOfMemoryError: unable to create native thread`: OS/thread or native-memory limit reached.
- `StackOverflowError`: thread stack exhausted, usually by deep or infinite recursion.
- `LinkageError`: incompatible or duplicate class definitions, versions, or loader constraints.

## 11. Advanced Core Java
- Serialization, Cloning
- Reflection, Annotations
- IO vs NIO, File handling
- JDBC

11. Advanced - Last Core Topic

A. Serialization, Cloning

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

B. Reflection, Annotations

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
C. IO vs NIO, File handling
IO (java.io) | NIO (java.nio - New IO - Java 4)
Blocking - thread waits till read completes | Non-blocking - thread can do other work
Stream oriented - byte/char stream | Channel + Buffer oriented - faster
No selector | Selector - 1 thread handles many channels - Netty uses for 10k connections
Old | New - for high performance file/network
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
D. JDBC - Java Database Connectivity - Connect Java to DB

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

### 11.5 Serialization Safety and Versioning

- `serialVersionUID` controls compatibility checks but does not guarantee semantic compatibility.
- Adding fields is often compatible because missing fields receive defaults; changing field types or hierarchy can break compatibility.
- Validate invariants in `readObject`; constructors are not called for normal serializable classes.
- Prefer a stable schema format for long-lived storage and inter-service communication.
- Never deserialize untrusted native Java streams without a strict object filter.

### 11.6 NIO Buffers and Channels

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

### 11.7 File-System Correctness

- Specify charsets explicitly, usually `StandardCharsets.UTF_8`.
- Use atomic move where supported for replace-style writes.
- Do not assume a single `read` or `write` processes the entire buffer.
- Close directory streams and file channels.
- Decide how symbolic links should be handled for security-sensitive operations.
- Use streaming APIs for large files rather than `readAllBytes`.

### 11.8 JDBC Transactions and Pooling

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

### 11.9 Reflection and Method Handles

Reflection is flexible but shifts errors to runtime and can conflict with module encapsulation. Cache validated metadata when repeatedly used.

`MethodHandle` and `VarHandle` provide typed, JVM-supported dynamic access:

- `MethodHandle`: invoke methods, constructors, and fields through a typed signature.
- `VarHandle`: access fields or array elements with defined memory-ordering modes.

Use ordinary calls when types are known statically.

### 11.10 ServiceLoader

`ServiceLoader` supports provider discovery without hard-coding implementations:

```java
ServiceLoader<PaymentProvider> providers =
    ServiceLoader.load(PaymentProvider.class);

for (PaymentProvider provider : providers) {
  provider.initialize();
}
```

Classpath providers use `META-INF/services/<interface-name>`; named modules use `uses` and `provides`.

## 12. SOLID Principles and Design Patterns

SOLID is a set of design guidelines that makes object-oriented code easier to change, test, and maintain. These are principles, not strict rules; apply them when they reduce coupling and clarify responsibilities.

### A. Single Responsibility Principle (SRP)

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

### B. Open/Closed Principle (OCP)

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

### C. Liskov Substitution Principle (LSP)

A subtype must be usable wherever its parent type is expected without breaking behavior. An override must preserve the parent's contract, including accepted inputs, outputs, and exceptions.

Bad example: a `ReadOnlyFile` extending `File` but throwing `UnsupportedOperationException` from a required `write()` method. Better: use separate `Readable` and `Writable` interfaces.

### D. Interface Segregation Principle (ISP)

Clients should not depend on methods they do not use. Prefer small capability-based interfaces.

```java
interface Printable { void print(); }
interface Scannable { void scan(); }
interface Faxable { void fax(); }
```

A basic printer can implement only `Printable`; it does not need fake `scan()` or `fax()` methods.

### E. Dependency Inversion Principle (DIP)

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

### F. Common Design Patterns

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

### G. Pattern Selection and Trade-Offs

Patterns are vocabulary for recurring designs, not goals by themselves.

- Start with the simplest direct design.
- Introduce a pattern when variation, lifecycle, or collaboration has become concrete.
- Prefer explicit dependencies over service locators and hidden global access.
- A pattern that adds more indirection than useful flexibility is over-engineering.
- Document ownership, thread-safety, error behavior, and extension points, not merely the pattern name.

### H. Additional Useful Patterns

- **Command:** represent an operation as an object; useful for queues, retries, and undo.
- **State:** move state-specific behavior out of large conditionals.
- **Chain of Responsibility:** pass a request through ordered handlers such as filters.
- **Facade:** provide a stable, simplified boundary over a complex subsystem.
- **Proxy:** control access for caching, security, transactions, or remote calls.
- **Repository:** isolate domain logic from persistence queries.
- **Unit of Work:** coordinate a set of persistence changes in one transaction.

### I. Domain Modeling Guidelines

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

### J. Refactoring Toward SOLID

1. Protect current behavior with tests.
2. Identify the reason a class changes.
3. Extract one responsibility or boundary at a time.
4. Pass dependencies through constructors.
5. Move condition-specific behavior behind a small interface only when multiple implementations are real.
6. Re-run tests and evaluate whether coupling and readability improved.

## 13. Maven and Gradle

Build tools compile source, run tests, resolve dependencies, package artifacts, and execute plugins consistently in local and CI environments.

### A. Standard Project Layout

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

### B. Maven

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

### C. Gradle

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

### D. Dependency Guidance

- Pin or centrally manage dependency and plugin versions.
- Inspect transitive dependencies before exclusions.
- Keep lockfiles or dependency verification metadata when the build tool supports them.
- Never place credentials directly in build files.
- Run vulnerability scanning in CI and update dependencies deliberately.
- Produce reproducible builds: the same source and inputs should create equivalent output.

### E. Maven Project Details

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

### F. Gradle Project Details

- The configuration phase creates the task graph; the execution phase runs selected tasks.
- Avoid doing network or expensive file work during configuration.
- Use lazy `Provider` APIs so values are calculated only when required.
- Version catalogs centralize dependency aliases and versions.
- The build cache reuses outputs when task inputs match.
- The configuration cache reuses configuration state when plugins and scripts are compatible.

### G. Dependency Resolution

When versions conflict, understand the build tool's selection strategy rather than adding exclusions blindly.

```text
mvn dependency:tree -Dverbose
gradlew dependencyInsight --dependency jackson-databind
```

- Direct dependencies should describe APIs the source actually uses.
- Transitive dependencies are implementation details of another dependency and can change.
- Maven's nearest-definition behavior and Gradle's conflict resolution differ.
- Test the packaged artifact, not only IDE execution, to catch missing runtime dependencies.

### H. Build Reproducibility and CI

- Use toolchains to select the compiler independently of the JVM running the build.
- Pin plugin versions.
- Normalize archive timestamps where byte-for-byte output matters.
- Separate unit and integration-test phases.
- Cache immutable dependency downloads, not mutable build outputs without correct keys.
- Publish checksums and software bills of materials for released artifacts.
- Run clean builds periodically so stale output cannot hide missing generated files.

## 14. Testing with JUnit and Mockito

### A. Testing Pyramid

- **Unit tests:** Fast and isolated; test one unit of behavior.
- **Integration tests:** Verify database, network, filesystem, framework, or multiple components together.
- **End-to-end tests:** Exercise the deployed system through its public interface; fewer because they are slower and more fragile.

Good tests follow Arrange-Act-Assert and describe behavior rather than implementation.

### B. JUnit 5

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

### C. Mockito

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

### D. Test Quality

- Test normal cases, boundaries, invalid input, and failure paths.
- Keep tests deterministic: control clocks, randomness, threads, and external services.
- Inject `Clock` instead of calling `LocalDateTime.now()` directly when time affects behavior.
- Use temporary directories for file tests.
- Prefer realistic integration tests for SQL queries and serialization contracts.
- Treat coverage as a signal, not the goal; assertions must verify meaningful behavior.

### E. Test Doubles

- **Dummy:** passed but never used.
- **Stub:** returns prepared data.
- **Spy:** records calls and may wrap real behavior.
- **Mock:** verifies expected interactions.
- **Fake:** lightweight working implementation, such as an in-memory repository.

Prefer state-based assertions when observable output is enough. Interaction verification is valuable at true boundaries, but excessive verification tightly couples tests to implementation.

### F. Test Data and Fixtures

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

### G. Integration Testing

Integration tests should exercise real boundaries where compatibility matters:

- Database schema, queries, constraints, and transactions.
- HTTP serialization and status/error contracts.
- Messaging acknowledgements and redelivery.
- Filesystem permissions and atomicity assumptions.
- Framework dependency injection and configuration.

Use isolated databases or containers with deterministic setup and cleanup. Do not replace every integration boundary with mocks and then assume integration works.

### H. Asynchronous and Concurrent Tests

- Prefer latches, futures, and await utilities over `Thread.sleep`.
- Set an upper timeout so failures terminate.
- Assert eventual outcomes without depending on one scheduler ordering.
- Repeat stress scenarios when testing race-prone code.
- A test that passes once does not prove absence of a data race; design using happens-before rules.

### I. Mutation, Property, and Contract Testing

- Mutation testing changes operators or branches to check whether tests detect behavior changes.
- Property-based testing generates many inputs and verifies invariants.
- Contract tests verify that providers and consumers agree on an interface.
- Snapshot tests are useful for stable structured output but require careful review of updates.

Use these techniques to supplement clear example tests, not replace them.

## 15. Java Platform Module System

JPMS was introduced in Java 9. A module explicitly declares what it requires and which packages it exposes.

```text
src/
  com.example.payment/
    module-info.java
    com/example/payment/PaymentService.java
```

```java
module com.example.payment {
  requires java.sql;
  requires transitive com.example.model;

  exports com.example.payment.api;
  opens com.example.payment.internal to com.fasterxml.jackson.databind;

  uses com.example.payment.api.PaymentProvider;
  provides com.example.payment.api.PaymentProvider
      with com.example.payment.internal.CardPaymentProvider;
}
```

Important directives:

- `requires`: reads another module.
- `requires transitive`: consumers also read that dependency.
- `exports`: makes a package accessible to other modules.
- `opens`: allows deep reflection, often for frameworks.
- `uses` and `provides ... with`: declare service loading.

The classpath is permissive and unnamed; the module path provides stronger encapsulation and reliable dependency declarations. A package should belong to only one named module; split packages cause problems.

### A. Named, Automatic, and Unnamed Modules

- A **named module** contains `module-info.class`.
- An **automatic module** is a non-modular JAR placed on the module path; its name comes from `Automatic-Module-Name` or the JAR file.
- The **unnamed module** contains classpath code and reads all observable modules.

Automatic modules ease migration but expose all packages and have less reliable naming unless the manifest defines it.

### B. Strong Encapsulation

`exports` allows normal compiled access to public types. `opens` allows deep reflection. They solve different problems:

```java
module com.example.orders {
  exports com.example.orders.api;
  opens com.example.orders.dto to com.fasterxml.jackson.databind;
}
```

Qualified exports or opens grant access only to listed modules. Avoid opening every package merely to silence reflective-access failures.

### C. Compilation and Execution

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

### D. Migration Strategy

1. Remove dependencies on JDK internals.
2. Give published JARs stable automatic module names.
3. Resolve split packages and cyclic dependencies.
4. Add descriptors to libraries from the leaves upward.
5. Open only packages that frameworks need for reflection.
6. Test both modular packaging and runtime launch commands.

## 16. Modern Java Features

Use the project's configured Java version before adopting a feature. Long-term-support releases commonly used in production include Java 8, 11, 17, 21, and 25.

### A. `var` for Local Variables (Java 10)

```java
var names = new ArrayList<String>(); // inferred as ArrayList<String>
var total = calculateTotal();        // inferred from return type
```

`var` is not dynamic typing. The compiler still assigns one static type. It works only for local variables with an initializer, enhanced-for variables, and lambda parameters. Avoid it when the inferred type is unclear.

### B. Helpful NullPointerExceptions (Java 14)

The JVM can identify which part of a chained expression was null. This improves diagnostics but does not replace input validation or null-safe design.

### C. Records (Final in Java 16)

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

### D. Sealed Types (Final in Java 17)

```java
sealed interface Result permits Success, Failure {}
record Success(String value) implements Result {}
record Failure(String message) implements Result {}
```

Permitted implementations must be `final`, `sealed`, or `non-sealed`. Sealed hierarchies work well with exhaustive pattern matching.

### E. Pattern Matching for `switch` (Final in Java 21)

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

### F. Virtual Threads (Final in Java 21)

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

### G. Sequenced Collections (Java 21)

`SequencedCollection`, `SequencedSet`, and `SequencedMap` provide a uniform API for ordered collections:

```java
SequencedCollection<String> names = new ArrayList<>();
names.addFirst("A");
names.addLast("B");
String first = names.getFirst();
SequencedCollection<String> reversed = names.reversed();
```

### H. Switch Expressions and Text Blocks

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

### I. Feature Lifecycle and Compatibility

Java features may be permanent, preview, incubating, or experimental:

- Preview language/API features require `--enable-preview` at compile and run time and may change between releases.
- Incubator modules are non-final APIs that must be added explicitly.
- Experimental JVM features may require flags and are not compatibility commitments.

Compile with the correct release target:

```text
javac --release 17 Main.java
```

`--release` constrains language features, bytecode level, and documented JDK APIs together. Setting only `-source` and `-target` does not prevent accidental use of newer library APIs.

### J. Pattern Matching Design

Patterns improve data-oriented branching but should not replace polymorphism automatically.

- Use polymorphism when behavior naturally belongs to each subtype.
- Use a pattern switch when an operation belongs to the consumer and the hierarchy is closed.
- Guarded cases should appear before broader cases.
- Exhaustive sealed-type switches make new subtype additions visible as compile errors.

### K. Virtual Threads vs Reactive Programming

Virtual threads simplify high-concurrency blocking code and stack traces. Reactive APIs remain useful when:

- End-to-end libraries are already non-blocking.
- Streaming backpressure is central.
- The application composes event streams rather than request-per-task workflows.

Do not mix models casually. Blocking inside an event-loop thread can stall many requests, while wrapping every trivial call in a virtual thread adds complexity without benefit.

### L. New Collection and Stream Conveniences

Modern JDKs include useful additions such as:

- `List.of`, `Set.of`, `Map.of` for compact immutable collections.
- `Stream.toList()` for an unmodifiable encounter-ordered list.
- `Collectors.teeing` to combine two downstream reductions.
- `Stream.mapMulti` for one-to-many mapping without creating a stream for each element.
- `Optional.stream` to integrate optional values into pipelines.

Check the exact minimum JDK version before adopting an API in a shared library.

## 17. Production Java Best Practices

### A. API and Object Design

- Validate constructor and method inputs at boundaries.
- Prefer immutable objects for values shared across threads.
- Use records for data carriers when their semantics fit.
- Prefer composition over inheritance unless there is a genuine substitutable IS-A relationship.
- Return empty collections rather than `null`.
- Use `Optional` primarily as a return type, not for every field or parameter.
- Avoid exposing mutable internal collections; return `List.copyOf(...)` or an unmodifiable view as appropriate.
- Program to interfaces when multiple implementations or test doubles are expected.

### B. Resource Management

Use try-with-resources for every `AutoCloseable`:

```java
try (InputStream input = Files.newInputStream(path);
     BufferedInputStream buffered = new BufferedInputStream(input)) {
  return buffered.readAllBytes();
}
```

Resources close in reverse declaration order. If both the body and `close()` fail, the close error becomes a suppressed exception accessible through `getSuppressed()`.

### C. Exception Design

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

### D. Money, Time, and Equality

- Use `BigDecimal` for decimal money calculations, not `double`.
- Construct decimals from strings: `new BigDecimal("0.10")`, not `new BigDecimal(0.10)`.
- Specify rounding explicitly when division may be non-terminating.
- Store machine timestamps as `Instant`; convert to a user time zone at the boundary.
- Use `LocalDate` for date-only values such as birthdays.
- Keep fields used by `equals()` and `hashCode()` stable while an object is a `HashMap` key or `HashSet` member.

### E. Logging

- Use a logging facade and parameterized messages: `log.info("Created order {}", orderId);`
- Choose levels consistently: `ERROR`, `WARN`, `INFO`, `DEBUG`, `TRACE`.
- Include correlation/request IDs for distributed flows.
- Do not log passwords, tokens, session IDs, full payment data, or sensitive personal information.
- Avoid expensive string construction when debug logging is disabled.

### F. Code Quality

- Keep methods focused and names intention-revealing.
- Remove dead code instead of commenting it out; version control keeps history.
- Use static analysis, formatting, tests, and compiler warnings in CI.
- Treat unchecked warnings as issues to understand, not noise to suppress broadly.
- Benchmark performance-sensitive alternatives instead of relying on intuition.

### G. Configuration Management

- Define configuration precedence explicitly.
- Validate required configuration at startup and fail with actionable messages.
- Separate secrets from ordinary configuration.
- Use typed configuration rather than scattered string lookups.
- Record non-sensitive effective configuration for diagnostics.
- Avoid runtime mutation unless the application has a designed reload mechanism.

### H. HTTP and Remote Calls

- Set connect, request, and read timeouts.
- Propagate deadlines rather than resetting a full timeout at each hop.
- Retry only transient failures and only when the operation is idempotent or has an idempotency key.
- Use exponential backoff with jitter.
- Limit concurrency to protect downstream systems.
- Validate response status, content type, and size before deserialization.
- Add circuit breaking only with clear fallback and recovery semantics.

### I. Serialization and API Evolution

- Treat wire formats as public contracts.
- Add fields compatibly and define behavior for unknown or missing fields.
- Do not expose internal persistence entities directly.
- Version APIs based on semantic incompatibility, not every implementation change.
- Use explicit date/time, number, enum, and null representations.
- Test backward and forward compatibility with stored examples.

### J. Database Practices

- Enforce critical invariants with database constraints as well as application validation.
- Index according to measured query plans.
- Avoid N+1 query patterns.
- Paginate large result sets with stable ordering.
- Use optimistic locking when concurrent updates must not silently overwrite each other.
- Design migrations to coexist with old and new application versions during rolling deployment.

### K. Graceful Lifecycle

On shutdown:

1. Stop accepting new work.
2. Mark the instance unready.
3. Allow in-flight requests a bounded grace period.
4. Stop consumers and scheduled tasks.
5. Shut down executors.
6. Flush telemetry where possible.
7. Close pools and other resources.

Shutdown hooks are best-effort and must finish quickly; abrupt process or host failure can bypass them.

## 18. Security Essentials

### A. Input and Output

- Validate input by type, length, range, format, and business rules.
- Prefer allowlists over blocklists.
- Encode output for its destination: HTML, JavaScript, URL, SQL, shell, or log contexts need different handling.
- Do not build SQL with string concatenation. Use `PreparedStatement`.
- Avoid executing OS commands with untrusted input. If unavoidable, pass fixed arguments without invoking a shell.

### B. Secrets and Cryptography

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

### C. Deserialization and Reflection

Native Java deserialization of untrusted bytes is dangerous because gadget chains can execute code. Prefer constrained formats such as JSON with explicit target types. If legacy serialization is unavoidable, use JDK object input filters and a strict allowlist.

Reflection can bypass normal access controls and type checks. Restrict reflected classes and members; do not derive class names or method names directly from untrusted input.

### D. Files and Paths

Prevent path traversal by resolving against an expected root and verifying the normalized result:

```java
Path root = Path.of("/data/uploads").toAbsolutePath().normalize();
Path target = root.resolve(userFileName).normalize();
if (!target.startsWith(root)) {
  throw new SecurityException("Invalid path");
}
```

Also limit upload size, verify content rather than trusting extensions, generate server-side file names, and apply least-privilege file permissions.

### E. Dependency and Runtime Security

- Use supported JDK releases and apply security updates.
- Scan direct and transitive dependencies.
- Remove unused dependencies and features.
- Run the process as a non-administrator with minimum filesystem and network access.
- Use TLS certificate validation; never install a trust-all manager in production.
- Set connection, read, request, and transaction timeouts.

### F. Authentication and Authorization

- Authentication establishes identity; authorization decides permitted actions.
- Check authorization on every protected server-side operation.
- Apply deny-by-default and least privilege.
- Avoid trusting role or ownership data supplied by a client.
- Keep session and token lifetimes appropriate to risk.
- Rotate signing and encryption keys through a controlled process.
- Validate token issuer, audience, signature, expiry, and allowed algorithms.

### G. Denial-of-Service Defenses

Set limits for:

- Request and upload size.
- Decompressed size and archive entry count.
- JSON/XML nesting depth and collection length.
- Regex complexity and input length.
- Query result size and pagination limits.
- Concurrent requests, queued work, and per-client rate.
- Time spent on outbound calls and database operations.

Resource exhaustion is possible even when input is syntactically valid.

### H. XML, JSON, and Template Safety

- Disable external XML entities and DTD processing unless explicitly required and safely configured.
- Bind JSON to expected types; avoid unrestricted polymorphic type loading.
- Limit parser depth and total tokens.
- Escape untrusted data according to the output context.
- Do not evaluate user-controlled template expressions or scripts.

### I. Logging and Audit Security

- Sanitize carriage returns and line feeds when untrusted values enter line-oriented logs.
- Mask secrets and minimize personal data.
- Protect logs from unauthorized read and modification.
- Record security-relevant events with actor, action, target, outcome, and correlation ID.
- Avoid revealing account existence or internal implementation details in user-facing errors.

### J. Security Review Checklist

1. Identify assets, trust boundaries, entry points, and attacker capabilities.
2. Trace untrusted data to SQL, files, commands, templates, logs, and deserializers.
3. Review identity and authorization decisions separately.
4. Inspect dependency and deployment configuration.
5. Verify failure behavior, rate limits, and resource limits.
6. Test with realistic malicious inputs.
7. Ensure monitoring can detect abuse without exposing sensitive data.

## 19. Performance, Monitoring, and Troubleshooting

### A. Measure First

Optimize only after measuring a representative workload. Wall-clock timing around a loop is unreliable for microbenchmarks because the JVM warms up, compiles hot code, and may eliminate unused work. Use JMH for microbenchmarks.

Important measures:

- Throughput: operations completed per unit time.
- Latency: duration of one operation; inspect percentiles such as p50, p95, and p99.
- Error rate: failed operations divided by total operations.
- Saturation: CPU, memory, thread, connection-pool, and queue utilization.

### B. JVM Tools

- `jcmd <pid> VM.flags`: show JVM flags.
- `jcmd <pid> GC.heap_info`: summarize heap configuration.
- `jcmd <pid> Thread.print`: capture a thread dump.
- `jstack <pid>`: inspect thread states and deadlocks.
- `jmap -histo <pid>`: display a heap object histogram.
- `jstat -gc <pid> 1000`: sample garbage-collection statistics.
- Java Flight Recorder (JFR): low-overhead recording of CPU, allocation, locks, I/O, and GC events.
- Java Mission Control (JMC): analyze JFR recordings.

Prefer `jcmd` on modern JDKs when it provides the needed operation.

### C. Common Failure Patterns

**High CPU**

1. Capture multiple thread dumps several seconds apart.
2. Find threads repeatedly in `RUNNABLE`.
3. Inspect hot stack traces.
4. Confirm with JFR or a profiler.

**Memory leak or rising heap**

1. Check whether usage returns to a stable level after GC.
2. Inspect object histograms over time.
3. Capture a heap dump near failure with `-XX:+HeapDumpOnOutOfMemoryError`.
4. Analyze retained size and GC-root paths.
5. Fix the retaining reference, cache policy, listener, `ThreadLocal`, or unbounded collection.

**Deadlock**

Thread dumps identify Java monitor deadlocks and show which threads own and wait for locks. Enforce a consistent lock order and reduce nested locking.

**Slow requests**

Check downstream latency, timeouts, pool saturation, lock contention, GC pauses, database query plans, and excessive allocation. Average latency alone can hide severe tail latency.

### D. GC Guidance

- Set memory limits appropriate to the container or host.
- Avoid choosing a collector or tuning dozens of flags before collecting evidence.
- G1 is a balanced default for many server applications.
- ZGC and Shenandoah target very low pause times for large heaps; verify availability and behavior on the chosen JDK.
- Allocation rate and live-set size often matter more than the number of objects created.
- A larger heap can reduce collection frequency but may increase memory footprint and some pause costs.

### E. Thread-Dump States

- `RUNNABLE`: executing or ready to execute; may also be inside native I/O.
- `BLOCKED`: waiting to enter a synchronized monitor.
- `WAITING`: waiting indefinitely for another action.
- `TIMED_WAITING`: waiting with a deadline, such as `sleep()` or timed `get()`.
- `TERMINATED`: completed.

One thread dump is a snapshot; compare several to distinguish persistent contention from temporary activity.

### F. Observability

Use complementary signals:

- **Logs:** detailed discrete events.
- **Metrics:** aggregate rates, counts, gauges, and distributions.
- **Traces:** request flow and timing across components.
- **Profiles:** where CPU time or allocation occurs inside a process.

Prefer low-cardinality metric labels. User IDs, request IDs, and raw URLs can create unbounded time series and excessive monitoring cost.

### G. Service-Level Indicators

Common SLIs include availability, successful request rate, latency percentiles, freshness, and correctness. Define them from the user's perspective.

- An SLO states the target level over a time window.
- An error budget is the allowed unreliability.
- Alerts should indicate actionable user impact or imminent exhaustion, not every internal fluctuation.

### H. Benchmarking with JMH

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

### I. Capacity and Load Testing

- Test expected load, peak load, sudden spikes, and sustained soak conditions.
- Include realistic payloads, database sizes, cache hit rates, and downstream latency.
- Observe queue growth and tail latency, not only throughput.
- Find the saturation point and define safe operating headroom.
- Verify recovery after overload; a system that remains degraded has not passed.

### J. Diagnostic Data Safety

Heap dumps, thread dumps, JFR recordings, and logs may contain credentials, personal data, and request contents. Restrict access, encrypt storage and transfer, define retention, and remove artifacts after diagnosis.

## 20. Quick Revision Checklist

Before an interview or code review, be able to explain:

1. JDK vs JVM and how source becomes bytecode and machine code.
2. Primitive vs reference types, widening vs narrowing, and wrapper caching.
3. Encapsulation, inheritance, composition, overriding, and overloading.
4. `==`, `equals()`, and the `hashCode()` contract.
5. String immutability and when to use `StringBuilder`.
6. Checked vs unchecked exceptions and try-with-resources.
7. `ArrayList`, `HashSet`, `HashMap`, `TreeMap`, queues, and concurrent collections.
8. Generic invariance, wildcards, PECS, and type erasure.
9. The Java Memory Model basics: visibility, atomicity, ordering, `volatile`, and `synchronized`.
10. Executors, futures, `CompletableFuture`, virtual threads, race conditions, and deadlocks.
11. Stream laziness, intermediate vs terminal operations, collectors, and side-effect-free pipelines.
12. `Optional`, `java.time`, records, sealed classes, and pattern-matching switches.
13. Heap, thread stacks, metaspace, class loading, JIT compilation, and garbage collection.
14. JDBC transactions, prepared statements, connection pooling, and resource closure.
15. SOLID trade-offs and when common design patterns are useful.
16. Unit vs integration tests, JUnit lifecycle, and responsible mocking.
17. Maven/Gradle lifecycles, dependency scopes, wrappers, and reproducible builds.
18. Input validation, secret handling, secure randomness, path traversal, and deserialization risks.
19. Logging, metrics, thread dumps, heap dumps, JFR, and evidence-based performance tuning.
20. The Java version required by each language or library feature used in a project.

### A. Suggested Learning Order

1. Syntax, types, control flow, methods, arrays, and strings.
2. Classes, interfaces, inheritance, composition, and exceptions.
3. Collections, generics, equality, and ordering.
4. Lambdas, streams, `Optional`, and `java.time`.
5. Testing, build tools, JDBC, and file handling.
6. Thread safety, executors, futures, and the Java Memory Model.
7. JVM memory, class loading, GC, profiling, and diagnostics.
8. Design principles, security, modules, and production operations.

### B. Coding Exercises

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

### C. Code Review Questions

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

### D. Scenario-Based Interview Practice

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

### E. Final Revision Method

For each topic, verify that you can:

1. Define it in one or two sentences.
2. Write a small correct example without copying.
3. Explain one common mistake.
4. Describe when not to use it.
5. Connect it to production behavior, testing, or diagnostics.
