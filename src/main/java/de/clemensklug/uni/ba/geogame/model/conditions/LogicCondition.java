/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame.model.conditions;

import de.clemensklug.uni.ba.geogame.GameManager;
import de.clemensklug.uni.ba.geogame.model.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author clemens
 */
public class LogicCondition extends Condition {
    public final static LogicMode DEFAULT_LOGIC_MODE = LogicMode.AND;
    private List<Condition> _conditions;
    private LogicMode _logicMode = DEFAULT_LOGIC_MODE;

    public LogicCondition() {
    }

    public LogicCondition(List<Condition> conditions) {
        _conditions = conditions;
    }

    @Override
    public boolean isSatisfied() {
        return _conditions != null && Logic.perform(_conditions, _logicMode);
    }

    @Override
    public List<Player> getSatisfyingPlayers() {
        if (_conditions == null) {
            return Collections.emptyList();
        }
        return Logic.getSatisfyingPlayers(_conditions, GameManager.getPlayers(), _logicMode);
    }

    @Override
    public void runSatisfiedAction() {
        super.runSatisfiedAction();
        if (_conditions != null) {
            _conditions.forEach(Condition::runSatisfiedAction);
        }
    }

    public List<Condition> getConditions() {
        return _conditions;
    }

    public void setConditions(List<Condition> conditions) {
        _conditions = conditions;
    }

    public void setLogicMode(LogicMode mode) {
        _logicMode = mode;
    }

    @Override
    public String toString() {
        return super.toString() + "*subcons: " + _conditions + "; logic: " + _logicMode;
    }

    public enum LogicMode {
        AND, OR, NOR, T, F
    }

    static class Logic {
        static boolean perform(List<Condition> conditions, LogicMode mode) {
            if (isUnary(mode)) {
                return LogicMode.T == mode;
            }
            conditions.removeIf(condition -> condition == null);
            //begin with false if OR is used, otherwise use true
            boolean result = !(LogicMode.OR == mode);
            for (Condition c : conditions) {
                switch (mode) {
                    case AND:
                        result = c.isSatisfied() && result;
                        break;
                    case OR:
                        result = c.isSatisfied() || result;
                        break;
                    case NOR:
                        result = !c.isSatisfied() && result;
                        break;
                }
            }
            return result;
        }

        private static boolean isUnary(LogicMode mode) {
            return LogicMode.T == mode || LogicMode.F == mode;
        }

        static List<Player> getSatisfyingPlayers(List<Condition> conditions, List<Player> allPlayers, LogicMode mode) {
            final List<Player> players = new ArrayList<>();
            switch (mode) {
                case AND:
                case OR:
                case NOR:
                    conditions.stream().filter(Condition::isSatisfied).forEach(c -> players.addAll(c.getSatisfyingPlayers()));
                    break;
                case T:
                    players.addAll(allPlayers);
                    break;
                case F:
                    players.clear();
            }
            return players;
        }
    }
}
