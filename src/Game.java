/** Game.java
 * 
 * By Samuel Baxter and Theo Tse
 * COMP2911
 * 18/03/2012
 */

// git push roma master

// need to implement cancelling an action
// change interface to deal with as much input parsing as possible - done
// more cards (haruspex) done
// consul, essedum
// cards need to be a list
// no way to cancel actions


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
    public final static int DRAW_CARD = 2;
    public final static int ACTIVATE_CARD = 3;
    public final static int LAY_CARD = 4;
    public final static int READ_RULES = 5;

    public Die die[];
    public Player players[];

    private static RomaUserInterface ui;
    private int turnNumber = 0;
    
    private int i = 0;
    private Deck deck;
    private boolean valid = false;
    private int input = 0;
    
    
    public static void main (String[] args) {
        
        ui = new textUI ();
        Game g = new Game ();
        ui.printWinner(g.run());
        
    }
    
    public void setUI (RomaUserInterface rUI) {
        
        ui = rUI;
        Cards.setUI (ui);
        
    }
    
    /**
     * Creates the players and dice.
     */
    public Game () {

        
        die = new Die[NUM_DICE];
        for (i = 0; i < NUM_DICE; i++) {

            die[i] = new Die();

        }
        
        players = new Player[NUM_PLAYERS];

        for (i = 0; i < NUM_PLAYERS; i++) {
            
            players[i] = new Player();
            
        }
        
        deck = new Deck (players, ui);
        deck.allocateCards();
        
        layStartCards();

    }
    
    public int run() {
        
        int input = 0;
        
        subStartingVP();
        
        while (rollDie()) { 
        }
        
        while (!endGame()) {
            
            preTurn();
            
            input = ui.pickAnInt(END_TURN);
            
            if (input == END_TURN) {
                
                turnNumber++;
                subStartingVP();
                while (rollDie()) { 
                }
                
            } else if (input == DRAW_CARD) {
                
                drawCard();
                
            } else if (input == BUY_MONEY) {
                
                ui.print("Enter the value (1 - " + NUM_SIDES_ON_DICE + ") of the die you wish to use to BUY MONEY");
                input = ui.pickAnInt(1);
                players[whoseTurn()].addMoney(input);
                
                for (i = 1; i < NUM_DICE; i++) {
                    
                    if (die[i].getValue() == input) {
                        
                        die[i].use();
                        i = NUM_DICE;
                        
                    }
                    
                }
                
            } else if (input == ACTIVATE_CARD) {
                
                activateCard();
                
            } else if (input == LAY_CARD) {
                
                layCard();
                
            } else if (input == READ_RULES) {
                
                ui.displayRules("Forum");
                
            }
            
        }
        
        
        return whoWon();
        
    }


    private void preTurn() {
        
        ui.displayBoard(players[0].getVictoryPoints(), players[1].getVictoryPoints(), 
                players[0].getMoney(), players[1].getMoney(), whoseTurn(), players[0].getCardsInPlay(),
                players[1].getCardsInPlay());
        
        ui.printDie(die);
        
        ui.print("Which action would you like to perform?");
        ui.print(Game.END_TURN + " - End turn");
        ui.print(Game.BUY_MONEY + " - Use a die to buy money");
        ui.print(Game.DRAW_CARD + " - Use a die to get more cards");
        ui.print(Game.ACTIVATE_CARD + " - Use a die to activate a card");
        ui.print(Game.LAY_CARD + " - Lay a card");
        ui.print(Game.READ_RULES + " - Read the rules");
        
    }


    private void activateCard() {
        
        int cardNum = 0;
        valid = false;
        
        while (!valid) {
        
            boolean usedDie = true;
            while (usedDie) {
                
                ui.print("Enter the value (1 - " + NUM_SIDES_ON_DICE + ") of the die you wish to use to ACTIVATE A CARD");
                input = ui.pickAnInt(1);
                
                // check that this die is unused
                for (i = 1; i < NUM_DICE; i++) {
                    
                    if ((die[i].getValue() == input) && (!die[i].isUsed())) {
                        
                        usedDie = false;
                        
                    }
                    
                }
                
                if (usedDie) {
                    
                    ui.printE("This Die has been used");
                    
                }
                
            }
            
            cardNum = validateActivate();
            
        }
        
        Cards[] cards = players[whoseTurn()].getCardsInPlay();
        
        if (cards[cardNum].isActivatable(cards[cardNum])) {
        
            cards[cardNum].activate(whoseTurn(), players, cards[cardNum], deck, cardNum, getDice());
            
            if (cardNum == 0) {
                
                players[whoseTurn()].addMoney(-input);
                
            }
            
            // use die
            for (i = 1; i < NUM_DICE; i++) {
                
                if (die[i].getValue() == input) {
                    
                    die[i].use();
                    i = NUM_DICE;
                    
                }
                
            }
            
        } else {
            
            ui.printE("You cannot activate this card!");
            
        }
    }

    private int validateActivate() {

        valid = false;
        int cardNum = -1;
        
        while (!valid) {
            
            ui.print("Enter the position (0 - " + NUM_SIDES_ON_DICE + ") of the card you wish to activate");
            cardNum = ui.pickAnInt(0);

            for (i = 0; (i < NUM_DICE) && (!valid); i++) {
            
                // checks that the position is a valid card
                if (players[whoseTurn()].getCardsInPlay()[cardNum] != Cards.NOTACARD) {
                    
                    // check that the die value matches the position
                    if (die[i].getValue() == input) {
                        
                        valid = true;
                        
                    } else if (input == 0) {
                        
                        // check player has at least as much money as the value of the die
                        // subtract the value
                        if (players[whoseTurn()].getMoney() < die[i].getValue()) {
                            
                            ui.printE ("ERROR: Not enough money to activate this card");
                            
                        } else {
                            
                            if (players[whoseTurn()].getCardsInPlay()[input] != Cards.NOTACARD) {
                                
                                valid = true;
                                
                            }
                            
                        }
                        
                    }
                    
                }
                
            }
            
            if (!valid) {
                
                ui.printE("Please match the value of the die to the position of a valid card, or activate a card on Disk 0");
                
            }
            
        }
        
        return cardNum;
        
    }


    private void drawCard() {
        
        valid = false;
        
        ui.print("Enter the value (1 - " + NUM_SIDES_ON_DICE + ") of the die you wish to use to DRAW A CARD");
        input = ui.pickAnInt(1);
        
        // draw #input cards
        // select which ONE to keep
        // discard others
        
        ui.print ("You drew:");
        
        Cards[] draw = new Cards[input];
        for (i = 0; i < draw.length; i++) {
            
            draw[i] = deck.drawCard();
            ui.print(draw[i].toString());
            
        }
        
        while (!valid) {
            
            ui.print("\nWhich card would you like to keep?");
            Cards c = ui.pickACard();
            
            for (i = 0; i < draw.length; i++) {
                
                if (c == draw[i]) {
                    
                    players[whoseTurn()].addCard(draw[i]);
                    draw[i] = Cards.NOTACARD;
                    i = draw.length;
                    valid = true;
                    
                }
            
            }
            
            if (!valid) {
                
                ui.printE("Invalid Card Name");
                
            }
            
        }
        
        for (i = 1; i < NUM_DICE; i++) {
            
            if (die[i].getValue() == input) {
                
                die[i].use();
                i = NUM_DICE;
                
            }
            
        }
        
    }


    private void layCard() {
        
        Cards[] hand = players[whoseTurn()].getHand();
        Cards lay = Cards.NOTACARD;
        boolean correctCard = false;
        
        ui.print("Your hand contains:");
        
        for (i = 0; i < hand.length; i++) {

            if (hand[i] != Cards.NOTACARD) {
                
                ui.print(hand[i].toString());
                
            }
        }
        
        // get the name of the card
        while (!correctCard) {
            
            ui.print("Type in the name of the card you wish to lay");
            lay = ui.pickACard();
            
            for (i = 0; i < hand.length; i++) {
                
                if (lay == hand[i]) {
                    
                    correctCard = true;
                    i = hand.length;
                    
                }
                
            }
            
            if ((!correctCard) || (lay.equals("NOTACARD"))) {
            
                ui.print ("Invalid Card Name");
                correctCard = false;
            
            }
            
        }
        
        // check that they have enough money
        if (players[whoseTurn()].getMoney() < lay.getMoneyCost()) {
            
            ui.printE("Not enough money to lay this card");
            return;
            
        }
        
        // get the position they wish to lay in
        int position = -1;
        
        while ((position < 0) || (position > 6)) {
            
            ui.print("Where would you like to lay this card?");
            position = ui.pickAnInt(0);
        
            if ((position < 0) || (position > 6)) {
                
                ui.printE("Invalid position");
                
            }
            
        }
        
        players[whoseTurn()].addMoney(-lay.getMoneyCost());
        players[whoseTurn()].layCard(lay, position);
        
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
        
        for (i = 0; i < Game.NUM_DICE; i++) {

            die[i].rollDie();

        }
        
        for (i = 1; (i < NUM_DICE - 1) && (sameDie); i++) {
            
            if (die[i].getValue() != die[i + 1].getValue()) {
                
                sameDie = false;
                
            }
            
        }
        
        if (sameDie) {
            
            ui.printDie(die);
            ui.print("Do you want to re-roll? (Type 'y' for yes)");
            if (ui.getString().equals("y")) {
                
                reRoll = true;
                
                
            }
            
        }
        
        
        return reRoll;
        
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
        
        } else {
            
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
            
            if ((value == die[i].getValue()) && (die[i].isUsed() == false)) {
        
                valid = true;
                die[i].use();
        
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
            
            ui.print("PLAYER " + (i + 1) + "'s hand:");
            
            for (j = 0; j < Deck.NUM_START_CARDS; j++) {
                
                ui.print(players[i].getHand()[j].toString());
                
            }
            
            for (j = 0; j < Deck.NUM_START_CARDS; j++) {

                ui.print("Where do you want to lay your " + players[i].getHand()[j] + "?");
                position = ui.pickAnInt(0);
                players[i].layCard(players[i].getHand()[j], position);
                
            }
            
            if (i < NUM_PLAYERS - 1) {
                
                ui.print("Player " + (i + 1) + ":");
                for (k = 0; k <= Game.NUM_SIDES_ON_DICE; k++) {
                
                    ui.printRaw("(" + k + ") ");
                    
                    temp1 = players[i].getCardsInPlay()[k];
                    
                    if (temp1 != Cards.NOTACARD){
                        
                        ui.print("**CARD**");
                        
                    } else {
                        
                        ui.print("");
                        
                    }
                    
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