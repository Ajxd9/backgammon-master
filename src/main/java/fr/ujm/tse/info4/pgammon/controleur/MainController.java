// 
//
//  @ Project : Project Gammon
//  @ File : MainController.java
//  @ Date : 12/12/2012
//  @ Authors : DONG Chuan, BONNETTO Benjamin, FRANCON Adrien, POTHELUNE Jean-Michel
//
//

package fr.ujm.tse.info4.pgammon.controller;

import java.awt.Container;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;

import javax.swing.JFrame;

import fr.ujm.tse.info4.pgammon.models.Master;
import fr.ujm.tse.info4.pgammon.models.GameParameter;
import fr.ujm.tse.info4.pgammon.models.Session;
import fr.ujm.tse.info4.pgammon.views.GameIntermediateView;
import fr.ujm.tse.info4.pgammon.views.MenuView;

public class MainController implements Controller {

    private MenuView menuView;
    private Session session;
    private Master master;
    private JFrame frame;
    private GameIntermediateView gameCreation;
    private MainController mainController;
    protected GameIntermediateController gameIntermediateController;
    protected PlayerListController playerListController;
    
    public MainController(Master master) {
        this.master = master;
        mainController = this;
        frame = new JFrame("Backgammon Game");
        session = null;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        Container panel = frame.getContentPane();
        panel.setLayout(new FlowLayout());

        menuView = new MenuView();
        frame.setContentPane(menuView);
    
        frame.setVisible(true);

        build();
    }

    public void build() {
        listenerQuitButton();
        listenerNewGameButton();
        listenerResumeGameButton();
        listenerAddButton();
        listenerHelpButton();
    }

    /**
     * Listener for "New Session" button
     */
    private void listenerNewGameButton() {
        menuView.getNewGameButton().addMouseListener(new MouseListener() {
            
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {}        
            @Override
            public void mouseExited(MouseEvent e) {}            
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseClicked(MouseEvent e) {
                menuView.setVisible(false);
                gameIntermediateController = new GameIntermediateController(true, mainController);    
            }
        });
    }
    
    private void listenerResumeGameButton() {
        menuView.getResumeGameButton().addMouseListener(new MouseListener() {
            
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {}        
            @Override
            public void mouseExited(MouseEvent e) {}            
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseClicked(MouseEvent e) {
                menuView.setVisible(false);
                gameIntermediateController = new GameIntermediateController(false, mainController);
            }
        });
    }
    
    private void listenerAddButton() {
        menuView.getAddButton().addMouseListener(new MouseListener() {
            
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {}        
            @Override
            public void mouseExited(MouseEvent e) {}            
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseClicked(MouseEvent e) {
                menuView.setVisible(false);
                playerListController = new PlayerListController(false, mainController);    
            }
        });
    }
    
    /**
     * 
     * listenerQuitButton
     * 
     */
    private void listenerQuitButton() {
        menuView.getQuitButton().addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.dispose();
            }
        });
    }
    
    private void listenerHelpButton() {
        menuView.getHelpButton().addMouseListener(new MouseListener() {
            
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {}        
            @Override
            public void mouseExited(MouseEvent e) {}            
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseClicked(MouseEvent e) {
               
                URI uri = URI.create("http://www.google.fr/url?sa=t&rct=j&q=&esrc=s&source=web&cd=1&ved=0CEoQFjAA&url=http%3A%2F%2Ffr.wikipedia.org%2Fwiki%2FBackgammon&ei=R0XUUM6YL4yHhQelkYHYBQ&usg=AFQjCNEOHnc7riItGN_di3jAPILrXp9twA&sig2=uesTfMvnLMwYI8reGb-vWw&bvm=bv.1355534169,d.ZG4&cad=rja");
                try {
                    Desktop.getDesktop().browse(uri);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    @Override
    public JFrame getFrame() {
        return frame;
    }
    
    @Override
    public void back() {
        frame.setContentPane(menuView);
        menuView.setVisible(true);
    }
    
    public void newSession(GameParameter gameParameter) {
        master.launchSession(gameParameter);
        session = master.getSession();
        session.StartGame();
        GameController gameController = new GameController(session, this);
        
        frame.setContentPane(gameController.getGameView());
    }
    
    public void endSession() {
        master.stopSession(session.getSessionId());
        //session = master.getSession();
        //session.StartGame();
        //GameController gameController = new GameController(session, this);
        
        //frame.setContentPane(gameController.getGameView());
    }
    
    public void loadSession(Session session) {
        master.addSession(session);
        this.session = session;
        //session.StartGame();
        GameController gameController = new GameController(session, this);
        
        frame.setContentPane(gameController.getGameView());
    }

    @Override
    public Controller getController() {
        return this;
    }

    public GameIntermediateView getGameCreation() {
        return gameCreation;
    }

    public void setGameCreation(GameIntermediateView gameCreation) {
        this.gameCreation = gameCreation;
    }
}
