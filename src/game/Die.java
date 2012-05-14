package game;


/** Dice.java
 * By Samuel Baxter and Theo Tse
 * COMP2911
 * 08/03/2012
 */

public class Die {
    
    private int value;
    private boolean used = false;
    
    /** 
     * Creates a 6-sided die
     */
    public Die () {
        
        this.value = 0;
        this.used = false;
        
    }
    
    
    /** 
     * Generates a random value (using math.random) between 1 and 6 inclusive
     */
    public void rollDie () {
        
        // Math.random returns a double [0-1] so * 100 % 6 + 1
        this.value = (int) (Math.random() * 100) % Game.NUM_SIDES_ON_DICE + 1;
        this.used = false;
        
    }
    
    /**
     * Returns the value that rollDie() randomed
     */
    public int getValue () {
        
        return value;
        
    }
    
    public boolean isUsed () {
        
        return used;
        
    }
    
    public void use () {
        
        used = true;
    }


    public void setValue(int i) {
        
        value = i;
        
    }

}
