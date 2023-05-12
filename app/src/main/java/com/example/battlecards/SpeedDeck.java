package com.example.battlecards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpeedDeck extends UIDeck {

    Speed speed;
    public SpeedDeck(Stage stage, int screenWidth, int screenHeight) {
        super(stage, screenWidth, screenHeight);
        this.speed = new Speed();
        List<String> lr = this.speed.pool();
        left = lr.get(0);
        right = lr.get(1);
        leftPos = screenWidth/2 - (cardWidth+20);
        rightPos = screenWidth/2 + (cardWidth+20);
        midYPos = screenHeight/2-cardHeight/2;
        handStartX = screenWidth/2 - 2*(cardWidth+10);
        space = cardWidth + 10;
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
        cardImageList.get(6).addAction(Actions.sequence(Actions.moveTo(screenWidth/2 - (cardWidth+20),screenHeight/2-cardHeight/2,0.5f)));
        cardImageList.get(7).addAction(Actions.sequence(Actions.moveTo(screenWidth/2 + (cardWidth+20),screenHeight/2-cardHeight/2,0.5f)));
        cardImageList.get(8).addAction(Actions.sequence(Actions.moveTo(screenWidth/2 + (2*cardWidth+50),screenHeight/2-cardHeight/2,0.5f)));
        InputListener onPress = new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Handle touch down event
                speed.wantCard(PLAYERS.A);
                addActor(getCard("blue"));
                return true; // Return true to indicate that the event was handled
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // Handle touch up event
                getCard("blue").remove();
            }
        };
        cardImageList.get(8).addListener(onPress);
        this.getCard(left).setPosition(leftPos,midYPos);
        this.addActor(this.getCard(left));
        this.getCard(right).setPosition(rightPos, midYPos);
        this.addActor(this.getCard(right));
        this.loadHand();
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

    public void update(){
        speed.gameLoop();
        List<String> lr = this.speed.pool();
        if(!lr.get(0).equals(left)){
            System.out.println("add to left pool");
            left = lr.get(0);
            this.getCard(left).setPosition(leftPos, midYPos);
            this.addActor(this.getCard(left));
        }
        if(!lr.get(1).equals(right)){
            right = lr.get(1);
            this.getCard(right).setPosition(rightPos, midYPos);
            this.addActor(this.getCard(right));
        }

    }

}
