/** Forum.java
 * By Samuel Baxter and Theo Tse
 * COMP2911
 * 27/03/2012
 */

public class Forum extends Card {

    
    public void action(int whoseTurn, Player[] players) {
        players[whoseTurn].addVictoryPoints(Interface.getDieInput("Activate Forum and get the victory points"));
    }
        
    public Forum() {
        
        moneyCost = 5;
        defenseValue = 5;
        type = BUILDING;
        
    }

}
