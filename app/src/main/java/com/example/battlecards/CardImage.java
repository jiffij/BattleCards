package com.example.battlecards;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

import org.lwjgl.Sys;

public class CardImage extends Image {
    private float lastX, lastY;
    private Texture texture;

    public CardImage(Texture texture) {
        super(texture);
        this.texture = texture;
//        this.setScale(0.3f, 0.3f);
        this.addListener(new DragListener() {
            float deltaX;
            float deltaY;

            @Override
            public void dragStart(InputEvent event, float x, float y, int pointer) {
                deltaX = x;
                deltaY = y;
                System.out.println("drag Start");
            }

            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                Actor actor = event.getListenerActor();
                actor.moveBy(x - deltaX, y - deltaY);
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                super.dragStop(event, x, y, pointer);
            }
        });
//        setBounds(getX(), getY(), getWidth(), getHeight());
    }

    void dispose(){
        texture.dispose();
    }

}