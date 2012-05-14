package game;

import userInterface.*;
import cards.*;

/** Player.java
 * By Samuel Baxter and Theo Tse
 * COMP2911
 * 13/03/2012
 */

public class Player {
    
    private int victoryPoints;
    private int money;
    private Card[] hand;
    private Card[] cardsInPlay;
    private RomaUserInterface ui;
    
    /**
     * Player class for Roma. Stores the player's current stats (money, cards, vp)
     */
    
    
    public Player () {
        
        int i;
        
        this.victoryPoints = Game.START_VICTORY_POINTS;
        this.money = Game.START_MONEY;
        this.hand = new Card[Deck.NUM_CARDS];
        
        for (i = 0; i < Deck.NUM_CARDS; i++) {
            
            hand[i] = Card.NOTACARD;
            
        }
        
        // this array is 7 long, the 0th is blank
        cardsInPlay = new Card[Game.NUM_SIDES_ON_DICE + 1];
        
        for (i = 0; i < Game.NUM_SIDES_ON_DICE + 1; i++) {
            
            cardsInPlay[i] = Card.NOTACARD;
            
        }
        
        /**
         * Create a Player. Cards is an ArrayList of cards
         */
        
    }
    
    public void setUI (RomaUserInterface rui) {
        
        ui = rui;
        
    }
    
    
    public int getVictoryPoints () {
        
        return this.victoryPoints;
        
    }
    
    public int getMoney() {
        
        return this.money;
        
    }
    
    public Card[] getHand() {
        
        
        return this.hand;
        
    }
    
    public void addVictoryPoints (int value) {
        
        if (value > Game.MAX_VICTORY_POINTS) {
            
            ui.printE ("ERROR: Invalid number of Victory Points");
            // exit
            
        }
        
        this.victoryPoints += value;
        
        /**
         * Add or subtract an integer value of Victory Points.
         */
        
    }
    
    /**
     * Add or subtract an integer value of money.
     */
    public void addMoney (int value) {
        
        this.money += value;
        
    }
    
    public void addCard (Card draw) {
        
        int i;
        for (i = 0; i < hand.length; i++) {
            
            if (hand[i] == Card.NOTACARD) {
                
                hand[i] = draw;
                i = hand.length;
                
            }
            
        }
        
    }
    
    public Card[] getCardsInPlay () {
        
        return cardsInPlay;
        
    }
    
    // discard the other card
    public Card layCard (Card c, int position) {
        
        Card temp = cardsInPlay[position];
            
        cardsInPlay[position] = c;
        
        int i;
        
        for (i = 0; i < hand.length; i++) {
            
            if (hand[i] == c) {
                
                hand[i] = Card.NOTACARD;
                i = hand.length;
                
            }
            
        }
        
        
        return temp;
        
    }


    public int getPosition (Card c) {
        
        int position = 0;
        int i;
        
        for (i = 1; i <= Game.NUM_SIDES_ON_DICE; i++) {
            
            if (cardsInPlay[i] == c) {
                
                position = i;
                i = Game.NUM_SIDES_ON_DICE;
                
            }
            
        }
        
        
        return position;
    }

    public void setMoney(int amount) {
        
        money = amount;
        
    }

    public void setVictoryPoints(int points) {

        victoryPoints = points;
        
    }

}
