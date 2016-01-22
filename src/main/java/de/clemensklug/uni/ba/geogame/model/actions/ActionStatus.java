package de.clemensklug.uni.ba.geogame.model.actions;

import de.clemensklug.uni.ba.geogame.model.Player;
import de.clemensklug.uni.ba.geogame.model.challenge.Challenge;
import de.clemensklug.uni.ba.geogame.model.spatial.Point;

import java.io.Serializable;
import java.util.List;

/**
 * @author clemens
 */
public class ActionStatus implements Serializable {
    private final Action _reference;
    private final Point _event;
    private final Player _player;

    public ActionStatus(Action reference, Point event, Player player) {
        _reference = reference;
        _event = event;
        _player = player;
    }

    public Action getReference() {
        return _reference;
    }

    public List<Challenge> getChallenges() {
        return _reference.getChallenges(_player);
    }

    public Point getEvent() {
        return _event;
    }

    public Player getPlayer() {
        return _player;
    }

    @Override
    public String toString() {
        return "ActionStatus{" +
                "_reference=" + _reference.getName() +
                ", _ref-active?=" + _reference.isActive() +
                '}';
    }
}
