package fr.ujm.tse.info4.pgammon.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LandingView extends JFrame {
    private JButton loginButton;

    public LandingView() {
        initializeComponents();
    }

    private void initializeComponents() {
        setTitle("Question Editor - Welcome");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(400, 300));

        // Main panel with vertical BoxLayout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));


        // Login button
        loginButton = new JButton("Login");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(150, 40));
        loginButton.setFont(new Font("Arial", Font.PLAIN, 14));

        // Add components with spacing
        mainPanel.add(Box.createVerticalGlue());

        mainPanel.add(loginButton);
        mainPanel.add(Box.createVerticalGlue());

        add(mainPanel, BorderLayout.CENTER);

        // Center on screen
        setLocationRelativeTo(null);
    }

    public void addLoginButtonListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }
}