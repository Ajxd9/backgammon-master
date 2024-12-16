package View;


import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class GameOptionWindow extends JFrame {

	public static GameOptionWindow optionsMenu;
	static final String LOCAL = "Versus local player";
	static final String LOCAL_AI = "Versus local AI";
	static final String ONLINE = "Versus online player";

	public GameOptionWindow(){
		super("CS1006 Backgammon Options");
		optionsMenu = this;
		this.setPreferredSize(new Dimension(500,300));

		JPanel localPanel = new LocalPanel();
		JPanel AIPanel = new AIPanel();
		JPanel OnlinePanel = new NetworkPanel();

		JTabbedPane cards = new JTabbedPane();
		cards.add(localPanel,LOCAL);
		cards.add(AIPanel,LOCAL_AI);
		cards.add(OnlinePanel,ONLINE);

		this.add(cards);
		this.pack();
		this.setVisible(true);
	}
}
