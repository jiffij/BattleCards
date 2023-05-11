package com.example.battlecards;

public class Card {
    String suit;
    String value;
    int realValue;

    public Card(String suit, String value, int realValue){
        this.suit = suit;
        this.value = value;
        this.realValue = realValue;
    }
    //********************************************************************************************
    public String getSuit(){
        return this.suit;
    }
    //********************************************************************************************
    public String getValue(){
        return this.value;
    }
    //********************************************************************************************
    public String toString(){
        return this.suit + this.value;
    }

    public boolean isNeighbor(Card card){
        int difference = realValue - card.realValue;
        difference = Math.abs(difference%11);
        return difference == 1? true: false;
    }
}
