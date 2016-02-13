/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame.parser;

import de.clemensklug.uni.ba.geogame.model.GeogameConfig;
import de.clemensklug.uni.ba.geogame.model.GeogameObject;
import de.clemensklug.uni.ba.geogame.model.actions.EchoAction;
import de.clemensklug.uni.ba.geogame.model.actions.EnableOtherActionAction;
import de.clemensklug.uni.ba.geogame.model.actions.TokenAction;
import de.clemensklug.uni.ba.geogame.model.challenge.QuestionChallenge;
import de.clemensklug.uni.ba.geogame.model.challenge.SyncTimeChallenge;
import de.clemensklug.uni.ba.geogame.model.conditions.LogicCondition;
import de.clemensklug.uni.ba.geogame.model.conditions.PlayerLocationCondition;
import de.clemensklug.uni.ba.geogame.model.conditions.TimeOutCondition;
import de.clemensklug.uni.ba.geogame.model.conditions.TokenCondition;
import de.clemensklug.uni.ba.geogame.model.spatial.Point;
import de.clemensklug.uni.ba.geogame.model.token.MarkerToken;
import de.clemensklug.uni.ba.geogame.model.token.Token;
import de.clemensklug.uni.ba.geogame.model.token.TokenSet;
import de.clemensklug.uni.ba.geogame.model.token.handler.TokenCapture;
import de.clemensklug.uni.ba.geogame.model.token.handler.TokenCounter;
import de.clemensklug.uni.ba.geogame.model.token.handler.TokenDispenser;

import java.util.HashMap;
import java.util.Map;

/**
 * @author clemens
 */
class Mapping {
    private final Map<String, Class> _classMap;

    public Mapping() {
        _classMap = new HashMap<>();
        _classMap.put(Namespace.POINT, Point.class);
        _classMap.put(Namespace.ECHO_ACTION, EchoAction.class);
        _classMap.put(Namespace.ENABLE_ACTION, EnableOtherActionAction.class);
        _classMap.put(Namespace.TOKEN_CAPTURE, TokenCapture.class);
        _classMap.put(Namespace.TOKEN_COUNTER, TokenCounter.class);
        _classMap.put(Namespace.TOKEN_DISPENSER, TokenDispenser.class);
        _classMap.put(Namespace.TOKEN_ACTION, TokenAction.class);
        _classMap.put(Namespace.SYNCTIME, SyncTimeChallenge.class);
        _classMap.put(Namespace.COMPOUND, QuestionChallenge.class);
        _classMap.put(Namespace.TOKEN_CONDITION, TokenCondition.class);
        _classMap.put(Namespace.LOGIC_CONDITION, LogicCondition.class);
        _classMap.put(Namespace.WIN_CONDITION, LogicCondition.class);
        _classMap.put(Namespace.START_CONDITION, LogicCondition.class);
        _classMap.put(Namespace.TOKEN, Token.class);
        _classMap.put(Namespace.MARKER, MarkerToken.class);
        _classMap.put(Namespace.TIMEOUT_CONDITION, TimeOutCondition.class);
        _classMap.put(Namespace.PLAYER_LOCATION_CONDITION, PlayerLocationCondition.class);
        _classMap.put(Namespace.TOKENSET, TokenSet.class);
        _classMap.put(Namespace.GEOGAME_ELEMENT, GeogameConfig.class);
    }

    public <T extends GeogameObject> T newInstance(String type) {
        try {
            if (!_classMap.containsKey(type)) {
                throw new IllegalAccessError("no such mapping defined for " + type);
            }
            return (T) _classMap.get(type).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException("Instantiation failed (" + e.getMessage() + ")");
        }
    }
}
