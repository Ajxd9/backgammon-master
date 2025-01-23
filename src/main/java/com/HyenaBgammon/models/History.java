package com.HyenaBgammon.models;

import java.time.Duration;
import java.time.LocalDateTime;

public class History {


    // Fields
    private String winnerName;
    private String loserName;
    private Duration gameDuration;
    private GameDifficulty difficultyLevel;
    private LocalDateTime endTime;
	public String getWinnerName() {
		return winnerName;
	}
	public void setWinnerName(String winnerName) {
		this.winnerName = winnerName;
	}
	public String getLoserName() {
		return loserName;
	}
	public void setLoserName(String loserName) {
		this.loserName = loserName;
	}
	public Duration getGameDuration() {
		return gameDuration;
	}
	public void setGameDuration(Duration gameDuration) {
		this.gameDuration = gameDuration;
	}
	public GameDifficulty getDifficultyLevel() {
		return difficultyLevel;
	}
	public void setDifficultyLevel(GameDifficulty difficultyLevel) {
		this.difficultyLevel = difficultyLevel;
	}
	public LocalDateTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}
	public History(String winnerName, String loserName, Duration gameDuration, GameDifficulty difficultyLevel,
			LocalDateTime endTime) {
		super();
		this.winnerName = winnerName;
		this.loserName = loserName;
		this.gameDuration = gameDuration;
		this.difficultyLevel = difficultyLevel;
		this.endTime = endTime;
	}

    
}
