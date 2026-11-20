public class LoginController {
    private final String validUsername = "admin";
    private final String validPassword = "1234";
    private String message = "";

    public boolean login(String username, String password) {
        if (username == null || username.trim().isEmpty()
                || password == null || password.isEmpty()) {
            message = "Please type your username and password.";
            return false;
        }
        if (validUsername.equals(username.trim())
                && validPassword.equals(password)) {
            message = "";
            return true;
        }
        message = "Wrong username or password.";
        return false;
    }

    public String getMessage() {
        return message;
    }
}