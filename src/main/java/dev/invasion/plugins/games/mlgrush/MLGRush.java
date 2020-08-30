package dev.invasion.plugins.games.mlgrush;

import dev.invasion.plugins.games.mlgrush.Commands.*;
import dev.invasion.plugins.games.mlgrush.Listener.joinListener;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerDataManager;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerState;
import dev.invasion.plugins.games.mlgrush.Stats.StatsManager;
import dev.invasion.plugins.games.mlgrush.Utils.File.FileManager;
import dev.invasion.plugins.games.mlgrush.Utils.InventoryHandler;
import dev.invasion.plugins.games.mlgrush.maps.*;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class MLGRush extends JavaPlugin {
    private static World mainworld;
    private static String gameName = "MLG-Rush";
    private static MLGRush instance;
    private static MapManager mapManager;
    private static StatsManager statsManager;

    private static SerializableLocation lobbySpawn = new SerializableLocation(0, 100, 0);

    @Override
    public void onEnable() {
        setInstance(this);
        commandRegistration();
        listenerRegistration();
        setWorld(Bukkit.getWorld("world"));
        setMapManager(new MapManager());
        setStatsManager(new StatsManager());
        //hardcode Map
        load();

        // Plugin startup logic
        this.getLogger().info("MLG-Rush v2 enabled");

    }
    public void reload() {
        save();
        load();
    }

    public static SerializableLocation getLobbySpawn() {
        return lobbySpawn;
    }

    public static StatsManager getStatsManager() {
        return statsManager;
    }

    public static void setStatsManager(StatsManager statsManager) {
        MLGRush.statsManager = statsManager;
    }

    public static MapManager getMapManager() {
        return mapManager;
    }

    public static void setMapManager(MapManager manager) {
        mapManager = manager;
    }
    public static void setInstance(MLGRush insta) {
        instance = insta;
    }
    public static MLGRush getInstance() {
        return instance;
    }

    public static String getGameName() {
        return gameName;
    }

    public static void setWorld(World world) {
        mainworld = world;
    }
    public static World getWorld() {
        return mainworld;
    }
    private void commandRegistration() {
        getCommand("join").setExecutor(new joinCommand());
        getCommand("create").setExecutor(new createCommand());
        getCommand("spectate").setExecutor(new spectateCommand());
        getCommand("build").setExecutor(new buildCommand());
        getCommand("debug").setExecutor(new debugCommand());
        getCommand("load").setExecutor(new loadCommand());
        getCommand("save").setExecutor(new saveCommand());
        getCommand("spawn").setExecutor(new spawnCommand());
    }

    private void listenerRegistration() {
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new joinListener(), this);
        manager.registerEvents(new PlayerDataManager(), this);
        manager.registerEvents(new InventoryHandler(), this);
    }

    public static void load() {
        //load mapManager
        try {
            mapManager = (MapManager) FileManager.load(MapManager.class, "Maps.json");
        } catch (IOException e) {
            e.printStackTrace();
            //this.getLogger().info("error couldn't load File " + "Maps.json");
        }
        try {
            statsManager = (StatsManager) FileManager.load(StatsManager.class, "Stats.json");
        } catch (IOException e) {
            e.printStackTrace();
           // getLogger().info("error couldn't load File " + "Stats.json");
        }
        for(gameMap map: mapManager.getMaps()) {
            map.setMapState(MapState.WAITING);
            map.setAvailable(true);
        }
    }
    public static void save() {
        statsManager.getGeneralStats().setTotalDeaths(1);
        //statsManager.getPlayerStats(Bukkit.getPlayer("Zeck_do")).setBeds(1);
        statsManager.getMapStats(0).setDateCreated(0);
        try {
            FileManager.save(getMapManager(), "Maps.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
          FileManager.save(statsManager, "Stats.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        save();
    }
}
