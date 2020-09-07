package dev.invasion.plugins.games.mlgrush.Commands;

import dev.invasion.plugins.games.mlgrush.Game.GameManager;
import dev.invasion.plugins.games.mlgrush.MLGRush;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerDataManager;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerState;
import dev.invasion.plugins.games.mlgrush.Utils.InvOpener;
import dev.invasion.plugins.games.mlgrush.Utils.Inventories;
import dev.invasion.plugins.games.mlgrush.maps.MapManager;
import dev.invasion.plugins.games.mlgrush.maps.MapState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class createCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(PlayerDataManager.getPlayerData(player).getState() == PlayerState.LOBBY) {
                PlayerDataManager.getPlayerData(player).setPage(0);
                InvOpener.openDelay(player, Inventories.CreateMap(player));
            }
        }
        return true;
    }
}
