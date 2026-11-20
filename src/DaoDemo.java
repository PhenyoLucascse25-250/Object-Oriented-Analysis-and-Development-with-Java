import java.util.Date;

public class DaoDemo {
    public static void main(String[] args) {
        CustomerDAO customerDAO = new CustomerDAO();
        AccountDAO accountDAO = new AccountDAO();

        // CREATE
        customerDAO.save(new Customer("C999", "Test", "User",
                "70000000", "999999", "test@test.com"));
        SavingsAccount acc = new SavingsAccount("TEST01", 200,
                "The Mall Branch", new Date(), 0.05);
        acc.setCustomerID("C999");
        accountDAO.save(acc);
        System.out.println("CREATE done.");

        // READ
        Account found = accountDAO.findByNumber("TEST01");
        System.out.println("READ: " + found + ", balance " + found.getBalance());

        // UPDATE
        found.deposit(300);
        accountDAO.updateBalance(found);
        System.out.println("UPDATE: new balance "
                + accountDAO.findByNumber("TEST01").getBalance());

        // DELETE
        accountDAO.delete("TEST01");
        customerDAO.delete("C999");
        System.out.println("DELETE: account is now "
                + accountDAO.findByNumber("TEST01"));
    }
}