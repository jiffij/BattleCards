package com.example.battlecards;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Realtime {
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://battlecards-527c9-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference myRef;
    ValueEventListener listener;

    Realtime(String ref){
        myRef = database.getReference(ref);
    }

//    Object readOnce(String path){
//        myRef.addListenerForSingleValueEvent(new ValueEventListener(){
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot){
//                String value = dataSnapshot.child("a1").getValue(String.class); //This is a1
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//
//            }
//
//        });
//    }
    void write(String path, int value){
        myRef.child(path).setValue(value, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error != null) {
                    Log.d("TAG", "Write failed: " + error.getMessage());
                } else {
                    Log.d("TAG", "Write succeeded");
                }
            }
        });
    }

    void write(String path, String value){
        myRef.child(path).setValue(value, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error != null) {
                    Log.d("TAG", "Write failed: " + error.getMessage());
                } else {
                    Log.d("TAG", "Write succeeded");
                }
            }
        });
    }

    void write(String path, List value){
        myRef.child(path).setValue(value, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error != null) {
                    Log.d("TAG", "Write failed: " + error.getMessage());
                } else {
                    Log.d("TAG", "Write succeeded");
                }
            }
        });
    }

    void write(String path, Map value){
        myRef.child(path).setValue(value, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error != null) {
                    Log.d("TAG", "Write failed: " + error.getMessage());
                } else {
                    Log.d("TAG", "Write succeeded");
                }
            }
        });
    }

    void addListener(MyRunnable func){
        // Read from the database
        listener = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
                if (dataSnapshot.exists()) {func.run(dataSnapshot);}

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });
    }

    void removeItem(String path){
        myRef.child(path).removeValue();
    }

    void removeListener(){
        myRef.removeEventListener(listener);
    }

}
