package game;

import java.util.Arrays;
import java.util.Collections;

import userInterface.*;

import cards.*;


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
    public final static int NUM_AESCULAPINUM = 2;
    public final static int NUM_GLADIATOR = 2;
    public final static int NUM_MACHINA = 2;
    public final static int NUM_CONSILIARIUS = 2;
    public final static int NUM_TURRIS = 2;
    public final static int NUM_HARUSPEX = 2;
    public final static int NUM_LEGIONARIUS = 3;
    
    public final static int NUM_SWAP = 4;

    public final static int NUM_CARDS = 52;
    public final static int NUM_START_CARDS = 5;

    private Card[] cards;
    private Card[] discard;
    
    private RomaUserInterface ui;
    private Player[] players;
    
    
    public Deck (Player[] players, RomaUserInterface rui) {
        
        int i = 0;
        int j = 0;
        
        ui = rui;
        this.players = players;
        
        cards = new Card[NUM_CARDS];
        discard = new Card[NUM_CARDS];

        for (i = 0; i < NUM_MERCATOR; i++) {
            
            cards[j] = Card.MERCATOR;
            Card.MERCATOR.setUI(ui);
            j++;
            
        }
        
        for (i = 0; i < NUM_TRIBUNUS_PLEBIS; i++) {
        
            cards[j] = Card.TRIBUNUSPLEBIS;
            Card.TRIBUNUSPLEBIS.setUI(ui);
            j++;
            
        }
        
        for (i = 0; i < NUM_FORUM; i++) {
            
            cards[j] = Card.FORUM;
            Card.FORUM.setUI(ui);
            j++;
            
        }
        
        for (i = 0; i < NUM_SICARIUS; i++) {
            
            cards[j] = Card.SICARIUS;
            Card.SICARIUS.setUI(ui);
            j++;
            
        }
        
        for (i = 0; i < NUM_NERO; i++) {
            
            cards[j] = Card.NERO;
            Card.NERO.setUI(ui);
            j++;
            
        }

        for (i = 0; i < NUM_MERCATUS; i++) {
            
            cards[j] = Card.MERCATUS;
            Card.MERCATUS.setUI(ui);
            j++;
            
        }
        
        for (i = 0; i < NUM_BASILICA; i++) {
            
            cards[j] = Card.BASILICA;
            Card.BASILICA.setUI(ui);
            j++;
            
        }
        
        for (i = 0; i < NUM_TEMPLUM; i++) {
            
            cards[j] = Card.TEMPLUM;
            Card.TEMPLUM.setUI(ui);
            j++;
            
        }
        
        for (i = 0; i < NUM_LEGAT; i++) {
            
            cards[j] = Card.LEGAT;
            Card.LEGAT.setUI(ui);
            j++;
            
        }
        
        for (i = 0; i < NUM_ARCHITECTUS; i++) {
            
            cards[j] = Card.ARCHITECTUS;
            Card.ARCHITECTUS.setUI(ui);
            j++;
            
        }
        
        for (i = 0; i < NUM_SENATOR; i++) {
            
            cards[j] = Card.SENATOR;
            Card.SENATOR.setUI(ui);
            j++;
            
        }
        
        for (i = 0; i < NUM_AESCULAPINUM; i++) {
            
            cards[j] = Card.AESCULAPINUM;
            Card.AESCULAPINUM.setUI(ui);
            j++;
            
        }
        
        for (i = 0; i < NUM_MACHINA; i++) {
            
            cards[j] = Card.MACHINA;
            Card.MACHINA.setUI(ui);
            j++;
            
        }
        
        for (i = 0; i < NUM_CONSILIARIUS; i++) {
            
            cards[j] = Card.CONSILIARIUS;
            Card.CONSILIARIUS.setUI(ui);
            j++;
            
        }
        
        for (i = 0; i < NUM_GLADIATOR; i++) {
            
            cards[j] = Card.GLADIATOR;
            Card.GLADIATOR.setUI(ui);
            j++;
            
        }
        
        for (i = 0; i < NUM_TURRIS; i++) {
            
            cards[j] = Card.TURRIS;
            Card.TURRIS.setUI(ui);
            j++;
            
        }
        
        for (i = 0; i < NUM_HARUSPEX; i++) {
            
            cards[j] = Card.HARUSPEX;
            Card.HARUSPEX.setUI(ui);
            j++;
            
        }
        
        for (i = 0; i < NUM_LEGIONARIUS; i++) {
            
            cards[j] = Card.LEGIONARIUS;
            Card.LEGIONARIUS.setUI(ui);
            j++;
            
        }
        
        for (; j < NUM_CARDS; j++) {
            
            cards[j] = Card.NOTACARD;
            Card.NOTACARD.setUI(ui);
            
        }
        
        for (i = 0; i < NUM_CARDS; i++) {
            
            discard[i] = Card.NOTACARD;
            
        }

        shuffleCards();
        
    }
    
    public Card[] getCards () {
        
        return cards;
        
    }
    
    public void setCards (Card[] c) {
        
        cards = c;
        
    }
    
    public Card[] getDiscard () {
        
        return discard;
        
    }

    
    public void allocateCards () {
        
        int i;
        int j;
        int k;
        Card draw;
        Card swap[] = new Card[NUM_SWAP];
        
        for (i = 0; i < Game.NUM_PLAYERS; i++) {
            
            ui.print ("PLAYER " + (i + 1) +  "'s Cards:");
            
            // give first 4 cards from cards[] and give to player
            for (j = 0; j < NUM_START_CARDS; j++) {
                
                draw = drawCard();
                players[i].addCard (draw);
                ui.print (draw.toString());
                
            }
            
            for (k = (NUM_SWAP - 1) - (i * 2); k >= (NUM_SWAP - 2) - (i * 2); k--) {
                
                swap[k] = getSwapCard (i + 1, players[i].getHand());
                
            }
            
        }
        
        swapCards (swap, players[0].getHand(), players[1].getHand());
        
    }
    
    
    public Card drawCard () {
        
        Card temp = Card.NOTACARD;
        int j = 0;
        
        while (temp == Card.NOTACARD) {
        
            for (j = 0; j < NUM_CARDS; j++) {
            
                if (cards[j] != Card.NOTACARD) {
                
                    temp = cards[j];
                    discardCard(cards[j], cards);
                    j = NUM_CARDS;
                    
                }
                
            }
            
            if (temp == Card.NOTACARD) {
                
                // shuffle old discard pile
                Collections.shuffle(Arrays.asList(discard));
                
                // swap discard pile and cards pile
                Card[] swap = discard;
                discard = cards;
                cards = swap;
                
            }
            
        }
        
        
        return temp;
        
    }
    
    
    public void shuffleCards() {
        
        // voodoo magic
        Collections.shuffle(Arrays.asList(cards));
        
    }
    
    
    public void discardCard (Card c, Card[] hand) {
        
        int i;
        for (i = 0; i < NUM_CARDS; i++) {
            
            if (discard[i] == Card.NOTACARD) {
                
                discard[i] = c;
                i = NUM_CARDS;
                
            }
            
        }
        
        for (i = 0; i < hand.length; i++) {
            
            if (c == hand[i]) {
                
                hand[i] = Card.NOTACARD;
                i = hand.length;
                
            }
            
        }
        
    }
    
    private Card getSwapCard (int whoseTurn, Card[] hand) {
        
        Card result = Card.NOTACARD;
        int i;
        Card swap;
        
        while (result == Card.NOTACARD) {
        
            ui.print("Pick the card you want to swap (PLAYER " + 
                    whoseTurn + "). (type in the full name)");
            swap = ui.pickACard();
        
            // check that entered card is in player's hand
            for (i = 0; i < NUM_START_CARDS; i++) {
                
                if (swap == hand[i]) {
                
                    result = hand[i];
                    i = NUM_START_CARDS;
                
                }
            
            }
            
            if (result == Card.NOTACARD) {
                
                ui.printE ("ERROR: Invalid Card name");
                
            }
            
        }
        
        return result;
        
    }
    
    
    private void swapCards (Card[] swap, Card[] p1Hand, Card[] p2Hand) {
        
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

    public void emptyDeck() {
        
        cards = new Card[NUM_CARDS];
        
    }

    public void addCard(Card c) {
        
        int i = 0;
        
        for (i = 0; i < NUM_CARDS; i++) {

            if (cards[i] == Card.NOTACARD) {
                
                cards[i] = c;
                
            }
            
        }
        
        
        
    }


}
