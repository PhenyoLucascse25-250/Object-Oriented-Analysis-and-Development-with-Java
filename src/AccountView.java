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
      
        Label pickLabel = new Label("Choose an account:");
        ComboBox<Account> accountBox = new ComboBox<>();
        accountBox.getItems().addAll(accountController.getAccounts());
        accountBox.getSelectionModel().selectFirst();
      
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
       
        Runnable refresh = () -> {
            Account acc = accountBox.getValue();
            balanceLabel.setText("Balance: BWP " + String.format("%.2f", acc.getBalance()));
            historyList.getItems().setAll(accountController.getHistory(acc));
        };
        accountBox.setOnAction(e -> {
            messageLabel.setText("");
            refresh.run();
        });
        depositButton.setOnAction(e -> {
            Double amount = readAmount(amountField, messageLabel);
            if (amount == null) { return; }
            messageLabel.setText(
                    accountController.deposit(accountBox.getValue(), amount));
            amountField.clear();
            refresh.run();
        });
        withdrawButton.setOnAction(e -> {
            Double amount = readAmount(amountField, messageLabel);
            if (amount == null) { return; }
            messageLabel.setText(
                    accountController.withdraw(accountBox.getValue(), amount));
            amountField.clear();
            refresh.run();
        });
        interestButton.setOnAction(e -> {
            messageLabel.setText(
                    accountController.payInterest(accountBox.getValue()));
            refresh.run();
        });
        logoutButton.setOnAction(e ->
                new LoginView(stage, loginController, accountController).show());

        refresh.run();   
        VBox root = new VBox(10, heading, pickLabel, accountBox, balanceLabel,
                amountField, buttonRow, messageLabel, historyLabel,
                historyList, logoutButton);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_LEFT);
        stage.setTitle("Banking System - Accounts");
        stage.setScene(new Scene(root, 560, 600));
        stage.show();
    }
    
    private Double readAmount(TextField field, Label messageLabel) {
        try {
            return Double.parseDouble(field.getText().trim());
        } catch (NumberFormatException ex) {
            messageLabel.setText("Please type a number, like 250.");
            return null;
        }
    }
}