import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/* textUI.java
 * By Samuel Baxter
 * COMP2911
 * 04/05/2012
 */

public class textUI implements RomaUserInterface {
    
    private Scanner sc;
    private int i;
    private boolean valid = false;
    
    public textUI () {
        
        sc = new Scanner (System.in);
        
    }

    
    public void displayBoard(int player1VP, int player2VP, int player1M,
            int player2M, int turn, Cards[] p1Cards, Cards[] p2Cards) {
        
        print ("Player 1\t\t\tPlayer 2");
        
        turn++;
        printCards(p1Cards, p2Cards);

        print("");
        printStats(player1VP, player2VP, player1M, player2M);
        print("");
        
        print("It is Player " + turn + "'s turn");

    }

    private void printCards(Cards[] p1Cards, Cards[] p2Cards) {
        
        for (i = 0; i <= Game.NUM_SIDES_ON_DICE; i++) {
            
            // formatting somehow
            System.out.print("\t");
            if (p1Cards[i] != Cards.NOTACARD){
                
                System.out.print(p1Cards[i] + "\t");
                
            } else {
                
                System.out.print("\t");
                
            }
                
            System.out.print("   \t(" + i + ")\t");
            
            if (p2Cards[i] != Cards.NOTACARD){
                
                System.out.print("\t" + p2Cards[i]);
                
            } else {
                
                System.out.print("\t");
                
            }
            
            print("");

        }
        
    }
    
    private void printStats(int player1VP, int player2VP, int player1M,
            int player2M) {
        
        print("\t   " + player1M + "\t      Money\t   " + player2M);
        print("\t   " + player1VP + "\t  Victory Points   " + player2VP);
        
    }
    
    public void printDie(Die[] die) {
        
        print ("You Rolled:");
        
        for (i = 1; i < Game.NUM_DICE; i++) {
            
            if (die[i].isUsed() == false) {
                
                System.out.print(die[i].getValue() + "\t");
                
            } else {
                
                System.out.print ("\t");
            }
            
        }
        
        print("");
        
    }

    
    public Cards pickACard() {
        
        valid = false;
        String s = "";
        
        while (!valid) {
            
            s = sc.next();
            s = s.toUpperCase();
            
            if (Cards.isCard(s)) {
                
                valid = true;
                
            } else {
                
                printE ("Invalid Card Name");
                
            }
            
        }
        
        
        return Cards.valueOf(s);
    }

    
    public int pickAnInt(int start) {
        
        int temp = 0;
        valid = false;
        
        while (!valid) {
        
            temp = sc.nextInt();
            
            if ((temp >= start) && (temp <= Game.NUM_SIDES_ON_DICE)) {
                
                valid = true;
                
            } else {
                
                printE ("Please enter a number between " + start + " and " + Game.NUM_SIDES_ON_DICE +" inclusive");
                
            }
            
        }
        
        
        return temp;
    }
    
    public String getString () {
        
        return sc.next();
        
    }

    /**
     * String s is printed using printf
     */
    public void print(String s) {
        
        System.out.printf (s + "\n");

    }
    
    /**
     * Print with no formatting applied
     */
    public void printRaw (String s) {
        
        System.out.print(s);
        
    }

    /**
     * String s is printed using printf
     */
    public void printE(String s) {
        
        System.err.printf (s + "\n");

    }

    
    public void printWinner(int whoWon) {
        
        print("---------------------------");
        print("|        Game ended       |");
        print("|       Player " + whoWon + " won      |");
        print("---------------------------");

    }
    
    
    
    
    // calling function will ask for another filename
    public void displayRules (String name) {

        // check that name is in file list
        String[] list = new String[100];
        int i;
        boolean fileInList = false;

        try {

            list = getFileList();

        } catch (IOException e) {

            System.out.println ("ERROR READING FILE LIST");
            e.printStackTrace();

        }
        
        for (i = 0; (i < 100) && (fileInList == false); i++) {

            if (list[i].equals(name)) {

                fileInList = true;

            }

        }

        if (fileInList) {

            try {

                printFile (name);

            } catch (IOException e) {

                System.out.println("ERROR READING FILE '" + name + "'");

            }

        } else {

            System.out.println ("File '" + name + "' does not exist");

        }

    }

    // 'Thinking in Java' by Bruce Eckel
    private void printFile (String filename) throws IOException {

        BufferedReader in = null;
        in = new BufferedReader (new FileReader("rules/" + filename));

        String s;
        StringBuilder sb = new StringBuilder();
        while ((s = in.readLine()) != null) {

            sb.append(s + "\n");

        }

        in.close();

        System.out.println (sb.toString());
        //wait?

    }

    private String[] getFileList () throws IOException {

        BufferedReader in = null;
        in = new BufferedReader (new FileReader("rules/list"));

        String s;
        String[] list = new String[100];
        int i = 0;
        while ((s = in.readLine()) != null) {

            list[i] = s;
            i++;

        }

        in.close();


        return list;

    }
    
}