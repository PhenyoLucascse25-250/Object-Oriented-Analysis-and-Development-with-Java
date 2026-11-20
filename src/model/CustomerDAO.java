import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class CustomerDAO {
    // CREATE
    public void save(Customer c) {
        String sql = "INSERT INTO customer (customer_id, first_name, last_name, "
                + "phone, id_number, email) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getCustomerID());
            ps.setString(2, c.getFirstName());
            ps.setString(3, c.getLastName());
            ps.setString(4, c.getPhone());
            ps.setString(5, c.getIdNumber());
            ps.setString(6, c.getEmail());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }
    // READ one
    public Customer findById(String customerID) {
        String sql = "SELECT * FROM customer WHERE customer_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, customerID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) { return buildCustomer(rs); }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }
    // READ all
    public List<Customer> findAll() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customer ORDER BY customer_id";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) { list.add(buildCustomer(rs)); }
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
        return list;
    }
    // UPDATE
    public void update(Customer c) {
        String sql = "UPDATE customer SET first_name = ?, last_name = ?, "
                + "phone = ?, id_number = ?, email = ? WHERE customer_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getFirstName());
            ps.setString(2, c.getLastName());
            ps.setString(3, c.getPhone());
            ps.setString(4, c.getIdNumber());
            ps.setString(5, c.getEmail());
            ps.setString(6, c.getCustomerID());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }
    // DELETE
    public void delete(String customerID) {
        String sql = "DELETE FROM customer WHERE customer_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, customerID);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }
    // Turns one database row into a Customer object
    private Customer buildCustomer(ResultSet rs) throws SQLException {
        return new Customer(
                rs.getString("customer_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("phone"),
                rs.getString("id_number"),
                rs.getString("email"));
    }
}