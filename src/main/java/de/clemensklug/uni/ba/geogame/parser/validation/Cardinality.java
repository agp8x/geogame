/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame.parser.validation;

/**
 * Created by clemens on 17.12.15.
 *
 * @author clemens
 */
public enum Cardinality {
    //TODO: add only(universal)?
    EXACT("owl:qualifiedCardinality"),
    MIN("owl:minQualifiedCardinality"),
    MAX("owl:maxQualifiedCardinality"),
    SOME("owl:someValuesFrom");
    private final String _string;

    Cardinality(final String s) {
        _string = s;
    }

    @Override
    public String toString() {
        return _string;
    }
}
