package dev.invasion.plugins.games.mlgrush.PlayerData;

import dev.invasion.plugins.games.mlgrush.maps.gameMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager implements Listener {
    private static HashMap<UUID, PlayerData> playerDataHashMap = new HashMap<>();

    public static PlayerData getPlayerData(Player player) {
        if (!playerDataHashMap.containsKey(player.getUniqueId())) {
            playerDataHashMap.put(player.getUniqueId(), new PlayerData());
        }
        return playerDataHashMap.get(player.getUniqueId());
    }

    public static ArrayList<Player> getPlayers(PlayerState state) {
        ArrayList<Player> players = new ArrayList<>();
        for(Map.Entry<UUID, PlayerData> player:playerDataHashMap.entrySet()) {
            if(player.getValue().getState() == state) {
                players.add(Bukkit.getPlayer(player.getKey()));
            }
        }
        return players;
    }
    public static ArrayList<Player> getPlayers(gameMap map) {
        ArrayList<Player> players = new ArrayList<>();
        for(Map.Entry<UUID, PlayerData> player:playerDataHashMap.entrySet()) {
            if(player.getValue().getMap() == map) {
                players.add(Bukkit.getPlayer(player.getKey()));
            }
        }
        return players;
    }

    public static ArrayList<Player> getDebugPlayers() {
        ArrayList<Player> players = new ArrayList<>();
        for(Map.Entry<UUID, PlayerData> player:playerDataHashMap.entrySet()) {
            if(player.getValue().isDebugOutput()) {
                players.add(Bukkit.getPlayer(player.getKey()));
            }
        }
        return players;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
         if(!playerDataHashMap.containsKey(event.getPlayer().getUniqueId())) {
             playerDataHashMap.put(event.getPlayer().getUniqueId(), new PlayerData());
         }
    }
}
