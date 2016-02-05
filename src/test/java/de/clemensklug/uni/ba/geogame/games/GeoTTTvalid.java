/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame.games;

import org.junit.Test;

/**
 * Created by clemens on 27.11.15.
 *
 * @author clemens
 */
public class GeoTTTvalid {
    private final String MAIN_FIELD = "src/main/resources/geoTTT.owl";
    private final String MAIN_GAME = "http://clemensklug.de/uni/ba/geogame/geoTTT#GeoTTTgame";
    private final String TEST_FIELD = "src/test/resources/geoTTT.owl";
    private final String TEST_GAME = "http://clemensklug.de/uni/ba/geogame/geoTTT#GeoTTTgame";

    @Test
    public void testGeotttValid() throws Exception {
        Validator.validate(MAIN_FIELD);
    }

    @Test
    public void testGeotttSpatialValid() throws Exception {
        Validator.spatialValid(MAIN_FIELD, MAIN_GAME);
    }

    @Test
    public void testGeotttTestValid() throws Exception {
        Validator.validate(TEST_FIELD);
    }

    @Test
    public void testGeotttTestSpatialValid() throws Exception {
        //its for testing, it doesnt need to be spatially sane
        //Validator.spatialValid(TEST_FIELD, TEST_GAME);
    }

    @Test
    public void testGeoTTTdefPartiallyInvalid() throws Exception {
        //the definition on itself is a valid ontology, but misses some properties to get a fully valid geogame
        Validator.validate("geoTTTdef.owl", 5);
    }

}