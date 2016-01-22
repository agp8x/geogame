package de.clemensklug.uni.ba.geogame.model.actions;

import de.clemensklug.uni.ba.geogame.model.Player;
import de.clemensklug.uni.ba.geogame.model.spatial.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * @author clemens
 */
public class EnableOtherActionAction extends Action {
    private List<Action> _enable = new ArrayList<>();
    private List<Action> _disable = new ArrayList<>();

    @Override
    protected boolean action(Point trigger, Point event, Player p) {
        log.info(this.getName() + " triggered @" + trigger);
        _enable.forEach(action -> action.setEnabled(true));
        _disable.forEach(action -> action.setEnabled(false));
        return true;
    }

    @Override
    protected String toStringExtra() {
        return "enables=" + _enable.toString() + ", disables=" + _disable.toString();
    }

    public void setEnableActions(List<Action> actions) {
        _enable = actions;
    }

    public void setDisableActions(List<Action> disable) {
        _disable = disable;
    }
}
