package de.clemensklug.uni.ba.geogame.model.conditions;

import de.clemensklug.uni.ba.geogame.GameManager;
import de.clemensklug.uni.ba.geogame.model.GeogameConfig;
import de.clemensklug.uni.ba.geogame.model.Player;
import de.clemensklug.uni.ba.geogame.model.token.TokenSet;
import de.clemensklug.uni.ba.geogame.model.token.handler.TokenHandler;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author clemens
 */
public class TokenConditionTest {
    private ArrayList<Player> _players;
    private TokenCondition _tc;
    private HashMap<Player, TokenSet> _map;
    private List<TokenHandler> _ths;

    @Before
    public void setUp() throws Exception {
        _tc = new TokenCondition();
        _players = new ArrayList<>();
        _players.add(new Player());

        GameManager gm = GameManager.getInstance(null,null);
        GeogameConfig _game = mock(GeogameConfig.class);
        when(_game.getActions()).thenReturn(Collections.emptyList());
        when(_game.getDrawCondition()).thenReturn(new LogicCondition());
        when(_game.getStartCondition()).thenReturn(new LogicCondition());
        when(_game.getWinCondition()).thenReturn(new LogicCondition());
        when(_game.getPlayerCount()).thenReturn(1);
        gm.setGame(_game);
        gm.addAllPlayers(_players);
        TokenHandler th = mock(TokenHandler.class);
        _ths = new ArrayList<>();
        _ths.add(th);
        _map = new HashMap<>();
        when(th.getTokens()).thenReturn(_map);
    }

    @Test(expected = NullPointerException.class)
    public void testIsSatisfiedNull() throws Exception {
        _tc.isSatisfied();
    }

    @Test
    public void testIsSatisfied() throws Exception {
        _tc.setTokenhandlers(_ths);
        assertFalse(_tc.isSatisfied());
        _map.put(_players.get(0), new TokenSet(_players.get(0).getIdentifier()));
        assertTrue(_tc.isSatisfied());
    }

    @Test
    public void testGetSatisfyingPlayers() throws Exception {
        _tc.setTokenhandlers(_ths);
        assertEquals(new ArrayList<>(), _tc.getSatisfyingPlayers());
        _map.put(_players.get(0), new TokenSet(_players.get(0).getIdentifier()));
        assertEquals(_players, _tc.getSatisfyingPlayers());
        _players.add(new Player("2"));
        assertEquals(_players.subList(0, 1), _tc.getSatisfyingPlayers());

    }
}