// 
//
//  @ Project : Project Gammon
//  @ File : Turn.java
//  @ Date : 12/12/2012
//  @ Authors : DONG Chuan, BONNETTO Benjamin, FRANCON Adrien, POTHELUNE Jean-Michel
//
//

package fr.ujm.tse.info4.pgammon.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Element;

public class Turn
{
    private ArrayList<SixSidedDice> sixSidedDice;
    private ArrayList<Movement> movementList;
    private PlayerColor playerColor;
    
    public Turn(PlayerColor playerColor, ArrayList<SixSidedDice> sixSidedDice)
    {
        this.playerColor = playerColor;
        this.sixSidedDice = sixSidedDice;
        this.movementList = new ArrayList<Movement>();
    }
    
    public Turn(SquareColor currentPlayer, ArrayList<SixSidedDie> sixSidedDie)
    {
    }
    
    public void addMovement(Movement movement)
    {
        movementList.add(movement);
    }
    
    public void removeLastMovement()
    {
        movementList.remove(movementList.size()-1);
    }
    
    public Movement getLastMovement()
    {   
        if (movementList.size()!=0)
            return movementList.get(movementList.size()-1);
        else
            return null;
    }
    
    public PlayerColor getPlayerColor() {
        return playerColor;
    }
    
    public ArrayList<SixSidedDice> getSixSidedDice() {
        return sixSidedDice;
    }
    
    public ArrayList<Movement> getMovementList(){
        return movementList;
    }
    
    // SERIALIZATION
    public void save(Element playerTurnHistoryXML)
    {
        Element turnXML = new Element("turn");
        playerTurnHistoryXML.addContent(turnXML);
            
        Element playerColorXML = new Element("playerColor");
        playerColorXML.setText(String.valueOf(playerColor));
        turnXML.addContent(playerColorXML);
    
        Element sixSidedDiceXML = new Element("sixSidedDice");
        turnXML.addContent(sixSidedDiceXML);
        
        for(int i=0; i<sixSidedDice.size(); i++){
            sixSidedDice.get(i).save(sixSidedDiceXML);
        }
        
        Element movementsXML = new Element("movements");
        turnXML.addContent(movementsXML);
        
        for(int i=0; i<movementList.size(); i++){
            movementList.get(i).save(movementsXML);
        }
    }
    
    public void load(Element turn, Game game)
    {
        switch(turn.getChildText("playerColor")){
            case "WHITE": playerColor = PlayerColor.WHITE; break;
            case "BLACK": playerColor = PlayerColor.BLACK; break;
            case "EMPTY": playerColor = PlayerColor.EMPTY;
        }
        
        List<Element> sixSidedDiceList = turn.getChild("sixSidedDice").getChildren("sixSidedDie");
        Iterator<Element> i = sixSidedDiceList.iterator();
         
        sixSidedDice = new ArrayList<SixSidedDice>();
        
        while(i.hasNext()){
            SixSidedDice tmpDie = new SixSidedDice();
            tmpDie.load(i.next());
            sixSidedDice.add(tmpDie);
        }
        
        List<Element> movementsList = turn.getChild("movements").getChildren("movement");
        Iterator<Element> it = movementsList.iterator();
        
        movementList = new ArrayList<Movement>();
        
        while(it.hasNext()){
            Movement tmpMovement = new Movement();
            tmpMovement.load(it.next(), game);
            movementList.add(tmpMovement);
        }
    }
}
