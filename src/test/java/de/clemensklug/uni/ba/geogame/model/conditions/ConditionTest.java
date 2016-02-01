/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame.model.conditions;

import de.clemensklug.uni.ba.geogame.GameManager;
import de.clemensklug.uni.ba.geogame.model.GeogameConfig;
import de.clemensklug.uni.ba.geogame.model.Player;
import de.clemensklug.uni.ba.geogame.model.spatial.Point;
import de.clemensklug.uni.ba.geogame.model.actions.Action;
import de.clemensklug.uni.ba.geogame.parser.Namespace;
import de.clemensklug.uni.ba.geogame.parser.OWLParser;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author clemens
 */
public class ConditionTest {
    private final static String _path = "src/test/resources/Conditions.owl";
    private List<Condition> _conditionList;
    private List<Action> _actions;
    private Action _startAction;
    private OWLParser _op;

    @Before
    public void setUp() throws Exception {
        _op = new OWLParser(_path);
        _conditionList = _op.getInstances(Namespace.START_CONDITION);
        _actions = _op.getInstances(Namespace.ACTION);
        _actions.stream().filter(a -> a.getName().contains("EnableAction0")).forEach(a -> _startAction = a);
    }

    @Test
    public void testLoadSatisfiedAction() throws Exception {
        assertFalse(_conditionList.isEmpty());
        for (Condition c : _conditionList) {
            assertNotNull(c.getSatisfiedAction());
            assertEquals(_startAction, c.getSatisfiedAction());
        }
        assertNotNull(_conditionList.get(0).getSatisfiedAction());
        assertEquals(_startAction, _conditionList.get(0).getSatisfiedAction());
    }

    @Test
    public void testRunSatisfiedAction() throws Exception {
        Action other = null;
        for (Action a : _actions) {
            if (a.getName().contains("Echo")) {
                other = a;
            }
        }
        GameManager gm = GameManager.getInstance(null,null);
        List<Player> players = new ArrayList<>();
        final Player mock = mock(Player.class);
        when(mock.getPosition()).thenReturn(new Point(0, 0));
        players.add(mock);
        GeogameConfig _game = mock(GeogameConfig.class);
        when(_game.getActions()).thenReturn(_actions);
        when(_game.getDrawCondition()).thenReturn(new LogicCondition());
        when(_game.getStartCondition()).thenReturn(new LogicCondition(_conditionList));
        when(_game.getWinCondition()).thenReturn(new LogicCondition(_conditionList));
        when(_game.getPlayerCount()).thenReturn(2);
        gm.setGame(_game);
        gm.addAllPlayers(players);

        assertNotNull(other);
        assertFalse(other.isEnabled());
        assertTrue(gm.isStartable());
        assertTrue(gm.startGame());
        assertTrue(other.isEnabled());
        assertFalse(_startAction.isEnabled());
    }

    @Test
    public void testSatisfiedAction() throws Exception {
        Condition c = new Condition() {
            @Override
            public boolean isSatisfied() {
                return false;
            }

            @Override
            public List<Player> getSatisfyingPlayers() {
                return null;
            }
        };
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
        c.setSatisfiedAction(a);
        c.runSatisfiedAction();
        verify(a).trigger(p.getPosition(), p);
    }
}