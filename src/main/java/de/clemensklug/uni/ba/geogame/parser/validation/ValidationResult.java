/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame.parser.validation;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by clemens on 26.11.15.
 *
 * @author clemens
 */
public class ValidationResult {
    private final List<String> _instances;
    private final String _property;

    private ValidationResult(List<String> instances, String property) {
        _instances = instances;
        _property = property;
    }

    private ValidationResult() {
        _instances = Collections.emptyList();
        _property = null;
    }

    public static ValidationResult valid() {
        return new ValidationResult();
    }

    public static ValidationResult invalid(List<String> instances, String property) {
        return new ValidationResult(instances, property);
    }

    public List<String> getInstances() {
        return _instances;
    }

    public String getProperty() {
        return _property;
    }

    public boolean isValid() {
        return _instances.isEmpty();
    }

    public boolean isInvalid() {
        return !_instances.isEmpty();
    }

    public ValidationResult add(ValidationResult r) {
        if (null != r && r._property != null) {
            LinkedList<String> tmp = new LinkedList<>(_instances);
            tmp.addAll(r._instances);
            return new ValidationResult(tmp, r._property);
        }
        return this;
    }

    @Override
    public String toString() {
        return "ValidationResult{" +
                "_instances=" + _instances +
                ", _property='" + _property + '\'' +
                '}';
    }
}
