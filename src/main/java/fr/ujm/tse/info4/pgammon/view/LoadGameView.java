package fr.ujm.tse.info4.pgammon.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdom2.JDOMException;

import fr.ujm.tse.info4.pgammon.gui.MonochromeButton;
import fr.ujm.tse.info4.pgammon.gui.MonochromeList;
import fr.ujm.tse.info4.pgammon.gui.SessionCellRenderer;
import fr.ujm.tse.info4.pgammon.models.SessionManager;
import fr.ujm.tse.info4.pgammon.models.Session;

public class LoadGameView extends JPanel {

    /**
     * This class manages the visualization of the Load Game view
     * It contains a list of sessions and a panel for session settings
     */

    private static final long serialVersionUID = 2698819973936287585L;

    private MonochromeButton startButton;
    private Collection<Session> sessionList;
    private MonochromeList<Session> sessionListView;
    private Session session;
    private ParametersPanelLoadView loadGameSettingsPanel;

    /**
     * Constructor for the class
     * @param s a list of sessions
     */
    public LoadGameView(ArrayList<Session> s, boolean isNewGame) {
        //if(!isNewGame)
        try {
            SessionManager sessionManager = SessionManager.getSessionManager();
            sessionList = sessionManager.getSessionList();
            //System.out.println(sessionList);
        } catch (IOException | JDOMException e1) {
            e1.printStackTrace();
        } finally {
            //sessionList = s;
            build();

            sessionListView.getList().addListSelectionListener(new ListSelectionListener() {

                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (sessionListView.getList().getSelectedValue() != null) {
                        session = sessionListView.getList().getSelectedValue();
                        updateData();
                    }
                }
            });
        }
    }

    /**
     * Method to update the class
     * such as updating the session list or session parameters
     */
    public void updateData() {
        loadGameSettingsPanel.setVisible(true);
        loadGameSettingsPanel.setSession(session);
        //sessionList.setListDatas(new Vector<Session>(profile.getList()));
    }

    private void build() {
        setLayout(null);
        setOpaque(false);

        //if (sessionList == 0)
        loadGameSettingsPanel = new ParametersPanelLoadView(null);
        loadGameSettingsPanel.setBounds(450, 20, 300, 400);
        add(loadGameSettingsPanel);
        loadGameSettingsPanel.setVisible(false);

        // I need to add the sessions

        sessionListView = new MonochromeList<>("Saved Sessions", sessionList, new SessionCellRenderer());
        sessionListView.setBounds(40, 20, 330, 400);
        add(sessionListView);

        startButton = new MonochromeButton("Start");
        startButton.setBounds(300, 430, 200, 50);
        add(startButton);
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

    /**
     * Getter for the start button
     * @return returns the start button class
     */
    public MonochromeButton getStartButton() {
        return startButton;
    }

    /**
     * Getter for session
     * @return returns a Session class
     */
    public Session getSession() {
        return session;
    }

    /**
     * Getter for the session settings view of the selected session
     * @return returns the session settings panel class
     */
    public ParametersPanelLoadView getParametersPanelLoadView() {
        return loadGameSettingsPanel;
    }
}
