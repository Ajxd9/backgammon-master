package com.HyenaBgammon.models;

import org.jdom2.Element;

public class GameParameters {
    private int secondsPerTurn;
    private int winningGamesCount;
    private boolean useDoubling;
    public Player whitePlayer;
    public Player blackPlayer;
    private GameDifficulty difficulty;
    
    public GameParameters(int secondsPerTurn, int winningGamesCount, boolean useDoubling, GameDifficulty difficulty, Player whitePlayer, Player blackPlayer) {
        this.secondsPerTurn = secondsPerTurn;
        this.winningGamesCount = winningGamesCount;
        this.useDoubling = useDoubling;
        this.difficulty = difficulty;
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
    }
    
    public GameParameters() {
    }
    
    public void save(Element session) {
        Element parameters = new Element("parameters");
        session.addContent(parameters);
        
        Element secondsPerTurnXML = new Element("secondsPerTurn");
        secondsPerTurnXML.setText(String.valueOf(secondsPerTurn));
        parameters.addContent(secondsPerTurnXML);
        
        Element winningGamesCountXML = new Element("winningGamesCount");
        winningGamesCountXML.setText(String.valueOf(winningGamesCount));
        parameters.addContent(winningGamesCountXML);
        
        Element useDoublingXML = new Element("useDoubling");
        useDoublingXML.setText(useDoubling ? "1" : "0");
        parameters.addContent(useDoublingXML);
    }
    
    public void load(Element parameters) {
        secondsPerTurn = Integer.valueOf(parameters.getChildText("secondsPerTurn"));
        winningGamesCount = Integer.valueOf(parameters.getChildText("winningGamesCount"));
        useDoubling = parameters.getChildText("useDoubling").equals("1");
    }
    
    public int getSecondsPerTurn() {
        return secondsPerTurn;
    }

    public int getWinningGamesCount() {
        return winningGamesCount;
    }

    public boolean isUseDoubling() {
        return useDoubling;
    }

    public Player getWhitePlayer() {
        return whitePlayer;
    }

    public Player getBlackPlayer() {
        return blackPlayer;
    }
    
    public Player getPlayer(SquareColor playerColor) {
        return playerColor == SquareColor.WHITE ? whitePlayer : blackPlayer;
    }

    public Player getOpponentPlayer(SquareColor playerColor) {
        return playerColor == SquareColor.WHITE ? blackPlayer : whitePlayer;
    }

    public void setBlackPlayer(Player blackPlayer) {
        this.blackPlayer = blackPlayer;
    }

    public void setWhitePlayer(Player whitePlayer) {
        this.whitePlayer = whitePlayer;
    }
    
    public GameDifficulty getDifficulty() {
        return difficulty;
    }
}
