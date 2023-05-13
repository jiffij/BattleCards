package com.example.battlecards;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

import org.lwjgl.Sys;

public class CardImage extends Image {
    private float lastX, lastY;
    private Texture texture;
    private DragListener dragListener;
    private SpeedDeck uiDeck;
    private String id;

    public CardImage(Texture texture, SpeedDeck uiDeck) {
        super(texture);
        this.texture = texture;
        this.uiDeck = uiDeck;
        dragListener = new DragListener() {
            float deltaX;
            float deltaY;

            @Override
            public void dragStart(InputEvent event, float x, float y, int pointer) {
                deltaX = x;
                deltaY = y;
            }

            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                Actor actor = event.getListenerActor();
                actor.moveBy(x - deltaX, y - deltaY);
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                float stageX = event.getStageX();
                float stageY = event.getStageY();
                if(stageY < uiDeck.midYPos - 200 || stageY > uiDeck.midYPos+200){
                    Actor actor = event.getListenerActor();
                    actor.setPosition(lastX, lastY);
                    return;
                }
                if(stageX > uiDeck.leftPos - 200 && stageX < uiDeck.leftPos+200){//left deck
                    if(!uiDeck.speed.playCard(PLAYERS.A, POOL.LEFT, id)){
                        Actor actor = event.getListenerActor();
                        actor.setPosition(lastX, lastY);
                        return;
                    }
                    Actor actor = event.getListenerActor();
                    actor.setPosition(uiDeck.leftPos, uiDeck.midYPos);
                    uiDeck.loadHand();
                }else if(stageX > uiDeck.rightPos - 200 && stageX < uiDeck.rightPos+200){//right deck
                    if(!uiDeck.speed.playCard(PLAYERS.A, POOL.RIGHT, id)) {
                        Actor actor = event.getListenerActor();
                        actor.setPosition(lastX, lastY);
                        return;
                    }
                    Actor actor = event.getListenerActor();
                    actor.setPosition(uiDeck.rightPos, uiDeck.midYPos);
                    uiDeck.loadHand();
                }else{
                    Actor actor = event.getListenerActor();
                    actor.setPosition(lastX, lastY);
                }

            }
        };
//        setBounds(getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        lastX = x;
        lastY = y;
    }

    @Override
    public void addAction(Action action) {
        super.addAction(Actions.sequence(action, Actions.run(this::clearActions)));
    }

    public void draggable(){
        this.addListener(dragListener);
    }

    public void setCardId(String id){
        this.id = id;
    }

    public void undraggable(){
        this.removeListener(dragListener);
    }

    void dispose(){
        texture.dispose();
    }

}