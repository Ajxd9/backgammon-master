package com.HyenaBgammon.exception;

public class TurnNotPlayableException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * 
     * @param message
     */
    public TurnNotPlayableException(String message) {
        super(message);
    }
}
