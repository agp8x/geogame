/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame.model;

/**
 * Created by clemens on 24.01.16.
 *
 * @author clemens
 */
public enum TriggeringMode {
    /**
     * any constellation of equals, within, withinNTPP
     */
    WITHIN,
    /**
     * same as within, not with intersection with any other Action
     */
    SINGLE_WITHIN,
    SIMPLIFIED_VORONOI
}
