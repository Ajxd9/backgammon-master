// File: src/com/QuestionEditor/login/controller/LoginHandler.java
package fr.ujm.tse.info4.pgammon.controller;

import fr.ujm.tse.info4.pgammon.models.AuthManager;
import fr.ujm.tse.info4.pgammon.models.LoginModel;
import fr.ujm.tse.info4.pgammon.view.LoginDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginHandler {
    private LoginModel model;
    private LoginDialog dialog;
    private Timer lockTimer;
    private int remainingAttempts = 3;

    public LoginHandler(JFrame parent) {
        this.model = new LoginModel();
        this.dialog = new LoginDialog(parent);

        initializeLockTimer();

        dialog.addLoginListener(new LoginListener());
    }

    private void initializeLockTimer() {
        lockTimer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        lockTimer.setRepeats(false);
    }

    public boolean showLoginDialog() {
        dialog.clearFields();
        dialog.setVisible(true);
        return dialog.isAuthenticated();
    }

    private class LoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = dialog.getUsername();
            String password = dialog.getPassword();

            if (username.isEmpty() || password.isEmpty()) {
                dialog.showError("Please enter both username and password");
                return;
            }

            if (model.hasExceededMaxAttempts()) {
                handleExceededAttempts();
                return;
            }

            if (model.validateCredentials(username, password)) {
                handleSuccessfulLogin(username);
            } else {
                handleFailedLogin();
            }
        }
    }

    private void handleSuccessfulLogin(String username) {
        AuthManager.getInstance().login(username);
        dialog.setAuthenticated(true);
        dialog.dispose();
    }

    private void handleFailedLogin() {
        remainingAttempts--;
        dialog.showError("Invalid username or password");
        dialog.clearFields();

        if (remainingAttempts > 0) {
            dialog.setAttemptsMessage("Remaining attempts: " + remainingAttempts);
        } else {
            handleExceededAttempts();
        }
    }

    private void handleExceededAttempts() {
        dialog.disableLogin();
        dialog.showError("Maximum login attempts exceeded. Application will close in 5 seconds.");
        lockTimer.start();
    }

    public void resetAttempts() {
        remainingAttempts = 3;
        model.resetAttempts();
        dialog.clearFields();
        dialog.enableLogin(true);
    }
}