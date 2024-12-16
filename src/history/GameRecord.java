package history;

public class GameRecord {
    private String player;
    private String date;
    private String time;
    private String result;
    private String difficulty;
    private String questionsAnswered;
    private String opponent;

    public GameRecord(String player, String date, String time, String result, String difficulty,
			String questionsAnswered, String opponent) {
		this.player = player;
		this.date = date;
		this.time = time;
		this.result = result;
		this.difficulty = difficulty;
		this.questionsAnswered = questionsAnswered;
		this.opponent = opponent;
	}

	public String getPlayer() {
		return player;
	}

	public void setPlayer(String player) {
		this.player = player;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}

	public String getQuestionsAnswered() {
		return questionsAnswered;
	}

	public void setQuestionsAnswered(String questionsAnswered) {
		this.questionsAnswered = questionsAnswered;
	}

	public String getOpponent() {
		return opponent;
	}

	public void setOpponent(String opponent) {
		this.opponent = opponent;
	}
}
