package com.HyenaBgammon.view;

import javax.swing.ImageIcon;


public enum Avatar{
    DEFAULT("default.png"),
    SLEEPING_FOX("asleep_renard.png"),
    OWL("chouette.png"),
    GUINEA_PIG("choupi.png"),
    RED_EYES("dark_eyes.png"),
    SNAIL("escargot.png"),
    FLOWER("fleur.png"),
    DROPS("goutes.png"),
    KITTEN("mini.png"),
    CAT_HEAD("miou.png"),
    BALLOON("Mongolfieres.png"),
    MOON("night.png"),
    OLD_LION("old_lion.png"),
    FAT_CAT("Patapouf.png"),
    BIRD("piou.png"),
    ROSE("rose.png"),
    HORSE("wind_horse.png"),
    WOLF("wolf.png"),
    YELLOW_CAT("yellow_cat.png");
	
	private final String path;
	private final ImageIcon icon;
	
	Avatar(String path){
		this.path = ImageAvatar.AVATAR_PATH + path;
		this.icon = new ImageIcon(this.path);
	}

	public ImageIcon getIcon() {
		return icon;
	}
	public String getPath() {
		return path;
	}
}