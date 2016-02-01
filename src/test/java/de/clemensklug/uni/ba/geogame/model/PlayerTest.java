/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame.model;

import de.clemensklug.uni.ba.geogame.model.token.MarkerToken;
import de.clemensklug.uni.ba.geogame.model.token.Token;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * @author clemens
 */
public class PlayerTest {

    private Player _player;

    @Before
    public void setUp() throws Exception {
        _player = new Player();
    }

    @Test
    public void testAddToInventory() throws Exception {
        Token t = new Token();
        _player.addToInventory(t);
        assertEquals(t, _player.takeToken(Token.class));
    }

    @Test
    public void testAddSameTokenTwiceResultsInOneTokenInInventory() throws Exception {
        Token t = new Token();
        _player.addToInventory(t);
        _player.addToInventory(t);
        assertEquals(1, _player.getInventorySize());
        assertEquals(t, _player.takeToken(Token.class));
    }

    @Test
    public void testAddNullToInventory() throws Exception {
        _player.addToInventory(null);
        assertEquals(0, _player.getInventorySize());
    }

    @Test
    public void testTakeToken() throws Exception {
        Token t = new Token();
        assertFalse(t.isDuplicable());
        _player.addToInventory(t);
        assertEquals(t, _player.takeToken(Token.class));
        assertEquals(null, _player.takeToken(Token.class));
    }

    @Test
    public void testTakeDuplicableToken() throws Exception {
        Token t = new MarkerToken();
        assertTrue(t.isDuplicable());
        _player.addToInventory(t);
        assertEquals(t, _player.takeToken(MarkerToken.class));
        assertEquals(t, _player.takeToken(t.getClass()));
        assertEquals(t, _player.takeToken(MarkerToken.class));
    }

    @Test
    public void testHasTokenTypeInInventory() throws Exception {
        Token t = new Token();
        _player.addToInventory(t);
        assertTrue(_player.hasTokenTypeInInventory(Token.class));
        assertFalse(_player.hasTokenTypeInInventory(MarkerToken.class));
    }

    @Test
    public void testHasTokenTypeInInventory2() throws Exception {
        Token t = new MarkerToken();
        _player.addToInventory(t);
        assertTrue(_player.hasTokenTypeInInventory(MarkerToken.class));
        assertFalse(_player.hasTokenTypeInInventory(Token.class));
    }

    @Test
    public void testMoveToDisposableInventory() throws Exception {
        assertTrue(_player.getInventory().isEmpty());
        Token t = mock(Token.class);
        _player.addToInventory(t);
        assertEquals(1, _player.getInventorySize());
        assertTrue(_player.getDisposableInventory().isEmpty());
        _player.moveToDisposableInventory(_player.getInventory());
        assertTrue(_player.getInventory().isEmpty());
        assertEquals(1, _player.getDisposableInventory().size());
        assertSame(t, _player.popDisposableItem());
    }
}