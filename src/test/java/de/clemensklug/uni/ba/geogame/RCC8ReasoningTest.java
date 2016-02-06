/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import org.junit.Test;
import org.mindswap.pellet.jena.PelletInfGraph;
import org.mindswap.pellet.jena.PelletReasonerFactory;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by clemens on 24.01.16.
 *
 * @author clemens
 */
public class RCC8ReasoningTest {
    private OntModel _ontModel;
    private final String _path = "src/test/resources/rcc8/geosparql.owl";

    @Test
    public void testPelletRCC8() throws Exception {
        _ontModel = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC );
        _ontModel.setDynamicImports(true);
        _ontModel.read(new FileInputStream(new File(_path)), null, "RDF/XML");
        ((PelletInfGraph) _ontModel.getGraph()).classify();
        for(Resource i:_ontModel.listSubjects().toList()){
            if (!i.toString().contains("agp8x")){
                continue;
            }
            System.out.println(i);
            for(Statement s:i.listProperties().toList()){
                System.out.print("\t");
                System.out.println(s);
            }
        }
    }
}