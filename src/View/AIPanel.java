package View;

import game.Game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import ai.AI;

public class AIPanel extends JPanel {
	
	private static JRadioButton whiteButton, blackButton, observerButton;
	private static JComboBox<String> aiList1, aiList2;
		
	public AIPanel(){
		JLabel l1 = new JLabel("Choose AI to play against:");
		this.add(l1);
		aiList1 = new JComboBox<String>(AI.aiNames);
		this.add(aiList1);
		aiList1.setSelectedIndex(0);
		aiList1.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				JComboBox<String> cb = (JComboBox<String>)e.getSource();
				int n = cb.getSelectedIndex();
				System.out.println(n);
				//change image?
			}
		});
		JLabel l2 = new JLabel("Choose second AI to play against (Only for observe mode):");
		this.add(l2);
		aiList2 = new JComboBox<String>(AI.aiNames);
		this.add(aiList2);
		aiList2.setSelectedIndex(0);
		aiList2.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				JComboBox<String> cb = (JComboBox<String>)e.getSource();
				int n = cb.getSelectedIndex();
				System.out.println(n);
				//change image?
			}
		});
		
		whiteButton = new JRadioButton("Play as white");
		this.add(whiteButton);
		blackButton = new JRadioButton("Play as black");
		this.add(blackButton);
		observerButton = new JRadioButton("Observe two AI");
		this.add(observerButton);
		
		ButtonGroup group = new ButtonGroup();
		group.add(whiteButton);
		group.add(blackButton);
		group.add(observerButton);
		whiteButton.setSelected(true);

		JButton aiStartBtn = new JButton("Start Game");
		aiStartBtn.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				if (observerButton.isSelected()){
					Game.startLocalAIGame(aiList1.getSelectedIndex(), aiList2.getSelectedIndex());
				} else {
					Game.startAIGame(aiList1.getSelectedIndex(),whiteButton.isSelected());					
				}
			}

		});
		this.add(aiStartBtn);
	}
}
