/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame.model.spatial;

import de.clemensklug.uni.ba.geogame.model.GeogameObject;

/**
 * Created by clemens on 16.01.16.
 *
 * @author clemens
 */
public class SpatialObject implements GeogameObject {
    protected String _name;
    protected double _latitude;
    protected double _longitude;
    protected boolean _enabled = true;

    public SpatialObject() {
    }

    public SpatialObject(double latitude, double longitude) {
        _latitude = latitude;
        _longitude = longitude;
    }

    public SpatialObject(double latitude, double longitude, String name) {
        this(latitude, longitude);
        _name = name;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public void setName(String name) {
        _name = name;
    }

    public double getLatitude() {
        return _latitude;
    }

    public void setLatitude(double latitude) {
        this._latitude = latitude;
    }

    public double getLongitude() {
        return _longitude;
    }

    public void setLongitude(double longitude) {
        this._longitude = longitude;
    }

    public boolean isEnabled() {
        return _enabled;
    }

    public void setEnabled(boolean enabled) {
        _enabled = enabled;
    }
}
