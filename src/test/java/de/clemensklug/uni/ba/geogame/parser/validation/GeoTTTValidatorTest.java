package de.clemensklug.uni.ba.geogame.parser.validation;

import de.clemensklug.uni.ba.geogame.location.GridLocationProvider;
import de.clemensklug.uni.ba.geogame.location.LocationProvider;
import de.clemensklug.uni.ba.geogame.model.spatial.Point;
import de.clemensklug.uni.ba.geogame.parser.ConfigParser;
import de.clemensklug.uni.ba.geogame.parser.Namespace;
import de.clemensklug.uni.ba.geogame.parser.OWLParser;
import de.clemensklug.uni.ba.geogame.parser.RCC8;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static de.clemensklug.uni.ba.geogame.parser.Namespace.POINT;
import static de.clemensklug.uni.ba.geogame.parser.validation.SpatialValidator.Category;
import static de.clemensklug.uni.ba.geogame.parser.validation.SpatialValidator.Category.*;
import static java.lang.System.out;
import static org.junit.Assert.*;

/**
 * Created by clemens on 03.12.15.
 *
 * @author clemens
 */
public class GeoTTTValidatorTest {
    private final static String _path = "src/test/resources/geoTTT.owl";
    private final static String _path2 = "src/test/resources/geoTTT_lecture_field.owl";
    private GeoTTTValidator _val;
    private static String _GEOTTTTEST = "http://clemensklug.de/uni/ba/geogame/geoTTT#GeoTTTgame";
    private static String _GEOTTTTEST2 = "http://clemensklug.de/uni/ba/geogame/test/geottt#GeoTTTInst_lecture_field";

    private static List<Point> getPoints() {
        LocationProvider lp = new GridLocationProvider();
        List<Point> points = new LinkedList<>();
        for (int i = 0; i < 9; i++) {
            points.add(lp.getPosition(null));
        }
        return points;
    }

    @Before
    public void setUp() throws Exception {
        _val = new GeoTTTValidator();

    }

    @Test
    public void testAverageDistanceLinear() throws Exception {
        List<Point> points = new LinkedList<>();
        for (int i = 0; i < 9; i++) {
            points.add(new Point(0, i));
        }
        double res = _val.averageDistance(points);
        assertEquals(3.33, res, 0.01);
        System.out.println(res);
        res = _val.averageDistance(points.subList(0, 2));
        assertEquals(1, res, 0);
    }

    @Test
    public void testAverageDistanceDiagonal() throws Exception {
        List<Point> points = new LinkedList<>();
        for (int i = 0; i < 9; i++) {
            points.add(new Point(i, i));
        }
        double res = _val.averageDistance(points);
        assertEquals(4.71, res, 0.01);
        System.out.println(res);
        res = _val.averageDistance(points.subList(0, 2));
        assertEquals(Math.sqrt(2), res, 0);
    }

    @Test
    public void testAverageDistanceGrid() throws Exception {
        List<Point> points = getPoints();
        double res = _val.averageDistance(points);
        assertEquals(1.63, res, 0.01);
        System.out.println(res);
        res = _val.averageDistance(points.subList(0, 2));
        assertEquals(1, res, 0);
    }

    @Test
    public void testAverageDistanceGeoTTT() throws Exception {
        ConfigParser cp = new OWLParser(_path);
        List<Point> points = cp.getInstances(POINT);
        double res = _val.averageDistance(points);
        assertEquals(1.89, res, 0.01);
    }

    @Test
    public void testCategorizePoints() throws Exception {
        ConfigParser cp = new OWLParser(_path);
        List<Point> points = cp.getInstances(POINT);
        out.println(_val.categorizePoints(points));
    }

    @Test
    public void testLineDistances() throws Exception {
        ConfigParser cp = new OWLParser(_path);
        out.println(_val.lineDistances(cp, _GEOTTTTEST));
    }

    @Test
    public void testLineDistances2() throws Exception {
        ConfigParser cp = new OWLParser(_path2);
        Map<Category, List<GeoTTTValidator.Distance>> map = _val.categorizePoints(cp.getGeogame(_GEOTTTTEST2).getPoints());
        Map<Category, List<GeoTTTValidator.Distance>> map1 = _val.categorizePoints(cp.getGeogame(_GEOTTTTEST2).getPoints(), 1.05);
        assertEquals(3, map.entrySet().size());
        assertEquals(3, map1.entrySet().size());
        assertEquals(0, map.get(AVERAGE).size());
        assertEquals(5, map.get(ABOVE).size());
        assertEquals(4, map.get(BELOW).size());
        assertEquals(1, map1.get(AVERAGE).size());
        assertEquals(4, map1.get(ABOVE).size());
        assertEquals(4, map1.get(BELOW).size());
    }

    @Test
    public void testGetPoints() throws Exception {
        ConfigParser cp = new OWLParser(_path);
        List l = cp.getGeogame(_GEOTTTTEST).getPoints();
        assertEquals(9, l.size());
    }

    @Test
    public void testGetPoints2() throws Exception {
        ConfigParser cp = new OWLParser(_path2);
        List l = cp.getGeogame(_GEOTTTTEST2).getPoints();
        assertEquals(9, l.size());
    }

    @Test
    public void testCheckRCCInvalid() throws Exception {
        ConfigParser cp = new OWLParser(_path);
        List<ValidationResult> results = _val.checkRCC(cp, _GEOTTTTEST, RCC8.DISJOINT);
        assertFalse(results.isEmpty());
        assertEquals(72, results.size());
    }

    @Test
    public void testCheckRCCPass() throws Exception {
        ConfigParser cp = new OWLParser(_path2);
        List<ValidationResult> results = _val.checkRCC(cp, _GEOTTTTEST2, RCC8.DISJOINT);
        assertTrue(results.isEmpty());
    }
    //TODO: add sane assertions to tests, remove souts

    @Test
    public void testName() throws Exception {
        GeoTTTValidator.Rect rect = GeoTTTValidator.extend(new GeoTTTValidator.Rect(new Point(0, 0)), new GeoTTTValidator.Rect(new Point(1, 1)));
        System.out.println(rect);
        GeoTTTValidator.Rect r2=GeoTTTValidator.extend(rect, new GeoTTTValidator.Rect(new Point(2,3)));
        System.out.println(r2);
    }
}