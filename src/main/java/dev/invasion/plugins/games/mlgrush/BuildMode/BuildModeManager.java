package dev.invasion.plugins.games.mlgrush.BuildMode;

import dev.invasion.plugins.games.mlgrush.MLGRush;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerData;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerDataManager;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerState;
import dev.invasion.plugins.games.mlgrush.Stats.MapStats;
import dev.invasion.plugins.games.mlgrush.Utils.AnvilGUI.AnvilGUI;
import dev.invasion.plugins.games.mlgrush.Utils.InvOpener;
import dev.invasion.plugins.games.mlgrush.Utils.Inventories;
import dev.invasion.plugins.games.mlgrush.Utils.InventoryHandler;
import dev.invasion.plugins.games.mlgrush.Utils.MessageCreator;
import dev.invasion.plugins.games.mlgrush.maps.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
                player.kickPlayer(MessageCreator.kickCreator("&cPlugin error", "&7aborting Map creation please rejoin and try again", false));
                break;
            case "rename":
                renameMap(player);
                break;
            case "deleterequest":
                InvOpener.openDelay(player, BuildModeInvs.Delete());
                break;
            case "delete":
                InvOpener.closeDelay(player);
                deleteMap(player);
                break;
            case "cancel":
                InvOpener.closeDelay(player);
                break;
            case "leave":
                InvOpener.closeDelay(player);
                handleLeave(player);
                break;
            case "main":
                InvOpener.openDelay(player, BuildModeInvs.BuildInv(PlayerDataManager.getPlayerData(player).getMap()));
                break;
            case "teams":
                InvOpener.openDelay(player, BuildModeInvs.TeamsInv(PlayerDataManager.getPlayerData(player).getMap()));
                break;
            case "rteam":
                removeTeam(player);
                break;
            case "ateam":
                addTeam(player);
                break;
            case "+size":
                PlayerDataManager.getPlayerData(player).getMap().getTeamManager().setTeamSize(PlayerDataManager.getPlayerData(player).getMap().getTeamManager().getTeamSize() + 1);
                InvOpener.openDelay(player, BuildModeInvs.TeamsInv(PlayerDataManager.getPlayerData(player).getMap()));
                break;
            case "-size":
                PlayerDataManager.getPlayerData(player).getMap().getTeamManager().setTeamSize(PlayerDataManager.getPlayerData(player).getMap().getTeamManager().getTeamSize() - 1);
                InvOpener.openDelay(player, BuildModeInvs.TeamsInv(PlayerDataManager.getPlayerData(player).getMap()));
                break;
            case "finish":
                finishMap(player);
                InvOpener.closeDelay(player);
                break;
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
        if (notEmpty > 20 && !delete) {
            player.teleport(MLGRush.getLobbySpawn().getTpLocation());
            new BukkitRunnable() {
                @Override
                public void run() {
                    InvOpener.openDelay(player, BuildModeInvs.BoxNotEmtpy());
                }
            }.runTaskLater(MLGRush.getInstance(), 20);

        } else {
            BoundingBoxActions.deleteBox(box);
            gameMap map = new gameMap(location, box, text);
            mapManager.addMap(map);
            map.setId(mapManager.getMaps().size() - 1);
            MapStats mapStats = MLGRush.getStatsManager().getMapStats(map);
            mapStats.setDateCreated(Instant.now().getEpochSecond());
            player.sendMessage(MessageCreator.prefix("MLG-Rush-Build", "&7Map was created &asuccessfully"));
            MLGRush.getWorld().getBlockAt(location.getCopy().getLocation().add(0, -1, 0)).setType(Material.DIAMOND_BLOCK);
            editMap(player, map);
        }
    }

    public static void editMap(Player player, gameMap map) {
        Bukkit.broadcastMessage(map.getMapState().name());
        if (map.getMapState() != MapState.GAME) {
            PlayerData playerData = PlayerDataManager.getPlayerData(player);
            map.setAvailable(false);
            map.setMapState(MapState.BUILD);
            playerData.setState(PlayerState.BUILD);
            playerData.setMap(map);

            BoundingBoxActions.replace(Material.SANDSTONE, map.getBoundingBox());
            for (Team team : map.getTeamManager().getTeams()) {
                if (team.getBed() != null) {
                    team.getBed().setBlock();
                }
            }
            player.teleport(map.getSpecSpawn().getTpLocation());
            player.setGameMode(GameMode.CREATIVE);
            player.setFlying(true);
        } else {
            player.sendMessage(MessageCreator.prefix("MLG-Rush-Build", "&cError you tried to edit a running Map"));
        }
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

    public static void handleLeave(Player player) {
        PlayerData data = PlayerDataManager.getPlayerData(player);
        if (data.getState() == PlayerState.BUILD) {
            gameMap map = data.getMap();
            if (PlayerDataManager.getPlayers(map).size() == 1) {
                map.setFinished(false);
                map.setMapState(MapState.WAITING);
                map.setAvailable(true);
            }
            leaveBuild(player);
        }
    }

    public static void leaveBuild(Player player) {
        PlayerData data = PlayerDataManager.getPlayerData(player);
        data.setState(PlayerState.LOBBY);
        data.setMap(null);
        player.teleport(MLGRush.getLobbySpawn().getTpLocation());
        player.setFlying(false);
        player.setGameMode(GameMode.SURVIVAL);
        player.sendMessage(MessageCreator.prefix("MLG-Rush-Build", "&7You &cleft &7BuildMode"));
    }

    public static void deleteMap(Player player) {
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData.getState() == PlayerState.BUILD) {
            if (player.hasPermission("MLG-Rush.DeleteMap")) {
                player.sendMessage(MessageCreator.prefix("MLG-Rush-Build", "Map &cdeletion&7 was initiated!"));
                playerData.setDebugOutput(true);
                gameMap map = playerData.getMap();
                BoundingBoxActions.deleteBox(map.getBoundingBox());
                MLGRush.getMapManager().getMaps().remove(map);
                playerData.setDebugOutput(false);
                player.sendMessage(MessageCreator.prefix("MLG-Rush-Build", "Map was &cdeleted&7 successfully"));
                for (Player player1 : PlayerDataManager.getPlayers(map)) {
                    leaveBuild(player1);
                }
            } else {
                player.sendMessage(MessageCreator.prefix("MLG-Rush-Build", "Couldn't initiate deletion! no permission"));
            }
        }
    }
    public static void finishMap(Player player) {
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if(playerData.getState() == PlayerState.BUILD) {
            gameMap map = playerData.getMap();
            if(checkFinished(map)) {
                map.setMapState(MapState.WAITING);
                map.setFinished(true);
                map.setAvailable(true);
                for(Player player1: PlayerDataManager.getPlayers(map)) {
                    MessageCreator.sendTitle(player1, "&aMap finished", "Your map will be saved", 50, true);
                    player1.sendMessage(MessageCreator.prefix("MLG-Rush-Build", "&6" + player.getName() + " &7saved the Map"));
                    leaveBuild(player1);
                }
            }else {
                player.sendMessage(MessageCreator.prefix("MLG-Rush-Build", "&cSorry you can't finish this map it's not fully configured yet"));
            }
        }

    }

    public static boolean checkFinished(gameMap map) {
        if (map.getTeamManager() == null) return false;

        for (Team team : map.getTeamManager().getTeams()) {
            if (team.getSpawn() == null) return false;
            if (team.getBed() == null) return false;
            if (team.getColor() == null) return false;
        }

        if(map.getBoundingBox() == null) return false;
        if(map.getName() == null) return false;

        return true;
    }

    public static void addTeam(Player player) {
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData.getState() == PlayerState.BUILD) {
            gameMap map = playerData.getMap();
            map.getTeamManager().addTeam();
            InvOpener.openDelay(player, BuildModeInvs.TeamsInv(map));
            player.sendMessage(MessageCreator.prefix("MLG-Rush-Build", "&aadded&7 a Team"));
        }
    }

    public static void removeTeam(Player player) {
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData.getState() == PlayerState.BUILD) {
            gameMap map = playerData.getMap();
            map.getTeamManager().removeTeam();
            InvOpener.openDelay(player, BuildModeInvs.TeamsInv(map));
            player.sendMessage(MessageCreator.prefix("MLG-Rush-Build", "&cremoved&7 a Team"));
        }
    }

    public static void setSpawnDirection(Player player, TeamColor color) {
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData.getState() == PlayerState.BUILD) {
            gameMap map = playerData.getMap();
            //calculate yaw
            float playerYaw = player.getLocation().getYaw();
            int yaw = 0;
            if(playerYaw >= 315 || playerYaw < 45) {
                yaw = 0;
            } else if (playerYaw >= 45 && playerYaw < 135) {
                yaw = 90;
            } else if (playerYaw >= 135 && playerYaw < 225) {
                yaw = 180;
            } else if (playerYaw >= 225 && playerYaw < 315) {
                yaw = 270;
            }

            map.getTeamManager().getTeam(color).getSpawn().setYaw(yaw);
            player.teleport(map.getTeamManager().getTeam(color).getSpawn().getTpLocation());
            MessageCreator.sendTitle(player, "&6Successfully Set", "the Spawn direction", 50);
            new BukkitRunnable() {
                @Override
                public void run() {
                    InvOpener.openDelay(player, BuildModeInvs.BuildInv(map));
                }
            }.runTaskLater(MLGRush.getInstance(), 50);
        }
    }

}
