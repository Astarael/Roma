/** Card.java
 * By Samuel Baxter and Theo Tse
 * COMP2911
 * 13/03/2012
 */

abstract public class Card {
    
    protected final static int BUILDING = 0;
    protected final static int CHARACTER = 1;
    
    public int type = 0;
    public int diceDisk = 0;
    public int moneyCost = 0;
    public int defenseValue = 0;
    
    // effect for each class
    public abstract void action(int whoseTurn, Player[] players);
    
    public int getMoneyCost() {
        return moneyCost;
    }

    public int getDefenseValue() {
        return defenseValue;
    }
    
    public int getType() {
        return type;
    }
    
    public String toString() {
        
        return this.getClass().getName();
        
    }
    

}
