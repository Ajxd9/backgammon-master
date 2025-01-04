package com.HyenaBgammon.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;

public class RulesView extends MonochromeView {

    private static final long serialVersionUID = 1L;
    private MonochromeLabel titleLabel;
    private MonochromeButton backButton;
    private JTextArea rulesTextArea;

    public RulesView(ActionListener backListener) {
        setLayout(null);
        build(backListener);
    }

    private void build(ActionListener backListener) {
        
    	// Title 
        titleLabel = new MonochromeLabel("Game Rules");
        titleLabel.setFont(titleLabel.getFont().deriveFont(36f));
        titleLabel.setBounds(250, 50, 300, 50);
        add(titleLabel);

        // Back Button
        backButton = new MonochromeButton("Back");
        backButton.setBounds(30, 52, 100, 40);
        backButton.addActionListener(backListener);
        add(backButton);

        // Rules Text Area
        rulesTextArea = new JTextArea();
        rulesTextArea.setText("Here are the rules of our Backgammon: \n\n"
        		+ "------------------------------------Easy game-----------------------------------\n\n"
        		+ "basically normal Backgammon, \n\n"
        		+ "Objective: Move all your checkers into your home board and then remove them from the board.\n"
        		+ "The first player to bear off all their checkers wins.\n\n"
        		+ "Setup: Each player starts with 15 checkers arranged in the standard backgammon formation.\n\n"
        		+ "Dice Rolls: Players roll two dice and move their checkers according to the numbers rolled.\n"
        		+ "You can split the numbers between two checkers or move one checker the total of both dice.\n\n"
        		+ "Movement Rules: Checkers can only land on points that are unoccupied, occupied by your own checkers, or have a single opponent checker (hitting it sends it to the bar).\n"
        		+ "A point with two or more opponent checkers is \"blocked.\"\n\n"
        		+ "Hitting and Entering: If a checker is hit, it moves to the bar and must re-enter the opponent's home board before continuing.\n\n"
        		+ "Bearing Off: Once all your checkers are in your home board, you can start bearing them off by rolling dice that match their positions.\n\n"
        		+ "----------------------------------Medium game----------------------------------\n\n" 
        		+ "This mode introduces questions to the gameplay:\n\n" 
        		+ "Question Stations: Points on the board are marked with a \"?\" icon.\n"
        		+ "Landing on a question point requires the player to answer a question to complete their turn.\n"
        		+ "Questions are chosen randomly and categorized as: Easy/ Medium/ Hard. \n\n"
        		+ "Question Dice: A third dice, the \"Question Dice,\" is rolled alongside the regular two dice.\n"
        		+ "The question dice determines the difficulty level of the question.\n\n"
        		+ "Answering Questions: Players must answer the question correctly to proceed with their move.\n"
        		+ "Incorrect answers result in forfeiting the turn.\n\n"
        		+ "Surprise Station: Landing on a Surprise Station triggers a reward.\n\n"
        		+ "-----------------------------------Hard game-----------------------------------\n\n"
        		+ "This mode increases the challenge by introducing enhanced gameplay mechanics:\n\n"
        		+ "Enhanced Dice: Players roll two enhanced dice that include values from -3 to 6.\n"
        		+ "Negative numbers force the player to move backward on the board.\n\n"
        		+ "Question Dice: The question dice is retained from Medium Mode to determine question difficulty.\n\n"
        		+ "Rules for Question Points: Players must correctly answer the question to complete their turn.\n"
        		+ "Incorrect answers result in penalties, such as losing one checker back to the bar.\n\n"
        		+ "Surprise Station: Landing on a Surprise Station triggers a reward.\n\n"
        		+ "Have Fun and Good Luck :)"
        		); 
        rulesTextArea.setEditable(false);
        rulesTextArea.setLineWrap(true);
        rulesTextArea.setWrapStyleWord(true);
        rulesTextArea.setFont(new Font("Verdana", Font.PLAIN, 14));
        rulesTextArea.setForeground(Color.WHITE);
        rulesTextArea.setOpaque(false);
        
        JScrollPane scrollPane = new JScrollPane(rulesTextArea);
        scrollPane.setBounds(100, 150, 600, 300);
        scrollPane.setOpaque(false); 
        scrollPane.getViewport().setOpaque(false); 
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); 
        SwingUtilities.invokeLater(() -> {
            scrollPane.getVerticalScrollBar().setValue(0);
        });
        add(scrollPane);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Paint p;
        int h = getHeight();
        int w = getWidth();

        p = new RadialGradientPaint(new Point2D.Double(getWidth() / 2.0, getHeight() / 2.0),
                getHeight(),
                new float[]{0.0f, 0.8f},
                new Color[]{new Color(0x333333), new Color(0x000000)},
                RadialGradientPaint.CycleMethod.NO_CYCLE);

        g2.setPaint(p);
        g2.fillRect(0, 0, w, h);

        p = new Color(0x808080);
        g2.setStroke(new BasicStroke(5.0f));
        g2.setPaint(p);
        g2.drawRect(2, 0, w - 5, h - 5);

        g2.dispose();
    }
}

