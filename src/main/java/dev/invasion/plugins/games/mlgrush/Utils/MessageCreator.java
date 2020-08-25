package dev.invasion.plugins.games.mlgrush.Utils;

import dev.invasion.plugins.games.mlgrush.MLGRush;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageCreator {
    public static String translate(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    public static String t(String message) {
        return translate(message);
    }
    public static TextComponent generateComponent(String message) {
        return new TextComponent(t(message));
    }
    public static String kickCreator(String row1, String row2, boolean yz_k) {
        StringBuilder toReturn = new StringBuilder();
        if(yz_k) {
            toReturn.append(t("&7\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500[ &c&l" + MLGRush.getGameName() + "&7 ]\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\n\n"));
            toReturn.append(t(row1));
            toReturn.append(t("\n\n"));
            toReturn.append(t(row2));
            toReturn.append(t("\n&7&oyz_k\n"));
            toReturn.append(t("\n\n"));
            toReturn.append(t("&7\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500"));
        } else {
            toReturn.append(t(row1));
            toReturn.append("\n");
            toReturn.append(t(row2));
        }
        return toReturn.toString();
    }
    public static void sendTitle(Player player, String title, String subtitle, int duration) {
        player.sendTitle(MessageCreator.t(title), MessageCreator.t(subtitle), 10, duration, 20);
    }
    public static void sendTitle(Player player, String title, String subtitle, int duration, boolean fade) {
        player.sendTitle(MessageCreator.t(title), MessageCreator.t(subtitle), 0, duration, 0);
    }
    public static String prefix(String prefix, String message) {
        return translate("&7[&b" + prefix + "&7] " + message);
    }
    public static String prefix(String message) {
        return translate("&7[&b" + MLGRush.getGameName() + "&7] " + message);
    }
}