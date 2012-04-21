import java.util.Arrays;
import java.util.Collections;

/* Deck.java
 * By Samuel Baxter and Theo Tse
 * COMP2911
 * 03/04/2012
 */

public class Deck {
    
    public final static int NUM_MERCATOR = 1;
    public final static int NUM_FORUM = 6;
    public final static int NUM_TRIBUNUS_PLEBIS = 2;
    public final static int NUM_SICARIUS = 1;
    public final static int NUM_NERO = 1;
    public final static int NUM_MERCATUS = 2;
    public final static int NUM_BASILICA = 2;
    public final static int NUM_TEMPLUM = 2;
    public final static int NUM_LEGAT = 2;
    public final static int NUM_ARCHITECTUS = 2;
    public final static int NUM_SENATOR = 2;
    
    public final static int NUM_SWAP = 4;

    public final static int NUM_CARDS = 52;
    public final static int NUM_START_CARDS = 4;

    private Cards[] cards;
    private Cards[] discard;
    
    
    public Deck (Game g) {
        
        int i = 0;
        int j = 0;
        
        cards = new Cards[NUM_CARDS];
        discard = new Cards[NUM_CARDS];

        for (i = 0; i < NUM_MERCATOR; i++) {
            
            cards[j] = Cards.MERCATOR;
            j++;
            
        }
        
        for (i = 0; i < NUM_TRIBUNUS_PLEBIS; i++) {
        
            cards[j] = Cards.TRIBUNUSPLEBIS;
            j++;
            
        }
        
        for (i = 0; i < NUM_FORUM; i++) {
            
            cards[j] = Cards.FORUM;
            j++;
            
        }
        
        for (i = 0; i < NUM_SICARIUS; i++) {
            
            cards[j] = Cards.SICARIUS;
            j++;
            
        }
        
        for (i = 0; i < NUM_NERO; i++) {
            
            cards[j] = Cards.NERO;
            j++;
            
        }

        for (i = 0; i < NUM_MERCATUS; i++) {
            
            cards[j] = Cards.MERCATUS;
            j++;
            
        }
        
        for (i = 0; i < NUM_BASILICA; i++) {
            
            cards[j] = Cards.BASILICA;
            j++;
            
        }
        
        for (i = 0; i < NUM_TEMPLUM; i++) {
            
            cards[j] = Cards.TEMPLUM;
            j++;
            
        }
        
        for (i = 0; i < NUM_LEGAT; i++) {
            
            cards[j] = Cards.LEGAT;
            j++;
            
        }
        
        for (i = 0; i < NUM_ARCHITECTUS; i++) {
            
            cards[j] = Cards.ARCHITECTUS;
            j++;
            
        }
        
        for (i = 0; i < NUM_SENATOR; i++) {
            
            cards[j] = Cards.SENATOR;
            j++;
            
        }

        shuffleCards();
        
    }

    
    public void allocateCards (Game g) {
        
        int i;
        int j;
        int k;
        Cards draw;
        Cards swap[] = new Cards[NUM_SWAP];
        
        for (i = 0; i < Game.NUM_PLAYERS; i++) {
            
            Interface.print ("PLAYER " + (i + 1) +  "'s Cards:\n");
            
            // give first 4 cards from cards[] and give to player
            for (j = 0; j < NUM_START_CARDS; j++) {
                
                draw = drawCard();
                g.players[i].addCard (draw);
                Interface.print (draw + "\n");
                
            }
            
            for (k = (NUM_SWAP - 1) - (i * 2); k >= (NUM_SWAP - 2) - (i * 2); k--) {
                
                swap[k] = getSwapCard (i + 1, g.players[i].getHand());
                
            }
            
        }
        
        swapCards (swap, g.players[0].getHand(), g.players[1].getHand());
        
    }
    
    
    public Cards drawCard () {
        
        Cards temp = null;
        int j = 0;
        
        while (temp == null) {
        
            for (j = 0; j < NUM_CARDS; j++) {
            
                if (cards[j] != null) {
                
                    temp = cards[j];
                    cards[j] = null;
                    j = NUM_CARDS;
                    
                }
                
            }
            
            if (temp == null) {
                
                // swap discard pile and cards pile
                // shuffle old discard pile
                // look at making it a collection
                
            }
            
        }
        
        
        return temp;
        
    }
    
    
    public void shuffleCards() {
        
        // voodoo magic
        Collections.shuffle(Arrays.asList(cards));
        
    }
    
    
    public void discardCard (Cards c) {
        
        int i;
        for (i = 0; i < NUM_CARDS; i++) {
            
            if (discard[i] == Cards.NOTACARD) {
                
                discard[i] = c;
                i = NUM_CARDS;
                
            }
            
        }
        
    }
    
    private Cards getSwapCard (int whoseTurn, Cards[] hand) {
        
        Cards result = Cards.NOTACARD;
        int i;
        
        while (result == Cards.NOTACARD) {
        
            String swap = Interface.getInput("Pick the card you want to swap (PLAYER " + 
                    whoseTurn + "). (type in the full name)");
        
            // check that entered card is in player's hand
            for (i = 0; i < NUM_START_CARDS; i++) {
                
                if (swap.equalsIgnoreCase(hand[i].toString())) {
                
                    result = hand[i];
                    i = NUM_START_CARDS;
                
                }
            
            }
            
            if (result == Cards.NOTACARD) {
                
                Interface.printE ("ERROR: Invalid Card name");
                
            }
            
        }
        
        return result;
        
    }
    
    
    private void swapCards (Cards[] swap, Cards[] p1Hand, Cards[] p2Hand) {
        
        // find the cards in the player hand
        int i;
        int j;
        
        // maybe fix this later
        
        for (i = 0; i < NUM_SWAP/Game.NUM_PLAYERS; i++) {
            
            for (j = 0; j < NUM_START_CARDS; j++) {
            
                if (p2Hand[j] == swap[i]) {
                    
                    p2Hand[j] = swap[i + Game.NUM_PLAYERS];
                    j = NUM_START_CARDS;
                    
                }
                
            }
            
        }
        
        for (i = 0; i < NUM_SWAP/Game.NUM_PLAYERS; i++) {
            
            for (j = 0; j < NUM_START_CARDS; j++) {
            
                if (p1Hand[j] == swap[i + Game.NUM_PLAYERS]) {
                    
                    p1Hand[j] = swap[i];
                    j = NUM_START_CARDS;
                    
                }
                
            }
            
        }
        
    }


}
