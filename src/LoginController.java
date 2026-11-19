public class LoginController {
    private final String validUsername = "admin";
    private final String validPassword = "1234";
    public boolean login(String username, String password) {
        return validUsername.equals(username)
                && validPassword.equals(password);
    }
}