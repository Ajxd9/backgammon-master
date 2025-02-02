package com.HyenaBgammon.view;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.HyenaBgammon.models.GameParameters;
import com.HyenaBgammon.models.Square;

public class CaseButton extends JButton{
	private static final long serialVersionUID = 6276324191590405443L;

	public static ImageIcon iconeNoire;
	public static ImageIcon iconeBlanche;
	public static final ImageIcon iconeAideBlanc = new ImageIcon("images/pion_assist_blanc.png");
	public static final ImageIcon iconeAideNoir = new ImageIcon("images/pion_assist_noir.png");
	public static final ImageIcon iconeNoireTransp = new ImageIcon("images/pion_noir_transp.png");
	public static final ImageIcon iconeBlancheTransp= new ImageIcon("images/pion_blanc_transp.png");

	private boolean isCandidate;
	private boolean isPossible;

	private Square c;
	/**
	 *
	 * @param _case Square to associate with the button.
	 */
	public CaseButton(Square _case){
		c = _case;
		isCandidate = false;
	}

	/**
	 *
	 * @return Returns the associated Square.
	 */
	
	public static void loadCheckerImages(String checkerSet) {

        if ("Black & White".equals(checkerSet)) {
        	iconeNoire = new ImageIcon("images/pion_noir.png");
            iconeBlanche = new ImageIcon("images/pion_blanc.png");
        } else { // Red & Blue
            iconeNoire = new ImageIcon("images/pion_red.png");  
            iconeBlanche = new ImageIcon("images/pion_blue.png");  
        }
    }
	
	public void updateCheckerIcon(boolean isBlack) {
        setIcon(isBlack ? iconeNoire : iconeBlanche);
    }
	
	public Square getCase() {
		return c;
	}
	public void setCase(Square _case) {
		c = _case;
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