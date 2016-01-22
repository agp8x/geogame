package de.clemensklug.uni.ba.geogame.model;

import de.clemensklug.uni.ba.geogame.model.spatial.Point;
import de.clemensklug.uni.ba.geogame.model.token.InvalidToken;
import de.clemensklug.uni.ba.geogame.model.token.Token;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author clemens
 */
public class Player implements Serializable {
    private final Token _identifier;
    private final Set<Token> _inventory;
    private final Set<Token> _disposableInventory;
    private String _name;
    private Point _position;

    public Player() {
        _identifier = new Token();
        _inventory = new HashSet<>();
        _position = new Point();
        _disposableInventory = new HashSet<>();
    }

    public Player(String name) {
        this();
        _name = name;
        _identifier.setName("PLAYER:" + name);
    }

    public Player(String name, Point position) {
        this(name);
        _position = position;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public Token getIdentifier() {
        return _identifier;
    }

    public Token takeToken(Class type) {
        Token token = null;
        for (Token t : _inventory) {
            if (t.getClass().equals(type)) {
                token = t;
            }
        }
        final Token t1 = token;
        _inventory.removeIf(token1 -> token1.equals(t1) && !t1.isDuplicable());
        return token;
    }

    public void addToInventory(Token t) {
        if (null != t) {
            _inventory.add(t);
        }
    }

    public void addAllToInventory(Set<Token> tokens) {
        _inventory.addAll(tokens);
    }

    public boolean hasTokenTypeInInventory(Class type) {
        for (Token t : _inventory) {
            if (t.getClass().equals(type)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Player{" +
                "_name='" + _name + '\'' +
                '}';
    }

    public String toLongString() {
        return "Player{" +
                "_disposableInventory=" + _disposableInventory +
                ", _name='" + _name + '\'' +
                ", _identifier=" + _identifier +
                ", _inventory=" + _inventory +
                ", _position=" + _position +
                '}';
    }

    public int getInventorySize() {
        return _inventory.size();
    }

    public Set<Token> getInventory() {
        return _inventory;
    }

    public Point getPosition() {
        return _position;
    }

    public void setPosition(Point position) {
        _position = position;
    }

    public Set<Token> getDisposableInventory() {
        return _disposableInventory;
    }

    public Token popDisposableItem() {
        if (_disposableInventory.isEmpty()) {
            return new InvalidToken();
        }
        Token token = _disposableInventory.iterator().next();
        _disposableInventory.remove(token);
        return token;
    }

    /**
     * move Tokens from Inventory to Disposable Inventory
     *
     * @param tokens Set of Token to move
     * @return all items of parameter are removed from inventory and added to disposable
     */
    public boolean moveToDisposableInventory(Set<Token> tokens) {
        Set<Token> set = new HashSet<>(tokens);
        return set.stream()
                .map(token -> _inventory.remove(token) && _disposableInventory.add(token))
                .reduce(Boolean.TRUE, Boolean::logicalAnd);
    }
}
