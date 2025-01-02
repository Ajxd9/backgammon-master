package com.HyenaBgammon.models;

import java.util.HashMap;
import org.jdom2.Attribute;
import org.jdom2.Element;

public class Session {
    private int sessionId;
    private int maxGameId;
    private Game currentGame;

    private Player sessionWinner;
    private SquareColor previousGamePlayerColor;
    private HashMap<Player, Integer> scores;
    private SessionState sessionState;
    private GameParameters gameParameters;
    
    public Session() {}
    
    public Session(int sessionId, GameParameters gameParameters) {
        previousGamePlayerColor = null;
        this.sessionId = sessionId;
        maxGameId = 1;
        sessionState = SessionState.CONFIGURATION;
        this.gameParameters = gameParameters;
        scores = new HashMap<Player, Integer>();
        scores.put(gameParameters.getWhitePlayer(), 0);
        scores.put(gameParameters.getBlackPlayer(), 0);
        sessionWinner = null;
        newGame();
    }
    
    public void endSession() {
        sessionWinner.getStats().addWin();
        
        if (sessionWinner == gameParameters.getWhitePlayer())
            gameParameters.getBlackPlayer().getStats().addLoss();
        else
            gameParameters.getWhitePlayer().getStats().addLoss();
    }
    
    public void newGame() {
        maxGameId++;
        currentGame = new Game(maxGameId, gameParameters);
    }
    
    public void StartGame() {
        if (previousGamePlayerColor == null)
            currentGame.startFirstGame();
        else
            currentGame.startNewGame(previousGamePlayerColor);
    }
    
    public void endGame() {
        previousGamePlayerColor = currentGame.getFirstPlayer();
        int doubling = currentGame.getDoublingCube().getValue();
        SquareColor winningColor = currentGame.getCurrentPlayer();
        currentGame.endGame();
        scores.put(gameParameters.getPlayer(winningColor), 
                  scores.get(gameParameters.getPlayer(winningColor)) + doubling);
    }
    
    public void endSession(Player sessionWinner) {
        this.sessionWinner = sessionWinner;
        endSession();
    }
    
    public Player getBestPlayer() {
        if (scores.get(gameParameters.whitePlayer) > scores.get(gameParameters.blackPlayer))
            return gameParameters.whitePlayer;
        else if (scores.get(gameParameters.whitePlayer) < scores.get(gameParameters.blackPlayer))
            return gameParameters.blackPlayer;
        else
            return null;
    }

    public boolean checkSessionEnd() {
        if(gameParameters.getWinningGamesCount() == 0)
            return false;
        if(scores.get(gameParameters.getWhitePlayer()) >= gameParameters.getWinningGamesCount()) {
            sessionState = SessionState.FINISHED;
            sessionWinner = gameParameters.getWhitePlayer();
            return true;
        }
        else if(scores.get(gameParameters.getBlackPlayer()) >= gameParameters.getWinningGamesCount()) {
            sessionState = SessionState.FINISHED;
            sessionWinner = gameParameters.getWhitePlayer();
            return true;
        }
        return false;
    }
    
    /**
     * Save all information under this root 
     * @param root is the "Sessions" root in the XML file
     */
    public void save(Element root) {
        Element session = new Element("session");
        root.addContent(session);
        
        Attribute idsession = new Attribute("id", String.valueOf(sessionId));
        session.setAttribute(idsession);
        
        Element sessionStateXML = new Element("sessionState");
        sessionStateXML.setText(String.valueOf(sessionState));
        session.addContent(sessionStateXML);
        
        Element gameDiffXML = new Element("gameDifficulty");
        gameDiffXML.setText(String.valueOf(gameParameters.getDifficulty()));
        session.addContent(gameDiffXML);
        
        Element maxGameIdXML = new Element("maxGameId");
        maxGameIdXML.setText(String.valueOf(maxGameId));
        session.addContent(maxGameIdXML);
        
        Element previousGamePlayerColorXML = new Element("previousGamePlayerColor");
        previousGamePlayerColorXML.setText(String.valueOf(previousGamePlayerColor));
        session.addContent(previousGamePlayerColorXML);

        Element playersXML = new Element("players");
        session.addContent(playersXML);
        
        Element blackPlayerXML = new Element("blackPlayer");
        playersXML.addContent(blackPlayerXML);
        
        Attribute idBlack = new Attribute("id", String.valueOf(gameParameters.getBlackPlayer().getId()));
        blackPlayerXML.setAttribute(idBlack);
        
        Element scoreBlackXML = new Element("score");
        scoreBlackXML.setText(String.valueOf(scores.get(gameParameters.getBlackPlayer())));
        blackPlayerXML.addContent(scoreBlackXML);    
            
        Element whitePlayerXML = new Element("whitePlayer");
        playersXML.addContent(whitePlayerXML);
            
        Attribute idWhite = new Attribute("id", String.valueOf(gameParameters.getWhitePlayer().getId()));
        whitePlayerXML.setAttribute(idWhite);
            
        Element scoreWhiteXML = new Element("score");
        scoreWhiteXML.setText(String.valueOf(scores.get(gameParameters.getWhitePlayer())));
        whitePlayerXML.addContent(scoreWhiteXML);
        
        gameParameters.save(session);
        currentGame.save(session);
    }
    
    /**
     * Load all information under this root 
     * @param root is the "Sessions" root in the XML file
     */
    public boolean load(Element root) {
        sessionId = Integer.valueOf(root.getChild("session").getAttributeValue("id"));
        switch(root.getChild("session").getChildText("sessionState")) {
            case "CONFIGURATION": sessionState = SessionState.CONFIGURATION; break;
            case "IN_PROGRESS": sessionState = SessionState.IN_PROGRESS; break;
            case "FINISHED": sessionState = SessionState.FINISHED;
        }
        maxGameId = Integer.valueOf(root.getChild("session").getChildText("maxGameId"));
        switch(root.getChild("session").getChildText("previousGamePlayerColor")) {
            case "WHITE": previousGamePlayerColor = SquareColor.WHITE; break;
            case "BLACK": previousGamePlayerColor = SquareColor.BLACK; break;
            case "EMPTY": previousGamePlayerColor = SquareColor.EMPTY;
        }
        
        GameDifficulty tmpGameDiff = GameDifficulty.valueOf(root.getChild("session").getChildText("gameDifficulty"));
            
        int tmpID = Integer.valueOf(root.getChild("session").getChild("players").getChild("blackPlayer").getAttributeValue("id"));
            
        scores = new HashMap<Player, Integer>();
            
        Profiles profiles = Profiles.getProfiles();
        Player blackPlayer = profiles.getPlayer(tmpID);
        if (blackPlayer == null)
            return false;
        scores.put(blackPlayer, Integer.valueOf(root.getChild("session").getChild("players").getChild("blackPlayer").getChildText("score")));
            
        tmpID = Integer.valueOf(root.getChild("session").getChild("players").getChild("whitePlayer").getAttributeValue("id"));
        Player whitePlayer = profiles.getPlayer(tmpID);
        if (whitePlayer == null)
            return false;
        scores.put(whitePlayer, Integer.valueOf(root.getChild("session").getChild("players").getChild("whitePlayer").getChildText("score")));
            
        gameParameters = new GameParameters();
            
        gameParameters.load(root.getChild("session").getChild("parameters"));
        gameParameters.setWhitePlayer(whitePlayer);
        gameParameters.setBlackPlayer(blackPlayer);
        gameParameters.setDifficulty(tmpGameDiff);
            
        currentGame = new Game(gameParameters);
        currentGame.load(root.getChild("session").getChild("game"));
        return true;
    }

    public int getSessionId() {
        return sessionId;
    }
    
    public Game getCurrentGame() {
        return currentGame;
    }

    public Player getSessionWinner() {
        return sessionWinner;
    }
    
    public HashMap<Player, Integer> getScores() {
        return scores;
    }

    public boolean isSessionFinished() {    
        if (sessionWinner == null)
            return false;
        else
            return true;    
    }

    public GameParameters getGameParameters() {
        return gameParameters;
    }
}
