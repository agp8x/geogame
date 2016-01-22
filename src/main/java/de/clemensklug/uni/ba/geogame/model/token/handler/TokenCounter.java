package de.clemensklug.uni.ba.geogame.model.token.handler;

import de.clemensklug.uni.ba.geogame.model.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author clemens
 */
public class TokenCounter extends BaseHandler implements TokenHandler {
    private final Map<Player, Integer> _counts = new HashMap<>();

    @Override
    public boolean drop(Player player) {
        if (_counts.containsKey(player)) {
            _counts.replace(player, _counts.get(player) + 1);
        } else {
            _counts.put(player, 1);
        }
        return super.drop(player);
    }

    public int getCount(Player p) {
        return _counts.get(p);
    }
}
