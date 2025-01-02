package com.HyenaBgammon.models;

import java.util.Random;

import org.jdom2.Element;

public class SixSidedDie {
    /* VARIABLES */
    private int value;
    private int diffValue;
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
            case QUESTION:
            	value = 0;
            	diffValue = rand.nextInt(3) + 1; // Values 1 for Easy, 2 for Medium, 3 for Hard
            	System.out.println("q value is: "+ diffValue);
            	break;
            default:
                System.out.println("invalid InPut!"); // For QUESTION die, handled separately
                break;
        }
    }
    
    
   
    public void setValue(int value) {
		this.value = value;
	}


	/* GET QUESTION DIFFICULTY BASED ON DIE VALUE */
    public String getQuestionDifficulty() {
        if (dieType == DieType.QUESTION) {
            switch (value) {
                case 1: return "Easy";
                case 2: return "Medium";
                case 3: return "Hard";
                default: return "Unknown";
            }
        }
        return "Not a Question Die";
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
    
    public SixSidedDie(int value, SquareColor dieColor, DieType dieType) {
		super();
		isUsed = false;
		this.value = value;
		this.dieColor = dieColor;
		this.dieType = dieType;
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
    
    public DieType getDieType() {
        return dieType;
    }

    public SquareColor getDieColor() {
        return dieColor;
    }

	public int getDiffValue() {
		return diffValue;
	}

}
