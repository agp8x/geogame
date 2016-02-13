/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame.model.actions;

import de.clemensklug.uni.ba.geogame.model.Player;
import de.clemensklug.uni.ba.geogame.model.spatial.Point;
import de.clemensklug.uni.ba.geogame.model.token.TokenSet;
import de.clemensklug.uni.ba.geogame.model.token.handler.TokenDispenser;
import de.clemensklug.uni.ba.geogame.model.token.handler.TokenHandler;

/**
 * @author clemens
 */
public class TokenAction extends Action {
    private TokenHandler _handler;

    @Override
    protected boolean action(Point trigger, Point event, Player p) {
        if (_handler == null) {
            log.debug("handler at " + getName() + " is null; disable action...");
            return true;
        }
        log.trace("CHECK CHALLENGE");
        if (checkChallenges(p)) {
            if (_handler instanceof TokenDispenser) {
                final TokenSet pickup = _handler.pickup(p);
                p.addAllToInventory(pickup.getTokens());
                log.info("picked up token for player " + p + "; tokens: '" + pickup + "' @" + this.getName() + ", active:" + _handler.isActive());
            } else {
                if (_handler.drop(p)) {
                    log.info("dropped token @" + this.getName() + ", total count:" + _handler.getTokens());
                } else {
                    log.error("dropping token @" + this.getName() + " failed for player " + p.getName());
                }
            }
            return !_handler.isActive();//disable this action if handler is no longer active
        } else {
            log.info("attempted to pick up token @" + this, getName() + ", waiting for completion of challenge");
        }
        return false;
    }

    @Override
    protected String toStringExtra() {
        if (_handler != null) {
            return "tokens: " + _handler.getTokens().toString();
        }
        return "TokenAction(no handler)";
    }

    public TokenHandler getHandler() {
        return _handler;
    }

    public void setHandler(TokenHandler h) {
        _handler = h;
    }
}
