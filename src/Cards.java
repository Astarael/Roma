/* Cards.java
 * By Samuel Baxter
 * COMP2911
 * 11/04/2012
 */

public enum Cards {
    
    AESCULAPINUM (0, 5, 2),
    ARCHITECTUS (1, 3, 4),
    BASILICA (0, 6, 5),
    CONSILIARIUS (1, 4, 4),
    FORUM (0, 5, 5),
    GLADIATOR (1, 6, 5),
    LEGAT (1, 5, 2),
    MACHINA (0, 4, 4),
    MERCATOR (1, 7, 2),
    MERCATUS (0, 6, 3),
    NERO (0, 8, 9),
    SENATOR (1, 3, 3),
    SICARIUS (1, 9, 2),
    TEMPLUM (0, 2, 2),
    TRIBUNUSPLEBIS (1, 5, 5),
    TURRIS (0, 6, 6),
    NOTACARD (-1, 0, 0);
    
    public final static int BUILDING = 0;
    public final static int CHARACTER = 1;
    
    private int type = 0;
    private int moneyCost = 0;
    private int defenseValue = 0;
    
    private int whoseTurn;
    private Player[] players;
    private Deck deck;
    
    Cards (int type, int cost, int defense) {
        
        this.type = type;
        this.moneyCost = cost;
        this.defenseValue = defense;
        
    }
    
    public void activate (int w, Player[] p, Cards c, Deck d, int position, Die[] dice) {
        
        whoseTurn = w;
        players = p;
        deck = d;
        
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
                
            } else if (position == 0) {
                
                if (players[whoseTurn].getCardsInPlay()[position + 1] == BASILICA) {
                    
                    basilica++;
                    
                }
                
                if (players[whoseTurn].getCardsInPlay()[position + 1] == TEMPLUM) {
                    
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
                
                if (Interface.getInput("Do you wish to use the Templum?").equals("yes")) {
                    
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
                        
                        Interface.printE("All dice are already used");
                        
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
            
            
            deck.discardCard(c, players[whoseTurn].getCardsInPlay());
            
            c = players[(whoseTurn + 1) % Game.NUM_PLAYERS].getCardsInPlay()[place];
            deck.discardCard(c, players[(whoseTurn + 1) % Game.NUM_PLAYERS].getCardsInPlay());
            
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
            
            int place = -1;
            
            while (place == -1) {
                
                place = Interface.getActionInput("Enter the positon of the building card you wish to eliminate");
                
                if (players[(whoseTurn + 1) % Game.NUM_PLAYERS].getCardsInPlay()[place].getType() != BUILDING) {
                    
                    place = -1;
                    Interface.printE("ERROR: Invalid Character Card");
                    
                }
                
            }
            
            // remove the nero from the players cards
            deck.discardCard(c, players[whoseTurn].getCardsInPlay());
            
            // remove the card the nero destroyed
            c = players[(whoseTurn + 1) % Game.NUM_PLAYERS].getCardsInPlay()[place];
            deck.discardCard(c, players[(whoseTurn + 1) % Game.NUM_PLAYERS].getCardsInPlay());
            
        } else if (c == MERCATUS) {
            
            int counter = 0;
            
            for (i = 0; i <= Game.NUM_SIDES_ON_DICE; i++) {
                
                if (players[whoseTurn + 1 % Game.NUM_PLAYERS].getCardsInPlay()[i] == FORUM) {
                    
                    counter++;
                    
                }
                
            }
            
            players[whoseTurn].addVictoryPoints(counter);
            
            
        } else if (c == ARCHITECTUS) {
            
            // print out their hand
            // check card is a building card
            // select which cards to lay
            // lay card
            // refund cost
            
            boolean finished = false;
            Cards[] hand = players[whoseTurn].getHand();
            Interface.print("Your Hand:");
            String card = "";
            position = 0;
            
            for (i = 0; i < hand.length; i++) {
                
                if (hand[i].getType() == BUILDING) {
                
                    Interface.print(hand[i].toString());
                
                }
                
            }
            
            while (!finished) {
                
                card = Interface.getInput("Which card would you like to lay?");
                card = card.toUpperCase();
                
                while ((position < 0) || (position > 6)) {
                    
                    position = Interface.getActionInput("Where would you like to place this card?");
                    
                    if ((position < 0) || (position > 6)) {
                        
                        Interface.printE("Invalid Position");
                        
                    }
                        
                }
                
                players[whoseTurn].layCard(valueOf(card), position);
                
                players[whoseTurn].addMoney(valueOf(card).getMoneyCost());
   
            }
            
        } else if (c == SENATOR) {
            
            // print out their hand
            // check card is a character card
            // select which cards to lay
            // lay card
            // refund cost
            
            boolean finished = false;
            Cards[] hand = players[whoseTurn].getHand();
            Interface.print("Your Hand:");
            String card = "";
            position = 0;
            
            for (i = 0; i < hand.length; i++) {
                
                if (hand[i].getType() == CHARACTER) {
                
                    Interface.print(hand[i].toString());
                
                }
                
            }
            
            while (!finished) {
                
                card = Interface.getInput("Which card would you like to lay?");
                card = card.toUpperCase();
                
                while ((position < 0) || (position > 6)) {
                    
                    position = Interface.getActionInput("Where would you like to place this card?");
                    
                    if ((position < 0) || (position > 6)) {
                        
                        Interface.printE("Invalid Position");
                        
                    }
                        
                }
                
                players[whoseTurn].layCard(valueOf(card), position);
                
                players[whoseTurn].addMoney(valueOf(card).getMoneyCost());
   
            }
            
        } else if (c == AESCULAPINUM) {
            
            // show the chars from discard pile
            // player selects which card
            // add that card to players hand
            Cards[] discard = deck.getDiscard();
            Boolean noCharCards = true;
            
            for (i = 0; i < Deck.NUM_CARDS; i++) {
                
                if (discard[i].getType() == CHARACTER) {
                    
                    noCharCards = false;
                    Interface.print(discard[i].toString() + "\t");
                    
                    if (i % 3 == 0) {
                        
                        Interface.print("");
                        
                    }
                    
                }
                
            }
            
            if (noCharCards) {
                
                Interface.printE("The Discard Pile contains no Character Cards");
                
                
            } else {
                
                Boolean valid = false;
                
                while (!valid) {
            
                    String card = Interface.getInput("Which card would you like to draw?");
                    card = card.toUpperCase();
                
                    for (i = 0; i < Deck.NUM_CARDS; i++) {
                    
                        if ((discard[i].getType() == CHARACTER) && (card.equals(discard[i].toString()))) {
                        
                            players[whoseTurn].addCard(discard[i]);
                            deck.discardCard(discard[i], discard);
                            i = Deck.NUM_CARDS;
                            valid = true;
                            
                        }
                        
                    }
                    
                    if (!valid) {
                        
                        Interface.printE("ERROR: Invalid Card Name");
                        
                    }
                    
                }
                
            }
            
        } else if (c == MACHINA) {
            
            // remove building cards from cardsInPlay
            // re-place building cards
            Boolean hasBuildings = false;
            Boolean valid = false;
            Cards[] temp = new Cards[Game.NUM_SIDES_ON_DICE];
            int j = 0;
            
            for (i = 0; i < Game.NUM_SIDES_ON_DICE; i++) {
                
                if (players[whoseTurn].getCardsInPlay()[i].getType() == BUILDING) {
                    
                    Interface.print(players[whoseTurn].getCardsInPlay()[i].toString());
                    temp[j] = players[whoseTurn].getCardsInPlay()[i];
                    j++;
                    
                    players[whoseTurn].getCardsInPlay()[i] = NOTACARD;
                    hasBuildings = true;
                    
                }
                
            }
            
            if (hasBuildings) {
                
                for (i = 0; i < Game.NUM_SIDES_ON_DICE; i++) {
                    
                    while (!valid) {
                
                        j = Interface.getActionInput("Where would you like to place your " + temp[i]);
                        
                        if ((j >= 0) || (j <= Game.NUM_SIDES_ON_DICE)) {
                            
                            valid = true;
                            players[whoseTurn].layCard(temp[i], j);
                            
                        }
                        
                        if (!valid) {
                            
                            Interface.printE ("ERROR: Invalid position");
                            
                        }
                        
                    }
                
                }
                
            }
            
        } else if (c == CONSILIARIUS) {
            
            // remove character cards from cardsInPlay
            // re-place character cards
            
            Boolean hasCharacters = false;
            Boolean valid = false;
            Cards[] temp = new Cards[Game.NUM_SIDES_ON_DICE];
            int j = 0;
            
            for (i = 0; i < Game.NUM_SIDES_ON_DICE; i++) {
                
                if (players[whoseTurn].getCardsInPlay()[i].getType() == CHARACTER) {
                    
                    Interface.print(players[whoseTurn].getCardsInPlay()[i].toString());
                    temp[j] = players[whoseTurn].getCardsInPlay()[i];
                    j++;
                    
                    players[whoseTurn].getCardsInPlay()[i] = NOTACARD;
                    hasCharacters = true;
                    
                }
                
            }
            
            if (hasCharacters) {
                
                for (i = 0; i < Game.NUM_SIDES_ON_DICE; i++) {
                    
                    while (!valid) {
                
                        j = Interface.getActionInput("Where would you like to place your " + temp[i]);
                        
                        if ((j >= 0) || (j <= Game.NUM_SIDES_ON_DICE)) {
                            
                            valid = true;
                            players[whoseTurn].layCard(temp[i], j);
                            
                        }
                        
                        if (!valid) {
                            
                            Interface.printE ("ERROR: Invalid position");
                            
                        }
                        
                    }
                    
                }
                
            }
            
        } else if (c == GLADIATOR) {
            
            // player picks a char card and returns it to opponents hand
            Boolean valid = false;
            
            while (!valid) {
                
                String card = Interface.getInput ("Which card would you like to return?");
                
                for (i = 0; i < Game.NUM_SIDES_ON_DICE; i++) {
                    
                    if (card.equals(players[(whoseTurn + 1) % Game.NUM_PLAYERS].getCardsInPlay()[i])) {
                        
                        if (players[(whoseTurn + 1) % Game.NUM_PLAYERS].getCardsInPlay()[i].getType() == CHARACTER) {
                        
                            valid = true;
                            players[(whoseTurn + 1) % Game.NUM_PLAYERS].addCard(players[(whoseTurn + 1) % Game.NUM_PLAYERS].getCardsInPlay()[i]);
                            players[(whoseTurn + 1) % Game.NUM_PLAYERS].getCardsInPlay()[i] = NOTACARD;
                        
                        }
                        
                    }
                    
                }
                
                if (!valid) {
                    
                    Interface.printE("ERROR: Invalid Card");
                    
                }
                
            }
            
        }
        
    }
    
    public int getMoneyCost() {
        
        return moneyCost;
        
    }

    public int getDefenseValue() {
        
        // check if current had a turris
        // check if other player has essedum activated
        int total = defenseValue;
        int i;
        
        for (i = 0; i < Game.NUM_SIDES_ON_DICE; i++) {
            
            if (players[whoseTurn].getCardsInPlay()[i] == TURRIS) {
                
                total++;
                
            }
            
        }
        
        return total;
        
    }
    
    public int getType() {
        
        return type;
        
    }
    
    public boolean isActivatable (Cards c) {
        
        boolean result = true;
        
        if ((c == TEMPLUM) || (c == BASILICA) || (c == TURRIS)) {
            
            result = false;
            
        }
        
        
        return result;
        
    }

}
