// Path: com/questioneditor/model/Question.java
package com.HyenaBgammon.models;

public class Question {
    private String question;
    private String[] answers;
    private String correctAns;
    private String difficulty;

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
    
    
 // converting int to String in difficulty level
 	public String getDifficulty (int difficulty) {
 		if (difficulty == 1)
 			return "Easy";
 		if (difficulty == 2)
 			return "Medium";
 		if (difficulty == 3)
 			return "Hard";
 		else 
 			return "";	
 	}
 	
 	

public Question() {
	super();
}

public Question(String question, String[] answers, String correctAns, String difficulty) {
	super();
	this.question = question;
	this.answers = answers;
	this.correctAns = correctAns;
	this.difficulty = difficulty;
}
 
 	
}