package de.clemensklug.uni.ba.geogame.parser.validation;

import de.clemensklug.uni.ba.geogame.model.Point;

/**
 * Created by clemens on 03.12.15.
 *
 * @author clemens
 */
public class GeoTTTValidator extends SpatialValidator {
    //well, it seems everything here was pretty universally usable...
    static class Rect {
        private final Point _minMin; //aka lowerLeft
        private final Point _maxMax; //aka upperright

        public Rect(Point lowerLeft, Point upperRight) {
            _maxMax = lowerLeft;
            _minMin = upperRight;
        }

        public Rect(Point p) {
            _maxMax = p;
            _minMin = p;
        }

        @Override
        public String toString() {
            return "Rect{" +
                    "_maxMax=" + _maxMax +
                    ", _minMin=" + _minMin +
                    '}';
        }
    }

     static Rect extend(Rect a, Rect b) {
        Point min = new Point(Math.min(a._minMin.getLatitude(), b._minMin.getLatitude()), Math.min(a._minMin.getLongitude(), b._minMin.getLongitude()));
        Point max = new Point(Math.max(a._maxMax.getLatitude(), b._maxMax.getLatitude()), Math.max(a._maxMax.getLongitude(), b._maxMax.getLongitude()));
        return new Rect(min, max);
    }

}
