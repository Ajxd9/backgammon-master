package com.HyenaBgammon.view;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.Timer;

public class AnimatedTransitionBase extends JButton implements ActionListener {

    private static final long serialVersionUID = 8608824468305555276L;
    protected int duration;
    protected int interval;

    private Timer timer;
    protected int value;

    public AnimatedTransitionBase(int interval, int duration) {
        this.duration = duration;
        this.interval = interval;
        timer = new Timer(interval, this);
    }

    public AnimatedTransitionBase(int interval) {
        this.duration = Integer.MAX_VALUE;
        this.interval = interval;
        timer = new Timer(interval, this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        value += interval;
        repaint();
        if (value > duration) {
            stop();
            this.setEnabled(false);
            this.setVisible(false);
        }
    }

    @Override
    protected void paintBorder(Graphics g) {
        // super.paintBorder(g);
    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    public void end() {
        setValue(duration);
        stop();
    }

    public void restart() {
        this.value = 0;
        timer.start();
    }

    public void setValue(int value) {
        this.value = value;
    }

    public float getRatio() {
        return (float) value / (float) duration;
    }
}
