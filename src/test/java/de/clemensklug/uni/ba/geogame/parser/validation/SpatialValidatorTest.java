/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame.parser.validation;

import de.clemensklug.uni.ba.geogame.model.GeogameConfig;
import de.clemensklug.uni.ba.geogame.model.spatial.Point;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by clemens on 12.02.16.
 *
 * @author clemens
 */
public class SpatialValidatorTest {

    @Test
    public void testValidateBoundingBox() throws Exception {
        SpatialValidator sv=new SpatialValidator();
        GeogameConfig c=new GeogameConfig();
        Point a=new Point(0,0);
        Point b=new Point(20,20);
        List<Point> bb=new LinkedList<>();
        bb.add(a);bb.add(b);
        c.setBounding(bb);
        c.setBbX(15);
        c.setBbY(15);
        assertFalse(sv.validateBoundingBox(c));
        c.setBbX(25);
        c.setBbY(25);
        assertTrue(sv.validateBoundingBox(c));
        c.setBbX(20);
        c.setBbY(20);
        assertTrue(sv.validateBoundingBox(c));
    }
}