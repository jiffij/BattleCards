package com.example.battlecards;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Deck {
    private ArrayList<Card> cards;
    int numOfCards = 0;
    String spade = "\u2660";
    String heart = "\u2665";
    String club = "\u2663";
    String diamond = "\u2666";
    String cardSuit[] = {spade,heart,club,diamond};
    String cardSuitAlpha[] = {"s", "h", "c", "d"};
    String cardValue[] = {"A","2","3","4","5","6","7","8","9","10","J","Q","K",};
    int cardIntValue[] = {1,2,3,4,5,6,7,8,9,10,11,12,13};

    public Deck(){
        this.cards = new ArrayList<Card>();
    }
    //********************************************************************************************
    public void addAllCards(){
        for(int i=0; i<4; i++){
            for(int j=0; j<13; j++){
                this.cards.add(new Card(cardSuit[i], cardValue[j], cardIntValue[j], cardSuitAlpha[i]));
                this.numOfCards += 1;
            }
        }
    }
    //********************************************************************************************
    public void shuffle(){
        Collections.shuffle(this.cards);
    }
    //********************************************************************************************
    public Card getCard(int i){
        return this.cards.get(i);
    }
    //********************************************************************************************
    public void removeCard(int i){
        this.cards.remove(i);
        this.numOfCards -= 1;
    }
    //********************************************************************************************
    public void addCard(Card newCard){
        this.cards.add(newCard);
        this.numOfCards += 1;
    }
    //********************************************************************************************
    public void draw(Deck cardDeck){
        this.cards.add(cardDeck.getCard(0));
        cardDeck.removeCard(0);
        this.numOfCards += 1;
    }
    //********************************************************************************************
    public void moveToTopFrom(Deck cardDeck, int i){
        this.cards.add(0, cardDeck.getCard(i));
        cardDeck.removeCard(i);
    }
    public void moveToTopFrom(Deck cardDeck, String i){
        this.cards.add(0, cardDeck.getCard(i));
        cardDeck.removeCard(i);
    }
    public List<Card> getTopNCards(int N){
        return this.cards.subList(0, N);
    }
    public void removeNCards(List<Card> cardList){
        this.cards.removeAll(cardList);
    }
    public void drawTopNCards(Deck cardDeck, int N){
        List<Card> cardList = cardDeck.getTopNCards(N);
        this.cards.addAll(cardList);
        cardDeck.removeNCards(cardList);
    }
    public List<String> getAllCardName(){
        List<String> cardsname = new ArrayList<>();
        for(Card card: cards){
            cardsname.add(card.toImageString());
        }
        return cardsname;
    }
    public List<Card> getAllCard(){
        return cards;
    }
    public Card getCard(String key){
        for(Card card : cards){
            if(card.toImageString().equals(key)) return card;
        }
        return null;
    }
    public void removeCard(String key){
        for(int i = 0; i < this.cards.size(); i++){
            if(this.cards.get(i).toImageString().equals(key)){
                this.cards.remove(i);
                break;
            }
        }
    }

    public void moveAllFrom(Deck cardDeck){
        int length = cardDeck.length();
        this.drawTopNCards(cardDeck, length);
    }

    public boolean isEmpty(){
        return this.cards.isEmpty();
    }

    public int length(){
        return this.cards.size();
    }
    public void addCardWithImgNameToTop(String name){
        String suit = name.substring(0,1);
        String value = name.substring(1).toUpperCase();
//        List cardSuitList = new ArrayList(Arrays.asList(cardSuitAlpha));
//        List cardValueList = new ArrayList(Arrays.asList(cardValue));
        int suitIdx = Arrays.asList(cardSuitAlpha).indexOf(suit);
        int valueIdx = Arrays.asList(cardValue).indexOf(value);
//        Log.d("suit", String.valueOf(suitIdx));
//        Log.d("value", String.valueOf(valueIdx));
        this.cards.add(0, new Card(cardSuit[suitIdx], cardValue[valueIdx], cardIntValue[valueIdx], cardSuitAlpha[suitIdx]));
    }

    public void addAllCardWithImgName(List<String> nameList){
        for(String key: nameList){
            this.addCardWithImgNameToTop(key);
        }
    }

    //********************************************************************************************
    public int cardsTotalValue(){
        int cardsTotalValue = 0;
        int aces = 0;
        for(Card tempCard: this.cards ){
            switch(tempCard.getValue()){

                case "2": cardsTotalValue +=2; break;
                case "3": cardsTotalValue +=3; break;
                case "4": cardsTotalValue +=4; break;
                case "5": cardsTotalValue +=5; break;
                case "6": cardsTotalValue +=6; break;
                case "7": cardsTotalValue +=7; break;
                case "8": cardsTotalValue +=8; break;
                case "9": cardsTotalValue +=9; break;
                case "10":
                case "J":
                case "Q":
                case "K":
                    cardsTotalValue +=10; break;
                case "A": aces +=1; break;
            }
        }
        for (int i=0; i< aces; i++){
            if (cardsTotalValue>10){
                cardsTotalValue +=1;
            }
            else{
                cardsTotalValue +=11;
            }
        }
        return cardsTotalValue;
    }
    //********************************************************************************************
    public void returnAllCards(Deck cardDeck) {
        int playingDeckSize = this.cards.size();
        for(int i=0; i<playingDeckSize; i++){
            cardDeck.addCard(this.getCard(i));
        }
        for(int i=0; i<playingDeckSize; i++){
            this.removeCard(0);
        }
    }
    //********************************************************************************************
    public String toString(){
        String cardsOutput = "";
        int i=1;
        for(Card tempCard: this.cards ){
            if (i==14 || i==27 || i==40) {
                cardsOutput += "\n" + " "+ tempCard.toString() + ", ";
            } else{
                cardsOutput += " "+ tempCard.toString()+ ", ";
            }
            i+=1;
        }
        return cardsOutput ;
    }
    //********************************************************************************************
    public String toString2(){
        String cardsOutput = "";
        cardsOutput += " "+ this.cards.get(0) + ", ?";
        return cardsOutput ;
    }

}

