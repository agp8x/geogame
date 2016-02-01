/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame.model.conditions;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author clemens
 */
public class TimeOutConditionTest {
    private TimeOutCondition _to;
    @Before
    public void setUp() throws Exception {
        _to=new TimeOutCondition();
        _to.setMaxDuration(1);
    }

    @Test
    public void testIsSatisfied() throws Exception {
        //isSatisfied shall be repeatably true at start
        assertTrue(_to.isSatisfied());
        assertTrue(_to.isSatisfied());
    }

    @Test
    public void testRunSatisfiedAction() throws Exception {
        assertTrue(_to.isSatisfied());
        _to.runSatisfiedAction();
        assertFalse(_to.isSatisfied());
        Thread.sleep(1001);
        assertTrue(_to.isSatisfied());
    }
}