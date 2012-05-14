/** AcceptanceInterfaceAdapter.java
 * By Samuel Baxter
 * COMP2911
 * 10/05/2012
 */
package classAcceptanceInterface;

import test.testUI;
import framework.interfaces.AcceptanceInterface;
import framework.interfaces.GameState;
import framework.interfaces.MoveMaker;
import game.Game;

public class AcceptanceInterfaceAdapter implements AcceptanceInterface {

    Game g;
    
    public AcceptanceInterfaceAdapter() {
        
        g = new Game();
        g.setUI (new testUI());
        
    }
    
    @Override
    public MoveMaker getMover(GameState state) {
        
        // yeah?
        return new MoveMakerAdapter(g);
        
    }

    @Override
    public GameState getInitialState() {
        
        return new GameStateAdapter(g);
        
    }

}
