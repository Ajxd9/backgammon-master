package com.HyenaBgammon.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Paint;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.HyenaBgammon.models.SquareColor;

public class ScoreDisplay extends JPanel {

	private static final long serialVersionUID = -1068393331289654980L;
	private int score = 0;
	private SquareColor color;
	private JLabel label;
	
	public ScoreDisplay(int score, SquareColor color) {
		super();
		this.score = score;
		this.color = color;
		build();
	}
	
	private void build() {
		setLayout(new GridBagLayout());
		label = new JLabel(String.valueOf(score));
		Color c = (color==SquareColor.WHITE)?new Color(0x111111):new Color(0xEEEEEE);
		label.setForeground(c);
        setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		updateFont();
		label.setAlignmentX(CENTER_ALIGNMENT);
		add(label);
	}
	
	private void updateFont(){
		int h = Math.max(getPreferredSize().height,getSize().height);
		int sizeReduction = (String.valueOf(score).length()-1)*5;
		int fontSize = (int) (h/1.5);
		label.setFont(new Font("Arial",Font.BOLD,fontSize - sizeReduction));
		label.setPreferredSize(getSize());
	}
	
	public void setScore(int score) {
		this.score = score;
		label.setText(String.valueOf(score));
		updateFont();
	}
	
	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		updateFont();
	}
	
	@Override
	public void setPreferredSize(Dimension preferredSize) {
		super.setPreferredSize(preferredSize);
		updateFont();
	}

	@Override
	protected void paintComponent(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g.create(); 
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); 
 
		int h = getHeight(); 
		int w = getWidth(); 
		
		Paint p;
		
		// Background
		p = (color == SquareColor.WHITE) ? new Color(0xEEEEEE):new Color(0x111111);
		g2.setPaint(p); 
		g2.fillRect(0, 0, w, h); 
		 
		// Border
		p = new Color(0x888888);
		g2.setStroke(new BasicStroke(3.0f) );
		g2.setPaint(p); 
		g2.drawRect(1, 1, w - 3, h - 3);
			
		g2.dispose(); 
		
	}
	
	@Override
	protected void paintBorder(Graphics g) {
		//super.paintBorder(g);
	}
}
