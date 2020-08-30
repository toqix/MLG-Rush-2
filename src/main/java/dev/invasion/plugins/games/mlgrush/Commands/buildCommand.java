package dev.invasion.plugins.games.mlgrush.Commands;

import dev.invasion.plugins.games.mlgrush.BuildMode.BuildModeInvs;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerData;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerDataManager;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerState;
import dev.invasion.plugins.games.mlgrush.Utils.InvOpener;
import dev.invasion.plugins.games.mlgrush.Utils.Inventories;
import dev.invasion.plugins.games.mlgrush.Utils.MessageCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class buildCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if(playerData.getState() == PlayerState.LOBBY) {
            InvOpener.openDelay(player, BuildModeInvs.BuildMode());
        }else {
            if(playerData.getState() == PlayerState.GAME) {
                player.sendMessage(MessageCreator.prefix("MLG-Rush-Build", "&7You &ccan't &7join the &6Build&7Mode whilst in Game"));
            }else if(playerData.getState() == PlayerState.WAITING) {
                player.sendMessage(MessageCreator.prefix("MLG-Rush-Build", "&7You &ccan't &7join the &6Build&7Mode whilst in Queue"));
            }else if(playerData.getState() == PlayerState.BUILD) {
                InvOpener.openDelay(player, BuildModeInvs.BuildInv(PlayerDataManager.getPlayerData(player).getMap()));
            }
        }

        return true;
    }
}
