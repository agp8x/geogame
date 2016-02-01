/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame.model.challenge;

import de.clemensklug.uni.ba.geogame.model.GeogameObject;

/**
 * @author clemens
 */
public interface Challenge extends GeogameObject {
    Challenge clone();

    boolean isFinished();

    void startChallenge();

    ChallengeData getData();

}
