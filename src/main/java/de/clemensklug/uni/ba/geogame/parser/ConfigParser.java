/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame.parser;

import com.hp.hpl.jena.query.QuerySolution;
import de.clemensklug.uni.ba.geogame.model.GeogameConfig;
import de.clemensklug.uni.ba.geogame.model.GeogameObject;

import java.io.InputStream;
import java.util.List;

/**
 * Parse game configuration
 *
 * @author clemens
 */
public interface ConfigParser {
    void importConfig(String filename);

    void importConfig(InputStream inputStream);

    /**
     * get all instances of OntClass in model
     *
     * @param ontClass name of the class
     * @param <T>      Type of the class mapped to Java
     * @return List of instances
     */
    <T extends GeogameObject> List<T> getInstances(String ontClass);

    /**
     * get a specified instance of a OntClass
     *
     * @param name     name of the individual
     * @param ontClass name of the class
     * @param <T>      Type of the class mapped to Java
     * @return instance
     */
    <T extends GeogameObject> T getInstance(String name, String ontClass);

    /**
     * get GeogameConfig
     *
     * @param name instance of game
     * @return GeogameConfig of given instance
     */
    GeogameConfig getGeogame(String name);

    /**
     * run a Query against model
     *
     * @param query Query
     * @return Solution
     */
    List<QuerySolution> runQuery(String query);

    /**
     * get problems and inconsistencies of the model itself
     *
     * @return List of errors
     */
    List<String> validate();

    /**
     * determine wether subClass is a sub- or equal class to superClass
     *
     * @param superClass super
     * @param subClass   sub or equal
     * @return truth
     */
    boolean isSubClass(String superClass, String subClass);
}
