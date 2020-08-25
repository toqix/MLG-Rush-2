package dev.invasion.plugins.games.mlgrush;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public final class MLGRush extends JavaPlugin {
    private static World mainworld;
    private static String gameName = "MLG-Rush";
    private static MLGRush instance;

    @Override
    public void onEnable() {
        setInstance(this);
        // Plugin startup logic
        this.getLogger().info("MLG-Rush v2 enabled");

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

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
