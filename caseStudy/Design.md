# Bank Account Management System — Design Document

## 1. Project Overview

A Java Swing desktop application for managing bank accounts. It supports creating Savings and Checking accounts, making deposits and withdrawals, calculating interest, and saving/loading account data to and from a text file.

## 2. UML Class Diagram

See `UML_Class_Diagram.png` in this folder for the rendered diagram. A text version is included below for reference/backup.

```
                 <<abstract>>
                 BankAccount
   -------------------------------------
   - accountNumber : String
   - accountHolder : String
   - balance : double
   -------------------------------------
   + BankAccount(number, holder)
   + BankAccount(number, holder, balance)
   + deposit(amount)
   + deposit(amount, note)
   + withdraw(amount) throws InsufficientFundsException
   + calculateInterest() : double     <<abstract>>
   + getAccountType() : String        <<abstract>>
   + toString() : String              <<override of Object>>
                     △
                     │ extends (inheritance)
        ┌────────────┴────────────┐
        │                         │
 SavingsAccount             CheckingAccount
 ─────────────────          ─────────────────
 - interestRate : double    - overdraftLimit : double
 ─────────────────          ─────────────────
 + calculateInterest()      + calculateInterest()  (always returns 0)
   override                   override
 + getAccountType()         + getAccountType()
   override                   override
 + withdraw(amount)         + withdraw(amount)
   override (enforces         override (allows overdraft
   $100 minimum balance)       up to the set limit)


        Bank                         InsufficientFundsException
 ─────────────────                   ─────────────────────────
 - accounts : List<BankAccount>      extends Exception
 ─────────────────
 + addAccount(BankAccount)
 + findAccount(String)               (method overload)
 + findAccount(String, boolean)      (method overload)
 + saveToFile() / loadFromFile()     (file I/O)


        BankGUI  (extends JFrame)
 ─────────────────────────────────
 Holds a Bank object; provides a form, buttons,
 and a JTable to list accounts. Calls Bank / BankAccount
 methods to perform create / deposit / withdraw / query operations.


        BankAccountTester  (has a main method - run separately from BankGUI)
 ─────────────────────────────────
 Console test harness. Creates accounts, calls their methods with
 valid data, invalid data, and a combination of both, and prints
 [PASS]/[FAIL] for each check plus a summary count.
```

## 3. Where Each Required Concept Appears in the Code

| Concept | Location in Code |
|---|---|
| **Class / Object** | `BankAccount`, `SavingsAccount`, `CheckingAccount`, `Bank`, and `BankGUI` are all classes; instances such as `new SavingsAccount(...)` created inside `BankGUI` are objects |
| **Encapsulation** | All fields in `BankAccount` are `private` and can only be accessed through methods such as `getAccountNumber()`, `getBalance()`, `setBalance()` |
| **Inheritance** | `SavingsAccount extends BankAccount` and `CheckingAccount extends BankAccount` |
| **Overriding** | Both subclasses override `calculateInterest()`, `getAccountType()`, and `withdraw()`; `BankAccount` overrides `Object.toString()` |
| **Overloading** | `BankAccount` has two constructors and two `deposit()` methods; `Bank` has two `findAccount()` methods (search by account number vs. by holder name) |
| **Polymorphism** | `Bank.getAccounts()` stores both subclass types in a single `List<BankAccount>`; when `BankGUI` calls `acc.calculateInterest()` or `acc.withdraw()`, the method that actually runs depends on the object's runtime type (Savings or Checking) |
| **File I/O** | `Bank.saveToFile()` / `Bank.loadFromFile()` use `BufferedWriter` / `BufferedReader` to write and read account data to/from `accounts.txt` |
| **GUI** | `BankGUI` implements a full Swing interface (`JFrame`, `JTable`, `JTextField`, `JButton`, `JOptionPane`, etc.) |
| **Exception Handling (CLR 7)** | Custom checked exception `InsufficientFundsException`; `BankGUI` uses `try/catch` to handle `NumberFormatException`, `IllegalArgumentException`, and `InsufficientFundsException`, showing an error dialog to the user |
| **Generic Collection (CLR 6)** | `Bank` manages its accounts internally with `List<BankAccount>` (an `ArrayList`) |
| **Testing with valid / invalid / combined data (CLR 9, CLR 10)** | `BankAccountTester.java` runs four groups of checks: valid data only, invalid data only, a combination of both, and a file I/O + polymorphism round-trip. Each check prints `[PASS]`/`[FAIL]` plus a final summary count |

## 4. Key Business Rules

- **SavingsAccount**: earns interest; `calculateInterest()` = balance × interestRate. A withdrawal that would drop the balance below $100 throws `InsufficientFundsException`.
- **CheckingAccount**: earns no interest (`calculateInterest()` always returns 0). Overdraft is allowed, but the balance cannot go below `-overdraftLimit`.
- **Deposit**: the amount must be positive, otherwise an `IllegalArgumentException` (unchecked) is thrown.

## 5. File Structure

```
BankAccountManagementSystem/
├── UML_Class_Diagram.png                 rendered class diagram
├── Design.md                             design document
└── src/
    └── bankapp/
        ├── BankAccount.java              abstract base class
        ├── SavingsAccount.java           savings account subclass
        ├── CheckingAccount.java          checking account subclass
        ├── InsufficientFundsException.java  custom exception
        ├── Bank.java                     account manager + file I/O
        ├── BankGUI.java                  Swing GUI (contains main method)
        └── BankAccountTester.java        console test harness (contains its own main method)
```

## 6. Running the Project in Eclipse

1. Open Eclipse → File → New → Java Project (any project name works, e.g. `BankAccountManagementSystem`).
2. Copy/import the entire `src/bankapp` folder into the new project's `src` directory (keep the package name `bankapp` unchanged).
3. Right-click `BankGUI.java` → Run As → Java Application.
4. Once the window opens:
   - Fill in the account number, holder name, type (Savings/Checking), balance, and interest rate/overdraft limit, then click **Create Account**.
   - Select a row in the table, enter an amount, then click **Deposit** / **Withdraw** / **Calc Interest**.
   - Clicking **Save to File** writes `accounts.txt` to the project's root folder; **Load from File** reads it back.
   - Try invalid input (e.g., a withdrawal exceeding the overdraft limit, or a non-numeric amount) to trigger the exception-handling dialogs — useful for screenshots demonstrating invalid-data testing (CLR 10).
5. To run the automated tests, right-click `BankAccountTester.java` → Run As → Java Application. The console will print `[PASS]`/`[FAIL]` for each test case (valid data, invalid data, combined data, and file I/O) plus a summary line.
