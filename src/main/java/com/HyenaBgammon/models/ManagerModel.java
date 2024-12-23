package com.HyenaBgammon.models;

import org.json.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ManagerModel {
    private List<Question> questions;
    private static final String FILE_NAME = "Questions.json";

    public ManagerModel() {
        questions = new ArrayList<Question>();
    }

    public void loadQuestions() throws IOException, JSONException {
        String jsonContent = readFile(FILE_NAME);
        JSONObject jsonObject = new JSONObject(jsonContent);
        JSONArray questionsArray = jsonObject.getJSONArray("questions");

        questions.clear();
        for (int i = 0; i < questionsArray.length(); i++) {
            JSONObject questionObj = questionsArray.getJSONObject(i);
            Question question = new Question();
            question.setQuestion(questionObj.getString("question"));

            JSONArray answersArray = questionObj.getJSONArray("answers");
            String[] answers = new String[4];
            for (int j = 0; j < answersArray.length(); j++) {
                answers[j] = answersArray.getString(j);
            }
            question.setAnswers(answers);

            question.setCorrectAns(questionObj.getString("correct_ans"));
            question.setDifficulty(questionObj.getString("difficulty"));

            questions.add(question);
        }
    }

    public void saveQuestions() throws IOException, JSONException {
        JSONObject root = new JSONObject();
        JSONArray questionsArray = new JSONArray();

        for (Question q : questions) {
            JSONObject questionObj = new JSONObject();
            questionObj.put("question", q.getQuestion());
            questionObj.put("answers", new JSONArray(q.getAnswers()));
            questionObj.put("correct_ans", q.getCorrectAns());
            questionObj.put("difficulty", q.getDifficulty());
            questionsArray.put(questionObj);
        }

        root.put("questions", questionsArray);

        FileWriter file = null;
        try {
            file = new FileWriter(FILE_NAME);
            file.write(root.toString(2));
        } finally {
            if (file != null) {
                file.close();
            }
        }
    }

    private String readFile(String filename) throws IOException {
        StringBuilder content = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return content.toString();
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public Question getQuestion(int index) {
        return questions.get(index);
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public void updateQuestion(int index, Question question) {
        questions.set(index, question);
    }

    public void deleteQuestion(int index) {
        if (index >= 0 && index < questions.size()) {
            questions.remove(index);
        }
    }

    public boolean isValidQuestion(Question question) {
        if (question.getQuestion() == null || question.getQuestion().trim().isEmpty()) {
            return false;
        }

        String[] answers = question.getAnswers();
        if (answers == null || answers.length != 4) {
            return false;
        }

        for (String answer : answers) {
            if (answer == null || answer.trim().isEmpty()) {
                return false;
            }
        }

        String correctAns = question.getCorrectAns();
        if (correctAns == null || !isValidAnswerNumber(correctAns)) {
            return false;
        }

        String difficulty = question.getDifficulty();
        if (difficulty == null || !isValidDifficulty(difficulty)) {
            return false;
        }

        return true;
    }

    private boolean isValidAnswerNumber(String number) {
        try {
            int num = Integer.parseInt(number);
            return num >= 1 && num <= 4;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidDifficulty(String difficulty) {
        try {
            int diff = Integer.parseInt(difficulty);
            return diff >= 1 && diff <= 5;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}