package dev.invasion.plugins.games.mlgrush.BuildMode;

import dev.invasion.plugins.games.mlgrush.MLGRush;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerData;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerDataManager;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerState;
import dev.invasion.plugins.games.mlgrush.Stats.MapStats;
import dev.invasion.plugins.games.mlgrush.Utils.AnvilGUI.AnvilGUI;
import dev.invasion.plugins.games.mlgrush.Utils.InvOpener;
import dev.invasion.plugins.games.mlgrush.Utils.InventoryHandler;
import dev.invasion.plugins.games.mlgrush.Utils.MessageCreator;
import dev.invasion.plugins.games.mlgrush.maps.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class BuildModeManager {
    private static MapManager mapManager = MLGRush.getMapManager();


    public static void handleClick(String arguments, Player player) {
        switch (arguments) {
            case "create":
                createMap(player, false);
                break;
            case "edit":
                PlayerDataManager.getPlayerData(player).setPage(0);
                InvOpener.openDelay(player, BuildModeInvs.EditMapMode(player));
                break;
            case "notemptydelete":
                InvOpener.closeDelay(player);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        createMap(player, true);
                    }
                }.runTaskLater(MLGRush.getInstance(), 5);
                break;
            case "notemptyabort":
                player.kickPlayer(MessageCreator.kickCreator("&cPlugin error", "&7aborting Map creation please rejoin and try again", false));break;
            case "rename":
                renameMap(player);
                break;
            case "deleterequest":
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        InvOpener.openDelay(player, BuildModeInvs.Delete());
                    }
                }.runTaskLater(MLGRush.getInstance(), 5);break;

            case "delete":
                InvOpener.closeDelay(player);
                deleteMap(player);break;
            case "cancel":InvOpener.closeDelay(player);break;
        }
    }

    private static void createMap(Player player, boolean delete) {
        new AnvilGUI.Builder()
                .onComplete(((player1, s) -> {
                    if (player == player1) {
                        onComplete(player, s, delete);
                        if (delete) {
                            Bukkit.getScheduler().runTaskLater(MLGRush.getInstance(), () -> InvOpener.closeDelay(player), 5);
                        }
                    }
                    return AnvilGUI.Response.text("Creating Map");

                }))
                .title("Name your map")
                .text(player.getName() + "'s new Map")
                .plugin(MLGRush.getInstance())
                .open(player);

    }

    private static void onComplete(Player player, String text, boolean delete) {
        SerializableLocation location;
        if (mapManager.getMaps().isEmpty()) {
            location = MLGRush.getLobbySpawn().getCopy();
        } else {
            location = mapManager.getMap(mapManager.getMaps().size() - 1).getSpecSpawn().getCopy();
        }
        location.add(0, 0, 95);
        BoundingBox box = new BoundingBox(location, 50, 30, 20);
        int notEmpty = BoundingBoxActions.checkEmpty(box);
        if (notEmpty > 20) {
            player.teleport(MLGRush.getLobbySpawn().getTpLocation());
            new BukkitRunnable() {
                @Override
                public void run() {
                    InvOpener.openDelay(player, BuildModeInvs.BoxNotEmtpy());
                }
            }.runTaskLater(MLGRush.getInstance(), 20);

        } else {
            BoundingBoxActions.deleteBox(box);
            mapManager.addMap(new gameMap(location, box, text));
            player.sendMessage(MessageCreator.prefix("MLG-Rush-Build", "&7Map was created &asuccessfully"));
            MLGRush.getWorld().getBlockAt(location.getCopy().getLocation().add(0, -1, 0)).setType(Material.DIAMOND_BLOCK);
            editMap(player, mapManager.getMap(mapManager.getMaps().size() - 1));
        }
    }

    public static void editMap(Player player, gameMap map) {
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        map.setAvailable(false);
        map.setMapState(MapState.BUILD);
        playerData.setState(PlayerState.BUILD);
        playerData.setMap(map);

        BoundingBoxActions.replace(Material.SANDSTONE, map.getBoundingBox());
        player.teleport(map.getSpecSpawn().getTpLocation());
        player.setGameMode(GameMode.CREATIVE);
        player.setFlying(true);
    }

    public static void renameMap(Player player) {
        if (PlayerDataManager.getPlayerData(player).getState() == PlayerState.BUILD) {
            gameMap map = PlayerDataManager.getPlayerData(player).getMap();

            new AnvilGUI.Builder()
                    .onComplete(((player1, s) -> {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                InvOpener.closeDelay(player);
                            }
                        }.runTaskLater(MLGRush.getInstance(), 10);
                        String response = "";
                        if (player == player1) {

                            if (map.getName().equals(s)) {
                                response = "This Name already exists";
                            } else {
                                map.setName(s);
                                response = "Name changed";
                            }
                        }
                        return AnvilGUI.Response.text(response);

                    }))
                    .title("Rename your map")
                    .text(map.getName())
                    .plugin(MLGRush.getInstance())
                    .open(player);
        }
    }

    public static void deleteMap(Player player) {
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if(playerData.getState() == PlayerState.BUILD) {
            if (player.isOp()) {
                player.sendMessage(MessageCreator.prefix("MLG-Rush-Build", "Map &cdeletion&7 was initiated!"));
                playerData.setDebugOutput(true);
                gameMap map = playerData.getMap();
                BoundingBoxActions.deleteBox(map.getBoundingBox());
                MLGRush.getMapManager().getMaps().remove(map);
                playerData.setDebugOutput(false);
                player.sendMessage(MessageCreator.prefix("MLG-Rush-Build", "Map was &cdeleted&7 successfully"));
            } else {
                player.sendMessage(MessageCreator.prefix("MLG-Rush-Build", "Couldn't initiate deletion! no permission"));
            }
        }
    }

}
