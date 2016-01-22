package de.clemensklug.uni.ba.geogame.location;

import de.clemensklug.uni.ba.geogame.model.Player;
import de.clemensklug.uni.ba.geogame.model.spatial.Point;

/**
 * Return always the same position (same instance)
 *
 * @author clemens
 */
public class GridLocationProvider implements LocationProvider {

    private final Point _location;
    private final Point _start;
    private final int _size;

    public GridLocationProvider() {
        _location = new Point(0, 0, 0);
        _start = new Point(_location);
        _size = 3;
        _location.setName(this.getClass().toString());
    }

    public GridLocationProvider(Point location, int size) {
        _location = location;
        _start = new Point(location);
        _size = size;
        _location.setName(this.getClass().toString());
    }

    public Point getPosition(Player player) {
        Point ret = new Point(_location);
        updateLocation();
        return ret;
    }

    private synchronized void updateLocation() {
        _location.setLatitude(_location.getLatitude() + 1);
        if (_location.getLatitude() > _size - 1) {
            _location.setLatitude(_start.getLatitude());
            _location.setLongitude(_location.getLongitude() + 1);
            if (_location.getLongitude() > _size - 1) {
                _location.setLatitude(_start.getLatitude());
                _location.setLongitude(_start.getLongitude());
            }
        }
    }

    @Override
    public String description() {
        return "Locations on a grid (default:3x3)";
    }

    @Override
    public String toString() {
        return description();
    }
}
