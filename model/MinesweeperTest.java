package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.platform.commons.annotation.Testable;

@Testable
public class MinesweeperTest {
    private static final int ROWS = 10;
    private static final int COLS = 10;
    private static Minesweeper m;

    static {
        m = new Minesweeper(ROWS, COLS, ((1-ROWS) * (1-COLS) / 4) );
    }
    
    @Test
    public void testInitialize() {
        assertTrue( m.getGameState().equals(GameState.NOT_STARTED) );
    }

    @Test  
    public void testPossibleSelections() {
        assertEquals(m.getPossibleSelections().size(), 100);
    }

    @Test
    public void testMakeSelection() {
        m.makeSelection(new Location(0, 0));
        assertTrue(m.getPossibleSelections().size() < 100);
        assertEquals(m.getMoveCount(), 1);
    }

    @Test
    public void testUncoverAll() {
        m.uncoverAll();
        assertEquals(m.getPossibleSelections().size(), 0);
    }

    @Test
    public void testQuitGame() {
        m.quitGame();
        assertEquals(m.getGameState(), GameState.LOST);
    }
}
