package dev.invasion.plugins.games.mlgrush;

import dev.invasion.plugins.games.mlgrush.BuildMode.BuildListeners;
import dev.invasion.plugins.games.mlgrush.Commands.*;
import dev.invasion.plugins.games.mlgrush.Game.Game;
import dev.invasion.plugins.games.mlgrush.Game.GameManager;
import dev.invasion.plugins.games.mlgrush.Game.GameRunner;
import dev.invasion.plugins.games.mlgrush.Listener.joinListener;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerDataManager;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerSettings;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerSettingsManager;
import dev.invasion.plugins.games.mlgrush.Stats.StatsManager;
import dev.invasion.plugins.games.mlgrush.Utils.File.FileManager;
import dev.invasion.plugins.games.mlgrush.Utils.InventoryHandler;
import dev.invasion.plugins.games.mlgrush.maps.*;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class MLGRush extends JavaPlugin {
    private static World mainworld;
    private static String gameName = "MLG-Rush";
    private static MLGRush instance;
    private static MapManager mapManager;
    private static StatsManager statsManager;
    private static GameRunner gameRunner;
    private static PlayerSettingsManager playerSettingsManager;

    private static SerializableLocation lobbySpawn = new SerializableLocation(0, 100, 0);

    @Override
    public void onEnable() {
        // Plugin startup logic
        setInstance(this);
        commandRegistration();
        setWorld(Bukkit.getWorld("world"));
        setMapManager(new MapManager());
        setStatsManager(new StatsManager());
        setGameRunner(new GameRunner());
        setPlayerSettingsManager(new PlayerSettingsManager());
        listenerRegistration();
        //hardcode Map
        load();
        this.getLogger().info("MLG-Rush Rewrite enabled");

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

    public static GameRunner getGameRunner() {
        return gameRunner;
    }

    public static void setGameRunner(GameRunner gameRunner) {
        MLGRush.gameRunner = gameRunner;
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

    public static PlayerSettingsManager getPlayerSettingsManager() {
        return playerSettingsManager;
    }

    public static void setPlayerSettingsManager(PlayerSettingsManager playerSettingsManager) {
        MLGRush.playerSettingsManager = playerSettingsManager;
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
        getCommand("leave").setExecutor(new leaveCommand());
        getCommand("start").setExecutor(new startCommand());
        getCommand("saveinventory").setExecutor(new saveInventoryCommand());
    }

    private void listenerRegistration() {
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new joinListener(), this);
        manager.registerEvents(new PlayerDataManager(), this);
        manager.registerEvents(new InventoryHandler(), this);
        manager.registerEvents(new BuildListeners(), this);
        manager.registerEvents(getGameRunner(), this);

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
        try {
            playerSettingsManager = (PlayerSettingsManager) FileManager.load(PlayerSettingsManager.class, "PlayerSettings.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(mapManager == null) mapManager = new MapManager();

        if(statsManager == null) statsManager = new StatsManager();

        if(playerSettingsManager == null) playerSettingsManager = new PlayerSettingsManager();

        for(gameMap map: mapManager.getMaps()) {
            map.setMapState(MapState.WAITING);
            map.setAvailable(true);
        }
    }
    public static void save() {

        try {
            FileManager.save(getMapManager(), "Maps.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Bukkit.broadcastMessage("deaths: " + statsManager.getGeneralStats().getTotalDeaths());
          FileManager.save(statsManager, "Stats.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileManager.save(getPlayerSettingsManager(), "PlayerSettings.json");
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
