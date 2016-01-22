package de.clemensklug.uni.ba.geogame.model.token.handler;

import de.clemensklug.uni.ba.geogame.model.Player;
import de.clemensklug.uni.ba.geogame.model.token.TokenSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * @author clemens
 */
public abstract class BaseHandler implements TokenHandler {
    protected final Logger log = LogManager.getLogger(this.getClass());
    /*TODO:
    * * add confirmation-option (force drop/disperse or ask player what to put/take
    * * disable drop @ disperser
    */
    final Map<Player, TokenSet> _map = new HashMap<>();
    private String _name;

    @Override
    public boolean drop(Player player) {
        _map.put(player, new TokenSet(player.getIdentifier()));
        return true;
    }

    @Override
    public TokenSet pickup(Player player) {
        return null;
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public Map<Player, TokenSet> getTokens() {
        return _map;
    }

    @Override
    public String toString() {
        return _name;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public void setName(String s) {
        _name = s;
    }
}
