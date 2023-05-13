package com.example.battlecards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class UIDeck {
    protected HashMap<String, CardImage> cardImages;
    protected Texture backTexture;
    protected Stage stage;
    protected int screenWidth;
    protected int screenHeight;
    protected final int cardWidth = 150;
    protected final int cardHeight = 217;
    String left;
    String right;
    int leftPos;
    int rightPos;
    int midYPos;
    int handStartX;
    int space;
    List<String> myHand;

    public UIDeck(Stage stage, int screenWidth, int screenHeight) {
        cardImages = new HashMap<String, CardImage>();
        this.stage = stage;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }
    public abstract void InitAnim();
    public abstract void update();
    public abstract void load();

    public abstract CardImage cardBack();

    public void addActor(String card){
        stage.addActor(cardImages.get(card));
    }

    public void addActor(CardImage cardImage){
        stage.addActor(cardImage);
    }

    public void addActor(List<CardImage> cardImageList){
        for(CardImage img : cardImageList){
            this.addActor(img);
        }
    }

    public CardImage getCard(String card){
        return cardImages.get(card);
    }

    public List<CardImage> loadNCardBack(int N){
        List<CardImage> cardImageList = new ArrayList<CardImage>();
        for(int i = 0; i < N; i++){
            cardImageList.add(this.cardBack());
        }
        return cardImageList;
    }

    public void clearAnim(List<CardImage> cardImageList){
        for(CardImage img : cardImageList){
            img.clearActions();
        }
    }

    public void removeActor(List<String> cardList){
        for(String key: cardList){
            this.getCard(key).remove();
        }
    }

    public void addMoveToAction(CardImage cardImage, int x, int y){
        cardImage.addAction(Actions.sequence(Actions.moveTo(x,y,0.5f), Actions.run(cardImage::clearActions)));
    }

    public void dispose(){
        cardImages.forEach((k,v)->{
            v.dispose();
        });
    }

}
