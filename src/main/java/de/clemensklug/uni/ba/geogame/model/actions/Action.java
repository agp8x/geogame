/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame.model.actions;

import de.clemensklug.uni.ba.geogame.GameManager;
import de.clemensklug.uni.ba.geogame.model.GeogameObject;
import de.clemensklug.uni.ba.geogame.model.Player;
import de.clemensklug.uni.ba.geogame.model.challenge.Challenge;
import de.clemensklug.uni.ba.geogame.model.spatial.Point;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Base class for handling events at locations
 *
 * @author clemens
 */
public abstract class Action implements GeogameObject {
    final Logger log = LogManager.getLogger(this.getClass());
    private final List<Challenge> _challenges = new CopyOnWriteArrayList<>();
    private final Map<Player, List<Challenge>> _playerChallengeMap = new ConcurrentHashMap<>();
    protected List<Point> _triggers = new CopyOnWriteArrayList<>();
    /**
     * Is this action currently doable
     */
    private boolean _enabled = true;
    /**
     * Is this action currently active, unfinished
     */
    private boolean _active = false;
    private String _name;

    /**
     * Perform an action as reaction to a reached position
     *
     * @param trigger <code>Point</code> being reached
     * @param event   <code>Point</code> of actual position
     * @param p       <code>Player</code> for which the action is performed
     * @return action is completed, no longer active, and triggered point
     * should be removed (true) OR kept active, and point should be kept
     */
    protected abstract boolean action(Point trigger, Point event, Player p);

    /**
     * shortcut for updating already triggered actions
     *
     * @param previousStatus ActionStatus of previous triggering
     * @return new ActionStatus
     */
    public ActionStatus trigger(ActionStatus previousStatus) {
        return trigger(previousStatus.getEvent(), previousStatus.getPlayer());
    }

    /**
     * @param event  current position
     * @param player active player
     * @return ActionStatus
     */
    public ActionStatus trigger(Point event, Player player) {
        //TODO: concurrency
        try {
            Point trigger = _triggers.stream().filter(point -> Point.triggerableIntersects(event, point)).collect(Collectors.toList()).get(0);
            log.trace("triggered by " + event + ", matching " + trigger + ", by " + player);
            if (isEnabled()) {
                setActive(true);
                if (!_playerChallengeMap.containsKey(player)) {
                    startChallenges(player);
                }
                if (action(trigger, event, player) && GameManager.isGameRunning()) {
                    setActive(false);
                    setEnabled(false);
                    log.trace("disabled finished action " + this);
                }
            }
        } catch (IndexOutOfBoundsException e) {
            log.fatal("could not find triggerable intersection with event: " + event + " ; Action: " + getName());
        }
        return new ActionStatus(this, event, player);
    }

    private void startChallenges(Player player) {
        if (!_challenges.isEmpty()) {
            List<Challenge> challenges = _challenges.stream().map(Challenge::clone).collect(Collectors.toList());
            challenges.forEach(Challenge::startChallenge);
            _playerChallengeMap.put(player, challenges);
        }
    }

    /**
     * @return whether challenge is solved or not
     */
    boolean checkChallenges(Player player) {
        return _challenges.isEmpty() || _playerChallengeMap.containsKey(player) && areAllChallengesDone(_playerChallengeMap.get(player));
    }

    private boolean areAllChallengesDone(List<Challenge> list) {
        //return list.stream().map(Challenge::isFinished).reduce(Boolean.TRUE, (a, b) -> a && b);
        return !list.stream().filter(challenge -> !challenge.isFinished()).findFirst().isPresent();
    }

    public boolean isEnabled() {
        return _enabled;
    }

    public void setEnabled(boolean enabled) {
        this._enabled = enabled;
    }

    public boolean isActive() {
        return _active;
    }

    void setActive(boolean active) {
        this._active = active;
    }

    public List<Point> getTriggers() {
        return _triggers;
    }

    public void setTriggers(List<Point> triggers) {
        this._triggers = triggers;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    protected abstract String toStringExtra();

    @Override
    public String toString() {
        return "Action{" +
                "name=" + _name +
                ", enabled=" + _enabled +
                ", active=" + _active +
                ", triggers=" + _triggers +
                ", challenges=" + _challenges +
                ", " + toStringExtra() +
                '}';
    }

    public void addChallenge(Challenge challenge) {
        _challenges.add(challenge);
    }

    public List<Challenge> getChallenges(Player player) {
        if (player == null) {
            log.info("player in getChallenges is null, returning base challenges");
            return _challenges;
        }
        return _playerChallengeMap.get(player);
    }
}
