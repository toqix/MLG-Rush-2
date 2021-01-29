package dev.invasion.plugins.games.mlgrush.Utils;

import dev.invasion.plugins.games.mlgrush.MLGRush;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

public class KeyAssistant {

    private static final Plugin instance = MLGRush.getInstance();
    private static final String no_col_name = "MLGRush";

    public static NamespacedKey getKey(String key) {
        return new NamespacedKey(instance, no_col_name + "/" + key);
    }

}