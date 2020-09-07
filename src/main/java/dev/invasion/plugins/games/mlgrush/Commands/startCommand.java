package dev.invasion.plugins.games.mlgrush.Commands;

import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerData;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerDataManager;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerState;
import dev.invasion.plugins.games.mlgrush.Utils.MessageCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class startCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            PlayerData playerData = PlayerDataManager.getPlayerData(player);
            if (playerData.getState() == PlayerState.GAME) {
                if (!playerData.getGame().isRunning()) {
                    playerData.getGame().start();
                }else {
                    player.sendMessage(MessageCreator.prefix("&cYou can't start a running Game"));
                }
            } else {
                player.sendMessage(MessageCreator.prefix("&cYou're not in a Game"));
            }
        }
        return true;
    }
}
