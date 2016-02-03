/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame.model.token.handler;

import de.clemensklug.uni.ba.geogame.model.Player;
import de.clemensklug.uni.ba.geogame.model.token.Token;
import de.clemensklug.uni.ba.geogame.model.token.TokenSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author clemens
 */
public class TokenDisperser extends BaseHandler implements TokenHandler {
    private List<Token> _tokens = new ArrayList<>();//TODO: concurrency
    private DisperseMode _mode = DisperseMode.SINGLE;

    /**
     * dropped tokens go to the pool of dispersable tokens
     *
     * @param player player to call getDisposableInventory on
     * @return success of the addition
     */
    @Override
    public boolean drop(Player player) {
        log.debug("dropping token on TokenDisperser (" + player + ")");
        Token param = new TokenSet();
        switch (_mode) {
            case SINGLE:
                param = player.popDisposableItem();
                break;
            case FLUSH:
                ((TokenSet) param).setTokens(player.getDisposableInventory());
                player.getDisposableInventory().clear();
                break;
        }
        if (!param.isValid()) {
            return false;
        }
        if (!(param instanceof TokenSet)) {
            param = new TokenSet(param);
        }
        return _tokens.add(param);
    }

    /**
     * as a TokenDisperser does not keep track of who dropped tokens, there is no useful return value except an empty map
     *
     * @return empty map
     */
    @Override
    public Map<Player, TokenSet> getTokens() {
        log.debug("I am a disperser, returning empty map");
        return Collections.emptyMap();
    }

    public void setTokens(List<Token> tokens) {
        _tokens = tokens;
    }

    public void setMode(DisperseMode mode) {
        _mode = mode;
    }

    @Override
    public boolean isActive() {
        return !_tokens.isEmpty();
    }

    @Override
    public TokenSet pickup(Player player) {
        log.trace("starting disperse: " + _tokens);
        if (_tokens.isEmpty()) {
            log.trace("no tokens, abort!");
            return new TokenSet();
        }
        Token c = _tokens.get(0);
        _tokens.remove(0);
        TokenSet set;
        if (c instanceof TokenSet) {
            set = (TokenSet) c;
        } else {
            set = new TokenSet(c);
        }
        log.trace("remaining tokens: " + _tokens);
        return set;
    }

    public void addTokens(List<TokenSet> tokens) {
        _tokens.addAll(tokens);
    }

    @Override
    public String toString() {
        return "TokenDisperser{" +
                "_tokens=" + _tokens +
                '}';
    }

    protected List<Token> getTokenRepository() {
        return _tokens;
    }

    public enum DisperseMode {
        SINGLE,
        FLUSH
    }
}