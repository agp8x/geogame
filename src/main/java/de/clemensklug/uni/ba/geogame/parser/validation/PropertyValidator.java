package de.clemensklug.uni.ba.geogame.parser.validation;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.rdf.model.RDFNode;
import de.clemensklug.uni.ba.geogame.parser.Cardinality;
import de.clemensklug.uni.ba.geogame.parser.ConfigParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by clemens on 19.11.15.
 *
 * @author clemens
 */
public class PropertyValidator {
    //TODO: respect GeogameConfig (i.e. only check one game at a time)
    private final ConfigParser _parser;
    private final Logger log = LogManager.getLogger(this.getClass());

    public PropertyValidator(ConfigParser cp) {
        this._parser = cp;
        List<String> reports = cp.validate();
        if (!reports.isEmpty()) {
            reports.forEach(log::error);
            throw new RuntimeException();
        }
    }

    /**
     * get violations to existential Restriction
     *
     * @param restriction existential Restriction
     * @return ValidationResult
     */
    public ValidationResult validateExistential(Restriction restriction) {
        String query = String.format(Queries.EXISTENTIAL_QUERY, restriction.getClassName(), restriction.getProperty());
        List<QuerySolution> results = _parser.runQuery(query);
        if (!results.isEmpty()) {
            List<String> instances = results.parallelStream().map(Object::toString).collect(Collectors.toList());
            log.debug("EXISTENTIAL FAIL");
            return ValidationResult.invalid(instances, restriction.getProperty());
        }
        return ValidationResult.valid();
    }

    /**
     * get violations of a Restriction and a Cardinality
     *
     * @param card        Cardinality to check
     * @param restriction Restriction to check
     * @return ValidationResult as result of the validation
     */
    private ValidationResult validateQuantified(Cardinality card, Restriction restriction) {
        ValidationResult result = ValidationResult.valid();
        Comparer comp = new Comparer(card);
        List<QuerySolution> querySolutions = quantifiedQuery(restriction.getClassName(), restriction.getProperty());
        List<String> offending = querySolutions
                .parallelStream()
                .filter(querySolution -> invalidRestriction(restriction, querySolution, comp))
                .map(Object::toString).collect(Collectors.toList());
        //using a reasoner, restrictions get propagated along the class-tree
        int solutions = querySolutions.size();  //propagated restrictions
        int violations = offending.size();      //invalid restrictions
        int valid = solutions - violations;     //valid restrictions
        log.trace(solutions + ", " + violations + ", " + valid + "==" + restriction.getQuantity());
        //when there are no solutions, there exists no indiviual with fitting type
        //to decide validity, there needs to be a valid restriction for every solution
        //therefore `solution` times n valid solutions need to exist
        if (solutions > 0 && valid == solutions * restriction.getQuantity()) {
            result = ValidationResult.invalid(offending, restriction.toString());
        }
        if (restriction.getQuantity() > 0) {
            result = result.add(validateExistential(restriction));
        }
        return result;
    }

    /**
     * evalute invalidity of a QuerySolution to a Restriction using a Comparer
     *
     * @param r Restriction
     * @param q QuerySolution
     * @param c Comparer
     * @return validity true if invalid
     */
    private boolean invalidRestriction(Restriction r, QuerySolution q, Comparer c) {
        if (r.isQualifiedCardinalityRestriction() && q.contains(Queries.TYPE)) {
            String queryType = q.get(Queries.TYPE).toString();
            log.trace("test query: " + queryType + ", " + r.getType());
            //inference gets us all the supertypes AND it propagates restrictions, so we need to check subclassing
            if (/*!Namespace.getFields().contains(q.get(Queries.TYPE).toString())
                    ||*/ !_parser.isSubClass(r.getType(), queryType)) {
                log.trace("REJECT!");
                return true;
            }
            log.debug(r + ", " + q + ", " + queryType + "==" + "<" + r.getType() + ">" + "??" + _parser.isSubClass(r.getType(), queryType));
        }
        if (!q.contains(Queries.SUBJECT)) {
            return true;
        }
        int number = q.get(Queries.NUMBER).asLiteral().getInt();
        if (!c.valid(r.getQuantity(), number)) {
            return true;
        }
        return false;
    }

    /**
     * format and run query to get the number of properties of all instances of className
     *
     * @param className className (Ontology)
     * @param property  property (Ontology)
     * @return QuerySolutions
     */
    private List<QuerySolution> quantifiedQuery(String className, String property) {
        String query = String.format(Queries.QUANTITY_QUERY, className, property);
        return _parser.runQuery(query);
    }

    /**
     * Load and validate all Restrictions
     *
     * @return invalid ValidationResults
     */
    public List<ValidationResult> validateRestrictions() {
        return validate(getRestrictions());
    }

    /**
     * Load all Restrictions
     *
     * @return Restrictions mapped by Cardinality
     */
    public Map<Cardinality, List<Restriction>> getRestrictions() {
        return Stream.of(Cardinality.values()).parallel()
                .collect(Collectors.toMap(Function.identity(), this::getRestrictions));
    }

    /**
     * load all Restrictions of given Cardinality from parser
     *
     * @param c Cardinality
     * @return Restrictions
     */
    private List<Restriction> getRestrictions(Cardinality c) {
        String query = String.format(Queries.RESTRICTION_QUERY_QUANTITY, c);
        return _parser.runQuery(query).parallelStream()
                .filter(solution -> !solution.get(Queries.SUBJECT).isAnon())
                .map(this::getRestriction)
                .collect(Collectors.toList());
    }

    /**
     * build a Restriction from a QuerySolution
     *
     * @param s QuerySolution
     * @return Restriction
     */
    private Restriction getRestriction(QuerySolution s) {
        RDFNode subject = s.get(Queries.SUBJECT);
        Restriction r;
        RDFNode number = s.get(Queries.NUMBER);
        String property = s.get(Queries.PROPERTY).toString();
        String type = null;
        if (s.contains(Queries.TYPE)) {
            type = s.get(Queries.TYPE).toString();
        }
        if (number.isLiteral()) {
            r = new Restriction(subject.toString(), property, number.asLiteral().getInt(), type);
        } else {
            // existential has a resource as object, so we substitute it with a reasonable number
            r = new Restriction(subject.toString(), property, 1, number.toString());
        }
        return r;
    }

    /**
     * validate all given restrictions
     *
     * @param restrictions map of restrictions
     * @return all invalid ValidationResults
     */
    List<ValidationResult> validate(Map<Cardinality, List<Restriction>> restrictions) {
        return restrictions.entrySet().parallelStream()
                .map(this::processCardinality)
                .flatMap(Collection::stream).collect(Collectors.toList());
    }

    /**
     * process one <Cardinality, Restrictions> Entry
     *
     * @param restEntry entry
     * @return List von results
     */
    private List<ValidationResult> processCardinality(Map.Entry<Cardinality, List<Restriction>> restEntry) {
        return restEntry.getValue().parallelStream()
                .map(restriction -> validateQuantified(restEntry.getKey(), restriction))
                .filter(ValidationResult::isInvalid)
                .collect(Collectors.toList());
    }

    /**
     * Convenience class for consecutive comparison of values with Cardinality strategy
     */
    private class Comparer {
        private final Cardinality _cardinality;

        private Comparer(Cardinality cardinality) {
            _cardinality = cardinality;
        }

        private boolean valid(int restriction, int value) {
            switch (_cardinality) {
                case EXACT:
                    return restriction == value;
                case MIN:
                    return restriction <= value;
                case MAX:
                    return value <= restriction;
                case SOME:
                    return value > 0;
            }
            return false;
        }
    }
}
