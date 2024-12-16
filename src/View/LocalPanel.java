package View;

import game.Game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class LocalPanel extends JPanel {

	public LocalPanel(){
		JButton localPlayerStartBtn = new JButton("Start Game");
		localPlayerStartBtn.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				Game.startLocalGame();
			}
		});
		this.add(localPlayerStartBtn);
	}
}
