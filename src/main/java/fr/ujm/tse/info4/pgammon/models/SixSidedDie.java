// 
//
//  @ Project: Project Gammon
//  @ File: SixSidedDie.java
//  @ Date: 12/12/2012
//  @ Authors: DONG Chuan, BONNETTO Benjamin, FRANCON Adrien, POTHELUNE Jean-Michel
//
//

package fr.ujm.tse.info4.pgammon.models;

import java.util.Random;

import org.jdom2.Element;

public class SixSidedDie {
    /* VARIABLES */
    private int value;
    private boolean isUsed;
    private SquareColor dieColor;
    private DieType dieType;  
    
    /* FUNCTIONS */
    public SixSidedDie(DieType dieType, SquareColor squareColor) {
        this.isUsed = false;
        this.dieType = dieType;
        this.dieColor = squareColor;
        roll(); // Automatically roll when created
    }

    
    public void roll() {
        Random rand = new Random();
        switch (dieType) {
            case REGULAR:
                value = rand.nextInt(6) + 1; // Values 1-6
                break;
            case ENHANCED:
                value = rand.nextInt(10) - 3; // Values -3 to 6
                break;
            default:
                value = 0; // For QUESTION die, handled separately
                break;
        }
    }
    
    
   

    
    /* FUNCTIONS */
    public SixSidedDie() {}
    
    public SixSidedDie(SquareColor squareColor) {
        isUsed = false;
        value = (int)(Math.random() * 6 + 1);
        dieColor = squareColor;
    }
    
    public SixSidedDie(SquareColor squareColor, int value) {
        isUsed = false;
        this.value = value;
        this.dieColor = squareColor;
    }
    
    public void use() {
        isUsed = true;
    }
    
    public void resetUse() {
        isUsed = false;
    }
    
    public void save(Element sixSidedDice) {
        Element dieXML = new Element("sixSidedDie");
        sixSidedDice.addContent(dieXML);
        
        Element valueXML = new Element("value");
        valueXML.setText(String.valueOf(value));
        dieXML.addContent(valueXML);
        
        Element isUsedXML = new Element("isUsed");
        isUsedXML.setText(isUsed ? "yes" : "no");
        dieXML.addContent(isUsedXML);
        
        Element dieColorXML = new Element("dieColor");
        dieColorXML.setText(String.valueOf(dieColor));
        dieXML.addContent(dieColorXML);
    }
    
    public void load(Element sixSidedDie) {
        value = Integer.valueOf(sixSidedDie.getChildText("value"));
        
        switch(sixSidedDie.getChildText("isUsed")) {
            case "yes": isUsed = true; break;
            case "no": isUsed = false;
        }
        
        switch(sixSidedDie.getChildText("dieColor")) {
            case "WHITE": dieColor = SquareColor.WHITE; break;
            case "BLACK": dieColor = SquareColor.BLACK; break;
            case "EMPTY": dieColor = SquareColor.EMPTY;
        }
    }
    
    /* GETTERS AND SETTERS */
    
    public int getValue() {
        return value;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public SquareColor getDieColor() {
        return dieColor;
    }
}
