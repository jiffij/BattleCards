package com.example.battlecards;

import java.util.Scanner;

public class MyDemo54 {

    public static void main(String ags[]) {

        Deck mainDeck = new Deck();
        mainDeck.addAllCards();

        System.out.println("Welcome to Java Blackjack!");
        System.out.println("Before Shuffle.");
        System.out.println(mainDeck.toString());

        mainDeck.shuffle();

        System.out.println("After Shuffle.");
        System.out.println(mainDeck.toString());

        Deck playerDeck = new Deck();
        Deck dealerDeck = new Deck();

        nextAction(mainDeck, playerDeck, dealerDeck);
    }
    //********************************************************************************************
    static void nextAction(Deck mainDeck, Deck playerDeck, Deck dealerDeck) {
        boolean gameEnd = false;
        boolean playerFinish = false;
        boolean playAgain = true;
        int playerMoney = 1000;
        int playerBet=0;

        while (playAgain) {
            Scanner userInput = new Scanner(System.in);
            Boolean inputComplete1= false;

            while (!inputComplete1) {
                System.out.println(" ");
                System.out.print("Your money:$" + playerMoney + " " + "Place your bet:$");
                int playerBetTemp = userInput.nextInt();
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
                int playerAction = userInput.nextInt();

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
                    int playerAction = userInput.nextInt();

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
