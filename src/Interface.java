import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/** Interface.java
 * By Samuel Baxter and Theo Tse
 * COMP2911
 * 22/03/2012
 */

public class Interface {
    
    private static Scanner sc;
    public static int input = -1;
    private static int i;
    private static Game g;
    
    
    public static void main (String[] args) {
        
        sc = new Scanner(System.in);
        g = new Game();
        g.run();
        System.out.print("\n\n");
        printWinner(g.whoWon());
        

        
    }
    
    public static void printWinner(int winner) {
        
        System.out.println("---------------------------");
        System.out.println("|        Game ended       |");
        System.out.println("|       Player " + g.whoWon() + " won      |");
        System.out.println("---------------------------");
        
    }
    
    public static void printDie(Die[] die) {
        
        System.out.println("You Rolled:");
        
        for (i = 1; i < Game.NUM_DICE; i++) {
            
            if (die[i].isUsed() == false) {
                
                System.out.print(die[i].getValue() + "\t");
                
            }
            
        }
        
        System.out.println();
        
    }
    
    public static int requestInput () {
        
        input = -1;

        while ((input < Game.END_TURN) || (input > Game.READ_RULES)) {

            System.out.println("Which action would you like to perform?");
            System.out.println(Game.END_TURN + " - End turn");
            System.out.println(Game.BUY_MONEY + " - Use a die to buy money");
            System.out.println(Game.DRAW_CARD + " - Use a die to get more cards");
            System.out.println(Game.ACTIVATE_CARD + " - Use a die to activate a card");
            System.out.println(Game.LAY_CARD + " - Lay a card");
            System.out.println(Game.READ_RULES + " - Read the rules");
            input = sc.nextInt();

            if ((input < Game.END_TURN) || (input > Game.READ_RULES)) {

                System.out.println ("Invalid Action");

            }

        }


        return input;

    }
    
    public static void updateBoard(int player1VP, int player2VP, int player1M, int player2M,
            int turn, Cards[] cards, Cards[] cards2, Die[] die) {

        System.out.println("Player 1\t\t\tPlayer 2");

        int i;
        Cards temp1;
        Cards temp2;
        turn++;

        for (i = 0; i <= Game.NUM_SIDES_ON_DICE; i++) {
            
            temp1 = g.players[0].getCardsInPlay()[i];
            temp2 = g.players[1].getCardsInPlay()[i];
            
            // formatting somehow
            System.out.print("\t");
            if (temp1 != Cards.NOTACARD){
                
                System.out.print(temp1 + "\t");
                
            } else {
                
                System.out.print("\t");
                
            }
                
            System.out.print("   \t(" + i + ")\t");
            
            if (temp2 != Cards.NOTACARD){
                
                System.out.print("\t" + temp2);
                
            } else {
                
                System.out.print("\t");
                
            }
            
            System.out.println();
            //System.out.print("\t  " + temp1 + "    (" + i + ")\t  " + temp2);

        }

        //System.out.println("\t\t     (Money)");
        System.out.println();
        System.out.println("\t   " + player1M + "\t      Money\t   " + player2M);
        System.out.println("\t   " + player1VP + "\t  Victory Points   " + player2VP);
        
        System.out.println();
        System.out.println("It is Player " + turn + "'s turn");
        
        printDie(die);


    }
    
    
    public static int getDieInput(String action) {
        
        input = -1;
        boolean valid = false;
        
        while ((input < 0) || (input > Game.NUM_SIDES_ON_DICE) || (valid == false)) {
            
            System.out.println ("Enter the value (1 - " + Game.NUM_SIDES_ON_DICE + ") of the die you wish to use to " + action);
            input = sc.nextInt();
            
            valid = g.useDie(input);
            
            if (valid == false) {
            
                System.out.println ("Invalid die number");
                
            }
            
            if (input == 0){
                
                break;
                
            }
            
        }
        
        return input;
        
    }
    
    
    public static String getInput (String s) {
        
        System.out.println(s);
        s = sc.next();
        
        return s;
        
    }
    
    public static int getActionInput (String string) {
        System.out.println(string);
        return sc.nextInt();
    }
    
    public static void printE (String s) {
        
        System.err.printf (s + "\n");
        
    }
    
    public static void print (String s) {
        
        System.out.printf (s + "\n");
        
    }
    
    public static int pickACard() {
        
        int temp = 0;
        int i = 0;
        boolean validDieNum = false;
        
        while (validDieNum == false) {
            
            System.out.println ("Enter the dice disk number of the card you wish to use");
        
            temp = sc.nextInt();
            Die[] die = g.getDice();
            
            if ((temp >= 0) && (temp <= 6)) {
                
                for (i = 0; (i < Game.NUM_DICE) && (!validDieNum); i++) {
                    
                    if (die[i].getValue() == temp) {
                        
                        if (g.players[g.whoseTurn()].getCardsInPlay()[temp] != Cards.NOTACARD) {
                            
                            validDieNum = true;
                            
                        }
                        
                    }
                    
                    else if (temp == 0) {
                        
                        // check player has at least as much money as the value of the die
                        // subtract the value
                        if (g.players[g.whoseTurn()].getMoney() < die[i].getValue()) {
                            
                            printE ("ERROR: Not enough money to activate this card");
                            
                        } else {
                            
                            if (g.players[g.whoseTurn()].getCardsInPlay()[temp] != Cards.NOTACARD) {
                                
                                validDieNum = true;
                                g.players[g.whoseTurn()].addMoney(-die[i].getValue());
                                
                            }
                            
                        }
                        
                    }
                    
                }
                
            }
            
            if (!validDieNum) {
                    
                System.err.println ("Invalid Disk Number");
                
            }
                
        }
        
        
        return temp;
        
    }
    
    // calling function will ask for another filename
    public static void displayRules (String name) {

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
    private static void printFile (String filename) throws IOException {

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

    private static String[] getFileList () throws IOException {

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
