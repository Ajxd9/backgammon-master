package com.HyenaBgammon.models;

public abstract class AbstractGame {
	// Template method defining the structure of the game lifecycle
    public final void playGame() {
        initializeGame();
        while (!isGameOver()) {
            playTurn();
        }
        endGame();
    }

    protected abstract void initializeGame();  // Step 1: Setup the game
    protected abstract boolean isGameOver();   // Step 2: Check game-over condition
    protected abstract void playTurn();        // Step 3: Handle a turn
    protected abstract void endGame();         // Step 4: Conclude the game

}
