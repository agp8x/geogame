/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame.parser.validation;

import com.hp.hpl.jena.query.QuerySolution;
import de.clemensklug.uni.ba.geogame.model.conditions.Condition;
import de.clemensklug.uni.ba.geogame.model.conditions.LogicCondition;
import de.clemensklug.uni.ba.geogame.model.conditions.TokenCondition;
import de.clemensklug.uni.ba.geogame.model.spatial.Point;
import de.clemensklug.uni.ba.geogame.model.token.handler.TokenHandler;
import de.clemensklug.uni.ba.geogame.parser.ConfigParser;
import de.clemensklug.uni.ba.geogame.parser.Namespace;
import de.clemensklug.uni.ba.geogame.parser.RCC8;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.clemensklug.uni.ba.geogame.model.spatial.Point.distance;

/**
 * Created by clemens on 03.12.15.
 *
 * @author clemens
 */
public class SpatialValidator {
    protected static final Logger log = LogManager.getLogger(GeoTTTValidator.class);

    /**
     * calculate average distance between points
     *
     * @param points List od points
     * @return average distance
     */
    public double averageDistance(List<Point> points) {
        return points.parallelStream().map(point -> points.parallelStream()
                        .filter(point1 -> point != point1)
                        .collect(Collectors.summarizingDouble(point2 -> distance(point2, point)))
                        .getAverage()
        ).collect(Collectors.summarizingDouble(Double::doubleValue)).getAverage();
    }

    /**
     * categorize distances of one point to all other points relating to the global average distance with fixed precision of 1
     *
     * @param points List of points
     * @return Map of Categories with assigned Distances
     */
    public Map<Category, List<Distance>> categorizePoints(List<Point> points) {
        return categorizePoints(points, 1);
    }

    /**
     * categorize distances of one point to all other points relating to the global average distance
     *
     * @param points    List of points
     * @param precision factor to comparison to average
     * @return Map of Categories with assigned Distances
     */
    public Map<Category, List<Distance>> categorizePoints(List<Point> points, double precision) {
        double avg = averageDistance(points);
        Map<Category, List<Distance>> map = Stream.of(Category.values()).
                collect(Collectors.toMap(Function.identity(), o -> new LinkedList<>()));
        for (Point p : points) {
            double dist = points.stream().parallel().filter(point -> p != point)
                    .collect(Collectors.summarizingDouble(point -> distance(point, p)))
                    .getAverage();
            Category c = Category.getCategory(precision, avg, dist);
            map.get(c).add(new Distance(dist, p));
        }
        return map;
    }


    /**
     * indented and simplified String of a Category Map
     *
     * @param map Map to format
     * @return formatted Map
     */
    public String formatMap(Map<Category, List<Distance>> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        map.forEach((category, distances) -> {
                    sb.append("\t").append(category).append("=[\n");
                    distances.forEach(distance -> {
                        sb.append("\t\tDistance{\n\t\t\tdistance=").append(distance._distance);
                        sb.append(",\n\t\t\tpoint=#").append(distance._point.getName().split("#")[1]);
                        sb.append("\n\t\t},\n");
                    });
                    sb.append("\t],\n");
                }
        );
        sb.append("}");
        return sb.toString();
    }

    /**
     * Check all RCC8 restriction types on a game
     *
     * @param cp   ConfigParser to load game from
     * @param game instance of the game
     * @return ValidationResults
     */
    public List<ValidationResult> checkAllRCC(ConfigParser cp, String game) {
        return Stream.of(RCC8.values()).parallel()
                .map(rcc -> checkRCC(cp, game, rcc))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    /**
     * check a single RCC8 restriction type on a game
     *
     * @param cp   ConfigParser to load game from
     * @param game instance of the game
     * @param rcc  RCC8 relation
     * @return ValidationResults
     */
    public List<ValidationResult> checkRCC(ConfigParser cp, String game, RCC8 rcc) {
        log.debug("check RCC8 " + rcc + " of game " + game);
        return cp.runQuery(String.format(Queries.POINTS_WITH_RCC, rcc, game)).parallelStream()
                .map(querySolution -> rcc(querySolution, cp, rcc))
                .filter(ValidationResult::isInvalid)
                .collect(Collectors.toList());
    }

    /**
     * check a single pair of points regarding RCC8
     *
     * @param solution QuerySolution with points
     * @param cp       ConfigParser to load points from
     * @param rcc      RCC8 relation
     * @return ValidationResult
     */
    private ValidationResult rcc(QuerySolution solution, ConfigParser cp, RCC8 rcc) {
        Point a = getPoint(cp, solution, Queries.SUBJECT);
        Point b = getPoint(cp, solution, Queries.OBJECT);
        if (Point.rcc(rcc, a, b)) {
            return ValidationResult.valid();
        }
        List<String> instances = new LinkedList<>();
        instances.add(a.getName());
        instances.add(b.getName());
        return ValidationResult.invalid(instances, "RCC " + rcc + " violated");
    }

    /**
     * @param cp       ConfigParser to load points from
     * @param solution QuerySolution containing identifier
     * @param variable name of the variable of the wanted Point in solution
     * @return Point
     */
    private Point getPoint(ConfigParser cp, QuerySolution solution, String variable) {
        return cp.getInstance(solution.get(variable).toString(), Namespace.POINT);
    }

    /**
     * assuming all TokenConditions in the game are grouping Points in "lines", the average distances in these lines is calculated
     *
     * @param cp   ConfigParser to load game from
     * @param game instance of the game
     * @return LineDistances
     */
    public List<LineDistance> lineDistances(ConfigParser cp, String game) {
        List<TokenCondition> lines = getTokenConditions(cp.getGeogame(game).getWinCondition());
        List<LineDistance> distances = new LinkedList<>();
        for (TokenCondition c : lines) {
            List<Point> points = new LinkedList<>();
            for (TokenHandler h : c.getHandlers()) {
                List<QuerySolution> solutions = cp.runQuery(String.format(Queries.POINT_FOR_TOKENHANDLER, h.getName()));
                for (QuerySolution s : solutions) {
                    Point instance = getPoint(cp, s, Queries.POINT);
                    points.add(instance);
                }
            }
            distances.add(new LineDistance(averageDistance(points), categorizePoints(points)));
        }
        log.debug(distances);
        return distances;
    }

    /**
     * get all nested TokenConditions
     *
     * @param winCondition Condition to start from
     * @return All TokenConditions
     */
    private List<TokenCondition> getTokenConditions(LogicCondition winCondition) {
        List<TokenCondition> lines = new LinkedList<>();
        Queue<Condition> conditions = new LinkedList<>();
        conditions.addAll(winCondition.getConditions());
        while (!conditions.isEmpty()) {
            Condition c = conditions.poll();
            if (c instanceof TokenCondition) {
                lines.add((TokenCondition) c);
            } else if (c instanceof LogicCondition) {
                conditions.addAll(((LogicCondition) c).getConditions());
            }
        }
        return lines;
    }

    public enum Category {
        ABOVE, AVERAGE, BELOW;

        public static Category getCategory(double precision, double average, double distance) {
            if (distance > average * precision) {
                return Category.ABOVE;
            } else if (distance < average / precision) {
                return Category.BELOW;
            }
            return Category.AVERAGE;
        }
    }

    /**
     * Store a Point and a Distance
     */
    public class Distance {
        private final Point _point;
        private final double _distance;

        public Distance(double distance, Point point) {
            _distance = distance;
            _point = point;
        }

        @Override
        public String toString() {
            return "Distance{" +
                    "distance=" + _distance +
                    ", point=" + _point +
                    '}';
        }

    }

    /**
     * Store a bunch of categorized Distances
     */
    public class LineDistance {
        private final double _average;
        private final Map<Category, List<Distance>> _distances;

        public LineDistance(double average, Map<Category, List<Distance>> distances) {
            _average = average;
            _distances = distances;
        }

        @Override
        public String toString() {
            return "LineDistance{" +
                    "average=" + _average +
                    ", distances=" + _distances +
                    '}';
        }
    }
}
