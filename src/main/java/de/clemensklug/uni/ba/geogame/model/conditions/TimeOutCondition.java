/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame.model.conditions;

import de.clemensklug.uni.ba.geogame.GameManager;
import de.clemensklug.uni.ba.geogame.model.Player;
import org.joda.time.DateTime;

import java.util.Collections;
import java.util.List;

/**
 * A TimeOutCondition is initially satisfied, and a timer starts when the satisfied Action is called.
 * Then the Condition is no longer satisfied until the timer has counted down.
 * Additional calls to runSatisfiedAction() will not reset the timer.
 *
 * @author clemens
 */
public class TimeOutCondition extends Condition {
    private int _maxDuration;
    private DateTime _finish;

    public void setMaxDuration(int duration) {
        _maxDuration = duration;
    }

    @Override
    public boolean isSatisfied() {
        return _finish == null || _finish.isBeforeNow();
    }

    @Override
    public List<Player> getSatisfyingPlayers() {
        if (isSatisfied()) {
            return GameManager.getPlayers();
        }
        return Collections.emptyList();
    }

    @Override
    public void runSatisfiedAction() {
        if (_finish == null && _maxDuration != 0) {
            _finish = DateTime.now().plusSeconds(_maxDuration);
        }
        super.runSatisfiedAction();
    }
}
