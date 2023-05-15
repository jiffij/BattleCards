package com.example.battlecards;

import android.content.Intent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import org.lwjgl.Sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SpeedDeck extends UIDeck {

    Speed speed;
    SpeedEnemy enemy;
    boolean online;
    String room;
    Realtime real;
    PLAYERS me;
    public SpeedDeck(Stage stage, int screenWidth, int screenHeight, PLAYERS me, boolean online, String room) {
        super(stage, screenWidth, screenHeight);
        this.speed = (me == PLAYERS.A) ? new SpeedA() : new SpeedB();
        this.me = me;

        if(me == PLAYERS.A) {
            List<String> lr = this.speed.pool();
            left = lr.get(0);
            right = lr.get(1);
        }else{
            left = "";
            right = "";
        }
        leftPos = screenWidth / 2 - (cardWidth + 20);
        rightPos = screenWidth / 2 + (cardWidth + 20);
        midYPos = screenHeight / 2 - cardHeight / 2;
        handStartX = screenWidth / 2 - 2 * (cardWidth + 10);
        space = cardWidth + 10;
        enemy = new SpeedAIEnemy(this);
        this.online = online;
        this.room = room;
        if (online && room != null) {//online mode, add pool card to top
            real = new Realtime(room);
            real.addListener((snapshot) -> {
                Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                String winner = (String) map.get("winner");
                String leftPool = (String) map.get("left");
                String rightPool = (String) map.get("right");
                String Awant = (String) map.get("AWant");
                String Bwant = (String) map.get("BWant");
                if(leftPool != null && !leftPool.equals(left)){
                    this.speed.leftPool.addCardWithImgNameToTop(leftPool);
//                    left = leftPool;
                }
                if(rightPool != null && !rightPool.equals(right)){
                    this.speed.rightPool.addCardWithImgNameToTop(rightPool);
//                    right = rightPool;
                }
                if(winner != null && !winner.equals("")){
                    this.speed.winner = winner.equals("A")? PLAYERS.A: PLAYERS.B;
                }
                if(Awant != null)
                if(Awant.equals("Y") && Bwant.equals("Y")){
                    speed.wantCard(PLAYERS.A);
                    speed.wantCard(PLAYERS.B);
                    real.write(me == PLAYERS.A? "AWant":"BWant", "N");
                }
            });
        }
    }
    public void load(){
        String cardSuitAlpha[] = {"s", "h", "c", "d"};
        String cardValue[] = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K",};
        for(int i=0; i<4; i++){
            for(int j=0; j<13; j++){
                String fileName = cardSuitAlpha[i]+cardValue[j];
                fileName = fileName.toLowerCase();
                Texture texture = new Texture(Gdx.files.internal(fileName+".png"));
                CardImage img = new CardImage(texture, this);
                cardImages.put(fileName, img);
            }
        }
        backTexture = new Texture(Gdx.files.internal("back.png"));
        CardImage img = new CardImage(backTexture, this);
        img.setPosition(screenWidth/2 + (2*cardWidth+50), midYPos);
        cardImages.put("back", img);
        Texture blueBack = new Texture(Gdx.files.internal("back2.png"));
        img = new CardImage(blueBack, this);
        img.setPosition(screenWidth/2 + (2*cardWidth+50), midYPos);
        cardImages.put("blue", img);
    }

    public CardImage cardBack(){
        return new CardImage(backTexture, this);
    }
    public void InitAnim(){
        List<CardImage> cardImageList = this.loadNCardBack(9);
        this.addActor(cardImageList);
        cardImageList.get(0).addAction(Actions.moveTo(screenWidth/2 - 2*(cardWidth+10),screenHeight-cardHeight,0.5f));
        cardImageList.get(1).addAction(Actions.sequence(Actions.moveTo(screenWidth/2 - (cardWidth+10),screenHeight-cardHeight,0.5f)));
        cardImageList.get(2).addAction(Actions.sequence(Actions.moveTo(screenWidth/2,screenHeight-cardHeight,0.5f)));
        cardImageList.get(3).addAction(Actions.sequence(Actions.moveTo(screenWidth/2 + (cardWidth+10),screenHeight-cardHeight,0.5f)));
        cardImageList.get(4).addAction(Actions.sequence(Actions.moveTo(screenWidth/2 + 2*(cardWidth+10),screenHeight-cardHeight,0.5f)));
        cardImageList.get(5).addAction(Actions.sequence(Actions.moveTo(screenWidth/2 - (2*cardWidth+50),screenHeight/2-cardHeight/2,0.5f)));
//        cardImageList.get(6).addAction(Actions.sequence(Actions.moveTo(screenWidth/2 - (cardWidth+20),screenHeight/2-cardHeight/2,0.5f)));
//        cardImageList.get(7).addAction(Actions.sequence(Actions.moveTo(screenWidth/2 + (cardWidth+20),screenHeight/2-cardHeight/2,0.5f)));
        cardImageList.get(8).addAction(Actions.sequence(Actions.moveTo(screenWidth/2 + (2*cardWidth+50),screenHeight/2-cardHeight/2,0.5f)));
        InputListener onPress = new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Handle touch down event
                speed.wantCard(PLAYERS.A);
                if(online){
                    real.write(me == PLAYERS.A? "AWant":"BWant", "Y");
                }
                addActor(getCard("blue"));
                return true; // Return true to indicate that the event was handled
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // Handle touch up event
                getCard("blue").remove();
            }
        };
        cardImageList.get(8).addListener(onPress);

        if(this.me == PLAYERS.B) {
            List<String> lr = this.speed.pool();
            left = lr.get(0);
            right = lr.get(1);
        }
        this.getCard(left).setPosition(leftPos,midYPos);
        this.addActor(this.getCard(left));
        this.getCard(right).setPosition(rightPos, midYPos);
        this.addActor(this.getCard(right));
        this.loadHand();
        System.out.println("after load hand");
    }

    public void loadHand(){
        this.speed.reload(PLAYERS.A);
        List<String> cards = this.speed.AHand.getAllCardName();
        if(myHand != null && myHand.equals(cards)) return;
        this.removeActor(cards);
        myHand = cards;
        for(int i = 0; i < cards.size(); i++){
            CardImage img = this.getCard(cards.get(i));
            img.draggable();
            img.setCardId(cards.get(i));
            img.setPosition(handStartX + i*space, 0);
            this.addActor(img);
        }
    }

    private void removePoolCard(){
        Set<String> leftPoolWithoutDup = new LinkedHashSet<>(this.speed.leftPool.getAllCardName());
        Set<String> rightPoolWithoutDup = new LinkedHashSet<>(this.speed.rightPool.getAllCardName());
        List<String> leftPoolListWithoutDup = new ArrayList<>(leftPoolWithoutDup);
        List<String> rightPoolListWithoutDup = new ArrayList<>(rightPoolWithoutDup);
        List<String> lr = this.speed.pool();
        leftPoolListWithoutDup.remove(lr.get(0));
        rightPoolListWithoutDup.remove(lr.get(1));
        this.removeActor(leftPoolListWithoutDup);
        this.removeActor(rightPoolListWithoutDup);
    }

    public boolean update(){
        if(this.speed.leftPool.isEmpty() || this.speed.rightPool.isEmpty()) return false;
        if(speed.gameLoop()){
            if(online && me == this.speed.winner)
                real.write("winner", me == PLAYERS.A? "A":"B");
            return true;
        }
        List<String> lr = this.speed.pool();
        if(!lr.get(0).equals(left)){
            left = lr.get(0);
            this.getCard(left).setPosition(leftPos, midYPos);
            this.addActor(this.getCard(left));
            if(this.online){
                real.write("left", lr.get(0));
            }
        }
        if(!lr.get(1).equals(right)){
            right = lr.get(1);
            this.getCard(right).setPosition(rightPos, midYPos);
            this.addActor(this.getCard(right));
            if(this.online){
                real.write("right", lr.get(1));
            }
        }
        removePoolCard();
        return false;
    }

    @Override
    public void dispose(){
        super.dispose();
        if(online) real.removeListener();
    }

}
