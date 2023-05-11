package com.example.battlecards;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class Black_Jack extends AppCompatActivity {

    private Button btn_bet;
    private Button btn_hit;
    private Button btn_stand;
    private SeekBar sb_bet;
    private TextView txt_bet;
    private TextView txt_bet_now;
    private TextView txt_dealer_card_value;
    private TextView txt_player_bc;
    private TextView txt_player_card_value;
    private TextView txt_round;
    private Context mContext;


    // For the game
    Deck mainDeck;
    Deck playerDeck;
    Deck dealerDeck;
    boolean gameEnd;
    Handler handler = new Handler();
    boolean playerFinish;
    boolean playAgain;
    int PlayerBattleCoins;
    int playerBet;
    int round;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_jack);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        btn_bet = (Button) findViewById(R.id.btn_bet);
        btn_hit = (Button) findViewById(R.id.btn_hit);
        btn_stand = (Button) findViewById(R.id.btn_stand);
        sb_bet = (SeekBar) findViewById(R.id.sb_bet);
        txt_bet = (TextView) findViewById(R.id.txt_bet);
        txt_bet_now = (TextView) findViewById(R.id.txt_bet_now);
        txt_dealer_card_value = (TextView) findViewById(R.id.txt_dealer_card_value);
        txt_player_bc = (TextView) findViewById(R.id.txt_player_bc);
        txt_player_card_value = (TextView) findViewById(R.id.txt_player_card_value);
        txt_round = (TextView) findViewById(R.id.txt_round);
        mContext = Black_Jack.this;
        // Default starting BattleCoins is 1000 bc
        PlayerBattleCoins = 1000;
        round = 0;
        bindViews();

        initState();


        Log.d("CHKECK", "Welcome to Java Blackjack!");
        mainDeck = new Deck();
        mainDeck.addAllCards();
        Log.d("CHKECK","Before Shuffle.");
        Log.d("CHKECK",mainDeck.toString());
        mainDeck.shuffle();

        Log.d("CHKECK","After Shuffle.");
        Log.d("CHKECK",mainDeck.toString());

        playerDeck = new Deck();
        dealerDeck = new Deck();
        //nextAction(mainDeck, playerDeck, dealerDeck);
    }

    private void initState() {
        gameEnd = false;
        playerFinish = false;
        playAgain = true;
        playerBet=0;
        round += 1;
        txt_round.setText("Round: " + round + " out of 5");
        btn_bet.setVisibility(View.VISIBLE);
        btn_hit.setVisibility(View.INVISIBLE);
        btn_stand.setVisibility(View.INVISIBLE);
        txt_player_bc.setText("Player's Remaining BattleCoins: \n" + PlayerBattleCoins + " bc");
        txt_bet_now.setText("Player's bet: " + playerBet + " bc");
        sb_bet.setVisibility(View.VISIBLE);
        txt_bet.setVisibility(View.VISIBLE);
        txt_player_card_value.setVisibility(View.INVISIBLE);
        txt_dealer_card_value.setVisibility(View.INVISIBLE);
    }

    private void bindViews() {
        sb_bet.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txt_bet.setText("Your bet: $" + progress*100);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Toast.makeText(mContext, "Slide your bet!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(mContext, "Release SeekBar", Toast.LENGTH_SHORT).show();
            }
        });
        btn_bet.setOnClickListener(bet);
    }

    public View.OnClickListener bet = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            playerBet = sb_bet.getProgress()*100;
            if (PlayerBattleCoins >= playerBet) {
                PlayerBattleCoins = PlayerBattleCoins - playerBet;
            }
            else {
                playerBet = PlayerBattleCoins;
                PlayerBattleCoins = 0;
            }
            txt_player_bc.setText("Player's Remaining BattleCoins: \n" + PlayerBattleCoins + " bc");
            txt_bet_now.setText("Player's bet: " + playerBet + " bc");
            if (playerBet != 0)
            {
                btn_bet.setVisibility(View.INVISIBLE);
                txt_bet.setVisibility(View.INVISIBLE);
                sb_bet.setVisibility(View.INVISIBLE);
                startRound();
            }
            else {
                Toast.makeText(Black_Jack.this, "You cannot bet 0 BattleCoins!", Toast.LENGTH_SHORT).show();
            }

        }
    };

    void startRound() {
        dealerDeck.draw(mainDeck);
        dealerDeck.draw(mainDeck);
        playerDeck.draw(mainDeck);
        playerDeck.draw(mainDeck);
        String dealer_value = "" + dealerDeck.cardsTotalValue();
        txt_dealer_card_value.setText(dealer_value);
        txt_dealer_card_value.setVisibility(View.VISIBLE);
        String player_value = "" + playerDeck.cardsTotalValue();
        txt_player_card_value.setText(player_value);
        txt_player_card_value.setVisibility(View.VISIBLE);
        btn_hit.setVisibility(View.VISIBLE);
        btn_stand.setVisibility(View.VISIBLE);

        btn_hit.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                hit();
                check();
                Toast.makeText(Black_Jack.this, "You clicked 1!", Toast.LENGTH_SHORT).show();}
        });
        btn_stand.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                stand();
                check();
                Toast.makeText(Black_Jack.this, "You clicked 2!", Toast.LENGTH_SHORT).show();}
        });
    }

    void hit() {
        playerDeck.draw(mainDeck);
        String player_value = "" + playerDeck.cardsTotalValue();
        txt_player_card_value.setText(player_value);

        if (playerDeck.cardsTotalValue() < 21) {
            gameEnd = false;
            playerFinish = false;
        } else if (playerDeck.cardsTotalValue() > 21) {
            gameEnd = true;
            playerFinish = true;
            btn_hit.setVisibility(View.INVISIBLE);
            btn_stand.setVisibility(View.INVISIBLE);
        } else {
            playerFinish = true;
            btn_hit.setVisibility(View.INVISIBLE);
            btn_stand.setVisibility(View.INVISIBLE);
        }
    }

    void stand() {
        playerFinish = true;
        btn_hit.setVisibility(View.INVISIBLE);
        btn_stand.setVisibility(View.INVISIBLE);
    }

    void check() {
        if (playerFinish && !gameEnd) {
            computerTurn();
            update();
        }
        else if (playerFinish && gameEnd) {
            // If gameEnd = true means player has exceed 21 already
            update();
        }
    }

    void computerTurn() {
        while (dealerDeck.cardsTotalValue() < 17) {
            dealerDeck.draw(mainDeck);
            Toast.makeText(Black_Jack.this, "Dealer draw a card!", Toast.LENGTH_SHORT).show();
            String dealer_value = "" + dealerDeck.cardsTotalValue();
            txt_dealer_card_value.setText(dealer_value);
        }
        gameEnd = true;
    }

    void update() {
        if (playerDeck.cardsTotalValue() == dealerDeck.cardsTotalValue()
                && playerDeck.cardsTotalValue() <= 21) {
            Toast.makeText(Black_Jack.this, "Draw!", Toast.LENGTH_SHORT).show();
            PlayerBattleCoins = PlayerBattleCoins + playerBet;
        } else if (playerDeck.cardsTotalValue() <= 21 && dealerDeck.cardsTotalValue() <= 21
                && (playerDeck.cardsTotalValue() > dealerDeck.cardsTotalValue())) {
            Toast.makeText(Black_Jack.this, "You Win!, you gain " + playerBet + "BattleCoins!", Toast.LENGTH_SHORT).show();
            PlayerBattleCoins = PlayerBattleCoins + playerBet*2;
        } else if (playerDeck.cardsTotalValue() <= 21 && dealerDeck.cardsTotalValue() <= 21
                && (playerDeck.cardsTotalValue() < dealerDeck.cardsTotalValue())) {
            Toast.makeText(Black_Jack.this, "You Lose!, you loss " + playerBet + "BattleCoins!", Toast.LENGTH_SHORT).show();
            //PlayerBattleCoins = PlayerBattleCoins - playerBet;
        } else if (playerDeck.cardsTotalValue() <= 21 && dealerDeck.cardsTotalValue() > 21) {
            Toast.makeText(Black_Jack.this, "Dealer Bust and You Win, you gain " + playerBet + "BattleCoins!", Toast.LENGTH_SHORT).show();
            PlayerBattleCoins = PlayerBattleCoins + playerBet*2;
        } else if (playerDeck.cardsTotalValue() > 21 && dealerDeck.cardsTotalValue() <= 21) {
            Toast.makeText(Black_Jack.this, "You Bust and You Lose, you loss " + playerBet + "BattleCoins!", Toast.LENGTH_SHORT).show();
            //PlayerBattleCoins = PlayerBattleCoins - playerBet;
        }
        txt_player_bc.setText("Player's Remaining BattleCoins: \n" + PlayerBattleCoins + " bc");
        txt_bet_now.setText("Player's bet: " + playerBet + " bc");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                playAgain();
            }
        }, 3000);
    }

    void playAgain() {
        if (PlayerBattleCoins > 0 && round <= 5) {
            playerDeck.returnAllCards(mainDeck);
            dealerDeck.returnAllCards(mainDeck);
            initState();
        }
        else if (PlayerBattleCoins == 0) {

        }
    }

    //********************************************************************************************
    static void nextAction(Deck mainDeck, Deck playerDeck, Deck dealerDeck) {
        boolean gameEnd = false;
        boolean playerFinish = false;
        boolean playAgain = true;
        int PlayerBattleCoins = 1000;
        int playerBet=0;

        while (playAgain) {
            //Scanner userInput = new Scanner(System.in);
            Boolean check1= false;

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
                PlayerBattleCoins=PlayerBattleCoins+playerBet;
            } else if (playerDeck.cardsTotalValue() <= 21 && dealerDeck.cardsTotalValue() <= 21
                    && (playerDeck.cardsTotalValue() < dealerDeck.cardsTotalValue())) {
                System.out.println(" ");
                System.out.println("You Lose!");
                PlayerBattleCoins=PlayerBattleCoins-playerBet;
            } else if (playerDeck.cardsTotalValue() <= 21 && dealerDeck.cardsTotalValue() > 21) {
                System.out.println(" ");
                System.out.println("You Win!");
                PlayerBattleCoins=PlayerBattleCoins+playerBet;
            } else if (playerDeck.cardsTotalValue() > 21 && dealerDeck.cardsTotalValue() <= 21) {
                System.out.println(" ");
                System.out.println("Bust and Lose!");
                PlayerBattleCoins=PlayerBattleCoins-playerBet;
            } else if (playerDeck.cardsTotalValue() > 21 && dealerDeck.cardsTotalValue() > 21) {
                System.out.println(" ");
                System.out.println("Bust and Draw!");
            }

            System.out.println(" ");
            System.out.println("Your money:$" + PlayerBattleCoins);

            if (PlayerBattleCoins>0) {
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