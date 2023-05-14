package com.example.battlecards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class OpenRoom extends AppCompatActivity {

    TextView roomid;
    TextView waitmessage;
    ProgressBar loading;
    Realtime real;
    int num_player = 1;

    //hash a room id
    public int hashString(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());
            BigInteger bigInt = new BigInteger(1, hash);
            int hashValue = bigInt.intValue();
            if (hashValue < 0) {
                hashValue = -(hashValue + 1);
            }
            return hashValue % 10000;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_room);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        String game = intent.getStringExtra("game");

        roomid = findViewById(R.id.roomId);
        loading = findViewById(R.id.progressBar);
        waitmessage = findViewById(R.id.message);
        int id = hashString(firebaseAuth.getCurrentUser().getUid());
        roomid.setText("Your room ID is: "+Integer.toString(id));
        roomid.setTextColor(Color.WHITE);
        roomid.setTextSize(50);
        roomid.setGravity(Gravity.CENTER);
        waitmessage.setText("Waiting others to join...");
        waitmessage.setTextColor(Color.WHITE);
        waitmessage.setTextSize(20);
        waitmessage.setGravity(Gravity.CENTER);

        real = new Realtime(Integer.toString(id));
        real.write("players", 1);
        List list = new ArrayList();
        list.add("");
        real.write("A", list);
        real.write("game", game);
        real.addListener((snapshot)->{
            Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
            Long val = (Long) map.get("players");
            if(val.intValue() == 2){
                System.out.println("start game");
                if(game.equals("Speed")) {
                    Intent intent1 = new Intent(OpenRoom.this, SpeedLauncher.class);
                    intent1.putExtra("mode", "multi");
                    intent1.putExtra("player", "A");
                    intent1.putExtra("room", Integer.toString(id));
                    startActivity(intent1);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        real.removeListener();
    }
}