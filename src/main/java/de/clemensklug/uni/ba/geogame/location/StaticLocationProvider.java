package de.clemensklug.uni.ba.geogame.location;

import de.clemensklug.uni.ba.geogame.model.Player;
import de.clemensklug.uni.ba.geogame.model.spatial.Point;

/**
 * Return always the same position (same instance)
 *
 * @author clemens
 */
public class StaticLocationProvider implements LocationProvider {

    private final Point _location;

    public StaticLocationProvider() {
        _location = new Point(1, 2, 0);
    }

    public StaticLocationProvider(Point location) {
        _location = location;
    }

    public Point getPosition(Player player) {
        return _location;
    }

    @Override
    public String description() {
        return "Static point location, changeable at instantiation";
    }

    @Override
    public String toString() {
        return description();
    }
}
