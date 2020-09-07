package dev.invasion.plugins.games.mlgrush.PlayerData;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PlayerSettingsManager {
    private HashMap<UUID, PlayerSettings> playerSettingsHashMap = new HashMap<UUID, PlayerSettings>();

    public PlayerSettings getPlayerSettings(Player player) {
        if (!playerSettingsHashMap.containsKey(player.getUniqueId())) {
            playerSettingsHashMap.put(player.getUniqueId(), new PlayerSettings());
        }
        return playerSettingsHashMap.get(player.getUniqueId());
    }


}
