package fr.ujm.tse.info4.pgammon.models;

public class LoginModel {
    private static final String VALID_USERNAME = "admin";
    private static final String VALID_PASSWORD = "admin";
    private int loginAttempts = 0;
    private static final int MAX_ATTEMPTS = 3;

    public boolean validateCredentials(String username, String password) {
        loginAttempts++;
        return VALID_USERNAME.equals(username) && VALID_PASSWORD.equals(password);
    }

    public int getRemainingAttempts() {
        return MAX_ATTEMPTS - loginAttempts;
    }

    public boolean hasExceededMaxAttempts() {
        return loginAttempts >= MAX_ATTEMPTS;
    }

    public void resetAttempts() {
        loginAttempts = 0;
    }
}