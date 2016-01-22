package de.clemensklug.uni.ba.geogame.model.challenge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author clemens
 */
public class QuestionChallenge implements Challenge {
    private final Logger log = LogManager.getLogger(this.getClass());
    private ChallengeData _data;
    private String _name;

    public QuestionChallenge() {
        _data = new ChallengeData();
    }

    @Override
    public Challenge clone() {
        QuestionChallenge c = new QuestionChallenge();
        c.setData(_data.clone());
        return c;
    }

    @Override
    public boolean isFinished() {
        log.trace(_data.isCompleted());
        return _data.isCompleted();
    }

    @Override
    public void startChallenge() {
        _data.setCompleted(false);
    }

    @Override
    public ChallengeData getData() {
        return _data;
    }

    public void setData(ChallengeData data) {
        _data = data;
    }

    public String getQuestion() {
        return _data.getQuestion();
    }

    public void setQuestion(String question) {
        _data.setQuestion(question);
    }

    public void setIsAnswered(boolean isAnswered) {
        _data.setCompleted(isAnswered);
    }

    @Override
    public String toString() {
        return "QuestionChallenge{" +
                "data=" + _data +
                ", isFinished=" + isFinished() +
                '}';
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public void setName(String name) {
        _name = name;
    }
}
