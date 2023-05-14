package com.example.battlecards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

public class Result extends AppCompatActivity {

    TextView state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        state = findViewById(R.id.congrate);
        Intent intent = getIntent();
        String result = intent.getStringExtra("result");
        state.setText(result.toUpperCase());
        state.setTextColor(Color.WHITE);
        state.setTextSize(50);
        state.setGravity(Gravity.CENTER);
    }
}