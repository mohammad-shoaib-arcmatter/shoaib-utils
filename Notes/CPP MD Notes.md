# Table of Contents

- [1. Preprocessor Directives](#1-preprocessor-directives)
- [2. Enum](#2-enum)
- [3. Alias](#3-alias)
- [4. Lambda Function](#4-lambda-function)
- [5. Function Template](#5-function-template)
- [6. Class](#6-class)
- [7. Operator Overloading](#7-operator-overloading)
- [8. Encapsulation](#8-encapsulation)
- [9. Abstraction](#9-abstraction)
- [10. Inheritance](#10-inheritance)
- [11. Polymorphism](#11-polymorphism)
- [12. Exception Handling](#12-exception-handling)
- [13. Functors](#13-functors)
- [14. Class Template](#14-class-template)
- [15. Lvalues and Rvalues](#15-lvalues-and-rvalues)
- [16. Smart Pointers](#16-smart-pointers)
- [17. Namespaces](#17-namespaces)
- [18. Typeid](#18-typeid)
- [19. Constexpr](#19-constexpr)
- [20. decltype](#20-decltype)
- [21. Type traits](#21-type-traits)
- [22. constexpr if](#22-constexpr-if)
- [23. Concepts (C++ 20)](#23-concepts-c-20)
- [24. File operation](#24-file-operation)
- [25. STL Container](#25-stl-container)
- [26. STL Algorithms](#26-stl-algorithms)
- [27. Concurrency and Multithreading](#27-concurrency-and-multithreading)
- [28. Ranges and Range Algorithm(C++20)](#28-ranges-and-range-algorithmc20)
- [29. Coroutines (C++20)](#29-coroutines-c20)
- [30. Modules (C++20)](#30-modules-c20)

# 1. Preprocessor Directives
Preprocessor directives are commands that are processed before the actual compilation of the code. They are used to instruct the compiler to perform specific actions, such as including header files, defining macros, or conditional compilation.

## 1.1. Types of Preprocessor Directives
1. **#include**: Includes a header file into the current file.
2. **#define**: Defines a macro, which can be a constant or a function-like macro.
3. **#undef**: Undefines a previously defined macro.
4. **#ifdef, #ifndef, #if, #else, #elif, #endif**: Used for conditional compilation, allowing code to be included or excluded based on certain conditions.
5. **#pragma**: Provides implementation-specific directives, which can vary between compilers.

## 1.2. #include Directive
The #include directive is used to include header files into the current file. There are two forms:
1. **#include <filename>**: Searches for the file in the standard include directories.
2. **#include "filename"**: Searches for the file in the current directory and then in the standard include directories.

## 1.3. #define Directive
The #define directive is used to define macros. There are two types:
1. **Object-like macros**: Define a constant or a value.
Example: `#define PI 3.14`

2. **Function-like macros**: Define a macro that takes arguments.
Example: `#define SQUARE(x) ((x) * (x))`

## 1.4. Conditional Compilation Directives
These directives allow code to be included or excluded based on certain conditions.
1. **#ifdef**: Checks if a macro is defined.
Example: `#ifdef DEBUG`

2. **#ifndef**: Checks if a macro is not defined.
Example: `#ifndef RELEASE`

3. **#if**: Evaluates a constant expression.
Example: `#if VERSION > 2`

4. **#else**: Specifies an alternative block of code.
Example: `#else /* code */`

5. **#elif**: Specifies an alternative condition.
Example: `#elif VERSION == 2`

6. **#endif**: Ends the conditional compilation block.

## 1.5. #pragma Directive
The #pragma directive is a preprocessor directive that provides implementation-specific instructions to the compiler. It allows developers to specify compiler-specific options, control compiler behavior, and optimize code generation.
The #pragma directive is used to:

1. **Control compiler warnings and errors**: Specify which warnings or errors to enable or disable.
2. **Optimize code generation**: Instruct the compiler to optimize code for performance, size, or other criteria.
3. **Specify compiler options**: Set compiler options, such as floating-point precision or alignment.
4. **Control linkage and visibility**: Specify linkage and visibility attributes for functions and variables.

## 1.6. Common #pragma Directives
1. **#pragma once**: Ensures a header file is included only once, preventing multiple inclusions and reducing compilation time.
2. **#pragma warning**: Controls warning messages, allowing developers to enable or disable specific warnings.
3. **#pragma optimize**: Specifies optimization options, such as optimization level or optimization techniques.
4. **#pragma pack**: Specifies the alignment of structure members, which can affect memory layout and performance.
5. **#pragma comment**: Inserts a comment into the object file or executable, which can be used for various purposes, such as specifying linker options.

Examples

1. **#pragma once:**
```cpp
#pragma once
// Header file contents
```

This directive ensures the header file is included only once, preventing multiple inclusions and reducing compilation time.

2. **#pragma warning:**
```cpp
#pragma warning(disable: 4100) // Disable warning C4100
// Code that triggers warning C4100
```

This directive disables warning C4100, which is triggered by an unused function parameter.

3. **#pragma optimize:**
```cpp
#pragma optimize("gsy", on) // Enable global optimization
// Code to be optimized
```

This directive enables global optimization for the specified code block.

## 1.7. Practical guidance

Preprocessing happens before C++ compilation: directives can include files, select conditional branches, and define macros, but they do not provide C++ type checking.

- Prefer `constexpr` constants and inline functions over macros when ordinary C++ can express the same thing.
- Give macros distinctive names, parenthesize parameters and results, and avoid arguments with side effects because macro arguments may be evaluated more than once.

Use include guards to prevent a header from being processed repeatedly:

```cpp
#ifndef PROJECT_POINT_HPP
#define PROJECT_POINT_HPP

class Point {};

#endif
```

The macro name should be unique to the header. `#pragma once` is widely supported, but is not part of the C++ standard.

# 2. Enum

```cpp
enum Color {
    RED,
    GREEN,
    BLUE
};
```

Enum values are implicitly assigned an integer value starting from 0. You can also explicitly assign values to enum members:
```cpp
enum Color {
    RED = 1,
    GREEN = 2,
    BLUE = 4
};
```

You can change the underlying data type
```cpp
enum Color: unsigned char {
    RED,
    GREEN,
    BLUE
};
```

## 2.1. Using Enums

```cpp
Color myColor = GREEN;
switch (myColor) {
    case RED:
        std::cout << "The color is red" << std::endl;
        break;
    case GREEN:
        std::cout << "The color is green" << std::endl;
        break;
    case BLUE:
        std::cout << "The color is blue" << std::endl;
        break;
    default:
        std::cout << "Invalid color" << std::endl;
        break;
}
```

## 2.2. Scoped Enums

```cpp
enum class Color {
    RED,
    GREEN,
    BLUE
};
Color myColor = Color::GREEN;
```

Scoped enums are type-safe and strongly typed, meaning that you can't assign an integer value to an enum variable without explicit casting.

## 2.3. Practical guidance

An unscoped `enum` exposes its enumerator names in the surrounding scope and converts to an integer. Prefer `enum class` for new code to avoid name collisions and accidental integer conversions.

- Specify an underlying type only when its size or representation is part of an interface or storage requirement.
- Enumerators are named values, not a guarantee that every integer in their range is a valid enumerator.

```cpp
enum class Status { pending, complete, failed };
Status status = Status::pending;
```

Use `static_cast<int>(status)` when an explicit conversion is needed.

# 3. Alias

## 3.1. using

```cpp
using HugeInt = unsigned long long int;
HugeInt huge_num {18'446'744'073'111};
```

## 3.2. typedef

```cpp
typedef unsigned long long int HugeInt;
```

## 3.3. Alias Templates

```cpp
template <typename T>
using MyVector = std::vector<T>;
```

## 3.4. Practical guidance

A type alias gives an existing type another name; it does not create a distinct type or add runtime cost. `using` is generally preferred because it also supports alias templates.

- Use a descriptive alias when it clarifies intent, but avoid aliases that hide important ownership or unit distinctions.
- A `using` alias template can name a family of types parameterized by a type.

```cpp
template <typename T>
using Matrix = std::vector<std::vector<T>>;
```

`Matrix<double>` is still a `std::vector<std::vector<double>>`; the alias does not prevent mixing it with that underlying type.

# 4. Lambda Function

Lambda functions are also known as **lambda expressions or closures**.

## 4.1. Syntax

```cpp
[capture](parameters) -> return_type {
    // lambda body
}
```

Where:
- [**capture**]: specifies how variables from the surrounding scope are captured by the lambda function.
- (**parameters**): specifies the input parameters of the lambda function.
- **-> return_type**: specifies the return type of the lambda function.
- **{ lambda body }**: specifies the code that is executed when the lambda function is called.

## 4.2. Capture

The capture clause specifies how variables from the surrounding scope are captured by the lambda function. There are several ways to capture variables:
- [x]: capture variable x by value.
- [&x]: capture variable x by reference.
- [this]: capture the this pointer.
- [=]: capture all variables in the surrounding scope by value.
- [&]: capture all variables in the surrounding scope by reference.

## 4.3. Example

```cpp
auto add = [](int x, int y) {
    return x + y;
};
int result = add(2, 3);
std::cout << "Result: " << result << std::endl;
```

## 4.4. Practical guidance

A lambda expression creates a callable object. Its capture list determines what outside state it stores; capturing by value copies the captured object, while capturing by reference refers to the original object.

- A reference capture must not outlive the referenced object. Be especially careful when returning or storing a lambda.
- Use an explicit capture list to make dependencies clear. A `mutable` lambda is needed to modify a value-captured copy.

```cpp
int threshold = 10;
auto is_large = [threshold](int value) { return value > threshold; };
```

This lambda owns its copy of `threshold`; changing the original later does not change the captured value.

# 5. Function Template

```cpp
template <typename T>
T maximum (T a, T b) {
    return (a>b) ? a : b;
}
maximum (a, b);
maximum<double> (a, b);
```

## 5.1. Template Specialization

```cpp
template <>
const char * maximum<const char*>(const char *a, const char *b) {
    return strcmp(a, b);
}
```

## 5.2. Practical guidance

Function templates let the compiler generate functions for types that satisfy the operations used by the implementation. The compiler commonly deduces template arguments from function arguments.

- Keep template requirements clear; in C++20, concepts can state them directly in the declaration.
- Use overloads or constraints when types need meaningfully different behavior rather than relying on cryptic substitution errors.

```cpp
template <typename T>
T larger(T left, T right)
{
    return left < right ? right : left;
}
```

Both arguments must deduce the same `T` here. An explicit conversion or a different template design is needed for mixed argument types.

# 6. Class

```cpp
class Cylinder {
public:
    double base_radius {1.0};
    double height {1.0};
public:
    double volume() {
        return PI * base_radius * base_radius * height;
    }
};
int main()
{
    Cylinder cylinder1;
    cylinder1.base_radius = 3.0;
    cylinder1.height = 2.0;
    cout << cylinder1.volume() << endl;
    return 0;
}
```

Members of class are **private by default**.

## 6.1. Constructors

Special method that is called when an instance of a class is created
No return type
Same name as the class
Can have parameters. Can also have an empty parameter list
Usually used to initialize member variables of a class
```cpp
class Cylinder {
public:
    double base_radius {1.0};
    double height {1.0};
public:
    Cylinder ()
    {
        base_radius = 2.0;
        height = 2.0;
    }
    Cylinder (double radius_param, double height_param)
    {
        base_radius = radius_param;
        height = height_param;
    }
    double volume() {
        return PI * base_radius * base_radius * height;
    }
};
```

## 6.2. Default constructor

```cpp
class Cylinder {
public:
    double base_radius {1.0};
    double height {1.0};
public:
    Cylinder () = default;
    Cylinder (double radius_param, double height_param)
    {
        base_radius = radius_param;
        height = height_param;
    }
    double volume() {
        return PI * base_radius * base_radius * height;
    }
};
```

## 6.3. Setters and getters

```cpp
class Cylinder {
public:
    double height {1.0};
public:
    double get_height ()
    {
        return height;
    }
    void set_height(double h)
    {
        height = h;
    }
    ...
};
```

## 6.4. Const Object

```cpp
class Dog {
public:
    Dog() {
    }
    Dog(string name) {
        this->name = name;
    }
    void print_info(){
        std::cout << "Name : " << name << std:endl;
    }
    // setters and getters
private:
    string name;
};
const Dog dog1("fetcher");

dog1.set_name("tommy"); //Error
dog1.print_info(); //Error
string name = dog1.get_name() //Error

//To resolve this error
class Dog {
public:
    Dog() {
    }
    Dog(string name) {
        this->name = name;
    }
    void print_info() const {
        std::cout << "Name : " << name << std:endl;
    }
    // setters and getters
private:
    string name;
};
```

## 6.5. Mutable Member Variables

A mutable class variable is a member variable of a class that can be modified even if the object is declared as const. Mutable variables are typically used to implement caching, lazy loading, or other optimization techniques.
```cpp
class Dog {
    mutable int count {0};
}
```

## 6.6. Structure Binding

```cpp
struct Point {
    double x;
    double y;
}
int main()
{
    Point point1  { 4.2,3.1};
    auto [a,b] = point1;
}
```

## 6.7. Default value in Constructor

```cpp
class Cylinder
{
private:
    double radius;
    double height;
public:
    Cylinder() = default;
    Cylinder(double radius_param, double height_param = 10);
    ...
};
int main()
{
    Cylinder cy(5);
    ...
}
```

## 6.8. Initializer list

```cpp
Cylinder:: Cylinder(double radius_param, double height_param)
: radius(radius_param),
height(height_param)
{
    // Empty body
}
```

### Member wise copy
Two steps
Object creation
Member variable assignment
Potential unnecessary copies of data
Order of member variables doesn't matter

### Initializer list
Initializing happens at real object creation
Unnecessary copies of data avoided
Order of member variables matters

## 6.9. Explicit Constructors

```cpp
class Square
{
public:
    explicit Square(double side_param);
    ~Square();
private:
    double side;
};
```

Square constructor will not be converted implicitly (like double to Square()).

## 6.10. Constructor Delegation

```cpp
class Square
{
public:
    explicit Square(double side_param);
    Square(double side_param, string color_param, int shading_param);
    ~Square();
private:
    double side;
    string color;
    int shading;
};

Square::Square(double side_param):
    Square(side_param, "red", 3) {
}

Square::Square(double side_param, string color_param, int shading_param):
    side(side_param), color(color_param), shading(shading_param) {
}
```

No further initializations before/after delegation call.

## 6.11. Copy constructor

```cpp
class Person
{
private:
    string last_name;
    string first_name;
    int *age;
public:
    Person() = default;
    Person(string last_name_param, string first_name_param, int age_param);
    Person(string last_name_param, string first_name_param);
    Person(string last_name_param);
};

Person::Person(const Person& source_person):
    last_name(source_person.get_last_name()), first_name(source_person.get_first_name()), age(new int(*(source_person.get_age())) {
}
```

Shallow copy - same memory for both the classes variable
Deep copy - their own memory

## 6.12. Delegating copy constructor

```cpp
Person::Person(const Person& source_person):
Person(source_person.get_last_name(), source_person.get_first_name(),
*(source_person.get_age()) {
}
```

## 6.13. Move constructor

```cpp
class Point
{
private:
    double *x{};
    double *y{};
public:
    Point(double x_param, double y_param);
    ~Point();
};

Point::Point(Point &&source_point):
x(source_point.get_x()),
y(source_point.get_y())
{
    source_point.invalidate(); //x=nullptr;y=nullptr;
}
Point p3(std::move(Point(40.7,50.3));
```

## 6.14. Deleted Constructors

To disable the constructor
```cpp
Point() = delete;
Point(const Point &source_point) = delete;
Point(Point &&source_point) = delete;
```

## 6.15. Initializer list constructors

```cpp
struct Point {
    double x;
    double y;
};
int main(int argc, char **argv)
{
    Point point1{12.5, 45.3}; // it will work
    std:cout << point1.x << " " << point1.y << std:endl;
    return 0;
}
class Point
{
public:
    Point(std::initializer_list<double> list)
    {
        x = *(list.begin());
        y = *(list.begin()+1);
    }
private:
    double x;
    double y;
};
```

## 6.16. Aggregate Initialization

```cpp
Point p1{10.0, 20.0};
int scores[] {1,2,3,4,5};
```

## 6.17. Designated Initializers

```cpp
struct Component {
    double x;
    double y;
    double z;
};
int main()
{
    Component c1{.x=10, .y=20, .z=30};
    Component c1{.x=10, .z=30};
    Component c1{.y=20, .z=30};
    Component c1{.x=10, .z=20, .y=30}; // Compiler ERROR. Should be in order.
}
```

## 6.18. Uniform Initialization

```cpp
User () or {}
```

## 6.19. Friend Function

It can access the private variable members.
```cpp
class Dog {
public:
    friend void debug_dog_info(const Dof &dog);
private:
    std::string dog_name;
    int dog_age;
};
void debug_dog_info(const Dog &dog) {
    std::cout<< "Dog name: " << dog.dog_name
    << "Age: " << dog.dog_age << std::endl;
}
```

## 6.20. Friend Class

```cpp
class Dog
{
public:
    Dog(string dog_name, int dog_age);
    friend class Cat;
private:
    string dog_name;
    int dog_age;
};
class Cat
{
public:
    Cat(string cat_name);
    void show_info_about_dog(const Dog &dog) const {
        cout << "Dog name: " << dog.dog_name << std:endl;
    }
private:
    string cat_name;
};
```

## 6.21. Static Members

Regular member variables are associated with objects. They belong to class objects.
Static member variables are not tied to any object of the class. They live in the context of objects blueprints. They are created even before a single class object has been created.
```cpp
class Point
{
public:
    int get_point_count() const {
        return m_point_count;
    }
private:
    double m_x;
    double x_y;
public:
    static int m_point_count;
};
```

Not allowed to initialize in header file.
It can be initialized in cpp file.
```cpp
int Point::m_point_count = 0;
```

## 6.22. Inline static member variables

An inline static member variable is a static member variable that can be defined directly inside the class definition. This feature was introduced in C++17.
```cpp
class Point
{
public:
    int get_point_count() const {
        return m_point_count;
    }
private:
    double m_x;
    double x_y;
public:
    inline static int m_point_count {0};
};
```

## 6.23. Static constants

```cpp
{
    static inline const double PI {3.14};
}
```

## 6.24. Non static constants can only be initialized using Initializer list.

## 6.25. Static Method

It is tied to class blueprint.
It has access to static member variables.
It does not have access to non-static member variables.

## 6.26. Practical guidance

A class combines data and operations into a type. Design it around a clear invariant: every public operation should leave the object in a valid, predictable state.

- Keep representation details private and expose operations that preserve the invariant.
- Initialize members in the member-initializer list; members are initialized in their declaration order, regardless of the order written in the list.
- Prefer standard-library members such as `std::string` and `std::vector` over owning raw pointers so resource management is automatic.

```cpp
class Counter {
public:
    explicit Counter(int initial = 0) : value_(initial) {}
    void increment() { ++value_; }
    int value() const { return value_; }
private:
    int value_;
};
```

# 7. Operator Overloading

## 7.1. Unary

member: ReturnType operator X()
non-member: ReturnType operator X(Type operand)

## 7.2. Binary

member: ReturnType operator X(Type right_operand)
non-member: ReturnType operator X(Type left_operand, Type right_operand)

## 7.3. Addition Operator as Member

```cpp
class Point
{
public:
    Point() = default;
    Point(double x, double y): m_x(x), m_y(y) {
    }
    ~Point() = default;
    Point operator + (const Point &right) const
    {
        return Point(m_x+right.m_x, m_y+right.m_y);
    }
private:
    double m_x{};
    double m_y{};
};
```

## 7.4. Addition Operator as Non Member

```cpp
class Point
{
    friend Point operator + (const Point &left, const Point &right);
public:
    Point() = default;
    Point(double x, double y): m_x(x), m_y(y) {
    }
    ~Point() = default;
private:
    double m_x{};
    double m_y{};
};
inline Point operator + (const Point &left, const Point &right)
{
    return Point(left.m_x+right.m_x, left.m_y+right.m_y);
}
```

## 7.5. Subscript Operator for reading

It should be member function only
```cpp
double operator[](int index)
{
    asset((index == 0) || (index ==1));
    return (index==0)?m_x:m_y;
}
```

## 7.6. Subscript Operator for reading and Writing

```cpp
double & operator[](int index)
{
    asset((index == 0) || (index ==1));
    return (index==0)?m_x:m_y;
}
```

## 7.7. Stream Insertion operation Operator

```cpp
class Point {
    friend std::ostream& operator << (std::ostream &os, const Point &point);
    ...
};
inline std::ostream& operator << (std::ostream &os, const Point &point)
{
    os << point.m_x << "" << point.m_y << std::endl;
    return os;
}
```

## 7.8. Stream Extraction Operator

```cpp
class Point {
    friend std::istream& operator >> (std::istream &is, const Point &point);
    ...
};
inline std::istream& operator >> (std::istream &is, const Point &point)
{
    double x;
    double y;
    std::cout <<"Enter the values: " << std::endl;
    is >> x >> y;
    point.m_x = x;
    point.m_y = y;
    return is;
}
```

## 7.9. Other Arithmetic Operators

```cpp
class Point
{
public:
    friend Point operator -(const Point &left, const Point &right);
    ...
};
inline Point operator -(const Point &left, const Point &right)
{
    return Point(left.m_x-right.m_x, left.m_y-right.m_y);
}
```

## 7.10. Custom Type Conversion

```cpp
class Number
{
public:
    Number() = default;
    Number(int value);
    explicit operator double() const
    {
        return static_cast<double>(m_int);
    }
    explicit operator Point() const
    {
        return Point(static_cast<double>(m_int), static_cast<double>(m_int));
    }
private:
    int m_int{};
};
//Type conversion can only be done as member function.
```

## 7.11. Prefix Increment Operator (++a)

```cpp
class Point
{
    ...
    void operator ++()
    {
        ++m_x;
        ++m_y;
    }
    ...
};
```

## 7.12. Postfix Increment Operator (a++)

```cpp
class Point
{
    ...
    Point operator ++(int)
    {
        Point local_point(*this);
        ++(*this);
        return local_point;
    }
    ...
};
```

## 7.13. Copy Assignment Operator

```cpp
class Point
{
    ...
    Point& operator =(const Point &right_operand)
    {
        if(this != &right_operand)
        {
            m_x = right_operand.m_x;
            m_y = right_operand.m_y;
        }
        return *this;
    }
    ...
};
```

## 7.14. Copy Assignment Operator with Memory

```cpp
class Point
{
    ...
    Point& operator =(const Point &right_operand)
    {
        delete m_data;
        m_data = new int(right_operand.m_data);
        m_x = right_operand.m_x;
        m_y = right_operand.m_y;
        return *this;
    }
    ...
};
```

## 7.15. Practical guidance

Operator overloading gives existing operators a type-specific meaning; it should preserve the operator's familiar expectations rather than surprise callers.

- Implement operators in terms of a small set of core operations to keep behavior consistent.
- Compound assignment commonly returns `T&`, allowing chaining; comparison operators should provide consistent results.
- Some operators, including assignment, subscripting, and function call, must be members; arithmetic operators may often be non-members.

```cpp
struct Point {
    int x;
    int y;
    friend Point operator+(Point left, Point right)
    {
        return {left.x + right.x, left.y + right.y};
    }
};
```

# 8. Encapsulation

Encapsulation is a fundamental concept in object-oriented programming (OOP) that binds together the data and the methods that manipulate that data. It is a way to hide the implementation details of an object from the outside world and only expose the necessary information through public methods.
```cpp
class BankAccount {
private:
    double balance;
public:
    BankAccount(double initialBalance) {
        balance = initialBalance;
    }
    void deposit(double amount of money) {
        balance += amount;
    }
    void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
        } else {
            std::cout << "Insufficient balance" << std::endl;
        }
    }
    double getBalance() {
        return balance;
    }
};
```

## 8.1. Practical guidance

Encapsulation protects an object's state by controlling how callers access and modify it. Private data lets the class enforce rules in one place instead of relying on every caller to behave correctly.

- Expose the smallest useful public interface; getters and setters are not automatically needed for every field.
- Validate changes at the boundary and make invalid states difficult or impossible to represent.

```cpp
class Temperature {
public:
    explicit Temperature(double celsius) : celsius_(celsius) {}
    double celsius() const { return celsius_; }
private:
    double celsius_;
};
```

If a domain has a restricted valid range, check it in the constructor and update operations before storing the value.

# 9. Abstraction

Abstraction is a fundamental concept in object-oriented programming (OOP) that enables us to focus on essential features of an object or system while ignoring its internal details. It is a way to represent complex systems in a simplified manner, making it easier to understand and interact with them.
```cpp
class Shape {
public:
    virtual void draw() = 0;
    virtual double area() = 0;
};
class Circle : public Shape {
private:
    double radius;
public:
    Circle(double radius) {
        this->radius = radius;
    }
    void draw() override {
        std::cout << "Drawing a circle" << std::endl;
    }
    double area() override {
        return 3.14 * radius * radius;
    }
};
class Rectangle : public Shape {
private:
    double width;
    double height;
public:
    Rectangle(double width, double height) {
        this->width = width;
        this->height = height;
    }
    void draw() override {
        std::cout << "Drawing a rectangle" << std::endl;
    }
    double area() override {
        return width * height;
    }
};
```

## 9.1. Practical guidance

Abstraction presents what a component does while hiding the details of how it does it. A well-designed interface allows callers to use a capability without depending on its implementation.

- Keep interfaces focused on stable operations and avoid exposing implementation-specific details.
- Prefer a simple value type or function interface when runtime polymorphism is not needed; an abstract base class is useful when implementations must be interchangeable.

```cpp
class Shape {
public:
    virtual ~Shape() = default;
    virtual double area() const = 0;
};
```

Callers can work with `Shape` without knowing the concrete shape type or how its area is calculated.

# 10. Inheritance

```cpp
class Person
{
    friend std::ostream& operator <<(std::ostream &os, const Person &person);
public:
    Person();
    Person(std::string first_name_param, std::string last_name_param);
    ~Person();
private:
    std::string first_name{"Mysterious"};
    std::string last_name{"Person"};
};
class Player: public Person
{
    friend std::ostream & operator <<(std::ostream &os, const Player &player);
public:
    Player() = default;
    Player(std::string game_param);
    ~Person();
private:
    std::string m_game{"None"};
};
```

## 10.1. Constructors with Inheritance
```cpp
Engineer::Engineer(const std::string &fullname, int age, const std::string address, int contract_count)
    :Person(fullname, age, address), contract_count(contract_count)
{
}
```

## 10.2. Copy Constructors with Inheritance
```cpp
Engineer::Engineer(const Engineer &source)
:Person(source), contract_count(source.contract_count)
{
}
```

## 10.3. Practical guidance

Public inheritance models an ?is-a? relationship: a derived object should be usable wherever the base type is expected. Inheritance also shares implementation, but it couples the derived type to the base interface.

- Prefer composition when a type merely needs to use another object's behavior.
- Use `override` on intended overrides so signature mistakes are diagnosed, and give polymorphic base classes a virtual destructor.

```cpp
class Animal {
public:
    virtual ~Animal() = default;
    virtual void speak() const = 0;
};

class Dog : public Animal {
public:
    void speak() const override { /* ... */ }
};
```

# 11. Polymorphism

Managing derived objects in memory though base pointers or references and getting the right method called on the base pointer or reference.
```cpp
class Shape
{
public:
    Shape() = default;
    Shape(const std::string &desc);
    ~Shape();
    virtual void draw() const
    {
        std::cout << "Shape::draw() called. Drawing " << m_desc << std::endl;
    }
protected:
    std::string m_desc{""};
};
class Oval: public Shape
{
public:
    Oval() = default;
    Oval(double x_radius, double y_radius, const std::string &desc);
    ~Oval();
    virtual void draw() const
    {
        std::cout << "Oval::draw() called. Drawing " << m_desc
        << "with m_x radius " << m_x_radius
        << "with m_y radius " << m_y_radius << std::endl;
    }
private:
    double m_x_radius{0.0};
    double m_y_radius{0.0};
};
class Circle: public Oval
{
public:
    Circle() = default;
    Circle(double radius, const std::string &desc);
    ~Circle();
    virtual void draw() const
    {
        std::cout << "Circle::draw() called. Drawing " << m_desc
        << "with radius " << get_x_rad() << std::endl;
    }
};
Shape shape("Shape");
Oval oval(2.0, 3.5, "Oval");
Circle circle(3.3, "Circle");
Shape *shape_ptr = &shape;

shape_ptr->draw();	// Shape::draw()
shape_ptr = &oval;

shape_ptr->draw();	// Oval::draw()
shape_ptr = &circle;

shape_ptr->draw();	// Circle::draw()
// If the draw() function is not marked virtual, then all the three calls will be to Shape::draw().
```

## 11.1. Override

```cpp
class Oval: public Shape
{
    ...
    virtual void draw() const override { ... }
    ...
};
```

Override is to tell the compiler that we are not declaring new function, but overriding the virtual function from the base class.

## 11.2. Final Specifier

```cpp
class Dog : public Animal
{
    ...
    void run() const override final { ... }
    ...
};
```

It can't be overridden further in derived class
```cpp
class Cat final: public Feline
{
    ...
};
```

Cat can't be derived further
Restrict how you override methods in derived classes.
Restrict how you can derive from a base class.

## 11.3. Virtual Destructors

```cpp
class Animal
{
public:
    Animal() = default;
    Animal(const std::string &desc);
    ~Animal();
    virtual void breathe() const
    {
        std::cout << "Animal::breathe" << std::endl;
    }
protected:
    std::string m_desc;
};
class Feline: public Animal
{
public:
    Feline() = default;
    Feline(const std::string &fur_style, const std::string &desc);
    ~Feline();
    virtual void breathe() const
    {
        std::cout << "Feline::breathe" << std::endl;
    }
private:
    std::string m_fur_style;
};
Animal *animal = new Feline("ABC", "ABC");
delete animal;
// Only Animal destructor is called. BAD
```

## 11.4. Solution

Make destructor as virtual
```cpp
class Feline: public Animal
{
public:
    Feline() = default;
    Feline(const std::string &fur_style, const std::string &desc);
    virtual ~Feline();
    virtual void breathe() const
    {
        std::cout << "Feline::breathe" << std::endl;
    }
private:
    std::string m_fur_style;
};
```

## 11.5. Practical guidance

Polymorphism lets one interface work with values of different concrete types. Runtime polymorphism uses virtual functions and dispatches according to the dynamic type of an object.

- Pass polymorphic objects by reference or pointer; passing a derived object by value as a base can slice off the derived part.
- Use `override` to verify overrides. Use templates or overloads when compile-time selection is more appropriate than virtual dispatch.

```cpp
void make_sound(const Animal& animal)
{
    animal.speak(); // Calls the override for the actual object type.
}
```

# 12. Exception Handling

```cpp
int a {10};
int b {0};
try
{
    Item item;
    if (b==0)
    throw 0;
    a++;
    b++;
    std::cout << "Code that executes when things are fine" << std::endl;
}
catch (int ex)
{
    std::cout << "Something went wrong. Exception thrown: " << ex << std::endl;
}
std::cout << "END." << std::endl;
catch(...) -> catch everything
```

## 12.1. Standard Exception

```cpp
try {
    ...
}
catch (std::exception &ex)
{
    std::cout << "Something is wrong " << ex.what() << std::endl;
}
```

## 12.2. Practical guidance

Exceptions report failures that prevent an operation from completing normally. They propagate up the call stack until a matching handler is found; local objects are destroyed during stack unwinding.

- Use RAII types to release resources on both normal and exceptional paths; do not manage locks or memory with unmatched manual cleanup.
- Throw an exception that describes the failure and catch polymorphic standard exceptions by `const` reference.
- Do not use exceptions for ordinary expected control flow.

```cpp
try {
    values.at(index); // May throw std::out_of_range.
} catch (const std::exception& error) {
    std::cerr << error.what() << '\n';
}
```

# 13. Functors

Class objects that can be called like ordinary functions.
We set them up by overloading the () operator for our class
```cpp
class Encrypt
{
public:
    char operator() (const char &param)
    {
        return static_cast<char>(param+3);
    }
};
Encrypt encrypt_functor;
std::cout << encrypt_functor('A') << std::endl;
```

Lambda functions are implemented internally as functors.

## 13.1. Practical guidance

A functor is an object that can be called like a function, typically by defining `operator()`. Unlike a plain function pointer, a functor can hold state and configuration.

- Lambdas are convenient functors generated by the compiler and are often the simplest callable for a local algorithm.
- Keep a callable's state and behavior coherent; use standard function wrappers only when type erasure or a uniform stored callable type is needed.

```cpp
struct IsLongerThan {
    std::size_t limit;
    bool operator()(const std::string& text) const
    {
        return text.size() > limit;
    }
};
```

# 14. Class Template

```cpp
template <typename T>
class BoxContainer
{
public:
    BoxContainer<T>(int capacity = 5);
    BoxContainer<T>(const BoxContainer<T> &source);
    ~BoxContainer<T>();
    void add (const T &item);
    bool remove_item (const T& item);
    void operator +=(const BoxContainer<T> &operand);
    void operator = (const BoxContainer<T> &source);
private:
    void expand(int new_capacity);
private:
    T *m_items;
    int m_capacity;
    int m_size;
};
template <typename T>
```

BoxContainer<T>::BoxContainer(int capacity)
```cpp
{
    m_items = new t[capacity];
    m_capacity = capacity;
    m_size = 0;
}
```

## 14.1. Non Type Template parameter

```cpp
template <typename T, int maximum>
class BoxContainer
{
public:
    BoxContainer<T, maximum>(int capacity = 5);
    BoxContainer<T, maximum>(const BoxContainer<T, maximum> &source);
    ~BoxContainer<T, maximum>();
    void add (const T &item);
    bool remove_item (const T& item);
    void operator +=(const BoxContainer<T, maximum> &operand);
    void operator = (const BoxContainer<T, maximum> &source);
private:
    void expand(int new_capacity);
private:
    T *m_items;
    int m_capacity;
    int m_size;
};
BoxContainer<int, 10> int_box1;
```

## 14.2. Typename as non type template parameter

```cpp
template <typename T, T threshold>
class Point
{
public:
    Point(T x, T y);
    ~Point() = default;
private:
    T m_x;
    T m_y;
};
template<typename T, T threshold>

Point<T, threshold>::Point(T x, T y)
: m_x(x), m_y(y)
{
}
```

## 14.3. Default values for template parameters

```cpp
template <typename T = int, int maximum = 10>
class BoxContainer
{
    // code
};
BoxContainer int_box;
BoxContainer<double> int_box2;
BoxContainer<char, 5> int_box3;
```

## 14.4. Explicit template instantiations

```cpp
#include <iostream>
#include <string>
#include "boxcontainer.h"
template class BoxContainer<double,10>;
template class BoxContainer<std::string, 5>;
int main(int argc, char **argv)
{
    std::cout << "Hello World" << std::endl;
    return 0;
}
```

## 14.5. Class Template Specialization

```cpp
template <typename T>
class Adder
{
public:
    Adder()
    {
    }
    T add (T x, T y);
};
template <typename T>


T Adder<T>::add(T a, T b)
{
    return a+b;
}
//Template Specialization
template <>
class Add<char *>
{
public:
    Adder()
    {
    }
    char *add(char *a, char *b);
};
//template<> 	<= this is not needed if defined outside of class
char *Adder<char *>::add(char *a, char *b)
{
    return strcat(a,b);
}
```

## 14.6. Template Specialization with select methods

```cpp
template <> inline
const char *BoxContainer<const char*>::get_max()
{
    // logic
}
```

## 14.7. Practical guidance

Class templates define families of types parameterized by types or compile-time values. The compiler instantiates a specialization when it needs a concrete type.

- Template definitions usually need to be visible where they are instantiated, which is why template implementations commonly live in headers.
- Use type parameters for types and non-type parameters for suitable compile-time values; constrain template parameters when requirements can be expressed.

```cpp
template <typename T>
class Box {
public:
    explicit Box(T value) : value_(std::move(value)) {}
    const T& value() const { return value_; }
private:
    T value_;
};
```

# 15. Lvalues and Rvalues

Lvalues are things you can grab an address for and use at a later time.
Rvalues are transient or temporary in nature, they only exist for a short time, and are quickly destroyed by the system when no longer needed.
Lvalues
```cpp
int x{5};
int y{10};
int z{20};
```

Rvalues
```cpp
z=(x+y); // (x+y) is Rvalues
std::cout << &(x+y); // ERROR Can't grab address
```
## 15.1. Rvalue Reference

When an rvalue reference is bound to an rvalue, the life of the rvalue is extended, and we can manipulate it through the rvalue reference.
```cpp
double add(double x, double y)
{
    return a+b;
}
int main(int argc, char **argv)
{
    int x{5};
    int y{10};
    int &&outcome = x+y; //Extends the lifetime of the temporary result
    double &&result = add(10.1, 20.2)l
    std::cout << "result: " << result << std::endl;
    std::cout <<"outcome: " << outcome << std::endl;
    return 0;
}
```

## 15.2. Move constructor

```cpp
template <typename T>


BoxContainer<T>::BoxContainer(BoxContainer &&source)
{
    if (this == &source)
    return;
    m_items = source.m_items;
    m_size = source.m_size;
    m_capacity = source.m_capacity;
    source.invalidate();
}
```

## 15.3. Move assignment operator

```cpp
template <typename T>
void BoxContainer<T>::operator=(BoxContainer &&source)
{
    if (this == &source)
    return;
    m_items = source.m_items;
    m_size = source.m_size;
    m_capacity = source.m_capacity;
    source.invalidate();
}
```

## 15.4. Moving Lvalues with std::move

```cpp
BoxContainer<int> box1;
BoxContainer<int> box2;
box2 = std::move(box1);
```

## 15.5. Practical guidance

An lvalue identifies an object with identity; an rvalue is a temporary value or an expression that can be used to initialize or assign. This distinction affects overload resolution and whether resources can be transferred.

- `T&` binds to a modifiable lvalue, while `const T&` can also bind to temporaries. `T&&` is an rvalue reference.
- `std::move` does not move by itself; it casts an expression so a move-aware operation can be selected. The source object remains valid but its value may have changed.

```cpp
std::string source = "notes";
std::string destination = std::move(source);
```

After the move, `source` can still be assigned to or destroyed, but do not assume it still contains its original text.

# 16. Smart Pointers

Smart pointers are a type of abstract data type in C++ that provide automatic memory management for dynamically allocated objects. They are designed to prevent common pitfalls such as memory leaks and dangling pointers.

## 16.1. Types of Smart Pointers:

**Unique Pointer (unique_ptr)**: Owns and manages a single object.
**Shared Pointer (shared_ptr)**: Shares ownership of an object with other shared pointers.
**Weak Pointer (weak_ptr)**: Observes an object owned by a shared pointer.
```cpp
#include <memory>
```

## 16.2. Unique Pointer

At any given moment there can only be one pointer managing the memory.
Memory is automatically released when the pointer goes out of scope.
```cpp
Dog *p_dog_3 = new Dog("Dog3");
std::unique_ptr<Dog> up_dog_4 {p_dog_3};
std::unique_ptr<Dog> up_dog_5 {new Dog("Dog5")};
std::unique_ptr<int> up_int {new int(200)};
std::unique_ptr<Dog> up_dog_6 {nullptr};
up_dog_5->print_dog();
*up_int = 500;
std::cout << "Integer is " << *up_int << std::endl;
std::cout << "Address is " << up_int.get() << std::endl;
```

## 16.3. make_unique

```cpp
std::unique_ptr<Dog> up_dog_7 = std::make_unique<Dog>("Dog7");
std::unique_ptr<int> up_int_3 = std::make_unique<int>(30);

std::unique_ptr<Dog> up_dog_9 = up_dog_7; //Error, copy is not allowed
std::unique_ptr<Dog> up_dog_9 = std::move(up_dog_7); //Valid, move is allowed
up_dog_9.reset(); //releases memory and sets the pointer to nullptr
```

## 16.4. Shared Pointer
```cpp
ref_count1	ptr_1	-> data
ref_count2	ptr_2	-> data
ref_count3	ptr_3	-> data
std::shared_ptr<int> int_ptr_1 {new int (20)};

std::cout << "Use count: " << int_ptr_1.use_count() << std::endl; //1
std::shared_ptr<int> int_ptr_2 = int_ptr_1;

std::cout << "Use count: " << int_ptr_2.use_count() << std::endl; //2
std::shared_ptr<int> int_ptr_3 = std::make_shared<int>(55);
```

## 16.5. Unique pointer to shared pointer

```cpp
std::unique_ptr<int> unique_ptr_1 = std::make_unique<int>(22);
std::shared_ptr<int> shared_ptr_1 = std::move(unique_int_1);
```

Shared to unique is not allowed

## 16.6. Weak Pointer

Non owning pointers that don't implement the -> or * operator. You can't use them directly to ready or modify data.
```cpp
std::shared_ptr<int> shared_ptr_1 = std::make_shared<int>(200);
std::weak_ptr<int> weak_ptr_1 (shared_ptr_1);
//To use weak_ptr, you have to turn it into a shared_ptr with lock method
std::shared_ptr<int> weak_turned_shared = weak_ptr_1.lock();
```

## 16.7. Cyclic Dependency Problem

```cpp
class Person
{
public:
    Person() = default;
    ~Person();
    Person(std::string name);
    void set_friend(std::shared_ptr<Person> p {
        m_friend = p;
    }
private:
    std::shared_ptr<Person> m_friend;
    std::string name {"Unnamed"};
};
//Circular dependencies
std::shared_ptr<Person> person_a = std::make_shared<Person>("Alison");
std::shared_ptr<Person> person_b = std::make_shared<Person>("Beth");
person_a->set_friend(person_b);
person_b->set_friend(person_a);
```

Memory will be leaked.
```cpp
Solution is to use weak_ptr;
class Person
{
public:
    Person() = default;
    ~Person();
    Person(std::string name);
    void set_friend(std::shared_ptr<Person> p {
        m_friend = p;
    }
private:
    std::weak_ptr<Person> m_friend;
    std::string name {"Unnamed"};
};
```

## 16.8. Practical guidance

Smart pointers express ownership and automate destruction. Choose a pointer based on who owns the object, not merely as a replacement for every raw pointer.

- Use `std::unique_ptr` for one clear owner; transfer ownership by moving it.
- Use `std::shared_ptr` only when ownership is genuinely shared. Use `std::weak_ptr` for non-owning observation of a shared object and to break ownership cycles.
- Prefer `std::make_unique` and `std::make_shared` to direct allocation with `new`.

```cpp
auto item = std::make_unique<Item>();
std::unique_ptr<Item> owner = std::move(item);
```

# 17. Namespaces

```cpp
namespace No_weight {
    double add(double x, double y)
    {
        return x+y;
    }
}
namespace Weight {
    double add(double x, double y)
    {
        return x+y - 1;
    }
}
int main()
{
    double result = Weight::add(4,2);
}
```

## 17.1. Default Global Namespace

```cpp
double add(double a, double b)
{
    return a+b;
}
namespace MyNamespace {
    double add(double x, double y)
    {
        return x+y - 1;
    }
    void do_something
    {
        double result = ::add(5,6); //Global add function, not namespace's function
    }
}
int main()
{
    MyNamespace::do_something();
    return 0;
}
using namespace std; //Not recommended
using std::cout;
using std::endl;
```

## 17.2. Anonymous Namespace

```cpp
namespace {
    double add(double x, double y)
    {
        return x+y;
    }
}
int main()
{
    double result = add(4,5);
    std::cout << "result: " << result << std::endl;
    return 0;
}
```

## 17.3. Namespace aliases

```cpp
namespace Level1 {
    namespace Level2 {
        namespace Level3 {
            const double weight = 33.33;
        }
    }
}
int main()
{
    std::cout << Level1::Level2::Level3::weight << std::endl;
    namespace Data = Level1::Level2::Level3;
    std::cout << Data::weight << std::endl;
    return 0;
}
```

## 17.4. Practical guidance

A namespace groups declarations and helps prevent name collisions. Qualified names make it clear which library or component a name belongs to.

- Avoid `using namespace` in headers: it can unexpectedly change name lookup in every file that includes the header.
- A using-declaration such as `using std::string;` imports one name into the current scope; keep it local when possible.

```cpp
namespace geometry {
    struct Point { double x; double y; };
}

geometry::Point origin{0.0, 0.0};
```

# 18. Typeid

```cpp
std::cout << "Type of int: " << typeid(int).name() << std::endl;
if (typeid(22) == typeid(int))
{
    std::cout << "22 is an int" << std::endl;
}
```

## 18.1. Pure virtual functions and Abstract classes

```cpp
class Shape
{
protected:
    Shape() = default;
    Shape(const std::string &desc);
public:
    virtual ~Shape() = default;
    //Pure virtual function
    virtual double perimeter() const = 0;
private:
    std::string m_desc;
};
```

Class with at least one pure virtual function is called Abstract class. Object cannot be created of Abstract class.
Derived class should implement the pure virtual function.

## 18.2. Practical guidance

`typeid` provides runtime type information through `std::type_info`. It is most useful when inspecting polymorphic objects; it is not a substitute for a clear virtual interface or visitor design.

- For a polymorphic class, `typeid(*pointer)` reports the dynamic type. For a non-polymorphic expression, it reports the static type.
- Compare `type_info` objects with `==`; the spelling returned by `.name()` is implementation-defined and should not be used as a stable identifier.

```cpp
if (typeid(*shape) == typeid(Circle)) {
    // The dynamic type is Circle when Shape is polymorphic.
}
```

The pointer must be non-null before dereferencing it for `typeid`.

# 19. Constexpr

constexpr is a way to tell the compiler that a function or variable can be evaluated at compile-time, as long as the inputs are constant expressions. This allows the compiler to evaluate the function or variable and replace it with its result, reducing the amount of work that needs to be done at runtime.

## 19.1. Constexpr Functions

constexpr functions are functions that can be evaluated at compile-time. Here's an example:
```cpp
constexpr int add(int a, int b) {
    return a + b;
}
```

## 19.2. Constexpr Variables

constexpr variables are variables that can be evaluated at compile-time. Here's an example:
```cpp
constexpr int x = 5;
constexpr int y = x * 2;
```

## 19.3. Common Use Cases

1. **Metaprogramming**: constexpr can be used to write metaprogramming code that is evaluated at compile-time.

2. **Constant expressions**: constexpr can be used to define constant expressions that can be evaluated at compile-time.

3. **Embedded systems**: constexpr can be used in embedded systems to reduce the amount of work that needs to be done at runtime.

## 19.4. Practical guidance

`constexpr` permits a variable or function to participate in constant evaluation when its inputs and context allow it. A `constexpr` function can also run at runtime when called with runtime values.

- A `const` object is not necessarily a compile-time constant; `constexpr` requires a constant-expression initializer.
- Since C++20, `consteval` requires immediate compile-time evaluation, while `constinit` requires static initialization but does not make an object `const`.

```cpp
constexpr int square(int value)
{
    return value * value;
}

static_assert(square(4) == 16);
```

# 20. decltype

decltype allows you to determine the type of an expression at compile-time.

## 20.1. Syntax

```cpp
decltype(expression)
int x = 5;
decltype(x) y = 10;
```

## 20.2. Common Use Cases

1. **Variable declarations**: decltype can be used to declare variables with the same type as an existing expression.
```cpp
auto x = 5;
decltype(x) y = 10;
```

2. **Function return types**: decltype can be used to deduce the return type of a function.
```cpp
template <typename T>
auto add(T x, T y) -> decltype(x + y) {
    return x + y;
}
```

3. **Template metaprogramming**: decltype can be used to manipulate types at compile-time.
```cpp
template <typename T>
using AddType = decltype(T() + T());
```

## 20.3. Practical guidance

`decltype(expression)` inspects an expression's type without evaluating it. It is useful in generic code and when a return type should follow an expression's exact type.

- For an unparenthesized variable name, `decltype(name)` gives the declared type. For other expressions, value category affects the result.
- `decltype(auto)` preserves the type and value category of its initializer or return expression; that can intentionally produce a reference.

```cpp
int value = 0;
decltype(value) copy = value;       // int
decltype((value)) reference = value; // int&
```

The extra parentheses change the `decltype` rule because `(value)` is an lvalue expression.

# 21. Type traits

Type traits are a feature in C++ that allows you to query and manipulate the properties of types at compile-time. They are a set of classes and functions that provide information about types, such as whether a type is a pointer, reference, or array, and whether it is a class, function, or enum.

## 21.1. Primary Type Categories

The C++ Standard Library provides several primary type categories that can be used to query the properties of types. These include:
**is_void**: checks if a type is void
**is_integral**: checks if a type is an integer type
**is_floating_point**: checks if a type is a floating-point type
**is_array**: checks if a type is an array type
**is_pointer**: checks if a type is a pointer type
**is_reference**: checks if a type is a reference type
**is_member_object_pointer**: checks if a type is a pointer to a member object
**is_member_function_pointer**: checks if a type is a pointer to a member function
**is_enum**: checks if a type is an enum type
**is_union**: checks if a type is a union type
**is_class**: checks if a type is a class type
**is_function**: checks if a type is a function type

## 21.2. Type Properties

In addition to primary type categories, the C++ Standard Library also provides several type properties that can be used to query the properties of types. These include:
**is_const**: checks if a type is const-qualified
**is_volatile**: checks if a type is volatile-qualified
**is_trivial**: checks if a type is trivial
**is_trivially_copyable**: checks if a type is trivially copyable
**is_standard_layout**: checks if a type is standard-layout

## 21.3. Type Relationships

The C++ Standard Library also provides several type relationships that can be used to query the relationships between types. These include:
**is_same**: checks if two types are the same
**is_base_of**: checks if one type is a base of another type
**is_convertible**: checks if one type can be converted to another type

## 21.4. Type Modifications

The C++ Standard Library also provides several type modifications that can be used to modify the properties of types. These include:
**remove_const**: removes const qualification from a type
**remove_volatile**: removes volatile qualification from a type
**remove_reference**: removes reference qualification from a type
**add_pointer**: adds a pointer to a type
**add_reference**: adds a reference to a type
```cpp
#include <type_traits>
#include <iostream>
int main() {
    // Check if int is an integer type
    std::cout << std::is_integral<int>::value << std::endl;  // Output: 1
    // Check if double is a floating-point type
    std::cout << std::is_floating_point<double>::value << std::endl;  // Output: 1
    // Check if int* is a pointer type
    std::cout << std::is_pointer<int*>::value << std::endl;  // Output: 1
    // Remove const qualification from const int
    using T = std::remove_const<const int>::type;
    std::cout << std::is_same<T, int>::value << std::endl;  // Output: 1
    return 0;
}
```

## 21.5. Practical guidance

Type traits are compile-time queries and transformations over types. They are provided primarily in `<type_traits>` and are commonly used by generic libraries.

- Prefer the `_v` and `_t` aliases, such as `std::is_integral_v<T>` and `std::remove_cv_t<T>`, where available.
- Use concepts for readable template constraints in C++20; use traits when you need a type-level result or support older language versions.

```cpp
template <typename T>
constexpr bool is_integer_v = std::is_integral_v<T>;
```

Traits answer specific compile-time questions; they do not validate runtime values.

# 22. constexpr if

Conditonal compilation made easier and more flexible
```cpp
template <typename T>
void func( T t)
{
    if constexpr (std::is_integral_v<T>)
    func_int(t);
    else if constexpr(std::is_floating_point_v<T>)
    func_float(t);
    else
    cout << "Wrong type";
}
```

## 22.1. Practical guidance

`if constexpr` selects a branch during template instantiation. The discarded branch is not instantiated once the condition is known, which allows type-dependent code that would otherwise be ill-formed.

- It requires a constant condition; outside templates it still behaves as a compile-time conditional, but both branches must be valid in non-dependent contexts.
- Use ordinary `if` when the decision depends on a runtime value.

```cpp
template <typename T>
void describe(const T& value)
{
    if constexpr (std::is_integral_v<T>) {
        std::cout << "integer\n";
    } else {
        std::cout << "other type\n";
    }
}
```

# 23. Concepts (C++ 20)

Concepts are a way to define constraints on template parameters, making it easier to write generic code that is both flexible and safe. Concepts are a way to define a set of requirements that a type must meet in order to be used as a template parameter. They are similar to interfaces in other languages.

## 23.1. Defining Concepts

```cpp
template <typename T>
concept Addable = requires(T a, T b) {
    { a + b } -> T;
};
```

## 23.2. Built-in concepts

same_as
derived_from
convertible_to
common_reference_with
common_with
integral
signed_integral
unsigned_integral
floating_point
A mechanism to place constraints on your template type parameters
```cpp
template<typename T>

requires std::integral<T>
T add (T a, T b)
{
    return a+b;
}
```

## 23.3. Syntax 2

```cpp
template<std::integral T>

T add (T a, T b)
{
    return a+b;
}
```

## 23.4. Syntax 3

```cpp
auto T add (std::integral auto a, std::integral auto b)
{
    return a+b;
}
```

## 23.5. Syntax 4

```cpp
template<typename T>

T add (T a, T b) requires std::integral<T>
{
    return a+b;
}
```

## 23.6. Custom concepts

```cpp
template <typename T>
concept MyIntegral = std::is_integral_v<T>;
template <typename T>
concept Multipliable = requires (T a, T b) {
    a * b; // Value is not calculated only checking if this statement is valid
};
template <typename T>

concept Incrementable = requires (T a)
{
    a+=1;
    ++a;
    a++;
};
```

## 23.7. Requires clause

It is used to specify the requirements that a type must meet in order to satisfy a concept.

## 23.8. Syntax

```cpp
template <typename T>
concept ConceptName = requires (/* parameter list */) {
    /* requirement list */
};
```

In this syntax, the requires keyword is followed by a parameter list and a requirement list.

## 23.9. Parameter List

The parameter list specifies the types and values that are used to define the requirements. For example:
```cpp
template <typename T>
concept Addable = requires (T a, T b) {
    /* requirement list */
};
```

In this example, the parameter list includes two parameters a and b of type T.

## 23.10. Requirement List

The requirement list specifies the actual requirements that must be met. For example:
```cpp
template <typename T>
concept Addable = requires (T a, T b) {
    { a + b } -> T;
};
```

In this example, the requirement list includes a single requirement that a + b must be valid and return a value of type T.
The require clause can take in four kinds of requirements:
Simple requirements
Nested requirements
Compound requirements
Type requirements

## 23.11. Simple requirements

```cpp
template <typename T>

concept TinyType = requires (T t)
{
    sizeof(T) <= 4; // Only checks syntax
};
```

## 23.12. Nested requirements

```cpp
template <typename T>

concept TinyType = requires (T t)
{
    sizeof(T) <= 4; // Only checks syntax
    requires sizeof(T) <= 4;  //checks if the expression is true
};
```

## 23.13. Compound requirements

```cpp
template <typename T>

concept Addable = requires (T a, T b)
{
    { a+b } -> std::convertible_to<int>;
    // checks if a+b is valid syntax, and the result is convertible to int
};
```

## 23.14. Practical guidance

Concepts name requirements on template arguments. They make constraints visible at the interface and generally produce clearer diagnostics than deeply nested substitution failures.

- Use standard concepts from `<concepts>` when they express the requirement; do not invent a new concept for an existing standard one.
- A `requires` expression can check whether specific operations and type relationships are valid.

```cpp
#include <concepts>

template <std::integral T>
T add(T left, T right)
{
    return left + right;
}
```

This function accepts integral types and excludes floating-point types at compile time.

# 24. File operation

## 24.1. Common header files

### iostream
Provides definitions for formatted input and output from/to streams.

### fstream
Provides definitions for formatted input and output from/to file streams.

### iomanip
Provides definitions for manipulators used to format stream I/O.

#### Commonly used stream classes
ios
ifstream
ofstream

fstream = ifstream + ofstream
stringstream = istringstream + ostringstream

## 24.2. Global stream objects

cin
cout
cerr
clog

## 24.3. File Opening and Closing Modes

**ios::in - **Open file for input operations. The file pointer is positioned at the beginning of the file.
**ios::out -** Open file for output operations. The file pointer is positioned at the beginning of the file. If the file already exists, its contents will be truncated.
**ios::app -** Open file for appending output operations. The file pointer is positioned at the end of the file.
**ios::ate - **Open file and move the file pointer to the end of the file.
**ios::trunc - **Truncate the file to zero length if it already exists.
**ios::binary - **Open file in binary mode. In binary mode, data is read and written in binary format, without any translations.
**ios::text - **Open file in text mode (default). In text mode, data is read and written in text format, with translations for newline characters and other special characters.

## 24.4. Common stream manipulators

Boolean
boolalpha, noboolalpha
Integer
dec, hex, oct, showbase, noshowbase, showpos, noshowpos, uppercase, nouppercase
Floating point
fixed, scientific, setprecision, showpoint, noshowpoint, showpos, noshowpos
Field width, justification and fill
setw, left, right, internal, setfill
Others
endl, flush, skipws, noskipws, ws

## 24.5. Stream Manipulators - Boolean

Default when displaying Boolean values is 1 or 0.
```cpp
std::cout << (10 == 10) << std::endl;
```
1

```cpp
std::cout << std::boolalpha;
std::cout << (10 == 10) << std::endl;
```
true

```cpp
std::count << (10 == 20) << std::endl;
```
false

## 24.6. Stream Manipulators - integers

dec - base10
noshowbase - prefix used to show hexadecimal or octal
nouppercase - when displaying a prefix and hex values it will be lower case
noshowpos - no '+' is displayed for positive numbers

## 24.7. Stream Manipulators - floating point

setprecision - number of digits displayed(default 6)
fixed - not fixed to a specific number of digits after the decimal point
noshowpoint - trailing zeroes are not displayed
nouppercase - when displaying in scientific notation
noshowpos - no '+' is displayed for positive numbers

## 24.8. Stream Manipulators - align and fill

setw - width not set by default
left - when no field width
right - when using field width
fill - not set by default - blank space is used

## 24.9. Reading from file

```cpp
#include <iostream>
#include <fstream>
int main()
{
    std::ifstream in_file("input.txt");
    std::string line;
    if(!in_file)
    {
        std::cerr << "Problem opening file" << std::endl;
        return 1;
    }
    while(!in_file.eof())
    {
        in_file >> line;
        std::cout << line << std::endl;
    }
    in_file.close();
    return 0;
}
while(std::getline(in_file, line))
{
    std::cout << line << std::endl;
}
char c{};
while(in_file.get(ch))
{
    std::cout << c;
}
std::cout << std::endl;
```

## 24.10. Writing to a Text File

```cpp
#include <iostream>
#include <fstream>
#include <string>
int main()
{
    std::ofstream out_file("output.txt");
    if(!out_file)
    {
        std::cerr << "Error creating file" << std::endl;
        return 1;
    }
    std::string line;
    std::cout << "Enter something to write to the file: " << std::endl;
    getline(std::cin, line);
    out_file << line << std::endl;
    out_file.close();
    return 0;
}
```

By default, trunc mode.
```cpp
std::ofstream out_file("output.txt", std::ios::app);
char c {};
std::cin >> c;
out_file.put( c);
```

## 24.11. Using string streams

```cpp
#include <sstream>
int num {};
double total {};
std::string name {};
std::string info {"Moe 100 1234.5"};
std::istringstream iss{info};
iss >> name >> num >> total;
#include <sstream>
int num {100};
double total {1234.5};
std::string name {"Moe"};
std::ostringstream oss {};
oss << name << " " << num << " " << total;
std::cout << oss.str() << std::endl;
```

## 24.12. Practical guidance

The standard file streams (`std::ifstream`, `std::ofstream`, and `std::fstream`) manage file handles with RAII. Their destructors close the file, including when control leaves scope early.

- Check that opening succeeded and inspect stream state after reading or writing when failure matters.
- Use `std::getline` for whole lines. Formatted extraction with `>>` skips whitespace and is better suited to token-oriented input.

```cpp
std::ifstream input("data.txt");
if (!input) {
    throw std::runtime_error("Could not open data.txt");
}

std::string line;
while (std::getline(input, line)) {
    // Process line.
}
```

# 25. STL Container

Containers are classified into four main categories: sequence containers, associative containers, unordered associative containers, and container adapters.

## 25.1. Sequence Containers

Sequence containers are containers that store elements in a linear sequence, where each element has a specific position. Examples of sequence containers include:
1. **vector**
2. **deque**
3. **list**
4. **array**

## 25.2. Associative Containers

Associative containers are containers that store elements as key-value pairs, where each element is associated with a unique key. The elements are ordered based on the key. Examples of associative containers include:
1. **set**
2. **multiset**
3. **map**
4. **multimap**

## 25.3. Unordered Associative Containers

Unordered associative containers are containers that store elements as key-value pairs, where each element is associated with a unique key. The elements are not ordered, and the container uses a hash function to store and retrieve elements. Examples of unordered associative containers include:
1. **unordered_set**
2. **unordered_multiset**
3. **unordered_map**
4. **unordered_multimap**

## 25.4. Container Adapters

Container adapters are classes that provide a different interface to an underlying container. They do not store elements themselves but instead modify the behavior of an existing container. Examples of container adapters include:
1. **stack**
2. **queue**
3. **priority_queue**

## 25.5. Key differences

1. **Element ordering**: Sequence containers store elements in a linear sequence, associative containers store elements in a sorted order, and unordered associative containers store elements in an unordered manner.

2. **Access methods**: Sequence containers provide indexed access, associative containers provide key-based access, and unordered associative containers provide fast lookup using a hash function.

3. **Container adapters**: Container adapters provide a modified interface to an underlying container.

## 25.6. std::vector

encapsulates dynamic size arrays
```cpp
#include <vector>
std::vector<int> ints2 = {1,2,3,4,5};
std::vector<int> ints3 {11,12,13,14,15};

std::vector<int> ints4(20, 50); //20 items, each value 50
vec_str[2]
vec_str.at(2)
vec_str.front()
vec_str.back()
ints2.push_back(100)
ints2.pop_back()
```

## 25.7. std::array

```cpp
#include <array>
std::array<int,3> int_array1;
std::array<int,3> int_array2{1,2};

int_array2[0]
int_array2.at(2)
int_array2.front()
int_array2.back()
int_array2.data()
```

## 25.8. Iterators

```cpp
std::vector<int> ints1{11,22,33,44,55};
std::vector<int>::iterator it = ints1.begin();
```

or
```cpp
auto it = ints1.begin();

it++;		//next element
ints1.end() 	//null, after the last element
it+3		//current+3 position
```

## 25.9. Reverse iterators

reverse_iterator
rbegin
rend

## 25.10. Constant iterators

Cannot change the underlying data
const_iterator
cbegin
end

## 25.11. Constant Reverse iterators

const_reverse_iterator
crbegin
crend

## 25.12. std::deque

Double Ended Queue
Very fast insertions and removals from both ends of the container
```cpp
std::deque<int> numbers = {1,2,3,4,5};


numbers[3]
numbers.at(3)
numbers.front()
numbers.back()
numbers.clear()

auto it = numbers.begin()+2

numbers.insert(it, 300)
numbers.emplace(it,45)
numbers.erase(numbers.begin()+4)
numbers.erase(numbers.begin()+1, numbers.begin()+4);

numbers.emplace_back(5)
numbers.push_back(5)
numbers.push_front()
numbers.pop_front()
numbers.pop_back()
```

## 25.13. std::forward_list

Very fast insertions and removals in the middle of the container.
It is implemented as a single linked list in memory.
It does not provide the random access operators like [].
```cpp
std::forward_list<int> numbers = {100,2,3,4,5};

numbers.front()
number.clear()

auto it = numbers.begin();

numbers.insert_after(it, 333)
numbers.emplace_after(it, 333)
numbers.unique()
numbers.erase_after(it)
```

## 25.14. std::list

Very fast insertions and removals in the middle of the container
It is implemented as a double linked list in memory
```cpp
std::list<int> numbers = {100,2,3,4,5};

numbers.clear()
number.max_size()
number.empty()
number.size()
auto it = numbers.begin();

numbers.insert (it, 333)
numbers.erase(it)
numbers.push_back(5)
numbers.push_front(4)
numbers.pop_front()
numbers.pop_back()
numbers.unique()
```

## 25.15. std::pair

std::pair is used to stores two data components as a single entity.
It provides facilities to manipulate the components through the **first** and **second** data members.
```cpp
std::pair<int, std::string> pair1 {0, "Book Shelf"};
auto pair2 = std::make_pair(1, "Table");
std::cout << pair1.first << " " << pair1.second << std::endl;
auto [idx, name] = pair1;
std::cout << idx << " " << name << std::endl;
```

## 25.16. std::set

Store element in sorted order
```cpp
#include <set>
std::set<int> numbers {11,16,2,9,12,6};

numbers.clear()
numbers.insert(20)
numbers.emplace(42)

auto it_erase = std::find(numbers.begin(), numbers.end(), 13);
if (it_erase != numbers.end())
std::cout << "Found" << std::endl;
else
std::cout << "Not Found" << std::endl;

numbers.erase(it_erase)
```

## 25.17. std::map

Key-value pair
stores elements ordered by key in increasing order
Doesn't store duplicate keys
```cpp
std::map<int, int> numbers {{1,11}, {2,22}, {3,33}};
auto it = numbers.begin();
while (it != numbers.end())
{
    std::cout << "Key: " << it->first
    << "Value:" << it->second << std::endl;
    it++;
}
numbers.clear();
numbers.insert({4,44});
auto it_erase = numbers.find(3);

numbers.erase(it_erase)
```

## 25.18. std::multiset & std::multimap

These can allow duplicate keys

## 25.19. std::unordered_set & std::unordered_map

Not stored in ascending/descending order
```cpp
#include <unordered_set>
#include <unordered_map>
```

## 25.20. std::stack

```cpp
std::stack<int> numbers;
numbers.push(10);
numbers.push(20);
numbers.push(30);
while(!numbers.empty())
{
    std::cout << numbers.top() << std::endl;
    numbers.pop();
}

numbers.size()
```

The underlying container is deque, but it can be changed by giving as second parameter.
```cpp
std::stack<int, deque<int>> numbers;
std::stack<int, vector<int>> numbers;
std::stack<int, list<int>> numbers;
```

## 25.21. std::queue

```cpp
back()
front()
push_back()
pop_front()
empty()
size()
```

## 25.22. std::priority_queue

sorted based on the higher priority
Default is descending order

## 25.23. Practical guidance

Choose a standard container based on its access pattern and invalidation rules, not just familiarity. Containers manage storage and integrate with iterators and standard algorithms.

- `std::vector` is a strong default for a sequence: it provides fast indexed access and efficient appends on average.
- Ordered maps and sets keep keys sorted; unordered variants use hashing and do not provide sorted iteration.
- Check iterator and reference invalidation rules whenever inserting into or erasing from a container.

```cpp
std::vector<int> values;
values.reserve(100);
values.push_back(42);
```

`reserve` may avoid some reallocations; it does not change the vector's size.

# 26. STL Algorithms

Following are some of the STL algorithms:
all_of
any_of
none_of
for_each
max_element
min_element
find
copy
copy_if
sort
transform

## 26.1. all_of

```cpp
std::vector<int> collection {2,6,8,40,64,70};
if(std::all_of(std::begin(collection),std::end(collection), [](int i) {return (i%2==0);}))
{
    std::cout << "All numbers are even" << std::endl;
}
else
{
    std::cout << "Not all numbers are even" << std::endl;
}
```

## 26.2. any_of

```cpp
std::vector<int> collection {2,6,8,40,64,70};
if(std::any_of(std::begin(collection),std::end(collection), [](int i) {return (i%2==0);}))
{
    std::cout << "At least one number is even" << std::endl;
}
else
{
    std::cout << "None of the numbers is even" << std::endl;
}
```

## 26.3. none_of

```cpp
std::vector<int> collection {2,6,8,40,64,70};
if(std::none_of(std::begin(collection),std::end(collection), [](int i) {return (i%2==0);}))
{
    std::cout << "None of the numbers is even" << std::endl;
}
else
{
    std::cout << "At least one number is even" << std::endl;
}
```

## 26.4. for_each

```cpp
void print(const int &n)
{
    std::cout << " " << n;
}
std::for_each(std::begin(collection), std::end(collection), print);
```

## 26.5. max_element

```cpp
auto result = std::max_element(std::begin(collection), std::end(collection));
```

## 26.6. min_element

```cpp
auto result = std::min_element(std::begin(collection), std::end(collection));
```

## 26.7. find

```cpp
int n = 4;
auto result = std::find(std::begin(collection), std::end(collection), n);
if (result != std::end(collection))
{
    std::cout << "Element found" << std::endl;
}
auto odd = [](int n)
{
    return (n%2 != 0);
}
auto odd_n_position = std::find_if(std::begin(collection), std::end(collection), odd);
if (odd_n_position != std::end(collection))
{
    std::cout << "Collection contains at least one odd number: " << *odd_n_position << std::endl;
}
```

## 26.8. copy

```cpp
std::vector<int> source {1,2,3,4,5,6,7,8,9};
std::vector<int> dest {15,21,12,53,30,40};
std::copy(std::begin(source), std::begin(source)+4, std::begin(dest));
// std::copy(from, to, at_position);
```

## 26.9. copy_if

```cpp
std::copy_if(std::begin(source), std::begin(source)+4, std::begin(dest), odd);
```

## 26.10. sort

```cpp
std::sort(std::begin(source), std::end(source));
```

## 26.11. transform

```cpp
std::transform(std::begin(source), std::end(source), [](int n) {return (n*2);});
```

## 26.12. Practical guidance

Standard algorithms express common operations over iterator ranges and can make loops shorter and easier to review. They work with many container types rather than being tied to one container.

- Prefer an algorithm such as `std::find`, `std::sort`, or `std::transform` over hand-written loops when it states the intent more clearly.
- Iterator ranges are half-open: the first iterator is included and the end iterator is excluded.
- Use `<algorithm>` for classic algorithms and `<ranges>` for C++20 ranges algorithms and adaptors.

```cpp
std::vector<int> values{4, 1, 3};
std::ranges::sort(values);
```

# 27. Concurrency and Multithreading

## 27.1. Concurrency

Concurrency refers to the ability of a program to perform multiple tasks or operations simultaneously, sharing the same resources. Concurrency can be achieved through various techniques, including:

1. **Multithreading**: Creating multiple threads within a single process, each executing a separate portion of the code.
2. **Multiprocessing**: Creating multiple processes, each executing a separate program or task.
3. **Asynchronous programm**ing: Using callbacks, futures, or other mechanisms to perform tasks asynchronously.

## 27.2. Multithreading

Multithreading is a specific type of concurrency where multiple threads are created within a single process. Each thread shares the same memory space and resources, but executes a separate portion of the code.
C++ provides a built-in multithreading library, <thread>, which allows developers to create and manage threads. Key features include:
**std::thread**: A class representing a thread.
**std::mutex**: A class representing a mutex (mutual exclusion) lock.
**std::lock_guard**: A class that provides a convenient way to lock and unlock a mutex.
**std::async**: A function that allows developers to execute a function asynchronously.
```cpp
#include <thread>
#include <mutex>
#include <iostream>
std::mutex mtx;
int counter = 0;
void incrementCounter() {
    for (int i = 0; i < 100000; i++) {
        std::lock_guard<std::mutex> lock(mtx);
        counter++;
    }
}
int main() {
    std::thread t1(incrementCounter);
    std::thread t2(incrementCounter);
    t1.join();
    t2.join();
    std::cout << "Counter: " << counter << std::endl;
    return 0;
}
```

## 27.3. Creating Threads

To create a thread, you can use the std::thread class. The std::thread constructor takes a function or a callable object as an argument, which will be executed by the new thread.
```cpp
#include <thread>
#include <iostream>
void threadFunction() {
    std::cout << "Hello from thread!" << std::endl;
}
int main() {
    std::thread t(threadFunction);
    // ...
}
```

## 27.4. Joining Threads

When a thread is created, it runs concurrently with the main thread. To ensure that the main thread waits for the created thread to finish its execution, you can use the join() function.
```cpp
#include <thread>
#include <iostream>
void threadFunction() {
    std::cout << "Hello from thread!" << std::endl;
}
int main() {
    std::thread t(threadFunction);
    t.join(); // Wait for thread t to finish
    std::cout << "Thread finished" << std::endl;
    return 0;
}
```

## 27.5. Detaching Threads

If you don't want to wait for a thread to finish its execution, you can detach it using the detach() function. When a thread is detached, it runs independently of the main thread, and the main thread cannot wait for it to finish.
```cpp
#include <thread>
#include <iostream>
void threadFunction() {
    std::cout << "Hello from thread!" << std::endl;
}
int main() {
    std::thread t(threadFunction);
    t.detach(); // Detach thread t
    std::cout << "Main thread continues" << std::endl;
    return 0;
}
```

## 27.6. Thread States

A thread can be in one of the following states:

1. **Default-constructed**: A thread object that has not been associated with a thread of execution.
2. **Joinable**: A thread object that has been associated with a thread of execution and has not yet been joined or detached.
3. **Non-joinable**: A thread object that has been joined, detached, or has not been associated with a thread of execution.

Example
```cpp
#include <thread>
#include <iostream>
void threadFunction(int id) {
    std::cout << "Hello from thread " << id << std::endl;
}
int main() {
    std::thread t1(threadFunction, 1);
    std::thread t2(threadFunction, 2);
    t1.join();
    t2.detach();
    std::cout << "Main thread continues" << std::endl;
    return 0;
}
```

## 27.7. mutex class

```cpp
#include <mutex>
std::mutex
lock()
try_lock()
```

unlock
```cpp
std::mutex task_mutex;
void task(const std::string& str)
{
    for(int i = 0; i < 5; i++)
    {
        task_mutex.lock();
        std::cout << str[0] << str[1] << str[2] << std::endl;
        task_mutex.unlock();
    }
}
```

## 27.8. Lock Guard

```cpp
std::lock_guard
void task()
{
    task_mutex.lock();
    throw std::exception();
    task_mutex.unlock();
}
catch (std::exception& e)
{
    ...
}
```

When the exception is thrown
The destructors are called for all objects in scope
The program flow jumps into the catch handler
The unlock call is never executed
The mutex remains locked
```cpp
std::lock_guard<std::mutex> lck_guard(task_mutex);
```

in C++17,
```cpp
std::lock_guard lck_guard(task_mutex);
void task(const std::string& str)
{
    for(int i = 0; i < 5; i++)
    {
        std::lock_guard<std::mutex> lck_guard(task_mutex);
        std::cout << str[0] << str[1] << str[2] << std::endl;
    }
}
```

mutex is unlocked once lck_guard object goes out of scope.

## 27.9. Unique lock

```cpp
std::unique_lock
```

The same basic features as std::lock_guard
Mutex data member
Constructor locks the mutex
Destructor unlocks it
It also has an unlock() member function
We can call this after the critical section
Avoids blocking other threads while we execute non-critical code
If we do not call unlock(), the destructor will unlock the mutex
The lock is always released
```cpp
void task(const std::string& str)
{
    for(int i = 0; i < 5; i++)
    {
        std::unique_lock<std::mutex> uniq_lck(task_mutex);
        std::cout << str[0] << str[1] << str[2] << std::endl;
        uniq_lck.unlock();
        std::this_thread::sleep_for(50ms);
    }
}
```

## 27.10. std::unique_lock Constructor Optional Second Argument

```cpp
std::try_to_lock
```

Calls the mutex's try_lock() member function
The owns_lock() member function checks if the mutex is locked
```cpp
std::defer_lock
```

Does not lock the mutex
Can lock it later by calling the lock() member function
Or by passing the std::unique_lock object to std::lock()
```cpp
std::adopt_lock
```

Takes a mutex that is already locked
Avoids locking the mutex twice
std::unique_lock object can not be copied

## 27.11. std::recursive_mutex

It allows the same thread to lock it multiple times without deadlocking.
It is useful for recursive functions or complex locking scenarios.

## 27.12. std::timed_mutex

It allows threads to wait for a specified duration to acquire the lock.
It is useful for avoiding indefinite blocking.

## 27.13. std::recursive_time_mutex

It combines the features of std::recursive_mutex and std::timed_mutex.

## 27.14. std::shared_mutex

```cpp
#include <shared_mutex>
```

It can be locked in two different ways
Exclusive lock
No other thread may acquire a lock
No other thread can enter a critical section
```cpp
std::lock_guard<std::shared_mutex>
std::unique_lock<std::shared_mutex>
```

Shared lock
Other threads may acquire a shared lock
They can execute critical sections concurrently
```cpp
std::shared_lock<std::shared_mutex>
```

## 27.15. std::shared_mutex Member Functions

Exclusive locking
```cpp
lock()
try_lock()
```

unlock
Shared locking
```cpp
lock_shared()
try_lock_shared()
unlock_shared()
```

## 27.16. Deadlock avoidance

```cpp
std::scoped_lock s_lck(mutex1, mutex2);
```

## 27.17. Livelock

A program cannot make progress
In deadlock, the threads are inactive
In livelock, the threads are active
A livelock can result from badly done deadlock avoidance
A thread cannot get a lock
Instead of blocking indefinitely, it backs off and tries again

## 27.18. Thread Synchronization

## 27.19. Condition variable

```cpp
#include <conditional_variable>
std::conditional_variable
wait()
```

Takes an argument of type std::unique_lock
It unlocks its argument and blocks the thread until a notification is received
```cpp
wait_for() and wait_until()
```

Re-lock their argument if a notification is not received in time
```cpp
notify_one()
```

Wake up one of the waiting threads
The scheduler decides which thread is woken up
```cpp
notify_all()
```

Wake up all the waiting threads
```cpp
#include <iostream>
#include <thread>
#include <conditional_variable>
#include <string>
#include <chrono>
using namespace std::literals;
```

std::string sdata; // Shared data
```cpp
std::mutex mut;
std::conditional_variable cond_var;
void reader()
{
    std::cout << "Reader thread locking mutex" << std::endl;
    std::unique_lock<std::mutex> uniq_lck(mut);
    std::cout << "Reader thread locked the mutex" << std::endl;
    std::cout << "Reader thread sleeping..." << std::endl;
    cond_var.wait(uniq_lck);
    std::cout << "Reader thread wakes up" << std::endl;
    std::cout << "Data is " << sdata << std::endl;
}
void writer()
{
    std::cout << "Writer thread locking mutex" << std::endl;
    std::lock_guard<std::mutex> lck_guard(mut);
    std::cout << "Writer thread has locked the mutex" << std::endl;
    std::this_thread::sleep_for(2s);
    std::cout << "Writer thread modifying data" << std::endl;
    sdata = "Populated";
    std::cout << "Writer thread sends the notification" << std::endl;
    cond_var.notify_one();
}
int main()
{
    sdata = "Empty";
    std::cout << "Data is " << sdata << std::endl;
    std::thread read(reader);
    std::thread write(writer);
    write.join();
    read.join();
}
```

std::conditional_variable only works with std::mutex
Does not work with std::timed_mutex
There is also std::condition_variable_any
Works with any mutex-like object
Including our own types
May have more overhead than std::conditional_variable

## 27.20. Lost Wakeup

In previous example there is a problem
wait() will block until the conditional variable is notified
If the writer calls notify() before the reader calls wait()
The conditional variable is notified when there are no threads waiting
The reader will never be woken up
The reader could be blocked forever
This is known as "Lost wakeup".

## 27.21. Spurious Wakeup

The reader will be "spuriously" woken up
The reader thread has called wait()
The writer thread has not called notify()
The condition variable wakes the reader up anyway
This is due to the way that std::conditional_variable is implemented
Avoiding spurious wakeups adds too much overhead

## 27.22. Conditional Variable with Predicate

wait() takes an optional second argument
A predicate
Typically, the predicate checks a shared bool
The bool is initialized to false
It is set to true when the writer sends the notification
The reader thread will call this predicate
It will only call wait() if the predicate returns false
Also available with with_for() and wait_until()
```cpp
bool condition = false;
void reader()
{
    std::unique_lock<std::mutex> uniq_lck(mut);
    cond_var.wait(uniq_lck, [] {return condition;});
}
```

It is similar to
```cpp
while(!condition)
{
    cond_var.wait();
}
void writer()
{
    std::lock_guard<std::mutex> lck_guard(mut);
    sdata = "Populated";
    condition = true;
}
cond_var.notify_one();
```

## 27.23. std::future and std::promise

Classes for transferring data between threads
Together, these set up a "shared state" between threads
The shared state can transfer data from one thread to another
No shared data variables
No explicit locking

## 27.24. Producer-Consumer Model

Futures are promises use a producer-consumer model
Reader/Writer area an example of this model
A "Producer" thread will generate a result
A "Consumer" thread waits for the result
The Producer thread generates the result
The Producer thread stores the result in the shared state
The Consumer thread reads the result from the shared state
std::promise is associated with the producer.
std::future object is associated with the consumer.
The consumer calls a member function of the future object.
The future blocks until the result becomes available
Future and Promises also with exceptions
The promise stores the exception in the shared state
This exception will be rethrown in the consumer thread
By the future's blocking function
The producer thread "throws" the exception to the consumer

## 27.25. std::future

```cpp
#include <future>
```

get() member function
Obtains the result when ready
Blocks until the operation is complete
Fetches the result and returns it
wait() and friends
Block but do not return a result
wait() blocks until the operation is complete
wait_for() and wait_until() block with a timeout

## 27.26. std::promise

```cpp
#include <future>
```

Constructor
Creates an associated std::future object
Sets up the shared state with it
get_future() member function
Returns the associated future
```cpp
std::promise<int> prom;
std::future fut = prom.get_future();
set_value()
```

Sets the result to its argument
set_exception
Indicates that an exception has occurred
This can be stored in the shared state
```cpp
#include <future>
#include <iostream>
#include <thread>
#include <chrono>
void produce(std::promise<int> &px)
{
    using namespace std::literals;
    int x = 42;
    std::this_thread::sleep_for(2s);
    std::cout << "Promise sets shared state to " << x << std::endl;
    px.set_value(x);
}
void consume(std::future<int> &fx)
{
    std::cout << "Future calling get()... " << std::endl;
    int x = fx.get();
    std::cout << "Future returns from calling get() " << std::endl;
    std::cout << "The answer is " << x << std::endl;
}
int main()
{
    std::promise<int> prom;
    std::future<int> fut = prom.get_future();
    std::thread thr_producer(produce, std::ref(prom));
    std::thread thr_consumer(consume, std::ref(fut));
    thr_producer.join();
    the_consumer.join();
}
```

## 27.27. Atomic Type

```cpp
int counter = 0;
void task()
{
    for (int i = 0; i < 100000; i++)
    {
        std::lock_guard<std::mutex> lck_guard(mut);
        ++counter;
    }
}
```

## 27.28. Atomic Keyword

The compiler will generate special instructions which
Disable pre-fetch for count
Flush the store buffer immediately after doing the increment
This also avoids some other problems
Hardware optimizations which change the instructions order
Compiler optimizations which change the instructions order
The result is that only one thread can access count at a time
This prevents the data race
It also makes the operation take much longer
```cpp
#include<atomic>
std::atomic<int> counter = 0;
void task()
{
    for(int i = 0; i < 100000; i++)
    ++counter;
}
```

## 27.29. Member functions for Atomic Types

```cpp
store()
```

Atomically replace the object's value with its argument
```cpp
load()
```

Atomically return the object's value
operator =()
operator T()
synonyms for store() and load()
```cpp
exchange()
```

Atomically replace the object's value with its argument
Returns the previous value
Atomic pointers support arithmetic
increment and decrement operators
fetch.add() synonym for x++
fetch.sub() synonym for x--

+= and -= operators
Integer specializations have these, plus
Atomic bitwise logical operations &,| and ^

## 27.30. std::atomic_flag

std::atomic_flag is an atomic Boolean type
Has less overhead than std::atomic<bool>
Only three operations
clear() sets the flag to false
test_and_set() sets the flag to true
and returns the previous value
operator =()
Must be initialized to false
```cpp
std::atomic_flag lock = ATOMIC_FLAG_INIT;
```

## 27.31. Spin lock

A spin lock is essentially an infinite loop
It keeps "spinning" until a condition becomes true.
An alternative to locking a mutex or using a conditional variable
We can use std::atomic_flag to implement a basic spin lock
The loop condition is the value of the flag

## 27.32. Spin lock with std::atomic_flag

Each thread calls test_and_sets() in a loop
If this returns true
Some other thread has set the flag and is in the critical section
Iterate again
If it returns false
This thread has set the flag
Exit the loop and proceed into the critical section
After the critical section, set the flag to false
This allows another thread to execute in the critical section
std::atomic_flag flag = ATOMIC_FLAG_INIT; //false
```cpp
void task(int x)
{
    while(flag.test_and_set())
    {
        // Code Logic
    }
    flag.clear();
}
```

## 27.33. Lock-free programming

We will implement a simple queue
No internal or external locks
The queue is only accessed by two threads
A producer thread inserts elements into the queue
A consumer thread removes elements from the queue
The code is carefully designed
The consumer and producer threads never work on adjacent elements
The two threads always work on different parts of the queue
Only the Producer thread can modify the queue
The producer queue inserts the element
The producer queue erases the element
The two threads never overlap
iHead and iTail never refer to the same element
The Producer thread never modifies iHead
The Consumer thread never accesses elements after iHead
```cpp
template <typename T>
struct LockFreeQueue
{
private:
    std::list<T> list;
    typename std::list<T>::iterator iHead, iTail;
public:
    LockFreeQueue()
    {
        list.push_back(T()); //create a dummy element
        iHead = list.begin();
        iTail = list.end();
    }
    bool Consume(T &t)
    {
        auto iFirst = iHead;
        ++iFirst;
        if (iFirst != iTail)
        {
            iHead = iFirst;
            t = *iHead;
            return true;
        }
        return false; // no elements to fetch
    }
    void Produce(const T &t)
    {
        list.push_back(t);
        iTail = list.end();
        list.erase(list.begin(), iHead);
    }
};
int main()
{
    LockFreeQueue<int> lfq;
    std::vector<std::thread> threads;
    int j = 1;
    for (int i = 0; i < 10; i++)
    {
        std::thread produce(&LockFreeQueue<int>::Produce, &lfq, std::ref(i));
        threads.push_back(std::move(produce));
        std::thread consume(&LockFreeQueue<int>::Consume, &lfq, std::ref(i));
        threads.push_back(std::move(consume));
    }
    for (auto & thr:threads)
    thr.join();
}
```

## 27.34. Practical guidance

Concurrency allows multiple threads to make progress during overlapping periods; parallel execution is one possible outcome, not a guarantee. Shared mutable state must be synchronized.

- Unsynchronized conflicting accesses create a data race and undefined behavior.
- Protect shared invariants with a mutex and a short RAII lock such as `std::lock_guard`.
- Use atomics for appropriate independent atomic state, not as a drop-in replacement for every mutex or for multi-variable invariants.

```cpp
void increment()
{
    std::lock_guard<std::mutex> lock(mutex);
    ++counter;
}
```

Condition-variable waits should use a predicate because notifications can be missed or spuriously observed.

# 28. Ranges and Range Algorithm(C++20)

## 28.1. Range Algorithms

Legacy Algorithms
Work on iterator pairs
Range Algorithms
Work on containers directly
```cpp
auto result = std::ranges::all_of(numbers, odd);
std::ranges::for_each(numbers, print);
std::ranges::sort(numbers);
auto odd_n_position = std::ranges::find_if(numbers, odd);
if(odd_n_position != std::end(numbers))
{
    std::cout << "Found" << std::endl;
}
```

## 28.2. Constrained Iterator Pair Algorithms

Ranges can also be used with iterators
```cpp
auto result = std::ranges::all_of(numbers.begin(), numbers.end(), odd);
```

## 28.3. Projections

```cpp
std::ranges::for_each(pairs, print, &pair::first);
```

or
```cpp
std::ranges::for_each(pairs, print, [](const pair &p){ return p.first;});
```

## 28.4. Views and Range Adaptors

A view is a non-owning range
```cpp
std::vector<int> vi {1,2,3,4,5,6,7,8,9};
auto evens = [](int i) {
    return (i%2 == 0);
}
```

std::ranges::filter::view v_evens = std::ranges::filter_view(vi, evens); //No computation
print(v_evens); // Computation happens here

## 28.5. transform_view

```cpp
std::ranges::transform_view v_transformed = std::ranges::transform_view(vi, [](int i){ return i*10;});
```

## 28.6. take_view

std::ranges::take_view v_taken = std::ranges::take_view(vi, 5); // Take only 5 elements

## 28.7. take_while_view

std::ranges::take_while_view v_taken_while = std::ranges::take_view(vi, [](int i){ return (i%2 != 0);}); // Take elements as long as the predicate condition is met

## 28.8. drop_view

```cpp
std::ranges::drop_view v_drop = std::ranges::drop_view(vi, 5);
```

## 28.9. drop_while_view

```cpp
std::ranges::take_drop_view v_drop_while = std::ranges::drop_view(vi, [](int i){ return (i%2 != 0);});
```

## 28.10. Keys and Values

```cpp
using pair = std::pair<int, std::string>;
std::vector<pair> numbers {{1,"one"}, {2,"two"}};
auto k_view = std::views::keys(numbers);
auto k_values = std::views::values(numbers);
```

## 28.11. Filter Range Adaptor

```cpp
auto v_evens = std::views::filter(vi, evens);
```

## 28.12. View Composition and pipe operator

```cpp
auto even = [](int n) { return (n%2 == 0);};
auto my_view = std::views::transform(std::views::filter(vi, even), [](auto n) {return n*=n;});
std::map<std::string, int> classroom {
    {"John", 11},
    {"Mary", 17}
};
auto names_view = classroom | std::views::keys;
```

## 28.13. Range Factories

```cpp
//Generate an infinite sequence of numbers
auto infinite_view = std::views::iota(1);
std::views::iota(1,20); //20 is not included
```

or
```cpp
std::views::iota(1) | std::views::take(20)
```

## 28.14. Practical guidance

C++20 ranges let algorithms work directly with ranges and let views compose lazy transformations. A view usually does not own its elements and may perform work only as it is iterated.

- Keep the underlying range alive for as long as a non-owning view is used.
- Views can be cheap to compose, but repeated iteration may repeat the transformation or predicate work.
- Use `std::ranges` algorithms when range-based calls improve clarity; specify iterator bounds where required by the algorithm.

```cpp
std::vector<int> values{1, 2, 3, 4};
auto evens = values | std::views::filter([](int n) { return n % 2 == 0; });
```

# 29. Coroutines (C++20)

### co_yield
suspends the execution and returns a value

### co_return
completes execution and optionally returns a value

### co_await
suspends the execution until resumed

If a function has one of those keywords, it becomes a coroutine.
The below functions can't be coroutine.
Constexpr functions
Constructors
Destructors
the main function

## 29.1. co_yield

```cpp
#include <iostream>
coro[int] func1()
{
    co_yield 45;
    co_yield 46;
    co_yield 47;
    co_yield 48;
}
int main(int argc, char **argv)
{
    auto f1 = func1();
    std::cout << f1() << std::endl;	//45
    std::cout << f1() << std::endl;	//46
    std::cout << f1() << std::endl;	//47
    std::cout << f1() << std::endl;	//48
    return 0;
}
```

## 29.2. co_return

```cpp
#include <iostream>
coro[int] func3()
{
    co_return 55;
}
int main(int argc, char **argv)
{
    auto f3 = func3();
    std::cout << f3() << std::endl;
    return 0;
}
```

## 29.3. co_await

```cpp
coro[int] do_work()
{
    std::cout << "Doing first thing ... " << std::endl;
    co_await std::suspend_always{};
    std::cout << "Doing second thing ... " << std::endl;
    co_await std::suspend_always{};
    std::cout << "Doing third thing ... " << std::endl;
}
int main(int argc, char **argv)
{
    auto task = do_work();
    task.resume();
    task.resume();
    task.resume();
    std::cout << "Done!" << std::endl;
    return 0;
}
```

## 29.4. Practical guidance

A coroutine is a function whose execution can suspend and later resume. The compiler transforms it into a state machine; the return type supplies the promise type and defines how the coroutine is created, resumed, and completed.

- `co_await`, `co_yield`, and `co_return` require a suitable coroutine return type; they are not standalone replacements for ordinary return or blocking calls.
- The coroutine frame and any referenced objects must remain valid while execution is suspended.
- Choose a coroutine/task or generator abstraction whose ownership and cancellation behavior are documented.

```cpp
task<void> work()
{
    co_await some_async_operation();
    co_return;
}
```

`task<void>` and `some_async_operation()` represent library-defined types; the C++ standard does not provide one universal task type.

# 30. Modules (C++20)

```cpp
module;
#include <cstring>
export module print;
import <string>;
import <iostream>;
export void print_msg(const std::string &msg)
{
    std::cout << "Msg: " << msg << std::endl;
}
```

Module file extension
Module files(.ixx)
```cpp
BMI(ifc)
```

Implementation files(.cpp)

## 30.1. math.ixx

```cpp
module;
//Global module fragment
#include <cstring> // C function includes must show up here
#include <string>
//Module declaration
export module math_stuff;
//Module preamble
import <iostream>;
//Module purview
export double add (doube a, double b)
{
    return a+b;
}
export void greet(const std::string &name)
{
    std::string dest;
    dest = "Hello ";
    dest.append(name);
    std::cout << dest << std::endl;
}
export void print_name_length(const char *c_str_name)
{
    std::cout << "Length: " << std::strlen(c_str_name) << std::endl;
}
```

Three options for working with modules
Include translation
Header importation
Module importation

## 30.2. main.cpp

```cpp
import <iostream>
import math_stuff;
int main()
{
    auto result = add(10,20);
    std::cout << "Result: " << result << std::endl;
    greet("John");
    print_name_length("John");
    return 0;
}
```

## 30.3. Practical guidance

C++20 modules provide named module interfaces that can be imported. Unlike textual inclusion, a module interface is compiled as a module unit and its exported declarations form the importable API.

- `export module name;` declares a named module; `export` marks declarations that importers may use.
- Keep implementation-only declarations unexported and avoid assuming that every compiler or build system has identical module support.
- A module interface must be built before translation units that import it; configure the build system to express that dependency.

```cpp
export module geometry;

export int double_value(int value)
{
    return value * 2;
}
```

An importing translation unit uses `import geometry;` rather than including this interface as a header.