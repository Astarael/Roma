/* STMoveMaker.java
 * By Samuel Baxter
 * COMP2911
 * 10/05/2012
 */
package classAcceptanceInterface;

import framework.cards.Card;
import framework.interfaces.MoveMaker;
import framework.interfaces.activators.CardActivator;
import game.Game;

public class MoveMakerAdapter implements MoveMaker {
    
    private Game g;
    
    public MoveMakerAdapter (Game g) {
        
        this.g = g;
        
    }

    @Override
    public CardActivator chooseCardToActivate(int disc)
            throws UnsupportedOperationException {
        // TODO Auto-generated method stub
        // find the type of card on int disc
        // return activator for that card
        return null;
    }

    @Override
    public void activateCardsDisc(int diceToUse, Card chosen)
            throws UnsupportedOperationException {
        // TODO Auto-generated method stub
        // call activator for given card

    }

    @Override
    public void activateMoneyDisc(int diceToUse)
            throws UnsupportedOperationException {
        
        g.players[g.whoseTurn()].addMoney(diceToUse);

    }

    @Override
    public CardActivator activateBribeDisc(int diceToUse)
            throws UnsupportedOperationException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void endTurn() throws UnsupportedOperationException {
        
        g.endTurn();

    }

    @Override
    public void placeCard(Card toPlace, int discToPlaceOn)
            throws UnsupportedOperationException {

        g.players[g.whoseTurn()].layCard(toPlace, discToPlaceOn);

    }

}
