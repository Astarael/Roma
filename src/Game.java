/** Game.java
 * 
 * By Samuel Baxter and Theo Tse
 * COMP2911
 * 18/03/2012
 */

// need to implement cancelling an action
// discarding
// change interface to have a controller
// change interface to deal with as much input parsing as possible
// more cards (aesculapinum, machina, consulilarius, gladiator, turris) done
// consul, haruspex, essedum
// cards need to be a list
// dice are 'used' too early - no way to cancel actions w/o 'unusing' die


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
    public final static int LAY_CARD = 4;
    public final static int READ_RULES = 5;

    public Die die[];
    public Player players[];

    // create cards
    private int turnNumber = 0;
    
    private int i = 0;
    private Deck deck;
    
    
    public int run() {
        
        initialiseGame();
        int input = 0;
        int cardNum;
        
        subStartingVP();
        while (rollDie()) { 
        }
        
        while (!endGame()) {
            
            Interface.updateBoard(players[0].getVictoryPoints(), players[1].getVictoryPoints(), 
                    players[0].getMoney(), players[1].getMoney(), whoseTurn(), players[0].getHand(),
                    players[1].getHand(), die);
            
            input = Interface.requestInput();
            if (input == END_TURN) {
                
                turnNumber++;
                subStartingVP();
                while (rollDie()) { 
                }
                
            } else if (input == BUY_CARDS) {
                
                boolean valid = false;
                
                input = Interface.getDieInput("BUY CARDS");
                
                // draw #input cards
                // select which ONE to keep
                // discard others
                
                Interface.print ("You drew:");
                
                Cards[] draw = new Cards[input];
                for (i = 0; i < draw.length; i++) {
                    
                    draw[i] = deck.drawCard();
                    Interface.print(draw[i].toString());
                    
                }
                
                while (!valid) {
                    
                    String card = Interface.getInput ("\nWhich card would you like to keep?");
                    card = card.toUpperCase();
                    
                    for (i = 0; i < draw.length; i++) {
                    
                        if (card.equals(draw[i].toString())) {
                            
                            valid = true;
                            players[whoseTurn()].addCard(draw[i]);
                            draw[i] = Cards.NOTACARD;
                            i = draw.length;
                            
                        }
                    
                    }
                    
                    if (!valid) {
                        
                        Interface.printE("Invalid Card Name");
                        
                    }
                    
                }
                
                for (i = 0; i < draw.length; i++) {
                    
                    deck.discardCard(draw[i], draw);
                    
                }
                
            } else if (input == BUY_MONEY) {
                
                input = Interface.getDieInput("BUY MONEY");
                players[whoseTurn()].addMoney(input);
                
            } else if (input == ACTIVATE_CARD) {
                
                input = Interface.getDieInput ("ACTIVATE A CARD");
                cardNum = Interface.pickACard();
                
                Cards[] cards = players[whoseTurn()].getCardsInPlay();
                
                if (cards[cardNum].isActivatable(cards[cardNum])) {
                
                    cards[cardNum].activate(whoseTurn(), players, cards[cardNum], deck, cardNum, getDice());
                    
                } else {
                    
                    Interface.printE("You cannot activate this card!");
                    // 'unuse' die
                    for (i = 1; i < NUM_DICE; i++) {

                        if (die[i].getValue() == cardNum + 1) {
                            
                            die[i].used = false;
                            i = NUM_DICE;
                            
                        }
                        
                    }
                    
                }
                
            } else if (input == LAY_CARD) {
                
                layCard();
                
            } else if (input == READ_RULES) {
                
                Interface.displayRules("Forum");
                
            }
            
        }
        
        
        return whoWon();
        
    }


    private void layCard() {
        
        Cards[] hand = players[whoseTurn()].getHand();
        String lay = "";
        boolean correctCard = false;
        
        Interface.print("Your hand contains:");
        
        for (i = 0; i < hand.length; i++) {

            if (hand[i] != Cards.NOTACARD) {
                
                Interface.print(hand[i].toString());
                
            }
        }
        
        // get the name of the card
        while (!correctCard) {
            
            lay = Interface.getInput("Type in the name of the card you wish to lay");
            lay = lay.toUpperCase();
            
            for (i = 0; i < hand.length; i++) {
                
                if (lay.equals(hand[i].toString())) {
                    
                    correctCard = true;
                    i = hand.length;
                    
                }
                
            }
            
            if ((!correctCard) || (lay.equals("NOTACARD"))) {
            
                Interface.print ("Invalid Card Name");
                correctCard = false;
            
            }
            
        }
        
        Cards temp = Cards.valueOf(lay);
        
        // check that they have enough money
        if (players[whoseTurn()].getMoney() < temp.getMoneyCost()) {
            
            Interface.printE("Not enough money to lay this card");
            return;
            
        }
        
        // get the position they wish to lay in
        int position = -1;
        
        while ((position < 0) || (position > 6)) {
            
            position = Interface.getActionInput("Where would you like to lay this card?");
        
            if ((position < 0) || (position > 6)) {
                
                Interface.printE("Invalid position");
                
            }
            
        }
        
        players[whoseTurn()].addMoney(-temp.getMoneyCost());
        players[whoseTurn()].layCard(temp, position);
        
    }


    private void subStartingVP() {
        
        int counter = 0;
        
        // subtract 1 VP for each unoccupied dice disk
        for (i = 0; i <= NUM_SIDES_ON_DICE; i++) {
            
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
            
            Interface.print("PLAYER " + (i + 1) + "'s hand:");
            
            for (j = 0; j < Deck.NUM_START_CARDS; j++) {
                
                Interface.print(players[i].getHand()[j].toString());
                
            }
            
            for (j = 0; j < Deck.NUM_START_CARDS; j++) {

                position = Interface.getActionInput("Where do you want to lay your " + players[i].getHand()[j] + "?");
                players[i].layCard(players[i].getHand()[j], position);
                
            }
            
            Interface.print("Player 1:");
            for (k = 0; k <= Game.NUM_SIDES_ON_DICE; k++) {
            
                Interface.print("(" + k + ") ");
                
                temp1 = players[0].getCardsInPlay()[k];
                
                if (temp1 != Cards.NOTACARD){
                    
                    Interface.print("**CARD**");
                    
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