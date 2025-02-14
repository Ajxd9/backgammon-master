// Path: com/questioneditor/controller/Controller.java
package com.HyenaBgammon.controller;

import com.HyenaBgammon.models.ManagerModel;
import com.HyenaBgammon.models.Question;
import com.HyenaBgammon.view.ManagerView;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManagerController {
    private ManagerModel model;
    private ManagerView view;
    private int currentQuestionIndex = -1;
    private boolean isNewQuestion = false;

    public ManagerController(ManagerModel model, ManagerView view) {
        this.model = model;
        this.view = view;

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
    }
    class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Question question = isNewQuestion ? new Question() : model.getQuestion(currentQuestionIndex);

            // Update question data from view
            updateQuestionFromFields(question);

            // Validate question before saving or adding
            if (!model.isValidQuestion(question)) {
                view.showError("Please fill in all fields correctly.");
                return;
            }

            if (isNewQuestion) {
                // Only add a new question if validation passes
                model.addQuestion(question);
                currentQuestionIndex = model.getQuestions().size() - 1;
                isNewQuestion = false;
            }

            try {
                model.saveQuestions();
                view.updateQuestionList(model.getQuestions());
                view.setEditMode(false);
                view.showSuccess("Question saved successfully!");
            } catch (Exception ex) {
                view.showError("Error saving question: " + ex.getMessage());
            }
        }
    }

    class NewButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Clear the form for new entry without pre-adding it
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
            if (currentQuestionIndex >= 0) {
                isNewQuestion = false;
                view.setEditMode(true);
            }
        }
    }

    class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentQuestionIndex >= 0) {
                int result = view.showConfirmDialog(
                        "Are you sure you want to delete this question?",
                        "Confirm Delete"
                );

                if (result == 0) { // Yes option
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

    private void updateQuestionFromFields(Question question) {
        question.setQuestion(view.getQuestionText());
        String[] answers = new String[4];
        for (int i = 0; i < 4; i++) {
            answers[i] = view.getAnswerFields()[i].getText();
        }
        question.setAnswers(answers);
        question.setCorrectAns((String) view.getCorrectAnswerCombo().getSelectedItem());
        question.setDifficulty((String) view.getDifficultyCombo().getSelectedItem());
    }
}