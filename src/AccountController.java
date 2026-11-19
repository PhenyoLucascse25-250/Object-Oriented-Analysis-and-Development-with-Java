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
    public List<Account> getAccounts() {
        return accounts;
    }
    public List<String> getHistory(Account acc) {
        return history.get(acc.getAccountNumber());
    }
    public String deposit(Account acc, double amount) {
        if (amount <= 0) {
            return "Amount must be more than 0.";
        }
        acc.deposit(amount);
        record(acc, "Deposit", amount);
        return "Deposit successful.";
    }

    public String withdraw(Account acc, double amount) {
        if (amount <= 0) {
            return "Amount must be more than 0.";
        }
        boolean ok;
        if (acc instanceof ChequeAccount) {
            ok = ((ChequeAccount) acc).withdraw(amount);
        } else if (acc instanceof InvestmentAccount) {
            ok = ((InvestmentAccount) acc).withdraw(amount);
        } else {
            return "Savings accounts do not allow withdrawals.";
        }
        if (ok) {
            record(acc, "Withdrawal", amount);
            return "Withdrawal successful.";
        }
        return "Withdrawal failed. Not enough money.";
    }
    public String payInterest(Account acc) {
        if (acc instanceof InterestBearing) {
            double before = acc.getBalance();
            ((InterestBearing) acc).payInterest();
            record(acc, "Interest", acc.getBalance() - before);
            return "Interest paid.";
        }
        return "This account does not earn interest.";
    }
    private void record(Account acc, String type, double amount) {
        String line = new Date() + " | " + type
                + " | BWP " + String.format("%.2f", amount)
                + " | Balance: BWP " + String.format("%.2f", acc.getBalance());
        history.get(acc.getAccountNumber()).add(line);
    }
}