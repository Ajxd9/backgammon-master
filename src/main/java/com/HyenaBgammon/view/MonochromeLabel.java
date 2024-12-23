package com.HyenaBgammon.view;

import javax.swing.JLabel;

/**
 * Extension of the label design.
 * Author: Jean-Mi
 */
public class MonochromeLabel extends MonochromeButton {
    private static final long serialVersionUID = 1L;
    String text;
    JLabel label;

    /**
     * New MonochromeLabel.
     */
    public MonochromeLabel() {
        super();
    }

    /**
     * New MonochromeLabel.
     * @param str text content.
     */
    public MonochromeLabel(String str) {
        super(str);
    }
}
