package com.HyenaBgammon.models;

import com.HyenaBgammon.models.SixSidedDie;

public class SixSidedDieFactory {
    public static SixSidedDie createSixSidedDie(String dieColor, int value) {
        return new SixSidedDie(SquareColor.valueOf(dieColor), value);
    }
}
