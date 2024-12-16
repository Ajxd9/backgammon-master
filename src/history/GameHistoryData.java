package history;

import java.util.ArrayList;
import java.util.List;

public class GameHistoryData {
    private List<GameRecord> gameRecords;

    
    public GameHistoryData(List<GameRecord> gameRecords) {
		this.gameRecords = gameRecords;
	}

	public GameHistoryData() {
        this.gameRecords = new ArrayList<>();
        loadSampleData();
    }

    //func for creating some states for the UI (will be deleted when implementing a DataBase)
    private void loadSampleData() {
        gameRecords.add(new GameRecord("Abed", "24/11/24", "14:56", "Victory", "Hard", "18/30", "Aiham"));
        gameRecords.add(new GameRecord("Abed", "24/11/24", "30:00", "Defeat", "Easy", "5/10", "Aiham"));
        gameRecords.add(new GameRecord("Abed", "23/11/24", "14:56", "Victory", "Medium", "12/28", "Roaa"));
        gameRecords.add(new GameRecord("Abed", "20/11/24", "14:56", "Victory", "Hard", "11/18", "Malak"));
        gameRecords.add(new GameRecord("Player 1", "19/11/24", "14:56", "Defeat", "Hard", "18/30", "Player 2"));
    }

	public List<GameRecord> getGameRecords() {
		return gameRecords;
	}
	public void setGameRecords(List<GameRecord> gameRecords) {
		this.gameRecords = gameRecords;
	}
}

