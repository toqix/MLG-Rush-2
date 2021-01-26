package dev.invasion.plugins.games.mlgrush.Commands;

import dev.invasion.plugins.games.mlgrush.Game.Game;
import dev.invasion.plugins.games.mlgrush.Game.GameManager;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerDataManager;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerState;
import dev.invasion.plugins.games.mlgrush.Utils.MessageCreator;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class joinCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 0) {
                Player owner = Bukkit.getPlayer(args[0]);
                if (owner != player) {
                    if (owner != null) {
                        if (PlayerDataManager.getPlayerData(owner).getState() == PlayerState.GAME) {
                            Game toJoin = PlayerDataManager.getPlayerData(owner).getGame();
                            if (toJoin != null) {
                                if (PlayerDataManager.getPlayerData(player).getState() == PlayerState.LOBBY) {
                                    GameManager.joinGame(player, toJoin);
                                }else {
                                    player.sendMessage(MessageCreator.prefix("&cYou can't join a Game whilst in Build or already in a Game"));
                                }
                            } else {
                                player.sendMessage(MessageCreator.prefix("&cThis Player is not in a Game"));
                            }
                        } else {
                            player.sendMessage(MessageCreator.prefix("&cThis Player is not in a Game"));
                        }
                    } else {
                        player.sendMessage(MessageCreator.prefix("&cThis Player doesn't exist"));
                    }
                } else {
                    player.sendMessage(MessageCreator.prefix("&cYou can't join Yourself"));
                }
            }
        }

        return true;
    }
}
