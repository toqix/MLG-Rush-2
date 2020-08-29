package dev.invasion.plugins.games.mlgrush.Commands;

import dev.invasion.plugins.games.mlgrush.MLGRush;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerData;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerDataManager;
import dev.invasion.plugins.games.mlgrush.Utils.MessageCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class debugCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        PlayerDataManager.getPlayerData(player).setDebugOutput(!PlayerDataManager.getPlayerData(player).isDebugOutput());
        if(PlayerDataManager.getPlayerData(player).isDebugOutput()) {
            player.sendMessage(MessageCreator.prefix("&7You &aenabled&7 Debug Output"));
        }else {
            player.sendMessage(MessageCreator.prefix("&7You &cdisabled&7 Debug Output"));
        }
        return true;
    }
}
