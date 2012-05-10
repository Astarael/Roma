package test;

import userInterface.*;
import game.*;
import cards.*;

/** testUI.java
 * By Samuel Baxter
 * COMP2911
 * 10/05/2012
 */

public class testUI implements RomaUserInterface {

    public void displayBoard(int player1vp, int player2vp, int player1m,
            int player2m, int turn, Cards[] p1Cards, Cards[] p2Cards) {
        // TODO Auto-generated method stub

    }

    public void printDie(Die[] die) {
        

    }

    public Cards pickACard() {
        
        return null;
    }

    public int pickAnInt(int start) {
        // TODO Auto-generated method stub
        return 0;
    }

    public String getString() {
        // TODO Auto-generated method stub
        return null;
    }

    public void print(String s) {
        

    }

    public void printRaw(String s) {
        

    }

    public void printE(String s) {
        System.err.println (s);

    }

    public void printWinner(int whoWon) {
        System.out.println ("Tests Concluded");

    }

    public void displayRules(String s) {
        

    }

}
