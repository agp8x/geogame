package de.clemensklug.uni.ba.geogame.ui.net;

import de.clemensklug.uni.ba.geogame.location.GridLocationProvider;
import de.clemensklug.uni.ba.geogame.model.Player;
import de.clemensklug.uni.ba.geogame.model.Point;
import de.clemensklug.uni.ba.geogame.model.actions.ActionStatus;
import de.clemensklug.uni.ba.geogame.model.challenge.Challenge;
import de.clemensklug.uni.ba.geogame.model.challenge.ChallengeData;
import de.clemensklug.uni.ba.geogame.model.challenge.QuestionChallenge;
import de.clemensklug.uni.ba.geogame.model.challenge.SyncTimeChallenge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author clemens
 */
public class Client {
    private final static Logger log = LogManager.getLogger(Client.class);

    private void gameStep(List<ActionStatus> statuses) {
        for (ActionStatus s : statuses) {
            if (s.getReference().isActive()) {
                for (Challenge c : s.getChallenges()) {
                    if (c instanceof SyncTimeChallenge) {
                        final int syncTime = ((SyncTimeChallenge) c).getSyncTime();
                        try {
                            Thread.sleep(syncTime * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (c instanceof QuestionChallenge) {
                        ((QuestionChallenge) c).setIsAnswered(true);
                    }
                    ActionStatus newStatus = s.getReference().trigger(s);
                }
            }
        }
    }

    public static void main(String[] args) {
        play("Client " + Math.random());
        play("Client " + Math.random());

    }

    private static void play(String name) {
        Runnable r = () -> {
            Player p = new Player("Client " + Math.random());
            log.info("Player: " + p.getName());
            log.error("connect...");
            try (Socket socket = new Socket("localhost", 1337);) {
                log.info("connected!");
                ObjectOutputStream stream = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
                log.info("write");
                stream.writeObject(new TransferObject(TransferObject.Status.QUERY_POINT_INIT, null, null, null));
                List<Point> points = new ArrayList<>();
                while (true) {
                    TransferObject t = (TransferObject) inStream.readObject();
                    if (t.getPoint() == null) {
                        break;
                    }
                    points.add(t.getPoint());
                    stream.writeObject(new TransferObject(TransferObject.Status.QUERY_POINTS, null, null, t.getPoint()));
                }
                log.info(points);
                stream.writeObject(new TransferObject(TransferObject.Status.ADD_PLAYER, p, null, p.getPosition()));

                log.info("read");
                Object o = inStream.readObject();
                if (o instanceof TransferObject) {
                    TransferObject t = (TransferObject) o;
                    if (t.getStatus() == TransferObject.Status.ADD_PLAYER) {
                        log.info("player registered");
                    }
                }
                final double v = 2000 * Math.random();
                log.fatal("WAIT FOR " + v);
                Thread.sleep((long) v);
                // Thread.sleep(500);
                log.info("update player");
                stream.writeObject(new TransferObject(TransferObject.Status.UPDATE_PLAYER, p, null, p.getPosition()));
                log.info("await response");
                TransferObject t = (TransferObject) inStream.readObject();
                log.info("status: " + t.getStatus());
                log.info(t);
                GridLocationProvider loc = new GridLocationProvider();
                int shuffle = (int) ((10 * Math.random()) % 10);
                log.fatal("SHUFFLE: " + shuffle);
                for (int i = 0; i < shuffle; i++) {
                    loc.getPosition(null);
                }int i=0;
                for (i=0; i < 30; i++) {
                    Thread.sleep((long) (2000 * Math.random()));
                    p.setPosition(loc.getPosition(null));
                    log.info("LOC: " + p.getPosition());
                    for (int j = 0; j < 2; j++) {
                        final TransferObject obj = new TransferObject(TransferObject.Status.UPDATE_PLAYER, p, null, p.getPosition());
                        log.info("LOC2: " + p.getPosition() + "--" + obj.getPlayer().getPosition());
                        stream.writeObject(obj);
                        log.info("await response");
                        t = (TransferObject) inStream.readObject();
                        log.info(t);
                        if (t.getStatus() != TransferObject.Status.UPDATE) {
                            break;
                        }
                        int syncTime = 0;
                        for (ActionNetStatus s : t.getStatuses()) {
                            for (ChallengeData c : s.getChallenges()) {
                                if (c.getSyncTime() > syncTime) {
                                    syncTime = c.getSyncTime();
                                }
                            }
                        }
                        log.info("wait for sync");
                        Thread.sleep(syncTime * 1000 + 10);
                    }
                    if (t.getStatus() == TransferObject.Status.GAME_OVER) {
                        break;
                    }
                }
                log.info("GAME OVER after "+i+" rounds: " + t);
                stream.close();
                inStream.close();
                socket.close();
                if (t.getPlayer() == null) {
                    log.info("The game was a draw");
                } else if (p.getName().equals(t.getPlayer().getName())) {
                    log.info("You (" + p.getName() + ") have won!");
                } else {
                    log.info("You have lost, player " + t.getPlayer().getName() + " has won!");
                }
            } catch (IOException e) {
                log.error(e.getMessage() + "; " + Arrays.asList(e.getStackTrace()).toString());
            } catch (ClassNotFoundException e) {
                log.error(e.getMessage());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        Thread t = new Thread(r);
        t.start();
    }
}
