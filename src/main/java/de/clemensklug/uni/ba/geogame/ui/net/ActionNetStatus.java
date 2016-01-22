package de.clemensklug.uni.ba.geogame.ui.net;

import de.clemensklug.uni.ba.geogame.model.Player;
import de.clemensklug.uni.ba.geogame.model.Point;
import de.clemensklug.uni.ba.geogame.model.actions.Action;
import de.clemensklug.uni.ba.geogame.model.actions.ActionStatus;
import de.clemensklug.uni.ba.geogame.model.challenge.Challenge;
import de.clemensklug.uni.ba.geogame.model.challenge.ChallengeData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author clemens
 */
public class ActionNetStatus implements Serializable {
    private final boolean _active;
    private final Point _event;
    private final Player _player;
    private final List<ChallengeData> _challenges;
    private final String _action;

    public ActionNetStatus(boolean active, Point event, Player player, List<Challenge> challenges, String action) {
        _active = active;
        _event = event;
        _player = player;
        _action = action;
        _challenges = new ArrayList<>();
        if (challenges != null) {
            challenges.forEach(challenge -> _challenges.add(challenge.getData()));
        }
    }

    public boolean isActive() {
        return _active;
    }

    public Point getEvent() {
        return _event;
    }

    public Player getPlayer() {
        return _player;
    }

    public List<ChallengeData> getChallenges() {
        return _challenges;
    }

    public String getAction() {
        return _action;
    }

    @Override
    public String toString() {
        return "ActionNetStatus{" +
                "_active=" + _active +
                ", _event=" + _event +
                ", _player=" + _player +
                ", _challenges=" + _challenges +
                ", _action='" + _action + '\'' +
                '}';
    }
}
