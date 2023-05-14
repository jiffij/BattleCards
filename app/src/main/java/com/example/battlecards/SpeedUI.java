package com.example.battlecards;

import android.content.Context;
import android.content.Intent;
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
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
;

import org.lwjgl.Sys;
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
    SpeedDeck speedDeck;
    SpeedEnemy speedEnemy;
    String mode;//multi, ai
    PLAYERS me;
    String room;
    Context context;

    SpeedUI(String mode, String player, String room, Context context){
        this.mode = mode;
        me = player.equals("A")? PLAYERS.A: PLAYERS.B;
        this.room = room;
        this.context = context;
    }
    @Override
    public void create () {
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        System.out.println(screenWidth);
        System.out.println(screenHeight);
        camera = new OrthographicCamera(screenWidth,screenHeight);
        batch = new SpriteBatch();

        stage = new Stage();
        speedDeck = new SpeedDeck(stage, screenWidth, screenHeight, me);
        speedDeck.load();
        speedDeck.addActor("back");
        Gdx.input.setInputProcessor(stage);
        speedDeck.InitAnim();
        if(this.mode.equals("ai")) {
            speedEnemy = new SpeedAIEnemy(speedDeck);
            speedEnemy.start();
        }else{
            if(me == PLAYERS.A) {
                Realtime real = new Realtime(room);
                real.write("BHand", speedDeck.speed.BHand.getAllCardName());
                real.write("BDeck", speedDeck.speed.BDeck.getAllCardName());
            }
        }
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
//        if(speedDeck.update()) dispose();
        dispose();
        stage.act();
        stage.draw();
    }

    @Override
    public void dispose () {
        speedDeck.dispose();
        try {
            batch.dispose();
            stage.dispose();
        }catch (Exception e){
            System.out.println(e);
        }
        System.out.println("dispose");
        Gdx.app.exit();
        Intent intent1 = new Intent(this.context, Result.class);
        intent1.putExtra("result", this.speedDeck.speed.winner == PLAYERS.A? "win": "loss");
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
    }
}
