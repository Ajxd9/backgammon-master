// File: src/com/questioneditor/view/LoginView.java
package fr.ujm.tse.info4.pgammon.view;

import fr.ujm.tse.info4.pgammon.models.Question;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class LoginView extends JFrame {
    private JList<String> questionList;
    private DefaultListModel<String> listModel;
    private JTextArea questionField;
    private JTextField[] answerFields;
    private JComboBox<String> correctAnswerCombo;
    private JComboBox<String> difficultyCombo;
    private JButton saveButton;
    private JButton newButton;
    private JButton deleteButton;
    private JButton editButton;
    private JButton logoutButton;
    private JLabel userLabel;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JPanel viewPanel;
    private JPanel editPanel;
    private JTextArea viewQuestionArea;
    private JTextField[] viewAnswerFields;
    private JTextField viewCorrectAnswer;
    private JTextField viewDifficulty;

    public LoginView() {
        initializeComponents();
    }

    private void initializeComponents() {
        setTitle("Question Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Create top panel for user info and logout
        createTopPanel();

        // Create main content panel
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create left panel with list and buttons
        contentPanel.add(createLeftPanel(), BorderLayout.WEST);

        // Create right panel with card layout for view/edit modes
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        viewPanel = createViewPanel();
        editPanel = createEditPanel();

        cardPanel.add(viewPanel, "VIEW");
        cardPanel.add(editPanel, "EDIT");

        contentPanel.add(cardPanel, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

        // Initial state
        cardLayout.show(cardPanel, "VIEW");
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
        saveButton.setEnabled(false);

        // Pack and center
        pack();
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 600));
    }

    private void createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // User info panel (left side)
        userLabel = new JLabel("Not logged in");
        topPanel.add(userLabel, BorderLayout.WEST);

        // Logout button (right side)
        logoutButton = new JButton("Logout");
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.add(logoutButton);
        topPanel.add(logoutPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.setPreferredSize(new Dimension(300, 500));

        // Create list
        listModel = new DefaultListModel<>();
        questionList = new JList<>(listModel);
        questionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(questionList);
        leftPanel.add(scrollPane, BorderLayout.CENTER);

        // Create buttons panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        newButton = new JButton("New");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        saveButton = new JButton("Save");

        buttonPanel.add(newButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(saveButton);

        leftPanel.add(buttonPanel, BorderLayout.SOUTH);
        return leftPanel;
    }

    private JPanel createViewPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0;

        // Question view
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Question:"), gbc);

        viewQuestionArea = new JTextArea(3, 30);
        viewQuestionArea.setEditable(false);
        viewQuestionArea.setLineWrap(true);
        viewQuestionArea.setWrapStyleWord(true);
        JScrollPane questionScroll = new JScrollPane(viewQuestionArea);
        gbc.gridx = 1;
        formPanel.add(questionScroll, gbc);

        // Answer fields
        viewAnswerFields = new JTextField[4];
        for (int i = 0; i < 4; i++) {
            gbc.gridx = 0;
            gbc.gridy = i + 1;
            formPanel.add(new JLabel("Answer " + (i + 1) + ":"), gbc);

            viewAnswerFields[i] = new JTextField(30);
            viewAnswerFields[i].setEditable(false);
            gbc.gridx = 1;
            formPanel.add(viewAnswerFields[i], gbc);
        }

        // Correct answer and difficulty
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Correct Answer:"), gbc);
        viewCorrectAnswer = new JTextField(30);
        viewCorrectAnswer.setEditable(false);
        gbc.gridx = 1;
        formPanel.add(viewCorrectAnswer, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Difficulty:"), gbc);
        viewDifficulty = new JTextField(30);
        viewDifficulty.setEditable(false);
        gbc.gridx = 1;
        formPanel.add(viewDifficulty, gbc);

        panel.add(formPanel, BorderLayout.NORTH);
        return panel;
    }

    private JPanel createEditPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0;

        // Question field
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Question:"), gbc);

        questionField = new JTextArea(3, 30);
        questionField.setLineWrap(true);
        questionField.setWrapStyleWord(true);
        JScrollPane questionScroll = new JScrollPane(questionField);
        gbc.gridx = 1;
        formPanel.add(questionScroll, gbc);

        // Answer fields
        answerFields = new JTextField[4];
        for (int i = 0; i < 4; i++) {
            gbc.gridx = 0;
            gbc.gridy = i + 1;
            formPanel.add(new JLabel("Answer " + (i + 1) + ":"), gbc);

            answerFields[i] = new JTextField(30);
            gbc.gridx = 1;
            formPanel.add(answerFields[i], gbc);
        }

        // Combo boxes
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Correct Answer:"), gbc);
        correctAnswerCombo = new JComboBox<>(new String[]{"1", "2", "3", "4"});
        gbc.gridx = 1;
        formPanel.add(correctAnswerCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Difficulty:"), gbc);
        difficultyCombo = new JComboBox<>(new String[]{"1", "2", "3", "4", "5"});
        gbc.gridx = 1;
        formPanel.add(difficultyCombo, gbc);

        panel.add(formPanel, BorderLayout.NORTH);
        return panel;
    }

    // Getters
    public JList<String> getQuestionList() {
        return questionList;
    }

    public DefaultListModel<String> getListModel() {
        return listModel;
    }

    public String getQuestionText() {
        return questionField.getText();
    }

    public JTextField[] getAnswerFields() {
        return answerFields;
    }

    public JComboBox<String> getCorrectAnswerCombo() {
        return correctAnswerCombo;
    }

    public JComboBox<String> getDifficultyCombo() {
        return difficultyCombo;
    }

    // Listeners
    public void addSaveButtonListener(ActionListener listener) {
        saveButton.addActionListener(listener);
    }

    public void addNewButtonListener(ActionListener listener) {
        newButton.addActionListener(listener);
    }

    public void addEditButtonListener(ActionListener listener) {
        editButton.addActionListener(listener);
    }

    public void addDeleteButtonListener(ActionListener listener) {
        deleteButton.addActionListener(listener);
    }

    public void addLogoutButtonListener(ActionListener listener) {
        logoutButton.addActionListener(listener);
    }

    public void addListSelectionListener(ListSelectionListener listener) {
        questionList.addListSelectionListener(listener);
    }

    // UI Update Methods
    public void setEditMode(boolean editMode) {
        cardLayout.show(cardPanel, editMode ? "EDIT" : "VIEW");
        saveButton.setEnabled(editMode);
    }

    public void enableEditDelete(boolean enable) {
        editButton.setEnabled(enable);
        deleteButton.setEnabled(enable);
    }

    public void updateQuestionList(List<Question> questions) {
        listModel.clear();
        for (int i = 0; i < questions.size(); i++) {
            listModel.addElement("Question " + (i + 1) + ": " + questions.get(i).getQuestion());
        }
    }

    public void displayQuestion(Question question) {
        // Update view panel
        viewQuestionArea.setText(question.getQuestion());

        String[] answers = question.getAnswers();
        for (int i = 0; i < 4; i++) {
            viewAnswerFields[i].setText(answers[i]);
        }

        viewCorrectAnswer.setText(question.getCorrectAns());
        viewDifficulty.setText(question.getDifficulty());

        // Update edit panel
        questionField.setText(question.getQuestion());
        for (int i = 0; i < 4; i++) {
            answerFields[i].setText(answers[i]);
        }
        correctAnswerCombo.setSelectedItem(question.getCorrectAns());
        difficultyCombo.setSelectedItem(question.getDifficulty());
    }

    public void clearFields() {
        // Clear edit panel
        questionField.setText("");
        for (JTextField field : answerFields) {
            field.setText("");
        }
        correctAnswerCombo.setSelectedIndex(0);
        difficultyCombo.setSelectedIndex(0);

        // Clear view panel
        viewQuestionArea.setText("");
        for (JTextField field : viewAnswerFields) {
            field.setText("");
        }
        viewCorrectAnswer.setText("");
        viewDifficulty.setText("");
    }

    public void updateUserLabel(String username) {
        userLabel.setText("Logged in as: " + username);
    }

    // Dialog Methods
    public void showError(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public int showConfirmDialog(String message, String title) {
        return JOptionPane.showConfirmDialog(this,
                message,
                title,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
    }
}