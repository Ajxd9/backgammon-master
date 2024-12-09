package gui.game;

import gui.sprites.SpriteSheet;

import java.awt.Graphics;

import javax.swing.JLabel;

public class WoodPaddingPanel extends JLabel {
	boolean extend;
	public WoodPaddingPanel(boolean extend) {
		this.extend = extend;
	}
	public WoodPaddingPanel() {
		this.extend = true;
	}
	
	public void paintComponent(Graphics g){
		if ( this.getWidth() > this.getHeight() && extend){
			int numberOfSprites = 5;
			for (int i = 0; i <= numberOfSprites+1; i++){
				g.drawImage(SpriteSheet.getWood(), (int) (this.getWidth()*(i/(1.0*numberOfSprites)) ),0,this.getWidth()/numberOfSprites,this.getHeight(),null);
			}
		} else {
			g.drawImage(SpriteSheet.getWood(), 0,0,this.getWidth(),this.getHeight(),null);			
		}

	}
}
