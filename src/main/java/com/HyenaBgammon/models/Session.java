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
        try {
            Element sessionElement = root.getChild("session");
            if (sessionElement == null) {
                System.err.println("Error: Missing 'session' element in XML.");
                return false;
            }

            // Parse session ID
            String sessionIdStr = sessionElement.getAttributeValue("id");
            if (sessionIdStr == null) {
                System.err.println("Error: Missing 'id' attribute in session element.");
                return false;
            }
            sessionId = Integer.parseInt(sessionIdStr);

            // Parse session state
            String sessionStateText = sessionElement.getChildText("sessionState");
            if (sessionStateText == null) {
                System.err.println("Error: Missing 'sessionState' element.");
                return false;
            }
            switch (sessionStateText) {
                case "CONFIGURATION":
                    sessionState = SessionState.CONFIGURATION;
                    break;
                case "IN_PROGRESS":
                    sessionState = SessionState.IN_PROGRESS;
                    break;
                case "FINISHED":
                    sessionState = SessionState.FINISHED;
                    break;
                default:
                    System.err.println("Error: Invalid session state: " + sessionStateText);
                    return false;
            }

            // Parse maxGameId
            String maxGameIdText = sessionElement.getChildText("maxGameId");
            if (maxGameIdText == null) {
                System.err.println("Error: Missing 'maxGameId' element.");
                return false;
            }
            maxGameId = Integer.parseInt(maxGameIdText);

            // Parse previousGamePlayerColor
            String previousColorText = sessionElement.getChildText("previousGamePlayerColor");
            if (previousColorText == null || "null".equalsIgnoreCase(previousColorText)) {
                previousGamePlayerColor = SquareColor.EMPTY; // Default to EMPTY for null or missing
            } else {
                switch (previousColorText) {
                    case "WHITE":
                        previousGamePlayerColor = SquareColor.WHITE;
                        break;
                    case "BLACK":
                        previousGamePlayerColor = SquareColor.BLACK;
                        break;
                    default:
                        System.err.println("Error: Invalid player color: " + previousColorText);
                        return false;
                }
            }

            // Parse gameDifficulty
            String gameDifficultyText = sessionElement.getChildText("gameDifficulty");
            if (gameDifficultyText == null) {
                System.err.println("Error: Missing 'gameDifficulty' element.");
                return false;
            }
            GameDifficulty tmpGameDiff;
            try {
                tmpGameDiff = GameDifficulty.valueOf(gameDifficultyText);
            } catch (IllegalArgumentException e) {
                System.err.println("Error: Invalid game difficulty: " + gameDifficultyText);
                return false;
            }

            // Parse blackPlayer
            Element playersElement = sessionElement.getChild("players");
            if (playersElement == null) {
                System.err.println("Error: Missing 'players' element.");
                return false;
            }
            Element blackPlayerElement = playersElement.getChild("blackPlayer");
            if (blackPlayerElement == null) {
                System.err.println("Error: Missing 'blackPlayer' element.");
                return false;
            }
            String blackPlayerIdText = blackPlayerElement.getAttributeValue("id");
            Player blackPlayer = Profiles.getProfiles().getPlayer(Integer.parseInt(blackPlayerIdText));
            if (blackPlayer == null) {
                System.err.println("Error: Black player not found.");
                return false;
            }

            // Parse blackPlayer score
            String blackPlayerScoreText = blackPlayerElement.getChildText("score");
            scores = new HashMap<>();
            scores.put(blackPlayer, Integer.parseInt(blackPlayerScoreText));

            // Parse whitePlayer
            Element whitePlayerElement = playersElement.getChild("whitePlayer");
            if (whitePlayerElement == null) {
                System.err.println("Error: Missing 'whitePlayer' element.");
                return false;
            }
            String whitePlayerIdText = whitePlayerElement.getAttributeValue("id");
            Player whitePlayer = Profiles.getProfiles().getPlayer(Integer.parseInt(whitePlayerIdText));
            if (whitePlayer == null) {
                System.err.println("Error: White player not found.");
                return false;
            }

            // Parse whitePlayer score
            String whitePlayerScoreText = whitePlayerElement.getChildText("score");
            scores.put(whitePlayer, Integer.parseInt(whitePlayerScoreText));

            // Load game parameters
            gameParameters = new GameParameters();
            Element parametersElement = sessionElement.getChild("parameters");
            if (parametersElement == null) {
                System.err.println("Error: Missing 'parameters' element.");
                return false;
            }
            gameParameters.load(parametersElement);
            gameParameters.setWhitePlayer(whitePlayer);
            gameParameters.setBlackPlayer(blackPlayer);
            gameParameters.setDifficulty(tmpGameDiff);

            // Load current game
            Element gameElement = sessionElement.getChild("game");
            if (gameElement == null) {
                System.err.println("Error: Missing 'game' element.");
                return false;
            }


            return true;
        } catch (NumberFormatException e) {
            System.err.println("Error: Invalid number format. " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            return false;
        }
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
