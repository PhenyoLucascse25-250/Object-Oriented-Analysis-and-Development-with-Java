import java.util.Date;

public abstract class Account {
    private String accountNumber;
    protected double balance;
    private String branch;
    private Date dateOpened;
    private String customerID;   // added: owner of this account

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

    public String getAccountNumber() {
        return accountNumber;
    }

    // added: getters the database class needs to read every field
    public String getBranch() {
        return branch;
    }

    public Date getDateOpened() {
        return dateOpened;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " - " + accountNumber;
    }
}