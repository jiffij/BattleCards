package com.example.battlecards;

public class Card {
    String suit;
    String value;

    public Card(String suit, String value){
        this.suit = suit;
        this.value = value;
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
}
