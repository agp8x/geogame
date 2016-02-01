/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame.location;

import de.clemensklug.uni.ba.geogame.model.Player;
import de.clemensklug.uni.ba.geogame.model.spatial.Point;

/**
 * Provide location information
 *
 * @author clemens
 */
public interface LocationProvider {
    /**
     * get current position
     *
     * @return <code>Point</code> with current position
     */
    Point getPosition(Player player);//TODO: concurrency

    /**
     * describe location provider
     *
     * @return <code>String</code> containing human readable description
     */
    String description();
}
