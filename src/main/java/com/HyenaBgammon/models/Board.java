//
//
//  @ Project : Project Gammon
//  @ File : Board.java
//
//

package com.HyenaBgammon.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Element;

public class Board
{
    private ArrayList<Square> squareList;
    private ArrayList<Square> victorySquare;
    private ArrayList<Square> barSquare;
    private Game game;

    public Board(Game g)
    {
        game = g;
        squareList = new ArrayList<Square>();
        victorySquare = new ArrayList<Square>();
        barSquare = new ArrayList<Square>();
        initializeSquares();
    }

    public Board()
    {
        game = null;

        squareList = new ArrayList<Square>();
        victorySquare = new ArrayList<Square>();
        barSquare = new ArrayList<Square>();
        initializeSquares();
    }

    public void initializeSquares(ArrayList<Square> listSquare)
    {
        squareList = new ArrayList<Square>();
        for (Square square1 : listSquare) {
            squareList.add(square1);
        }
        victorySquare = new ArrayList<Square>();
        barSquare = new ArrayList<Square>();
    }

    public void initializeBarSquares(ArrayList<Square> listSquare)
    {
        barSquare.add(listSquare.get(0));
        barSquare.add(listSquare.get(1));
    }

    public void initializeVictorySquares(ArrayList<Square> listSquare)
    {
        victorySquare.add(listSquare.get(0));
        victorySquare.add(listSquare.get(1));
    }

    public void initializeSquares()
    {
        squareList.clear();
        victorySquare.clear();
        barSquare.clear();

        victorySquare.add(new Square(SquareColor.WHITE, 0, 25));
        victorySquare.add(new Square(SquareColor.BLACK, 0, 0));

        barSquare.add(new Square(SquareColor.WHITE, 0, 0));
        barSquare.add(new Square(SquareColor.BLACK, 0, 25));

        for (int i=1; i<=24; i++)
        {
            if(i==1)
                squareList.add(new Square(SquareColor.WHITE, 2, i));
            else if(i==6)
                squareList.add(new Square(SquareColor.BLACK, 5, i));
            else if(i==8)
                squareList.add(new Square(SquareColor.BLACK, 3, i));
            else if(i==12)
                squareList.add(new Square(SquareColor.WHITE, 5, i));
            else if(i==13)
                squareList.add(new Square(SquareColor.BLACK, 5, i));
            else if(i==17)
                squareList.add(new Square(SquareColor.WHITE, 3, i));
            else if(i==19)
                squareList.add(new Square(SquareColor.WHITE, 5, i));
            else if(i==24)
                squareList.add(new Square(SquareColor.BLACK, 2, i));
            else
                squareList.add(new Square(SquareColor.EMPTY, 0, i));
        }
    }

    public void resetSquares()
    {
        victorySquare.get(0).setSquare(SquareColor.WHITE, 0);
        victorySquare.get(1).setSquare(SquareColor.BLACK, 0);

        barSquare.get(0).setSquare(SquareColor.WHITE, 0);
        barSquare.get(1).setSquare(SquareColor.BLACK, 0);

        for (int i=0; i<24; i++)
        {
            if(i==0)
                squareList.get(i).setSquare(SquareColor.WHITE, 2);
            else if(i==5)
                squareList.get(i).setSquare(SquareColor.BLACK, 5);
            else if(i==7)
                squareList.get(i).setSquare(SquareColor.BLACK, 3);
            else if(i==11)
                squareList.get(i).setSquare(SquareColor.WHITE, 5);
            else if(i==12)
                squareList.get(i).setSquare(SquareColor.BLACK, 5);
            else if(i==16)
                squareList.get(i).setSquare(SquareColor.WHITE, 3);
            else if(i==18)
                squareList.get(i).setSquare(SquareColor.WHITE, 5);
            else if(i==23)
                squareList.get(i).setSquare(SquareColor.BLACK, 2);
            else
                squareList.get(i).setSquare(SquareColor.EMPTY, 0);
        }
    }

    public int distanceBetweenSquares(Square start, Square end)
    {
        return end.getPosition() - start.getPosition();
    }

    public boolean isMovementDirectionCorrect(Square start, Square end)
    {
        if (start == null || end == null) {
            return false;
        }
        // Allow movement in both directions due to enhanced die support
        return true;
    }

    public boolean isMovePossible(Square start, Square end)
    {
        if (start == null || end == null || start.getNumCheckers() == 0) {
            return false;
        }

        if(getBarSquare(start.getCheckerColor()).getNumCheckers() != 0
                && (start.getPosition() != 0 && start.getCheckerColor() == SquareColor.WHITE
                || start.getPosition() != 25 && start.getCheckerColor() == SquareColor.BLACK))
            return false;

        if (end.isHomeSquare()){
            if (canBearOff(start.getCheckerColor())
                    && end.getCheckerColor() == start.getCheckerColor())
                return true;
            else
                return false;
        }

        if(start.getCheckerColor() == end.getCheckerColor())
            return true;
        else
        {
            if (end.getNumCheckers() <= 1)
                return true;
            else
                return false;
        }
    }

    public boolean canCapturePiece(Square start, Square end)
    {
        if(start.getCheckerColor() != end.getCheckerColor()
                && end.getCheckerColor() != SquareColor.EMPTY
                && end.getNumCheckers() == 1)
            return true;
        else
            return false;
    }

    public Move intToMove(int startInt, int endInt, SquareColor startSquareColor)
    {
        Move move = new Move();

        if (startInt == 0 && startSquareColor == SquareColor.WHITE)
            move.setStartSquare(barSquare.get(0));
        else if (startInt == 25 && startSquareColor == SquareColor.BLACK)
            move.setStartSquare(barSquare.get(1));
        else if (startInt != 0 && startInt != 25)
            move.setStartSquare(squareList.get(startInt-1));

        if (endInt == 25 && startSquareColor == SquareColor.WHITE)
            move.setEndSquare(victorySquare.get(0));
        else if (endInt == 0 && startSquareColor == SquareColor.BLACK)
            move.setEndSquare(victorySquare.get(1));
        else if (endInt != 0 && endInt != 25)
            move.setEndSquare(squareList.get(endInt-1));

        return move;
    }

    public boolean movePiece(int startInt, int endInt, SquareColor startSquareColor)
    {
        Move move = intToMove(startInt, endInt, startSquareColor);
        return movePiece(move.getStartSquare(), move.getEndSquare());
    }

    public boolean movePiece(Square start, Square end)
    {
        if(isMovePossible(start, end))
        {
            Square startSquareSave = new Square(start.getCheckerColor(), start.getNumCheckers(), start.getPosition());

            if (!start.removeChecker())
                return false;

            if (canCapturePiece(startSquareSave, end))
            {
                if (end.getCheckerColor() == SquareColor.WHITE)
                    barSquare.get(0).addChecker(SquareColor.WHITE);
                else
                    barSquare.get(1).addChecker(SquareColor.BLACK);
            }

            end.addChecker(startSquareSave.getCheckerColor());

            return true;
        }
        else
            return false;
    }

    public boolean isSquareBefore(Square pieceSquare)
    {
        int numPieces = 0;

        if(pieceSquare.getCheckerColor() == SquareColor.WHITE)
        {
            for (int i=18; i<(pieceSquare.getPosition()-1); i++)
            {
                if (getSquareList().get(i).getCheckerColor() == SquareColor.WHITE)
                    numPieces += getSquareList().get(i).getNumCheckers();
            }
        }
        else
        {
            for (int i=5; i>=pieceSquare.getPosition(); i--)
            {
                if (getSquareList().get(i).getCheckerColor() == SquareColor.BLACK)
                    numPieces += getSquareList().get(i).getNumCheckers();
            }
        }

        return numPieces != 0;
    }

    public boolean canBearOff(SquareColor color)
    {
        int numPieces = 0;

        if(color == SquareColor.WHITE)
        {
            numPieces += getBarSquare().get(0).getNumCheckers();
            for (int i=0; i<18; i++)
            {
                if (getSquareList().get(i).getCheckerColor() == SquareColor.WHITE)
                    numPieces += getSquareList().get(i).getNumCheckers();
            }
        }
        else
        {
            numPieces += getBarSquare().get(1).getNumCheckers();
            for (int i=6; i<24; i++)
            {
                if (getSquareList().get(i).getCheckerColor() == SquareColor.BLACK)
                    numPieces += getSquareList().get(i).getNumCheckers();
            }
        }
        return numPieces == 0;
    }

    public boolean isAllPiecesMarked(SquareColor color)
    {
        if (color == SquareColor.WHITE && getVictorySquare().get(0).getNumCheckers() == 15
                || color == SquareColor.BLACK && getVictorySquare().get(1).getNumCheckers() == 15)
            return true;
        else
            return false;
    }

    public Square getSquareAtDistance(Square s, SixSidedDie die)
    {
        if (s == null || die == null) {
            return null;
        }

        if(s.getCheckerColor() == SquareColor.WHITE) {
            int whiteAtDistance = s.getPosition() + die.getValue();
            if(whiteAtDistance <= 24 && whiteAtDistance >= 1) {
                return squareList.get(whiteAtDistance-1);
            }
            else if(whiteAtDistance > 24) {
                return victorySquare.get(0);
            }
            else {
                // Handle moving below position 1
                return squareList.get(0);
            }
        }
        else {
            int blackAtDistance = s.getPosition() - die.getValue();
            if(blackAtDistance >= 1 && blackAtDistance <= 24) {
                return squareList.get(blackAtDistance-1);
            }
            else if(blackAtDistance < 1) {
                return victorySquare.get(1);
            }
            else {
                // Handle moving above position 24
                return squareList.get(23);
            }
        }
    }

    public boolean isPieceOnBar(SquareColor color)
    {
        return getBarSquare(color).getNumCheckers() != 0;
    }

    public List<Move> getPossibleMoves(SixSidedDie die, SquareColor color)
    {
        List<Move> list = new ArrayList<Move>();

        if (die == null || color == null || die.getValue() == 0) {
            return list;
        }

        // If there are pieces on the bar, they must be moved first
        if (isPieceOnBar(color)) {
            Square barSquare = getBarSquare(color);
            Square endSquare = getSquareAtDistance(barSquare, die);
            if (isMovePossible(barSquare, endSquare)) {
                list.add(new Move(barSquare, endSquare));
            }
            return list;
        }

        // Check all squares for possible moves
        List<Square> allSquares = getAllSquares();
        for (Square square : allSquares) {
            if(square.getCheckerColor() == color && !die.isUsed()) {
                Square possibleEnd = getSquareAtDistance(square, die);
                if(isMovePossible(square, possibleEnd)) {
                    list.add(new Move(square, possibleEnd));
                }
            }
        }
        return list;
    }

    public List<Move> getPossibleMoves(List<SixSidedDie> dice, SquareColor color)
    {
        if (dice == null || dice.isEmpty() || color == null) {
            return new ArrayList<>();
        }

        // Handle regular case with 1-2 dice
        if(dice.size() <= 2) {
            List<Move> allMoves = new ArrayList<>();

            // Add single die moves, skipping any with value 0
            for (SixSidedDie die : dice) {
                if (die.getValue() != 0) {
                    allMoves.addAll(getPossibleMoves(die, color));
                }
            }

            // If we have exactly 2 dice, add combined move if neither is 0
            if (dice.size() == 2 && dice.get(0).getValue() != 0 && dice.get(1).getValue() != 0) {
                int combinedValue = dice.get(0).getValue() + dice.get(1).getValue();
                SixSidedDie combinedDie = new SixSidedDie(dice.get(0).getDieColor(), combinedValue);
                allMoves.addAll(getPossibleMoves(combinedDie, color));
            }

            return allMoves;
        }
        // Handle doubles (4 dice case)
        else {
            List<Move> allMoves = new ArrayList<>();
            int singleValue = dice.get(0).getValue();

            // Add movements for 1, 2, 3, and 4 times the die value, but only if value isn't 0
            if (singleValue != 0) {
                for (int multiplier = 1; multiplier <= 4; multiplier++) {
                    SixSidedDie multipliedDie = new SixSidedDie(
                            dice.get(0).getDieColor(),
                            singleValue * multiplier
                    );
                    allMoves.addAll(getPossibleMoves(multipliedDie, color));
                }
            }

            return allMoves;
        }
    }

    public void save(Element game)
    {
        Element boardXML = new Element("board");
        game.addContent(boardXML);

        Element squaresXML = new Element("Squares");
        boardXML.addContent(squaresXML);

        for(int i = 0; i < squareList.size(); i++) {

            Element SquareXML = new Element("Square");
            squaresXML.addContent(SquareXML);

            Attribute idSquare = new Attribute("id", String.valueOf(squareList.get(i).getPosition()));
            SquareXML.setAttribute(idSquare);

            Element pieceColorXML = new Element("pieceColor");
            pieceColorXML.setText(String.valueOf(squareList.get(i).getCheckerColor()));
            SquareXML.addContent(pieceColorXML);

            Element numPiecesXML = new Element("numPieces");
            numPiecesXML.setText(String.valueOf(squareList.get(i).getNumCheckers()));
            SquareXML.addContent(numPiecesXML);
        }

        Element VictorySquaresXML = new Element("VictorySquares");
        boardXML.addContent(VictorySquaresXML);

        for(int i = 0; i < victorySquare.size(); i++) {

            Element VictorySquareXML = new Element("VictorySquare");
            VictorySquaresXML.addContent(VictorySquareXML);

            Attribute idVictorySquare = new Attribute("id", String.valueOf(victorySquare.get(i).getPosition()));
            VictorySquareXML.setAttribute(idVictorySquare);

            Element pieceColorVXML = new Element("pieceColor");
            pieceColorVXML.setText(String.valueOf(victorySquare.get(i).getCheckerColor()));
            VictorySquareXML.addContent(pieceColorVXML);

            Element numPiecesVXML = new Element("numPieces");
            numPiecesVXML.setText(String.valueOf(victorySquare.get(i).getNumCheckers()));
            VictorySquareXML.addContent(numPiecesVXML);
        }

        Element BarSquaresXML = new Element("BarSquares");
        boardXML.addContent(BarSquaresXML);

        for(int i = 0; i < barSquare.size(); i++) {

            Element BarSquareXML = new Element("BarSquare");
            BarSquaresXML.addContent(BarSquareXML);

            Attribute idBarSquare = new Attribute("id", String.valueOf(barSquare.get(i).getPosition()));
            BarSquareXML.setAttribute(idBarSquare);

            Element pieceColorBXML = new Element("pieceColor");
            if(barSquare.get(i).getPosition() == 0)
                pieceColorBXML.setText(String.valueOf(SquareColor.WHITE));
            else
                pieceColorBXML.setText(String.valueOf(SquareColor.BLACK));
            BarSquareXML.addContent(pieceColorBXML);

            Element numPiecesBXML = new Element("numPieces");
            numPiecesBXML.setText(String.valueOf(barSquare.get(i).getNumCheckers()));
            BarSquareXML.addContent(numPiecesBXML);
        }
    }

    public void load(Element Game)
    {
        squareList = new ArrayList<Square>();

        List<Element> listSquareList = Game.getChild("board").getChild("Squares").getChildren("Square");
        Iterator<Element> ia = listSquareList.iterator();

        while(ia.hasNext()) {
            Square tmpSquare = new Square();
            tmpSquare.load(ia.next());
            squareList.add(tmpSquare);
        }

        victorySquare = new ArrayList<Square>();

        List<Element> listVictorySquare = Game.getChild("board").getChild("VictorySquares").getChildren("VictorySquare");
        Iterator<Element> ib = listVictorySquare.iterator();

        while(ib.hasNext()) {
            Square tmpSquare = new Square();
            tmpSquare.load(ib.next());
            victorySquare.add(tmpSquare);
        }

        barSquare = new ArrayList<Square>();

        List<Element> listBarSquare = Game.getChild("board").getChild("BarSquares").getChildren("BarSquare");
        Iterator<Element> ic = listBarSquare.iterator();

        while(ic.hasNext()) {
            Square tmpSquare = new Square();
            tmpSquare.load(ic.next());
            barSquare.add(tmpSquare);
        }
    }

    public ArrayList<Square> getSquareList() {
        return squareList;
    }

    public ArrayList<Square> getVictorySquare() {
        return victorySquare;
    }

    public Square getVictorySquare(SquareColor color) {
        if(color == SquareColor.WHITE)
            return victorySquare.get(0);
        else
            return victorySquare.get(1);
    }

    public ArrayList<Square> getBarSquare() {
        return barSquare;
    }

    public Square getSquare(int position, SquareColor color) {
        if (position >= 1 && position <= 24)
            return squareList.get(position-1);
        else if (position == 25 && color.equals(SquareColor.WHITE))
            return victorySquare.get(0);
        else if (position == 0 && color.equals(SquareColor.BLACK))
            return victorySquare.get(1);
        else if (position == 0 && color.equals(SquareColor.WHITE))
            return barSquare.get(0);
        else if(position == 25 && color.equals(SquareColor.BLACK))
            return barSquare.get(1);
        else
            return null;
    }

    public Square getBarSquare(SquareColor color) {
        if(color == SquareColor.WHITE)
            return barSquare.get(0);
        else
            return barSquare.get(1);
    }

    public ArrayList<Square> getAllSquares() {
        ArrayList<Square> listAllSquares = new ArrayList<Square>();
        for (Square square1 : squareList) {
            listAllSquares.add(square1);
        }
        for (Square square1 : victorySquare) {
            listAllSquares.add(square1);
        }
        for (Square square1 : barSquare) {
            listAllSquares.add(square1);
        }
        return listAllSquares;
    }

    public Game getGame() {
        return game;
    }
}