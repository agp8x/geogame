package de.clemensklug.uni.ba.geogame.ui.net;

import de.clemensklug.uni.ba.geogame.GameManager;
import de.clemensklug.uni.ba.geogame.location.LocationProvider;
import de.clemensklug.uni.ba.geogame.location.LocationStore;
import de.clemensklug.uni.ba.geogame.model.Player;
import de.clemensklug.uni.ba.geogame.model.Point;
import de.clemensklug.uni.ba.geogame.model.actions.ActionStatus;
import de.clemensklug.uni.ba.geogame.parser.OWLParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author clemens
 */
public class Server {
    final Logger log = LogManager.getLogger(this.getClass());
    private GameManager _gameManager;
    private Map<Player, LocationProvider> _locators;
    private final List<Point> _points;

    public Server() {
        this("src/main/resources/geoTTT.owl");
        //this("src/test/resources/geoTTT.owl");
    }

    public Server(String configfile) {
        final OWLParser parser = new OWLParser();
        parser.importConfig(configfile);
        _gameManager = GameManager.getInstance(parser);
        _locators = new ConcurrentHashMap<>();
        _points = new ArrayList<>(parser.getInstances(Point.class));
    }

    private boolean addPlayer(Player p) {
        try {
            _gameManager.addPlayer(p);
            LocationStore l = new LocationStore();
            l.setPosition(p.getPosition());
            _locators.put(p, l);
            log.info("added player " + p);
            return true;
        } catch (IllegalAccessError e) {
            return false;
        }
    }

    private TransferObject updatePlayer(Player p) {
        if (!_locators.containsKey(p)) {
            addPlayer(p);
        } else {
            ((LocationStore) _locators.get(p)).setPosition(p.getPosition());
            log.info("updated Player " + p.getPosition());
        }
        if (_gameManager.isOver()) {
            log.info("GAME OVER");
            Player lastPlayer = _gameManager.getLastPlayer();
            if (_gameManager.isDraw()) {
                lastPlayer = null;
            }
            return new TransferObject(TransferObject.Status.GAME_OVER, lastPlayer, null, null);
        }
        if (GameManager.isGameRunning()) {
            log.info("update for " + p);
            ;
            final List<ActionStatus> update = _gameManager.update(_locators.get(p), p);
            log.info(_gameManager.getActions());
            return new TransferObject(TransferObject.Status.UPDATE, p, update, null);
        }
        if (_gameManager.isStartable()) {
            log.info("game startable");
            if (_gameManager.startGame()) {
                log.info("game started");
                return new TransferObject(TransferObject.Status.GAME_STARTED, p, null, null);
            } else {
                log.fatal("start failed");
                return new TransferObject(TransferObject.Status.START_FAILED, p, null, null);
            }
        } else {
            log.fatal("not startable");
            return new TransferObject(TransferObject.Status.GAME_NOT_STARTED, p, null, null);
        }
    }

    private TransferObject update(TransferObject obj) {
        if (obj.getStatus() == TransferObject.Status.ADD_PLAYER) {
            if(addPlayer(obj.getPlayer())){
            //updatePlayer(obj.getPlayer());
            return obj;}return null;
        }
        if (obj.getStatus() == TransferObject.Status.QUERY_POINT_INIT) {
            Point p = _points.get(0);
            return new TransferObject(TransferObject.Status.QUERY_POINTS, null, null, p);
        }
        if (obj.getStatus() == TransferObject.Status.QUERY_POINTS) {
            final int index = _points.indexOf(obj.getPoint()) + 1;
            Point p = (index < _points.size()) ? _points.get(index) : null;
            return new TransferObject(TransferObject.Status.QUERY_POINTS, null, null, p);
        }
        Player p = obj.getPlayer();
        p.setPosition(obj.getPoint());
        log.info(p.getPosition());
        return updatePlayer(p);
    }

    public void serve() {
        Socket client;
        log.info("start serving");
        try (ServerSocket serverSocket = new ServerSocket(1337);) {
            while (true) {
                log.info("waiting...");
                client = serverSocket.accept();
                handleConnection(client);
            }
        } catch (IOException | ClassNotFoundException e) {
            log.fatal(e + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleConnection(Socket client) throws IOException, ClassNotFoundException {

        final ObjectInputStream stream = new ObjectInputStream(client.getInputStream());
        final ObjectOutputStream outStream = new ObjectOutputStream(client.getOutputStream());
        Runnable r = () -> {
            try {
                log.info("recv");
                while (true) {
                    log.info("read...");
                    TransferObject obj = (TransferObject) stream.readObject();
                    log.info("recieved: " + obj + " (" + obj.getStatus() + ")");
                    final TransferObject update = update(obj);
                    log.info(update);
                    outStream.writeObject(update);
                    log.info("done");
                    if (obj==null || obj.getStatus() == TransferObject.Status.GAME_OVER) {
                        stream.close();
                        outStream.close();
                        client.close();
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                log.error(e);
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    public static void main(String[] args) {
        Server s = new Server();
        s.serve();
    }
}
