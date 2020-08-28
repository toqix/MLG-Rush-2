package dev.invasion.plugins.games.mlgrush.Listener;

import dev.invasion.plugins.games.mlgrush.MLGRush;
import dev.invasion.plugins.games.mlgrush.maps.BoundingBoxActions;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.Arrays;

public class joinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        MLGRush.setWorld(event.getPlayer().getWorld());
        event.setJoinMessage("Minequafter " + event.getPlayer().getName() + " sagt Grüßgott");
        BoundingBoxActions.replace(Arrays.asList(Material.STONE), MLGRush.getMapManager().getMap(0).getBoundingBox());
    }
}
