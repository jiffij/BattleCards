import com.example.battlecards.Deck;
import com.example.battlecards.PLAYERS;

enum POOL{
    LEFT, RIGHT
}

public class Speed {

    Deck mainDeck = new Deck();
    Deck ADeck;
    Deck BDeck;
    Deck AHand;
    Deck BHand;
    Deck left;
    Deck right;
    Deck leftPool;
    Deck rightPool;
    boolean AWant = false;
    boolean BWant = false;

    public Speed(){
        mainDeck = new Deck();
        ADeck = new Deck();
        BDeck = new Deck();
        AHand = new Deck();
        BHand = new Deck();
        left = new Deck();
        right = new Deck();
        leftPool = new Deck();
        rightPool = new Deck();


        mainDeck.addAllCards();

        mainDeck.shuffle();

        ADeck.drawTopNCards(mainDeck, 15);
        BHand.drawTopNCards(mainDeck, 5);
        ADeck.drawTopNCards(mainDeck, 15);
        BHand.drawTopNCards(mainDeck, 5);
        left.drawTopNCards(mainDeck, 5);
        right.drawTopNCards(mainDeck, 5);
        leftPool.draw(mainDeck);
        rightPool.draw(mainDeck);

        System.out.println("After Shuffle.");
        System.out.println(ADeck.toString());
    }

    private PLAYERS checkWin(){
         if(ADeck.isEmpty() && AHand.isEmpty()){
             return PLAYERS.A;
         }else if(BDeck.isEmpty() && BHand.isEmpty()){
             return PLAYERS.B;
         }else{
             return PLAYERS.NONE;
         }
    }

    public void reload(PLAYERS who){
        Deck playerDeck = (who == PLAYERS.A)? ADeck: BDeck;
        if(playerDeck.isEmpty()) return;
        playerDeck.draw(mainDeck);
    }

    public boolean playCard(PLAYERS who, POOL lr, int card){
        Deck pool = (lr == POOL.LEFT)? leftPool: rightPool;
        Deck hand = (who == PLAYERS.A)? AHand: BHand;
        if(hand.length()-1 < card) return false;
        if(!pool.getCard(0).isNeighbor(hand.getCard(card))) return false;
        pool.moveToTopFrom(hand, card);
        return true;
    }

    public void wantCard(PLAYERS who){
        switch (who){
            case A:
                AWant = true;
                break;
            case B:
                BWant = true;
                break;
        }
    }

    private void flipNewCard(){
        if(!AWant || !BWant) return;
        AWant = false;
        BWant = false;
        //left right pile are empty
        if(left.isEmpty() || right.isEmpty()){
            leftPool.shuffle();
            rightPool.shuffle();
            left.moveAllFrom(leftPool);
            right.moveAllFrom(rightPool);
        }

        leftPool.moveToTopFrom(left, 0);
        rightPool.moveToTopFrom(right, 0);
    }



    public void gameLoop(){
        while(checkWin() == PLAYERS.NONE){
            flipNewCard();
        }
    }

}
