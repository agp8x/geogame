package de.clemensklug.uni.ba.geogame.model.token;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by clemens on 02.12.15.
 *
 * @author clemens
 */
public class TokenSet extends Token {
    private Set<Token> _tokens = new HashSet<>();

    public TokenSet() {
    }

    public TokenSet(Token token) {
        _tokens.add(token);
    }

    public Set<Token> getTokens() {
        return _tokens;
    }

    public void setTokens(Collection<Token> tokens) {
        _tokens.addAll(tokens);
    }

    @Override
    public String toString() {
        return "TokenSet{" +
                "_tokens=" + _tokens +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TokenSet tokenSet = (TokenSet) o;

        return !(_tokens != null ? !_tokens.equals(tokenSet._tokens) : tokenSet._tokens != null);

    }

    @Override
    public boolean isValid() {
        return !_tokens.isEmpty();
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (_tokens != null ? _tokens.hashCode() : 0);
        return result;
    }
}
