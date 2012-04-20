/** Game.java
 * By Samuel Baxter and Theo Tse
 * COMP2911
 * 18/03/2012
 */
// picking up cards
// subtract money when laying a card


import java.util.Arrays;
import java.util.Collections;

/**
 * Game class for Roma. Deals with beginning, running and ending a game.
 */

public class Game {

    // game consts
    public final static int MAX_VICTORY_POINTS = 36;
    public final static int START_VICTORY_POINTS = 10;
    public final static int START_MONEY = 0;
    public final static int NUM_DICE = 4;
    public final static int NUM_SIDES_ON_DICE = 6;
    public final static int NUM_PLAYERS = 2;
    
    // interface actions
    public final static int END_TURN = 0;
    public final static int BUY_MONEY = 1;
    public final static int BUY_CARDS = 2;
    public final static int ACTIVATE_CARD = 3;
    public final static int READ_RULES = 4;

    public Die die[];
    public Player players[];

    // create cards
    private int turnNumber = 0;
    
    private int i = 0;
    private Deck deck;
    
    
    public int run() {
        
        initialiseGame();
        int input = Interface.input;
        int card;
        
        subStartingVP();
        while (rollDie()) { 
        }
        
        while (!endGame()) {
            
            Interface.displayBoard(players[0].getVictoryPoints(), players[1].getVictoryPoints(), 
                    players[0].getMoney(), players[1].getMoney(), whoseTurn(), players[0].getHand(),
                    players[1].getHand(), die);
            
            input = Interface.requestInput();
            if (input == END_TURN) {
                
                turnNumber++;
                subStartingVP();
                while (rollDie()) { 
                }
                
            } else if (input == BUY_CARDS) {
                
                input = Interface.getDieInput("BUY CARDS");
                
            } else if (input == BUY_MONEY) {
                
                input = Interface.getDieInput("BUY MONEY");
                players[whoseTurn()].addMoney(input);
                
            } else if (input == ACTIVATE_CARD) {
                
                input = Interface.getDieInput ("ACTIVATE A CARD");
                card = Interface.pickACard();
                
                Cards[] hand = players[whoseTurn()].getCardsInPlay();
                for (i = 0; i < hand.length; i++) {
                    System.out.println (hand[i]);
                }
                hand[card - 1].activate(whoseTurn(), players, hand[card - 1], deck, card, getDice());
                
            } else if (input == READ_RULES) {
                
                Interface.displayRules("Forum");
                
            }
            
        }
        
        
        return whoWon();
        
    }


    private void subStartingVP() {
        
        int counter = 0;
        
        // subtract 1 VP for each unoccupied dice disk
        for (i = 1; i <= NUM_SIDES_ON_DICE; i++) {
            
            if (players[whoseTurn()].getCardsInPlay()[i] == Cards.NOTACARD) {
                
                counter++;
                
            }
            
        }
        
        players[whoseTurn()].addVictoryPoints(-counter);
    }


    public boolean rollDie() {
        
        boolean sameDie = true;
        boolean reRoll = false;
        
        for (i = 1; i < Game.NUM_DICE; i++) {

            die[i].rollDie();
            die[i].used = false;

        }
        
        for (i = 1; (i < NUM_DICE - 1) && (sameDie); i++) {
            
            if (die[i].getValue() != die[i + 1].getValue()) {
                
                sameDie = false;
                
            }
            
        }
        
        if (sameDie) {
                        
            if (Interface.getInput ("Do you want to re-roll?").equals("y")) {
                
                reRoll = true;
                
                
            }
            
        }
        
        
        return reRoll;
        
    }


    /**
     * Creates the players and dice.
     */
    public void initialiseGame () {

        
        die = new Die[NUM_DICE];
        for (i = 0; i < NUM_DICE; i++) {

            die[i] = new Die();

        }
        
        players = new Player[NUM_PLAYERS];

        for (i = 0; i < NUM_PLAYERS; i++) {
            
            players[i] = new Player();
            
        }
        
        deck = new Deck (this);
        // UNCOMMENT THIS LINE TO RUN THE TESTS
        deck.allocateCards(this);
        
        layStartCards();

    }
    
    
    /**
     * Determines if the conditions for victory or defeat are met.
     */
    public boolean endGame () {

        boolean result = false;

        if ((players[0].getVictoryPoints() <= 0) || (players[1].getVictoryPoints() <= 0)
                || (players[0].getVictoryPoints() + players[1].getVictoryPoints() >= MAX_VICTORY_POINTS)) {

            result = true;

        }


        return result;

    }
    
    /**
     * Returns either a 1 or a 2 indicating the player number of the player who won
     */
    public int whoWon () {

        int winner = 0;
        
        if (players[0].getVictoryPoints() > players[1].getVictoryPoints()) {
            
            winner = 1;
        
        } else if (players[1].getVictoryPoints () > players[0].getVictoryPoints()) {
            
            winner = 2;
            
        }
        
        
        return winner;
        
    }
    
    public int whoseTurn () {
        
        return turnNumber % NUM_PLAYERS;
        
    }
    
    public boolean useDie (int value) {
        
        boolean valid = false;
        
        for (i = 1; (i <NUM_DICE) && (valid == false); i++) {
            
            if ((value == die[i].getValue()) && (die[i].used == false)) {
        
                valid = true;
                die[i].used = true;
        
            }
            
        }
        
        return valid;
        
    }
    
    private void layStartCards () {
        
        int i;
        int j;
        int k;
        int position;
        Cards temp1;
        
        for (i = 0; i < NUM_PLAYERS; i++) {
            
            Interface.print("PLAYER " + (i + 1) + "'s hand:\n");
            
            for (j = 0; j < Deck.NUM_START_CARDS; j++) {
                
                Interface.print(players[i].getHand()[j] + "\n");
                
            }
            
            for (j = 0; j < Deck.NUM_START_CARDS; j++) {

                position = Interface.getActionInput("Where do you want to lay your " + players[i].getHand()[j] + "?");
                players[i].layCard(players[i].getHand()[j], position);
                
            }
            
            Interface.print("Player 1:\n");
            for (k = 1; k <= Game.NUM_SIDES_ON_DICE; k++) {
            
                Interface.print("(" + k + ") ");
                
                temp1 = players[0].getCardsInPlay()[k];
                
                if (temp1 != Cards.NOTACARD){
                    
                    Interface.print("**CARD**\n");
                    
                } else {
                    
                    Interface.print("\n");
                    
                }
                
            }
            
        }
        
    }
    
    public Deck getDeck() {
        
        return deck;
        
    }
    
    public Die[] getDice() {
        
        
        return die;
        
    }

}