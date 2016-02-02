/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame.parser;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.reasoner.ValidityReport;
import de.clemensklug.uni.ba.geogame.model.GeogameConfig;
import de.clemensklug.uni.ba.geogame.model.GeogameObject;
import de.clemensklug.uni.ba.geogame.model.TriggeringMode;
import de.clemensklug.uni.ba.geogame.model.actions.Action;
import de.clemensklug.uni.ba.geogame.model.actions.EchoAction;
import de.clemensklug.uni.ba.geogame.model.actions.EnableOtherActionAction;
import de.clemensklug.uni.ba.geogame.model.actions.TokenAction;
import de.clemensklug.uni.ba.geogame.model.challenge.QuestionChallenge;
import de.clemensklug.uni.ba.geogame.model.challenge.SyncTimeChallenge;
import de.clemensklug.uni.ba.geogame.model.conditions.*;
import de.clemensklug.uni.ba.geogame.model.spatial.Point;
import de.clemensklug.uni.ba.geogame.model.token.Token;
import de.clemensklug.uni.ba.geogame.model.token.TokenSet;
import de.clemensklug.uni.ba.geogame.model.token.handler.TokenDisperser;
import de.clemensklug.uni.ba.geogame.model.token.handler.TokenHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author clemens
 */
public class OWLParser implements ConfigParser {
    private final Map<String, GeogameObject> _instanceMap;
    private final Logger log = LogManager.getLogger(this.getClass());
    private final Mapping _mapping;
    private OntModel _ontModel;
    private OntProperty _atProperty;
    private OntProperty _enabledProperty;
    private OntProperty _synctimeProperty;
    private OntProperty _typeProperty;
    private OntProperty _latProperty;
    private OntProperty _lonProperty;
    private OntProperty _radProperty;
    private OntProperty _enablesProperty;
    private OntProperty _disablesProperty;
    private OntProperty _tokenhandlerProperty;
    private OntProperty _questionProperty;
    private OntProperty _tokenatProperty;
    private OntProperty _hasconditionProperty;
    private OntProperty _combinationProperty;
    private OntProperty _dispersedTokenProperty;
    private OntProperty _satisfiedProperty;
    private OntProperty _timeoutProperty;
    private OntProperty _echoText;
    private OntProperty _hasTokenProperty;
    private OntProperty _playersProperty;
    private OntProperty _gameActionProperty;
    private OntProperty _gameCondDrawProperty;
    private OntProperty _gameCondStartProperty;
    private OntProperty _gameCondWinProperty;
    private OntProperty _triggeringProperty;

    public OWLParser(String filename) {
        _instanceMap = new HashMap<>();
        _mapping = new Mapping();
        importConfig(filename);
    }

    /**
     * @param filename path to OWL in RDF/XML
     */
    @Override
    public void importConfig(String filename) {
        try {
            importConfig(new FileInputStream(new File(filename)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void importConfig(InputStream inputStream) {
        _ontModel = ModelFactory.createOntologyModel();
        _ontModel.setDynamicImports(true);
        _ontModel.read(inputStream, null, "RDF/XML");
        _atProperty = _ontModel.getOntProperty(Namespace.PROP_AT);
        _enabledProperty = _ontModel.getOntProperty(Namespace.PROP_ENABLED);
        _typeProperty = _ontModel.getOntProperty(Namespace.PROP_TYPE);
        _latProperty = _ontModel.getOntProperty(Namespace.PROP_LAT);
        _lonProperty = _ontModel.getOntProperty(Namespace.PROP_LONG);
        _radProperty = _ontModel.getOntProperty(Namespace.PROP_RADIUS);
        _enablesProperty = _ontModel.getOntProperty(Namespace.PROP_ENABLES);
        _disablesProperty = _ontModel.getOntProperty(Namespace.PROP_DISABLES);
        _tokenhandlerProperty = _ontModel.getOntProperty(Namespace.PROP_HANDLER);
        _synctimeProperty = _ontModel.getOntProperty(Namespace.PROP_SYNC);
        _questionProperty = _ontModel.getOntProperty(Namespace.PROP_QUESTION);
        _tokenatProperty = _ontModel.getOntProperty(Namespace.PROP_TOKEN_AT);
        _hasconditionProperty = _ontModel.getOntProperty(Namespace.PROP_SUBCONDITION);
        _combinationProperty = _ontModel.getOntProperty(Namespace.PROP_COMBINATION);
        _dispersedTokenProperty = _ontModel.getOntProperty(Namespace.PROP_DISPERSE);
        _satisfiedProperty = _ontModel.getOntProperty(Namespace.PROP_SATISFIED);
        _timeoutProperty = _ontModel.getOntProperty(Namespace.PROP_TIMEOUT);
        _echoText = _ontModel.getOntProperty(Namespace.PROP_ECHOTEXT);
        _hasTokenProperty = _ontModel.getOntProperty(Namespace.PROP_HAS_TOKEN);
        _playersProperty = _ontModel.getOntProperty(Namespace.PROP_GAME_PLAYERS);
        _gameActionProperty = _ontModel.getOntProperty(Namespace.PROP_GAME_ACTION);
        _gameCondDrawProperty = _ontModel.getOntProperty(Namespace.PROP_GAME_CONDITION_DRAW);
        _gameCondStartProperty = _ontModel.getOntProperty(Namespace.PROP_GAME_CONDITION_START);
        _gameCondWinProperty = _ontModel.getOntProperty(Namespace.PROP_GAME_CONDITION_WIN);
        _triggeringProperty = _ontModel.getOntProperty(Namespace.PROP_GAME_TRIGGERING);
    }

    public <T extends GeogameObject> List<T> getInstances(String ontClass) {
        List<Individual> individuals = _ontModel.listIndividuals().toList();
        List t = individuals.parallelStream()
                .filter(individual -> individual.hasOntClass(ontClass))
                .map(individual -> getInstance(individual, ontClass))
                .collect(Collectors.toList());
        return new LinkedList<>(t);
    }

    @Override
    public GeogameConfig getGeogame(String name) {
        return getInstance(name, Namespace.GEOGAME_ELEMENT);
    }

    @Override
    public <T extends GeogameObject> T getInstance(String name, String ontClass) {
        Individual individual = _ontModel.getIndividual(name);
        if (null != individual && individual.isIndividual()) {
            return getInstance(individual, ontClass);
        }
        throw new IllegalArgumentException("no instance of name '" + name + "' exists");
    }

    private <T extends GeogameObject> T newInstance(Individual individual) {
        List<String> types = new LinkedList<>();
        individual.listPropertyValues(_typeProperty).toList().forEach(type -> types.add(type.toString()));
        for (String type : types) {
            try {
                log.trace("newInstance of: " + type);
                return _mapping.newInstance(type);
            } catch (IllegalAccessError e) {
                log.debug(e.getMessage());
            }
        }
        log.error("instantiation failed for " + individual + " with types " + types);
        throw new IllegalArgumentException("instantiation failed for " + individual);
    }

    private <T extends GeogameObject> T getInstance(Individual individual, String ontClass) {
        if (!individual.hasOntClass(ontClass)) {
            log.trace("Individual " + individual + " has OntClass " + ontClass + " NOT");
            return null;
        }
        log.trace("Individual " + individual + " has OntClass " + ontClass);
        final String name = individual.toString();
        if (_instanceMap.containsKey(name)) {
            return (T) _instanceMap.get(name);
        }
        T instance = null;
        if (Namespace.isAction(ontClass)) {
            instance = buildAction(individual);
        } else if (Namespace.POINT.equals(ontClass)) {
            instance = buildPoint(individual);
        } else if (Namespace.TOKEN_HANLDER.equals(ontClass)) {
            instance = buildTokenHandler(individual);
        } else if (Namespace.TOKEN.equals(ontClass) || Namespace.TOKENSET.equals(ontClass)) {
            instance = buildToken(individual);
        } else if (Namespace.isCondition(ontClass)) {
            instance = buildCondition(individual);
        } else if (Namespace.GEOGAME_ELEMENT.equals(ontClass)) {
            instance = buildGeogame(individual);
        }
        if (instance == null) {
            log.error("instantiation of " + individual + " with class " + ontClass + " failed");
            return null;
        }
        instance.setName(name);
        _instanceMap.put(name, instance);
        return instance;
    }

    private <T extends GeogameObject> T buildGeogame(Individual individual) {
        GeogameConfig g = new GeogameConfig();
        g.setActions(filteredInstances(individual, _gameActionProperty, Namespace.ACTION));
        g.setPlayerCount(individual.getPropertyValue(_playersProperty).asLiteral().getInt());
        g.setDrawCondition((LogicCondition) filteredInstances(individual, _gameCondDrawProperty, Namespace.DRAW_CONDITION).get(0));
        g.setStartCondition((LogicCondition) filteredInstances(individual, _gameCondStartProperty, Namespace.START_CONDITION).get(0));
        g.setWinCondition((LogicCondition) filteredInstances(individual, _gameCondWinProperty, Namespace.WIN_CONDITION).get(0));
        if (individual.hasProperty(_triggeringProperty)) {
            g.setTriggeringMode(TriggeringMode.valueOf(individual.getProperty(_triggeringProperty).getString()));
        }
        return (T) g;
    }

    private <T extends GeogameObject> T buildCondition(Individual individual) {
        Condition c = newInstance(individual);
        if (individual.hasProperty(_satisfiedProperty)) {
            List<Action> actions = filteredInstances(individual, _satisfiedProperty, Namespace.ACTION);
            if (!actions.isEmpty()) {
                c.setSatisfiedAction(actions.get(0));
            }
        }
        if (c instanceof TokenCondition) {
            TokenCondition tokenCondition = (TokenCondition) c;
            tokenCondition.setTokenhandlers(filteredInstances(individual, _tokenatProperty, Namespace.TOKEN_HANLDER));
        } else if (c instanceof LogicCondition) {
            LogicCondition logicCondition = (LogicCondition) c;
            logicCondition.setConditions(filteredInstances(individual, _hasconditionProperty, Namespace.CONDITION));
            if (individual.hasProperty(_combinationProperty)) {
                logicCondition.setLogicMode(LogicCondition.LogicMode.valueOf(individual.getProperty(_combinationProperty).getString()));
            }
        } else if (c instanceof PlayerLocationCondition) {
            PlayerLocationCondition playerLocationCondition = (PlayerLocationCondition) c;
            playerLocationCondition.setPoints(filteredInstances(individual, _atProperty, Namespace.POINT));
        } else if (c instanceof TimeOutCondition) {
            TimeOutCondition timeOutCondition = (TimeOutCondition) c;
            if (individual.hasProperty(_timeoutProperty)) {
                timeOutCondition.setMaxDuration(individual.getProperty(_timeoutProperty).getInt());
            }
        }
        return (T) c;
    }

    private <T extends GeogameObject> T buildToken(Individual individual) {
        Token t = newInstance(individual);
        t.setValue(individual.toString());
        if (t instanceof TokenSet) {
            if (individual.hasProperty(_hasTokenProperty)) {
                ((TokenSet) t).setTokens(filteredInstances(individual, _hasTokenProperty, Namespace.TOKEN));
            }
        }
        return (T) t;
    }

    private <T extends GeogameObject> T buildTokenHandler(Individual individual) {
        TokenHandler tokenHandler = newInstance(individual);
        if (tokenHandler instanceof TokenDisperser) {
            TokenDisperser handler = (TokenDisperser) tokenHandler;
            List<Token> tokens;
            if (individual.hasProperty(_dispersedTokenProperty)) {
                tokens = filteredInstances(individual, _dispersedTokenProperty, Namespace.TOKENSET);
            } else {
                tokens = Collections.emptyList();
            }
            handler.setTokens(tokens);
            log.trace("tokens: " + tokens + "; actually used tokens: " + tokens);
        }
        return (T) tokenHandler;
    }

    private <T extends GeogameObject> T buildPoint(Individual individual) {
        double latitude = individual.getProperty(_latProperty).getDouble();
        double longitude = individual.getProperty(_lonProperty).getDouble();
        double radius = 0;
        if (individual.hasProperty(_radProperty)) {
            radius = individual.getProperty(_radProperty).getDouble();
        }
        Point point = _mapping.newInstance(Namespace.POINT);
        point.setLatitude(latitude);
        point.setLongitude(longitude);
        point.setRadius(radius);
        return (T) point;
    }

    private <T extends GeogameObject> T buildAction(Individual individual) {
        Action action = newInstance(individual);
        List<Point> points = filteredInstances(individual, _atProperty, Namespace.POINT);
        action.setTriggers(points);
        if (individual.hasProperty(_enabledProperty)) {
            action.setEnabled(individual.getProperty(_enabledProperty).getBoolean());
        }
        if (individual.hasProperty(_questionProperty)) {
            QuestionChallenge c = _mapping.newInstance(Namespace.COMPOUND);
            c.setQuestion(individual.getProperty(_questionProperty).getString());
            action.addChallenge(c);
        }
        if (individual.hasProperty(_synctimeProperty)) {
            SyncTimeChallenge c = _mapping.newInstance(Namespace.SYNCTIME);
            c.setSyncTime(individual.getProperty(_synctimeProperty).getInt());
            action.addChallenge(c);
        }
        if (action instanceof EnableOtherActionAction) {
            ((EnableOtherActionAction) action).setEnableActions(filteredInstances(individual, _enablesProperty, Namespace.ACTION));
            ((EnableOtherActionAction) action).setDisableActions(filteredInstances(individual, _disablesProperty, Namespace.ACTION));
        } else if (action instanceof TokenAction) {
            final List<TokenHandler> ts = filteredInstances(individual, _tokenhandlerProperty, Namespace.TOKEN_HANLDER);
            if (!ts.isEmpty()) {
                TokenHandler th = ts.get(0);
                ((TokenAction) action).setHandler(th);
            }
        } else if (action instanceof EchoAction) {
            if (individual.hasProperty(_echoText)) {
                ((EchoAction) action).setText(individual.getProperty(_echoText).getString());
            }
        }
        return (T) action;
    }

    /**
     * get all Individuals of ontClass, which are referenced from individual with property
     *
     * @param individual the indiviual which refers the wanted individuals
     * @param property   the property which refers the wanted individuals
     * @param ontClass   the OWL type of the wanted individuals
     * @param <T>        any implementation of GeogameObject
     */
    private <T extends GeogameObject> List<T> filteredInstances(Individual individual, OntProperty property, String ontClass) {
        final List<RDFNode> rdfNodes = individual.listPropertyValues(property).toList();
        List t = rdfNodes.parallelStream()
                .map(rdfNode -> getInstance(_ontModel.getIndividual(rdfNode.toString()), ontClass))
                .collect(Collectors.toList());
        return new LinkedList<>(t);
    }

    @Override
    public List<QuerySolution> runQuery(String query) {
        log.debug("executing query '" + query + "'");
        List<QuerySolution> results = new LinkedList<>();
        Query q = QueryFactory.create(query);
        try (QueryExecution e = QueryExecutionFactory.create(q, _ontModel)) {
            e.execSelect().forEachRemaining(results::add);
        }
        return results;
    }

    public List<Map<String, String>> runQueryString(String Query) {
        final List<Map<String, String>> q = new LinkedList<>();
        runQuery(Query)
                .forEach(querySolution -> {
                    final List<String> var = new LinkedList<>();
                    querySolution.varNames().forEachRemaining(var::add);
                    q.add(var.stream().collect(Collectors.toMap(Function.identity(), s -> querySolution.get(s).toString())));
                });
        return q;
    }

    @Override
    public List<String> validate() {
        ValidityReport report = _ontModel.validate();
        if (report.isValid()) {
            return Collections.emptyList();
        }
        List<String> results = new LinkedList<>();
        report.getReports().forEachRemaining(r -> results.add(r.toString()));
        return results;
    }

    @Override
    public boolean isSubClass(String superClass, String subClass) {
        if (superClass.equals(subClass)) {
            return true;
        }
        return _ontModel.getOntClass(superClass).listSubClasses().toList().stream().
                map(Object::toString).filter(sc -> sc.equals(subClass)).findFirst().isPresent();
    }
}
