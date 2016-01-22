package de.clemensklug.uni.ba.geogame.model.actions;

import de.clemensklug.uni.ba.geogame.model.Player;
import de.clemensklug.uni.ba.geogame.model.spatial.Point;

/**
 * Debug Action
 *
 * @author clemens
 */
public class EchoAction extends Action {
    private String _text = "";

    @Override
    protected boolean action(Point trigger, Point event, Player p) {
        log.info(this.getName() + " triggered @" + trigger);
        log.fatal("NOTFATAL: msg: " + _text);
        return true;
    }

    @Override
    protected String toStringExtra() {
        return "EchoAction";
    }

    public String getText() {
        return _text;
    }

    public void setText(String text) {
        _text = text;
    }
}
