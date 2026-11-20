import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class AccountController {
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final AccountDAO accountDAO = new AccountDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();
    private final String processedBy = "admin";
    public AccountController() {
        addSampleDataIfEmpty();
    }
    private void addSampleDataIfEmpty() {
        if (!accountDAO.findAll().isEmpty()) { return; }
        customerDAO.save(new Customer("C001", "Phenyo", "Lucas",
                "71234567", "123456", "phenyo@test.com"));
        saveSample(new ChequeAccount("CH001", 1000, "The Mall Branch",
                new Date(), "ABC Ltd", "Gaborone", 500));
        saveSample(new SavingsAccount("SA001", 1000, "The Mall Branch",
                new Date(), 0.05));
        saveSample(new InvestmentAccount("IN001", 5000, "The Mall Branch",
                new Date(), 5, 500));
    }
    private void saveSample(Account acc) {
        acc.setCustomerID("C001");
        accountDAO.save(acc);
    }
    // ----- things the View can ask for (all answers are text) ----
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
    // ----- things the View can ask us to do ----
    public String deposit(String accNo, String amountText) {
        Double amount = parseAmount(amountText);
        if (amount == null) { return "Please type a number, like 250."; }
        if (amount <= 0) { return "Amount must be more than 0."; }
        try {
            Account acc = accountDAO.findByNumber(accNo);
            if (acc == null) { return "Account not found."; }
            acc.deposit(amount);                       // model does the maths
            accountDAO.updateBalance(acc);             // DAO saves the balance
            transactionDAO.save(accNo, "Deposit", amount,
                    acc.getBalance(), processedBy);    // DAO saves the history
            return "Deposit successful.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
    public String withdraw(String accNo, String amountText) {
        Double amount = parseAmount(amountText);
        if (amount == null) { return "Please type a number, like 250."; }
        if (amount <= 0) { return "Amount must be more than 0."; }
        try {
            Account acc = accountDAO.findByNumber(accNo);
            if (acc == null) { return "Account not found."; }
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
            transactionDAO.save(accNo, "Withdrawal", amount,
                    acc.getBalance(), processedBy);
            return "Withdrawal successful.";
        } catch (RuntimeException e) {
            return e.getMessage();
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
            transactionDAO.save(accNo, "Interest", acc.getBalance() - before,
                    acc.getBalance(), processedBy);
            return "Interest paid.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
    // ----- private helper ----
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