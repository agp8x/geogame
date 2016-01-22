package de.clemensklug.uni.ba.geogame.location;

import de.clemensklug.uni.ba.geogame.model.Player;
import de.clemensklug.uni.ba.geogame.model.spatial.Point;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author clemens
 */
public class IteratorLocationProvider implements LocationProvider {
    private final Map<Player, Iterator<Point>> _it;
    private final Map<Player, List<Point>> _map;


    public IteratorLocationProvider(Map<Player, List<Point>> points) {
        _map = points;
        _it = new ConcurrentHashMap<>();
        for (Object o : points.keySet()) {
            Player p = (Player) o;
            _it.put(p, _map.get(p).iterator());
        }
    }

    @Override
    public Point getPosition(Player player) {
        if (!_it.get(player).hasNext()) {
            _it.replace(player, _map.get(player).iterator());
        }
        Point next = _it.get(player).next();
        next.setName("pos_" + player.getName() + "_pos");
        return next;
    }

    @Override
    public String description() {
        return "Locations from a list per player, repeated indefinitely";
    }
}
