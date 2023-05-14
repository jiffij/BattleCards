package com.example.battlecards;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.lwjgl.Sys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ranking extends AppCompatActivity {
    TextView title;
    ImageButton imgBtn;
    ListView listView;
    List<List<String>> rank_list = new ArrayList<List<String>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        title = findViewById(R.id.ranking_title);
        imgBtn = findViewById(R.id.ranking_back_button);
        listView = findViewById(R.id.ranking_list);
        String titleStr = "Ranking";
        title.setText(titleStr);
        title.setTextColor(Color.WHITE);
        title.setTextSize(50);
        title.setGravity(Gravity.CENTER);
        imgBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Ranking.this, HomePage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        System.out.println("before get");
        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String name = document.getString("name");
                            String wins = document.getString("win");
                            String losses = document.getString("loss");
                            List<String> player = new ArrayList<>();
                            player.add(name);
                            player.add(wins);
                            player.add(losses);
                            this.rank_list.add(player);
                        }
                        System.out.println(this.rank_list.size());
                        Collections.sort(rank_list, new Comparator<List>() {
                            @Override
                            public int compare(List o1, List o2) {
                                return Integer.parseInt(o2.get(1).toString()) - Integer.parseInt(o1.get(1).toString());
                            }
                        });
                        List<String> arr = new ArrayList<>();
                        for (List<String> list:rank_list) {
                            String sum = "";
                            sum = sum + list.get(0);
                            for(int i = 0; i < (30 - list.get(0).length()); i++){
                                sum += " ";
                            }
                            sum = sum + list.get(1);
                            for(int i = 0; i < 10 - list.get(1).length(); i++){
                                sum += " ";
                            }
                            sum = sum + list.get(2);
                            arr.add(sum);
                        }
                        WhiteTextAdapter adapter = new WhiteTextAdapter(this, arr);
                        adapter.setTextSize(13);
                        adapter.setGravity(Gravity.LEFT);
                        listView.setAdapter(adapter);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });


    }
}