package de.clemensklug.uni.ba.geogame.location;

import de.clemensklug.uni.ba.geogame.model.Player;
import de.clemensklug.uni.ba.geogame.model.spatial.Point;
import de.clemensklug.uni.ba.geogame.ui.cli.CLI;

/**
 * @author clemens
 */
public class InteractiveLocationProvider implements LocationProvider {
    @Override
    public Point getPosition(Player player) {
        String input = CLI.getAnswer(player.getName() + ", enter your location (lat, long):");
        int comma = input.indexOf(",");
        if (comma == -1) {
            System.err.println("not a valid input");
            return new Point(0, 0, 0, input);
        }
        double lat = Double.parseDouble(input.substring(0, comma));
        double lon = Double.parseDouble(input.substring(comma + 1));
        return new Point(lat, lon, 0, input);
    }

    @Override
    public String description() {
        return "Interactive locations";
    }
}
