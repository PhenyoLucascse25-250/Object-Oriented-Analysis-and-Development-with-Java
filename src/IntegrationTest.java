public class IntegrationTest {
    private static int passed = 0;
    private static int failed = 0;

    private static void check(String name, boolean condition) {
        if (condition) { passed++; System.out.println("PASS  " + name); }
        else { failed++; System.out.println("FAIL  " + name); }
    }

    private static boolean near(double a, double b) {
        return Math.abs(a - b) < 0.001;
    }

    public static void main(String[] args) {
        // ---- Boundary entry: login (the GUI calls these same methods) ----
        LoginController login = new LoginController();
        check("T1 wrong password is rejected", !login.login("admin", "wrong"));
        check("T1 wrong password gives a message",
                login.getMessage().equals("Wrong username or password."));
        check("T2 correct login accepted", login.login("admin", "1234"));

        // ---- Controller + DAO + database ----
        AccountController controller = new AccountController();
        AccountDAO checkDAO = new AccountDAO();            // independent reader
        TransactionDAO txnDAO = new TransactionDAO();

        check("T3 sample accounts loaded from database",
                controller.getAccountNumbers().contains("CH001")
                && controller.getAccountNumbers().contains("SA001")
                && controller.getAccountNumbers().contains("IN001"));

        // ---- Full flow: deposit ----
        double before = checkDAO.findByNumber("CH001").getBalance();
        int txnBefore = txnDAO.findByAccount("CH001").size();
        String msg = controller.deposit("CH001", "500");
        check("T4 deposit message", msg.equals("Deposit successful."));
        check("T4 database balance increased by 500",
                near(checkDAO.findByNumber("CH001").getBalance(), before + 500));
        check("T4 transaction row saved",
                txnDAO.findByAccount("CH001").size() == txnBefore + 1);

        // ---- Validation (nothing may change in the database) ----
        double afterDeposit = checkDAO.findByNumber("CH001").getBalance();
        check("T5 text amount rejected",
                controller.deposit("CH001", "abc").equals("Please type a number, like 250."));
        check("T5 negative amount rejected",
                controller.deposit("CH001", "-5").equals("Amount must be more than 0."));
        check("T5 database unchanged after bad input",
                near(checkDAO.findByNumber("CH001").getBalance(), afterDeposit));

        // ---- Withdrawal rules ----
        check("T6 cheque withdrawal succeeds",
                controller.withdraw("CH001", "100").equals("Withdrawal successful."));
        check("T6 database balance reduced by 100",
                near(checkDAO.findByNumber("CH001").getBalance(), afterDeposit - 100));
        check("T7 savings withdrawal blocked",
                controller.withdraw("SA001", "50")
                        .equals("Savings accounts do not allow withdrawals."));

        // ---- Interest (rules from the model) ----
        double sBefore = checkDAO.findByNumber("SA001").getBalance();
        check("T8 savings interest paid",
                controller.payInterest("SA001").equals("Interest paid."));
        check("T8 savings balance +0.05% in database",
                near(checkDAO.findByNumber("SA001").getBalance(), sBefore * 1.0005));

        double iBefore = checkDAO.findByNumber("IN001").getBalance();
        controller.payInterest("IN001");
        check("T9 investment balance +5% in database",
                near(checkDAO.findByNumber("IN001").getBalance(), iBefore * 1.05));

        check("T10 cheque account earns no interest",
                controller.payInterest("CH001").equals("This account does not earn interest."));

        // ---- Persistence: a new controller (like restarting the app) ----
        double saved = checkDAO.findByNumber("CH001").getBalance();
        AccountController restarted = new AccountController();
        check("T11 balance survives a restart",
                restarted.getBalanceText("CH001")
                        .equals("BWP " + String.format("%.2f", saved)));
        check("T11 history survives a restart",
                !restarted.getHistory("CH001").isEmpty());

        System.out.println();
        System.out.println("Passed: " + passed + "   Failed: " + failed);
        if (failed > 0) { System.exit(1); }
    }
}