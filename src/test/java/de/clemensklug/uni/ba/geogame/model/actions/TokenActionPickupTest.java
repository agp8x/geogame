/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame.model.actions;

import de.clemensklug.uni.ba.geogame.model.Player;
import de.clemensklug.uni.ba.geogame.model.token.TokenSet;
import de.clemensklug.uni.ba.geogame.model.token.handler.TokenDispenser;
import de.clemensklug.uni.ba.geogame.model.token.handler.TokenHandler;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * @author clemens
 */
public class TokenActionPickupTest {
    TokenAction _action;
    TokenHandler _handler;
    private Player _player;
    private TokenSet _mockedCollection;

    @Before
    public void setUp() throws Exception {
        _action = new TokenAction();
        _handler = mock(TokenDispenser.class);
        _action.setHandler(_handler);
        _player = mock(Player.class);
        _mockedCollection = mock(TokenSet.class);
        when(_handler.pickup(_player)).thenReturn(_mockedCollection);
    }

    @Test
    public void testAction() throws Exception {
        _action.action(null, null, _player);
        verify(_handler).pickup(_player);
    }

    @Test
    public void testSetHandler() throws Exception {
        _action.action(null, null, _player);
        verify(_player).addAllToInventory(_mockedCollection.getTokens());

    }
}