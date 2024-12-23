package com.HyenaBgammon.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Calendar;
import javax.swing.JFrame;

import com.HyenaBgammon.models.Player;
import com.HyenaBgammon.models.Profiles;
import com.HyenaBgammon.view.AddPlayerView;
import com.HyenaBgammon.view.PlayerListView;

public class PlayerListController implements Controller {

    private PlayerListView PlayerListView;
    private Controller controller;
    private Profiles profile;
    private AddPlayerView AddPlayerView;
    private Boolean flag = true;
    private Integer id = 0;
    private boolean isLoaded;
    private JFrame frame;
    
    public PlayerListController(boolean isLoaded, Controller controller) 
    {
        this.controller = controller;
        profile = Profiles.getProfiles();
        this.isLoaded = isLoaded;
        PlayerListView = new PlayerListView();
        controller.getController().getFrame().setContentPane(PlayerListView);

        this.AddPlayerView = PlayerListView.getAddPlayerView();
        build();
    }
    
    /**
     * All button listeners
     */
    public void build()
    {
        listenerButtonBack();
        listenerButtonAdd();
        listenerButtonModify();
        listenerButtonDelete();
        listenerButtonSelect();

        listenerButtonCloseEditProfile();
        listenerButtonSaveEditProfile();
    }

    /**
     * Button listener "Select"
     */
    public void listenerButtonSelect()
    {
        if (isLoaded)
        {
            PlayerListView.getSelectButton().addMouseListener(new MouseListener() {
                
                @Override
                public void mouseReleased(MouseEvent e) {
                    if (isLoaded)
                    {
                        if (PlayerListView.getDescriptionPanel().getPlayer() != null){
                            Player p = PlayerListView.getDescriptionPanel().getPlayer();
                            ((IntermediateGameController)controller).back(p);
                            PlayerListView.setVisible(false);
                            profile.save();
                        }
                        else
                        {
                            //PlayerListView.showRequestWindow("Do you accept the video?", null).addActionListener(new ActionListener() {
                        }
                    }
                    else
                    {
                        PlayerListView.setVisible(false);
                        profile.save();
                        controller.back();
                    }
                }            
                @Override
                public void mousePressed(MouseEvent e) {}            
                @Override
                public void mouseExited(MouseEvent e) {}            
                @Override
                public void mouseEntered(MouseEvent e) {}            
                @Override
                public void mouseClicked(MouseEvent e) {}
            });
        }
        else
            PlayerListView.getSelectButton().setVisible(false);
    }

    /**
     * Button listener "Back"
     */
    public void listenerButtonBack()
    {
        PlayerListView.getBackButton().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {
                PlayerListView.setVisible(false);
                profile.save();
                controller.back();}
        });
    }

    /**
     * Button listener "Add"
     */
    public void listenerButtonAdd()
    {
        PlayerListView.getAddButton().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {
                PlayerListView.getAddPlayerView().setPath("");
                PlayerListView.showEditProfile();
                AddPlayerView = PlayerListView.getAddPlayerView();
                
                flag = true;
                buildEditProfile();}
        });
    }

    /**
     * Button listener "Modify"
     */
    public void listenerButtonModify(){
        PlayerListView.getDescriptionPanel().getModifyButton().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {
                PlayerListView.showEditProfile();
                AddPlayerView.setPath(PlayerListView.getDescriptionPanel().getPlayer().getImageSource());
                flag = false;
                buildEditProfile();}
        });
    }

    /**
     * Button listener "Delete"
     */
    public void listenerButtonDelete(){
        PlayerListView.getDescriptionPanel().getDeleteButton().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {
                profile.delete(PlayerListView.getDescriptionPanel().getPlayer());
                PlayerListView.updateList();
                PlayerListView.updateData();
                PlayerListView.getDescriptionPanel().setVisible(false);
            }
        });
    }

    /**
     * EditProfile listener. Here, flag is to verify Modify or Add with EditProfile
     */
    public void buildEditProfile(){
        if(flag){
            AddPlayerView.getPlayerNickname().setText("");
        }else{
            AddPlayerView.getPlayerNickname().setText(PlayerListView.getDescriptionPanel().getPlayer().getUsername());
        }
    }

    /**
     * "Back" listener for EditProfile
     */
    public void listenerButtonCloseEditProfile(){
        AddPlayerView.getCancelButton().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {
                PlayerListView.hideEditProfile();
            }
        });
    }

    /**
     * "Save" listener for EditProfile
     */
    public void listenerButtonSaveEditProfile(){
        AddPlayerView.getSaveButton().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {
                if(flag){
                    boolean flag = true;
                    
                    Player tmpPlayer = new Player();        
                    tmpPlayer.setUsername(AddPlayerView.getPlayerNickname().getText());    
                    tmpPlayer.setImageSource(PlayerListView.getAddPlayerView().getPath());
                    Calendar date = Calendar.getInstance();
                    id = 10000*date.get(Calendar.MONTH)+1000*date.get(Calendar.DATE)
                            +100*date.get(Calendar.HOUR)+10*date.get(Calendar.MINUTE)+date.get(Calendar.SECOND);
                    
                    tmpPlayer.setId(id);
                    
                    for(int i=0; i<profile.getList().size(); i++){
                        if(tmpPlayer.getId() == profile.getList().get(i).getId()){
                            flag = false;
                        }
                    }
                    
                    if(flag){
                        profile.getList().add(tmpPlayer);
                        profile.display();
                    }
                    PlayerListView.setPlayer(tmpPlayer);
                    
                }else{
                    PlayerListView.getDescriptionPanel().getPlayer().setUsername(AddPlayerView.getPlayerNickname().getText());
                    PlayerListView.getDescriptionPanel().getPlayer().setImageSource(PlayerListView.getAddPlayerView().getPath());
                }
                PlayerListView.hideEditProfile();
                PlayerListView.updateList();
                PlayerListView.updateData();
            }
        });
    }

    @Override
    public Controller getController() {
        return this;
    }
    
    @Override
    public JFrame getFrame() {
        return frame;
    }

    @Override
    public void back(){}
}
