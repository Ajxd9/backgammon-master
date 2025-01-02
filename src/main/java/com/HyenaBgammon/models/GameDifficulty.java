package com.HyenaBgammon.models;

import java.util.ArrayList;
import java.util.List;

public enum GameDifficulty {
    EASY("EASY", 2, 0, 0),     // 2 regular dice
    MEDIUM("MEDIUM", 2, 1, 0), // 2 regular dice, 1 question die
    HARD("HARD", 0, 1, 2);     // 2 enhanced dice, 1 question die

    private final String difficultyName;
    private final int regularDiceCount;
    private final int questionDiceCount;
    private final int enhancedDiceCount;

    /* CONSTRUCTOR */
    GameDifficulty(String name, int regularDice, int questionDice, int enhancedDice) {
        this.difficultyName = name;
        this.regularDiceCount = regularDice;
        this.questionDiceCount = questionDice;
        this.enhancedDiceCount = enhancedDice;
    }

    /* GETTERS */
    public String getDifficultyName() {
        return difficultyName;
    }

    public int getRegularDiceCount() {
        return regularDiceCount;
    }

    public int getQuestionDiceCount() {
        return questionDiceCount;
    }

    public int getEnhancedDiceCount() {
        return enhancedDiceCount;
    }

    /**
     * Generates the list of dice based on the difficulty level.
     * @param playerColor - The color of the dice for this player
     * @return List<SixSidedDie>
     */
    public List<SixSidedDie> generateDice(SquareColor playerColor) {
        List<SixSidedDie> diceList = new ArrayList<>();

        // Add regular dice
        for (int i = 0; i < regularDiceCount; i++) {
            diceList.add(new SixSidedDie(DieType.REGULAR, playerColor));
        }

        // Add question dice
        for (int i = 0; i < questionDiceCount; i++) {
            diceList.add(new SixSidedDie(DieType.QUESTION, playerColor));
        }

        // Add enhanced dice
        for (int i = 0; i < enhancedDiceCount; i++) {
            diceList.add(new SixSidedDie(DieType.ENHANCED, playerColor));
        }

        return diceList;
    }

    @Override
    public String toString() {
        return difficultyName;
    }
}