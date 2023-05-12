package com.example.battlecards;

import android.os.Handler;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SpeedUI extends ApplicationAdapter {
//    SpriteBatch batch;
//    Texture img;
    private static final Color BACKGROUND_COLOR = new Color(42/255f, 111/255f, 55/255f, 1.0f);
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Stage stage;
    int screenWidth;
    int screenHeight;
    UIDeck speedDeck;

    @Override
    public void create () {
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        System.out.println(screenWidth);
        System.out.println(screenHeight);
        camera = new OrthographicCamera(screenWidth,screenHeight);
        batch = new SpriteBatch();

        stage = new Stage();
        speedDeck = new SpeedDeck(stage, screenWidth, screenHeight);
        speedDeck.load();
        speedDeck.addActor("back");
        Gdx.input.setInputProcessor(stage);
        speedDeck.InitAnim();

    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(BACKGROUND_COLOR.r,
                BACKGROUND_COLOR.g,
                BACKGROUND_COLOR.b,
                BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.end();
        speedDeck.update();
        stage.act();
        stage.draw();

    }

    @Override
    public void dispose () {
        speedDeck.dispose();
        batch.dispose();
        stage.dispose();

    }
}
