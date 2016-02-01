/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame.location;

import de.clemensklug.uni.ba.geogame.model.Player;
import de.clemensklug.uni.ba.geogame.model.spatial.Point;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author clemens
 */
public class IteratorLocationProviderTest {

    @Test
    public void testGetPosition() throws Exception {
        Player a = new Player("test");
        List<Point> points = new ArrayList<>();
        points.add(new Point(1, 2));
        points.add(new Point(3, 4));
        points.add(new Point(5, 6));
        Map<Player, List<Point>> map=new HashMap<>();
        map.put(a,points);
        IteratorLocationProvider ilp = new IteratorLocationProvider(map);
        for (Point p : points) {
            assertEquals(p, ilp.getPosition(a));
        }
    }
}