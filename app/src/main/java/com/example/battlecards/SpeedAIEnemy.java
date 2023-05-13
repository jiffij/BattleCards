package com.example.battlecards;

import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;

import java.util.List;

public class SpeedAIEnemy extends Thread implements SpeedEnemy{

    SpeedDeck uiDeck;
    public SpeedAIEnemy(SpeedDeck uiDeck) {
        this.uiDeck = uiDeck;
    }

    @Override
    public void update() {
        if(uiDeck.speed.BHand.length() < 5) uiDeck.speed.reload(PLAYERS.B);// reload card
        List<Card> pool = uiDeck.speed.poolCard();
        List<Card> cards = uiDeck.speed.BHand.getAllCard();
        int pick = -1;
        POOL lr = null;
        for(int i = 0; i < cards.size(); i++){
            for(int j = 0; j < pool.size(); j++){
                if(cards.get(i).isNeighbor(pool.get(j))){
                    lr = j < 1? POOL.LEFT: POOL.RIGHT;
                    pick = i;
                    break;
                }
            }
            if(pick > -1) break;
        }
        if(lr == null) {
            if(this.uiDeck.speed.AWant){
                this.uiDeck.speed.wantCard(PLAYERS.B);
            }
            return;
        }
        this.uiDeck.speed.playCard(PLAYERS.B, lr, cards.get(pick).toImageString());
    }

    @Override
    public void run(){
        while(uiDeck.speed.checkWin() == PLAYERS.NONE){
            update();
            try {
                Thread.sleep(2000); // Sleep for 1 second
            } catch (InterruptedException e) {
                // Handle the exception
            }
        }
    }
}
