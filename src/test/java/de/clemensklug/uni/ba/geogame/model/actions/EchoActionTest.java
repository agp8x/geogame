/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame.model.actions;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author clemens
 */
public class EchoActionTest {

    private EchoAction _action;

    @Before
    public void setUp() throws Exception {
        _action = new EchoAction();
        _action.setEnabled(true);
        _action.setActive(true);
    }
}