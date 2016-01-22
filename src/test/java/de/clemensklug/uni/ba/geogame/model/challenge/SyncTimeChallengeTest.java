package de.clemensklug.uni.ba.geogame.model.challenge;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author clemens
 */
public class SyncTimeChallengeTest {
    private SyncTimeChallenge _stc;

    @Before
    public void setUp() throws Exception {
        _stc = new SyncTimeChallenge();
    }

    @After
    public void tearDown() throws Exception {
        _stc = null;
    }

    @Test
    public void testIsChallengeFinishedWithNullTarget() throws Exception {
        assertFalse(_stc.isFinished());
    }

    @Test
    public void testIsChallengeFinished() throws Exception {
        _stc.setSyncTime(2);
        _stc.startChallenge();
        assertFalse(_stc.isFinished());
        Thread.sleep(2100);
        assertTrue(_stc.isFinished());
    }

    @Test
    public void testIsChallengeFinishedNegativeTime() throws Exception {
        _stc.setSyncTime(-2);
        _stc.startChallenge();
        assertTrue(_stc.isFinished());
    }

    @Test
    public void testStartSyncTime() throws Exception {
        _stc.startChallenge();
        Thread.sleep(1);
        assertTrue(_stc.isFinished());
    }

}