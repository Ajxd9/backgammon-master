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
    private ArrayList<SixSidedDie> SixSidedDie;
    private ArrayList<Movement> movementList;
    private SquareColor playerColor;
    
    public Turn(SquareColor playerColor, ArrayList<SixSidedDie> sixSidedDie)
    {
        this.playerColor = playerColor;
        this.SixSidedDie = sixSidedDie;
        this.movementList = new ArrayList<Movement>();
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
    
    public SquareColor getPlayerColor() {
        return playerColor;
    }
    
    public ArrayList<SixSidedDie> getSixSidedDie() {
        return SixSidedDie;
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
    
        Element SixSidedDieXML = new Element("SixSidedDie");
        turnXML.addContent(SixSidedDieXML);
        
        for(int i=0; i<SixSidedDie.size(); i++){
            SixSidedDie.get(i).save(SixSidedDieXML);
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
            case "WHITE": playerColor = SquareColor.WHITE; break;
            case "BLACK": playerColor = SquareColor.BLACK; break;
            case "EMPTY": playerColor = SquareColor.EMPTY;
        }
        
        List<Element> SixSidedDieList = turn.getChild("SixSidedDie").getChildren("sixSidedDie");
        Iterator<Element> i = SixSidedDieList.iterator();
         
        SixSidedDie = new ArrayList<SixSidedDie>();
        
        while(i.hasNext()){
            SixSidedDie tmpDie = new SixSidedDie();
            tmpDie.load(i.next());
            SixSidedDie.add(tmpDie);
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
