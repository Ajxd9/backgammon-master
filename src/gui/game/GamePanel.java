package gui.game;

import game.Board;
import game.Column;
import gui.sprites.SpriteSheet;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class GamePanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GamePanel(Board board){
		
		int colNum = 0;
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx=0.8;
		c.weighty=0.8;
		
		c.gridx=0;
		c.gridy=0;
		c.gridheight=5;
		c.weightx=0.15;
		this.add(new WoodPaddingPanel(),c);
		c.gridx=15;
		this.add(new WoodPaddingPanel(),c);
		c.weightx=0.8;
		c.gridheight=1;
		
		c.gridy = 0;
		c.gridx = 1;
		c.gridwidth=14;
		c.weighty=0.05;
		this.add(new WoodPaddingPanel(),c);
		c.gridy=4;
		this.add(new WoodPaddingPanel(),c);
		c.gridwidth=1;
		c.weighty=0.8;
		

		//bottom
		c.gridy=3;
		c.gridx=14;
		this.add(new EndPanel(colNum, false, board),c);
		colNum++;
		
		for (int i = 13; i > 7; i--){
			c.gridx=i;
			this.add(new ColumnPanel(colNum, true, board),c);
			colNum++;
		}
		c.gridx = 7;
		this.add(new WoodPanel(Board.WOOD_BLACK,false, board),c);
		for (int i = 6; i >= 1; i--){
			c.gridx=i;
			this.add(new ColumnPanel(colNum, true, board),c);
			colNum++;
		}
		c.gridy=1;
		//top
		for (int i = 1; i < 7; i++){
			c.gridx=i;
			this.add(new ColumnPanel(colNum, false, board),c);
			colNum++;
		}
		c.gridx = 7;
		this.add(new WoodPanel(Board.WOOD_WHITE,true, board),c);
		for (int i = 8; i <= 13; i++){
			c.gridx=i;
			this.add(new ColumnPanel(colNum, false, board),c);
			colNum++;
		}
		c.gridx=14;
		this.add(new EndPanel(colNum, true, board),c);
		
		//middle
		c.gridy=2;
		c.gridx=3;
		c.gridwidth=2;
		c.weighty=0.2;
		this.add(new DicePanel(board,Column.BLACK),c);

		c.gridx = 7;
		c.gridwidth=1;
		this.add(new WoodPaddingPanel(false),c);
		
		c.gridx = 14;
		this.add(new WoodPaddingPanel(false),c);
		
		c.gridx=10;
		c.gridwidth=2;
		this.add(new DicePanel(board,Column.WHITE),c);
		
	}
	
	public void paintComponent(Graphics g){
		int sizePerSprite = 128;
		BufferedImage boardSprite = SpriteSheet.getBoard();
		int x = sizePerSprite, y = sizePerSprite;
		while (x - sizePerSprite < this.getWidth()){
			while (y - sizePerSprite < this.getHeight()){
				g.drawImage(boardSprite,x-sizePerSprite,y-sizePerSprite,sizePerSprite,sizePerSprite,null);				
				y+= sizePerSprite;
			}
			y = sizePerSprite;
			x+= sizePerSprite;
		}
	}
	
}
