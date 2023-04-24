package com.example.battlecards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

public class GameModePage extends AppCompatActivity {

    ImageView ivImage;
    TextView tvName;
    FirebaseAuth firebaseAuth;
    GoogleSignInClient googleSignInClient;
    ListView scrollMenu;
    String[] Games = {"AI", "Random", "Nearby rooms", "Join a Room", "Create a Room"};
    TextView gameTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_mode_page);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Assign variable
        ivImage = findViewById(R.id.iv_image);
        tvName = findViewById(R.id.tv_name);
        scrollMenu = findViewById(R.id.list);
        gameTitle = findViewById(R.id.game_title);


        Intent intent = getIntent();
        String game = intent.getStringExtra("game");
        gameTitle.setText(game);
        gameTitle.setTextColor(Color.WHITE);
        gameTitle.setTextSize(50);
        gameTitle.setGravity(Gravity.CENTER);

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Games);
        WhiteTextAdapter adapter = new WhiteTextAdapter(this, Arrays.asList(Games));
        scrollMenu.setAdapter(adapter);

        scrollMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String word = (String) parent.getItemAtPosition(position);
                Toast.makeText(GameModePage.this, "You clicked on " + word, Toast.LENGTH_SHORT).show();
            }
        });

        // Initialize firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize firebase user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        // Check condition
        if (firebaseUser != null) {
            // When firebase user is not equal to null set image on image view
            Glide.with(GameModePage.this).load(firebaseUser.getPhotoUrl()).into(ivImage);
            // set name on text view
            String greeting = "Welcome, " + firebaseUser.getDisplayName();
            tvName.setText(greeting);
        }

    }
}