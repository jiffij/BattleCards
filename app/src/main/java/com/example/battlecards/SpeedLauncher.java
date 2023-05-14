package com.example.battlecards;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class SpeedLauncher extends AndroidApplication {
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String mode = intent.getStringExtra("mode");
        String player = intent.getStringExtra("player");
        String room = mode.equals("multi")? intent.getStringExtra("room"): null;
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new SpeedUI(mode, player, room, getApplicationContext()), config);
    }

}
