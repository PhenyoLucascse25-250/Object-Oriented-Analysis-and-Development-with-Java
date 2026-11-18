import java.util.Date;

public abstract class Account {
    private String accountNumber;
    protected double balance;
    private String branch;
    private Date dateOpened;

    public Account(String accountNumber, double balance, String branch, Date dateOpened) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.branch = branch;
        this.dateOpened = dateOpened;
    }

    public void deposit(double amount) {
        balance = balance + amount;
    }

    public double getBalance() {
        return balance;
    }

    public double calculateInterestRate() {
        return 0;
    }
}