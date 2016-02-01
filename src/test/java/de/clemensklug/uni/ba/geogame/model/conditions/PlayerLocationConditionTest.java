/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame.model.conditions;

import de.clemensklug.uni.ba.geogame.GameManager;
import de.clemensklug.uni.ba.geogame.model.GeogameConfig;
import de.clemensklug.uni.ba.geogame.model.Player;
import de.clemensklug.uni.ba.geogame.model.spatial.Point;
import de.clemensklug.uni.ba.geogame.parser.Namespace;
import de.clemensklug.uni.ba.geogame.parser.OWLParser;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author clemens
 */
public class PlayerLocationConditionTest {
    private final static String _PATH = "src/test/resources/PlayerLocationCondition.owl";
    private PlayerLocationCondition _plc;
    private ArrayList<Player> _players;

    @Before
    public void setUp() throws Exception {
        _plc = new PlayerLocationCondition();
        _players = new ArrayList<>();
        _players.add(new Player());

        GameManager gm = GameManager.getInstance(null,null);
        GeogameConfig _game = mock(GeogameConfig.class);
        when(_game.getActions()).thenReturn(Collections.emptyList());
        when(_game.getDrawCondition()).thenReturn(new LogicCondition());
        when(_game.getStartCondition()).thenReturn(new LogicCondition());
        when(_game.getWinCondition()).thenReturn(new LogicCondition());
        when(_game.getPlayerCount()).thenReturn(1);
        gm.setGame(_game);
        gm.addAllPlayers(_players);
    }

    @Test
    public void testIsSatisfied() throws Exception {
        assertFalse(_plc.isSatisfied());

        List<Point> p = new ArrayList<>();
        p.add(new Point(1, 1));
        _plc.setPoints(p);
        assertFalse(_plc.isSatisfied());

        _players.get(0).setPosition(new Point(1, 1));
        assertTrue(_plc.isSatisfied());

        p.add(new Point(1, 2));
        assertTrue(_plc.isSatisfied());

        _players.get(0).setPosition(new Point(0, 1));
        assertFalse(_plc.isSatisfied());
    }

    @Test
    public void testGetSatisfyingPlayers() throws Exception {
        assertFalse(_players.isEmpty());
        assertEquals(new ArrayList<Player>(), _plc.getSatisfyingPlayers());
        assertFalse(_players.isEmpty());

        List<Point> p = new ArrayList<>();
        p.add(new Point(0, 0));
        _plc.setPoints(p);
        assertFalse(_players.isEmpty());
        assertEquals(_players, _plc.getSatisfyingPlayers());
        assertFalse(_players.isEmpty());

        _players.add(new Player("test"));
        _players.get(1).setPosition(new Point(1, 1));
        assertEquals(2, _players.size());
        assertEquals(_players.subList(0, 1), _plc.getSatisfyingPlayers());
        assertEquals(2, _players.size());
    }

    @Test
    public void testFromOWL() throws Exception {
        OWLParser cp = new OWLParser(_PATH);
        GameManager gm = GameManager.getInstance(null,null);
        List<Condition> a= new LinkedList<>();
        a.addAll(cp.getInstances(Namespace.PLAYER_LOCATION_CONDITION));
        GeogameConfig _game = mock(GeogameConfig.class);
        when(_game.getActions()).thenReturn(Collections.emptyList());
        when(_game.getDrawCondition()).thenReturn(new LogicCondition());
        when(_game.getStartCondition()).thenReturn(new LogicCondition(a));
        when(_game.getWinCondition()).thenReturn(new LogicCondition());
        when(_game.getPlayerCount()).thenReturn(1);
        System.out.println(_game.getStartCondition());
        System.out.println(a);
        System.out.println(cp.getInstances(Namespace.PLAYER_LOCATION_CONDITION));
        gm.setGame(_game);
        Player p1 = new Player();
        gm.addPlayer(p1);
        assertFalse(gm.isStartable());
        p1.setPosition(new Point(1,1));
        assertTrue(gm.isStartable());
    }
}