package org.realm_war.Controllers;

import org.realm_war.Models.GameState;

public class GameCtrl {
    private GameState gameState;

    public GameCtrl(GameState gameState) {
        this.gameState = gameState;
    }

    public void startGame(){
        gameState.setRunning(true);
    }

    public void stopGame(){
        gameState.setRunning(false);
    }

    public GameState getGameState() {
        return gameState;
    }
}
