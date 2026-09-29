# Bank Management App

A Java CLI banking application with PostgreSQL database integration using Supabase.

## Features

- Create users
- Create accounts
- Deposit money
- Withdraw money
- Transfer money
- Currency conversion between AZN, USD, EUR
- Transaction history
- PostgreSQL database integration

## Technologies

- Java
- Maven
- JDBC
- PostgreSQL
- Supabase
- IntelliJ IDEA

<img width="1130" height="850" alt="diagram-export-9-29-2026-6_32_19-PM" src="https://github.com/user-attachments/assets/668de990-3dc8-4196-b5a4-755a1c8c693d" />

## What I Learned

- In real-world projects, `int` and `double` aren't used for money, because they can't represent fractional amounts like cents precisely. `BigDecimal` is used instead.
- `Optional` is used when a method might return `null`. It has built-in methods for handling the empty case without null checks.
- Enums are helpful for keeping user input consistent. For example, the user picks a bank account type with a button instead of typing its full name.
- `LocalDate` is a better choice than strings for handling dates. (For date and time together, there is `LocalDateTime`.)
- It's good practice to declare variables with the interface type, e.g. `List` instead of `ArrayList`.
- I learned about N-Tier (Layered) Architecture, where classes are split into packages by responsibility, such as model, view, controller, and repository.
- I applied my OOP knowledge in this project: access modifiers for encapsulation, abstract classes and interfaces (repositories) for abstraction, and parent-child class relationships for inheritance.
- Instead of random strings or integers, I used `UUID` to generate unique IDs consistently.
- I used AI tools (mostly ChatGPT and Gemini) to learn topics I had no experience with, such as working with `Optional`, `BigDecimal`, and connecting my project to a database.
- I wrote custom exceptions for specific situations.
- I was reminded that calling `put` on a `HashMap` with an existing key doesn't create a duplicate. It overwrites the old value, because a map can't contain two identical keys.

## Getting Started

1. Clone the repository
2. Create a Supabase project and set up the database tables
3. Add your database credentials (URL, user, password) to the config
4. Run the app with Maven
