package de.clemensklug.uni.ba.geogame;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.reasoner.ValidityReport;
import de.clemensklug.uni.ba.geogame.parser.OWLParser;
import de.clemensklug.uni.ba.geogame.parser.validation.GeoTTTValidator;
import de.clemensklug.uni.ba.geogame.parser.validation.PropertyValidator;
import de.clemensklug.uni.ba.geogame.parser.validation.ValidationResult;
import org.junit.Test;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mindswap.pellet.jena.PelletReasonerFactory.THE_SPEC;

/**
 * Created by clemens on 27.11.15.
 *
 * @author clemens
 */
public class GeoTTTvalid {
    private final StringBuilder _err = new StringBuilder();
    public static final String PELLET_GEOSPARQL_ERROR = "Unsupported axiom: Ignoring object value used with DatatypeProperty: http://www.opengis.net/ont/geosparql @dc:source http://www.opengis.net/doc/IS/geosparql/1.0\n";
    private final String MAIN_FIELD = "src/main/resources/geoTTT.owl";
    private final String MAIN_GAME = "http://clemensklug.de/uni/ba/geogame/geoTTT#GeoTTTgame";
    private final String TEST_FIELD = "src/test/resources/geoTTT.owl";
    private final String TEST_GAME = "http://clemensklug.de/uni/ba/geogame/geoTTT#GeoTTTgame";

    @Test
    public void testGeotttValid() throws Exception {
        validate(MAIN_FIELD);
    }

    @Test
    public void testGeotttSpatialValid() throws Exception {
        assertTrue(spatialValid(MAIN_FIELD, MAIN_GAME));
    }

    @Test
    public void testGeotttTestValid() throws Exception {
        validate(TEST_FIELD);
    }

    @Test
    public void testGeotttTestSpatialValid() throws Exception {
        assertFalse(spatialValid(TEST_FIELD, TEST_GAME));
    }

    @Test
    public void testGeoTTTdefPartiallyInvalid() throws Exception {
        //the definition on itself is a valid ontology, but misses some properties to get a fully valid geogame
        validate("geoTTTdef.owl", 5);
    }

    private void validate(String file) throws FileNotFoundException {
        validate(file, 0);
    }

    private void validate(String file, int number) throws FileNotFoundException {
        OntModel m = ModelFactory.createOntologyModel(THE_SPEC);
        m.read(new FileInputStream(new File(file)), null, "RDF/XML");
        ValidityReport validityReport = getValidityReport(file, m);
        if (!validityReport.isValid()) {
            validityReport.getReports().forEachRemaining(System.err::println);
            fail("validation failed on ValidityReport");
        }
        assertTrue(m.validate().isValid());
        PropertyValidator val = new PropertyValidator(new OWLParser(file));
        List<ValidationResult> results = val.validateRestrictions();
        if (results.size() != number) {
            System.err.println(results);
            fail("validation failed on PropertyValidator");
        }
        assertTrue(results.size() == number);
    }

    private boolean spatialValid(String file, String game) {
        GeoTTTValidator val = new GeoTTTValidator();
        List<ValidationResult> list = val.checkAllRCC(new OWLParser(file), game);
        if (!list.isEmpty()) {
            System.out.println(list);
        }
        return list.isEmpty();
    }

    private ValidityReport getValidityReport(String file, OntModel m) {
        //Pellet has a problem with a tripel of GEOSPARQL, so we override System.err, so we can hide this special error
        PrintStream err = System.err;
        System.setErr(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                _err.append((char) b);
            }
        }));
        ValidityReport validityReport = m.validate();
        System.setErr(err);
        String error = _err.toString();
        if (!error.isEmpty() && !error.endsWith(PELLET_GEOSPARQL_ERROR)) {
            //but all other errors should be printed
            System.err.println(file);
            System.err.println(error);
        }
        return validityReport;
    }
}