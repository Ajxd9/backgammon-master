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

import fr.ujm.tse.info4.pgammon.models.Player;

public class PlayerCellRenderer extends JPanel implements ListCellRenderer<Player> {
    private static final long serialVersionUID = 2419031075848394031L;
    JLabel label;
    ImageAvatar playerImage;
    Player player;

    public PlayerCellRenderer() {
        label = new JLabel();
        player = new Player();

        setLayout(null);
        setPreferredSize(new Dimension(100, 45));

        playerImage = new ImageAvatar(player.getImageSource());
        playerImage.setBounds(2, 6, 30, 30);
        add(playerImage);

        label.setBounds(40, 0, 250, 45);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        add(label);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Player> list,
            Player p, int index, boolean isSelected, boolean cellHasFocus) {
        label.setForeground(new Color(0xCCCCCC));
        Color bgColor;
        if (isSelected)
            if (cellHasFocus)
                bgColor = new Color(0x333333);
            else
                bgColor = new Color(0x252525);
        else if (cellHasFocus)
            bgColor = new Color(0x202020);
        else
            bgColor = new Color(0x111111);

        setBackground(bgColor);
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0x555555)));
        label.setText(p.getUsername());
        setAlignmentX(0.5f);
        player = p;
        playerImage.setPath(p.getImageSource());
        return this;
    }
}
