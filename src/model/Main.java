import java.util.Date;

public class Main {
    public static void main(String[] args) {
        Customer cust = new Customer("C001", "Phenyo", "Lucas", "71234567", "123456", "phenyo@test.com");
        System.out.println("Customer ID: " + cust.getCustomerID());

        ChequeAccount cheque = new ChequeAccount("CH001", 1000, "Gaborone", new Date(), "ABC Ltd", "Gaborone", 500);
        cheque.deposit(500);
        System.out.println("Cheque balance: " + cheque.getBalance());
        System.out.println("Cheque withdraw 2000: " + cheque.withdraw(2000));
        System.out.println("Cheque balance after: " + cheque.getBalance());

        SavingsAccount savings = new SavingsAccount("SA001", 1000, "Gaborone", new Date(), 5);
        savings.payInterest();
        System.out.println("Savings balance after interest: " + savings.getBalance());

        InvestmentAccount invest = new InvestmentAccount("IN001", 5000, "Gaborone", new Date(), 10, 1000);
        invest.payInterest();
        System.out.println("Investment balance after interest: " + invest.getBalance());
        System.out.println("Investment withdraw 4500: " + invest.withdraw(4500));
    }
}