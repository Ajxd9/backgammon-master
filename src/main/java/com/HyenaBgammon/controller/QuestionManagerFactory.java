package com.HyenaBgammon.controller;

import com.HyenaBgammon.models.QuestionManager;

public class QuestionManagerFactory {
    public static QuestionManager createQuestionManager() {
        return new QuestionManager();  // You can modify this if additional setup is required.
    }
}
