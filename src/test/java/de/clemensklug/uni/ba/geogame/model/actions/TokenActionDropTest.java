package de.clemensklug.uni.ba.geogame.model.actions;

import de.clemensklug.uni.ba.geogame.model.Player;
import de.clemensklug.uni.ba.geogame.model.token.handler.TokenHandler;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * @author clemens
 */
public class TokenActionDropTest {
    TokenAction _action;
    private TokenHandler _handler;

    @Before
    public void setUp() throws Exception {
        _action=new TokenAction();
        _handler = mock(TokenHandler.class);
        _action.setHandler(_handler);

    }

    @Test
    public void testAction() throws Exception {
        final Player player = mock(Player.class);
        _action.action(null,null, player);
        verify(_handler).drop(player);
    }
}