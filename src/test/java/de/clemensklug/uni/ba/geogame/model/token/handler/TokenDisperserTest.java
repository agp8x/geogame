/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame.model.token.handler;

import de.clemensklug.uni.ba.geogame.model.Player;
import de.clemensklug.uni.ba.geogame.model.actions.Action;
import de.clemensklug.uni.ba.geogame.model.actions.TokenAction;
import de.clemensklug.uni.ba.geogame.model.spatial.Point;
import de.clemensklug.uni.ba.geogame.model.token.TokenSet;
import de.clemensklug.uni.ba.geogame.parser.OWLParser;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.LinkedList;
import java.util.List;

import static de.clemensklug.uni.ba.geogame.parser.Namespace.ACTION;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * @author clemens
 */
public class TokenDisperserTest {
    private final static String _path = "src/test/resources/TokenDispenser/tokendispenser-";

    @Test
    public void testLoad2Token() throws Exception {
        OWLParser op = new OWLParser(_path + "2token.owl");
        List<Action> actions = op.getInstances(ACTION);
        Player p = new Player("player");
        assertTrue(p.getInventory().isEmpty());

        actions.get(0).trigger(new Point(0, 0), p);
        assertEquals(1, p.getInventory().size());

        actions.get(0).trigger(new Point(0, 0), p);
        assertEquals(2, p.getInventory().size());

        actions.get(0).trigger(new Point(0, 0), p);
        assertEquals(2, p.getInventory().size());
    }

    @Test
    public void testLoadMax3Token() throws Exception {
        OWLParser op = new OWLParser(_path + "max3token.owl");
        List<Action> actions = op.getInstances(ACTION);
        Player p = new Player("player");
        assertTrue(p.getInventory().isEmpty());

        actions.get(0).trigger(new Point(0, 0), p);
        assertEquals(1, p.getInventory().size());

        actions.get(0).trigger(new Point(0, 0), p);
        assertEquals(2, p.getInventory().size());

        actions.get(0).trigger(new Point(0, 0), p);
        assertEquals(3, p.getInventory().size());

        actions.get(0).trigger(new Point(0, 0), p);
        assertEquals(3, p.getInventory().size());
    }

    @Test
    public void testLoad2TokenAtOnce() throws Exception {
        OWLParser op = new OWLParser(_path + "2token_at_once.owl");
        List<Action> actions = op.getInstances(ACTION);
        Player p = new Player("player");
        assertTrue(p.getInventory().isEmpty());

        actions.get(0).trigger(new Point(0, 0), p);
        assertEquals(2, p.getInventory().size());

        actions.get(0).trigger(new Point(0, 0), p);
        assertEquals(4, p.getInventory().size());

        actions.get(0).trigger(new Point(0, 0), p);
        assertEquals(6, p.getInventory().size());

        actions.get(0).trigger(new Point(0, 0), p);
        assertEquals(8, p.getInventory().size());

        actions.get(0).trigger(new Point(0, 0), p);
        assertEquals(8, p.getInventory().size());
    }

    @Test
    public void testLoad1MarkerToken() throws Exception {
        OWLParser op = new OWLParser(_path + "1marker.owl");
        List<Action> actions = op.getInstances(ACTION);
        Player p = new Player("player");
        assertTrue(p.getInventory().isEmpty());

        actions.get(0).trigger(new Point(0, 0), p);
        assertEquals(1, p.getInventory().size());

        actions.get(0).trigger(new Point(0, 0), p);
        assertEquals(1, p.getInventory().size());

        actions.get(0).trigger(new Point(0, 0), p);
        assertEquals(1, p.getInventory().size());

        actions.get(0).trigger(new Point(0, 0), p);
        assertEquals(1, p.getInventory().size());

        actions.get(0).trigger(new Point(0, 0), p);
        assertEquals(1, p.getInventory().size());
    }

    @Test
    public void testLoadGeoTTTToken() throws Exception {
        OWLParser op = new OWLParser("src/test/resources/TokenDispenser/geoTTT_dispenser.owl");
        List<Action> disps = op.getInstances(ACTION);
        assertEquals(1, disps.size());
        Action a = disps.get(0);
        List<Point> triggers = new LinkedList<>();
        triggers.add(new Point(0, 0));
        a.setTriggers(triggers);
        TokenDispenser d = (TokenDispenser) ((TokenAction) a).getHandler();
        assertFalse(d.getTokenRepository().isEmpty());
        assertEquals(2, d.getTokenRepository().size());
        Player p = new Player("foo");
        assertEquals(0, p.getInventorySize());
        a.trigger(new Point(0, 0), p);

        assertEquals(1, d.getTokenRepository().size());
        assertEquals(6, p.getInventorySize());
    }

    @Test
    public void testAddTokens() throws Exception {
        TokenDispenser disp = new TokenDispenser();
        List<TokenSet> tokens = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            TokenSet t = Mockito.mock(TokenSet.class);
            when(t.getName()).thenReturn(String.valueOf(i));
            when(t.toString()).thenReturn(String.valueOf(i));
            tokens.add(t);
        }
        assertTrue(disp.getTokenRepository().isEmpty());
        disp.addTokens(tokens);
        assertFalse(disp.getTokenRepository().isEmpty());
        assertEquals(tokens, disp.getTokenRepository());
    }

    @Test
    public void testAddRealTokens() throws Exception {
        TokenDispenser disp = new TokenDispenser();
        List<TokenSet> tokens = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            TokenSet t = new TokenSet();
            t.setName("token " + i);
            tokens.add(t);
        }
        assertTrue(disp.getTokenRepository().isEmpty());
        disp.addTokens(tokens);
        assertFalse(disp.getTokenRepository().isEmpty());
        assertEquals(tokens, disp.getTokenRepository());
    }
}