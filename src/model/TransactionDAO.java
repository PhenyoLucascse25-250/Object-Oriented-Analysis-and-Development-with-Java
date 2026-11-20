import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    // CREATE
    public void save(String accountNumber, String type, double amount,
                     double balanceAfter, String processedBy) {
        String sql = "INSERT INTO transactions (account_number, txn_type, amount, "
                + "txn_time, balance_after, processed_by) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            ps.setString(2, type);
            ps.setDouble(3, amount);
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            ps.setDouble(5, balanceAfter);
            ps.setString(6, processedBy);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }

    // READ: one text line per transaction, oldest first
    public List<String> findByAccount(String accountNumber) {
        List<String> lines = new ArrayList<>();
        String sql = "SELECT txn_type, amount, txn_time, balance_after, processed_by "
                + "FROM transactions WHERE account_number = ? ORDER BY transaction_id";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lines.add(rs.getTimestamp("txn_time")
                            + " | " + rs.getString("txn_type")
                            + " | BWP " + String.format("%.2f", rs.getDouble("amount"))
                            + " | Balance: BWP "
                            + String.format("%.2f", rs.getDouble("balance_after"))
                            + " | by " + rs.getString("processed_by"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
        return lines;
    }
}