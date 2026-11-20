import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class AccountController {
    private final List<Account> accounts = new ArrayList<>();
    private final Map<String, List<String>> history = new HashMap<>();
    public AccountController() {
        addAccount(new ChequeAccount("CH001", 1000, "The Mall Branch",
                new Date(), "ABC Ltd", "Gaborone", 500));
        addAccount(new SavingsAccount("SA001", 1000, "The Mall Branch",
                new Date(), 0.05));
        addAccount(new InvestmentAccount("IN001", 5000, "The Mall Branch",
                new Date(), 5, 500));
    }
    private void addAccount(Account acc) {
        accounts.add(acc);
        history.put(acc.getAccountNumber(), new ArrayList<>());
    }
   
    public List<String> getAccountNumbers() {
        List<String> numbers = new ArrayList<>();
        for (Account acc : accounts) {
            numbers.add(acc.getAccountNumber());
        }
        return numbers;
    }
    public String getAccountType(String accNo) {
        Account acc = find(accNo);
        return (acc == null) ? "Unknown" : acc.getClass().getSimpleName();
    }
    public String getBalanceText(String accNo) {
        Account acc = find(accNo);
        if (acc == null) { return "Unknown"; }
        return "BWP " + String.format("%.2f", acc.getBalance());
    }
    public List<String> getHistory(String accNo) {
        List<String> list = history.get(accNo);
        return (list == null) ? new ArrayList<>() : list;
    }
    
    public String deposit(String accNo, String amountText) {
        Account acc = find(accNo);
        if (acc == null) { return "Account not found."; }
        Double amount = parseAmount(amountText);
        if (amount == null) { return "Please type a number, like 250."; }
        if (amount <= 0) { return "Amount must be more than 0."; }
        acc.deposit(amount);
        record(acc, "Deposit", amount);
        return "Deposit successful.";
    }
    public String withdraw(String accNo, String amountText) {
        Account acc = find(accNo);
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
        record(acc, "Withdrawal", amount);
        return "Withdrawal successful.";
    }
    public String payInterest(String accNo) {
        Account acc = find(accNo);
        if (acc == null) { return "Account not found."; }
        if (!(acc instanceof InterestBearing)) {
            return "This account does not earn interest.";
        }
        double before = acc.getBalance();
        ((InterestBearing) acc).payInterest();
        record(acc, "Interest", acc.getBalance() - before);
        return "Interest paid.";
    }
  
    private Account find(String accNo) {
        for (Account acc : accounts) {
            if (acc.getAccountNumber().equals(accNo)) { return acc; }
        }
        return null;
    }
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
    private void record(Account acc, String type, double amount) {
        String line = new Date() + " | " + type
                + " | BWP " + String.format("%.2f", amount)
                + " | Balance: BWP " + String.format("%.2f", acc.getBalance());
        history.get(acc.getAccountNumber()).add(line);
    }
}