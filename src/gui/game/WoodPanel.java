package gui.game;

import game.Board;
import game.Column;
import gui.sprites.SpriteSheet;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;


public class WoodPanel extends ColumnPanel {
	public WoodPanel(int i, boolean faceDown, Board board){
		super(i, faceDown, board);
	}
	
	public void paintComponent(Graphics g){
		BufferedImage woodSprite = SpriteSheet.getWood();
		g.drawImage(woodSprite,0,0,this.getWidth(),this.getHeight(),null);
		if (this.isSelected()){
			g.setColor(Color.YELLOW);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
		}
		
		for (int i = 0; i < column.getPieces().size(); i++){
			int pieceSize = this.getWidth();
			BufferedImage pieceSprite;
			if (column.getColor() == Column.WHITE){
				pieceSprite = SpriteSheet.getWhite();
			} else {
				pieceSprite = SpriteSheet.getBlack();
			}
			int y = 0;
			if (this.faceDown){
				y = this.getHeight()-(i+1)*pieceSize;
			} else {
				y = i*pieceSize;
			}
			g.drawImage(pieceSprite,0,y,pieceSize,pieceSize,null);
		} 
	}
}
