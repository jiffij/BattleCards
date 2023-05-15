package com.example.battlecards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePage extends AppCompatActivity {

    ImageView ivImage;
    TextView tvName;
    Button btLogout;
    FirebaseAuth firebaseAuth;
    GoogleSignInClient googleSignInClient;
    ListView scrollMenu;
    String[] Games = {"Black Jack", "Speed", "Queen of Spades", "Rank"};
//    Button test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Assign variable
        ivImage = findViewById(R.id.iv_image);
        tvName = findViewById(R.id.tv_name);
        btLogout = findViewById(R.id.bt_logout);
        scrollMenu = findViewById(R.id.list);
//        test = findViewById(R.id.button3);

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Games);
        WhiteTextAdapter adapter = new WhiteTextAdapter(this, Arrays.asList(Games));
        scrollMenu.setAdapter(adapter);

        scrollMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String word = (String) parent.getItemAtPosition(position);
                Toast.makeText(HomePage.this, "You clicked on " + word, Toast.LENGTH_SHORT).show();
                switch(word) {
                    case "Rank":
                        startActivity(new Intent(HomePage.this, Ranking.class));
                        break;
                    default:
                        Intent intent = new Intent(HomePage.this, GameModePage.class);
                        intent.putExtra("game", word);
                        startActivity(intent);
                        break;
                }
                //Intent intent = new Intent(HomePage.this, GameModePage.class);
                //intent.putExtra("game", word);
                //startActivity(intent);
            }
        });

        // Initialize firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize firebase user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        // Check condition
        if (firebaseUser != null) {
            // When firebase user is not equal to null set image on image view
            Glide.with(HomePage.this).load(firebaseUser.getPhotoUrl()).into(ivImage);
            // set name on text view
            String greeting = "Welcome, " + firebaseUser.getDisplayName();
            tvName.setText(greeting);
        }

        // Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(HomePage.this, GoogleSignInOptions.DEFAULT_SIGN_IN);

        btLogout.setOnClickListener(view -> {
            // Sign out from google
            googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    // Check condition
                    if (task.isSuccessful()) {
                        // When task is successful sign out from firebase
                        firebaseAuth.signOut();
                        // Display Toast
                        Toast.makeText(getApplicationContext(), "Logout successful", Toast.LENGTH_SHORT).show();
                        // Finish activity
                        finish();
                    }
                }
            });
        });

        //testing write to firebase realtime database
//        test.setOnClickListener(view->{
//
//            String[] cards = {"ca", "da", ""};
//            Realtime real = new Realtime("1");
//            real.write("2", Arrays.asList(cards));
//
//        });

    }
}