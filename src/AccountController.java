import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Controller: applies the rules, asks the model to do the money maths,
// and saves the result through the DAOs. No JavaFX and no SQL in here.
public class AccountController {
    private static final String USER = "admin";   // the logged-in user

    private final AccountDAO accountDAO = new AccountDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();

    public AccountController() {
        seedIfEmpty();
    }

    // Sample data, inserted only the first time (empty database)
    private void seedIfEmpty() {
        if (!accountDAO.findAll().isEmpty()) { return; }
        if (customerDAO.findById("C001") == null) {
            customerDAO.save(new Customer("C001", "Sample", "Customer",
                    "70000000", "123456789", "sample@example.com"));
        }
        ChequeAccount ch = new ChequeAccount("CH001", 1000, "The Mall Branch",
                new Date(), "ABC Ltd", "Gaborone", 500);
        SavingsAccount sa = new SavingsAccount("SA001", 1000, "The Mall Branch",
                new Date(), 0.05);
        InvestmentAccount in = new InvestmentAccount("IN001", 5000, "The Mall Branch",
                new Date(), 5, 500);
        ch.setCustomerID("C001");
        sa.setCustomerID("C001");
        in.setCustomerID("C001");
        accountDAO.save(ch);
        accountDAO.save(sa);
        accountDAO.save(in);
    }

    // ----- things the View can ask for (all answers are text) -----
    public List<String> getAccountNumbers() {
        List<String> numbers = new ArrayList<>();
        for (Account acc : accountDAO.findAll()) {
            numbers.add(acc.getAccountNumber());
        }
        return numbers;
    }

    public String getAccountType(String accNo) {
        Account acc = accountDAO.findByNumber(accNo);
        return (acc == null) ? "Unknown" : acc.getClass().getSimpleName();
    }

    public String getBalanceText(String accNo) {
        Account acc = accountDAO.findByNumber(accNo);
        if (acc == null) { return "Unknown"; }
        return "BWP " + String.format("%.2f", acc.getBalance());
    }

    public List<String> getHistory(String accNo) {
        return transactionDAO.findByAccount(accNo);
    }

    // ----- things the View can ask us to do -----
    public String deposit(String accNo, String amountText) {
        try {
            Account acc = accountDAO.findByNumber(accNo);
            if (acc == null) { return "Account not found."; }
            Double amount = parseAmount(amountText);
            if (amount == null) { return "Please type a number, like 250."; }
            if (amount <= 0) { return "Amount must be more than 0."; }

            acc.deposit(amount);                         // model does the maths
            accountDAO.updateBalance(acc);               // DAO saves the balance
            transactionDAO.save(accNo, "DEPOSIT", amount, acc.getBalance(), USER);
            return "Deposit successful.";
        } catch (RuntimeException e) {
            return "Database problem. Please try again.";
        }
    }

    public String withdraw(String accNo, String amountText) {
        try {
            Account acc = accountDAO.findByNumber(accNo);
            if (acc == null) { return "Account not found."; }
            Double amount = parseAmount(amountText);
            if (amount == null) { return "Please type a number, like 250."; }
            if (amount <= 0) { return "Amount must be more than 0."; }

            boolean ok;
            if (acc instanceof ChequeAccount) {
                ok = ((ChequeAccount) acc).withdraw(amount);
            } else if (acc instanceof InvestmentAccount) {
                ok = ((InvestmentAccount) acc).withdraw(amount);
            } else {
                return "Savings accounts do not allow withdrawals.";
            }
            if (!ok) { return "Withdrawal failed. Not enough money."; }

            accountDAO.updateBalance(acc);
            transactionDAO.save(accNo, "WITHDRAWAL", amount, acc.getBalance(), USER);
            return "Withdrawal successful.";
        } catch (RuntimeException e) {
            return "Database problem. Please try again.";
        }
    }

    public String payInterest(String accNo) {
        try {
            Account acc = accountDAO.findByNumber(accNo);
            if (acc == null) { return "Account not found."; }
            if (!(acc instanceof InterestBearing)) {
                return "This account does not earn interest.";
            }
            double before = acc.getBalance();
            ((InterestBearing) acc).payInterest();
            accountDAO.updateBalance(acc);
            transactionDAO.save(accNo, "INTEREST", acc.getBalance() - before,
                    acc.getBalance(), USER);
            return "Interest paid.";
        } catch (RuntimeException e) {
            return "Database problem. Please try again.";
        }
    }

    // ----- private helper -----
    private Double parseAmount(String text) {
        if (text == null) { return null; }
        try {
            double value = Double.parseDouble(text.trim());
            if (Double.isNaN(value) || Double.isInfinite(value)) { return null; }
            return value;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}