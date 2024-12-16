package fr.ujm.tse.info4.pgammon.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Calendar;
import javax.swing.JFrame;

import fr.ujm.tse.info4.pgammon.models.Player;
import fr.ujm.tse.info4.pgammon.models.Profiles;
import fr.ujm.tse.info4.pgammon.views.ViewAddPlayer;
import fr.ujm.tse.info4.pgammon.views.ViewPlayerList;

public class PlayerListController implements Controller {

    private ViewPlayerList viewPlayerList;
    private Controller controller;
    private Profiles profile;
    private ViewAddPlayer viewAddPlayer;
    private Boolean flag = true;
    private Integer id = 0;
    private boolean isLoaded;
    private JFrame frame;
    
    public PlayerListController(boolean isLoaded, Controller controller) 
    {
        this.controller = controller;
        
        profile = Profiles.getProfiles();
        
        this.isLoaded = isLoaded;
        
        viewPlayerList = new ViewPlayerList();
        controller.getController().getFrame().setContentPane(viewPlayerList);

        viewAddPlayer = viewPlayerList.getViewAddPlayer();
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
            viewPlayerList.getSelect().addMouseListener(new MouseListener() {
                
                @Override
                public void mouseReleased(MouseEvent e) {
                    if (isLoaded)
                    {
                        if (viewPlayerList.getPanelDescription().getPlayer() != null){
                            Player p = viewPlayerList.getPanelDescription().getPlayer();
                            ((IntermediateGameController)controller).back(p);
                            viewPlayerList.setVisible(false);
                            profile.save();
                        }
                        else
                        {
                            //viewPlayerList.showRequestWindow("Do you accept the video?", null).addActionListener(new ActionListener() {
                        }
                    }
                    else
                    {
                        viewPlayerList.setVisible(false);
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
            viewPlayerList.getSelect().setVisible(false);
    }

    /**
     * Button listener "Back"
     */
    public void listenerButtonBack()
    {
        viewPlayerList.getBack().addMouseListener(new MouseListener() {
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
                viewPlayerList.setVisible(false);
                profile.save();
                controller.back();}
        });
    }

    /**
     * Button listener "Add"
     */
    public void listenerButtonAdd()
    {
        viewPlayerList.getAdd().addMouseListener(new MouseListener() {
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
                viewPlayerList.getViewAddPlayer().setPath("");
                viewPlayerList.showEditProfile();
                viewAddPlayer = viewPlayerList.getViewAddPlayer();
                
                flag = true;
                buildEditProfile();}
        });
    }

    /**
     * Button listener "Modify"
     */
    public void listenerButtonModify(){
        viewPlayerList.getPanelDescription().getModify().addMouseListener(new MouseListener() {
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
                viewPlayerList.showEditProfile();
                viewAddPlayer.setPath(viewPlayerList.getPanelDescription().getPlayer().getImageSource());
                flag = false;
                buildEditProfile();}
        });
    }

    /**
     * Button listener "Delete"
     */
    public void listenerButtonDelete(){
        viewPlayerList.getPanelDescription().getDelete().addMouseListener(new MouseListener() {
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
                profile.delete(viewPlayerList.getPanelDescription().getPlayer());
                viewPlayerList.updateList();
                viewPlayerList.updateData();
                viewPlayerList.getPanelDescription().setVisible(false);
            }
        });
    }

    /**
     * EditProfile listener. Here, flag is to verify Modify or Add with EditProfile
     */
    public void buildEditProfile(){
        if(flag){
            viewAddPlayer.getNickname().setText("");
        }else{
            viewAddPlayer.getNickname().setText(viewPlayerList.getPanelDescription().getPlayer().getNickname());
        }
    }

    /**
     * "Back" listener for EditProfile
     */
    public void listenerButtonCloseEditProfile(){
        viewAddPlayer.getBack().addMouseListener(new MouseListener() {
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
                viewPlayerList.hideEditProfile();
            }
        });
    }

    /**
     * "Save" listener for EditProfile
     */
    public void listenerButtonSaveEditProfile(){
        viewAddPlayer.getSave().addMouseListener(new MouseListener() {
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
                    tmpPlayer.setNickname(viewAddPlayer.getNickname().getText());    
                    tmpPlayer.setImageSource(viewPlayerList.getViewAddPlayer().getPath());
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
                    viewPlayerList.setPlayer(tmpPlayer);
                    
                }else{
                    viewPlayerList.getPanelDescription().getPlayer().setNickname(viewAddPlayer.getNickname().getText());
                    viewPlayerList.getPanelDescription().getPlayer().setImageSource(viewPlayerList.getViewAddPlayer().getPath());
                }
                viewPlayerList.hideEditProfile();
                viewPlayerList.updateList();
                viewPlayerList.updateData();
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
