package dev.invasion.plugins.games.mlgrush.Commands;

import dev.invasion.plugins.games.mlgrush.MLGRush;
import dev.invasion.plugins.games.mlgrush.Utils.MessageCreator;
import dev.invasion.plugins.games.mlgrush.maps.MapManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class saveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        MLGRush.save();
        sender.sendMessage(MessageCreator.prefix("&asaved &7all current data"));
        return true;
    }
}
