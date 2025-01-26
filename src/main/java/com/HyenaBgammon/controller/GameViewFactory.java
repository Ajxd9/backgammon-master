package com.HyenaBgammon.controller;

import com.HyenaBgammon.view.GameView;
import com.HyenaBgammon.models.Game;

public class GameViewFactory {
    public static GameView createGameView(Game game) {
        return new GameView(game);
    }
}
