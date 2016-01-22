package de.clemensklug.uni.ba.geogame.model.conditions;

import de.clemensklug.uni.ba.geogame.GameManager;
import de.clemensklug.uni.ba.geogame.model.Player;
import de.clemensklug.uni.ba.geogame.model.token.handler.TokenHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author clemens
 */
public class TokenCondition extends Condition {
    //TODO: different evaluations (token-at, token-count)?
    private List<TokenHandler> _handlers;

    @Override
    public boolean isSatisfied() {
        return !getSatisfyingPlayers().isEmpty();
    }

    @Override
    public List<Player> getSatisfyingPlayers() {
        List<Player> satisfyingPlayers = new ArrayList<>(GameManager.getPlayers());
        for (TokenHandler t : _handlers) {
            satisfyingPlayers.removeIf(player -> !t.getTokens().containsKey(player));
        }
        if (log.isTraceEnabled()) {
            //dump extensive information about the handler and its state, also strip the domain/path of the URI
            log.trace((getName() + _handlers.stream().map(tokenHandler -> tokenHandler.getName() + tokenHandler.getTokens().toString()).reduce("", (s, s2) -> s + "; " + s2)).replace(getName().split("#")[0], ""));
            log.trace(satisfyingPlayers);
        }
        return satisfyingPlayers;
    }

    public void setTokenhandlers(List<TokenHandler> handlers) {
        _handlers = handlers;
    }

    public List<TokenHandler> getHandlers() {
        return _handlers;
    }
}
