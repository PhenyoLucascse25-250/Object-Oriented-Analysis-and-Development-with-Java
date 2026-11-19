import javafx.application.Application;
import javafx.stage.Stage;
public class BankApp extends Application {
    @Override
    public void start(Stage stage) {
        LoginController loginController = new LoginController();
        AccountController accountController = new AccountController();
        new LoginView(stage, loginController, accountController).show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}