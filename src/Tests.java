/** Tests.java
 * By Samuel Baxter and Theo Tse
 * COMP2911
 * 13/03/2012
 */


// NOTE: dont roll a triple, it crashes

public class Tests {
    
    public static void main(String[] args) {
        
        System.out.println ("Starting Roma Tests....");
        
        dieTests();
        playerTests();
        gameTests();
        mercatorTests();
        forumTests();
        tribunusPlebisTests();
        
        System.out.println ("All tests passed, you are awesome!");

    }
    
    private static void dieTests() {
        
        final int NUM_SIDES_ON_DICE = 6;
        
        System.out.println("Starting Die tests");
        
        final int NUM_TESTS = 1000;
        
        Die die = new Die();
        assert (die != null);
        int i;
        int result;
        
        for (i = 0; i < NUM_TESTS; i++) {
            
            die.rollDie();
            result = die.getValue();
            assert (result >= 1);
            assert (result <= NUM_SIDES_ON_DICE);
            // System.out.println (result);
            
        }
        
        System.out.println ("Passed!");
        
    }
    
    private static void playerTests() {
        
        System.out.println("Starting Player tests");
        
        // no cards, so passing in null (change later)
        Player test = new Player();
        assert (test != null);
        
        // test creation data is correct
        assert (test.getVictoryPoints() == Game.START_VICTORY_POINTS);
        assert (test.getMoney() == Game.START_MONEY);
        
        // add some victory points
        test.addVictoryPoints(3);
        assert (test.getVictoryPoints() == (Game.START_VICTORY_POINTS + 3));
        
        // add some money
        test.addMoney(5);
        assert (test.getMoney() == (Game.START_MONEY + 5));
        
        // subtract some money
        test.addMoney(-2);
        assert (test.getMoney() == (Game.START_MONEY + 3));
        
        // subtract some VP
        test.addVictoryPoints(-4);
        assert (test.getVictoryPoints() == (Game.START_VICTORY_POINTS - 1));
        
        // lay cards, addcard, getPosition
        
        System.out.println ("Passed!");
        
    }
    
    public static void gameTests () {
        
        int i;
        System.out.println("Starting Game tests");
        // display some rules (requires manual checking that output works)
        /*
        Game.displayRules("Sicarius");
        Game.displayRules("Cards");
        Game.displayRules("Phase 3");
        */
        
        //test endGame()
        // whoWon()
        // whoseTurn();
        
        Game g = new Game();
        //g.setUI (null);
        g.rollDie();
        assert (g.whoseTurn() == 0);
        assert (g.players.length == Game.NUM_PLAYERS);
        for (i = 0; i < Game.NUM_PLAYERS; i++) {
            
            //assert (g.players[i].getHand().length == Game.NUM_CARDS);
        
        }
        
        g.players[0].addMoney(20);
        assert (g.players[0].getMoney() == 20);
        assert (g.players[1].getMoney() == 0);
        g.players[1].addVictoryPoints(20);
        assert (g.players[1].getVictoryPoints() == 30);
        assert (g.players[0].getVictoryPoints() == 10);
        
        assert (g.whoWon() == 2);
        
        
        //assert (g.cards.length == Game.NUM_CARDS);
        
        
        System.out.println ("Passed!");
        
    }
    
    public static void mercatorTests() {
        
        System.out.println ("Starting Mercator Tests");
        
        Cards c = Cards.MERCATOR;
        assert (c.getDefenseValue() == 2);
        assert (c.getMoneyCost() == 7);
        assert (c.getType() == Card.CHARACTER);
        
        
        System.out.println ("Passed!");
        
        
    }
    
    public static void forumTests() {
        
        System.out.println ("Starting Forum Tests");
        
        Cards c = Cards.FORUM;
        assert (c.getDefenseValue() == 5);
        assert (c.getMoneyCost() == 5);
        assert (c.getType() == Card.BUILDING);
        
        
        System.out.println ("Passed!");
        
        
    }
    
    public static void tribunusPlebisTests() {
        
        System.out.println ("Starting Tribunus Plebis Tests");
        
        Cards c = Cards.TRIBUNUSPLEBIS;
        assert (c.getDefenseValue() == 5);
        assert (c.getMoneyCost() == 5);
        assert (c.getType() == Card.CHARACTER);
        
        Game g = new Game();
        //g.setUI(null);
        c.activate(0, g.players, c, g.getDeck(), 0, g.getDice());
        assert (g.players[0].getVictoryPoints() == 11);
        assert (g.players[1].getVictoryPoints() == 9);
        c.activate(0, g.players, c, g.getDeck(), 0, g.getDice());
        assert (g.players[0].getVictoryPoints() == 12);
        assert (g.players[1].getVictoryPoints() == 8);
        c.activate(1, g.players, c, g.getDeck(), 0, g.getDice());
        assert (g.players[0].getVictoryPoints() == 11);
        assert (g.players[1].getVictoryPoints() == 9);
        c.activate(1, g.players, c, g.getDeck(), 0, g.getDice());
        assert (g.players[0].getVictoryPoints() == 10);
        assert (g.players[1].getVictoryPoints() == 10);
        
        System.out.println ("Passed!");
        
        
    }

}
