package de.clemensklug.uni.ba.geogame.ui.net;

import de.clemensklug.uni.ba.geogame.model.Player;
import de.clemensklug.uni.ba.geogame.model.Point;
import de.clemensklug.uni.ba.geogame.model.actions.Action;
import de.clemensklug.uni.ba.geogame.model.actions.ActionStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author clemens
 */
public class TransferObject implements Serializable {
    private final Status _status;
    private final Player _player;
    private final List<ActionNetStatus> _statuses;
    private final Point _point;

    public Point getPoint() {
        return _point;
    }

    public TransferObject(Status status, Player player, List<ActionStatus> statuses, Point point) {
        _status = status;
        _player = player;
        if (player != null) {
            _player.setPosition(player.getPosition());
        }
        if (statuses != null) {
            _statuses = copyActionStatuses(statuses);
        } else {
            _statuses = null;
        }
        _point = point;
    }

    private List<ActionNetStatus> copyActionStatuses(List<ActionStatus> statuses) {
        List<ActionNetStatus> copied = new ArrayList<>();
        for (ActionStatus a : statuses) {
            ActionNetStatus n = new ActionNetStatus(a.getReference().isActive(), a.getEvent(), a.getPlayer(), a.getChallenges(), a.getReference().getName());
            copied.add(n);
        }
        return copied;
    }

    public Status getStatus() {
        return _status;
    }

    public Player getPlayer() {
        return _player;
    }

    public List<ActionNetStatus> getStatuses() {
        return _statuses;
    }

    @Override
    public String toString() {
        return "TransferObject{" +
                "_status=" + _status +
                ", _player=" + _player +
                ", _statuses=" + _statuses +
                ", _point=" + _point +
                '}';
    }

    public enum Status {ADD_PLAYER, UPDATE_PLAYER, GAME_STARTED, GAME_NOT_STARTED, START_FAILED, UPDATE, GAME_OVER, QUERY_POINTS, QUERY_POINT_INIT}
}
