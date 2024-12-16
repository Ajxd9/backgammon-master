package fr.ujm.tse.info4.pgammon.models;

public interface ClockEventListener {
    void clockEnd(ClockEvent evt);
    void updateClock(ClockEvent evt);
}
