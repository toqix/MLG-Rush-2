package dev.invasion.plugins.games.mlgrush.BuildMode;

import dev.invasion.plugins.games.mlgrush.MLGRush;
import dev.invasion.plugins.games.mlgrush.maps.BoundingBoxActions;
import dev.invasion.plugins.games.mlgrush.maps.MapManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class BuildMode {

    private ArrayList<Player> builders = new ArrayList<>();
    private int chosenMap;
    private MapManager mapManager;
    public BuildMode(Player player, int chosenMap) {
        builders.add(player);
        mapManager = MLGRush.getMapManager();
        this.chosenMap = chosenMap;

        BoundingBoxActions.replace(Material.SANDSTONE, mapManager.getMap(chosenMap).getBoundingBox());
    }

    private void runBuildMode() {
        new BukkitRunnable() {

            @Override
            public void run() {

            }
        }.runTaskTimer(MLGRush.getInstance(), 0, 20);
    }

    public ArrayList<Player> getBuilders() {
        return builders;
    }


}
