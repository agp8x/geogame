/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame.games;

import de.clemensklug.uni.ba.geogame.GameManager;
import de.clemensklug.uni.ba.geogame.location.IteratorLocationProvider;
import de.clemensklug.uni.ba.geogame.location.LocationProvider;
import de.clemensklug.uni.ba.geogame.model.Player;
import de.clemensklug.uni.ba.geogame.model.spatial.Point;
import de.clemensklug.uni.ba.geogame.model.actions.ActionStatus;
import de.clemensklug.uni.ba.geogame.model.challenge.Challenge;
import de.clemensklug.uni.ba.geogame.model.challenge.QuestionChallenge;
import de.clemensklug.uni.ba.geogame.model.challenge.SyncTimeChallenge;
import de.clemensklug.uni.ba.geogame.parser.ConfigParser;
import de.clemensklug.uni.ba.geogame.parser.OWLParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * @author clemens
 */
public class ParallelGeoTTTTest {
    private final static String _path = "src/test/resources/geoTTT.owl";
    private static final String _NAME = "http://clemensklug.de/uni/ba/geogame/geoTTT#GeoTTTgame";
    private LocationProvider _locationProvider;
    private GameManager _gameManager;
    private List<Player> _players;
    private Map<Player, List<Point>> _map;
    private final Logger log = LogManager.getLogger(this.getClass());
    private final Point[][] _points = {
            {new Point(0, 0), new Point(0, 1), new Point(0, 2)},
            {new Point(1, 0), new Point(1, 1), new Point(1, 2)},
            {new Point(2, 0), new Point(2, 1), new Point(2, 2)},
    };
    private final List<List<Point>> _winning = winning_configs();

    @Before
    public void setUp() throws Exception {
        ConfigParser cp = new OWLParser(_path);
        _gameManager = GameManager.getInstance(cp,_NAME);
        _players = new ArrayList<>();
        _map = new HashMap<>();
        for (int i = 0; i < 2; i++) {
            final Player player = new Player(String.valueOf(i));
            player.setPosition(new Point(-1,-1));
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
            gameStep(player);

            i++;
            if (i > 10) {
                fail("did not finish game");
            }
        }
    }

    private void gameStep(Player player) {
        // log.trace(player);
        List<ActionStatus> statuses = _gameManager.update(_locationProvider, player);
        for (ActionStatus s : statuses) {
            if (s.getReference().isActive()) {
                for (Challenge c : s.getChallenges()) {
                    if (c instanceof SyncTimeChallenge) {
                        final int syncTime = ((SyncTimeChallenge) c).getSyncTime();
                        try {
                            Thread.sleep(syncTime * 1000);
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
    }

    private void runParallelGame() {
        List<Runnable> runnables = new ArrayList<>();
        for (Player p : _players) {
            log.info("new runnable for player");
            runnables.add(() -> {
                while (!_gameManager.isWon()) {
                    gameStep(p);
                }
            });
        }
        ExecutorService executor = Executors.newScheduledThreadPool(_players.size());
        for (Runnable command : runnables) {
            log.info("submit runnable to executor");
            executor.submit(command);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
            try {
                executor.awaitTermination(1, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void testParallelGame() {
        _map.put(_players.get(0), _winning.get(0));
        _map.put(_players.get(1), _winning.get(1));
        _locationProvider = new IteratorLocationProvider(_map);
        runParallelGame();
        try {
            assertEquals(_players.get(0), _gameManager.getLastPlayer());
            log.error("player 0 has won parallel");
        } catch (AssertionError e) {
            assertEquals(_players.get(1), _gameManager.getLastPlayer());
            log.error("player 1 has won parallel");
        }
    }

    @Test
    public void testWin0() {
        testParallelGame();
    }

    @Test
    public void testWin1() {
        testParallelGame();
    }

    @Test
    public void testWin2() {
        testParallelGame();
    }

    @Test
    public void testWin3() {
        testParallelGame();
    }

    @Test
    public void testWin4() {
        testParallelGame();
    }

    @Test
    public void testWin5() {
        testParallelGame();
    }

    @Test
    public void testWin6() {
        testParallelGame();
    }

    @Test
    public void testWin7() {
        testParallelGame();
    }

    @Test
    public void testWin8() {
        testParallelGame();
    }

    @Test
    public void testWin9() {
        testParallelGame();
    }
/*
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
    }*/

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