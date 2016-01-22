package de.clemensklug.uni.ba.geogame.model.token.handler;

import de.clemensklug.uni.ba.geogame.model.Player;
import de.clemensklug.uni.ba.geogame.model.token.Token;
import de.clemensklug.uni.ba.geogame.model.token.TokenSet;

/**
 * @author clemens
 */
public class TokenCapture extends BaseHandler implements TokenHandler {

    @Override
    public boolean drop(Player player) {
        if (_map.isEmpty()) {
            Token token = player.popDisposableItem();
            if (log.isDebugEnabled()) {
                log.debug("token from disposableInventory(" + player.getName() + ": " + token);
            }
            if (token.isValid()) {
                _map.put(player, new TokenSet(token));
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isActive() {
        return _map.isEmpty();
    }

}
