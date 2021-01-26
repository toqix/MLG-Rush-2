package dev.invasion.plugins.games.mlgrush.BuildMode;

import dev.invasion.plugins.games.mlgrush.MLGRush;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerDataManager;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerState;
import dev.invasion.plugins.games.mlgrush.Utils.MessageCreator;
import dev.invasion.plugins.games.mlgrush.maps.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Map;

public class BuildListeners implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if(PlayerDataManager.getPlayerData(player).getState() == PlayerState.BUILD) {
            if(event.getBlock().getType() == Material.SANDSTONE) {
                event.setCancelled(true);
                player.sendMessage(MessageCreator.prefix("MLG-Rush-Build", "&cYou can't use this Block to Build"));
            }
            for (gameMap map : MLGRush.getMapManager().getMaps()) {
                if (map.getMapState() == MapState.BUILD) {
                    for (Player player1 : PlayerDataManager.getPlayers(map)) {
                        if (player == player1) {
                            BoundingBox box = map.getBoundingBox();
                            if (!box.isInside(new SerializableLocation(event.getBlock()))) {
                                event.setCancelled(true);
                                player.sendMessage(MessageCreator.prefix("MLG-Rush-Build", "You can't build outside the Map"));
                            }
                        }
                    }
                }
            }
        }
    }
    @EventHandler
    public void onBLockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if(PlayerDataManager.getPlayerData(player).getState() == PlayerState.BUILD) {

            for (gameMap map : MLGRush.getMapManager().getMaps()) {
                if (map.getMapState() == MapState.BUILD) {
                    for (Player player1 : PlayerDataManager.getPlayers(map)) {
                        if (player == player1) {
                            BoundingBox box = map.getBoundingBox();
                            if (!box.isInside(new SerializableLocation(event.getBlock()))) {
                                event.setCancelled(true);
                                player.sendMessage(MessageCreator.prefix("MLG-Rush-Build", "You can't build outside the Map"));
                            }
                        }
                    }
                }
            }
        }
    }
}
