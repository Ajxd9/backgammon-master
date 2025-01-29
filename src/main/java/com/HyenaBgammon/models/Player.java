package com.HyenaBgammon.models;

import java.util.Random;
import org.jdom2.Attribute;
import org.jdom2.Element;

public class Player {
    private Integer id;
    private String username;
    private String imageSource;
    private AssistantLevel assistanceLevel;
    private PlayerStatistics stats;
    private boolean isAI; // New field to indicate AI player

    /**
     * Default constructor (for human player).
     */
    public Player() {
        this.id = 0;
        this.username = "Player";
        this.imageSource = "";
        this.assistanceLevel = AssistantLevel.NOT_USED;
        this.stats = new PlayerStatistics();
        this.isAI = false; // Default is not AI
    }

    /**
     * Constructor for a player with all attributes.
     * @param id Player ID.
     * @param username Player's username.
     * @param imageSource Image source path.
     * @param assistanceLevel Level of assistance.
     * @param isAI Boolean indicating if it's an AI player.
     */
    public Player(Integer id, String username, String imageSource, AssistantLevel assistanceLevel, boolean isAI) {
        this.id = (id != null) ? id : 0; // Assign default ID if null
        this.username = username;
        this.imageSource = (imageSource != null) ? imageSource : ""; // Default image source
        this.assistanceLevel = (assistanceLevel != null) ? assistanceLevel : AssistantLevel.NOT_USED;
        this.stats = new PlayerStatistics();
        this.isAI = isAI;
    }

    /**
     * New constructor for creating an AI player with just a name.
     * @param username The name of the AI player.
     * @param isAI Whether this player is AI-controlled.
     */
    public Player(String username, boolean isAI) {
        this.id = -1; // AI players get a special ID
        this.username = username;
        this.imageSource = "default_ai_avatar.png"; // Example default avatar
        this.assistanceLevel = AssistantLevel.NOT_USED;
        this.stats = new PlayerStatistics();
        this.isAI = isAI;
    }

    /**
     * AI makes a move if enabled.
     */
    public void makeMove(Game game) {
        if (isAI) {
            System.out.println(username + " (AI) is making a move...");

            try {
                if (game.hasPossibleMove()) {
                    System.out.println("AI has possible moves. Executing move...");
                    game.randomMove(); // AI randomly selects a move
                } else {
                    System.out.println("AI has no valid moves. Ending turn.");
                }
            } catch (Exception e) {
                System.out.println("AI failed to make a move: " + e.getMessage());
            }
        }
    }


    /**
     * Save player state to XML.
     */
    public void save(Element root) {
        Element player = new Element("players");
        root.addContent(player);

        player.setAttribute(new Attribute("id", id.toString()));

        player.addContent(new Element("username").setText(username));
        player.addContent(new Element("imageSource").setText(imageSource));
        player.addContent(new Element("assistanceLevel").setText(assistanceLevel.toString()));
        player.addContent(new Element("isAI").setText(String.valueOf(isAI))); // Saving AI status

        stats.save(player);
    }

    /**
     * Load player state from XML.
     */
    public void load(Element it) {
        id = Integer.valueOf(it.getAttributeValue("id"));
        username = it.getChildText("username");
        imageSource = it.getChildText("imageSource");

        // Properly setting assistance level
        switch (it.getChildText("assistanceLevel")) {
            case "NOT_USED":
                assistanceLevel = AssistantLevel.NOT_USED;
                break;
            case "BASIC":
                assistanceLevel = AssistantLevel.BASIC;
                break;
            case "FULL":
                assistanceLevel = AssistantLevel.FULL;
                break;
            default:
                assistanceLevel = AssistantLevel.NOT_USED;
                break;
        }

        isAI = Boolean.parseBoolean(it.getChildText("isAI")); // Correctly loads AI status
        stats.load(it.getChild("playerStatistics"));
    }

    /**
     * Checks if the player is AI.
     * @return True if AI, false otherwise.
     */
    public boolean isAI() {
        return isAI;
    }

    /**
     * Sets whether the player is AI-controlled.
     * @param isAI True if AI-controlled, false otherwise.
     */
    public void setAI(boolean isAI) {
        this.isAI = isAI;
    }

    /* GETTERS & SETTERS */

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

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", imageSource='" + imageSource + '\'' +
                ", assistanceLevel=" + assistanceLevel +
                ", isAI=" + isAI +
                '}';
    }
}
