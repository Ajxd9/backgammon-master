package fr.ujm.tse.info4.pgammon.view;

import java.awt.Color;
import javax.swing.JPanel;

public class CompletedGameViewBottom extends JPanel {
    /**
     * This class displays the game replay by calling the replay bar.
     */

    private static final long serialVersionUID = -4862879609262291182L;
    private ReplayBar replayBar;

    public CompletedGameViewBottom() {
        build();
    }

    private void build() {
        setLayout(null);
        setBackground(Color.BLACK);
        replayBar = new ReplayBar(null);
        replayBar.setBounds(0, 0, 800, 300);
        add(replayBar);
    }
    
    /**
     * Getter for the replay bar.
     * @return returns the replay bar class.
     */
    public ReplayBar getReplayBar() {
        return replayBar;
    }
}
