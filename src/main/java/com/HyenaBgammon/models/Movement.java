// 
//
//  @ Project: Project Gammon
//  @ File: Movement.java
//  @ Date: 12/12/2012
//  @ Authors: DONG Chuan, BONNETTO Benjamin, FRANCON Adrien, POTHELUNE Jean-Michel
//
//

package com.HyenaBgammon.models;

import org.jdom2.Element;

public class Movement {
    private Square startSquare;
    private Square endSquare;
    private boolean isHitMove;
    private double movementId;

    public Movement(Square startSquare, Square endSquare, boolean isHitMove) {
        this.startSquare = startSquare;
        this.endSquare = endSquare;
        this.isHitMove = isHitMove;
        this.movementId = Math.random() * Double.MAX_VALUE;
    }
    
    public Movement() {
        this.movementId = Math.random() * Double.MAX_VALUE;
    }
    
    public void save(Element movementsXML) {
        Element movementXML = new Element("movement");
        movementsXML.addContent(movementXML);
        
        Element startSquareXML = new Element("startSquare");
        startSquareXML.setText(String.valueOf(startSquare.getPosition()));
        movementXML.addContent(startSquareXML);
        
        Element endSquareXML = new Element("endSquare");
        endSquareXML.setText(String.valueOf(endSquare.getPosition()));
        movementXML.addContent(endSquareXML);
        
        Element isHitMoveXML = new Element("isHitMove");
        isHitMoveXML.setText(isHitMove ? "true" : "false");
        movementXML.addContent(isHitMoveXML);
    }
    
    public void load(Element movement, Game game) {
        // CAUTION: DO NOT MODIFY
        int startPosition = Integer.valueOf(movement.getChildText("startSquare"));
        int endPosition = Integer.valueOf(movement.getChildText("endSquare"));
        SquareColor color;
        
        if (startPosition == 0)
            color = SquareColor.WHITE;
        else if (startPosition == 25)
            color = SquareColor.BLACK;
        else if (endPosition == 0)
            color = SquareColor.BLACK;
        else if (endPosition == 25)
            color = SquareColor.WHITE;
        else if(startPosition < endPosition)
            color = SquareColor.WHITE;
        else if(startPosition > endPosition)
            color = SquareColor.BLACK;
        else 
            color = SquareColor.EMPTY;
        
        startSquare = game.getBoard().getSquare(startPosition, color);
        endSquare = game.getBoard().getSquare(endPosition, color);
        
        isHitMove = Boolean.valueOf(movement.getChildText("isHitMove"));
    }

    public Square getStartSquare() {
        return startSquare;
    }

    public Square getEndSquare() {
        return endSquare;
    }

    public boolean isHitMove() {
        return isHitMove;
    }

    public boolean isBearOffMove() {
        return endSquare.isHomeSquare();
    }

    public double getMovementId() {
        return movementId;
    }
}
