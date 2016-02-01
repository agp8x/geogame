/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame.parser.validation;

/**
 * Created by clemens on 23.11.15.
 *
 * @author clemens
 */
public class Restriction {
    private final String _className;
    private final String _property;
    private final int _quantity;
    private final String _type;

    public Restriction(String className, String property, int quantity) {
        _property = property;
        _quantity = quantity;
        _className = className;
        _type = null;
    }

    public Restriction(String className, String property, int quantity, String type) {
        _property = property;
        _quantity = quantity;
        _className = className;
        _type = type;
    }

    public String getClassName() {
        return _className;
    }

    public String getProperty() {
        return _property;
    }

    public int getQuantity() {
        return _quantity;
    }

    public String getType() {
        return _type;
    }

    public boolean isQualifiedCardinalityRestriction() {
        return _type != null;
    }

    @Override
    public String toString() {
        return "Restriction{" +
                "_className='" + _className + '\'' +
                ", _property='" + _property + '\'' +
                ", _type='" + _type + '\'' +
                ", _quantity=" + _quantity +
                '}';
    }
}
