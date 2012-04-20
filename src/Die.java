/** Dice.java
 * By Samuel Baxter and Theo Tse
 * COMP2911
 * 08/03/2012
 */


public class Die {
    
    private int value;
    public boolean used = false;
    
    
    public Die () {
        
        this.value = 0;
        
        /** 
         * Creates a 6-sided die
         */
        
    }
    
    public void rollDie () {
        
        // Math.random returns a double [0-1] so * 100 % 6 + 1
        this.value = (int) (Math.random() * 100) % Game.NUM_SIDES_ON_DICE + 1;
        
        /** 
         * Generates a random value (using math.random) between 1 and 6 inclusive
         */
        
        
    }
    
    public int getValue () {
        
        return this.value;
        
        /**
         * Returns the value that rollDie() randomed
         */
        
    }

}
