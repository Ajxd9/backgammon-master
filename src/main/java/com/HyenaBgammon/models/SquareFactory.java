package com.HyenaBgammon.models;

import com.HyenaBgammon.models.Square;
import com.HyenaBgammon.models.SquareColor;

public class SquareFactory {
    public static Square createSquare(SquareColor color, int numCheckers, int position) {
        return new Square(color, numCheckers, position);
    }
}
