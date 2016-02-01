/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame.model.token;

import de.clemensklug.uni.ba.geogame.model.GeogameObject;

/**
 * @author clemens
 */
public class Token implements GeogameObject {
    private String value;
    private String _name;

    public boolean isValid() {
        return true;
    }

    public boolean isDuplicable() {
        return false;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String _token) {
        this.value = _token;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public void setName(String name) {
        _name = name;
    }

    @Override
    public String toString() {
        return "Token{" +
                "name='" + _name + "', " +
                "token='" + value + "'" +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token = (Token) o;

        if (value != null ? !value.equals(token.value) : token.value != null) return false;
        return !(_name != null ? !_name.equals(token._name) : token._name != null);

    }

}
