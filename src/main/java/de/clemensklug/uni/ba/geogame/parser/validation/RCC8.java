/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame.parser.validation;

/**
 * Created by clemens on 17.12.15.
 *
 * @author clemens
 */
public enum RCC8 {
    DISJOINT("geosparql:rcc8dc"),
    TOUCHES("geosparql:rcc8ec"),
    OVERLAPS("geosparql:rcc8po"),
    EQUALS("geosparql:rcc8eq"),
    WITHIN("geosparql:rcc8tpp"),
    CONTAINS("geosparql:rcc8tppi"),
    WITHIN_NTPP("geosparql:rcc8ntpp"),
    CONTAINS_NTTPI("geosparql:rcc8ntppi"),
    /**
     * technically, intersects is not a RCC8-relation, rather the negation of DISJOINT, therefore here the ogc-property is used
     */
    INTERSECTS("geosparql:sfIntersects");

    private final String _string;

    RCC8(final String s) {
        _string = s;
    }

    @Override
    public String toString() {
        return _string;
    }
}
