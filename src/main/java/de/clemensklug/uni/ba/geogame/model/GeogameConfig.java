/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame.model;

import de.clemensklug.uni.ba.geogame.model.actions.Action;
import de.clemensklug.uni.ba.geogame.model.conditions.LogicCondition;
import de.clemensklug.uni.ba.geogame.model.spatial.Point;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Contains all relevant information to configure a Geogame
 *
 * @author clemens
 */
public class GeogameConfig implements GeogameObject {
    private String _name;
    private int _playerCount;
    private LogicCondition _drawCondition;
    private LogicCondition _startCondition;
    private LogicCondition _winCondition;
    private List<Action> _actions;
    private List<Player> _players;

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public void setName(String name) {
        _name = name;
    }

    public LogicCondition getDrawCondition() {
        return _drawCondition;
    }

    public void setDrawCondition(LogicCondition drawCondition) {
        _drawCondition = drawCondition;
    }

    public int getPlayerCount() {
        return _playerCount;
    }

    public void setPlayerCount(int playerCount) {
        _playerCount = playerCount;
    }

    public LogicCondition getStartCondition() {
        return _startCondition;
    }

    public void setStartCondition(LogicCondition startCondition) {
        _startCondition = startCondition;
    }

    public LogicCondition getWinCondition() {
        return _winCondition;
    }

    public void setWinCondition(LogicCondition winCondition) {
        _winCondition = winCondition;
    }

    public List<Action> getActions() {
        return _actions;
    }

    public void setActions(List<Action> actions) {
        _actions = actions;
    }

    public List<Player> getPlayers() {
        return _players;
    }

    public void setPlayers(List<Player> players) {
        _players = players;
    }

    /**
     * shortcut to get all Points utilized as Trigger of an Action in this game
     *
     * @return List of Points
     */
    public List<Point> getPoints() {
        return getActions().parallelStream().flatMap(this::getTriggers).collect(Collectors.toList());
    }

    private Stream<Point> getTriggers(Action action) {
        return action.getTriggers().stream();
    }
}
