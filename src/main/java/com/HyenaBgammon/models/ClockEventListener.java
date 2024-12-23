package com.HyenaBgammon.models;

public interface ClockEventListener {
    void clockEnd(ClockEvent evt);
    void updateClock(ClockEvent evt);
}
