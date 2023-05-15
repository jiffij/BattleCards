package com.example.battlecards;

import java.util.ArrayList;
import java.util.List;

public class SpeedB extends Speed {
    public SpeedB(){
        mainDeck = new Deck();
        ADeck = new Deck();
        BDeck = new Deck();
        AHand = new Deck();
        BHand = new Deck();
        left = new Deck();
        right = new Deck();
        leftPool = new Deck();
        rightPool = new Deck();
    }

    public PLAYERS checkWin(){
        if(ADeck.isEmpty() && AHand.isEmpty()){
            return PLAYERS.B;
        }else{
            return PLAYERS.NONE;
        }
    }

    public List<String> pool(){
        List<String> lr = new ArrayList<String>();
        lr.add(leftPool.getCard(0).toImageString());
        lr.add(rightPool.getCard(0).toImageString());
        return lr;
    }

    public List<Card> poolCard(){
        List<Card> lr = new ArrayList<Card>();
        lr.add(leftPool.getCard(0));
        lr.add(rightPool.getCard(0));
        return lr;
    }

    public void reload(PLAYERS who){
        Deck playerHand = (who == PLAYERS.A)? AHand: BHand;
        Deck fromDeck = (who == PLAYERS.A)? ADeck: BDeck;
        if(fromDeck.isEmpty() || playerHand.length() >= 5) return;
        playerHand.draw(fromDeck);
    }

    public boolean playCard(PLAYERS who, POOL lr, String id){
        Deck pool = (lr == POOL.LEFT)? leftPool: rightPool;
        Deck hand = (who == PLAYERS.A)? AHand: BHand;
        Card card = hand.getCard(id);
        if(card == null) return false;
        if(!pool.getCard(0).isNeighbor(card)) return false;
        pool.moveToTopFrom(hand, id);
        return true;
    }

    public void wantCard(PLAYERS who){
        switch (who){
            case A:
                AWant = true;
                break;
            case B:
                BWant = true;
                break;
        }
    }

    protected void flipNewCard(){
        if(!AWant || !BWant) return;
        AWant = false;
        BWant = false;
        //left right pile are empty
        if(left.isEmpty() || right.isEmpty()){
            leftPool.shuffle();
            rightPool.shuffle();
            left.moveAllFrom(leftPool);
            right.moveAllFrom(rightPool);
        }

        leftPool.moveToTopFrom(left, 0);
        rightPool.moveToTopFrom(right, 0);
    }



    public boolean gameLoop(){
//        PLAYERS winner = PLAYERS.NONE;
//        while(GameContinue){
        flipNewCard();
        if(winner == PLAYERS.NONE) winner = checkWin();
        else return true;
        return false;
    }
}
