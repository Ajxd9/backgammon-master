// File: src/com/questioneditor/login/view/LoginDialog.java
package fr.ujm.tse.info4.pgammon.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class LoginDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton cancelButton;
    private JLabel attemptsLabel;
    private JLabel messageLabel;
    private boolean authenticated = false;

    public LoginDialog(JFrame owner) {
        super(owner, "Login Required", true);
        initializeUI();
    }

    private void initializeUI() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel usernameLabel = new JLabel("Username:");
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        usernameField = new JTextField(15);
        formPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        JLabel passwordLabel = new JLabel("Password:");
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        passwordField = new JPasswordField(15);
        formPanel.add(passwordField, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginButton = new JButton("Login");
        cancelButton = new JButton("Cancel");
        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);

        // Message labels panel
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));

        messageLabel = new JLabel("Please enter your credentials");
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        attemptsLabel = new JLabel("Remaining attempts: 3");
        attemptsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        attemptsLabel.setForeground(Color.GRAY);

        messagePanel.add(messageLabel);
        messagePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        messagePanel.add(attemptsLabel);

        // Add all panels to main panel
        mainPanel.add(formPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(messagePanel);

        // Add main panel to dialog
        add(mainPanel);

        // Add key listeners
        addKeyListeners();

        // Set default button
        getRootPane().setDefaultButton(loginButton);

        // Cancel button action
        cancelButton.addActionListener(e -> {
            authenticated = false;
            dispose();
        });

        // Pack and center
        pack();
        setLocationRelativeTo(getOwner());
    }

    private void addKeyListeners() {
        KeyListener enterKeyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (e.getSource() == usernameField) {
                        passwordField.requestFocus();
                    } else if (e.getSource() == passwordField) {
                        loginButton.doClick();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        };

        usernameField.addKeyListener(enterKeyListener);
        passwordField.addKeyListener(enterKeyListener);
    }

    public void addLoginListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    public String getUsername() {
        return usernameField.getText().trim();
    }

    public String getPassword() {
        return new String(passwordField.getPassword()).trim();
    }

    public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        usernameField.requestFocus();
        messageLabel.setText("Please enter your credentials");
        messageLabel.setForeground(Color.BLACK);
        attemptsLabel.setText("Remaining attempts: 3");
        attemptsLabel.setForeground(Color.GRAY);
        enableLogin(true);
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    public void setAttemptsMessage(String message) {
        attemptsLabel.setText(message);
        attemptsLabel.setForeground(Color.RED);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Login Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public void enableLogin(boolean enable) {
        loginButton.setEnabled(enable);
        usernameField.setEnabled(enable);
        passwordField.setEnabled(enable);
        cancelButton.setEnabled(enable);
    }

    public void disableLogin() {
        enableLogin(false);
        messageLabel.setText("Account locked");
        messageLabel.setForeground(Color.RED);
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }
}