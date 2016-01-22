package de.clemensklug.uni.ba.geogame.model.conditions;

import de.clemensklug.uni.ba.geogame.GameManager;
import de.clemensklug.uni.ba.geogame.model.Player;
import de.clemensklug.uni.ba.geogame.model.spatial.Point;

import java.util.LinkedList;
import java.util.List;

/**
 * @author clemens
 */
public class PlayerLocationCondition extends Condition {
    private List<Point> _points;

    @Override
    public boolean isSatisfied() {
        if (null == _points) {
            return false;
        }
        boolean status = true;
        for (Player p : GameManager.getPlayers()) {
            status = status && Point.triggerableIntersectsAny(p.getPosition(), _points);
        }
        return status;
    }

    @Override
    public List<Player> getSatisfyingPlayers() {
        List<Player> playerList = new LinkedList<>();
        if (null == _points) {
            return playerList;
        }
        playerList.addAll(GameManager.getPlayers());
        playerList.removeIf(player -> !Point.triggerableIntersectsAny(player.getPosition(), _points));
        return playerList;
    }

    public void setPoints(List<Point> point) {
        _points = point;
    }
}
