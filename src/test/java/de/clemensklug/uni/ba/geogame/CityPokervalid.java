/*
 *  See the file "LICENSE" for the full license governing this code.
 */

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
import java.util.List;

import static org.junit.Assert.*;
import static org.mindswap.pellet.jena.PelletReasonerFactory.THE_SPEC;

/**
 * Created by clemens on 27.01.16.
 *
 * @author clemens
 */
public class CityPokervalid {
    private final StringBuilder _err = new StringBuilder();
    public static final String PELLET_GEOSPARQL_ERROR = "Unsupported axiom: Ignoring object value used with DatatypeProperty: http://www.opengis.net/ont/geosparql @dc:source http://www.opengis.net/doc/IS/geosparql/1.0\n";
    private final String GAME_DEF = "citypoker.owl";
    private final String GAME_FIELD = "CPinst.owl";
    private final String MAIN_GAME = "http://clemensklug.de/uni/ba/geogame/citypoker#CityPokergame";

    @Test
    public void testCityPokerDefInvalid() throws Exception {
        validate(GAME_DEF, 8);
    }

    @Test
    public void testCityPokerField() throws Exception {
        validate(GAME_FIELD);
    }

    @Test
    public void testCityPokerFieldSpatial() throws Exception {
        spatialValid(GAME_FIELD, MAIN_GAME);
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
        }
        assertEquals("validation failed on PropertyValidator", number, results.size());
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
