package com.HyenaBgammon.models;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class Profiles {
    public List<Player> players = new ArrayList<Player>();
    private List<Element> playerList;
    private static Profiles profile;
    
    private Profiles() {
    }
    
    /**
     * Singleton constructor
     * @return profile
     */
    public static Profiles getProfiles() {
        if(profile == null){
            profile = new Profiles();
            try {
                profile.load();
            } catch (JDOMException e) {
                //todo error message
            } catch (IOException e){
                //todo error message
            }
        }   
        return profile; 
    }
    
    /**
     * Save player information in the "save" folder using XML
     */
    public void save() {
        Element root = new Element("profiles");
        Document document = new Document(root);
        
        for(int i=0; i<players.size(); i++){
            players.get(i).save(root);
        }
    
        try {
            File path = new File("save");
            if(!path.exists()) 
                path.mkdirs();
            
            XMLOutputter output = new XMLOutputter(Format.getPrettyFormat());
            output.output(document, new FileOutputStream("save/profiles.xml"));
       
        } catch(Exception e) {
            System.out.println("Save error");
        }
    }

    /**
     * Load player information from XML file in the "save" folder
     * @throws JDOMException
     * @throws IOException
     */
    public void load() throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder();
        Document document = builder.build("save/profiles.xml");
        Element root = document.getRootElement();
        
        playerList = root.getChildren("players");
        Iterator<Element> it = playerList.iterator();
        
        while(it.hasNext()){
            Player p = new Player();
            p.load(it.next());
            players.add(p);
        }
        
        // Load MAP<Player,Integer> in Stats for each player
        Iterator<Element> itStats = playerList.iterator();
        while(itStats.hasNext()){
            Element e = itStats.next();
            Iterator<Element> itAgainst = e.getChild("playerStatistics").getChild("gamesAgainstPlayer").getChildren("players").iterator();
            while(itAgainst.hasNext()){
                Element c = itAgainst.next();
                for(int i=0; i<players.size(); i++){
                    if(players.get(i).getId() == Integer.valueOf(e.getAttributeValue("id")))
                        for(int j=0; j<players.size(); j++){
                            if(players.get(j).getId() == Integer.valueOf(c.getAttributeValue("id"))){
                                Player playerAgainst = players.get(j);
                                players.get(i).getStats().getGamesAgainstPlayer().put(playerAgainst, Integer.valueOf(c.getChildText("numGames")));
                            }
                        }
                }
            }
        }
    }

    /**
     * Other functions    
     */
    public void add(String _username, String _imageSource, AssistantLevel _level) {
        players.add(new Player(players.size()+1, _username, _imageSource, _level, false));
    }
    
    public void modifyUsername(String _username, Player p) {
        for(int i=0; i<players.size(); i++) {
            if(players.get(i).getId() == p.getId()) {
                players.get(i).setUsername(_username);
            }
        }
    }
    
    public void modifyImageSource(String _imageSource, Player p) {
        for(int i=0; i<players.size(); i++) {
            if(players.get(i).getId() == p.getId()) {
                players.get(i).setImageSource(_imageSource);
            }
        }
    }
    
    public void delete(Player p) {
        for(int i=0; i<players.size(); i++) {
            if(players.get(i).getId() == p.getId()) {
                players.remove(i);
            }
        }
    }
    
    public void display() {
        for(int i=0; i<players.size(); i++) {
            System.out.println(players.get(i).getId()+" "+players.get(i).getUsername()+" "+
                             players.get(i).getImageSource()+" "+players.get(i).getAssistantLevel());
        }
    }
    
    public List<Player> getList() {
        return players;
    }
    
    public Player getPlayer(int id) {
        for(int i=0; i<players.size(); i++) {
            if(players.get(i).getId() == id)
                return players.get(i);
        }
        return null;
    }
}
