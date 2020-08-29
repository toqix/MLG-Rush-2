package dev.invasion.plugins.games.mlgrush.BuildMode;

import dev.invasion.plugins.games.mlgrush.MLGRush;
import dev.invasion.plugins.games.mlgrush.Utils.AnvilGUI.AnvilGUI;
import dev.invasion.plugins.games.mlgrush.Utils.MessageCreator;
import dev.invasion.plugins.games.mlgrush.maps.BoundingBox;
import dev.invasion.plugins.games.mlgrush.maps.MapManager;
import dev.invasion.plugins.games.mlgrush.maps.SerializableLocation;
import dev.invasion.plugins.games.mlgrush.maps.gameMap;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class BuildModeManager {

    public static void handleClick(String arguments, Player player) {
        switch (arguments) {
            case "create":
                createMap(player);
                break;
            case "edit":
                editMap(player);
                break;
        }
    }

    private static void createMap(Player player) {
        new AnvilGUI.Builder()
                .onComplete(((player1, s) -> {
                    if (player == player1) {
                        onComplete(player, s);
                    }
                    return AnvilGUI.Response.text("Creating Map");
                }))
                .title("Name your map")
                .text(player.getName() + "'s new Map")
                .plugin(MLGRush.getInstance())
                .open(player);

    }

    private static void onComplete(Player player, String text) {
        SerializableLocation location;
        if(MLGRush.getMapManager().getMaps().isEmpty()) {
            location = MLGRush.getLobbySpawn().getCopy();
        }else {
            location = MLGRush.getMapManager().getMap(MLGRush.getMapManager().getMaps().size()-1).getSpecSpawn().getCopy();
        }
        location.add(0,0,95);

        MLGRush.getMapManager().addMap(new gameMap(location, new BoundingBox(location, 50, 30, 20), text));
        player.sendMessage(MessageCreator.prefix("MLG-Rush-Build", "&7Map was created &asuccessfully"));
    }

    private static void editMap(Player player) {

    }

}
