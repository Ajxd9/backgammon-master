// 
//
//  @ Project : Project Gammon
//  @ File : Player.java
//  @ Date : 12/12/2012
//  @ Authors : DONG Chuan, BONNETTO Benjamin, FRANCON Adrien, POTHELUNE Jean-Michel
//
//

package fr.ujm.tse.info4.pgammon.models;

import org.jdom2.Attribute;
import org.jdom2.Element;

public class Player {
    private Integer id;
    private String username;
    private String imageSource;
    private AssistantLevel assistanceLevel;
    private PlayerStatistics stats;
    
    public Player() {
        this.id = 0;
        this.username = "";
        this.imageSource = "";
        this.assistanceLevel = AssistantLevel.NOT_USED;
        stats = new PlayerStatistics();
    }

    public Player(Integer id, String username, String imageSource, AssistantLevel assistanceLevel) {
        this.id = id;
        this.username = username;
        this.imageSource = imageSource;
        this.assistanceLevel = assistanceLevel;
        stats = new PlayerStatistics();
    }
    
    public void save(Element root) {
        Element player = new Element("players");
        root.addContent(player);
        
        Attribute playerId = new Attribute("id", id.toString());
        player.setAttribute(playerId);
        
        Element playerUsername = new Element("username");
        playerUsername.setText(username);
        player.addContent(playerUsername);
        
        Element playerImageSource = new Element("imageSource");
        playerImageSource.setText(imageSource);
        player.addContent(playerImageSource);
        
        Element playerAssistantLevel = new Element("assistanceLevel");
        playerAssistantLevel.setText(assistanceLevel.toString());
        player.addContent(playerAssistantLevel);
        
        stats.save(player);
    }
    
    public void load(Element it) {
        id = Integer.valueOf(it.getAttributeValue("id"));
        username = it.getChildText("username");
        imageSource = it.getChildText("imageSource");
        switch(it.getChildText("assistanceLevel")){
            case "NOT_USED": assistanceLevel = AssistantLevel.NOT_USED; break;
            case "SIMPLE": assistanceLevel = AssistantLevel.BASIC; break;
            case "COMPLETE": assistanceLevel = AssistantLevel.FULL;
        }
        stats.load(it.getChild("playerStatistics"));
    }   
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public AssistantLevel getAssistantLevel() {
        return assistanceLevel;
    }

    public void setAssistantLevel(AssistantLevel assistanceLevel) {
        this.assistanceLevel = assistanceLevel;
    }

    public PlayerStatistics getStats() {
        return stats;
    }

    public void setStats(PlayerStatistics stats) {
        this.stats = stats;
    }
}
