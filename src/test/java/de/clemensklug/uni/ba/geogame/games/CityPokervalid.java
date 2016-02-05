/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame.games;

import org.junit.Test;

/**
 * Created by clemens on 27.01.16.
 *
 * @author clemens
 */
public class CityPokervalid {
    private final String GAME_DEF = "citypoker.owl";
    private final String GAME_FIELD = "CPinst.owl";
    private final String MAIN_GAME = "http://clemensklug.de/uni/ba/geogame/citypoker#CityPokergame";

    @Test
    public void testCityPokerDefInvalid() throws Exception {
        Validator.validate(GAME_DEF, 8);
    }

    @Test
    public void testCityPokerField() throws Exception {
        Validator.validate(GAME_FIELD);
    }

    @Test
    public void testCityPokerFieldSpatial() throws Exception {
        Validator.spatialValid(GAME_FIELD, MAIN_GAME);
    }

}
