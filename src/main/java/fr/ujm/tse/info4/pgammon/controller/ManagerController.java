// File: src/fr.ujm.tse.info4.pgammon/controller/Controller.java
package fr.ujm.tse.info4.pgammon.controller;

import fr.ujm.tse.info4.pgammon.models.AuthManager;
import fr.ujm.tse.info4.pgammon.models.ManagerModel;
import fr.ujm.tse.info4.pgammon.models.Question;
import fr.ujm.tse.info4.pgammon.view.MenuView;
import fr.ujm.tse.info4.pgammon.view.LoginView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManagerController {
    private ManagerModel model;
    private LoginView view;
    private MenuView landingView;
    private int currentQuestionIndex = -1;
    private boolean isNewQuestion = false;

    public ManagerController(ManagerModel model, LoginView view, MenuView landingView) {
        this.model = model;
        this.view = view;
        this.landingView = landingView;

        // Initialize view with data from model
        try {
            model.loadQuestions();
            view.updateQuestionList(model.getQuestions());
        } catch (Exception e) {
            view.showError("Error loading questions: " + e.getMessage());
        }

        // Add listeners
        view.addSaveButtonListener(new SaveButtonListener());
        view.addNewButtonListener(new NewButtonListener());
        view.addEditButtonListener(new EditButtonListener());
        view.addDeleteButtonListener(new DeleteButtonListener());
        view.addListSelectionListener(new QuestionListListener());
        view.addLogoutButtonListener(new LogoutListener());

        // Update user information
        view.updateUserLabel(AuthManager.getInstance().getCurrentUser());
    }

    class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!AuthManager.getInstance().isAuthenticated()) {
                handleLogout();
                return;
            }

            Question question;
            if (isNewQuestion) {
                question = new Question();
                model.addQuestion(question);
                currentQuestionIndex = model.getQuestions().size() - 1;
            } else {
                question = model.getQuestion(currentQuestionIndex);
            }

            updateQuestionFromFields(question);

            try {
                model.saveQuestions();
                view.updateQuestionList(model.getQuestions());
                view.setEditMode(false);
                isNewQuestion = false;
                view.showSuccess("Question saved successfully!");
            } catch (Exception ex) {
                view.showError("Error saving question: " + ex.getMessage());
            }
        }
    }

    class NewButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!AuthManager.getInstance().isAuthenticated()) {
                handleLogout();
                return;
            }

            isNewQuestion = true;
            view.clearFields();
            view.setEditMode(true);
            currentQuestionIndex = -1;
            view.getQuestionList().clearSelection();
            view.enableEditDelete(false);
        }
    }

    class EditButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!AuthManager.getInstance().isAuthenticated()) {
                handleLogout();
                return;
            }

            if (currentQuestionIndex >= 0) {
                isNewQuestion = false;
                view.setEditMode(true);
            }
        }
    }

    class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!AuthManager.getInstance().isAuthenticated()) {
                handleLogout();
                return;
            }

            if (currentQuestionIndex >= 0) {
                int result = view.showConfirmDialog(
                        "Are you sure you want to delete this question?",
                        "Confirm Delete"
                );

                if (result == JOptionPane.YES_OPTION) {
                    model.deleteQuestion(currentQuestionIndex);
                    try {
                        model.saveQuestions();
                        view.updateQuestionList(model.getQuestions());
                        currentQuestionIndex = -1;
                        view.getQuestionList().clearSelection();
                        view.enableEditDelete(false);
                        view.setEditMode(false);
                        view.showSuccess("Question deleted successfully!");
                    } catch (Exception ex) {
                        view.showError("Error deleting question: " + ex.getMessage());
                    }
                }
            }
        }
    }

    class QuestionListListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                currentQuestionIndex = view.getQuestionList().getSelectedIndex();
                if (currentQuestionIndex >= 0) {
                    Question question = model.getQuestion(currentQuestionIndex);
                    view.displayQuestion(question);
                    view.enableEditDelete(true);
                    view.setEditMode(false);
                    isNewQuestion = false;
                } else {
                    view.enableEditDelete(false);
                }
            }
        }
    }

    class LogoutListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            handleLogout();
        }
    }

    private void handleLogout() {
        int choice = view.showConfirmDialog(
                "Are you sure you want to logout?",
                "Confirm Logout"
        );

        if (choice == JOptionPane.YES_OPTION) {
            AuthManager.getInstance().logout();
            view.clearFields();
            view.setVisible(false);
            landingView.setVisible(true);
        }
    }

    private void updateQuestionFromFields(Question question) {
        question.setQuestion(view.getQuestionText());
        String[] answers = new String[4];
        JTextField[] answerFields = view.getAnswerFields();

        for (int i = 0; i < 4; i++) {
            answers[i] = answerFields[i].getText();
        }

        question.setAnswers(answers);
        question.setCorrectAns((String) view.getCorrectAnswerCombo().getSelectedItem());
        question.setDifficulty((String) view.getDifficultyCombo().getSelectedItem());
    }
}

