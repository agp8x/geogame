/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame;

import de.clemensklug.uni.ba.geogame.location.LocationProvider;
import de.clemensklug.uni.ba.geogame.model.GeogameConfig;
import de.clemensklug.uni.ba.geogame.model.Player;
import de.clemensklug.uni.ba.geogame.model.TriggeringMode;
import de.clemensklug.uni.ba.geogame.model.actions.Action;
import de.clemensklug.uni.ba.geogame.model.actions.EchoAction;
import de.clemensklug.uni.ba.geogame.model.conditions.Condition;
import de.clemensklug.uni.ba.geogame.model.conditions.LogicCondition;
import de.clemensklug.uni.ba.geogame.model.conditions.PlayerLocationCondition;
import de.clemensklug.uni.ba.geogame.model.spatial.Point;
import de.clemensklug.uni.ba.geogame.parser.ConfigParser;
import de.clemensklug.uni.ba.geogame.parser.OWLParser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author clemens
 */
public class GameManagerTest {
    private final static String _path = "src/test/resources/StartCondition.owl";
    private final static String _path_DRAW = "src/test/resources/DrawCondition.owl";

    private Action _testAction;
    private LocationProvider _testProvider;
    private List<Action> _actions;
    private List<Condition> _conditionsTrue;
    private List<Condition> _conditionsFalse;
    private List<Player> _players;
    private Player _player;
    private Point _l;
    private GameManager _gm;
    private GeogameConfig _game;

    @Before
    public void setUp() throws Exception {
        _l = new Point();
        List<Point> points = new LinkedList<>();
        points.add(_l);
        _player = Mockito.mock(Player.class);
        _testAction = Mockito.mock(Action.class);
        when(_testAction.isEnabled()).thenReturn(true);
        when(_testAction.getTriggers()).thenReturn(points);
        when(_testAction.trigger(_l, _player)).thenReturn(null);

        _testProvider = mock(LocationProvider.class);
        when(_testProvider.getPosition(_player)).thenReturn(_l);

        _actions = new ArrayList<>();
        _actions.add(_testAction);

        _conditionsTrue = new ArrayList<>();
        _conditionsFalse = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Condition c = Mockito.mock(Condition.class);
            if (i % 2 == 0) {
                when(c.isSatisfied()).thenReturn(true);
                _conditionsTrue.add(c);
            } else {
                when(c.isSatisfied()).thenReturn(false);
            }
            _conditionsFalse.add(c);
        }
        _players = new ArrayList<>();

        _gm = GameManager.getInstance(null, null);
        _game = mock(GeogameConfig.class);
        when(_game.getActions()).thenReturn(_actions);
        when(_game.getDrawCondition()).thenReturn(new LogicCondition());
        when(_game.getStartCondition()).thenReturn(new LogicCondition(_conditionsTrue));
        when(_game.getWinCondition()).thenReturn(new LogicCondition(_conditionsTrue));
        when(_game.getPlayerCount()).thenReturn(2);
        when(_game.getTriggeringMode()).thenReturn(TriggeringMode.WITHIN);
        _gm.setGame(_game);
        _gm.addAllPlayers(_players);
    }

    @Test
    public void testUpdate() throws Exception {
        _gm.addPlayer(_player);
        _gm.startGame();
        _gm.update(_testProvider, _player);
        verify(_testAction).trigger(_l, _player);
    }

    @Test
    public void testIsWonNull() throws Exception {
        GameManager gm = GameManager.getInstance(null, null);
        assertFalse(gm.isWon());
    }

    @Test
    public void testIsWonTrue() throws Exception {
        assertFalse(_gm.isWon());
        _gm.startGame();
        assertTrue(_gm.isWon());
    }

    @Test
    public void testIsWonFalse() throws Exception {
        assertFalse(_gm.isWon());
        when(_game.getWinCondition()).thenReturn(new LogicCondition(_conditionsFalse));
        _gm.setGame(_game);
        assertFalse(_gm.isWon());
    }

    @Test(expected = IllegalAccessError.class)
    public void testStartFalse() throws Exception {
        _gm.update(null, null);
    }

    @Test
    public void testStartFalse2() throws Exception {
        when(_game.getStartCondition()).thenReturn(new LogicCondition(_conditionsFalse));
        _gm.setGame(_game);
        assertFalse(_gm.startGame());
    }

    @Test
    public void testStartTrue() throws Exception {
        when(_game.getStartCondition()).thenReturn(new LogicCondition(_conditionsTrue));
        _gm.setGame(_game);
        assertTrue(_gm.startGame());
    }

    @Test
    public void testStartTrue2() throws Exception {
        assertTrue(_gm.startGame());
    }

    @Test(expected = IllegalAccessError.class)
    public void testStartFalse3() throws Exception {
        assertTrue(_gm.startGame());
        _gm.setGame(_game);
    }

    @Test
    public void testStartSetup() throws Exception {
        GameManager gm = GameManager.getInstance(null, null);
        assertFalse(gm.startGame());
        gm.addAllPlayers(_players);
        assertFalse(gm.startGame());
        gm.setGame(_game);
        assertTrue(gm.startGame());
    }

    @Test
    public void testStartCondition() throws Exception {
        GameManager gm = GameManager.getInstance(null, null);
        List<Condition> startCond = new ArrayList<>();
        PlayerLocationCondition locationCondition = new PlayerLocationCondition();
        List<Point> p = new ArrayList<>();
        p.add(new Point(10, 10));
        locationCondition.setPoints(p);
        startCond.add(locationCondition);
        when(_game.getStartCondition()).thenReturn(new LogicCondition(startCond));
        when(_game.getWinCondition()).thenReturn(new LogicCondition());
        when(_game.getActions()).thenReturn(Collections.emptyList());
        gm.setGame(_game);
        Player p1 = new Player("1");
        Player p2 = new Player("2");
        gm.addPlayer(p1);
        gm.addPlayer(p2);

        assertFalse(gm.isStartable());

        p1.setPosition(new Point(10, 10));
        assertFalse(gm.isStartable());

        p2.setPosition(new Point(10, 10));
        assertTrue(gm.isStartable());
    }

    @Test
    public void testStartConditionOWL() throws Exception {
        ConfigParser op = new OWLParser(_path);
        GameManager gm = GameManager.getInstance(null, null);
        gm.setGame(op.getGeogame("http://clemensklug.de/uni/ba/geogame/test/startcondition#game"));
        Player p1 = new Player("1");
        Player p2 = new Player("2");
        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        gm.addAllPlayers(players);

        assertTrue(gm.isStartable());
        assertTrue(gm.startGame());
    }

    @Test
    public void testStartConditionOWLWithGetInstance() throws Exception {
        ConfigParser op = new OWLParser(_path);
        GameManager gm = GameManager.getInstance(op, "http://clemensklug.de/uni/ba/geogame/test/startcondition#game");
        Player p1 = new Player("1");
        Player p2 = new Player("2");
        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        gm.addAllPlayers(players);

        assertTrue(gm.isStartable());
        assertTrue(gm.startGame());
    }

    @Test
    public void testDrawConditionOWLWithGetInstance() throws Exception {
        ConfigParser op = new OWLParser(_path_DRAW);
        GameManager gm = GameManager.getInstance(op, "http://clemensklug.de/uni/ba/geogame/test/drawcondition#game");
        Player p1 = new Player("1");
        gm.addPlayer(p1);

        assertTrue(gm.isStartable());
        assertTrue(gm.startGame());
        assertFalse(gm.isOver());
        assertFalse(gm.isDraw());
        assertFalse(gm.isWon());
        Thread.sleep(1001);
        gm.update(_testProvider, p1);
        assertTrue(gm.isOver());
        assertFalse(gm.isWon());
        assertTrue(gm.isDraw());
    }

    @Test(expected = IllegalAccessError.class)
    public void testDrawConditionOWLLateUpdateFails() throws Exception {
        ConfigParser op = new OWLParser(_path_DRAW);
        GameManager gm = GameManager.getInstance(op, "http://clemensklug.de/uni/ba/geogame/test/drawcondition#game");
        Player p1 = new Player("1");
        gm.addPlayer(p1);

        assertTrue(gm.isStartable());
        assertTrue(gm.startGame());
        assertFalse(gm.isOver());
        assertFalse(gm.isDraw());
        assertFalse(gm.isWon());
        Thread.sleep(1001);
        gm.update(_testProvider, p1);
        assertTrue(gm.isOver());
        assertFalse(gm.isWon());
        assertTrue(gm.isDraw());
        gm.update(_testProvider, p1);
    }

    @Test
    public void testSetPlayers() throws Exception {
        GameManager gm = GameManager.getInstance(null, null);
        GeogameConfig _game = mock(GeogameConfig.class);
        when(_game.getActions()).thenReturn(Collections.emptyList());
        when(_game.getDrawCondition()).thenReturn(new LogicCondition());
        when(_game.getStartCondition()).thenReturn(new LogicCondition());
        when(_game.getWinCondition()).thenReturn(new LogicCondition());
        when(_game.getPlayerCount()).thenReturn(1);
        gm.setGame(_game);
        assertNotNull(GameManager.getPlayers());
        assertTrue(GameManager.getPlayers().isEmpty());
        gm.addPlayer(Mockito.mock(Player.class));
        assertFalse(GameManager.getPlayers().isEmpty());
    }

    @Test
    public void testVoronoiMode() throws Exception {
        GameManager gm = GameManager.getInstance(null, null);
        GeogameConfig _game = mock(GeogameConfig.class);
        List<Point> points = new LinkedList<>();
        points.add(new Point(30, 30, 45));
        points.add(new Point(30, 90, 45));
        points.add(new Point(90, 20, 45));
        points.add(new Point(85, 85, 45));
        List<Action> actions = new LinkedList<>();
        for (Point p : points) {
            Action a = new EchoAction();
            a.setTriggers(points.stream().filter(point -> point == p).collect(Collectors.toList()));
            a.setName(String.valueOf(points.indexOf(p)));
            ((EchoAction) a).setText(p.toString() + " - " + a.getName());
            actions.add(a);
        }
        when(_game.getActions()).thenReturn(actions);
        when(_game.getDrawCondition()).thenReturn(new LogicCondition());
        LogicCondition t = new LogicCondition();
        t.setLogicMode(LogicCondition.LogicMode.T);
        t.setConditions(Collections.emptyList());
        when(_game.getStartCondition()).thenReturn(t);
        when(_game.getWinCondition()).thenReturn(new LogicCondition());
        when(_game.getPlayerCount()).thenReturn(1);
        when(_game.getTriggeringMode()).thenReturn(TriggeringMode.SIMPLIFIED_VORONOI);
        gm.setGame(_game);
        Player p1 = new Player("1");
        gm.addPlayer(p1);
        assertTrue(gm.startGame());
        p1.setPosition(new Point(80, 80, 5));
        assertTrue(gm.update(p1).get(0).getReference().getName().equals("3"));
        gm.getActions().forEach(action -> action.setEnabled(true));
        p1.setPosition(new Point(40, 40, 5));
        assertTrue(gm.update(p1).get(0).getReference().getName().equals("0"));
        gm.getActions().forEach(action -> action.setEnabled(true));
        p1.setPosition(new Point(55, 40, 5));
        assertTrue(gm.update(p1).get(0).getReference().getName().equals("0"));
        gm.getActions().forEach(action -> action.setEnabled(true));
        p1.setPosition(new Point(40, 55, 5));
        assertTrue(gm.update(p1).get(0).getReference().getName().equals("0"));
        gm.getActions().forEach(action -> action.setEnabled(true));
        p1.setPosition(new Point(40, 61, 5));
        assertTrue(gm.update(p1).get(0).getReference().getName().equals("1"));
        gm.getActions().forEach(action -> action.setEnabled(true));
        p1.setPosition(new Point(61, 40, 5));
        assertTrue(gm.update(p1).get(0).getReference().getName().equals("0"));
        gm.getActions().forEach(action -> action.setEnabled(true));
        p1.setPosition(new Point(70, 40, 5));
        assertTrue(gm.update(p1).get(0).getReference().getName().equals("2"));
        gm.getActions().forEach(action -> action.setEnabled(true));
    }
}