package fr.ujm.tse.info4.pgammon.models;

import java.util.EventObject;

public class ClockEvent extends EventObject {
    ClockEventType type;
    
    public enum ClockEventType {
        END,
        UPDATE
    }
    
    public ClockEvent(Clock clock, ClockEventType type) {
        super(clock);
        this.type = type;
    }
    
    public ClockEventType getEventType() {
        return type;
    }

    private static final long serialVersionUID = 1570857988187744152L;
}
