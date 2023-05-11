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
//    private Texture texture1;
//    private Texture bgTexture;
//    private Image cardImage1;
    private Stage stage;
    int screenWidth;
    int screenHeight;
    private HashMap<String, CardImage> cardImages;
    private List<Texture> textures;
    private Texture backTexture;
    Speed speed;
    int count = 0;

    @Override
    public void create () {
//        batch = new SpriteBatch();
//        img = new Texture("back.png");
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        System.out.println(screenWidth);
        System.out.println(screenHeight);
        camera = new OrthographicCamera(screenWidth,screenHeight);
        batch = new SpriteBatch();

        speed = new Speed();
        textures = new ArrayList<Texture>();
//        bgTexture = new Texture(Gdx.files.internal("pokertablezoomin.jpg"));

        //read all card textures
        String cardSuitAlpha[] = {"s", "h", "c", "d"};
        String cardValue[] = {"A","2","3","4","5","6","7","8","9","10","J","Q","K",};
        cardImages = new HashMap<String, CardImage>();
        for(int i=0; i<4; i++){
            for(int j=0; j<13; j++){
                String fileName = cardSuitAlpha[i]+cardValue[j];
                fileName = fileName.toLowerCase();
                Texture texture = new Texture(Gdx.files.internal(fileName+".png"));
//                textures.add(texture);
                CardImage img = new CardImage(texture);
//                img.setScale(0.3f, 0.3f);
                cardImages.put(fileName, img);
            }
        }
        backTexture = new Texture(Gdx.files.internal("back.png"));
//        textures.add(backTexture);
        CardImage img = new CardImage(backTexture);
//        cardImage1.setOrigin( 500/2,  726/2);
        img.setPosition(0, 0);
//        img.setScale(0.3f, 0.3f);//300, 217.8
        cardImages.put("back", img);

        stage = new Stage();

        stage.addActor(cardImages.get("sa"));
        Gdx.input.setInputProcessor(stage);
//        cardImages.get("sa").addAction(Actions.sequence(Actions.moveTo(500,0,0.5f)));

    }

    @Override
    public void render () {
//        Gdx.gl.glClearColor(42/255f, 111/255f, 55/255f, 1f);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        batch.begin();
//        batch.draw(img, 500, 500);
//        batch.end();
        Gdx.gl.glClearColor(BACKGROUND_COLOR.r,
                BACKGROUND_COLOR.g,
                BACKGROUND_COLOR.b,
                BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
//        batch.draw(bgTexture,   // Texture
//                -screenWidth/2-150, -screenHeight-300,     // x, y
//                0, 0,           // originX, originY
//                screenWidth, screenHeight,      // width, height
//                1.7f, 1.7f,           // scaleX, scaleY
//                0.0f,           // rotation
//                0, 0,           // srcX, srcY
//                screenWidth, screenHeight,      // srcWidth, srcHeight
//                false, false);  // flipX, flipY

        batch.end();
        stage.act();
        stage.draw();

    }

    @Override
    public void dispose () {
//        batch.dispose();
//        img.dispose();
//        for (CardImage img : cardImages ) {
//            img.dispose();
//        }
        cardImages.forEach((k,v)->{
            v.dispose();
        });
        batch.dispose();
        stage.dispose();

    }
}
