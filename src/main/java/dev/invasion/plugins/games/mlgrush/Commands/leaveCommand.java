package dev.invasion.plugins.games.mlgrush.Commands;

import dev.invasion.plugins.games.mlgrush.Game.GameManager;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerDataManager;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerState;
import dev.invasion.plugins.games.mlgrush.Utils.MessageCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class leaveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if(PlayerDataManager.getPlayerData(player).getState() == PlayerState.GAME) {
                GameManager.leaveGame(player);
                //player.sendMessage(MessageCreator.prefix("You &cleft &7the&6 Game"));
                //MessageCreator.sendTitle(player, "&7You &cleft&7","the &6Game", 50, true);
            }
        }

        return true;
    }
}
