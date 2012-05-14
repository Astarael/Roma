package cards;

import userInterface.*;
import game.*;

/** Cards.java
 * By Samuel Baxter
 * COMP2911
 * 11/04/2012
 */

public enum Card {
    
    AESCULAPINUM (0, 5, 2),
    ARCHITECTUS (1, 3, 4),
    BASILICA (0, 6, 5),
    CONSILIARIUS (1, 4, 4),
    FORUM (0, 5, 5),
    GLADIATOR (1, 6, 5),
    HARUSPEX (0, 4, 3),
    LEGAT (1, 5, 2),
    LEGIONARIUS (1, 4, 5),
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
    private Die[] dice;
    
    private RomaUserInterface ui;
    
    Card (int type, int cost, int defense) {
        
        this.type = type;
        this.moneyCost = cost;
        this.defenseValue = defense;
        
    }
    
    public void setUI (RomaUserInterface rui) {
        
        ui = rui;
        
    }
    
    public void activate (int w, Player[] p, Card c, Deck d, int position, Die[] di) {
        
        whoseTurn = w;
        players = p;
        deck = d;
        dice = di;
        
        if (c == MERCATOR) {
            
            activateMercator();
            
        } else if (c == TRIBUNUSPLEBIS) { 
            
            activateTribunusPlebis();
            
        } else if (c == FORUM) {
            
            activateForum(position, dice);
            
        } else if (c == SICARIUS) {
            
            activateSicarius(c);
            
        } else if (c == LEGAT) {
            
            activateLegat();
            
        } else if (c == NERO) {
            
            activateNero(c);
            
        } else if (c == MERCATUS) {
            
            activateMercatus();
            
        } else if (c == ARCHITECTUS) {
            
            activateArchitectus();
            
        } else if (c == SENATOR) {
            
            activateSenator();
            
        } else if (c == AESCULAPINUM) {
            
            actiavteAesculapinum();
            
        } else if (c == MACHINA) {
            
            activateMachina();
            
        } else if (c == CONSILIARIUS) {
            
            activateConsiliarius();
            
        } else if (c == GLADIATOR) {
            
            activateGladiator();
            
        } else if (c == HARUSPEX) {
            
            activateHaruspex();
            
        } else if (c == LEGIONARIUS) {
            
            activateLegionarius(position);
            
        }
        
    }
    
    
    private void activateLegionarius(int position) {
        
        // show battle die
        // compare battle die to opposite cards defense
        // battledie >= defense value
        if (players[(whoseTurn + 1) % Game.NUM_PLAYERS].getCardsInPlay()[position] == NOTACARD) {
            
            ui.printE("ERROR: No Card Opposite");
            
        } else {
            
            ui.print("Battle!");
            ui.print(dice[0].getValue() + "");
            
            int dValue = players[whoseTurn + 1 % Game.NUM_PLAYERS].getCardsInPlay()[position].getDefenseValue();
            ui.print("Defense Value = " + dValue);
            
            if (dice[0].getValue() >= dValue) {
                
                ui.print("You Won the Battle!");
                deck.discardCard (players[whoseTurn + 1 % Game.NUM_PLAYERS].getCardsInPlay()[position], 
                        players[whoseTurn + 1 % Game.NUM_PLAYERS].getCardsInPlay());
                
            } else {
                
                ui.print("You Lost the Battle!");
                
            }
            
        }
        
    }

    private void activateHaruspex() {
        
        int i;
        // show the deck
        // player picks a card
        // shuffle the deck
        Boolean valid = false;
        
        ui.print("Pick a card:");
        for (i = 0; i < deck.getCards().length; i++) {
            
            if (deck.getCards()[i] != NOTACARD) {
                
                ui.print(deck.getCards()[i].toString());
                
            }
            
        }
        
        while (!valid) {
            
            Card card = ui.pickACard();
            for (i = 0; i < deck.getCards().length; i++) {
                
                if (card == deck.getCards()[i]) {
                    
                    valid = true;
                    players[whoseTurn].addCard(deck.getCards()[i]);
                    deck.discardCard(deck.getCards()[i], deck.getCards());
                    i = deck.getCards().length;
                    deck.shuffleCards();
                    
                }
                
            }
            
            if (!valid) {
                
                ui.printE("Invalid Card, Please choose another");
                
            }
            
        }
        
    }

    private void activateGladiator() {
        
        int i;
        // player picks a char card and returns it to opponents hand
        Boolean valid = false;
        
        while (!valid) {
            
            ui.print("Which card would you like to return?");
            Card card = ui.pickACard();
            
            for (i = 0; i < Game.NUM_SIDES_ON_DICE; i++) {
                
                if (card == players[(whoseTurn + 1) % Game.NUM_PLAYERS].getCardsInPlay()[i]) {
                    
                    if (players[(whoseTurn + 1) % Game.NUM_PLAYERS].getCardsInPlay()[i].getType() == CHARACTER) {
                    
                        valid = true;
                        players[(whoseTurn + 1) % Game.NUM_PLAYERS].addCard(players[(whoseTurn + 1) % Game.NUM_PLAYERS].getCardsInPlay()[i]);
                        players[(whoseTurn + 1) % Game.NUM_PLAYERS].getCardsInPlay()[i] = NOTACARD;
                    
                    }
                    
                }
                
            }
            
            if (!valid) {
                
                ui.printE("ERROR: Invalid Card");
                
            }
            
        }
        
    }

    private void activateConsiliarius() {
        
        int i;
        // remove character cards from cardsInPlay
        // re-place character cards
        
        Boolean hasCharacters = false;
        Boolean valid = false;
        Card[] temp = new Card[Game.NUM_SIDES_ON_DICE];
        int j = 0;
        
        for (i = 0; i < Game.NUM_SIDES_ON_DICE; i++) {
            
            if (players[whoseTurn].getCardsInPlay()[i].getType() == CHARACTER) {
                
                ui.print(players[whoseTurn].getCardsInPlay()[i].toString());
                temp[j] = players[whoseTurn].getCardsInPlay()[i];
                j++;
                
                players[whoseTurn].getCardsInPlay()[i] = NOTACARD;
                hasCharacters = true;
                
            }
            
        }
        
        if (hasCharacters) {
            
            for (i = 0; i < Game.NUM_SIDES_ON_DICE; i++) {
                
                while (!valid) {
            
                    ui.print("Where would you like to place your " + temp[i]);
                    j = ui.pickAnInt(0);
                    
                    if ((j >= 0) || (j <= Game.NUM_SIDES_ON_DICE)) {
                        
                        valid = true;
                        players[whoseTurn].layCard(temp[i], j);
                        
                    }
                    
                    if (!valid) {
                        
                        ui.printE ("ERROR: Invalid position");
                        
                    }
                    
                }
                
            }
            
        }
        
    }

    private void activateMachina() {
        
        int i;
        // remove building cards from cardsInPlay
        // re-place building cards
        Boolean hasBuildings = false;
        Boolean valid = false;
        Card[] temp = new Card[Game.NUM_SIDES_ON_DICE];
        int j = 0;
        
        for (i = 0; i < Game.NUM_SIDES_ON_DICE; i++) {
            
            if (players[whoseTurn].getCardsInPlay()[i].getType() == BUILDING) {
                
                ui.print(players[whoseTurn].getCardsInPlay()[i].toString());
                temp[j] = players[whoseTurn].getCardsInPlay()[i];
                j++;
                
                players[whoseTurn].getCardsInPlay()[i] = NOTACARD;
                hasBuildings = true;
                
            }
            
        }
        
        if (hasBuildings) {
            
            for (i = 0; i < Game.NUM_SIDES_ON_DICE; i++) {
                
                while (!valid) {
            
                    ui.print("Where would you like to place your " + temp[i]);
                    j = ui.pickAnInt(0);
                    
                    if ((j >= 0) || (j <= Game.NUM_SIDES_ON_DICE)) {
                        
                        valid = true;
                        players[whoseTurn].layCard(temp[i], j);
                        
                    }
                    
                    if (!valid) {
                        
                        ui.printE ("ERROR: Invalid position");
                        
                    }
                    
                }
            
            }
            
        }
        
    }

    private void actiavteAesculapinum() {
        
        int i;
        // show the chars from discard pile
        // player selects which card
        // add that card to players hand
        Card[] discard = deck.getDiscard();
        Boolean noCharCards = true;
        
        for (i = 0; i < Deck.NUM_CARDS; i++) {
            
            if (discard[i].getType() == CHARACTER) {
                
                noCharCards = false;
                ui.print(discard[i].toString() + "\t");
                
                if (i % 3 == 0) {
                    
                    ui.print("");
                    
                }
                
            }
            
        }
        
        if (noCharCards) {
            
            ui.printE("The Discard Pile contains no Character Cards");
            
            
        } else {
            
            Boolean valid = false;
            
            while (!valid) {
        
                ui.print("Which card would you like to draw?");
                Card card = ui.pickACard();
            
                for (i = 0; i < Deck.NUM_CARDS; i++) {
                
                    if ((discard[i].getType() == CHARACTER) && (card == discard[i])) {
                    
                        players[whoseTurn].addCard(discard[i]);
                        deck.discardCard(discard[i], discard);
                        i = Deck.NUM_CARDS;
                        valid = true;
                        
                    }
                    
                }
                
                if (!valid) {
                    
                    ui.printE("ERROR: Invalid Card Name");
                    
                }
                
            }
            
        }
        
    }

    private void activateSenator() {
        
        int position;
        int i;
        // print out their hand
        // check card is a character card
        // select which cards to lay
        // lay card
        // refund cost
        
        boolean finished = false;
        Card[] hand = players[whoseTurn].getHand();
        ui.print("Your Hand:");
        Card card;
        position = 0;
        
        for (i = 0; i < hand.length; i++) {
            
            if (hand[i].getType() == CHARACTER) {
            
                ui.print(hand[i].toString());
            
            }
            
        }
        
        while (!finished) {
            
            ui.print("Which card would you like to lay?");
            card = ui.pickACard();
            
            while ((position < 0) || (position > 6)) {
                
                ui.print("Where would you like to place this card?");
                position = ui.pickAnInt(0);
                
                if ((position < 0) || (position > 6)) {
                    
                    ui.printE("Invalid Position");
                    
                }
                    
            }
            
            players[whoseTurn].layCard(card, position);
            
            players[whoseTurn].addMoney(card.getMoneyCost());
   
        }
        
    }

    private void activateArchitectus() {
        
        int position;
        int i;
        // print out their hand
        // check card is a building card
        // select which cards to lay
        // lay card
        // refund cost
        
        boolean finished = false;
        Card[] hand = players[whoseTurn].getHand();
        ui.print("Your Hand:");
        Card card;
        position = 0;
        
        for (i = 0; i < hand.length; i++) {
            
            if (hand[i].getType() == BUILDING) {
            
                ui.print(hand[i].toString());
            
            }
            
        }
        
        while (!finished) {
            
            ui.print("Which card would you like to lay?");
            card = ui.pickACard();
            
            while ((position < 0) || (position > 6)) {
                
                ui.print("Where would you like to place this card?");
                position = ui.pickAnInt(0);
                
                if ((position < 0) || (position > 6)) {
                    
                    ui.printE("Invalid Position");
                    
                }
                    
            }
            
            players[whoseTurn].layCard(card, position);
            
            players[whoseTurn].addMoney(card.getMoneyCost());
   
        }
        
    }

    private void activateMercatus() {
        
        int i;
        int counter = 0;
        
        for (i = 0; i <= Game.NUM_SIDES_ON_DICE; i++) {
            
            if (players[whoseTurn + 1 % Game.NUM_PLAYERS].getCardsInPlay()[i] == FORUM) {
                
                counter++;
                
            }
            
        }
        
        players[whoseTurn].addVictoryPoints(counter);
        
    }

    private void activateNero(Card c) {
        
        int place = -1;
        
        while (place == -1) {
            
            ui.print("Enter the positon of the building card you wish to eliminate");
            place = ui.pickAnInt(0);
            
            if (players[(whoseTurn + 1) % Game.NUM_PLAYERS].getCardsInPlay()[place].getType() != BUILDING) {
                
                place = -1;
                ui.printE("ERROR: Invalid Character Card");
                
            }
            
        }
        
        // remove the nero from the players cards
        deck.discardCard(c, players[whoseTurn].getCardsInPlay());
        
        // remove the card the nero destroyed
        c = players[(whoseTurn + 1) % Game.NUM_PLAYERS].getCardsInPlay()[place];
        deck.discardCard(c, players[(whoseTurn + 1) % Game.NUM_PLAYERS].getCardsInPlay());
        
    }

    private void activateLegat() {
        
        int i;
        int counter = 0;
        
        // subtract 1 VP for each unoccupied dice disk
        for (i = 1; i <= Game.NUM_SIDES_ON_DICE; i++) {
            
            if (players[whoseTurn + 1 % Game.NUM_PLAYERS].getCardsInPlay()[i] == Card.NOTACARD) {
                
                counter++;
                
            }
            
        }
        
        players[whoseTurn].addVictoryPoints(counter);
        
    }

    private void activateSicarius(Card c) {
        
        int place = 0;
        
        while (place == 0) {
            
            ui.print("Enter the positon of the character card you wish to eliminate");
            place = ui.pickAnInt(0);
            
            if (players[(whoseTurn + 1) % Game.NUM_PLAYERS].getCardsInPlay()[place].getType() != CHARACTER) {
                
                place = 0;
                ui.printE("ERROR: Invalid Character Card");
                
            }
            
        }
        
        
        deck.discardCard(c, players[whoseTurn].getCardsInPlay());
        
        c = players[(whoseTurn + 1) % Game.NUM_PLAYERS].getCardsInPlay()[place];
        deck.discardCard(c, players[(whoseTurn + 1) % Game.NUM_PLAYERS].getCardsInPlay());
        
    }

    private void activateForum(int position, Die[] dice) {
        
        int i;
        int basilica = 0;
        boolean templum = false;
        
        ui.print("Which diee would you like to use to Activate the Forum and get the Victory Points");
        players[whoseTurn].addVictoryPoints(ui.pickAnInt(1));
        
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
            
            ui.print("Do you wish to use the Templum? (1 for yes)");
            if (ui.pickAnInt(0) == 1) {
                
                int value = -1;
                    
                    for (i = 0; i < dice.length; i++) {
                        
                        if (dice[i].isUsed() == false) {
                            
                            value = dice[i].getValue();
                            dice[i].use();
                            
                        }
                        
                    }
            
                if (value != -1) {
                    
                    players[whoseTurn].addVictoryPoints(value);
                    
                } else {
                    
                    ui.printE("All dice are already used");
                    
                }
                
            }
            
        }
        
    }

    private void activateTribunusPlebis() {
        
        players[whoseTurn].addVictoryPoints(1);
        players[(whoseTurn + 1) % 2].addVictoryPoints(-1);
        
    }

    private void activateMercator() {
        
        int money = players[whoseTurn].getMoney();
        int victoryPoints = players[(whoseTurn + 1) % 2].getVictoryPoints();
        int value= -1;
        
        if (money > 0 && victoryPoints > 0) {
            money = money / 2;
            while (value < 0) {
            
                ui.print("Please enter the number of victory points you want to buy");
                value = ui.pickAnInt(1);
                if (value <= money) {
                    
                    players[whoseTurn].addVictoryPoints(value);
                    players[whoseTurn].addMoney(-money * 2);
                
                    players[(whoseTurn + 1) % 2].addVictoryPoints(-value);
                    players[(whoseTurn + 1) % 2].addMoney(money * 2);
                    
                } else  {
                    
                    value = -1;
                    ui.printE ("Invalid Input");
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
    
    public boolean isActivatable (Card c) {
        
        boolean result = true;
        
        if ((c == TEMPLUM) || (c == BASILICA) || (c == TURRIS)) {
            
            result = false;
            
        }
        
        
        return result;
        
    }
    
    public static boolean isCard (String s) {
        
        boolean valid = false;
            
        for (Card c : Card.values()) {
                
            // compare c to the uppercase version of s
            try { 
                
                if (c == valueOf(s)) {
                    
                    valid = true;
                    
                }
                  
            } catch (IllegalArgumentException e) {
                
            }
        }

        
        return valid;
        
    }

}