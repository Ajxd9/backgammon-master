package gui.game;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import game.Board;
import game.Column;
import gui.sprites.SpriteSheet;

import javax.swing.JLabel;

public class ColumnPanel extends JLabel {
	Column column;
	boolean faceDown;

	public ColumnPanel(int i, boolean faceDown, Board board){
		super();
		this.column = board.find(i);
		this.column.panel = this;
		this.faceDown = faceDown;
		this.addMouseListener(new BackGammonListener(this));
	}

	public void paintComponent(Graphics g){

		BufferedImage colSprite;
		boolean isTop = column.getNumber() > 12;
		boolean isGreen = column.getNumber() % 2 == 0;
		if (column.isSelected()){
				colSprite = isTop ? SpriteSheet.getSelectedColFlipped() : SpriteSheet.getSelectedCol();
		} else if (column.isHighlighted){
				colSprite = isTop ? SpriteSheet.getHighlightedColFlipped() : SpriteSheet.getHighlightedCol();
		} else if (isGreen){
				colSprite = isTop ? SpriteSheet.getGreenColFlipped() : SpriteSheet.getGreenCol();				
		} else {
				colSprite = isTop ? SpriteSheet.getRedColFlipped() : SpriteSheet.getRedCol();
		}
		g.drawImage(colSprite, 0, 0,this.getWidth(),this.getHeight(), null);

		int pieceSize = this.getWidth();
		BufferedImage pieceSprite;
		if (column.getColor() == Column.WHITE){
			pieceSprite = SpriteSheet.getWhite();
		} else {
			pieceSprite = SpriteSheet.getBlack();
		}
		for (int i = 0; i < column.getPieces().size(); i++){
			int y = 0;
			if (this.faceDown){
				y = this.getHeight()-(i+1)*pieceSize;
				if ( y < 0){
					if (y - pieceSize < 0){
						y = (int) ( this.getHeight() - pieceSize*((i+1)%(this.getHeight()/(pieceSize))) - pieceSize*Math.pow(0.5, (int) (i/(this.getHeight()/pieceSize))) );
					}
				}
			} else {
				y = i*pieceSize;
				if (y + pieceSize >  this.getHeight()){
					y = (int) ( pieceSize*(i%(this.getHeight()/(pieceSize))) + pieceSize*Math.pow(0.5, (int) (i/(this.getHeight()/pieceSize))) );
				}
			}
			g.drawImage(pieceSprite, 0, y, pieceSize, pieceSize, null);
		} 
	}

	public boolean isSelected() {
		return column.isSelected();
	}

	public void unSelect() {
		column.board.unSelect();
		
	}

	public void select() {
		column.select();
	}	
}

class BackGammonListener implements MouseListener{

	ColumnPanel panel;
	
	public BackGammonListener(ColumnPanel columnPanel) {
		panel = columnPanel;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (panel.isSelected()){
			panel.unSelect();
		} else {
			panel.select();
		}
		panel.repaint();

	}

	@Override
	public void mousePressed(MouseEvent e) {
		panel.repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		panel.repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		panel.repaint();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		panel.repaint();
	}
}
