package com.HyenaBgammon.models;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

import com.HyenaBgammon.models.ClockEvent.ClockEventType;

public class Clock implements ActionListener {
    private final int UPDATE_PERIOD = 10;
    ArrayList<ClockEventListener> listeners = new ArrayList<>();
    Timer timer;
    int duration;
    int value;
    private boolean running;
    
    public Clock(int duration_ms) {
        timer = new Timer(UPDATE_PERIOD, this);
        this.duration = duration_ms;  
        running = false;
    }
    
    public void addListener(ClockEventListener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(ClockEventListener listener) {
        listeners.remove(listener);
    }
    
    private void fireClockEnd() {
        ClockEvent event = new ClockEvent(this, ClockEventType.END);
        for (ClockEventListener listener : listeners) {
            listener.clockEnd(event);
        }
    }
    
    private void fireUpdate() {
        ClockEvent event = new ClockEvent(this, ClockEventType.UPDATE);
        for (ClockEventListener listener : listeners) {
            listener.updateClock(event);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        value += UPDATE_PERIOD;
        if(value > duration) {
            stop();
            fireClockEnd();
        }
        fireUpdate();
    }
    
    public void start() {
        running = true;
        timer.start();
    }
    
    public void stop() {
        running = false;
        timer.stop();
    }

    public void end() {
        setValue(duration);
        stop();
    }
    
    public void restart() {
        setValue(0);
        start();
    }
    
    public void setValue(int value) {
        this.value = value;
        fireUpdate();
    }

    public void setDuration(int value) {
        this.duration = value;
        fireUpdate();
    }
    
    public float getRatio() {
        return (float)(value)/(float)(duration);
    }
    
    public Timer getTimer() {
        return timer;
    }
    
    public int getDuration() {
        return duration;
    }
    
    public int getValue() {
        return value;
    }
    
    public boolean isRunning() {
        return running;
    }
    
    public String getRemainingTime() {
        int i = (duration - value)/1000; 
        int j = i/60;        
        if(duration == 0) {
            return "\u221E";
        } else if(i > 60) {
            return new Integer(i/60).toString() + " : " + new Integer(i-60*j);
        } else {
            return new Integer(i).toString() + " s";
        }
    }
}
