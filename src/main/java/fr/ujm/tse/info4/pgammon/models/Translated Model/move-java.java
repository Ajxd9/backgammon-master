// 
//
//  @ Project: Project Gammon
//  @ File: Move.java
//  @ Date: 12/12/2012
//  @ Authors: DONG Chuan, BONNETTO Benjamin, FRANCON Adrien, POTHELUNE Jean-Michel
//
//

package fr.ujm.tse.info4.pgammon.models;

public class Move {
    private Square startSquare;
    private Square endSquare;
    
    public Move() {
        this.startSquare = null;
        this.endSquare = null;
    }
    
    public Move(Square startSquare, Square endSquare) {
        this.startSquare = startSquare;
        this.endSquare = endSquare;
    }

    public Square getStartSquare() {
        return startSquare;
    }

    public void setStartSquare(Square startSquare) {
        this.startSquare = startSquare;
    }

    public Square getEndSquare() {
        return endSquare;
    }

    public void setEndSquare(Square endSquare) {
        this.endSquare = endSquare;
    }
}
