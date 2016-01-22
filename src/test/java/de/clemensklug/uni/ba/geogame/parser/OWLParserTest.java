package de.clemensklug.uni.ba.geogame.parser;

import de.clemensklug.uni.ba.geogame.model.GeogameConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author clemens
 */
public class OWLParserTest {
    private OWLParser _op;
    private final static String _path = "src/test/resources/OWLParser.owl";

    @Before
    public void setUp() throws Exception {
        _op = new OWLParser(_path);
    }

    @Test
    public void testGetActionsNotNull() throws Exception {
        assertNotNull(_op.getInstances(Namespace.ACTION));
    }

    @Test
    public void testGetActionsSizeIs2() throws Exception {
        assertEquals(2, _op.getInstances(Namespace.ACTION).size());
    }

    @Test
    public void testGetActionsContentsNotNull() throws Exception {
        _op.getInstances(Namespace.ACTION).forEach(Assert::assertNotNull);
    }

    @Test
    public void testGetActionsContentsAreActions() throws Exception {
        _op.getInstances(Namespace.ACTION).forEach(action -> assertTrue(action != null));
    }

    @Test
    public void testGetStartConditionsNotNull() throws Exception {
        assertNotNull(_op.getInstances(Namespace.START_CONDITION));
    }

    @Test
    public void testGetStartConditionsSizeIs1() throws Exception {
        assertEquals(1, _op.getInstances(Namespace.START_CONDITION).size());
    }

    @Test
    public void testGetStartConditionsContentsNotNull() throws Exception {
        _op.getInstances(Namespace.START_CONDITION).forEach(Assert::assertNotNull);
    }

    @Test
    public void testGetStartConditionsContentsAreConditions() throws Exception {
        _op.getInstances(Namespace.START_CONDITION).forEach(con -> assertTrue(con != null));
    }

    @Test
    public void testGetWinConditionsNotNull() throws Exception {
        assertNotNull(_op.getInstances(Namespace.WIN_CONDITION));
    }

    @Test
    public void testGetWinConditionsSizeIs1() throws Exception {
        assertEquals(1, _op.getInstances(Namespace.WIN_CONDITION).size());
    }

    @Test
    public void testGetWinConditionsContentsNotNull() throws Exception {
        _op.getInstances(Namespace.WIN_CONDITION).forEach(Assert::assertNotNull);
    }

    @Test
    public void testGetWinConditionsContentsAreConditions() throws Exception {
        _op.getInstances(Namespace.WIN_CONDITION).forEach(con -> assertTrue(con != null));
    }

    @Test
    public void testGetInstancesActionsNotNull() throws Exception {
        assertNotNull(_op.getInstances(Namespace.ACTION));
    }

    @Test
    public void testGetInstancesActions() throws Exception {
        assertEquals(2, _op.getInstances(Namespace.ACTION).size());
    }

    @Test
    public void testGetInstancesPointNotNull() throws Exception {
        assertNotNull(_op.getInstances(Namespace.POINT));
    }

    @Test
    public void testGetInstancesPoint() throws Exception {
        assertEquals(3, _op.getInstances(Namespace.POINT).size());
    }

    @Test
    public void testGetInstancesTokenHandlerNotNull() throws Exception {
        assertNotNull(_op.getInstances(Namespace.TOKEN_HANLDER));
    }

    @Test
    public void testGetInstancesTokenHandler() throws Exception {
        assertEquals(2, _op.getInstances(Namespace.TOKEN_HANLDER).size());
    }

    @Test
    public void testGetInstancesTokenNotNull() throws Exception {
        assertNotNull(_op.getInstances(Namespace.TOKEN));
    }

    @Test
    public void testGetInstancesToken() throws Exception {
        assertEquals(1, _op.getInstances(Namespace.TOKEN).size());
    }

    @Test
    public void testGetInstancesStartConditionNotNull() throws Exception {
        assertNotNull(_op.getInstances(Namespace.START_CONDITION));
    }

    @Test
    public void testGetInstancesStartCondition() throws Exception {
        assertEquals(1, _op.getInstances(Namespace.START_CONDITION).size());
    }

    @Test
    public void testGetInstancesWinConditionNotNull() throws Exception {
        assertNotNull(_op.getInstances(Namespace.WIN_CONDITION));
    }

    @Test
    public void testGetInstancesWinCondition() throws Exception {
        assertEquals(1, _op.getInstances(Namespace.WIN_CONDITION).size());
    }

    @Test
    public void testGetInstancesConditionNotNull() throws Exception {
        assertNotNull(_op.getInstances(Namespace.LOGIC_CONDITION));
    }

    @Test
    public void testGetInstancesCondition() throws Exception {
        assertEquals(5, _op.getInstances(Namespace.LOGIC_CONDITION).size());
    }

    @Test
    public void testGetGeogame() throws Exception {
        GeogameConfig g = _op.getGeogame("http://clemensklug.de/uni/ba/geogame/test/owlparser#GeoTTTInst");
        assertNotNull(g);
        assertNotNull(g.getDrawCondition());
        assertNotNull(g.getStartCondition());
        assertNotNull(g.getWinCondition());
        assertNotNull(g.getActions());
        assertTrue(g.getPlayerCount() > 0);
    }

    @Test
    public void testIsSubClass() throws Exception {
        assertTrue(_op.isSubClass(Namespace.ACTION, Namespace.ECHO_ACTION));
        assertFalse(_op.isSubClass(Namespace.GEOGAME_ELEMENT, Namespace.CONDITION));
    }
}