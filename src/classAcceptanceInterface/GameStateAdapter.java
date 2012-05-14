/** GameStateAdapter.java
 * By Samuel Baxter and Theo Tse
 * COMP2911
 * 10/05/2012
 */
package classAcceptanceInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import cards.Card;

import framework.Rules;
import framework.interfaces.GameState;
import game.*;
import test.testUI;
import userInterface.*;
import cards.*;
import framework.cards.*;

public class GameStateAdapter implements GameState {
    
    private Game g;
    private RomaUserInterface ui;
    
    private int i;
    
    public GameStateAdapter (Game g) {
        
        ui = new testUI();
        g.setUI(ui);
        
    }

    @Override
    public int getWhoseTurn() {
        
        return g.whoseTurn();
        
    }

    @Override
    public void setWhoseTurn(int player) {

        g.setWhoseTurn(player);
        
    }

    @Override
    public List<Card> getDeck() {
        
        List<Card> l = Arrays.asList(g.getDeck().getCards());
        
        return l;
    }

    @Override
    public void setDeck(List<Card> deck) {
        
        g.getDeck().emptyDeck();
        for (Card c : deck) {
            
            g.getDeck().addCard(c);
            
        }
        
    }

    @Override
    public List<Card> getDiscard() {
        
        List<Card> l = Arrays.asList(g.getDeck().getDiscard());
        
        return l;
    }

    @Override
    public void setDiscard(List<Card> discard) {
        
        for (Card c : discard) {
            
            g.getDeck().discardCard(c, null);
            
        }
        
    }

    @Override
    public int getPlayerSestertii(int playerNum) {
        
        return g.players[playerNum].getMoney();
        
    }

    @Override
    public void setPlayerSestertii(int playerNum, int amount) {
        
        g.players[playerNum].setMoney(amount);
        
    }

    @Override
    public int getPlayerVictoryPoints(int playerNum) {
        
        return g.players[playerNum].getVictoryPoints();
        
    }

    @Override
    public void setPlayerVictoryPoints(int playerNum, int points) {
        
        g.players[playerNum].setVictoryPoints(points);
        
    }

    @Override
    public Collection<Card> getPlayerHand(int playerNum) {

        Collection<Card> c =  Arrays.asList(g.players[playerNum].getHand());
        return c;
    }

    @Override
    public void setPlayerHand(int playerNum, Collection<Card> hand) {
            
        for (Card c : hand) {
                
            g.players[playerNum].addCard(c);
                
        }
        
    }

    @Override
    public Card[] getPlayerCardsOnDiscs(int playerNum) {
        
        return g.players[playerNum].getCardsInPlay();
        
    }

    @Override
    public void setPlayerCardsOnDiscs(int playerNum, Card[] discCards) {
        
        for (i = 0; i < discCards.length; i++) {
            
            g.players[playerNum].layCard(discCards[i], i);
            
        }
        
    }

    @Override
    public int[] getActionDice() {
        
        int[] die = new int[Game.NUM_DICE];
        for (i = 0; i < Game.NUM_DICE; i++) {
            
            die[i] = g.getDice()[i + 1].getValue();
            
        }
        
        return die;
    }

    @Override
    public void setActionDice(int[] dice) {
        
        for (i = 0; i < Game.NUM_DICE; i++) {
            
            g.die[i + 1].setValue(dice[i]);
            
        }
        
    }

    @Override
    public int getPoolVictoryPoints() {
        
        int total = 0;
        for (i = 0; i < Game.NUM_PLAYERS; i++) {
            
            total += g.players[i].getVictoryPoints();
            
        }
        
        return Rules.GAME_VICTORY_POINTS - total;
        
    }

    @Override
    public boolean isGameCompleted() {
        
        return g.endGame();
        
    }

    @Override
    public void setPlayerCardsOnDiscs(int playerNum,
            framework.cards.Card[] discCards) {
        
        for (int i = 0; i < Rules.NUM_DICE_DISCS; i++) {
        
            g.players[playerNum].layCard(discCards[i], i + 1);
            
        }
        
    }

}
