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
