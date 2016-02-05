/*
 *  See the file "LICENSE" for the full license governing this code.
 */

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
public class Neocartographervalid {
    private final String GAME_DEF = "neocartographer.owl";
    private final String GAME_FIELD = "NCinst.owl";
    private final String MAIN_GAME = "http://clemensklug.de/uni/ba/geogame/neocartographer/NCSampleInstance#ncInst";

    @Test
    public void testNCDefInvalid() throws Exception {
        Validator.validate(GAME_DEF);
    }

    @Test
    public void testNCField() throws Exception {
        Validator.validate(GAME_FIELD);
    }

    @Test
    public void testNCFieldSpatial() throws Exception {
        Validator.spatialValid(GAME_FIELD, MAIN_GAME);
    }

}
