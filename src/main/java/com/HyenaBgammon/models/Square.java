// 
//
//  @ Project: Project Gammon
//  @ File: Square.java
//  @ Date: 12/12/2012
//  @ Authors: DONG Chuan, BONNETTO Benjamin, FRANCON Adrien, POTHELUNE Jean-Michel
//
//

package com.HyenaBgammon.models;

import org.jdom2.Element;

public class Square {
    private SquareColor checkerColor;
    private int numCheckers;
    private int position;
    private SquareType squareType;
    private boolean isSurpriseTriangle;
    private boolean isActivated;
    public Square(SquareColor color, int numCheckers, int position) {
        this.checkerColor = color;
        this.numCheckers = numCheckers;
        this.position = position;
    }
    
    public Square() {
    }
    
    public void load(Element square) {
        switch(square.getChildText("checkerColor")) {
            case "WHITE": checkerColor = SquareColor.WHITE; break;
            case "BLACK": checkerColor = SquareColor.BLACK; break;
            case "EMPTY": checkerColor = SquareColor.EMPTY;
        }
        
        numCheckers = Integer.valueOf(square.getChildText("numCheckers"));
        position = Integer.valueOf(square.getAttributeValue("id"));
    }
    
    public void loadBV(Element square) {
        switch(square.getChildText("checkerColor")) {
            case "WHITE": checkerColor = SquareColor.WHITE; break;
            case "BLACK": checkerColor = SquareColor.BLACK; break;
            case "EMPTY": checkerColor = SquareColor.EMPTY;
        }
        
        numCheckers = Integer.valueOf(square.getChildText("numCheckers"));
    }
    public Square(SquareColor color, int numCheckers, int position, SquareType squareType) {
        this.checkerColor = color;
        this.numCheckers = numCheckers;
        this.position = position;
        this.squareType = squareType; // Assign type
    }

    public boolean isHomeSquare() {
        return (position == 25 && checkerColor == SquareColor.WHITE) || 
               (position == 0 && checkerColor == SquareColor.BLACK);
    }

    public boolean isBarSquare() {
        return (position == 0 && checkerColor == SquareColor.WHITE) || 
               (position == 25 && checkerColor == SquareColor.BLACK);
    }

    public boolean addChecker(SquareColor color) {
        // If square is same color, add a checker
        if (this.checkerColor == color) {
            numCheckers += 1;
            return true;
        }
        // If square is empty, change color and increment
        else if(this.checkerColor == SquareColor.EMPTY) {
            numCheckers += 1;
            this.checkerColor = color;
            return true;
        }
        // If square has one or fewer checkers, just change color
        else if (numCheckers <= 1) {
            this.checkerColor = color;
            return true;
        }
        else
            return false;
    }

    public boolean removeChecker() {
        if (numCheckers >= 1) {
            numCheckers -= 1;
            if (numCheckers == 0)
                checkerColor = SquareColor.EMPTY;
            return true;
        }
        return false;
    }

    public SquareColor getCheckerColor() {
        return checkerColor;
    }

    public int getNumCheckers() {
        return numCheckers;
    }

    public int getPosition() {
        return position;
    }
    
    public void setCheckerColor(SquareColor color) {
        checkerColor = color;
    }
    public SquareType getSquareType() {
        return squareType;
    }
    public void setSquareType(SquareType squareType) {
        this.squareType = squareType;
    }
    
    public void setNumCheckers(int num) {
        numCheckers = num;
    }
    
    public void setPosition(int pos) {
        position = pos;
    }
    public Square(boolean isSurpriseTriangle) {
        this.isSurpriseTriangle = isSurpriseTriangle;
        this.isActivated = false;
    }
    public void setSquare(SquareColor color, int numCheckers) {
        this.checkerColor = color;
        this.numCheckers = numCheckers;
    }

    public boolean isSurpriseTriangle() {
        return isSurpriseTriangle;
    }

    public void setSurpriseTriangle(boolean isSurpriseTriangle) {
        this.isSurpriseTriangle = isSurpriseTriangle;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void activate() {
        this.isActivated = true;
    }
    @Override
    public String toString() {
        return "Square{" +
               "checkerColor=" + checkerColor +
               ", numCheckers=" + numCheckers +
               ", position=" + position +
               ", squareType=" + squareType +
               '}';
    }
}
