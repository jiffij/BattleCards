//package com.example.battlecards;
//
//import android.content.Intent;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//public class SpeedHEnemy extends Thread implements SpeedEnemy{
//
//    SpeedDeck uiDeck;
//    Realtime real;
//    String room;
//    public SpeedHEnemy(SpeedDeck uiDeck, String room) {
//        this.uiDeck = uiDeck;
//        this.room = room;
//        real = new Realtime(room);
//        List list = new ArrayList();
//        uiDeck.speed.
//        real.write("B", list);
//
//    }
//
//    @Override
//    public void update() {
//        real.addListener((snapshot)->{
//            Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
//            Long val = (Long) map.get("players");
//            if(val.intValue() == 2){
//                System.out.println("start game");
//                if(game.equals("Speed")) {
//                    Intent intent1 = new Intent(OpenRoom.this, SpeedLauncher.class);
//                    intent1.putExtra("mode", "multi");
//                    intent1.putExtra("player", "A");
//                    startActivity(intent1);
//                }
//            }
//        });
//    }
//
//    @Override
//    public void run(){
//        update();
//    }
//}
