package com.HyenaBgammon.models;

import java.util.Arrays;

public class Question {
    private String question;       // The text of the question
    private String[] answers;      // Array of possible answers
    private String correctAns;     // The correct answer
    private String difficulty;     // Difficulty level (e.g., "Easy", "Medium", "Hard")

    // Default Constructor
    public Question() {
        // Empty constructor for flexibility
    }

    // Parameterized Constructor
    public Question(String question, String[] answers, String correctAns, String difficulty) {
        this.question = question;
        this.answers = answers;
        this.correctAns = correctAns;
        this.difficulty = difficulty;
    }

    // Getters and Setters
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String[] getAnswers() {
        return answers;
    }

    public void setAnswers(String[] answers) {
        this.answers = answers;
    }

    public String getCorrectAns() {
        return correctAns;
    }

    public void setCorrectAns(String correctAns) {
        this.correctAns = correctAns;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    // Static Utility Method for Difficulty Conversion
    public static String convertDifficulty(int difficulty) {
        switch (difficulty) {
            case 1:
                return "Easy";
            case 2:
                return "Medium";
            case 3:
                return "Hard";
            default:
                return "Unknown";
        }
    }

    // Validates if the given answer is correct
    public boolean isCorrect(String userAnswer) {
        return correctAns != null && correctAns.equals(userAnswer);
    }

    @Override
    public String toString() {
        return "Question{" +
                "question='" + question + '\'' +
                ", answers=" + Arrays.toString(answers) +
                ", correctAns='" + correctAns + '\'' +
                ", difficulty='" + difficulty + '\'' +
                '}';
    }
}
