package com.HyenaBgammon.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AddPlayerView extends MonochromePanel {

    /**
     * This class manages the addition or modification of a player
     */

    private static final long serialVersionUID = 3196089768815616270L;

    private MonochromeButton changeAvatarButton;
    private MonochromeButton deleteAvatarButton;
    private MonochromeButton cancelButton;
    private MonochromeButton saveButton;

    private JTextField playerNickname;

    private ImageAvatar playerImage;

    private String path = "";

    /**
     * Constructor for the Add Player View
     */
    public AddPlayerView() {
        super("Profile Editing");
        build();
    }

    /**
     * Setter for the path
     * @param p changes the avatar path and updates it
     */
    public void setPath(String p) {
        path = p;
        updateData();
    }

    /**
     * Updates the avatar path
     */
    private void updateData() {
        playerImage.setPath(path);
    }

    private void build() {
        setLayout(null);

        playerImage = new ImageAvatar(path);
        playerImage.setBounds(25, 130, 105, 105);
        add(playerImage);

        changeAvatarButton = new MonochromeButton("Add Avatar");
        changeAvatarButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        changeAvatarButton.setBounds(150, 130, 230, 40);
        add(changeAvatarButton);

        deleteAvatarButton = new MonochromeButton("Delete Avatar");
        deleteAvatarButton.setBounds(150, 180, 230, 40);
        add(deleteAvatarButton);

        cancelButton = new MonochromeButton("Cancel");
        cancelButton.setBounds(40, 245, 150, 40);
        add(cancelButton);

        saveButton = new MonochromeButton("Save");
        saveButton.setBounds(200, 245, 150, 40);
        add(saveButton);

        playerNickname = new JTextField();
        playerNickname.setBounds(10, 60, 380, 40);
        playerNickname.setBackground(new Color(0x61685A));
        playerNickname.setForeground(new Color(0xCCCCCC));
        add(playerNickname);

        JLabel nicknameLabel = new JLabel("Nickname");
        nicknameLabel.setForeground(new Color(0xCCCCCC));
        nicknameLabel.setBounds(10, 20, 100, 50);
        add(nicknameLabel);

        JLabel avatarLabel = new JLabel("Avatar");
        avatarLabel.setForeground(new Color(0xCCCCCC));
        avatarLabel.setBounds(10, 90, 100, 50);
        add(avatarLabel);

        listenerDeleteAvatar();
    }

    /**
     * Getter for the path
     * @return returns the player's avatar path
     */
    public String getPath() {
        return path;
    }

    /**
     * Setter for the path
     * @param path changes the avatar path to display the new image
     */
    public void setPath1(String path) {
        this.path = path;
    }

    /**
     * Resets the avatar path if the avatar is to be deleted
     */
    private void listenerDeleteAvatar() {
        deleteAvatarButton.addMouseListener(new MouseListener() {

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
                setPath("");
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.dispose();
        super.paintComponent(g);
    }

    /**
     * Getter for the cancel button
     * @return returns the cancel button class
     */
    public MonochromeButton getCancelButton() {
        return cancelButton;
    }

    /**
     * Getter for the change avatar button
     * @return returns the change avatar button class
     */
    public MonochromeButton getChangeAvatarButton() {
        return changeAvatarButton;
    }

    /**
     * Getter for the delete avatar button
     * @return returns the delete avatar button class
     */
    public MonochromeButton getDeleteAvatarButton() {
        return deleteAvatarButton;
    }

    /**
     * Getter for the save button
     * @return returns the save button class
     */	
    public MonochromeButton getSaveButton() {
        return saveButton;
    }

    /**
     * Getter for the player nickname field
     * @return returns the player's nickname value
     */
    public JTextField getPlayerNickname() {
        return playerNickname;
    }
}
