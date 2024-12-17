package fr.ujm.tse.info4.pgammon.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JFrame;

import org.jdom2.JDOMException;

import fr.ujm.tse.info4.pgammon.models.SquareColor;
import fr.ujm.tse.info4.pgammon.models.SessionManager;
import fr.ujm.tse.info4.pgammon.models.Player;
import fr.ujm.tse.info4.pgammon.models.GameParameters;
import fr.ujm.tse.info4.pgammon.view.IntermediateGameView;

public class IntermediateGameController implements Controller {

    private MainController mainController;
    private IntermediateGameController intermediateGameController;
    private boolean isNewGame;
    private IntermediateGameView gameCreationView;
    private JFrame frame;
    private SquareColor playerBeingModified;
    protected PlayerListController playerListController;
    
    public IntermediateGameController(boolean isNewGame, MainController mainController) {
        this.mainController = mainController;
        this.isNewGame = isNewGame;
        intermediateGameController = this;
        gameCreationView = new IntermediateGameView(this.isNewGame);
        mainController.getFrame().setContentPane(gameCreationView);
        frame = mainController.getFrame();
        build();
    }
    
    public void build() {
        listenerBack();
        listenerStart();
        listenerStartLoad();
        listenerSessionDeletion();
        listenerLoadPlayer1();
        listenerLoadPlayer2();
    }
    
    public void listenerSessionDeletion() {
        gameCreationView.getLoadGameView().getParametersPanelLoadView().getDeleteButton().addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {
                try {
                    SessionManager management = SessionManager.getSessionManager();
                    management.deleteSession(gameCreationView.getLoadGameView().getSession().getSessionId());
                    management.save();
                    gameCreationView.getLoadGameView().updateData();
                    gameCreationView.getLoadGameView().updateUI();

                } catch (IOException | JDOMException e1) {
                    e1.printStackTrace();
                    gameCreationView.displayRequestWindow("Small issue", "Sessions were not loaded");
                }
            }
        });
    }
    
    public void listenerBack() {
        gameCreationView.getBackButton().addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {
                gameCreationView.setVisible(false);
                mainController.back();
            }
        });
    }
    
    public void listenerStart() {
        gameCreationView.getNewSessionView().getStartButton().addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {    
                
                int time = gameCreationView.getNewSessionView().getSettingsPanel().getTimeLimit() * 1000;
                int numGames = gameCreationView.getNewSessionView().getSettingsPanel().getNumberOfGames();
                boolean doubling = gameCreationView.getNewSessionView().getSettingsPanel().getdoublingCube().isSelected();
                
                Player whitePlayer = gameCreationView.getNewSessionView().getPlayerPanel1().getPlayer();
                Player blackPlayer = gameCreationView.getNewSessionView().getPlayerPanel2().getPlayer();
                
                if(whitePlayer == null || blackPlayer == null) {
                    gameCreationView.displayRequestWindow("Oops!", "Please choose players!");
                    return;
                }
                
                if(whitePlayer == blackPlayer) {
                    gameCreationView.displayRequestWindow("Oops!", "Players chosen are the same!");
                    return;
                }
                // Creation of game parameters
                GameParameters params = new GameParameters(time, numGames, doubling, whitePlayer, blackPlayer);
                gameCreationView.setVisible(false);
                mainController.newSession(params);
            }
        });
    }
    
    public void listenerStartLoad() {
        gameCreationView.getLoadGameView().getStartButton().addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {
                if(gameCreationView.getLoadGameView().getSession() == null) {
                    gameCreationView.displayRequestWindow("Oops!", "No session selected");
                }
                else {
                    gameCreationView.setVisible(false);
                    mainController.loadSession(gameCreationView.getLoadGameView().getSession());
                }
            }
        });
    }
    
    public void listenerLoadPlayer1() {
        gameCreationView.getNewSessionView().getChangeWhitePlayerButton().addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {
                gameCreationView.setVisible(false);
                playerListController = new PlayerListController(true, intermediateGameController);
                playerBeingModified = SquareColor.WHITE;
            }
        });
    }
    
    public void listenerLoadPlayer2() {
        gameCreationView.getNewSessionView().getChangeBlackPlayerButton().addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {
                gameCreationView.setVisible(false);
                playerListController = new PlayerListController(true, intermediateGameController);
                playerBeingModified = SquareColor.BLACK;
            }
        });
    }
    
    @Override
    public Controller getController() {
        return this;
    }
    
    @Override
    public JFrame getFrame() {
        return frame;
    }

    @Override
    public void back() {
        getFrame().setContentPane(gameCreationView);
        gameCreationView.setVisible(true);
    }
    
    public void back(Player p) {
        getFrame().setContentPane(gameCreationView);
        gameCreationView.setVisible(true);
        if (playerBeingModified == SquareColor.WHITE)
            gameCreationView.getNewSessionView().setPlayer1(p);
        else
            gameCreationView.getNewSessionView().setPlayer2(p);
        // TODO update the window
    }
}
