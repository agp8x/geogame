package de.clemensklug.uni.ba.geogame.model.conditions;

import de.clemensklug.uni.ba.geogame.GameManager;
import de.clemensklug.uni.ba.geogame.model.GeogameObject;
import de.clemensklug.uni.ba.geogame.model.Player;
import de.clemensklug.uni.ba.geogame.model.actions.Action;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * @author clemens
 */
public abstract class Condition implements GeogameObject {
    final Logger log = LogManager.getLogger(this.getClass());
    private Action _action;
    private String _name = "EMPTY";

    public Action getSatisfiedAction() {
        return _action;
    }

    public void setSatisfiedAction(Action action) {
        _action = action;
    }

    public abstract boolean isSatisfied();

    public abstract List<Player> getSatisfyingPlayers();

    public String getName() {
        return _name;
    }

    public void setName(String s) {
        _name = s;
    }

    @Override
    public String toString() {
        return "Condition{" +
                "name=" + _name + ", " +
                "action=" + _action + "}";
    }

    public void runSatisfiedAction() {
        log.trace("ACTION @ " + _name + ": " + _action);
        if (_action != null) {
            for (Player p : GameManager.getPlayers()) {
                _action.trigger(p.getPosition(), p);
            }
            _action.setEnabled(false);
        }
    }
}
