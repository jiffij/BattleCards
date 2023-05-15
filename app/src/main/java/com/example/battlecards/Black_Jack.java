package com.example.battlecards;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

public class Black_Jack extends AppCompatActivity {

    private Button btn_bet;
    private Button btn_hit;
    private Button btn_stand;
    private Button btn_start;
    private ImageView img_dealer_card_1;
    private ImageView img_dealer_card_2;
    private ImageView img_dealer_card_3;
    private ImageView img_dealer_card_4;
    private ImageView img_dealer_card_5;
    private ImageView img_player1card_1;
    private ImageView img_player1card_2;
    private ImageView img_player1card_3;
    private ImageView img_player1card_4;
    private ImageView img_player1card_5;
    private SeekBar sb_bet;
    private TextView txt_bet;
    private TextView txt_bet_now;
    private TextView txt_bet_now2;
    private TextView txt_dealer_card_value;
    private TextView txt_message;
    private TextView txt_player_bc;
    private TextView txt_player_bc2;
    private TextView txt_player1_card_value;
    private TextView txt_mul_player1_card_value;
    private TextView txt_mul_player2_card_value;
    private TextView txt_round;
    private TextView txt_instruction;
    private Context mContext;

    // For the game
    Deck mainDeck;
    Deck player1Deck;
    Deck player2Deck;
    Deck dealerDeck;
    boolean gameEnd;
    boolean player1gameEnd;
    boolean player2gameEnd;
    boolean player1Finish;
    boolean player2Finish;
    Handler handler = new Handler();
    int currentPlayer;
    int thisPlayer;
    int numOfPlayer;
    int Player1BattleCoins;
    int Player2BattleCoins;
    int player1Bet;
    int player2Bet;
    int round;
    List<String> cardName;

    // For multiplayer
    Realtime real;
    String mode;
    String room;

    // For sound effect
    private SoundPlayer sound;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_jack);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // For music
        sound = new SoundPlayer(this);
        btn_bet = (Button) findViewById(R.id.btn_bet);
        btn_hit = (Button) findViewById(R.id.btn_hit);
        btn_stand = (Button) findViewById(R.id.btn_stand);
        btn_start = (Button) findViewById(R.id.btn_start);
        img_dealer_card_1 = (ImageView) findViewById(R.id.img_dealer_card_1);
        img_dealer_card_2 = (ImageView) findViewById(R.id.img_dealer_card_2);
        img_dealer_card_3 = (ImageView) findViewById(R.id.img_dealer_card_3);
        img_dealer_card_4 = (ImageView) findViewById(R.id.img_dealer_card_4);
        img_dealer_card_5 = (ImageView) findViewById(R.id.img_dealer_card_5);
        img_player1card_1 = (ImageView) findViewById(R.id.img_player1card_1);
        img_player1card_2 = (ImageView) findViewById(R.id.img_player1card_2);
        img_player1card_3 = (ImageView) findViewById(R.id.img_player1card_3);
        img_player1card_4 = (ImageView) findViewById(R.id.img_player1card_4);
        img_player1card_5 = (ImageView) findViewById(R.id.img_player1card_5);
        sb_bet = (SeekBar) findViewById(R.id.sb_bet);
        txt_bet = (TextView) findViewById(R.id.txt_bet);
        txt_bet_now = (TextView) findViewById(R.id.txt_bet_now);
        txt_bet_now2 = (TextView) findViewById(R.id.txt_bet_now2);
        txt_message = (TextView) findViewById(R.id.txt_message);
        txt_dealer_card_value = (TextView) findViewById(R.id.txt_dealer_card_value);
        txt_player_bc = (TextView) findViewById(R.id.txt_player_bc);
        txt_player_bc2 = (TextView) findViewById(R.id.txt_player_bc2);
        txt_player1_card_value = (TextView) findViewById(R.id.txt_player1_card_value);
        txt_mul_player1_card_value = (TextView) findViewById(R.id.txt_mul_player1_card_value);
        txt_mul_player2_card_value = (TextView) findViewById(R.id.txt_mul_player2_card_value);
        txt_round = (TextView) findViewById(R.id.txt_round);
        txt_instruction = (TextView) findViewById(R.id.txt_instruction);
        mContext = Black_Jack.this;

        // Todo: Assign player into player number and change the number of player
        // player 1 is the first player and the player in solo mode as well
        Intent intent = getIntent();
        thisPlayer = Integer.parseInt(intent.getStringExtra("player"));
        numOfPlayer = Integer.parseInt(intent.getStringExtra("numOfPlayer"));
        mode = intent.getStringExtra("mode");
        //room = mode.equals("multi")? intent.getStringExtra("room"): null;
        room = "999";
        if (room != null) {
            real = new Realtime(room);
            //addListener(real);
        }
        //thisPlayer = 1;
        //numOfPlayer = 1;

        // Default starting BattleCoins is 1000 bc
        Player1BattleCoins = 1000;
        Player2BattleCoins = 1000;
        round = 0;
        bindViews();


        mainDeck = new Deck();
        mainDeck.addAllCards();
        mainDeck.shuffle();
        // Toast.makeText(mContext, "Main deck number of cards:" + mainDeck.numOfCards, Toast.LENGTH_SHORT).show();

        player1Deck = new Deck();
        player2Deck = new Deck();
        dealerDeck = new Deck();

        instruction();
    }

    private void initState() {
        currentPlayer = 1;
        player1gameEnd = false;
        player2gameEnd = false;
        player1Finish = false;
        player2Finish = false;
        player1Bet=0;
        player2Bet=0;
        round += 1;
        txt_round.setText("Round: " + round + " out of 5");
        btn_bet.setVisibility(View.VISIBLE);
        btn_hit.setVisibility(View.INVISIBLE);
        btn_stand.setVisibility(View.INVISIBLE);
        img_dealer_card_1.setVisibility(View.INVISIBLE);
        img_dealer_card_2.setVisibility(View.INVISIBLE);
        img_dealer_card_3.setVisibility(View.INVISIBLE);
        img_dealer_card_4.setVisibility(View.INVISIBLE);
        img_dealer_card_5.setVisibility(View.INVISIBLE);
        img_player1card_1.setVisibility(View.INVISIBLE);
        img_player1card_2.setVisibility(View.INVISIBLE);
        img_player1card_3.setVisibility(View.INVISIBLE);
        img_player1card_4.setVisibility(View.INVISIBLE);
        img_player1card_5.setVisibility(View.INVISIBLE);
        if (numOfPlayer == 1) {
            txt_player_bc2.setVisibility(View.INVISIBLE);
            txt_bet_now2.setVisibility(View.INVISIBLE);
        }
        switch (thisPlayer) {
            case 1:
                txt_player_bc.setText("Player's Remaining BattleCoins: \n" + Player1BattleCoins + " bc");
                txt_bet_now.setText("Player's bet: " + player1Bet + " bc");
                break;
            case 2:
                txt_player_bc2.setText("Player's Remaining BattleCoins: \n" + Player2BattleCoins + " bc");
                txt_bet_now2.setText("Player's bet: " + player2Bet + " bc");
            default:
        }
        sb_bet.setVisibility(View.VISIBLE);
        txt_bet.setVisibility(View.VISIBLE);
        txt_message.setVisibility(View.INVISIBLE);
        txt_player1_card_value.setVisibility(View.INVISIBLE);
        txt_mul_player1_card_value.setVisibility(View.INVISIBLE);
        txt_mul_player2_card_value.setVisibility(View.INVISIBLE);
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
                //Toast.makeText(mContext, "Slide your bet!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Toast.makeText(mContext, "Release SeekBar", Toast.LENGTH_SHORT).show();
            }
        });
        btn_bet.setOnClickListener(bet);
    }

    public View.OnClickListener bet = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (thisPlayer) {
                case 1:
                    player1Bet = sb_bet.getProgress()*100;
                    if (Player1BattleCoins >= player1Bet) {
                        Player1BattleCoins = Player1BattleCoins - player1Bet;
                    }
                    else {
                        player1Bet = Player1BattleCoins;
                        Player1BattleCoins = 0;
                    }
                    txt_player_bc.setText("Player's Remaining BattleCoins: \n" + Player1BattleCoins + " bc");
                    txt_bet_now.setText("Player's bet: " + player1Bet + " bc");
                    if (numOfPlayer == 2) {
                        real.write("player1Bet", player1Bet);
                    }
                    if (player1Bet != 0)
                    {
                        btn_bet.setVisibility(View.INVISIBLE);
                        txt_bet.setVisibility(View.INVISIBLE);
                        sb_bet.setVisibility(View.INVISIBLE);
                        startRound();
                    }
                    else {
                        txt_message.setText("You cannot bet 0 BattleCoins!");
                        txt_message.setVisibility(View.VISIBLE);
                        handler.postDelayed(() -> txt_message.setVisibility(View.INVISIBLE), 3000);
                    }
                    break;
                case 2:
                    player2Bet = sb_bet.getProgress()*100;
                    if (Player2BattleCoins >= player2Bet) {
                        Player2BattleCoins = Player2BattleCoins - player2Bet;
                    }
                    else {
                        player2Bet = Player2BattleCoins;
                        Player2BattleCoins = 0;
                    }
                    txt_player_bc.setText("Player's Remaining BattleCoins: \n" + Player2BattleCoins + " bc");
                    txt_bet_now.setText("Player's bet: " + player2Bet + " bc");
                    if (numOfPlayer == 2) {
                        real.write("player2Bet", player2Bet);
                    }
                    if (player2Bet != 0)
                    {
                        btn_bet.setVisibility(View.INVISIBLE);
                        txt_bet.setVisibility(View.INVISIBLE);
                        sb_bet.setVisibility(View.INVISIBLE);
                        startRound();
                    }
                    else {
                        txt_message.setText("You cannot bet 0 BattleCoins!");
                        txt_message.setVisibility(View.VISIBLE);
                        handler.postDelayed(() -> txt_message.setVisibility(View.INVISIBLE), 3000);
                    }
                    break;
                default:
            }
        }
    };

    void startRound() {
        dealerDeck.draw(mainDeck);
        dealerDeck.draw(mainDeck);
        player1Deck.draw(mainDeck);
        player1Deck.draw(mainDeck);
        img_dealer_card_1.setImageDrawable(getCardImage(dealerDeck, 0));
        img_dealer_card_1.setVisibility(View.VISIBLE);
        if (numOfPlayer == 1 && thisPlayer == 1) {
            img_player1card_1.setImageDrawable(getCardImage(player1Deck, 0));
            img_player1card_2.setImageDrawable(getCardImage(player1Deck, 1));
            img_player1card_1.setVisibility(View.VISIBLE);
            img_player1card_2.setVisibility(View.VISIBLE);
        }
        if (numOfPlayer == 2) {
            player2Deck.draw(mainDeck);
            player2Deck.draw(mainDeck);
        }
        // Todo player 2 need to get value from realtime server
        String dealer_value = "" + dealerDeck.cardsTotalValue();
        txt_dealer_card_value.setText(dealer_value);
//        txt_dealer_card_value.setVisibility(View.VISIBLE);
        String player1_value = "" + player1Deck.cardsTotalValue();
        if (numOfPlayer == 1) {
            txt_player1_card_value.setText(player1_value);
            txt_player1_card_value.setVisibility(View.VISIBLE);
        }
        if (numOfPlayer == 2) {
            txt_mul_player1_card_value.setText(player1_value);
            txt_mul_player1_card_value.setVisibility(View.VISIBLE);
            String player2_value = "" + player2Deck.cardsTotalValue();
            txt_mul_player2_card_value.setText(player2_value);
            txt_mul_player2_card_value.setVisibility(View.VISIBLE);
        }
        if (thisPlayer == currentPlayer) {
            btn_hit.setVisibility(View.VISIBLE);
            btn_stand.setVisibility(View.VISIBLE);
        }

        btn_hit.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                hit(currentPlayer);
                check();
                //multiPlayerCheck();
            }
        });
        btn_stand.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                stand(currentPlayer);
                check();
                //multiPlayerCheck();
            }
        });
    }

    void hit(int player) {
        String player_value;
        switch (player) {
            case 1:
                player1Deck.draw(mainDeck);
                player_value = "" + player1Deck.cardsTotalValue();
                txt_player1_card_value.setText(player_value);
                if (numOfPlayer == 1) {
                    switch (player1Deck.numOfCards) {
                        case 3:
                            img_player1card_3.setImageDrawable(getCardImage(player1Deck, 2));
                            img_player1card_3.setVisibility(View.VISIBLE);
                            break;
                        case 4:
                            img_player1card_4.setImageDrawable(getCardImage(player1Deck, 3));
                            img_player1card_4.setVisibility(View.VISIBLE);
                            break;
                        case 5:
                            img_player1card_5.setImageDrawable(getCardImage(player1Deck, 4));
                            img_player1card_5.setVisibility(View.VISIBLE);
                            break;
                        default:
                            break;
                    }
                } else if (numOfPlayer == 2) {
                    txt_mul_player1_card_value.setText(player_value);
                }
                if (player1Deck.cardsTotalValue() <= 21 && player1Deck.numOfCards >= 5) {
                    player1gameEnd = true;
                    player1Finish = true;
                    btn_hit.setVisibility(View.INVISIBLE);
                    btn_stand.setVisibility(View.INVISIBLE);
                } else if (player1Deck.cardsTotalValue() < 21) {
                    player1gameEnd = false;
                    player1Finish = false;
                } else if (player1Deck.cardsTotalValue() > 21) {
                    player1gameEnd = true;
                    player1Finish = true;
                    btn_hit.setVisibility(View.INVISIBLE);
                    btn_stand.setVisibility(View.INVISIBLE);
                } else {
                    player1Finish = true;
                    btn_hit.setVisibility(View.INVISIBLE);
                    btn_stand.setVisibility(View.INVISIBLE);
                }
                break;
            case 2:
                player2Deck.draw(mainDeck);
                player_value = "" + player2Deck.cardsTotalValue();
                txt_mul_player2_card_value.setText(player_value);

                if (player2Deck.cardsTotalValue() < 21 && player2Deck.numOfCards >= 5) {
                    player2gameEnd = true;
                    player2Finish = true;
                    btn_hit.setVisibility(View.INVISIBLE);
                    btn_stand.setVisibility(View.INVISIBLE);
                }
                if (player2Deck.cardsTotalValue() < 21) {
                    player2gameEnd = false;
                    player2Finish = false;
                } else if (player2Deck.cardsTotalValue() > 21) {
                    player2gameEnd = true;
                    player2Finish = true;
                    btn_hit.setVisibility(View.INVISIBLE);
                    btn_stand.setVisibility(View.INVISIBLE);
                } else {
                    player2Finish = true;
                    btn_hit.setVisibility(View.INVISIBLE);
                    btn_stand.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }

    void stand(int player) {
        switch (player) {
            case 1:
                player1Finish = true;
                break;
            case 2:
                player2Finish = true;
                break;
            default:
        }
        btn_hit.setVisibility(View.INVISIBLE);
        btn_stand.setVisibility(View.INVISIBLE);
    }

    void check() {
        switch (numOfPlayer) {
            case 1:
                if (player1Finish && !player1gameEnd) {
                    computerTurn();
                    update();
                }
                else if (player1Finish && player1gameEnd) {
                    txt_dealer_card_value.setVisibility(View.VISIBLE);
                    img_dealer_card_2.setImageDrawable(getCardImage(dealerDeck, 1));
                    img_dealer_card_2.setVisibility(View.VISIBLE);
                    update();
                }
                break;
            case 2:
                // player?gameEnd = true when they exceed 21
                if (player1gameEnd && player2gameEnd) {
                    gameEnd = true;
                }
                if (player1Finish) {
                    // player 2 turn
                    currentPlayer += 1;
                    thisPlayer = currentPlayer;
                    real.write("currentPlayer", 2);
                }
                if (player1Finish && player2Finish && !gameEnd) {
                    computerTurn();
                    update();
                }
                else if (player1Finish && player2Finish && gameEnd) {
                    // If gameEnd = true means all player has exceed 21
                    update();
                }
                break;
            default:
        }
    }

    void computerTurn() {
        img_dealer_card_2.setImageDrawable(getCardImage(dealerDeck, 1));
        img_dealer_card_2.setVisibility(View.VISIBLE);
        while (dealerDeck.cardsTotalValue() < 17) {
            dealerDeck.draw(mainDeck);
            //handler.postDelayed(() -> {dealerDeck.draw(mainDeck)}, 1000);
            switch (dealerDeck.numOfCards) {
                case 3:
                    img_dealer_card_3.setImageDrawable(getCardImage(dealerDeck, 2));
                    img_dealer_card_3.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    img_dealer_card_4.setImageDrawable(getCardImage(dealerDeck, 3));
                    img_dealer_card_4.setVisibility(View.VISIBLE);
                    break;
                case 5:
                    img_dealer_card_5.setImageDrawable(getCardImage(dealerDeck, 4));
                    img_dealer_card_5.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }

            String dealer_value = "" + dealerDeck.cardsTotalValue();
            txt_dealer_card_value.setText(dealer_value);
            txt_dealer_card_value.setVisibility(View.VISIBLE);
        }
        // gameEnd = true;
    }

    void update() {
        txt_dealer_card_value.setVisibility(View.VISIBLE);
        for (int i = 1; i <= numOfPlayer; i ++) {
            switch (i) {
                case 1:
                    if (player1Deck.cardsTotalValue() <= 21 && player1Deck.numOfCards >= 5) {
                        txt_message.setText("Owned 5 cards and You win!\nYou gain " + player1Bet + " BattleCoins!");
                        Player1BattleCoins = Player1BattleCoins + player1Bet*2;
                        sound.playCoinSound();
                    } else if (player1Deck.cardsTotalValue() == 21 && player1Deck.numOfCards == 2) {
                        txt_message.setText("Black Jack!\nYou gain 1.5*" + player1Bet + " BattleCoins!");
                        Player1BattleCoins = (int) (Player1BattleCoins + player1Bet*2.5);
                        sound.playCoinSound();
                    } else if (player1Deck.cardsTotalValue() == dealerDeck.cardsTotalValue()
                            && player1Deck.cardsTotalValue() <= 21) {
                        txt_message.setText("Draw!\nYou didn't gain or lose any BattleCoins");
                        Player1BattleCoins = Player1BattleCoins + player1Bet;
                    } else if (player1Deck.cardsTotalValue() <= 21 && dealerDeck.cardsTotalValue() <= 21
                            && (player1Deck.cardsTotalValue() > dealerDeck.cardsTotalValue())) {
                        txt_message.setText("You Win!\nYou gain " + player1Bet + " BattleCoins!");
                        Player1BattleCoins = Player1BattleCoins + player1Bet*2;
                        sound.playCoinSound();
                    } else if (player1Deck.cardsTotalValue() <= 21 && dealerDeck.cardsTotalValue() <= 21
                            && (player1Deck.cardsTotalValue() < dealerDeck.cardsTotalValue())) {
                        txt_message.setText("You Lose!\nYou loss " + player1Bet + " BattleCoins!");
                        //PlayerBattleCoins = PlayerBattleCoins - playerBet;
                    } else if (player1Deck.cardsTotalValue() <= 21 && dealerDeck.cardsTotalValue() > 21) {
                        txt_message.setText("Dealer Bust and You Win!\nYou gain " + player1Bet + " BattleCoins!");
                        Player1BattleCoins = Player1BattleCoins + player1Bet*2;
                        sound.playCoinSound();
                    } else if (player1Deck.cardsTotalValue() > 21 && dealerDeck.cardsTotalValue() <= 21) {
                        txt_message.setText("You Bust and You Lose!\nYou loss " + player1Bet + " BattleCoins!");
                        //PlayerBattleCoins = PlayerBattleCoins - playerBet;
                    }
                    if (thisPlayer == i) {
                        txt_message.setVisibility(View.VISIBLE);
                        txt_player_bc.setText("Player's Remaining BattleCoins: \n" + Player1BattleCoins + " bc");
                        txt_bet_now.setText("Player's bet: " + player1Bet + " bc");
                    }
                    break;
                case 2:
                    if (player2Deck.cardsTotalValue() <= 21 && player2Deck.numOfCards >= 5) {
                        txt_message.setText("Owned 5 cards and You win!\nYou gain " + player2Bet + " BattleCoins!");
                        Player2BattleCoins = Player2BattleCoins + player2Bet*2;
                        sound.playCoinSound();
                    } else if (player2Deck.cardsTotalValue() == 21 && player2Deck.numOfCards == 2) {
                        txt_message.setText("Black Jack!\nYou gain 1.5*" + player2Bet + " BattleCoins!");
                        Player2BattleCoins = (int) (Player2BattleCoins + player2Bet*2.5);
                        sound.playCoinSound();
                    } else if (player2Deck.cardsTotalValue() == dealerDeck.cardsTotalValue()
                            && player2Deck.cardsTotalValue() <= 21) {
                        txt_message.setText("Draw!\nYou didn't gain or lose any BattleCoins");
                        Player2BattleCoins = Player2BattleCoins + player2Bet;
                    } else if (player2Deck.cardsTotalValue() <= 21 && dealerDeck.cardsTotalValue() <= 21
                            && (player2Deck.cardsTotalValue() > dealerDeck.cardsTotalValue())) {
                        txt_message.setText("You Win!\nYou gain " + player2Bet + " BattleCoins!");
                        Player2BattleCoins = Player2BattleCoins + player2Bet*2;
                        sound.playCoinSound();
                    } else if (player2Deck.cardsTotalValue() <= 21 && dealerDeck.cardsTotalValue() <= 21
                            && (player2Deck.cardsTotalValue() < dealerDeck.cardsTotalValue())) {
                        txt_message.setText("You Lose!\nYou loss " + player2Bet + " BattleCoins!");
                        //PlayerBattleCoins = PlayerBattleCoins - playerBet;
                    } else if (player2Deck.cardsTotalValue() <= 21 && dealerDeck.cardsTotalValue() > 21) {
                        txt_message.setText("Dealer Bust and You Win!\nYou gain " + player2Bet + " BattleCoins!");
                        Player2BattleCoins = Player2BattleCoins + player2Bet*2;
                    } else if (player2Deck.cardsTotalValue() > 21 && dealerDeck.cardsTotalValue() <= 21) {
                        txt_message.setText("You Bust and You Lose!\nYou loss " + player2Bet + " BattleCoins!");
                        //PlayerBattleCoins = PlayerBattleCoins - playerBet;
                    }
                    if (thisPlayer == i) {
                        txt_message.setVisibility(View.VISIBLE);
                        txt_player_bc.setText("Player's Remaining BattleCoins: \n" + Player2BattleCoins + " bc");
                        txt_bet_now.setText("Player's bet: " + player2Bet + " bc");
                    }
                    break;
                default:
            }
        }
        handler.postDelayed(() -> playAgain(), 3000);
    }

    void playAgain() {
        player1Deck.returnAllCards(mainDeck);
        if (numOfPlayer == 2) {
            player2Deck.returnAllCards(mainDeck);
        }
        real.write("dealerDeck", dealerDeck.getAllCardName());
        dealerDeck.returnAllCards(mainDeck);
        switch (numOfPlayer) {
            case 1:
                if (Player1BattleCoins > 0 && Player1BattleCoins < 2500  && round < 5) {
                    initState();
                } else if (Player1BattleCoins >= 2500) {
                    txt_message.setText("Congratulations! You have win the game by exceeding 2500 BattleCoins!");
                    txt_message.setVisibility(View.VISIBLE);
                    updateRealtimeServer_1();
                    handler.postDelayed(() -> displayResult("win"), 3000);

                } else if (Player1BattleCoins == 0) {
                    txt_message.setText("Oops! You have lose the game by losing all BattleCoins!");
                    txt_message.setVisibility(View.VISIBLE);
                    updateRealtimeServer_1();
                    handler.postDelayed(() -> displayResult("lose"), 3000);
                } else {
                    txt_message.setText("Oops! You have lose the game by unable to reach 2500 BattleCoins in 5 rounds!");
                    txt_message.setVisibility(View.VISIBLE);
                    updateRealtimeServer_1();
                    handler.postDelayed(() -> displayResult("lose"), 3000);
                }
                break;
            case 2:
                if (Player1BattleCoins > 0 && Player2BattleCoins > 0 && round < 5) {
                    initState();
                } else if (Player1BattleCoins > Player2BattleCoins) {
                    switch (thisPlayer) {
                        case 1:
                            txt_message.setText("Congratulations! You own" + Player1BattleCoins + " bc and win the game!");
                            break;
                        case 2:
                            txt_message.setText("Oops! Player 1 own" + Player1BattleCoins + " bc and have win the game! Try hard next time!");
                        default:
                    }
                    txt_message.setVisibility(View.VISIBLE);
                    // Todo: Navigate to result page
                } else if (Player2BattleCoins > Player1BattleCoins) {
                    switch (thisPlayer) {
                        case 1:
                            txt_message.setText("Oops! Player 2 own" + Player2BattleCoins + " bc and have win the game! Try hard next time!");
                            break;
                        case 2:
                            txt_message.setText("Congratulations! You own" + Player2BattleCoins + " bc and win the game!");
                        default:
                    }
                    txt_message.setVisibility(View.VISIBLE);
                    // Todo: Navigate to result page
                } else if (Player1BattleCoins == Player2BattleCoins) {
                    txt_message.setText("Both players own the same amount of bc, It is a Tie game!");
                    txt_message.setVisibility(View.VISIBLE);
                    // Todo: Navigate to result page
                } else {
                    txt_message.setText("Both players own 0 bc, It is a Tie game! Come on guys...");
                    txt_message.setVisibility(View.VISIBLE);
                    // Todo: Navigate to result page
                }
                break;
            default:
        }
    }
    Drawable getCardImage(Deck deck, int idx) {
        cardName = deck.getAllCardName();
        String uri = "@drawable/"+ cardName.get(idx);  // where my resource (without the extension) is the file
        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
        Drawable res = getResources().getDrawable(imageResource);
        return res;
    }

    void multiPlayerCheck() {
        if (thisPlayer == currentPlayer) {
            btn_hit.setVisibility(View.VISIBLE);
            btn_stand.setVisibility(View.VISIBLE);
        }
    }

    void updateRealtimeServer_1() {
        real.write("player1Bet", player1Bet);
        real.write("numOfPlayer", numOfPlayer);
        real.write("currentPlayer", currentPlayer);
        real.write("Player1BattleCoins", Player1BattleCoins);
        real.write("round", round);
    }

    //********************************************************************************************
    void addListener(Realtime real) {
        real.addListener((snapshot) -> {
            Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
            String Player1BattleCoins = (String) map.get("Player1BattleCoins");
            String Player2BattleCoins = (String) map.get("Player2BattleCoins");
            String player1Bet = (String) map.get("player1Bet");
            String player2Bet = (String) map.get("player2Bet");
            String round = (String) map.get("round");
            String currentPlayer = (String) map.get("currentPlayer");
            List dealerDeck = (List) map.get("dealerDeck");

            if (round != null) {
                txt_round.setText("Round: " + round + " out of 5");
            }
            if (Integer.parseInt(player1Bet) != 0) {
                this.player2Bet = Integer.parseInt(player1Bet);
            }
            if (Integer.parseInt(player2Bet) != 0) {
                this.player2Bet = Integer.parseInt(player2Bet);
            }
            if (Integer.parseInt(currentPlayer) == 2) {
                this.currentPlayer = 2;
            }
            if (Integer.parseInt(Player1BattleCoins) != 0) {
                this.Player1BattleCoins = Integer.parseInt(Player1BattleCoins);
            }
            if (Integer.parseInt(Player2BattleCoins) != 0) {
                this.Player2BattleCoins = Integer.parseInt(Player2BattleCoins);
            }
        });
    }

    void displayResult(String result) {
        Intent intent = new Intent(Black_Jack.this, Result.class);
        intent.putExtra("result", result);
        startActivity(intent);
    }

    void instruction() {
        String instruction = "Black Jack Instruction\nThe object of the game of Blackjack is to have your cards total 21 or as near to 21 as possible without going over 21.In Blackjack, everyone plays against the dealer (the house). Your goal is to draw cards with a value as close to 21 as possible without going over. A hand that goes over 21 is a bust or break . You are playing against the dealer. Each player only has to beat the dealer's hand to Win. You do this in one of two ways: 1 Have a card total value greater than that of the dealer and not \"bust\". 2 Win \"by default” if the dealer \"busts.\n" +
                "When you receive your first two cards, you may either \"Stand\", \"Hit\". When you \"Stand\" it means you feel you are close enough to 21 and no longer wish any additional cards. On the other hand you may wish to receive another card or \"Hit\".\n" +
                "All winning wagers are paid 1 to 1 with the exception of Blackjack which is paid at 1 to 1.5\n\n" +
                "Your goal:\n" +
                "Collect 2500 or above BattleCoins with 5 rounds.\n" +
                "How to Play:\n" +
                "1. Slide your bet within 0 to 1,000 BattleCoins (100 BC per bet), then press bet to start the game.\n" +
                "2. Choose \"Stand\" or \"Hit\" by pressing relative button.\n" +
                "3. if you are ready, press the \"Start\" button, trust your luck and have FUN!";
        txt_instruction.setText(instruction);
        txt_instruction.setVisibility(View.VISIBLE);
        btn_bet.setVisibility(View.INVISIBLE);
        btn_stand.setVisibility(View.INVISIBLE);
        btn_hit.setVisibility(View.INVISIBLE);
        btn_start.setVisibility(View.VISIBLE);

        btn_start.setOnClickListener(v -> {
            txt_instruction.setVisibility(View.INVISIBLE);
            btn_start.setVisibility(View.INVISIBLE);
            initState();
        });
    }
}