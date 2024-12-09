package gui.game;

import game.Board;
import game.Column;
import game.Game;
import game.Piece;
import gui.sprites.SpriteSheet;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class EndPanel extends ColumnPanel {

	public EndPanel(int i, boolean faceDown, Board board) {
		super(i, faceDown, board);
	}

	public void paintComponent(Graphics g){
		g.drawImage(SpriteSheet.getEnd(),0,0,this.getWidth(),this.getHeight(),null);
		
		if (this.column.isHighlighted){
			g.setColor(new Color(0.8f,0.8f,0.0f,0.5f));
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
		}
		
		BufferedImage pieceSprite;
		if (column.getColor() == Column.WHITE){
			pieceSprite = SpriteSheet.getWhiteEnd();
		} else {
			pieceSprite = SpriteSheet.getBlackEnd();
		}

		int height = (int) ((this.getHeight() - Game.PIECE_NUMBER) /Game.PIECE_NUMBER);
		for (int i = 0; i < column.getPieces().size(); i++){
			if (column.getPieces().get(i).getColor() == Piece.WHITE){
				g.setColor(Color.white);
			} else {
				g.setColor(Color.BLACK);
			}
			int y = 0;
			if (this.faceDown){
				y = this.getHeight()-(i+1)*height - i;
			} else {
				y = i*height + i;
			}
			g.drawImage(pieceSprite,0, y, this.getWidth(), height,null);
		} 
	}
	
}
