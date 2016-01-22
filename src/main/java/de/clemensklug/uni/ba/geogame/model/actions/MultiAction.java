package de.clemensklug.uni.ba.geogame.model.actions;

import de.clemensklug.uni.ba.geogame.model.Player;
import de.clemensklug.uni.ba.geogame.model.Point;

import java.util.LinkedList;
import java.util.List;

/**
 * This Action groups multiple actions together.
 * Subactions do not get triggered, they can perform their actions when this Action is triggered.
 * Challenges of subactions are ignored, too.
 * <p>
 * <p>
 * Created by clemens on 05.01.16.
 *
 * @author clemens
 */
public class MultiAction extends Action {
    private List<Action> _actions = new LinkedList<>();

    @Override
    protected boolean action(Point trigger, Point event, Player p) {
        return _actions.stream().map(action -> action.action(trigger, event, p)).reduce(Boolean.FALSE, Boolean::logicalOr);
    }

    @Override
    protected String toStringExtra() {
        return "_actions=" + _actions;
    }

    public List<Action> getActions() {
        return _actions;
    }

    public void setActions(List<Action> actions) {
        _actions = actions;
    }

    @Override
    void setActive(boolean active) {
        super.setActive(active);
        _actions.forEach(action -> action.setActive(active));
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        _actions.forEach(action -> action.setEnabled(enabled));
    }
}
