import java.util.Date;

public class SavingsAccount extends Account implements InterestBearing {
    private double interestRate;

    public SavingsAccount(String accountNumber, double balance, String branch, Date dateOpened, double interestRate) {
        super(accountNumber, balance, branch, dateOpened);
        this.interestRate = interestRate;
    }

    public void payInterest() {
        balance = balance + calculateInterestRate();
    }

    public double calculateInterestRate() {
        return balance * interestRate / 100;
    }

    // added: getter for the database class
    public double getInterestRate() {
        return interestRate;
    }
}