/** Player.java
 * By Samuel Baxter and Theo Tse
 * COMP2911
 * 13/03/2012
 */

public class Player {
    
    private int victoryPoints;
    private int money;
    private Cards[] hand;
    private Cards[] cardsInPlay;
    
    /**
     * Player class for Roma. Stores the player's current stats (money, cards, vp)
     */
    
    
    public Player () {
        
        int i;
        
        this.victoryPoints = Game.START_VICTORY_POINTS;
        this.money = Game.START_MONEY;
        this.hand = new Cards[Deck.NUM_CARDS];
        
        for (i = 0; i < Deck.NUM_CARDS; i++) {
            
            hand[i] = Cards.NOTACARD;
            
        }
        
        // this array is 7 long, the 0th is blank
        cardsInPlay = new Cards[Game.NUM_SIDES_ON_DICE + 1];
        
        for (i = 0; i < Game.NUM_SIDES_ON_DICE + 1; i++) {
            
            cardsInPlay[i] = Cards.NOTACARD;
            
        }
        
        /**
         * Create a Player. cards is an ArrayList of cards
         */
        
    }
    
    
    public int getVictoryPoints () {
        
        return this.victoryPoints;
        
    }
    
    public int getMoney() {
        
        return this.money;
        
    }
    
    public Cards[] getHand() {
        
        
        return this.hand;
        
    }
    
    public void addVictoryPoints (int value) {
        
        if (value > Game.MAX_VICTORY_POINTS) {
            
            Interface.printE ("ERROR: Invalid number of Victory Points");
            // exit
            
        }
        
        this.victoryPoints += value;
        
        /**
         * Add or subtract an integer value of Victory Points.
         */
        
    }
    
    public void addMoney (int value) {
        
        this.money += value;
        
        /**
         * Add or subtract an integer value of money.
         */
        
    }
    
    public void addCard (Cards draw) {
        
        int i;
        for (i = 0; i < hand.length; i++) {
            
            if (hand[i] == Cards.NOTACARD) {
                
                hand[i] = draw;
                i = hand.length;
                
            }
            
        }
        
    }
    
    public Cards[] getCardsInPlay () {
        
        return cardsInPlay;
        
    }
    
    public Cards layCard (Cards c, int position) {
        
        Cards temp = cardsInPlay[position];
            
        cardsInPlay[position] = c;
        
        int i;
        
        for (i = 0; i < hand.length; i++) {
            
            if (hand[i] == c) {
                
                hand[i] = Cards.NOTACARD;
                
            }
            
        }
        
        
        return temp;
        
    }


    public int getPosition (Cards c) {
        
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

}
