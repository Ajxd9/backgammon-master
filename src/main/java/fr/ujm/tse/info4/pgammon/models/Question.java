package fr.ujm.tse.info4.pgammon.models;

import java.util.List;


public class Question {
    private String question;
    private List<String> answers;
    private int correctAns;
    private int difficulty;
    
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public List<String> getAnswers() {
		return answers;
	}
	public void setAnswers(List<String> answers) {
		this.answers = answers;
	}
	
    // Method to set an individual answer by index
    public void setAnswerByIndex(int index, String answer) {
            answers.set(index, answer);
    }
    
	public int getCorrectAns() {
		return correctAns;
	}
	public void setCorrectAns(int correctAns) {
		this.correctAns = correctAns;
	}
	public int getDifficulty() {
		return difficulty;
	}
	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}
	// converting String to int in difficulty level
	public void getDifficulty (String difficulty) {
		if (difficulty == "Easy")
			this.difficulty = 1;
		if (difficulty == "Medium")
			this.difficulty = 2;
		if (difficulty == "Hard")
			this.difficulty = 3;
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
	public Question(String question, List<String> answers, int correctAns, int difficulty) {
		super();
		this.question = question;
		this.answers = answers;
		this.correctAns = correctAns;
		this.difficulty = difficulty;
	}
    
    

}