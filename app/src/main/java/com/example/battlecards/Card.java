package com.example.battlecards;

public class Card {
    String suit;
    String value;
    int realValue;
    String suitAlpha;

    public Card(String suit, String value, int realValue, String suitAlpha){
        this.suit = suit;
        this.value = value;
        this.realValue = realValue;
        this.suitAlpha = suitAlpha;
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

    public String toImageString(){
        String imgName = this.suitAlpha+this.value;
        return imgName.toLowerCase();
    }

    public boolean isNeighbor(Card card){
        int difference = this.realValue - card.realValue;
        difference = Math.abs(difference%11);
        return difference == 1? true: false;
    }
}
