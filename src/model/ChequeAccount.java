import java.util.Date;

public class ChequeAccount extends Account {
    private String employerName;
    private String employerAddress;
    private double overdraftLimit;

    public ChequeAccount(String accountNumber, double balance, String branch, Date dateOpened, String employerName, String employerAddress, double overdraftLimit) {
        super(accountNumber, balance, branch, dateOpened);
        this.employerName = employerName;
        this.employerAddress = employerAddress;
        this.overdraftLimit = overdraftLimit;
    }

    public boolean withdraw(double amount) {
        if (amount <= balance + overdraftLimit) {
            balance = balance - amount;
            return true;
        }
        return false;
    }

    // added: getters for the database class
    public String getEmployerName() {
        return employerName;
    }

    public String getEmployerAddress() {
        return employerAddress;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }
}