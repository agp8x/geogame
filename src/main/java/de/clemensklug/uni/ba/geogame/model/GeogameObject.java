package de.clemensklug.uni.ba.geogame.model;

import java.io.Serializable;

/**
 * @author clemens
 */
public interface GeogameObject extends Serializable {
    String getName();

    void setName(String name);
}
