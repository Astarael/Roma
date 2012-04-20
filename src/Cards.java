import java.util.Scanner;

/* Cards.java
 * By Samuel Baxter
 * COMP2911
 * 11/04/2012
 */

public enum Cards {
    
    MERCATOR (1, 7, 2),
    TRIBUNUSPLEBIS (1, 5, 5),
    FORUM (0, 5, 5),
    SICARIUS (1, 9, 2),
    LEGAT (1, 5, 2),
    NERO (0, 8, 9),
    MERCATUS (0, 6, 3),
    BASILICA (0, 6, 5),
    TEMPLUM (0, 2, 2),
    NOTACARD (-1 ,0 ,0);
    
    public final static int BUILDING = 0;
    public final static int CHARACTER = 1;
    
    private int type = 0;
    private int moneyCost = 0;
    private int defenseValue = 0;
    
    Cards (int type, int cost, int defense) {
        
        this.type = type;
        this.moneyCost = cost;
        this.defenseValue = defense;
        
    }
    
    public void activate (int whoseTurn, Player[] players, Cards c, Deck deck, int position, Die[] dice) {
        
        int i;
        
        if (c == MERCATOR) {
            
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
            
        } else if (c == TRIBUNUSPLEBIS) { 
            
            players[whoseTurn].addVictoryPoints(1);
            players[(whoseTurn + 1) % 2].addVictoryPoints(-1);
            
        } else if (c == FORUM) {
            
            int basilica = 0;
            boolean templum = false;
            
            players[whoseTurn].addVictoryPoints(Interface.getDieInput("Activate Forum and get the victory points"));
            
            if (position == 6) {
                
                if (players[whoseTurn].getCardsInPlay()[position - 1] == BASILICA) {
                    
                    basilica++;
                    
                }
                
                if (players[whoseTurn].getCardsInPlay()[position - 1] == TEMPLUM) {
                    
                    templum = true;
                    
                }
                
            } else {
                
                if (players[whoseTurn].getCardsInPlay()[position - 1] == BASILICA) {
                    
                    basilica++;
                    
                }
                
                if (players[whoseTurn].getCardsInPlay()[position + 1] == BASILICA) {
                    
                    basilica++;
                    
                }
                
                if ((players[whoseTurn].getCardsInPlay()[position - 1] == TEMPLUM) ||
                        (players[whoseTurn].getCardsInPlay()[position + 1] == TEMPLUM)) {
                    
                    templum = true;
                    
                }
                
            }
                
            players[whoseTurn].addVictoryPoints(2 * basilica);
            
            if (templum) {
                
                if (Interface.getInput("Do you wish to use the Templum?").equals("y")) {
                    
                    int value = -1;
                        
                        for (i = 0; i < dice.length; i++) {
                            
                            if (dice[i].used == false) {
                                
                                value = dice[i].getValue();
                                dice[i].used = true;
                                
                            }
                            
                        }
                
                    if (value != -1) {
                        
                        players[whoseTurn].addVictoryPoints(value);
                        
                    } else {
                        
                        Interface.printE("All dice are already used\n");
                        
                    }
                    
                }
                
            }
            
            
        } else if (c == SICARIUS) {
            
            int place = 0;
            
            while (place == 0) {
                
                place = Interface.getActionInput("Enter the positon of the character card you wish to eliminate");
                
                if (players[(whoseTurn + 1) % Game.NUM_PLAYERS].getCardsInPlay()[place].getType() != CHARACTER) {
                    
                    place = 0;
                    Interface.printE("ERROR: Invalid Character Card");
                    
                }
                
            }
            
            
            deck.discardCard(c);
            players[whoseTurn].layCard(NOTACARD, position);
            
            c = players[(whoseTurn + 1) % Game.NUM_PLAYERS].getCardsInPlay()[place];
            deck.discardCard(c);
            players[(whoseTurn + 1) % Game.NUM_PLAYERS].layCard(NOTACARD, place);
            
        } else if (c == LEGAT) {
            
            int counter = 0;
            
            // subtract 1 VP for each unoccupied dice disk
            for (i = 1; i <= Game.NUM_SIDES_ON_DICE; i++) {
                
                if (players[whoseTurn + 1 % Game.NUM_PLAYERS].getCardsInPlay()[i] == Cards.NOTACARD) {
                    
                    counter++;
                    
                }
                
            }
            
            players[whoseTurn].addVictoryPoints(counter);
            
        } else if (c == NERO) {
            
            int place = 0;
            
            while (place == 0) {
                
                place = Interface.getActionInput("Enter the positon of the building card you wish to eliminate");
                
                if (players[(whoseTurn + 1) % Game.NUM_PLAYERS].getCardsInPlay()[place].getType() != BUILDING) {
                    
                    place = 0;
                    Interface.printE("ERROR: Invalid Character Card");
                    
                }
                
            }
            
            
            deck.discardCard(c);
            players[whoseTurn].layCard(NOTACARD, position);
            
            c = players[(whoseTurn + 1) % Game.NUM_PLAYERS].getCardsInPlay()[place];
            deck.discardCard(c);
            players[(whoseTurn + 1) % Game.NUM_PLAYERS].layCard(NOTACARD, place);
            
        } else if (c == MERCATUS) {
            
            int counter = 0;
            
            for (i = 0; i <= Game.NUM_SIDES_ON_DICE; i++) {
                
                if (players[whoseTurn + 1 % Game.NUM_PLAYERS].getCardsInPlay()[i] == FORUM) {
                    
                    counter++;
                    
                }
                
            }
            
            players[whoseTurn].addVictoryPoints(counter);
            
            
        }
        
        
    }
    
    public int getMoneyCost() {
        
        return moneyCost;
        
    }

    public int getDefenseValue() {
        
        return defenseValue;
        
    }
    
    public int getType() {
        
        return type;
        
    }

}
