import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


// Boundary class: NO business logic
public class LoginView {
    private final Stage stage;
    private final LoginController loginController;
    private final AccountController accountController;
    public LoginView(Stage stage, LoginController loginController,
                     AccountController accountController) {
        this.stage = stage;
        this.loginController = loginController;
        this.accountController = accountController;
    }
    public void show() {
        Label heading = new Label("Banking System");
        heading.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");
        Label hint = new Label("Please log in to continue");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        Button loginButton = new Button("Login");
        loginButton.setDefaultButton(true);   
        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red;");
        
        loginButton.setOnAction(e -> {
            boolean ok = loginController.login(
                    usernameField.getText(), passwordField.getText());
            if (ok) {
                new AccountView(stage, loginController, accountController).show();
            } else {
                messageLabel.setText("Wrong username or password.");
                passwordField.clear();
            }
        });
        VBox layout = new VBox(12, heading, hint, usernameField,
                passwordField, loginButton, messageLabel);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));
        layout.setMaxWidth(300);


        VBox root = new VBox(layout);
        root.setAlignment(Pos.CENTER);
        stage.setTitle("Banking System - Login");
        stage.setScene(new Scene(root, 420, 360));
        stage.show();
    }
}