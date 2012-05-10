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

    private Cards[] cards;
    private Cards[] discard;
    
    private RomaUserInterface ui;
    private Player[] players;
    
    
    public Deck (Player[] players, RomaUserInterface rui) {
        
        int i = 0;
        int j = 0;
        
        ui = rui;
        this.players = players;
        
        cards = new Cards[NUM_CARDS];
        discard = new Cards[NUM_CARDS];

        for (i = 0; i < NUM_MERCATOR; i++) {
            
            cards[j] = Cards.MERCATOR;
            Cards.MERCATOR.setUI(ui);
            j++;
            
        }
        
        for (i = 0; i < NUM_TRIBUNUS_PLEBIS; i++) {
        
            cards[j] = Cards.TRIBUNUSPLEBIS;
            Cards.TRIBUNUSPLEBIS.setUI(ui);
            j++;
            
        }
        
        for (i = 0; i < NUM_FORUM; i++) {
            
            cards[j] = Cards.FORUM;
            Cards.FORUM.setUI(ui);
            j++;
            
        }
        
        for (i = 0; i < NUM_SICARIUS; i++) {
            
            cards[j] = Cards.SICARIUS;
            Cards.SICARIUS.setUI(ui);
            j++;
            
        }
        
        for (i = 0; i < NUM_NERO; i++) {
            
            cards[j] = Cards.NERO;
            Cards.NERO.setUI(ui);
            j++;
            
        }

        for (i = 0; i < NUM_MERCATUS; i++) {
            
            cards[j] = Cards.MERCATUS;
            Cards.MERCATUS.setUI(ui);
            j++;
            
        }
        
        for (i = 0; i < NUM_BASILICA; i++) {
            
            cards[j] = Cards.BASILICA;
            Cards.BASILICA.setUI(ui);
            j++;
            
        }
        
        for (i = 0; i < NUM_TEMPLUM; i++) {
            
            cards[j] = Cards.TEMPLUM;
            Cards.TEMPLUM.setUI(ui);
            j++;
            
        }
        
        for (i = 0; i < NUM_LEGAT; i++) {
            
            cards[j] = Cards.LEGAT;
            Cards.LEGAT.setUI(ui);
            j++;
            
        }
        
        for (i = 0; i < NUM_ARCHITECTUS; i++) {
            
            cards[j] = Cards.ARCHITECTUS;
            Cards.ARCHITECTUS.setUI(ui);
            j++;
            
        }
        
        for (i = 0; i < NUM_SENATOR; i++) {
            
            cards[j] = Cards.SENATOR;
            Cards.SENATOR.setUI(ui);
            j++;
            
        }
        
        for (i = 0; i < NUM_AESCULAPINUM; i++) {
            
            cards[j] = Cards.AESCULAPINUM;
            Cards.AESCULAPINUM.setUI(ui);
            j++;
            
        }
        
        for (i = 0; i < NUM_MACHINA; i++) {
            
            cards[j] = Cards.MACHINA;
            Cards.MACHINA.setUI(ui);
            j++;
            
        }
        
        for (i = 0; i < NUM_CONSILIARIUS; i++) {
            
            cards[j] = Cards.CONSILIARIUS;
            Cards.CONSILIARIUS.setUI(ui);
            j++;
            
        }
        
        for (i = 0; i < NUM_GLADIATOR; i++) {
            
            cards[j] = Cards.GLADIATOR;
            Cards.GLADIATOR.setUI(ui);
            j++;
            
        }
        
        for (i = 0; i < NUM_TURRIS; i++) {
            
            cards[j] = Cards.TURRIS;
            Cards.TURRIS.setUI(ui);
            j++;
            
        }
        
        for (i = 0; i < NUM_HARUSPEX; i++) {
            
            cards[j] = Cards.HARUSPEX;
            Cards.HARUSPEX.setUI(ui);
            j++;
            
        }
        
        for (i = 0; i < NUM_LEGIONARIUS; i++) {
            
            cards[j] = Cards.LEGIONARIUS;
            Cards.LEGIONARIUS.setUI(ui);
            j++;
            
        }
        
        for (; j < NUM_CARDS; j++) {
            
            cards[j] = Cards.NOTACARD;
            Cards.NOTACARD.setUI(ui);
            
        }
        
        for (i = 0; i < NUM_CARDS; i++) {
            
            discard[i] = Cards.NOTACARD;
            
        }

        shuffleCards();
        
    }
    
    public Cards[] getCards () {
        
        return cards;
        
    }
    
    public void setCards (Cards[] c) {
        
        cards = c;
        
    }
    
    public Cards[] getDiscard () {
        
        return discard;
        
    }

    
    public void allocateCards () {
        
        int i;
        int j;
        int k;
        Cards draw;
        Cards swap[] = new Cards[NUM_SWAP];
        
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
    
    
    public Cards drawCard () {
        
        Cards temp = Cards.NOTACARD;
        int j = 0;
        
        while (temp == Cards.NOTACARD) {
        
            for (j = 0; j < NUM_CARDS; j++) {
            
                if (cards[j] != Cards.NOTACARD) {
                
                    temp = cards[j];
                    discardCard(cards[j], cards);
                    j = NUM_CARDS;
                    
                }
                
            }
            
            if (temp == Cards.NOTACARD) {
                
                // shuffle old discard pile
                Collections.shuffle(Arrays.asList(discard));
                
                // swap discard pile and cards pile
                Cards[] swap = discard;
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
    
    
    public void discardCard (Cards c, Cards[] hand) {
        
        int i;
        for (i = 0; i < NUM_CARDS; i++) {
            
            if (discard[i] == Cards.NOTACARD) {
                
                discard[i] = c;
                i = NUM_CARDS;
                
            }
            
        }
        
        for (i = 0; i < hand.length; i++) {
            
            if (c == hand[i]) {
                
                hand[i] = Cards.NOTACARD;
                i = hand.length;
                
            }
            
        }
        
    }
    
    private Cards getSwapCard (int whoseTurn, Cards[] hand) {
        
        Cards result = Cards.NOTACARD;
        int i;
        Cards swap;
        
        while (result == Cards.NOTACARD) {
        
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
            
            if (result == Cards.NOTACARD) {
                
                ui.printE ("ERROR: Invalid Card name");
                
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
