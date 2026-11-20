import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
// Boundary class: shows information and collects input.
// It talks ONLY to the controllers. It has NO business logic.
public class AccountView {
    private final Stage stage;
    private final LoginController loginController;
    private final AccountController accountController;
    public AccountView(Stage stage, LoginController loginController,
                       AccountController accountController) {
        this.stage = stage;
        this.loginController = loginController;
        this.accountController = accountController;
    }
    public void show() {
        Label heading = new Label("My Accounts");
        heading.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        Label pickLabel = new Label("Choose an account number:");
        ComboBox<String> accountBox = new ComboBox<>();
        accountBox.getItems().addAll(accountController.getAccountNumbers());
        accountBox.getSelectionModel().selectFirst();
        Label typeLabel = new Label();
        Label balanceLabel = new Label();
        balanceLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        TextField amountField = new TextField();
        amountField.setPromptText("Amount in BWP");
        Button depositButton = new Button("Deposit");
        Button withdrawButton = new Button("Withdraw");
        Button interestButton = new Button("Pay Month-End Interest");
        HBox buttonRow = new HBox(10, depositButton, withdrawButton, interestButton);
        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: darkblue;");
        Label historyLabel = new Label("Transaction history:");
        ListView<String> historyList = new ListView<>();

        historyList.setPrefHeight(180);
        Button logoutButton = new Button("Logout");
        // Ask the controller for fresh text and show it
        Runnable refresh = () -> {
            String accNo = accountBox.getValue();
            typeLabel.setText("Type: " + accountController.getAccountType(accNo));
            balanceLabel.setText("Balance: " + accountController.getBalanceText(accNo));
            historyList.getItems().setAll(accountController.getHistory(accNo));
        };
        accountBox.setOnAction(e -> {
            messageLabel.setText("");
            refresh.run();
        });
        depositButton.setOnAction(e -> {
            messageLabel.setText(accountController.deposit(
                    accountBox.getValue(), amountField.getText()));
            amountField.clear();
            refresh.run();
        });
        withdrawButton.setOnAction(e -> {
            messageLabel.setText(accountController.withdraw(
                    accountBox.getValue(), amountField.getText()));
            amountField.clear();
            refresh.run();
        });
        interestButton.setOnAction(e -> {
            messageLabel.setText(accountController.payInterest(
                    accountBox.getValue()));
            refresh.run();
        });
        logoutButton.setOnAction(e ->
                new LoginView(stage, loginController, accountController).show());
        refresh.run();
        VBox root = new VBox(10, heading, pickLabel, accountBox, typeLabel,
                balanceLabel, amountField, buttonRow, messageLabel,
                historyLabel, historyList, logoutButton);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_LEFT);
        stage.setTitle("Banking System - Accounts");
        stage.setScene(new Scene(root, 560, 620));
        stage.show();
    }
}