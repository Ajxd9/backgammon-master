package com.HyenaBgammon.view;

import com.HyenaBgammon.models.Question;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class ManagerView extends JFrame {
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
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JPanel viewPanel;
    private JPanel editPanel;
    private JTextArea viewQuestionArea;
    private JTextField[] viewAnswerFields;
    private JTextField viewCorrectAnswer;
    private JTextField viewDifficulty;

    public ManagerView() {
        setTitle("Question Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        initializeComponents();
        pack();
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 600));
    }

    private void initializeComponents() {
        // Left panel with question list and buttons
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(300, 600));

        // Question list
        listModel = new DefaultListModel<String>();
        questionList = new JList<String>(listModel);
        questionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(questionList);
        scrollPane.setPreferredSize(new Dimension(280, 500));
        leftPanel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        newButton = new JButton("New");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        saveButton = new JButton("Save");

        buttonPanel.add(newButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(saveButton);

        leftPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(leftPanel, BorderLayout.WEST);

        // Card Panel for ManagerView/Edit modes
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Create view panel
        viewPanel = createViewPanel();
        cardPanel.add(viewPanel, "VIEW");

        // Create edit panel
        editPanel = createEditPanel();
        cardPanel.add(editPanel, "EDIT");

        add(cardPanel, BorderLayout.CENTER);

        // Start with view mode
        cardLayout.show(cardPanel, "VIEW");
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
        saveButton.setEnabled(false);
    }

    private JPanel createViewPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel viewContent = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0;

        // Question view
        gbc.gridx = 0;
        gbc.gridy = 0;
        viewContent.add(new JLabel("Question:"), gbc);

        viewQuestionArea = new JTextArea(3, 30);
        viewQuestionArea.setEditable(false);
        viewQuestionArea.setLineWrap(true);
        viewQuestionArea.setWrapStyleWord(true);
        JScrollPane questionScroll = new JScrollPane(viewQuestionArea);
        gbc.gridx = 1;
        viewContent.add(questionScroll, gbc);

        // Answer fields
        viewAnswerFields = new JTextField[4];
        for (int i = 0; i < 4; i++) {
            gbc.gridx = 0;
            gbc.gridy = i + 1;
            viewContent.add(new JLabel("Answer " + (i + 1) + ":"), gbc);

            viewAnswerFields[i] = new JTextField(30);
            viewAnswerFields[i].setEditable(false);
            gbc.gridx = 1;
            viewContent.add(viewAnswerFields[i], gbc);
        }

        // Correct answer and difficulty
        gbc.gridx = 0;
        gbc.gridy++;
        viewContent.add(new JLabel("Correct Answer:"), gbc);
        viewCorrectAnswer = new JTextField(30);
        viewCorrectAnswer.setEditable(false);
        gbc.gridx = 1;
        viewContent.add(viewCorrectAnswer, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        viewContent.add(new JLabel("Difficulty:"), gbc);
        viewDifficulty = new JTextField(30);
        viewDifficulty.setEditable(false);
        gbc.gridx = 1;
        viewContent.add(viewDifficulty, gbc);

        panel.add(viewContent, BorderLayout.NORTH);
        return panel;
    }

    private JPanel createEditPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel editContent = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0;

        // Question field
        gbc.gridx = 0;
        gbc.gridy = 0;
        editContent.add(new JLabel("Question:"), gbc);

        questionField = new JTextArea(3, 30);
        questionField.setLineWrap(true);
        questionField.setWrapStyleWord(true);
        JScrollPane questionScroll = new JScrollPane(questionField);
        gbc.gridx = 1;
        editContent.add(questionScroll, gbc);

        // Answer fields
        answerFields = new JTextField[4];
        for (int i = 0; i < 4; i++) {
            gbc.gridx = 0;
            gbc.gridy = i + 1;
            editContent.add(new JLabel("Answer " + (i + 1) + ":"), gbc);

            answerFields[i] = new JTextField(30);
            gbc.gridx = 1;
            editContent.add(answerFields[i], gbc);
        }

        // Combo boxes
        gbc.gridx = 0;
        gbc.gridy++;
        editContent.add(new JLabel("Correct Answer:"), gbc);
        correctAnswerCombo = new JComboBox<String>(new String[]{"1", "2", "3", "4"});
        gbc.gridx = 1;
        editContent.add(correctAnswerCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        editContent.add(new JLabel("Difficulty:"), gbc);
        difficultyCombo = new JComboBox<String>(new String[]{"1", "2", "3", "4", "5"});
        gbc.gridx = 1;
        editContent.add(difficultyCombo, gbc);

        panel.add(editContent, BorderLayout.NORTH);
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

    // Action Listeners
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
    public void addListSelectionListener(javax.swing.event.ListSelectionListener listener) {
        questionList.addListSelectionListener(listener);
    }

    // UI State Methods
    public void setEditMode(boolean editMode) {
        cardLayout.show(cardPanel, editMode ? "EDIT" : "VIEW");
        saveButton.setEnabled(editMode);
    }

    public void enableEditDelete(boolean enable) {
        editButton.setEnabled(enable);
        deleteButton.setEnabled(enable);
    }

    // Update Methods
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

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public int showConfirmDialog(String message, String title) {
        return JOptionPane.showConfirmDialog(this, message, title,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }
}