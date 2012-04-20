/** Mercator.java
 * By Samuel Baxter and Theo Tse
 * COMP2911
 * 24/03/2012
 */

public class Mercator extends Card {
    
    public Mercator() {
        moneyCost = 7;
        defenseValue = 2;
        type = CHARACTER;
    }
    
    
    public void action(int whoseTurn, Player[] players) {
        int money = players[whoseTurn].getMoney();
        int victoryPoints = players[(whoseTurn + 1) % 2].getVictoryPoints();
        int value= -1;
        
        if (money > 0 && victoryPoints > 0) {
            money = money / 2;
            while (value < 0) {
            
                value = Interface.getActionInput("Please enter the number of victory points you want to buy");
                if (value <= money) {
                    players[whoseTurn].addVictoryPoints(value);
                    players[whoseTurn].addMoney(-money * 2);
                
                    players[(whoseTurn + 1) % 2].addVictoryPoints(-value);
                    players[(whoseTurn + 1) % 2].addMoney(money * 2);
                    
                } else  {
                    
                    value = -1;
                    Interface.printE ("Invalid Input");
                }
                
            }
        }
    }
    
    /*public String toString () {
        
        return "MERCATOR - M:" + moneyCost + " D:" + defenseValue;
        
    }*/
}
