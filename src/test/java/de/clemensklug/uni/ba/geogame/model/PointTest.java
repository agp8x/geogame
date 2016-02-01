/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame.model;

import de.clemensklug.uni.ba.geogame.model.spatial.Point;
import de.clemensklug.uni.ba.geogame.parser.RCC8;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author clemens
 */
public class PointTest {

    @Test
    public void testEquals() throws Exception {
        Point l1 = new Point();
        Point l2 = new Point();
        assertTrue(l1.equals(l2));
        l1.setLatitude(1);
        assertFalse(l1.equals(l2));
        l2.setLongitude(2);
        assertFalse(l1.equals(l2));
        l1.setLongitude(2);
        assertFalse(l1.equals(l2));
        l2.setLatitude(1);
        assertTrue(l1.equals(l2));
    }

    @Test
    public void testDistance() throws Exception {
        Point p0 = new Point(0, 0);
        Point p1 = new Point(1, 1);
        Point p2 = new Point(2, 2);
        assertEquals(0, p0.distance(p0), 0);
        assertEquals(p0.distance(p1), p1.distance(p0), 0);
        assertEquals(p0.distance(p1), p1.distance(p2), 0);
        assertEquals(p0.distance(p1) * 2, p0.distance(p2), 0);
        Point p3 = new Point(-1, -1);
        assertEquals(p0.distance(p1), p0.distance(p3), 0);
        assertEquals(Point.distance(p0, p1), p0.distance(p1), 0);
    }

    @Test
    public void testRCC8DC() throws Exception {
        Point p0 = new Point(20, 20, 20);
        Point p1 = new Point(80, 80, 20);
        assertTrue(Point.rcc(RCC8.DISJOINT, p0, p1));

        assertFalse(Point.rcc(RCC8.TOUCHES, p0, p1));
        assertFalse(Point.rcc(RCC8.OVERLAPS, p0, p1));
        assertFalse(Point.rcc(RCC8.EQUALS, p0, p1));
        assertFalse(Point.rcc(RCC8.WITHIN, p0, p1));
        assertFalse(Point.rcc(RCC8.CONTAINS, p0, p1));
        assertFalse(Point.rcc(RCC8.WITHIN_NTPP, p0, p1));
        assertFalse(Point.rcc(RCC8.CONTAINS_NTTPI, p0, p1));
        assertFalse(Point.rcc(RCC8.INTERSECTS, p0, p1));
    }

    @Test
    public void testRCC8EC() throws Exception {
        Point p0 = new Point(20, 20, 20);
        Point p1 = new Point(20, 60, 20);
        assertTrue(Point.rcc(RCC8.TOUCHES, p0, p1));
        assertTrue(Point.rcc(RCC8.INTERSECTS, p0, p1));

        assertFalse(Point.rcc(RCC8.DISJOINT, p0, p1));
        assertFalse(Point.rcc(RCC8.OVERLAPS, p0, p1));
        assertFalse(Point.rcc(RCC8.EQUALS, p0, p1));
        assertFalse(Point.rcc(RCC8.WITHIN, p0, p1));
        assertFalse(Point.rcc(RCC8.CONTAINS, p0, p1));
        assertFalse(Point.rcc(RCC8.WITHIN_NTPP, p0, p1));
        assertFalse(Point.rcc(RCC8.CONTAINS_NTTPI, p0, p1));
    }

    @Test
    public void testRCC8PO() throws Exception {
        Point p0 = new Point(20, 20, 20);
        Point p1 = new Point(40, 40, 20);
        assertTrue(Point.rcc(RCC8.OVERLAPS, p0, p1));
        assertTrue(Point.rcc(RCC8.INTERSECTS, p0, p1));

        assertFalse(Point.rcc(RCC8.DISJOINT, p0, p1));
        assertFalse(Point.rcc(RCC8.TOUCHES, p0, p1));
        assertFalse(Point.rcc(RCC8.EQUALS, p0, p1));
        assertFalse(Point.rcc(RCC8.WITHIN, p0, p1));
        assertFalse(Point.rcc(RCC8.CONTAINS, p0, p1));
        assertFalse(Point.rcc(RCC8.WITHIN_NTPP, p0, p1));
        assertFalse(Point.rcc(RCC8.CONTAINS_NTTPI, p0, p1));
    }

    @Test
    public void testRCC8EQ() throws Exception {
        Point p0 = new Point(20, 20, 20);
        Point p1 = new Point(20, 20, 20);
        assertTrue(Point.rcc(RCC8.EQUALS, p0, p1));
        assertTrue(Point.rcc(RCC8.INTERSECTS, p0, p1));

        assertFalse(Point.rcc(RCC8.OVERLAPS, p0, p1));
        assertFalse(Point.rcc(RCC8.DISJOINT, p0, p1));
        assertFalse(Point.rcc(RCC8.TOUCHES, p0, p1));
        assertFalse(Point.rcc(RCC8.WITHIN, p0, p1));
        assertFalse(Point.rcc(RCC8.CONTAINS, p0, p1));
        assertFalse(Point.rcc(RCC8.WITHIN_NTPP, p0, p1));
        assertFalse(Point.rcc(RCC8.CONTAINS_NTTPI, p0, p1));
    }

    @Test
    public void testRCC8TPP() throws Exception {
        Point p0 = new Point(60, 20, 20);
        Point p1 = new Point(60, 60, 60);
        assertTrue(Point.rcc(RCC8.WITHIN, p0, p1));
        assertTrue(Point.rcc(RCC8.INTERSECTS, p0, p1));

        assertFalse(Point.rcc(RCC8.DISJOINT, p0, p1));
        assertFalse(Point.rcc(RCC8.TOUCHES, p0, p1));
        assertFalse(Point.rcc(RCC8.OVERLAPS, p0, p1));
        assertFalse(Point.rcc(RCC8.EQUALS, p0, p1));
        assertFalse(Point.rcc(RCC8.CONTAINS, p0, p1));
        assertFalse(Point.rcc(RCC8.WITHIN_NTPP, p0, p1));
        assertFalse(Point.rcc(RCC8.CONTAINS_NTTPI, p0, p1));
    }

    @Test
    public void testRCC8TPPi() throws Exception {
        Point p0 = new Point(60, 60, 60);
        Point p1 = new Point(60, 20, 20);
        assertTrue(Point.rcc(RCC8.CONTAINS, p0, p1));
        assertTrue(Point.rcc(RCC8.INTERSECTS, p0, p1));

        assertFalse(Point.rcc(RCC8.DISJOINT, p0, p1));
        assertFalse(Point.rcc(RCC8.TOUCHES, p0, p1));
        assertFalse(Point.rcc(RCC8.OVERLAPS, p0, p1));
        assertFalse(Point.rcc(RCC8.EQUALS, p0, p1));
        assertFalse(Point.rcc(RCC8.WITHIN, p0, p1));
        assertFalse(Point.rcc(RCC8.WITHIN_NTPP, p0, p1));
        assertFalse(Point.rcc(RCC8.CONTAINS_NTTPI, p0, p1));
    }

    @Test
    public void testRCC8nTPP() throws Exception {
        Point p0 = new Point(35, 35, 20);
        Point p1 = new Point(60, 60, 60);
        assertTrue(Point.rcc(RCC8.WITHIN_NTPP, p0, p1));
        assertTrue(Point.rcc(RCC8.INTERSECTS, p0, p1));

        assertFalse(Point.rcc(RCC8.DISJOINT, p0, p1));
        assertFalse(Point.rcc(RCC8.TOUCHES, p0, p1));
        assertFalse(Point.rcc(RCC8.OVERLAPS, p0, p1));
        assertFalse(Point.rcc(RCC8.EQUALS, p0, p1));
        assertFalse(Point.rcc(RCC8.WITHIN, p0, p1));
        assertFalse(Point.rcc(RCC8.CONTAINS, p0, p1));
        assertFalse(Point.rcc(RCC8.CONTAINS_NTTPI, p0, p1));
    }

    @Test
    public void testRCC8nTPPi() throws Exception {
        Point p0 = new Point(60, 60, 60);
        Point p1 = new Point(35, 35, 20);
        assertTrue(Point.rcc(RCC8.CONTAINS_NTTPI, p0, p1));
        assertTrue(Point.rcc(RCC8.INTERSECTS, p0, p1));

        assertFalse(Point.rcc(RCC8.DISJOINT, p0, p1));
        assertFalse(Point.rcc(RCC8.TOUCHES, p0, p1));
        assertFalse(Point.rcc(RCC8.OVERLAPS, p0, p1));
        assertFalse(Point.rcc(RCC8.EQUALS, p0, p1));
        assertFalse(Point.rcc(RCC8.WITHIN, p0, p1));
        assertFalse(Point.rcc(RCC8.CONTAINS, p0, p1));
        assertFalse(Point.rcc(RCC8.WITHIN_NTPP, p0, p1));
    }
}