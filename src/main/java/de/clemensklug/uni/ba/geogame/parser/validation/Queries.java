/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame.parser.validation;

public class Queries {
    static final String PREFIXES = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
            "PREFIX geosparql: <http://www.opengis.net/ont/geosparql#>\n";
    static final String SUBJECT = "?subject";
    static final String PROPERTY = "?property";
    static final String NUMBER = "?number";
    static final String POINT = "?point";
    static final String OBJECT = "?object";
    static final String TYPE = "?type";

    /**
     * get all cardinality restrictions
     * <p>
     * String.format:
     * cardinality to select
     */
    static final String RESTRICTION_QUERY_QUANTITY = PREFIXES +
            "SELECT ?subject ?property ?number ?type\n" +
            "\tWHERE { ?restriction %s ?number.\n" +
            "?subject rdfs:subClassOf ?restriction. \n" +
            "?restriction owl:onProperty ?property.\n" +
            "OPTIONAL {?restriction owl:onClass ?type}}";

    /**
     * find individuals without property
     * <p>
     * String.format:
     * className
     * property
     */
    static final String EXISTENTIAL_QUERY = PREFIXES +
            "SELECT ?subject\n" +
            "\tWHERE {" +
            " ?subject rdf:type <%s>." +
            "FILTER NOT EXISTS {?subject <%s> ?name}" +
            "}";
    /**
     * get number and type of property per individual
     * <p>
     * String.format:
     * className
     * property
     */
    static final String QUANTITY_QUERY = PREFIXES +
            "SELECT ?number ?subject ?type ?object ?prop (<%2$s> as ?property) WHERE {\n" +
            "    {\n" +
            "        SELECT (COUNT(distinct ?object) as ?number) ?object WHERE{\n" +
            "                ?subject a <%s>. \n" +
            "                ?subject <%s>  ?object.\n" +
            "        }GROUP BY(?object)\n" +
            "    }.\n" +
            "    ?subject <%2$s> ?object.\n" +
            "    ?subject ?prop ?object.\n" +
            "    ?subject a <%1$s>.\n" +
            "    OPTIONAL {\n" +
            "        ?object a ?type.\n" +
            "        FILTER(?type != owl:NamedIndividual && ?type != rdfs:Resource && !isBlank(?type)).\n" +
            "    }\n" +
            "}";
    /**
     * get point for tokenhandler
     * String.format:
     * tokenhandler
     */
    static final String POINT_FOR_TOKENHANDLER = PREFIXES +
            "SELECT ?point\n" +
            "\tWHERE {" +
            "   ?action rdf:type <http://clemensklug.de/uni/ba/geogame#TokenAction>.\n" +
            "   ?action <http://clemensklug.de/uni/ba/geogame#tokenhandler-is> <%s>.\n" +
            "   ?action <http://clemensklug.de/uni/ba/geogame#at> ?point.}";
    /**
     * get points with geosparql-relation
     * String.format:
     * RCC
     * game
     */
    static final String POINTS_WITH_RCC = PREFIXES +
            "SELECT ?subject ?object\n" +
            "\tWHERE { ?subject %s ?object.\n" +
            "\t\t?action <http://clemensklug.de/uni/ba/geogame#at> ?subject.\n" +
            "\t\t<%s> <http://clemensklug.de/uni/ba/geogame#game_action> ?action. }";
}