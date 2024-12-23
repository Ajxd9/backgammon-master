package com.HyenaBgammon.view;

import java.util.SortedSet;

import javax.swing.JPanel;

public class MonochromeView extends JPanel {
    private static final long serialVersionUID = -5240761849241606403L;
    private JPanel animationPanel;

    public MonochromeView() {
        build();
    }

    private void build() {
        this.setLayout(null);
        animationPanel = new JPanel();
        animationPanel.setLayout(null);
        animationPanel.setBounds(0, 0, 800, 600);
        animationPanel.setOpaque(false);
        add(animationPanel);
    }

    private void setAnimation(AnimatedTransitionBase animation) {
        animation.setBounds(animationPanel.getBounds());
        animationPanel.removeAll();
        animationPanel.add(animation);
    }

    /**
     * Displays a transition with the provided text parameters.
     * @param title Text of the main banner.
     * @param text Text for the exterior.
     */
    public void displayTransition(String title, String text) {
        TurnChangeAnimation turnAnimation = new TurnChangeAnimation(title, text);
        turnAnimation.start();
        setAnimation(turnAnimation);
    }

    /**
     * Displays a window with buttons allowing interaction with the user.
     * @param title Main text.
     * @param responses List of button labels.
     * @return Returns the created RequestAnimationWindowBase. This component can
     * be listened to for user responses.
     */
    public RequestAnimationWindowBase displayRequestWindow(String title, SortedSet<String> responses) {
        RequestAnimationWindowBase requestWindow = new RequestAnimationWindowBase(title, responses);
        setAnimation(requestWindow);
        requestWindow.start();
        return requestWindow;
    }

    /**
     * Displays a simple window to inform the user.
     * @param title Main text.
     * @param subTitle Subtitle or description.
     * @return Returns the created RequestAnimationWindowBase. This component can
     * be listened to for window closure events.
     */
    public RequestAnimationWindowBase displayRequestWindow(String title, String subTitle) {
        RequestAnimationWindowBase requestWindow = new RequestAnimationWindowBase(title, subTitle);
        setAnimation(requestWindow);
        requestWindow.start();
        return requestWindow;
    }
}

