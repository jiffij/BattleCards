package com.example.battlecards;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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
    private TextView txt_message;
    private TextView txt_player_bc;
    private TextView txt_player1_card_value;
    private TextView txt_player2_card_value;
    private TextView txt_round;
    private Context mContext;


    // For the game
    Deck mainDeck;
    Deck player1Deck;
    Deck player2Deck;
    Deck dealerDeck;
    boolean gameEnd;
    boolean player1gameEnd;
    boolean player2gameEnd;
    Handler handler = new Handler();
    int currentPlayer;
    int thisPlayer;
    boolean player1Finish;
    boolean player2Finish;
    int numOfPlayer;
    int Player1BattleCoins;
    int Player2BattleCoins;
    int player1Bet;
    int player2Bet;
    int round;

    @SuppressLint("MissingInflatedId")
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
        txt_message = (TextView) findViewById(R.id.txt_message);
        txt_dealer_card_value = (TextView) findViewById(R.id.txt_dealer_card_value);
        txt_player_bc = (TextView) findViewById(R.id.txt_player_bc);
        txt_player1_card_value = (TextView) findViewById(R.id.txt_player1_card_value);
        txt_player2_card_value = (TextView) findViewById(R.id.txt_player2_card_value);
        txt_round = (TextView) findViewById(R.id.txt_round);
        mContext = Black_Jack.this;

        // Todo: Assign player into player number and change the number of player
        // player 1 is the first player and the player in solo mode
        thisPlayer = 1;
        numOfPlayer = 1;

        // Default starting BattleCoins is 1000 bc
        Player1BattleCoins = 1000;
        Player2BattleCoins = 1000;
        round = 0;
        bindViews();

        initState();

        mainDeck = new Deck();
        mainDeck.addAllCards();
        mainDeck.shuffle();
        // Toast.makeText(mContext, "Main deck number of cards:" + mainDeck.numOfCards, Toast.LENGTH_SHORT).show();

        player1Deck = new Deck();
        player2Deck = new Deck();
        dealerDeck = new Deck();
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
        switch (thisPlayer) {
            case 1:
                txt_player_bc.setText("Player's Remaining BattleCoins: \n" + Player1BattleCoins + " bc");
                txt_bet_now.setText("Player's bet: " + player1Bet + " bc");
                break;
            case 2:
                txt_player_bc.setText("Player's Remaining BattleCoins: \n" + Player2BattleCoins + " bc");
                txt_bet_now.setText("Player's bet: " + player2Bet + " bc");
            default:
        }
        sb_bet.setVisibility(View.VISIBLE);
        txt_bet.setVisibility(View.VISIBLE);
        txt_message.setVisibility(View.INVISIBLE);
        txt_player1_card_value.setVisibility(View.INVISIBLE);
        txt_player2_card_value.setVisibility(View.INVISIBLE);
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
                //Toast.makeText(mContext, "Release SeekBar", Toast.LENGTH_SHORT).show();
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
                    if (player2Bet != 0)
                    {
                        btn_bet.setVisibility(View.INVISIBLE);
                        txt_bet.setVisibility(View.INVISIBLE);
                        sb_bet.setVisibility(View.INVISIBLE);
                        // startRound(1);
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
        if (numOfPlayer == 2) {
            player2Deck.draw(mainDeck);
            player2Deck.draw(mainDeck);
        }
        String dealer_value = "" + dealerDeck.cardsTotalValue();
        txt_dealer_card_value.setText(dealer_value);
        txt_dealer_card_value.setVisibility(View.VISIBLE);
        String player1_value = "" + player1Deck.cardsTotalValue();
        txt_player1_card_value.setText(player1_value);
        txt_player1_card_value.setVisibility(View.VISIBLE);
        if (numOfPlayer == 2) {
            String player2_value = "" + player2Deck.cardsTotalValue();
            txt_player2_card_value.setText(player2_value);
            txt_player2_card_value.setVisibility(View.VISIBLE);
        }
        if (thisPlayer == currentPlayer) {
            btn_hit.setVisibility(View.VISIBLE);
            btn_stand.setVisibility(View.VISIBLE);
        }

        btn_hit.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                hit(currentPlayer);
                check();
            }
        });
        btn_stand.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                stand(currentPlayer);
                check();
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

                if (player1Deck.cardsTotalValue() < 21) {
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
                txt_player2_card_value.setText(player_value);

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
                    // If gameEnd = true means all player has exceed 21
                    update();
                }
                break;
            case 2:
                // player?gameEnd = true when they exceed 21
                if (player1gameEnd && player2gameEnd) {
                    gameEnd = true;
                }
                if (player1Finish) {
                    currentPlayer += 1;
                    // player 2 turn
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
        while (dealerDeck.cardsTotalValue() < 17) {
            dealerDeck.draw(mainDeck);
            String dealer_value = "" + dealerDeck.cardsTotalValue();
            txt_dealer_card_value.setText(dealer_value);
        }
        // gameEnd = true;
    }

    void update() {
        for (int i = 1; i <= numOfPlayer; i ++) {
            switch (i) {
                case 1:
                    if (player1Deck.cardsTotalValue() == 21 && player1Deck.numOfCards == 2) {
                        txt_message.setText("Black Jack!\nYou gain 1.5*" + player1Bet + " BattleCoins!");
                        Player1BattleCoins = (int) (Player1BattleCoins + player1Bet*2.5);
                    } else if (player1Deck.cardsTotalValue() == dealerDeck.cardsTotalValue()
                            && player1Deck.cardsTotalValue() <= 21) {
                        txt_message.setText("Draw!\nYou didn't gain or lose any BattleCoins");
                        Player1BattleCoins = Player1BattleCoins + player1Bet;
                    } else if (player1Deck.cardsTotalValue() <= 21 && dealerDeck.cardsTotalValue() <= 21
                            && (player1Deck.cardsTotalValue() > dealerDeck.cardsTotalValue())) {
                        txt_message.setText("You Win!\nYou gain " + player1Bet + " BattleCoins!");
                        Player1BattleCoins = Player1BattleCoins + player1Bet*2;
                    } else if (player1Deck.cardsTotalValue() <= 21 && dealerDeck.cardsTotalValue() <= 21
                            && (player1Deck.cardsTotalValue() < dealerDeck.cardsTotalValue())) {
                        txt_message.setText("You Lose!\nYou loss " + player1Bet + " BattleCoins!");
                        //PlayerBattleCoins = PlayerBattleCoins - playerBet;
                    } else if (player1Deck.cardsTotalValue() <= 21 && dealerDeck.cardsTotalValue() > 21) {
                        txt_message.setText("Dealer Bust and You Win!\nYou gain " + player1Bet + " BattleCoins!");
                        Player1BattleCoins = Player1BattleCoins + player1Bet*2;
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
                    if (player2Deck.cardsTotalValue() == 21 && player2Deck.numOfCards == 2) {
                        txt_message.setText("Black Jack!\nYou gain 1.5*" + player2Bet + " BattleCoins!");
                        Player2BattleCoins = (int) (Player2BattleCoins + player2Bet*2.5);
                    } else if (player2Deck.cardsTotalValue() == dealerDeck.cardsTotalValue()
                            && player2Deck.cardsTotalValue() <= 21) {
                        txt_message.setText("Draw!\nYou didn't gain or lose any BattleCoins");
                        Player2BattleCoins = Player2BattleCoins + player2Bet;
                    } else if (player2Deck.cardsTotalValue() <= 21 && dealerDeck.cardsTotalValue() <= 21
                            && (player2Deck.cardsTotalValue() > dealerDeck.cardsTotalValue())) {
                        txt_message.setText("You Win!\nYou gain " + player2Bet + " BattleCoins!");
                        Player2BattleCoins = Player2BattleCoins + player2Bet*2;
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
        dealerDeck.returnAllCards(mainDeck);
        switch (numOfPlayer) {
            case 1:
                if (Player1BattleCoins > 0 && Player1BattleCoins < 2500  && round < 5) {
                    initState();
                } else if (Player1BattleCoins >= 2500) {
                    txt_message.setText("Congratulations! You have win the game by exceeding 2500 BattleCoins!");
                    txt_message.setVisibility(View.VISIBLE);
                    // Todo: Navigate to result page
                } else if (Player1BattleCoins == 0) {
                    txt_message.setText("Oops! You have lose the game by losing all BattleCoins!");
                    txt_message.setVisibility(View.VISIBLE);
                    // Todo: Navigate to result page
                } else {
                    txt_message.setText("Oops! You have lose the game by unable to reach 2500 BattleCoins in 5 rounds!");
                    txt_message.setVisibility(View.VISIBLE);
                    // Todo: Navigate to result page
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
                } else {
                    txt_message.setText("Both players own 0 bc, It is a Tie game! Come on guys...");
                    txt_message.setVisibility(View.VISIBLE);
                }
                break;
            default:
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