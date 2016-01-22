package de.clemensklug.uni.ba.geogame.model.challenge;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

/**
 * Just block until sync time is over
 *
 * @author clemens
 */
public class SyncTimeChallenge implements Challenge {
    private final Logger log = LogManager.getLogger(this.getClass());
    private ChallengeData _data;
    private DateTime _target;
    private String _name;

    public SyncTimeChallenge() {
        _data = new ChallengeData();
    }

    @Override
    public Challenge clone() {
        final SyncTimeChallenge syncTimeChallenge = new SyncTimeChallenge();
        syncTimeChallenge.setData(_data.clone());
        return syncTimeChallenge;
    }

    public int getSyncTime() {
        return _data.getSyncTime();
    }

    public void setSyncTime(int syncTime) {
        _data.setSyncTime(syncTime);
    }

    @Override
    public boolean isFinished() {
        log.trace(_target);
        return _target != null && _target.isBeforeNow();
    }

    @Override
    public void startChallenge() {
        log.trace("start SyncTime (" + _data.getSyncTime() + " s)");
        _target = DateTime.now().plusSeconds(_data.getSyncTime());
    }

    @Override
    public ChallengeData getData() {
        return _data;
    }

    public void setData(ChallengeData data) {
        _data = data;
    }

    @Override
    public String toString() {
        return "SyncTimeChallenge{" +
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
