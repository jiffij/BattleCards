package com.example.battlecards;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundPlayer {
    private static SoundPool soundPool;
    private static int coin;

    public SoundPlayer(Context context) {
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);

        coin = soundPool.load(context, R.raw.coin, 1);
    }

    public void playCoinSound() {
        soundPool.play(coin, 100, 100, 1, 0, 1.0f);

    }
}