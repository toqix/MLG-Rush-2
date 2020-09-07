package dev.invasion.plugins.games.mlgrush.Commands;

import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerDataManager;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerState;
import dev.invasion.plugins.games.mlgrush.Utils.Inventories;
import dev.invasion.plugins.games.mlgrush.Utils.MessageCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class saveInventoryCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if(PlayerDataManager.getPlayerData(player).getState() == PlayerState.GAME) {
                Inventories.saveGameInv(player);
            }else {
                player.sendMessage(MessageCreator.prefix("&cYou have to be in a Game to save your Inventory"));
            }
        }

        return true;
    }
}
