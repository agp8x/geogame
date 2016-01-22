package de.clemensklug.uni.ba.geogame.model.token.handler;

import de.clemensklug.uni.ba.geogame.model.Player;
import de.clemensklug.uni.ba.geogame.model.actions.Action;
import de.clemensklug.uni.ba.geogame.model.actions.TokenAction;
import de.clemensklug.uni.ba.geogame.model.spatial.Point;
import de.clemensklug.uni.ba.geogame.parser.OWLParser;
import org.junit.Test;

import java.util.List;

import static de.clemensklug.uni.ba.geogame.parser.Namespace.ACTION;
import static org.junit.Assert.assertEquals;

/**
 * @author clemens
 */
public class TokenCounterTest {
    private final static String _path = "src/test/resources/TokenCounter.owl";

    @Test(expected = NullPointerException.class)
    public void testEmpty() {
        OWLParser op = new OWLParser(_path);
        List<Action> actions = op.getInstances(ACTION);
        Player p = new Player("player");
        TokenCounter tc = ((TokenCounter) ((TokenAction) actions.get(0)).getHandler());

        tc.getCount(p);
    }

    @Test
    public void testCount() throws Exception {
        OWLParser op = new OWLParser(_path);
        List<Action> actions = op.getInstances(ACTION);
        Player p = new Player("player");
        TokenCounter tc = ((TokenCounter) ((TokenAction) actions.get(0)).getHandler());

        actions.get(0).trigger(new Point(0, 0), p);
        assertEquals(1, tc.getCount(p));

        actions.get(0).trigger(new Point(0, 0), p);
        assertEquals(2, tc.getCount(p));

        actions.get(0).trigger(new Point(0, 0), p);
        assertEquals(3, tc.getCount(p));
    }

    @Test
    public void testCount2Players() throws Exception {
        OWLParser op = new OWLParser(_path);
        List<Action> actions = op.getInstances(ACTION);
        Player p = new Player("player");
        Player p2 = new Player("asdf");
        TokenCounter tc = ((TokenCounter) ((TokenAction) actions.get(0)).getHandler());

        actions.get(0).trigger(new Point(0, 0), p);
        assertEquals(1, tc.getCount(p));

        actions.get(0).trigger(new Point(0, 0), p);
        assertEquals(2, tc.getCount(p));

        actions.get(0).trigger(new Point(0, 0), p2);
        assertEquals(2, tc.getCount(p));
        assertEquals(1, tc.getCount(p2));
    }

}