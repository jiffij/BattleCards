package com.example.battlecards;

import android.content.Context;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class GPS extends Thread {

    private Realtime real;
    private boolean keep = true;
    private String game;
    private int id;
    private double latitude = -1;
    private double longitude = -1;
    private Context appContext;

    public GPS(String game, Context applicationContext){

        this.game = game;
        this.appContext = applicationContext;
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        id = hashString(firebaseAuth.getCurrentUser().getUid());
        real = new Realtime("GPS/"+game);
        real.addListener((snapshot)->{
            Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Map value = (Map) entry.getValue();
                if(key.equals(String.valueOf(id))) continue;
                double lat2 = (double) value.get("latitude");
                double lon2 = (double) value.get("longitude");
                double dist = distance(latitude, longitude, lat2, lon2);
                if(dist < 2){
                    int roomId = id > Integer.parseInt(key)? id: Integer.parseInt(key);
                    real.removeItem(String.valueOf(id));
                    if(roomId == id){
                        Realtime one = new Realtime(String.valueOf(roomId));
                        one.write("game", this.game);
                    }
                    if(game.equals("Speed")) {
                        Intent intent1 = new Intent(this.appContext, SpeedLauncher.class);
                        intent1.putExtra("mode", "multi");
                        intent1.putExtra("player", roomId == id? "A": "B");
                        intent1.putExtra("room", Integer.toString(roomId));
                        this.appContext.startActivity(intent1);
                    }else{ //TODO black jack

                    }
                    break;
                }
            }
        });
    }

    public static final double EARTH_RADIUS = 6371.0; // Earth's radius in kilometers

    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS * c;
        return distance;
    }

    @Override
    public void run() {
        super.run();
        while(keep){
            if(latitude != -1 && longitude != -1) {
//                Map map = new HashMap();
                Map subMap = new HashMap();
                subMap.put("latitude", this.latitude);
                subMap.put("longitude", this.longitude);
//                map.put(id, subMap);
                real.write(String.valueOf(id), subMap);
            }
            try {
                Thread.sleep(5000); // Sleep for 1 second
            } catch (InterruptedException e) {
                // Handle the exception
            }
        }
    }

    public void end(){
        this.keep = false;
        real.removeItem(String.valueOf(id));
    }

    public void update(double la, double lo){
        this.latitude = la;
        this.longitude = lo;
    }

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
}
