/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame.games;

import de.clemensklug.uni.ba.geogame.GameManager;
import de.clemensklug.uni.ba.geogame.location.IteratorLocationProvider;
import de.clemensklug.uni.ba.geogame.location.LocationProvider;
import de.clemensklug.uni.ba.geogame.location.StaticLocationProvider;
import de.clemensklug.uni.ba.geogame.model.Player;
import de.clemensklug.uni.ba.geogame.model.spatial.Point;
import de.clemensklug.uni.ba.geogame.model.actions.Action;
import de.clemensklug.uni.ba.geogame.model.actions.ActionStatus;
import de.clemensklug.uni.ba.geogame.model.actions.TokenAction;
import de.clemensklug.uni.ba.geogame.model.challenge.Challenge;
import de.clemensklug.uni.ba.geogame.model.challenge.QuestionChallenge;
import de.clemensklug.uni.ba.geogame.model.challenge.SyncTimeChallenge;
import de.clemensklug.uni.ba.geogame.model.token.handler.TokenCapture;
import de.clemensklug.uni.ba.geogame.model.token.handler.TokenHandler;
import de.clemensklug.uni.ba.geogame.parser.ConfigParser;
import de.clemensklug.uni.ba.geogame.parser.OWLParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author clemens
 */
public class GeoTTTTest {
    private final static String _path = "src/main/resources/geoTTT.owl";
    private static final String _NAME = "http://clemensklug.de/uni/ba/geogame/geoTTT#GeoTTTgame";
    private final static String _CHALLENGE_PATH = "src/test/resources/passChallengeST.owl";
    private LocationProvider _locationProvider;
    private GameManager _gameManager;
    private List<Player> _players;
    private Map<Player, List<Point>> _map;
    private final Point[][] _points = {
            {new Point(10, 10), new Point(10, 20), new Point(10, 30)},
            {new Point(20, 10), new Point(20, 20), new Point(20, 30)},
            {new Point(30, 10), new Point(30, 20), new Point(30, 30)},
    };
    private final List<List<Point>> _winning = winning_configs();

    @Before
    public void setUp() throws Exception {
        ConfigParser cp = new OWLParser(_path);
        _gameManager = GameManager.getInstance(cp, _NAME);
        _players = new ArrayList<>();
        _map = new HashMap<>();
        for (int i = 0; i < 2; i++) {
            final Player player = new Player(String.valueOf(i));
            player.setPosition(new Point(-10, -10));
            _players.add(player);
        }
        _gameManager.addAllPlayers(_players);
        assertTrue(_gameManager.startGame());
        _players.forEach(player -> player.moveToDisposableInventory(player.getInventory()));
    }

    @After
    public void tearDown() throws Exception {
        _gameManager.exit();
        _gameManager = null;
        _locationProvider = null;
        _players = null;
        _map = null;

    }

    private void runGame() {
        int i = 0;
        while (!_gameManager.isWon()) {
            Player player = _players.get(i % _players.size());
            List<ActionStatus> statuses = _gameManager.update(_locationProvider, player);
            for (ActionStatus s : statuses) {
                if (s.getReference().isActive()) {
                    for (Challenge c : s.getChallenges()) {
                        if (c instanceof SyncTimeChallenge) {
                            final int syncTime = ((SyncTimeChallenge) c).getSyncTime();
                            try {
                                Thread.sleep(syncTime * 1100);//add some more time to make sure the timer has finished
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        if (c instanceof QuestionChallenge) {
                            ((QuestionChallenge) c).setIsAnswered(true);
                        }
                        s.getReference().trigger(s);
                    }
                }
            }
            i++;
            if (i > 10) {
                fail("did not finish game");
            }
        }
    }

    @Test
    public void testWin0() {
        _map.put(_players.get(0), _winning.get(0));
        _map.put(_players.get(1), _winning.get(1));
        _locationProvider = new IteratorLocationProvider(_map);
        runGame();
        assertEquals(_players.get(0), _gameManager.getLastPlayer());
    }

    @Test
    public void testWin1() {
        _map.put(_players.get(0), _winning.get(1));
        _map.put(_players.get(1), _winning.get(2));
        _locationProvider = new IteratorLocationProvider(_map);
        runGame();
        assertEquals(_players.get(0), _gameManager.getLastPlayer());
    }

    @Test
    public void testWin2() {
        _map.put(_players.get(0), _winning.get(2));
        _map.put(_players.get(1), _winning.get(0));
        _locationProvider = new IteratorLocationProvider(_map);
        runGame();
        assertEquals(_players.get(0), _gameManager.getLastPlayer());
    }

    @Test
    public void testWin2b() {
        _map.put(_players.get(0), _winning.get(2));
        _map.put(_players.get(1), _winning.get(3));
        _locationProvider = new IteratorLocationProvider(_map);
        runGame();
        assertEquals(_players.get(1), _gameManager.getLastPlayer());
    }

    @Test
    public void testWin3() {
        _map.put(_players.get(0), _winning.get(3));
        _map.put(_players.get(1), _winning.get(4));
        _locationProvider = new IteratorLocationProvider(_map);
        runGame();
        assertEquals(_players.get(0), _gameManager.getLastPlayer());
    }

    @Test
    public void testWin4() {
        _map.put(_players.get(0), _winning.get(4));
        _map.put(_players.get(1), _winning.get(5));
        _locationProvider = new IteratorLocationProvider(_map);
        runGame();
        assertEquals(_players.get(0), _gameManager.getLastPlayer());
    }

    @Test
    public void testWin5() {
        _map.put(_players.get(0), _winning.get(5));
        _map.put(_players.get(1), _winning.get(6));
        _locationProvider = new IteratorLocationProvider(_map);
        runGame();
        assertEquals(_players.get(0), _gameManager.getLastPlayer());
    }

    @Test
    public void testWin6() {
        _map.put(_players.get(0), _winning.get(6));
        _map.put(_players.get(1), _winning.get(7));
        _locationProvider = new IteratorLocationProvider(_map);
        runGame();
        assertEquals(_players.get(0), _gameManager.getLastPlayer());
    }

    @Test
    public void testWin7() {
        _map.put(_players.get(0), _winning.get(7));
        _map.put(_players.get(1), _winning.get(6));
        _locationProvider = new IteratorLocationProvider(_map);
        runGame();
        assertEquals(_players.get(0), _gameManager.getLastPlayer());
    }

    @Test
    public void testWin7b() {
        _map.put(_players.get(0), _winning.get(7));
        _map.put(_players.get(1), _winning.get(0));
        _locationProvider = new IteratorLocationProvider(_map);
        runGame();
        assertEquals(_players.get(1), _gameManager.getLastPlayer());
    }

    private List<List<Point>> winning_configs() {
        List<List<Point>> list = new ArrayList<>();
        List<Point> points0 = new ArrayList<>();
        points0.add(_points[2][0]);
        points0.add(_points[2][1]);
        points0.add(_points[2][2]);
        List<Point> points1 = new ArrayList<>();
        points1.add(_points[1][2]);
        points1.add(_points[1][0]);
        points1.add(_points[1][1]);
        List<Point> points2 = new ArrayList<>();
        points2.add(_points[0][1]);
        points2.add(_points[0][0]);
        points2.add(_points[0][2]);
        List<Point> points3 = new ArrayList<>();
        points3.add(_points[0][2]);
        points3.add(_points[1][2]);
        points3.add(_points[2][2]);
        List<Point> points4 = new ArrayList<>();
        points4.add(_points[0][1]);
        points4.add(_points[1][1]);
        points4.add(_points[2][1]);
        List<Point> points5 = new ArrayList<>();
        points5.add(_points[0][0]);
        points5.add(_points[1][0]);
        points5.add(_points[2][0]);
        List<Point> points6 = new ArrayList<>();
        points6.add(_points[0][0]);
        points6.add(_points[1][1]);
        points6.add(_points[2][2]);
        List<Point> points7 = new ArrayList<>();
        points7.add(_points[0][2]);
        points7.add(_points[1][1]);
        points7.add(_points[2][0]);
        list.add(points0);
        list.add(points1);
        list.add(points2);
        list.add(points3);
        list.add(points4);
        list.add(points5);
        list.add(points6);
        list.add(points7);
        return list;
    }

    @Test
    public void testPassChallenge() throws Exception {
        OWLParser parser = new OWLParser(_CHALLENGE_PATH);
        GameManager gm = GameManager.getInstance(parser, "http://clemensklug.de/uni/ba/geogame/test/timeout#game");
        Player p = Mockito.mock(Player.class);
        when(p.getPosition()).thenReturn(new Point());
        gm.addPlayer(p);
        TokenHandler th = mock(TokenCapture.class);
        when(th.drop(p)).thenReturn(true);
        ((TokenAction) gm.getActions().get(0)).setHandler(th);
        assertTrue(gm.isStartable());
        assertTrue(gm.startGame());
        LocationProvider loc = new StaticLocationProvider(new Point(1, 1));
        List<ActionStatus> statuses = gm.update(loc, p);
        assertFalse(statuses.isEmpty());
        Challenge challenge = statuses.get(0).getChallenges().get(0);
        assertFalse(challenge.isFinished());
        Thread.sleep(challenge.getData().getSyncTime() * 1000 + 50);
        assertTrue(challenge.isFinished());
        Action reference = statuses.get(0).getReference();
        assertTrue(reference.isActive());
        statuses = gm.update(loc, p);
        verify(th).drop(p);
        assertFalse(statuses.isEmpty());
        challenge = statuses.get(0).getChallenges().get(0);
        assertTrue(challenge.isFinished());
        reference = statuses.get(0).getReference();
        assertFalse(reference.isActive());
        assertFalse(reference.isEnabled());
    }
    /*
points:

00 01 02
10 11 12
20 21 22

tokenhandler:

0 1 2
3 4 5
6 7 8

0: _point[0][0]
1: _point[0][1]
2: _point[0][2]
3: _point[1][0]
4: _point[1][1]
5: _point[1][2]
6: _point[2][0]
7: _point[2][1]
8: _point[2][2]

conditions:

0: 678
1: 345
2: 012
3: 258
4: 147
5: 036
6: 048
7: 246
     */
}