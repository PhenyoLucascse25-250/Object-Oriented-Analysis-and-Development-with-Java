import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DatabaseConnection {
    // Saves to a file called banking_db.mv.db in the project folder
    private static final String URL =
            "jdbc:h2:./banking_db;INIT=RUNSCRIPT FROM 'schema.sql'";
    private static final String USER = "sa";
    private static final String PASSWORD = "";
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}