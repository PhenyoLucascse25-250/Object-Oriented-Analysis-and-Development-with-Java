import java.util.Date;

public class InvestmentAccount extends Account implements InterestBearing {
    private double interestRate;
    private double minimumDeposit;

    public InvestmentAccount(String accountNumber, double balance, String branch, Date dateOpened, double interestRate, double minimumDeposit) {
        super(accountNumber, balance, branch, dateOpened);
        this.interestRate = interestRate;
        this.minimumDeposit = minimumDeposit;
    }

    public void payInterest() {
        balance = balance + calculateInterestRate();
    }

    public boolean withdraw(double amount) {
        if (balance - amount >= minimumDeposit) {
            balance = balance - amount;
            return true;
        }
        return false;
    }

    public double calculateInterestRate() {
        return balance * interestRate / 100;
    }

    // added: getters for the database class
    public double getInterestRate() {
        return interestRate;
    }

    public double getMinimumDeposit() {
        return minimumDeposit;
    }
}