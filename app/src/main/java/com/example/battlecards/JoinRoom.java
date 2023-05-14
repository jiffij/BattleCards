package com.example.battlecards;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JoinRoom extends AppCompatActivity {

    EditText RoomInput;
    Button JoinButton;
    int num_player = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_room);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        RoomInput = findViewById(R.id.enterRoomId);
        JoinButton = findViewById(R.id.joinBtn);

        JoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = RoomInput.getText().toString().trim();
                Realtime real = new Realtime(text);
                List list = new ArrayList();
                list.add("");
                real.write("B", list);
                real.addListener((snapshot)->{
                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                    Long val = (Long) map.get("players");
                    num_player = val.intValue();
                    if(num_player >= 2){
                        System.out.println("Room is full");
                        return;
                    }
                    ++num_player;
                    real.write("players", num_player);

                });

            }
        });

    }
}