package de.clemensklug.uni.ba.geogame.model.challenge;

import de.clemensklug.uni.ba.geogame.model.actions.Action;
import de.clemensklug.uni.ba.geogame.parser.Namespace;
import de.clemensklug.uni.ba.geogame.parser.OWLParser;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author clemens
 */
public class ChallengeTest {
    private static final String _PATH = "src/test/resources/Challenges.owl";

    @Test
    public void testLoadChallenges() throws Exception {
        OWLParser op = new OWLParser(_PATH);
        List<Action> actions = op.getInstances(Namespace.ACTION);
        assertEquals(1, actions.get(0).getChallenges(null).size());
        assertEquals(2, actions.get(1).getChallenges(null).size());
        assertEquals(2, actions.get(2).getChallenges(null).size());
    }
}