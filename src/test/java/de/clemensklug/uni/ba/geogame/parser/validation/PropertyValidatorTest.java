package de.clemensklug.uni.ba.geogame.parser.validation;

import de.clemensklug.uni.ba.geogame.parser.Namespace;
import de.clemensklug.uni.ba.geogame.parser.OWLParser;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by clemens on 20.11.15.
 *
 * @author clemens
 */
public class PropertyValidatorTest {
    private final String _path = "src/test/resources/validator.owl";
    private final String _path_valid = "src/test/resources/validator_valid.owl";
    private final String _path_qualified = "src/test/resources/validator_qualifiedCardinality.owl";
    PropertyValidator _val;

    @Before
    public void setUp() throws Exception {
        _val = new PropertyValidator(new OWLParser(_path));
    }

    @Test
    public void testValidateExistentialPass() throws Exception {
        ValidationResult result = _val.validateExistential(new Restriction(Namespace.ACTION, Namespace.PROP_AT, 1));
        assertTrue(result.isValid());
    }

    @Test
    public void testValidateExistentialFail() throws Exception {
        ValidationResult result = _val.validateExistential(new Restriction(Namespace.POINT, Namespace.PROP_RADIUS, 1));
        assertFalse(result.isValid());
    }

    @Test
    public void testValidateExistentialFailSingleEntryOnly() throws Exception {
        ValidationResult result = _val.validateExistential(new Restriction(Namespace.POINT, Namespace.PROP_RADIUS, 1));
        assertEquals(1, result.getInstances().size());
    }

    @Test
    public void testValidateExistentialFailMultipleFails() throws Exception {
        ValidationResult result = _val.validateExistential(new Restriction(Namespace.POINT, Namespace.PROP_LONG, 1));
        assertEquals(3, result.getInstances().size());
    }

    @Test
    public void testCheckRestrictions() throws Exception {
        Map res = _val.getRestrictions();
        List<ValidationResult> results = _val.validate(res);
        assertFalse(results.stream().map(ValidationResult::isValid).reduce(Boolean.TRUE, (vr, vr2) -> vr && vr2));
    }

    @Test
    public void testCheckRestrictionsValid() throws Exception {
        _val = new PropertyValidator(new OWLParser(_path_valid));
        Map res = _val.getRestrictions();
        //System.out.println(res);
        List<ValidationResult> results = _val.validate(res);
        //System.out.println(results);
        assertTrue(results.stream().map(ValidationResult::isValid).reduce(Boolean.TRUE, (vr, vr2) -> vr && vr2));
    }

    @Test
    public void testQualified() throws Exception {
        OWLParser cp = new OWLParser(_path_qualified);
        assertTrue(cp.validate().isEmpty());
        _val = new PropertyValidator(cp);

        List res = _val.validateRestrictions();
        //System.out.println(res.size());
        assertEquals(Collections.emptyList(), res);
        assertTrue(res.isEmpty());
    }
}