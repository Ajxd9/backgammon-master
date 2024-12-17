package fr.ujm.tse.info4.pgammon.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JLabel;

import fr.ujm.tse.info4.pgammon.gui.MonochromeButton;
import fr.ujm.tse.info4.pgammon.gui.MonochromeView;
import fr.ujm.tse.info4.pgammon.models.Session;

public class IntermediateGameView extends MonochromeView {

    /**
     * This class manages the New Session and Load Session views
     */
    private static final long serialVersionUID = -8524922151654361657L;

    private NewSessionView newSessionView;
    private LoadGameView loadGameView;

    private Boolean windowToDisplay;

    private MonochromeButton resumeButton;
    private MonochromeButton backButton;

    private MonochromeButton newGameButton;

    private JLabel newSessionText;
    private JLabel loadSessionText;

    ArrayList<Session> sessionList;

    // Parameter passing to determine which window to display
    /**
     * Method to switch views based on a boolean value
     * @param window boolean defining which window to choose
     */
    public IntermediateGameView(Boolean window) {

        windowToDisplay = window;
        build();
        changeSessionText();

    }

    /**
     * Getter for the window to display
     * @return returns a boolean
     */
    public Boolean getWindowToDisplay() {
        return windowToDisplay;
    }

    /**
     * Setter for window to display
     * @param windowToDisplay changes the boolean based on the window to display
     */
    public void setWindowToDisplay(Boolean windowToDisplay) {
        this.windowToDisplay = windowToDisplay;
        changeSessionText();
    }

    /**
     * Getter for the resume session button
     * @return returns the resume session button class
     */
    public MonochromeButton getResumeButton() {
        return resumeButton;
    }

    /**
     * Getter for the new session button
     * @return returns the new session button class
     */
    public MonochromeButton getNewGameButton() {
        return newGameButton;
    }

    private void build() {
        setLayout(null);
        setOpaque(false);

        sessionList = new ArrayList<>();

        newSessionView = new NewSessionView();
        newSessionView.setBounds(0, 80, 794, 495);
        add(newSessionView);

        loadGameView = new LoadGameView(sessionList, windowToDisplay);
        loadGameView.setBounds(0, 80, 794, 495);
        add(loadGameView);

        backButton = new MonochromeButton("Return");
        backButton.setBounds(550, 15, 200, 50);
        add(backButton);

        // Problem with button action, so removing them
        resumeButton = new MonochromeButton("Load");
        resumeButton.setBounds(300, 15, 200, 50);
        add(resumeButton);
        resumeButton.setVisible(false);

        newGameButton = new MonochromeButton("New Game");
        newGameButton.setBounds(50, 15, 200, 50);
        add(newGameButton);
        newGameButton.setVisible(false);

        if (windowToDisplay == true) {
            newSessionView.setVisible(true);

            loadGameView.setVisible(true);
            loadGameView.setVisible(false);
        } else {
            newSessionView.setVisible(false);
            loadGameView.setVisible(true);
        }

        newSessionText = new JLabel("New session");
        newSessionText.setBounds(50, 15, 400, 50);
        newSessionText.setFont(new Font("Arial", Font.BOLD, 36));
        newSessionText.setForeground(new Color(0xCCCCCC));
        add(newSessionText);

        loadSessionText = new JLabel("Load Session");
        loadSessionText.setBounds(50, 15, 400, 50);
        loadSessionText.setFont(new Font("Arial", Font.BOLD, 36));
        loadSessionText.setForeground(new Color(0xCCCCCC));
        add(loadSessionText);

        resumeButtonListener();
        newGameButtonListener();

    }

    private void changeSessionText() {
        if (windowToDisplay == true) {
            newSessionText.setVisible(true);
            loadSessionText.setVisible(false);
        } else {
            newSessionText.setVisible(false);
            loadSessionText.setVisible(true);
        }
    }

    private void newGameButtonListener() {
        newGameButton.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
                if (windowToDisplay == false) {
                    newSessionView.setVisible(true);
                    loadGameView.setVisible(false);
                    windowToDisplay = true;
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseClicked(MouseEvent e) {}
        });
    }

    private void resumeButtonListener() {
        resumeButton.addMouseListener(new MouseListener() {

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
                if (windowToDisplay == true) {
                    newSessionView.setVisible(false);
                    loadGameView.setVisible(true);
                    windowToDisplay = false;
                }
            }
        });
    }

    /**
     * Getter for the new session view
     * @return returns the new session view class
     */
    public NewSessionView getNewSessionView() {
        return newSessionView;
    }

    /**
     * Getter for the load game or resume session view
     * @return returns the load game or resume session view class
     */
    public LoadGameView getLoadGameView() {
        return loadGameView;
    }

    /**
     * Getter for the return button
     * @return returns the return button class
     */
    public MonochromeButton getBackButton() {
        return backButton;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Paint p;
        int h = getHeight();
        int w = getWidth();

        // Background
        p = new RadialGradientPaint(new Point2D.Double(getWidth() / 2.0, getHeight() / 2.0),
                getHeight(),
                new float[]{0.0f, 0.8f},
                new Color[]{new Color(0x333333), new Color(0x000000)},
                RadialGradientPaint.CycleMethod.NO_CYCLE);

        g2.setPaint(p);
        g2.fillRect(0, 0, w, h);

        // Border
        p = new Color(0x808080);
        g2.setStroke(new BasicStroke(5.0f));
        g2.setPaint(p);
        g2.drawRect(2, 0, w - 5, h - 5);

        g2.dispose();

    }

}
