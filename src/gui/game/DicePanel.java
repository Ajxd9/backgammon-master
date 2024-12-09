package gui.game;

import game.Board;
import gui.sprites.SpriteSheet;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;

public class DicePanel extends JLabel {
	private int color;
	private Board board;
	
	public DicePanel(Board board,int color){
		this.color = color;
		this.board = board;
	}
	
	public void paintComponent(Graphics g){
		if (board.getTurn() == this.color){
			if (board.getDice() == null){
				return;
			}
			int w = this.getWidth();
			int h = this.getHeight();
			BufferedImage dice1 = SpriteSheet.getDice(board.getDice()[0]);
			BufferedImage dice2 = SpriteSheet.getDice(board.getDice()[1]);
			if (h > w/2){
				g.drawImage(dice1, 0, 0, w/2, w/2, null);
				g.drawImage(dice2, w/2, 0, w/2, w/2, null);								
			} else {
				g.drawImage(dice1, 0, 0, h, h, null);
				g.drawImage(dice2, w/2, 0, h, h, null);				
			}
		}
	}
}
