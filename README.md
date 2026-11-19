# Banking System GUI

CSE202 Object Oriented Analysis and Design with Java
Part B, Section 5: GUI Design and Implementation (JavaFX)

## Requirements

- Java 17 or newer (JDK)
- Maven

## How to run

From the project folder (the one with pom.xml):

    mvn javafx:run

The first run is slow because Maven downloads JavaFX.

## Test login

- Username: admin
- Password: 1234

## Structure

- Boundary classes: LoginView, AccountView (screens only, no business logic)
- Controllers: LoginController, AccountController (all rules and checks)
- Model: Account, SavingsAccount, InvestmentAccount, ChequeAccount, Customer, InterestBearing

## Sample accounts

- CH001: Cheque account
- SA001: Savings account
- IN001: Investment account