/** TribunusPlebis.java
 * By Samuel Baxter and Theo Tse
 * COMP2911
 * 24/03/2012
 */

public class TribunusPlebis extends Card {
    
    public TribunusPlebis() {
        moneyCost = 5;
        defenseValue = 5;
        type = CHARACTER;
    }
    
    public void action(int whoseTurn, Player[] players) {
        players[whoseTurn].addVictoryPoints(1);
        players[(whoseTurn + 1) % 2].addVictoryPoints(-1);
    }
    

}
