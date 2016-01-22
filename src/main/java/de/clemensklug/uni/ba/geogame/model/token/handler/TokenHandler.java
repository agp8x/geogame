package de.clemensklug.uni.ba.geogame.model.token.handler;

import de.clemensklug.uni.ba.geogame.model.GeogameObject;
import de.clemensklug.uni.ba.geogame.model.Player;
import de.clemensklug.uni.ba.geogame.model.token.TokenSet;

import java.util.Map;

/**
 * @author clemens
 */
public interface TokenHandler extends GeogameObject {
    /**
     * leave a token from player at this handler
     *
     * @param player player
     * @return success of dropping
     */
    boolean drop(Player player);

    /**
     * get Tokens placed at this handler
     *
     * @return Tokens
     */
    Map<Player, TokenSet> getTokens();

    /**
     * when active, a handler can interact with players
     *
     * @return status
     */
    boolean isActive();

    /**
     * add Tokens to players Inventory
     *
     * @param player player
     * @return tokens for player
     */
    TokenSet pickup(Player player);

}
