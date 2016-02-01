/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame.model.token;

/**
 * Created by clemens on 08.01.16.
 *
 * @author clemens
 */
public class InvalidToken extends Token {
    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public String toString() {
        return "InvalidToken{}";
    }
}
