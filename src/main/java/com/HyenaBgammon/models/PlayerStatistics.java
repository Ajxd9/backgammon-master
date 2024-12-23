// 
//
//  @ Project : Project Backgammon
//  @ File : PlayerStatistics.java
//  @ Date : 12/12/2012
//  @ Authors : DONG Chuan, BONNETTO Benjamin, FRANCON Adrien, POTHELUNE Jean-Michel
//
//

package com.HyenaBgammon.models;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.jdom2.Attribute;
import org.jdom2.Element;

public class PlayerStatistics {
    private Integer gamesPlayed;
    private Integer winCount;
    private Map<Player, Integer> gamesAgainstPlayer;
    private float playTime;
    
    public PlayerStatistics() {
        gamesPlayed = 0;
        winCount = 0;
        gamesAgainstPlayer = new HashMap<>();
        playTime = 0;    
    }
    
    public float getWinPercentage() {
        float percentage = winCount / (float)gamesPlayed;
        return percentage;
    }
    
    public String getFavoriteOpponent() {
        Iterator<Entry<Player, Integer>> it = gamesAgainstPlayer.entrySet().iterator();
        int maxValue = 0; 
        Player maxKey = null;
        while (it.hasNext()) {
            Map.Entry<Player, Integer> entry = it.next();
            int value = entry.getValue();
            Player key = entry.getKey();
            if(value > maxValue) {
                maxKey = key;
            }
        }
        if(maxKey == null)
            return "Nobody";
        return maxKey.getUsername();
    }
    
    public void addPlayTime(float time) {
        playTime = playTime + time;
    }
    
    public void addWin() {
        winCount = winCount + 1;
        gamesPlayed = gamesPlayed + 1;
    }
    
    public void addLoss() {
        gamesPlayed = gamesPlayed + 1;
    }
    
    public void addOpponent(Player opponent) {    
        if(gamesAgainstPlayer.get(opponent) == null) {
            gamesAgainstPlayer.put(opponent, 1);
        }
        else {
            int i = gamesAgainstPlayer.get(opponent);
            gamesAgainstPlayer.put(opponent, i + 1);
        }
    }
    
    public void save(Element player) {
        Element playerStatistics = new Element("playerStatistics");
        player.addContent(playerStatistics);
        
        Element gamesPlayedXML = new Element("gamesPlayed");
        gamesPlayedXML.setText(gamesPlayed.toString());
        playerStatistics.addContent(gamesPlayedXML);
        
        Element winCountXML = new Element("winCount");
        winCountXML.setText(winCount.toString());
        playerStatistics.addContent(winCountXML);
        
        Element lossCountXML = new Element("lossCount");
        lossCountXML.setText(getLossCount().toString());
        playerStatistics.addContent(lossCountXML);
        
        Element gamesAgainstPlayerXML = new Element("gamesAgainstPlayer");
        playerStatistics.addContent(gamesAgainstPlayerXML);
        
        Collection<Player> c = gamesAgainstPlayer.keySet();
        Iterator<Player> it = c.iterator();
        
        while(it.hasNext()) {
            Player p = it.next();
            Element playerAgainst = new Element("players");
            gamesAgainstPlayerXML.addContent(playerAgainst);
             
            Attribute idAgainst = new Attribute("id", p.getId().toString());
            playerAgainst.setAttribute(idAgainst);
            
            Element gameCount = new Element("gameCount");
            gameCount.setText(gamesAgainstPlayer.get(p).toString());
            playerAgainst.addContent(gameCount);
        }
        
        Element playTimeXML = new Element("playTime");
        playTimeXML.setText(String.valueOf(playTime));
        playerStatistics.addContent(playTimeXML);
    }
    
    public void load(Element it) {
        gamesPlayed = Integer.valueOf(it.getChildText("gamesPlayed"));
        winCount = Integer.valueOf(it.getChildText("winCount"));
        playTime = Float.valueOf(it.getChildText("playTime"));
    }

    public float getPlayTime() {
        return playTime;
    }
    
    public Map<Player, Integer> getGamesAgainstPlayer() {
        return gamesAgainstPlayer;
    }

    public Integer getLossCount() {
        return(gamesPlayed - winCount);
    }
    
    public Integer getGamesPlayed() {
        return gamesPlayed;
    }

    public Integer getWinCount() {
        return winCount;
    }
    
    public void setPlayTime(float playTime) {
        this.playTime = playTime;
    }

    public void setGamesAgainstPlayer(Map<Player, Integer> gamesAgainstPlayer) {
        this.gamesAgainstPlayer = gamesAgainstPlayer;
    }
    
    public void setWinCount(int winCount) {
        this.winCount = winCount;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }
}
