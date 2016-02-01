/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame.model.challenge;

import de.clemensklug.uni.ba.geogame.model.GeogameObject;

/**
 * @author clemens
 */
public class ChallengeData implements GeogameObject {
    private String _name;
    private int _syncTime;
    private String _question;
    private boolean _completed;

    public boolean isCompleted() {
        return _completed;
    }

    public void setCompleted(boolean completed) {
        _completed = completed;
    }

    public int getSyncTime() {
        return _syncTime;
    }

    public void setSyncTime(int syncTime) {
        _syncTime = syncTime;
    }

    public String getQuestion() {
        return _question;
    }

    public void setQuestion(String question) {
        _question = question;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public void setName(String name) {
        _name = name;
    }

    public ChallengeData clone() {
        ChallengeData n = new ChallengeData();
        n.setCompleted(_completed);
        n.setQuestion(_question);
        n.setSyncTime(_syncTime);
        n.setName(_name);
        return n;
    }

    @Override
    public String toString() {
        return "ChallengeData{" +
                "_name='" + _name + '\'' +
                ", _syncTime=" + _syncTime +
                ", _question='" + _question + '\'' +
                ", _completed=" + _completed +
                '}';
    }
}
