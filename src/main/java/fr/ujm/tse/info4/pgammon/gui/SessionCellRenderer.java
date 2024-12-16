package fr.ujm.tse.info4.pgammon.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import fr.ujm.tse.info4.pgammon.models.CellColor;
import fr.ujm.tse.info4.pgammon.models.Session;

public class SessionCellRenderer extends JPanel implements ListCellRenderer<Session> {
    private static final long serialVersionUID = 2809364114788243599L;

    JLabel player1Label;
    JLabel player2Label;
    ScoreDisplay player1ScoreDisplay;
    ScoreDisplay player2ScoreDisplay;

    public SessionCellRenderer() {
        setLayout(null);
        setPreferredSize(new Dimension(100, 60));

        player1ScoreDisplay = new ScoreDisplay(0, CellColor.WHITE);
        player1ScoreDisplay.setBounds(290, 0, 25, 25);
        add(player1ScoreDisplay);

        player1Label = new JLabel();
        player1Label.setBounds(10, 0, 300, 30);
        player1Label.setFont(new Font("Arial", Font.BOLD, 18));
        add(player1Label);

        player2Label = new JLabel();
        player2Label.setBounds(10, 30, 300, 30);
        player2Label.setFont(new Font("Arial", Font.BOLD, 18));
        add(player2Label);

        player2ScoreDisplay = new ScoreDisplay(0, CellColor.BLACK);
        player2ScoreDisplay.setBounds(290, 30, 25, 25);
        add(player2ScoreDisplay);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Session> list,
                                                  Session session, int index, boolean isSelected, boolean cellHasFocus) {
        player1Label.setForeground(new Color(0xCCCCCC));
        player2Label.setForeground(new Color(0xCCCCCC));
        Color backgroundColor;

        if (isSelected) {
            backgroundColor = cellHasFocus ? new Color(0x333333) : new Color(0x252525);
        } else {
            backgroundColor = cellHasFocus ? new Color(0x202020) : new Color(0x111111);
        }
        setBackground(backgroundColor);
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0x555555)));

        try {
            player1Label.setText(session.getCurrentGame().getGameSettings().getWhitePlayer().getPseudo());
            player2Label.setText(session.getCurrentGame().getGameSettings().getBlackPlayer().getPseudo());
            setAlignmentX(0.5f);
            player1ScoreDisplay.setScore(session.getScores().get(session.getCurrentGame().getGameSettings().getWhitePlayer()));
            player2ScoreDisplay.setScore(session.getScores().get(session.getCurrentGame().getGameSettings().getBlackPlayer()));
        } catch (Exception e) {
            player1Label.setText("");
            player2Label.setText("");
            setAlignmentX(0.5f);
            player1ScoreDisplay.setScore(0);
            player2ScoreDisplay.setScore(0);
        }

        return this;
    }
}
