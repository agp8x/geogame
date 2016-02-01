/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame.parser;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author clemens
 */
public class NamespaceTest {

    @Test
    public void testIsAction() throws Exception {
        assertTrue(Namespace.isAction(Namespace.ACTION));
        assertTrue(Namespace.isAction(Namespace.ECHO_ACTION));
        assertTrue(Namespace.isAction(Namespace.ENABLE_ACTION));
        assertTrue(Namespace.isAction(Namespace.TOKEN_ACTION));
        assertFalse(Namespace.isAction(Namespace.TOKEN));
        assertFalse(Namespace.isAction(Namespace.CONDITION));
        assertFalse(Namespace.isAction(Namespace.PROP_AT));
    }

    @Test
    public void testIsCondition() throws Exception {
        assertTrue(Namespace.isCondition(Namespace.CONDITION));
        assertTrue(Namespace.isCondition(Namespace.START_CONDITION));
        assertTrue(Namespace.isCondition(Namespace.WIN_CONDITION));
        assertTrue(Namespace.isCondition(Namespace.LOGIC_CONDITION));
        assertTrue(Namespace.isCondition(Namespace.TOKEN_CONDITION));
        assertFalse(Namespace.isCondition(Namespace.ACTION));
        assertFalse(Namespace.isCondition(Namespace.SYNCTIME));

    }
}