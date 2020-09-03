package dev.invasion.plugins.games.mlgrush.Listener;

import dev.invasion.plugins.games.mlgrush.MLGRush;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerData;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerDataManager;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerState;
import dev.invasion.plugins.games.mlgrush.Utils.MessageCreator;
import dev.invasion.plugins.games.mlgrush.maps.BoundingBoxActions;
import dev.invasion.plugins.games.mlgrush.maps.MapState;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.Arrays;

public class joinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        MLGRush.setWorld(player.getWorld());
        event.setJoinMessage("Minequafter " + event.getPlayer().getName() + " sagt Grüßgott");
        player.teleport(MLGRush.getLobbySpawn().getTpLocation());
        if(playerData.getState() == PlayerState.BUILD) {
            if(playerData.getMap().getMapState() == MapState.BUILD) {
                player.teleport(playerData.getMap().getSpecSpawn().getTpLocation());
                MessageCreator.sendTitle(player, "&7You re&ajoined&7 the Build", "&7You where editing &6" + playerData.getMap().getName(), 70, true);
            }
        }
    }
}
