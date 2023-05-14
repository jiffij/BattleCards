package com.example.battlecards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Result extends AppCompatActivity {

    TextView state;
    ImageButton imgBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        state = findViewById(R.id.congrate);
        imgBtn = findViewById(R.id.back_button);
        Intent intent = getIntent();
        String result = intent.getStringExtra("result");
        state.setText(result.toUpperCase());
        state.setTextColor(Color.WHITE);
        state.setTextSize(50);
        state.setGravity(Gravity.CENTER);
        imgBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Result.this, HomePage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        db.collection("users").document(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    String winStr = document.get("win").toString();
                    String lossStr = document.get("loss").toString();
                    int win = Integer.parseInt(winStr);
                    int loss = Integer.parseInt(lossStr);
                    if(result.equals("win"))
                        win++;
                    else
                        loss++;
                    Map<String, Object> data = new HashMap<>();
                    data.put("win", String.valueOf(win));
                    data.put("loss", String.valueOf(loss));
                    db.collection("users").document(firebaseUser.getUid())
                            .update(data);
                } else {
                    System.out.println("read document error");
                }
            }
        });

    }
}