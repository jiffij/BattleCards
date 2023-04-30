package com.example.battlecards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Scanner;

public class Black_Jack extends AppCompatActivity {

    private SeekBar sb_bet;
    private TextView txt_bet;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_jack);
        mContext = Black_Jack.this;
        bindViews();

        Log.d("CHKECK", "Welcome to Java Blackjack!");
        Deck mainDeck = new Deck();
        mainDeck.addAllCards();
        Log.d("CHKECK","Before Shuffle.");
        Log.d("CHKECK",mainDeck.toString());
        mainDeck.shuffle();

        Log.d("CHKECK","After Shuffle.");
        Log.d("CHKECK",mainDeck.toString());

        Deck playerDeck = new Deck();
        Deck dealerDeck = new Deck();
        //nextAction(mainDeck, playerDeck, dealerDeck);
    }

    private void bindViews() {
        sb_bet = (SeekBar) findViewById(R.id.sb_bet);
        txt_bet = (TextView) findViewById(R.id.txt_bet);
        sb_bet.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txt_bet.setText("Your bet: $" + progress*100);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Toast.makeText(mContext, "触碰SeekBar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(mContext, "放开SeekBar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //********************************************************************************************
    static void nextAction(Deck mainDeck, Deck playerDeck, Deck dealerDeck) {
        boolean gameEnd = false;
        boolean playerFinish = false;
        boolean playAgain = true;
        int playerMoney = 1000;
        int playerBet=0;

        while (playAgain) {
            //Scanner userInput = new Scanner(System.in);
            Boolean inputComplete1= false;

            while (!inputComplete1) {
                System.out.println(" ");
                System.out.print("Your money:$" + playerMoney + " " + "Place your bet:$");
                int playerBetTemp = 0;//userInput.nextInt();
                if (playerBetTemp < 0 || playerBetTemp > playerMoney) {
                    inputComplete1 = false;

                } else {
                    inputComplete1 = true;
                }
                playerBet = playerBetTemp;
            }

            dealerDeck.draw(mainDeck);
            dealerDeck.draw(mainDeck);
            System.out.println(" ");
            System.out.print("Dealer Cards: "+ dealerDeck.toString2());

            playerDeck.draw(mainDeck);
            playerDeck.draw(mainDeck);
            System.out.println(" ");
            System.out.print("Player Cards: "+ playerDeck.toString() + " Total: "+
                    playerDeck.cardsTotalValue());

            while (!gameEnd && !playerFinish) {
                System.out.println(" ");
                System.out.print("Would you like to Hit(1) or Stand(2)?");
                int playerAction = 0;//userInput.nextInt();

                if (playerAction == 1) {
                    playerDeck.draw(mainDeck);
                    System.out.println(" ");
                    System.out.print("Player Cards: " + playerDeck.toString() + " Total: " +
                            playerDeck.cardsTotalValue());

                    if (playerDeck.cardsTotalValue() < 21) {
                        gameEnd = false;
                        playerFinish = false;
                    } else if (playerDeck.cardsTotalValue() > 21) {
                        System.out.println(" ");
                        System.out.println("Bust! Game End!!");
                        gameEnd = true;
                        playerFinish = true;
                    } else {
                        playerFinish = true;
                    }
                } else if (playerAction == 2) {
                    playerFinish = true;
                } else {
                    System.out.print("Select Hit(1) or Stand(2) only!");
                    gameEnd = false;
                    playerFinish = false;
                }
            }

            while (dealerDeck.cardsTotalValue() < 17) {
                dealerDeck.draw(mainDeck);
            }
            System.out.println(" ");
            System.out.print("Dealer Cards: " + dealerDeck.toString() + " Total: " +
                    dealerDeck.cardsTotalValue());
            gameEnd = true;

            if (playerDeck.cardsTotalValue() == dealerDeck.cardsTotalValue()
                    && playerDeck.cardsTotalValue() <= 21) {
                System.out.println(" ");
                System.out.println("Draw!");
            } else if (playerDeck.cardsTotalValue() <= 21 && dealerDeck.cardsTotalValue() <= 21
                    && (playerDeck.cardsTotalValue() > dealerDeck.cardsTotalValue())) {
                System.out.println(" ");
                System.out.println("You Win!");
                playerMoney=playerMoney+playerBet;
            } else if (playerDeck.cardsTotalValue() <= 21 && dealerDeck.cardsTotalValue() <= 21
                    && (playerDeck.cardsTotalValue() < dealerDeck.cardsTotalValue())) {
                System.out.println(" ");
                System.out.println("You Lose!");
                playerMoney=playerMoney-playerBet;
            } else if (playerDeck.cardsTotalValue() <= 21 && dealerDeck.cardsTotalValue() > 21) {
                System.out.println(" ");
                System.out.println("You Win!");
                playerMoney=playerMoney+playerBet;
            } else if (playerDeck.cardsTotalValue() > 21 && dealerDeck.cardsTotalValue() <= 21) {
                System.out.println(" ");
                System.out.println("Bust and Lose!");
                playerMoney=playerMoney-playerBet;
            } else if (playerDeck.cardsTotalValue() > 21 && dealerDeck.cardsTotalValue() > 21) {
                System.out.println(" ");
                System.out.println("Bust and Draw!");
            }

            System.out.println(" ");
            System.out.println("Your money:$" + playerMoney);

            if (playerMoney>0) {
                Boolean inputComplete2 = false;
                while (!inputComplete2) {
                    System.out.println(" ");
                    System.out.print("Play again? Yes(1) or No(2):");
                    int playerAction = 0;//userInput.nextInt();

                    if (playerAction == 1) {
                        playAgain = true;
                        gameEnd = false;
                        playerFinish = false;
                        inputComplete2 = true;

                    } else if (playerAction == 2) {
                        playAgain = false;
                        System.out.println(" ");
                        System.out.println("Game End!");
                        inputComplete2 = true;

                    } else {
                        System.out.println(" ");
                        System.out.println("Please select Yes(1) or No(2) only!");
                        inputComplete2 = false;
                    }
                }
                playerDeck.returnAllCards(mainDeck);
                dealerDeck.returnAllCards(mainDeck);
            } else{
                playAgain = false;
                System.out.println(" ");
                System.out.println("No money! Game Over!");
            }
        }
    }
}