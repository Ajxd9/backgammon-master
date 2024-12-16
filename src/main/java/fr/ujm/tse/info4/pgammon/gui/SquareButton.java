package fr.ujm.tse.info4.pgammon.gui;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import fr.ujm.tse.info4.pgammon.models.Square;

public class SquareButton extends JButton{
	private static final long serialVersionUID = 6276324191590405443L;

	public static final ImageIcon iconeNoire = new ImageIcon("images/pion_noir.png");
	public static final ImageIcon iconeBlanche = new ImageIcon("images/pion_blanc.png");
	public static final ImageIcon iconeAideBlanc = new ImageIcon("images/pion_assist_blanc.png");
	public static final ImageIcon iconeAideNoir = new ImageIcon("images/pion_assist_noir.png");
	public static final ImageIcon iconeNoireTransp = new ImageIcon("images/pion_noir_transp.png");
	public static final ImageIcon iconeBlancheTransp= new ImageIcon("images/pion_blanc_transp.png");

	private boolean isCandidate;
	private boolean isPossible;

	private Square c;
	/**
	 * 
	 * @param _Square Square to associate with the button.
	 */
<<<<<<< Updated upstream:src/main/java/fr/ujm/tse/info4/pgammon/gui/SquareButton.java
	public SquareButton(Square _Square){
		c = _Square;
=======
	public CaseButton(Square _case){
		c = _case;
>>>>>>> Stashed changes:src/main/java/fr/ujm/tse/info4/pgammon/gui/CaseButton.java
		isCandidate = false;
	}

	
	
	/**
	 * 
	 * @return Returns the associated Square.
	 */
<<<<<<< Updated upstream:src/main/java/fr/ujm/tse/info4/pgammon/gui/SquareButton.java
	public Square getSquare() {
		return c;
	}

	public void setSquare(Square _Square) {
		c = _Square;
=======
	public Square getCase() {
		return c;
	}

	public void setCase(Square _case) {
		c = _case;
>>>>>>> Stashed changes:src/main/java/fr/ujm/tse/info4/pgammon/gui/CaseButton.java
	}
	
	
	public boolean isCandidate() {
		return isCandidate;
	}

	public void setCandidated(boolean isCandidate) {
		this.isCandidate = isCandidate;
	}



	public boolean isPossible() {
		return isPossible;
	}



	public void setPossible(boolean isPossible) {
		this.isPossible = isPossible;
	}

}
