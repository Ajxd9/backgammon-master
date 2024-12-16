package fr.ujm.tse.info4.pgammon.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JFrame;

import org.jdom2.JDOMException;

import fr.ujm.tse.info4.pgammon.models.CaseColor;
import fr.ujm.tse.info4.pgammon.models.SessionManagement;
import fr.ujm.tse.info4.pgammon.models.Player;
import fr.ujm.tse.info4.pgammon.models.GameParameters;
import fr.ujm.tse.info4.pgammon.views.IntermediateGameView;

public class IntermediateGameController implements Controller {

    private MainController mainController;
    private IntermediateGameController intermediateGameController;
    private boolean isNewGame;
    private IntermediateGameView gameCreationView;
    private JFrame frame;
    private CaseColor playerBeingModified;
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
        gameCreationView.getLoadGameView().getLoadViewParametersPanel().getDeleteButton().addMouseListener(new MouseListener() {

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
                    SessionManagement management = SessionManagement.getSessionManagement();
                    management.deleteSession(gameCreationView.getLoadGameView().getSession().getSessionId());
                    management.save();
                    gameCreationView.getLoadGameView().updateData();
                    gameCreationView.getLoadGameView().updateUI();

                } catch (IOException | JDOMException e1) {
                    e1.printStackTrace();
                    gameCreationView.showRequestWindow("Small issue", "Sessions were not loaded");
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
                
                int time = gameCreationView.getNewSessionView().getParameterPanel().getTimeAmount() * 1000;
                int numGames = gameCreationView.getNewSessionView().getParameterPanel().getNumGames();
                boolean doubling = gameCreationView.getNewSessionView().getParameterPanel().getDoubling().isSelected();
                
                Player whitePlayer = gameCreationView.getNewSessionView().getPlayer1Panel().getPlayer();
                Player blackPlayer = gameCreationView.getNewSessionView().getPlayer2Panel().getPlayer();
                
                if(whitePlayer == null || blackPlayer == null) {
                    gameCreationView.showRequestWindow("Oops!", "Please choose players!");
                    return;
                }
                
                if(whitePlayer == blackPlayer) {
                    gameCreationView.showRequestWindow("Oops!", "Players chosen are the same!");
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
                    gameCreationView.showRequestWindow("Oops!", "No session selected");
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
                playerBeingModified = CaseColor.WHITE;
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
                playerBeingModified = CaseColor.BLACK;
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
        if (playerBeingModified == CaseColor.WHITE)
            gameCreationView.getNewSessionView().setPlayer1(p);
        else
            gameCreationView.getNewSessionView().setPlayer2(p);
        // TODO update the window
    }
}
