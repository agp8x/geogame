/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame.parser;

/**
 * @author clemens
 */
public class Namespace {
    //XML-Namespaces
    public final static String GEOGAME_NAMESPACE = "http://clemensklug.de/uni/ba/geogame#";
    public final static String RDF_NAMESPACE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    public final static String GEO_NAMESPACE = "http://www.w3.org/2003/01/geo/wgs84_pos#";
    public final static String GEORSS_NAMESPACE = "http://www.georss.org/georss/";
    //Elements
    public static final String GEOGAME_ELEMENT = GEOGAME_NAMESPACE + "Geogame";
    public static final String GEOTTT_ELEMENT = GEOGAME_NAMESPACE + "GeoTTT";
    public final static String POINT = GEO_NAMESPACE + "Point";
    //Actions
    public final static String ACTION = GEOGAME_NAMESPACE + "Action";
    public final static String ECHO_ACTION = GEOGAME_NAMESPACE + "EchoAction";
    public final static String ENABLE_ACTION = GEOGAME_NAMESPACE + "EnableOtherActionAction";
    public final static String TOKEN_ACTION = GEOGAME_NAMESPACE + "TokenAction";
    //TokenHandler
    public final static String TOKEN_HANLDER = GEOGAME_NAMESPACE + "TokenHandler";
    public final static String TOKEN_CAPTURE = GEOGAME_NAMESPACE + "TokenCapture";
    public final static String TOKEN_DISPERSER = GEOGAME_NAMESPACE + "TokenDisperser";
    public final static String TOKEN_COUNTER = GEOGAME_NAMESPACE + "TokenCount";
    //Challenge
    public final static String SYNCTIME = GEOGAME_NAMESPACE + "SyncTimeChallenge";
    public final static String COMPOUND = GEOGAME_NAMESPACE + "QuestionChallenge";
    //Condition
    public final static String CONDITION = GEOGAME_NAMESPACE + "Condition";
    public final static String TIMEOUT_CONDITION = GEOGAME_NAMESPACE + "TimeOutCondition";
    public final static String TOKEN_CONDITION = GEOGAME_NAMESPACE + "TokenCondition";
    public final static String PLAYER_LOCATION_CONDITION = GEOGAME_NAMESPACE + "PlayerLocationCondition";
    public final static String LOGIC_CONDITION = GEOGAME_NAMESPACE + "LogicCondition";
    public final static String START_CONDITION = GEOGAME_NAMESPACE + "StartCondition";
    public final static String WIN_CONDITION = GEOGAME_NAMESPACE + "WinCondition";
    public static final String DRAW_CONDITION = GEOGAME_NAMESPACE + "DrawCondition";
    //Token
    public final static String TOKEN = GEOGAME_NAMESPACE + "Token";
    public final static String MARKER = GEOGAME_NAMESPACE + "Marker";
    public static final String TOKENSET = GEOGAME_NAMESPACE + "TokenSet";

    //Properties
    public final static String PROP_TYPE = RDF_NAMESPACE + "type";
    //Geogame
    public static final String PROP_GAME_PLAYERS = GEOGAME_NAMESPACE + "players";
    public static final String PROP_GAME_ACTION = GEOGAME_NAMESPACE + "game_action";
    public static final String PROP_GAME_CONDITION_DRAW = GEOGAME_NAMESPACE + "game_condition_draw";
    public static final String PROP_GAME_CONDITION_START = GEOGAME_NAMESPACE + "game_condition_start";
    public static final String PROP_GAME_CONDITION_WIN = GEOGAME_NAMESPACE + "game_condition_win";
    //Point
    public final static String PROP_LAT = GEO_NAMESPACE + "lat";
    public final static String PROP_LONG = GEO_NAMESPACE + "long";
    public final static String PROP_RADIUS = GEORSS_NAMESPACE + "radius";
    //Action
    public final static String PROP_AT = GEOGAME_NAMESPACE + "at";
    public final static String PROP_ENABLED = GEOGAME_NAMESPACE + "enabled";
    public final static String PROP_ENABLES = GEOGAME_NAMESPACE + "enables";
    public final static String PROP_DISABLES = GEOGAME_NAMESPACE + "disables";
    public final static String PROP_HANDLER = GEOGAME_NAMESPACE + "tokenhandler-is";
    public final static String PROP_SYNC = GEOGAME_NAMESPACE + "synctime";
    public final static String PROP_QUESTION = GEOGAME_NAMESPACE + "question";
    //TokenHandler
    public final static String PROP_DISPERSE = GEOGAME_NAMESPACE + "dispersed-tokens";
    //Token
    public static final String PROP_HAS_TOKEN = GEOGAME_NAMESPACE + "has-token";
    //Condition
    public final static String PROP_TOKEN_AT = GEOGAME_NAMESPACE + "token-at";
    public final static String PROP_SUBCONDITION = GEOGAME_NAMESPACE + "has-condition";
    public final static String PROP_COMBINATION = GEOGAME_NAMESPACE + "combination";
    public final static String PROP_SATISFIED = GEOGAME_NAMESPACE + "satisfied-action";
    public static final String PROP_TIMEOUT = GEOGAME_NAMESPACE + "timeout";
    public static final String PROP_ECHOTEXT = GEOGAME_NAMESPACE + "text";

    public static boolean isAction(String name) {
        return name.endsWith("Action");
    }

    public static boolean isCondition(String name) {
        return name.endsWith("Condition");
    }
}
