/*
 *  See the file "LICENSE" for the full license governing this code.
 */

package de.clemensklug.uni.ba.geogame.ui.cli;

import de.clemensklug.uni.ba.geogame.GameManager;
import de.clemensklug.uni.ba.geogame.location.InteractiveLocationProvider;
import de.clemensklug.uni.ba.geogame.location.LocationProvider;
import de.clemensklug.uni.ba.geogame.model.Player;
import de.clemensklug.uni.ba.geogame.model.actions.Action;
import de.clemensklug.uni.ba.geogame.model.actions.ActionStatus;
import de.clemensklug.uni.ba.geogame.model.actions.TokenAction;
import de.clemensklug.uni.ba.geogame.model.challenge.Challenge;
import de.clemensklug.uni.ba.geogame.model.challenge.QuestionChallenge;
import de.clemensklug.uni.ba.geogame.model.challenge.SyncTimeChallenge;
import de.clemensklug.uni.ba.geogame.model.conditions.PlayerLocationCondition;
import de.clemensklug.uni.ba.geogame.model.spatial.Point;
import de.clemensklug.uni.ba.geogame.model.token.Token;
import de.clemensklug.uni.ba.geogame.parser.ConfigParser;
import de.clemensklug.uni.ba.geogame.parser.OWLParser;
import de.clemensklug.uni.ba.geogame.parser.validation.GeoTTTValidator;
import de.clemensklug.uni.ba.geogame.parser.validation.PropertyValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static de.clemensklug.uni.ba.geogame.GameManager.getInstance;
import static de.clemensklug.uni.ba.geogame.GameManager.getPlayers;

/**
 * @author clemens
 */
public class CLI {
    private final static Scanner _scanner = new Scanner(System.in);
    private final static Logger log = LogManager.getLogger(CLI.class);
    private final LocationProvider _locationProvider;
    private final GameManager _gameManager;
    private final String _game = "http://clemensklug.de/uni/ba/geogame/geoTTT#GeoTTTgame";

    public CLI() {
        _locationProvider = new InteractiveLocationProvider();
        ConfigParser cp = new OWLParser("src/main/resources/geoTTT.owl");
        PropertyValidator val = new PropertyValidator(cp);
        log.info("validating gamefield...");
        if (!val.validateRestrictions().isEmpty()) {
            log.error("gamefield has errors...");
        } else {
            log.info("validation: OK");
            GeoTTTValidator val2 = new GeoTTTValidator();
            if (!val2.checkAllRCC(cp, _game).isEmpty()) {
                log.error("gamefield has spatial errors");
            } else {
                log.info("spatial check: OK");
                log.info("commencing game...");
            }
        }
        _gameManager = getInstance(cp, _game);
        Map<String, Player> players = new HashMap<>();
        for (int i = 0; i < 2; i++) {
            String p = getAnswer("Name for player " + (i + 1) + ":");
            Player player = new Player(p);
            players.put(p, player);
            _gameManager.addPlayer(player);
            log.info("Player " + player + " stands at " + player.getPosition());
        }
        while (!_gameManager.isStartable()) {
            Point start = _gameManager.getGame().getStartCondition().getConditions().stream()
                    .filter(condition -> (condition instanceof PlayerLocationCondition)).findFirst().get()
                    .getSatisfiedAction().getTriggers().stream().findFirst().get();
            log.info("to start game, both players need to be present at " + start);
            String player = "";
            while (!players.containsKey(player)) {
                player = getAnswer("Choose player to make move:");
            }
            Player player1 = players.get(player);
            player1.setPosition(_locationProvider.getPosition(player1));
        }
        if (!_gameManager.startGame()) {
            log.error("STARTING THE GAME FAILED!");
        }

        printInventories(getPlayers());
        log.info("moving all players inventory to disposable inventory...");
        getPlayers().forEach(player -> player.moveToDisposableInventory(player.getInventory()));
        printInventories(getPlayers());
        printPoints(_gameManager.getGame().getPoints());
        for (int i = 0; i < 9; i++) {
            String player = "";
            while (!players.containsKey(player)) {
                player = getAnswer("Choose player to make move:");
            }
            update(players.get(player));
            printGame();
            if (_gameManager.isWon() || _gameManager.isDraw()) {
                break;
            }
        }
        if (_gameManager.isWon()) {
            log.info("there are " + _gameManager.getWinningPlayers().size() + " winners: " + _gameManager.getWinningPlayers());
            log.info("the game was won by: " + _gameManager.getWinningPlayers().get(0).getName());
        } else {
            log.info("GAME OVER, no one won...");
        }
    }

    public static String getAnswer(String question) {
        log.info(question + "\n>");
        return _scanner.nextLine();
    }

    private void printInventories(List<Player> players) {
        StringBuilder sb = new StringBuilder();
        for (Player p : players) {
            sb.append("\t").append(p.getName()).append(":\n\t\tInventory:\n");
            if (p.getInventory().isEmpty()) {
                sb.append("\t\t\t---\n");
            } else {
                for (Token t : p.getInventory()) {
                    sb.append("\t\t\t* ").append(t.getName()).append(" (").append(t.getValue()).append(")\n");
                }
            }
            sb.append("\t\tDisposable inventory:\n");
            if (p.getDisposableInventory().isEmpty()) {
                sb.append("\t\t\t---\n");
            } else {
                for (Token t : p.getDisposableInventory()) {
                    sb.append("\t\t\t* ").append(t.getName()).append(" (").append(t.getValue()).append(")\n");
                }
            }
        }
        log.info(sb.toString());
    }

    private void printPoints(List<Point> points) {
        StringBuilder sb = new StringBuilder();
        sb.append("points:\n");
        for (Point p : points) {
            sb
                    .append("\t* ")
                    .append(p.getLatitude())
                    .append(", ")
                    .append(p.getLongitude())
                    .append("\n");
        }
        log.info(sb.toString());
    }

    private void printGame() {
        StringBuilder result = new StringBuilder();
        result.append("gamestatus:\n");
        for (Action a : _gameManager.getActions()) {
            result.append("\t * ");
            String extra = "";
            if (a instanceof TokenAction) {
                extra = "tokens: " + ((TokenAction) a).getHandler().getTokens().toString();
            }
            result.append(a.getName())
                    .append(" ")
                    .append(a.isEnabled() ? "(enabled)" : "(done)").append(" ")
                    .append(extra)
                    .append("\n");
        }
        result.append("\t-------\n");
        for (Player p : GameManager.getPlayers()) {
            result.append("\t * " + p.getName() + "@ " + p.getPosition() + ": " + p.getInventory() + "\n");
        }
        log.info(result.toString());
    }

    private void update(Player player) {
        log.info(player.getName() + ", its your turn!");
        List<ActionStatus> stati = _gameManager.update(_locationProvider, player);
        if (stati.size() == 0) {
            log.info("=> no action triggered");
            return;
        }
        for (ActionStatus status : stati) {
            if (status.getReference().isActive()) {
                log.info("=> action still alive:" + status);
                for (Challenge c : status.getChallenges()) {
                    if (c instanceof SyncTimeChallenge) {
                        final int syncTime = ((SyncTimeChallenge) c).getSyncTime();
                        log.info("=> waiting for synctime (" + syncTime + ")");
                        try {
                            Thread.sleep(syncTime * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        boolean done = c.isFinished();
                        if (done) {
                            log.info("=> synctime passed");
                        } else {
                            log.info("=> something prevented the synctime to pass");
                        }
                    }
                    if (c instanceof QuestionChallenge) {
                        //TODO: actually answer question ;)
                        String answer = getAnswer("question: " + ((QuestionChallenge) c).getQuestion());
                        ((QuestionChallenge) c).setIsAnswered(!answer.isEmpty());
                    }
                    log.info("=> attempted to solve challenges, resubmit event...");
                    status = status.getReference().trigger(status);
                    log.info("=> next status: " + status);
                    if (!c.isFinished()) {
                        log.info("WAITING FOR FINISH...");
                        //status.getChallenge().waitForFinish();
                    }
                }
            }
        }
        log.info("=> " + stati.size() + " actions were triggered, " + activeActions(stati) + " of which are still active");
    }

    private int activeActions(List<ActionStatus> statuses) {
        int count = 0;
        for (ActionStatus a : statuses) {
            if (a.getReference().isActive()) {
                count++;
            }
        }
        return count;
    }
}
