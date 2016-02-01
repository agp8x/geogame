/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame.model.spatial;

import de.clemensklug.uni.ba.geogame.parser.RCC8;

import java.util.Collection;

/**
 * Store Location
 *
 * @author clemens
 */
public class Point extends SpatialObject {
    private double _radius;

    public Point() {
    }

    public Point(double latitude, double longitude) {
        super(latitude, longitude);
        this._radius = 1;
    }

    public Point(double latitude, double longitude, double radius) {
        super(latitude, longitude);
        this._radius = radius;
    }

    public Point(double latitude, double longitude, double radius, String name) {
        super(latitude, longitude, name);
        this._radius = radius;
    }

    public Point(Point point) {
        this(point.getLatitude(), point.getLongitude(), point.getRadius(), point.getName());
    }

    public static double distance(Point a, Point b) {
        return Math.sqrt(Math.pow(a._latitude - b._latitude, 2) + Math.pow(a._longitude - b._longitude, 2));
    }

    public static boolean rcc(RCC8 rcc, Point a, Point b) {
        switch (rcc) {
            case DISJOINT:
                return disjoint(a, b);
            case TOUCHES:
                return touches(a, b);
            case OVERLAPS:
                return overlaps(a, b);
            case EQUALS:
                return equals(a, b);
            case WITHIN:
                return within(a, b);
            case CONTAINS:
                return within(b, a);
            case WITHIN_NTPP:
                return withinNTTP(a, b);
            case CONTAINS_NTTPI:
                return withinNTTP(b, a);
            case INTERSECTS:
                return intersects(a, b);
        }
        throw new UnsupportedOperationException();
    }

    public static boolean disjoint(Point a, Point b) {
        return distance(a, b) > a._radius + b._radius;
    }

    public static boolean touches(Point a, Point b) {
        return distance(a, b) == a._radius + b._radius;
    }

    public static boolean overlaps(Point a, Point b) {
        return distance(a, b) < a._radius + b._radius && distance(a, b) > Math.abs(a._radius - b._radius);
    }

    public static boolean equals(Point a, Point b) {
        return distance(a, b) == 0 && a._radius == b._radius;
    }

    /**
     * a within b
     *
     * @param a inner point
     * @param b outer point
     * @return a within b
     */
    public static boolean within(Point a, Point b) {
        return distance(a, b) < b._radius && distance(a, b) + a._radius == b._radius && a._radius < b._radius;
    }

    /**
     * a within b (no touching borders)
     *
     * @param a inner point
     * @param b outer point
     * @return a within b (no touching borders)
     */
    public static boolean withinNTTP(Point a, Point b) {
        return distance(a, b) < b._radius && distance(a, b) + a._radius < b._radius;
    }

    public static boolean intersects(Point a, Point b) {
        return !disjoint(a, b);
    }

    public static boolean triggerableIntersectsAny(Point ref, Collection<Point> points) {
        return points.parallelStream().filter(p -> triggerableIntersects(ref, p)).findFirst().isPresent();
    }

    /**
     * determine wether two Points are positioned so they can trigger an Action
     *
     * @param a first Point
     * @param b second Point
     * @return a can trigger b
     */
    public static boolean triggerableIntersects(Point a, Point b) {
        return equals(a, b) ||
                within(a, b) || within(b, a) ||
                withinNTTP(a, b) || withinNTTP(b, a);
    }

    public double getRadius() {
        return _radius;
    }

    public void setRadius(double radius) {
        this._radius = radius;
    }

    @Override
    public String toString() {
        return "Point{" +
                "latitude=" + _latitude +
                ", longitude=" + _longitude +
                ", radius=" + _radius +
                ", name=" + _name +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Point) {
            Point point = (Point) obj;
            return getLatitude() == point.getLatitude() && getLongitude() == point.getLongitude() && getRadius() == point.getRadius();
            //return getLatitude() == point.getLatitude() && getLongitude() == point.getLongitude();
        }
        return false;
    }

    public double distance(Point o) {
        return distance(this, o);
    }

    private boolean intersects(Point p) {
        return intersects(this, p);
    }

}
