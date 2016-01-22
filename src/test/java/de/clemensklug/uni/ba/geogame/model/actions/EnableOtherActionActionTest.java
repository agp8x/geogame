package de.clemensklug.uni.ba.geogame.model.actions;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author clemens
 */
public class EnableOtherActionActionTest {
    private EnableOtherActionAction _action;
    private List<Action> _actions;

    @Test
    public void testAction() throws Exception {
        _action.action(null, null, null);
        for (Action a :_actions){
            verify(a).setEnabled(true);
        }
    }

    @Before
    public void setUp() throws Exception {
        _action = new EnableOtherActionAction();
        _actions=new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Action a = mock(Action.class);
            _actions.add(a);
        }
        _action.setEnableActions(_actions);
    }
}