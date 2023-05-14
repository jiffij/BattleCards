package com.example.battlecards;

import java.util.ArrayList;
import java.util.List;

enum POOL{
    LEFT, RIGHT
}

public abstract class Speed {

    protected Deck mainDeck = new Deck();
    protected Deck ADeck;
    protected Deck BDeck;
    protected Deck AHand;
    protected Deck BHand;
    protected Deck left;
    protected Deck right;
    protected Deck leftPool;
    protected Deck rightPool;
    protected boolean AWant = false;
    protected boolean BWant = false;
    protected boolean GameContinue = false;
    protected PLAYERS winner = PLAYERS.NONE;

    public abstract PLAYERS checkWin();
    public abstract List<String> pool();
    public abstract List<Card> poolCard();
    public abstract void reload(PLAYERS who);
    public abstract boolean playCard(PLAYERS who, POOL lr, String id);
    public abstract void wantCard(PLAYERS who);
    protected abstract void flipNewCard();
    public abstract boolean gameLoop();

}
