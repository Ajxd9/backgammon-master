package gui.game;

import game.Board;
import gui.options.GameMenu;
import gui.sprites.SpriteSheet;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class Window extends JFrame {
	GamePanel panel;
	Board board;
	
	public Window(Board board){
		super("CS 1006 Backgammon");
		this.board = board;
		SpriteSheet.init();
		panel = new GamePanel(board);
		this.add(panel);
		
		GameMenu menu = new GameMenu();
		this.setJMenuBar(menu);		
		this.setPreferredSize(new Dimension(800,800) );
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);

		
	}
	
	public void reset(){
		this.remove(panel);
		panel = new GamePanel(board);
		this.add(panel);
		this.pack();
		this.repaint();
	}
}

class PaintMouseListener implements MouseListener{

	JComponent component;
	
	public PaintMouseListener(JComponent component) {
		this.component = component;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		component.repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		component.repaint();		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		component.repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		component.repaint();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		component.repaint();
	}
	
}

class PaintMouseMotionListener implements MouseMotionListener{

	JComponent component;
	
	public PaintMouseMotionListener(JComponent component) {
		this.component = component;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		component.repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		component.repaint();		
	}
	
}