package game;

import java.io.IOException;
import java.net.UnknownHostException;
import networking.Client;
import networking.Network;
import networking.Server;
import ai.AI;
import ai.aoi.Aoi;
import ai.homura.Homura;
import ai.miki.Miki;
import ai.random.RandomAI;
import gui.game.Window;
import gui.options.GameOptionWindow;

/**
 * The main game class.
 *
 */
public class Game {

	public static int turn = 0;
	public static int turnNumber = 0;
	public static int blackwins = 0;
	public static int whitewins = 0;
	public static Window gameWindow;
	public static final int PIECE_NUMBER = 15;
	public static final int LOCAL = 0;
	public static int winner;
	public static boolean gameOver;
	public static boolean hasStarted;

	public static boolean whiteIsHuman;
	public static boolean blackIsHuman;
	public static boolean isServer;
	public static boolean whiteIsNetwork;
	public static boolean blackIsNetwork;
	
	public static AI whiteAI, blackAI;
	public static final long sleepTime = 10;
	public static int gamesPlayed = 0;
	public static int maxGames = 100;
	
	public static Board gameBoard;
	
	public static void main(String[] args) throws InterruptedException, IOException {
		gameBoard = new Board(Board.WHITE);
		Game.gameOver = false;
		gameBoard.init();
		gameWindow = new Window(gameBoard);

		// The main game loop.
		while (true){
			while (!hasStarted){
				Thread.sleep(sleepTime*2);
			}
			System.out.println("starting game...");
			while (!gameOver){
				while (gameBoard.getTurn() == Column.WHITE && !Game.gameOver){
					if (whiteIsNetwork){
						Network.run();
						Thread.sleep(sleepTime);
					} else {
						if (whiteIsHuman){
							Thread.sleep(sleepTime);
						} else {
							whiteAI.makeMove();
							Thread.sleep(sleepTime*3);
						}						
					}
					gameWindow.repaint();
				}
				while (gameBoard.getTurn() == Column.BLACK && !Game.gameOver){
					if (blackIsNetwork){
						Network.run();
						Thread.sleep(sleepTime);
					} else {
						if (blackIsHuman){
							Thread.sleep(sleepTime);
						} else {
							blackAI.makeMove();
							Thread.sleep(sleepTime*3);
						}
					}
					gameWindow.repaint();
				}
			}
			
			if (blackIsNetwork || whiteIsNetwork ){
				Network.run();
			}
			
			if (winner == Column.BLACK){
				System.out.println("Black wins");
				if (blackIsNetwork){
					Network.addText("you-win; bye");
					Network.run();
					Network.close();
				}
				if (blackAI instanceof Homura){
					((Homura)blackAI).addWinData();
				}
				blackwins++;
			} else {
				System.out.println("White wins");
				if (whiteIsNetwork){
					Network.addText("you-win; bye");
					Network.run();
					Network.close();
				}
				if (blackAI instanceof Homura){
					((Homura)blackAI).addLoseData();					
				}
				whitewins++;
			}
			gamesPlayed++;
			if (gamesPlayed >= maxGames){
				System.out.println("Black won: " + blackwins);
				System.out.println("White won: " + whitewins);
				System.exit(0);
			}
			Game.reset();
		}
		
	}

	public static void changeTurn() {

		gameBoard.setSelected(null);
		Move.message = Move.message.substring(0, Move.message.length() - 1) + ";";
		Network.addText(Move.message);
		if (!Game.gameOver){
			if(gameBoard.getAll()[0].getPieces().size() == Game.PIECE_NUMBER){
				Game.winner = Column.BLACK;
				Game.gameOver = true;

			} else if (gameBoard.getAll()[25].getPieces().size() == Game.PIECE_NUMBER) {
				Game.winner = Column.WHITE;
				Game.gameOver = true;
			}
			gameBoard.changeTurn();

			gameWindow.repaint();
			if (
					(Game.blackIsNetwork && gameBoard.getTurn() == Board.BLACK) ||
					(Game.whiteIsNetwork && gameBoard.getTurn() == Board.WHITE)
					){
				System.out.println("waiting for other player...");
			} else {
				Move.rollDice(gameBoard);
				Move.setPossibleMoves(gameBoard);
				// If there are no valid moves, pass the turn.
				if (Move.possibleMoves.isEmpty()){
					Move.message = Move.message + "(-1|-1);";
					Game.changeTurn();
				}				
			}
			gameBoard.setSelected(null);
			gameBoard.unSelect();
		}
	}

	public static void reset(){
		gameOver = false;
		hasStarted = false;
		turnNumber = 0;
	}

	public static void startGame(){
		gameBoard.init();
		gameWindow.reset();
		gameBoard.addPieces();
		gameBoard.changeTurn(Board.WHITE);
		Move.rollDice(gameBoard);
		if (GameOptionWindow.optionsMenu != null){
			GameOptionWindow.optionsMenu.dispose();			
		}
		gameWindow.repaint();
		Game.hasStarted = true;
	}

	public static void startLocalGame() {
		whiteIsHuman = true;
		blackIsHuman = true;
		startGame();
		
	}

	public static void startAIGame(int AIType, boolean playAsWhite) {
		if (playAsWhite){
			whiteIsHuman = true;
			blackIsHuman = false;
			
			blackAI = makeAI(AIType);
			
		} else {
			whiteIsHuman = false;
			blackIsHuman = true;
			
			whiteAI = makeAI(AIType);
			
		}
		startGame();
	}

	public static void startNetworkGame(int aiType, int port) {
		blackIsNetwork = true;
		if (aiType != -1){	
			whiteIsHuman = false;
			whiteAI = makeAI(aiType);
		} else {
			whiteIsHuman = true;	
		}
		
		try {
			Server.start(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		startGame();
	}

	/**
	 * Joins a network game as a client.
	 * In this implementation, white will always be the server, and black will always be the client.
	 */
	public static void joinNetworkGame(int aiType, int port, String hostName) {
		whiteIsNetwork = true;
		if (aiType != -1){
			blackIsHuman = false;
			blackAI = makeAI(aiType);
		} else {
			blackIsHuman = true;
		}
		
		try {
			Client.start(hostName, port);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		startGame();
	}

	public static void startLocalAIGame(int AIType1, int AIType2) {
		whiteIsHuman = false;
		blackIsHuman = false;
		
		whiteAI = makeAI(AIType1);
		blackAI = makeAI(AIType2);
		
		startGame();
	}

	public static AI makeAI(int type){
		AI ai = null;
		switch(type){
		case AI.AoiIndex:
			return new Aoi();
		case AI.HomuraIndex:
			return new Homura();
		case AI.RandomIndex:
			return new RandomAI();
		case AI.MikiIndex:
			return new Miki();
		}
		return ai;
	}
	
}
