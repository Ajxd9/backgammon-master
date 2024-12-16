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
import javax.swing.JTextField;

import ai.AI;
import networking.Network;

public class NetworkPanel extends JPanel {

	private static JComboBox aiList;
	
	static JRadioButton startServerButton;
	static JRadioButton joinServerButton;
	
	public NetworkPanel(){
		//Online Panel
		startServerButton = new JRadioButton("Start a Sever");
		this.add(startServerButton);
		JLabel hostnameLbl = new JLabel("Your host name is: " + Network.getHostName());
		this.add(hostnameLbl);
		joinServerButton = new JRadioButton("Connect to a Sever");
		this.add(joinServerButton);
		joinServerButton.setSelected(true);
		
		JLabel l5 = new JLabel("Host name:");
		this.add(l5);
		
		JTextField hostName = new JTextField(15);
		this.add(hostName);
		
		JLabel l6 = new JLabel("Port Number:");
		this.add(l6);

		JTextField portNumber = new JTextField(4);
		this.add(portNumber);
		
		JLabel l1 = new JLabel("Choose AI to send:");
		this.add(l1);
		aiList = new JComboBox<String>(AI.aiNames);
		this.add(aiList);
		aiList.setSelectedIndex(0);
		aiList.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox)e.getSource();
				int n = cb.getSelectedIndex();
				System.out.println(n);
				//change image?
			}
		});
		
		JRadioButton playAsHuman = new JRadioButton("Play as Human:");
		this.add(playAsHuman);
		JRadioButton sendAI = new JRadioButton("Send an AI:");
		this.add(sendAI);
		playAsHuman.setSelected(true);
		
		ButtonGroup group1 = new ButtonGroup();
		group1.add(playAsHuman);
		group1.add(sendAI);
		
		ButtonGroup group2 = new ButtonGroup();
		group2.add(startServerButton);
		group2.add(joinServerButton);
		
		JButton networkStartBtn = new JButton("Start Game");
		networkStartBtn.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				int aiType;
				if (sendAI.isSelected()){
					aiType = aiList.getSelectedIndex();
				} else {
					aiType = -1;
				}
				int port = 0;
				String name = hostName.getText();
				try{
					port = Integer.parseInt(portNumber.getText());	
				} catch (NumberFormatException err){
					System.err.println("Invalid port number.");
				}
				if (startServerButton.isSelected()){
					Game.startNetworkGame(aiType, port);
				} else {
					Game.joinNetworkGame(aiType, port, name);
				}
			}

		});
		this.add(networkStartBtn);
		
	}
}
