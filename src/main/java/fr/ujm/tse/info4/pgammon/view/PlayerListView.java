package fr.ujm.tse.info4.pgammon.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import fr.ujm.tse.info4.pgammon.gui.AvatarList;
import fr.ujm.tse.info4.pgammon.gui.ImageAvatar;
import fr.ujm.tse.info4.pgammon.gui.PlayerCellRenderer;
import fr.ujm.tse.info4.pgammon.gui.MonochromeButton;
import fr.ujm.tse.info4.pgammon.gui.MonochromeList;
import fr.ujm.tse.info4.pgammon.gui.MonochromeView;
import fr.ujm.tse.info4.pgammon.gui.OpaqueBackground;
import fr.ujm.tse.info4.pgammon.models.Player;
import fr.ujm.tse.info4.pgammon.models.Profiles;

public class PlayerListView extends MonochromeView {

    private static final long serialVersionUID = 9216988183357324981L;
    
    private Profiles profile;
    private Player player;
    
    private PlayerListViewDescriptionPanel descriptionPanel;
    private MonochromeList<Player> playerList;
    
    private String path = "";
    
    private AddPlayerView addPlayerView;
    private MonochromeButton addButton;
    private MonochromeButton selectButton;
    private MonochromeButton backButton;
    private OpaqueBackground bg;
    
    private AvatarList avatarList;
    
    private ImageAvatar playerImage;
    
    public PlayerListView() {
        
        profile = Profiles.getProfiles();
        build();
        
        playerList.getList().addListSelectionListener(new ListSelectionListener() {
            
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (playerList.getList().getSelectedValue() != null) {
                    player = playerList.getList().getSelectedValue();
                    updateData();
                }
            }
        });
    }
    
    public void showEditProfile() {
        addPlayerView.setVisible(true);
        bg.setVisible(true);
    }
    
    public void hideEditProfile() {
        addPlayerView.setVisible(false);
        bg.setVisible(false);
    }
    
    public void updateData() {
        descriptionPanel.setVisible(true);
        descriptionPanel.setPlayer(player);
        updateList();
    }
    
    private void build() {
        setLayout(null);
        setOpaque(false);
        
        player = null;
        
        bg = new OpaqueBackground();
        
        avatarList = new AvatarList();
        avatarList.setBounds(0, 0, 800, 600);
        add(avatarList);
        avatarList.setVisible(false);

        addPlayerView = new AddPlayerView();
        addPlayerView.setBounds(200, 150, 400, 300);
        add(addPlayerView);
        add(bg);
        hideEditProfile();
        
        descriptionPanel = new PlayerListViewDescriptionPanel(player);
        descriptionPanel.setBounds(420, 30, 330, 450);
        add(descriptionPanel);
        descriptionPanel.setVisible(false);
        
        playerList = new MonochromeList<>("Saved Players", profile.getList(), new PlayerCellRenderer());
        playerList.setBounds(40, 30, 330, 450);
        add(playerList);
        
        addButton = new MonochromeButton("Add a new player");
        addButton.setBounds(50, 505, 300, 50);
        add(addButton);
        
        selectButton = new MonochromeButton("Select");
        selectButton.setBounds(420, 505, 150, 50);
        add(selectButton);
        
        backButton = new MonochromeButton("Return");
        backButton.setBounds(600, 505, 150, 50);
        add(backButton);
        
        avatarChangeListener();
        addAvatarListener();
    }
    
    private void addAvatarListener() {
        avatarList.getAdd().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                path = avatarList.getPath();
                
                addPlayerView.setPath(path);
                avatarList.setVisible(false);
                updateList();
            }
        });
    }
    
    private void avatarChangeListener() {
        addPlayerView.getChangeAvatar().addMouseListener(new MouseListener() {
            
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {}        
            @Override
            public void mouseExited(MouseEvent e) {}            
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseClicked(MouseEvent e) {
                avatarList.setVisible(true);
            }
        });
    }
    
    public void updateList() {
        playerList.updateList(new PlayerCellRenderer());
    }
    
    public Profiles getProfile() {
        return profile;
    }

    public PlayerListViewDescriptionPanel getDescriptionPanel() {
        return descriptionPanel;
    }

    public MonochromeList<Player> getPlayerList() {
        return playerList;
    }

    public MonochromeButton getAddButton() {
        return addButton;
    }

    public MonochromeButton getSelectButton() {
        return selectButton;
    }

    public MonochromeButton getBackButton() {
        return backButton;
    }

    public AddPlayerView getAddPlayerView() {
        return addPlayerView;
    }
    
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
        updateData();
    }

    public AvatarList getAvatarList() {
        return avatarList;
    }

    public ImageAvatar getPlayerImage() {
        return playerImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create(); 

        bg.setBounds(getBounds());
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); 
        
        Paint p;
        int h = getHeight(); 
        int w = getWidth(); 
        
        p = new RadialGradientPaint(new Point2D.Double(getWidth() / 2.0, getHeight() / 2.0), 
                                    getHeight(),
                                    new float[] { 0.0f, 0.8f },
                                    new Color[] { new Color(0x333333), new Color(0x000000) },
                                    RadialGradientPaint.CycleMethod.NO_CYCLE);
        
        g2.setPaint(p); 
        g2.fillRect(0, 0, w, h); 
        
        p = new Color(0x808080);
        g2.setStroke(new BasicStroke(5.0f) );
        g2.setPaint(p); 
        g2.drawRect(2, 0, w - 5 , h - 5 );
        
        g2.dispose(); 
    }
}
