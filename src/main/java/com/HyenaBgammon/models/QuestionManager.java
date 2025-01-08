package com.HyenaBgammon.models;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestionManager {
    private List<Question> questions;

    public QuestionManager() {
        this.questions = new ArrayList<>(); // Initialize the list
        loadQuestions("Questions.json");    // Adjust the path if needed
    }

    private void loadQuestions(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            // Parse JSON file
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            JSONArray questionArray = (JSONArray) jsonObject.get("questions");
            for (Object obj : questionArray) {
                JSONObject questionObj = (JSONObject) obj;

                String questionText = (String) questionObj.get("question");
                JSONArray answersArray = (JSONArray) questionObj.get("answers");
                String[] answers = new String[answersArray.size()];
                for (int i = 0; i < answersArray.size(); i++) {
                    answers[i] = (String) answersArray.get(i);
                }
                String correctAnswer = (String) questionObj.get("correct_ans");
                String difficulty = (String) questionObj.get("difficulty");

                questions.add(new Question(questionText, answers, correctAnswer, difficulty));
            }
        } catch (IOException | ParseException e) {
            System.err.println("Error loading questions from file: " + filePath);
            e.printStackTrace();
        }
    }

    public Question getRandomQuestion() {
        if (questions == null || questions.isEmpty()) {
            System.err.println("No questions available.");
            return null;
        }
        Random random = new Random();
        return questions.get(random.nextInt(questions.size()));
    }
}
