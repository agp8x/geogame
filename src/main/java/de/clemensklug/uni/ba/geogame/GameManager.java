package de.clemensklug.uni.ba.geogame;

import de.clemensklug.uni.ba.geogame.location.LocationProvider;
import de.clemensklug.uni.ba.geogame.model.GeogameConfig;
import de.clemensklug.uni.ba.geogame.model.Player;
import de.clemensklug.uni.ba.geogame.model.actions.Action;
import de.clemensklug.uni.ba.geogame.model.actions.ActionStatus;
import de.clemensklug.uni.ba.geogame.model.conditions.Condition;
import de.clemensklug.uni.ba.geogame.model.conditions.LogicCondition;
import de.clemensklug.uni.ba.geogame.model.spatial.Point;
import de.clemensklug.uni.ba.geogame.parser.ConfigParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * handle updates of locations, notify actions
 *
 * @author clemens
 */
public class GameManager {
    private static List<Player> players = new CopyOnWriteArrayList<>();
    private static boolean _running = false;
    private final Logger log = LogManager.getLogger(this.getClass());
    private List<Action> _actions;
    private LogicCondition _winCondition;
    private LogicCondition _startCondition;
    private LogicCondition _drawCondition;
    private Player _last;
    private boolean _started = false;
    private int _numPlayers;
    private GeogameConfig _game;

    private GameManager() {
    }

    public static List<Player> getPlayers() {
        return players;
    }

    public static boolean isGameRunning() {
        return _running;
    }

    public static GameManager getInstance(ConfigParser parser, String game) {
        GameManager gameManager = new GameManager();
        players.removeIf(player -> true);
        if (parser != null && game != null) {
            gameManager.setGame(parser.getGeogame(game));
        }
        return gameManager;
    }

    public void addAllPlayers(List<Player> newPlayers) {
        guard("players");
        if (players.size() + newPlayers.size() > _numPlayers) {
            throw new IllegalArgumentException("players exceed limit");
        }
        players.addAll(newPlayers);
    }

    public void addPlayer(Player player) {
        guard("players");
        if (_numPlayers == players.size()) {
            throw new IllegalArgumentException("there are already maximum players");
        }
        players.add(player);
    }

    public boolean startGame() {
        if (_started) {
            return false;
        }
        if (isStartable()) {
            _startCondition.runSatisfiedAction();
            _started = true;
            GameManager._running = true;
        }
        return _started;
    }

    private void stopGame() {
        _running = false;
    }

    public boolean isStartable() {
        return _actions != null && _winCondition != null && players != null && checkCondition(_startCondition);
    }

    public List<ActionStatus> update(Player player) {
        assertGameIsStarted();
        assertPlayerInGame(player);
        return update(player.getPosition(), player);
    }

    public List<ActionStatus> update(LocationProvider locationProvider, Player player) {
        //TODO: concurrency
        assertGameIsStarted();
        assertPlayerInGame(player);
        Point position = locationProvider.getPosition(player);
        player.setPosition(position);
        return update(position, player);
    }

    private List<ActionStatus> update(Point position, Player p0) {
        assertGameIsStarted();
        //No concurrency issues here
        List<ActionStatus> statuses = new ArrayList<>();
        log.info("updating player " + p0 + "@ " + position);
        for (Action a : _actions) {
            if (a.isEnabled()) {
                if (Point.triggerableIntersectsAny(position, a.getTriggers())) {
                    log.debug("trigger action " + a.getName());
                    statuses.add(a.trigger(position, p0));
                }
            } else {
                log.debug("->action no longer active: " + a.getName());
            }
        }
        _last = p0;
        if (isDraw() || isWon()) {
            stopGame();
        }
        return statuses;
    }

    public List<Action> getActions() {
        return _actions;
    }

    public boolean isWon() {
        return _started && checkCondition(_winCondition);
    }

    public boolean isDraw() {
        return _started && checkCondition(_drawCondition);
    }

    private boolean checkCondition(Condition c) {
        return null != c && c.isSatisfied();
    }

    public List<Player> getWinningPlayers() {
        return _winCondition.getSatisfyingPlayers();
    }

    public Player getLastPlayer() {
        return _last;
    }

    @Override
    public String toString() {
        return "GameManager{" +
                "actions=" + _actions + ", " +
                "started=" + _started +
                '}';
    }

    public void exit() {
        log.exit();
    }

    private void guard(String unit) {
        if (_started) {
            throw new IllegalAccessError("Changing " + unit + " after starting the game is currently not supported");
        }
    }

    private void assertGameIsStarted() {
        if (!_started || !_running) {
            throw new IllegalAccessError("Start game first! (maybe it's already over)");
        }
    }

    public boolean isOver() {
        // !_started: game was not started yet
        // !_running: game has already been stopped
        return _started && !_running;
    }

    private void assertPlayerInGame(Player player) {
        if (!players.contains(player)) {
            throw new IllegalArgumentException("Player '" + player + "' not in game");
        }
    }

    public GeogameConfig getGame() {
        return _game;
    }

    public void setGame(GeogameConfig game) {
        guard("game config");
        _actions = game.getActions();
        _drawCondition = game.getDrawCondition();
        _startCondition = game.getStartCondition();
        _winCondition = game.getWinCondition();
        _numPlayers = game.getPlayerCount();
        _game = game;
        _game.setPlayers(players);
    }
}
