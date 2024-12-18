package fr.ujm.tse.info4.pgammon.models;

public class AuthManager {
    private static AuthManager instance;
    private boolean isAuthenticated = false;
    private String currentUser = null;

    private AuthManager() {}

    public static AuthManager getInstance() {
        if (instance == null) {
            instance = new AuthManager();
        }
        return instance;
    }

    public void login(String username) {
        isAuthenticated = true;
        currentUser = username;
    }

    public void logout() {
        isAuthenticated = false;
        currentUser = null;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public String getCurrentUser() {
        return currentUser;
    }
}