/** RomaUserInterface.java
 * By Samuel Baxter and Theo Tse
 * COMP2911
 * 04/05/2012
 */

public interface RomaUserInterface {
    
    public void displayBoard (int player1VP, int player2VP, int player1M, int player2M,
            int turn, Cards[] p1Cards, Cards[] p2Cards);
    public void printDie (Die[] die);
    /**
     * Only checks that the string that is entered is a valid card name
     */
    public Cards pickACard ();
    /**
     * Returns an int between 0 and 6 inclusive.
     */
    public int pickAnInt (int start);
    public String getString ();
    public void print (String s);
    public void printRaw (String s);
    public void printE (String s);
    /**
     * int whoWon is an integer from 0 .. (NUM_PLAYERS - 1)
     */
    public void printWinner (int whoWon);
    public void displayRules (String s);

}
