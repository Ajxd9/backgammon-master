// 
//
//  @ Project: Project Gammon
//  @ File: Square.java
//  @ Date: 12/12/2012
//  @ Authors: DONG Chuan, BONNETTO Benjamin, FRANCON Adrien, POTHELUNE Jean-Michel
//
//

package fr.ujm.tse.info4.pgammon.models;

public class Square {
    private SquareColor checkerColor;
    private int numCheckers;
    private int position;

    public Square(int position) {
        this.position = position;
        this.checkerColor = SquareColor.EMPTY;
        this.numCheckers = 0;
    }
    
    public Square(int position, SquareColor color) {
        this.position = position;
        this.checkerColor = color;
        this.numCheckers = 0;
    }
    
    public Square(int position, SquareColor color, int numCheckers) {
        this.position = position;
        this.checkerColor = color;
        this.numCheckers = numCheckers;
    }

    public boolean isHomeSquare() {
        return (position == 25 && checkerColor == SquareColor.WHITE) || 
               (position == 0 && checkerColor == SquareColor.BLACK);
    }

    public boolean addChecker(SquareColor color) {
        if (this.checkerColor == color) {
            numCheckers += 1;
            return true;
        }
        else if(this.checkerColor == SquareColor.EMPTY) {
            numCheckers += 1;
            this.checkerColor = color;
            return true;
        }
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
}
