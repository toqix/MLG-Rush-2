package dev.invasion.plugins.games.mlgrush.Stats;

import dev.invasion.plugins.games.mlgrush.maps.gameMap;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class StatsManager {
    private HashMap<UUID, PlayerStats> playerStatsHashMap = new HashMap<>();
    private HashMap<Integer, MapStats> mapStatsHashMap = new HashMap<>();
    private GeneralStats generalStats;

    public StatsManager() {
        generalStats = new GeneralStats();
    }

    public PlayerStats getPlayerStats(Player player) {
        if(!playerStatsHashMap.containsKey(player.getUniqueId())) {
            playerStatsHashMap.put(player.getUniqueId(), new PlayerStats());
        }
        return playerStatsHashMap.get(player.getUniqueId());
    }


    public MapStats getMapStats(gameMap map) {
        if(!mapStatsHashMap.containsKey(map.getId())) {
            mapStatsHashMap.put(map.getId(), new MapStats());
        }
        return mapStatsHashMap.get(map.getId());
    }

    public GeneralStats getGeneralStats() {
        return generalStats;
    }

}
