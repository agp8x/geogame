/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame.model.conditions;

import static org.junit.Assert.*;

import de.clemensklug.uni.ba.geogame.GameManager;
import de.clemensklug.uni.ba.geogame.model.GeogameConfig;
import de.clemensklug.uni.ba.geogame.model.Player;
import de.clemensklug.uni.ba.geogame.model.spatial.Point;
import de.clemensklug.uni.ba.geogame.model.actions.Action;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author clemens
 */
public class LogicTest {

    /**
     * only true
     */
    private List<Condition> _conditionsTrue;
    /**
     * only false
     */
    private List<Condition> _conditionsFalse;
    /**
     * true/false
     */
    private List<Condition> _conditionsMixed;

    @Before
    public void setUp() throws Exception {
        _conditionsTrue = new ArrayList<>();
        _conditionsFalse = new ArrayList<>();
        _conditionsMixed = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Condition c = Mockito.mock(Condition.class);
            if (i % 2 == 0) {
                when(c.isSatisfied()).thenReturn(true);
                _conditionsTrue.add(c);
            } else {
                when(c.isSatisfied()).thenReturn(false);
                _conditionsFalse.add(c);
            }
            _conditionsMixed.add(c);
        }
    }

    @Test
    public void testgetPlayersT() throws Exception {
        List<Player> players = new ArrayList<>();
        assertEquals(LogicCondition.Logic.getSatisfyingPlayers(null, players, LogicCondition.LogicMode.T), players);

    }

    @Test
    public void testgetPlayersF() throws Exception {
        assertTrue(LogicCondition.Logic.getSatisfyingPlayers(null, null, LogicCondition.LogicMode.F).isEmpty());

    }

    @Test
    public void testPerformANDTrue() throws Exception {
        assertTrue(LogicCondition.Logic.perform(_conditionsTrue, LogicCondition.LogicMode.AND));
    }

    @Test
    public void testPerformANDEmpty() throws Exception {
        assertTrue(LogicCondition.Logic.perform(new ArrayList<>(), LogicCondition.LogicMode.AND));
    }

    @Test
    public void testPerformANDFalse() throws Exception {
        assertFalse(LogicCondition.Logic.perform(_conditionsFalse, LogicCondition.LogicMode.AND));
    }

    @Test
    public void testPerformANDMixed() throws Exception {
        assertFalse(LogicCondition.Logic.perform(_conditionsMixed, LogicCondition.LogicMode.AND));
    }

    @Test
    public void testPerformORTrue() throws Exception {
        assertTrue(LogicCondition.Logic.perform(_conditionsTrue, LogicCondition.LogicMode.OR));
    }

    @Test
    public void testPerformORFalse() throws Exception {
        assertFalse(LogicCondition.Logic.perform(_conditionsFalse, LogicCondition.LogicMode.OR));
    }

    @Test
    public void testPerformORMixed() throws Exception {
        assertTrue(LogicCondition.Logic.perform(_conditionsMixed, LogicCondition.LogicMode.OR));
    }

    @Test
    public void testPerformOREmpty() throws Exception {
        assertFalse(LogicCondition.Logic.perform(new ArrayList<>(), LogicCondition.LogicMode.OR));
    }

    @Test
    public void testPerformNORTrue() throws Exception {
        assertTrue(LogicCondition.Logic.perform(_conditionsFalse, LogicCondition.LogicMode.NOR));
    }

    @Test
    public void testPerformNORFalse() throws Exception {
        assertFalse(LogicCondition.Logic.perform(_conditionsTrue, LogicCondition.LogicMode.NOR));
    }

    @Test
    public void testPerformNORMixed() throws Exception {
        assertFalse(LogicCondition.Logic.perform(_conditionsMixed, LogicCondition.LogicMode.NOR));
    }

    @Test
    public void testPerformNOREmpty() throws Exception {
        assertTrue(LogicCondition.Logic.perform(new ArrayList<>(), LogicCondition.LogicMode.NOR));
    }

    @Test
    public void testPerformTTrue() throws Exception {
        assertTrue(LogicCondition.Logic.perform(_conditionsTrue, LogicCondition.LogicMode.T));
    }

    @Test
    public void testPerformTFalse() throws Exception {
        assertTrue(LogicCondition.Logic.perform(_conditionsFalse, LogicCondition.LogicMode.T));
    }

    @Test
    public void testPerformFTrue() throws Exception {
        assertFalse(LogicCondition.Logic.perform(_conditionsTrue, LogicCondition.LogicMode.F));
    }

    @Test
    public void testPerformFFalse() throws Exception {
        assertFalse(LogicCondition.Logic.perform(_conditionsFalse, LogicCondition.LogicMode.F));
    }

    @Test
    public void testMisc() throws Exception {
        assertEquals(LogicCondition.LogicMode.AND, LogicCondition.LogicMode.valueOf("AND"));
        assertEquals(LogicCondition.LogicMode.OR, LogicCondition.LogicMode.valueOf("OR"));
    }

    @Test
    public void testIsSatisfied() throws Exception {
        LogicCondition lc = new LogicCondition();
        assertFalse(lc.isSatisfied());
        lc.setConditions(null);
        assertFalse(lc.isSatisfied());
        final ArrayList<Condition> conditions = new ArrayList<>();
        lc.setConditions(conditions);
        assertTrue(lc.isSatisfied());
        conditions.add(null);
        assertTrue(lc.isSatisfied());
    }

    @Test
    public void testSatisfiedAction() throws Exception {
        LogicCondition lc = new LogicCondition();
        GameManager gm = GameManager.getInstance(null,null);
        GeogameConfig _game = mock(GeogameConfig.class);
        when(_game.getActions()).thenReturn(Collections.emptyList());
        when(_game.getDrawCondition()).thenReturn(new LogicCondition());
        when(_game.getStartCondition()).thenReturn(new LogicCondition());
        when(_game.getWinCondition()).thenReturn(new LogicCondition());
        when(_game.getPlayerCount()).thenReturn(1);
        gm.setGame(_game);
        List<Player> players = new ArrayList<>();
        Player p = mock(Player.class);
        when(p.getPosition()).thenReturn(new Point(0, 0));
        players.add(p);
        gm.addAllPlayers(players);
        Action a = mock(Action.class);
        lc.setSatisfiedAction(a);
        lc.runSatisfiedAction();
        verify(a).trigger(p.getPosition(), p);
        List<Condition> mocks = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Condition c = mock(Condition.class);
            mocks.add(c);
        }
        lc.setConditions(mocks);
        lc.runSatisfiedAction();
        for (Condition c : mocks) {
            verify(c).runSatisfiedAction();
        }
    }
}