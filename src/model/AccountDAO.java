import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class AccountDAO {
    // CREATE
    public void save(Account acc) {
        String sql = "INSERT INTO account (account_number, account_type, branch, "
                + "date_opened, balance, interest_rate, minimum_deposit, "
                + "employer_name, employer_address, overdraft_limit, customer_id) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String type;
        Double interestRate = null;
        Double minimumDeposit = null;
        String employerName = null;
        String employerAddress = null;
        Double overdraftLimit = null;
        if (acc instanceof SavingsAccount) {
            type = "SAVINGS";
            interestRate = ((SavingsAccount) acc).getInterestRate();
        } else if (acc instanceof InvestmentAccount) {
            type = "INVESTMENT";
            interestRate = ((InvestmentAccount) acc).getInterestRate();
            minimumDeposit = ((InvestmentAccount) acc).getMinimumDeposit();
        } else {
            type = "CHEQUE";
            employerName = ((ChequeAccount) acc).getEmployerName();
            employerAddress = ((ChequeAccount) acc).getEmployerAddress();
            overdraftLimit = ((ChequeAccount) acc).getOverdraftLimit();
        }
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, acc.getAccountNumber());
            ps.setString(2, type);
            ps.setString(3, acc.getBranch());
            ps.setDate(4, new java.sql.Date(acc.getDateOpened().getTime()));
            ps.setDouble(5, acc.getBalance());
            ps.setObject(6, interestRate, Types.DOUBLE);
            ps.setObject(7, minimumDeposit, Types.DOUBLE);
            ps.setObject(8, employerName, Types.VARCHAR);
            ps.setObject(9, employerAddress, Types.VARCHAR);
            ps.setObject(10, overdraftLimit, Types.DOUBLE);
            ps.setString(11, acc.getCustomerID());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }
    // READ one
    public Account findByNumber(String accountNumber) {
        String sql = "SELECT * FROM account WHERE account_number = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) { return buildAccount(rs); }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }
    // READ all
    public List<Account> findAll() {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT * FROM account ORDER BY account_number";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) { list.add(buildAccount(rs)); }
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
        return list;
    }
    // UPDATE (the balance is the thing that changes)
    public void updateBalance(Account acc) {
        String sql = "UPDATE account SET balance = ? WHERE account_number = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, acc.getBalance());
            ps.setString(2, acc.getAccountNumber());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }
    // DELETE (first its transactions, then the account)
    public void delete(String accountNumber) {
        try (Connection con = DatabaseConnection.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM transactions WHERE account_number = ?")) {
                ps.setString(1, accountNumber);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM account WHERE account_number = ?")) {

                ps.setString(1, accountNumber);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }
    // Turns one database row into the right kind of Account object
    private Account buildAccount(ResultSet rs) throws SQLException {
        String type = rs.getString("account_type");
        String number = rs.getString("account_number");
        double balance = rs.getDouble("balance");
        String branch = rs.getString("branch");
        Date opened = rs.getDate("date_opened");
        Account acc;
        if (type.equals("SAVINGS")) {
            acc = new SavingsAccount(number, balance, branch, opened,
                    rs.getDouble("interest_rate"));
        } else if (type.equals("INVESTMENT")) {
            acc = new InvestmentAccount(number, balance, branch, opened,
                    rs.getDouble("interest_rate"),
                    rs.getDouble("minimum_deposit"));
        } else {
            acc = new ChequeAccount(number, balance, branch, opened,
                    rs.getString("employer_name"),
                    rs.getString("employer_address"),
                    rs.getDouble("overdraft_limit"));
        }
        acc.setCustomerID(rs.getString("customer_id"));
        return acc;
    }
}