package dev.invasion.plugins.games.mlgrush.Game;

import dev.invasion.plugins.games.mlgrush.MLGRush;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerData;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerDataManager;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerState;
import dev.invasion.plugins.games.mlgrush.Stats.StatsManager;
import dev.invasion.plugins.games.mlgrush.Utils.Inventories;
import dev.invasion.plugins.games.mlgrush.Utils.MessageCreator;
import dev.invasion.plugins.games.mlgrush.maps.BoundingBoxActions;
import dev.invasion.plugins.games.mlgrush.maps.Respawn;
import dev.invasion.plugins.games.mlgrush.maps.SerializableLocation;
import dev.invasion.plugins.games.mlgrush.maps.Team;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Bed;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class GameRunner implements Listener {

    public GameRunner() {
        runnable();
    }

    //all needed Loops
    private void runnable() {
        //fall and die
        new BukkitRunnable() {
            @Override
            public void run() {
                //loop through every Player who is in a Game
                for (Player player : GameManager.getAllPlayers()) {
                    if (PlayerDataManager.getPlayerData(player).getGame().isRunning()) {
                        //teleport the Player to his Spawn if he falls below 90
                        if (player.getLocation().getY() < 90) {
                            playerDie(player);
                        }
                    }
                }
            }
        }.runTaskTimer(MLGRush.getInstance(), 0, 5);

        //The actionbar to display gameinfo
        new BukkitRunnable() {
            @Override
            public void run() {
                //loop to display actionbar for all playing players
                for (Player player : GameManager.getAllPlayers()) {
                    if (PlayerDataManager.getPlayerData(player).getGame().isRunning()) {
                        PlayerData playerData = PlayerDataManager.getPlayerData(player);
                        StringBuilder info = new StringBuilder();

                        info.append("  &6Your Team ").append(playerData.getTeam().getName()).append("&7: &0&l").append(playerData.getTeam().getScore());

                        for (GameTeam gameTeam : playerData.getGame().getTeams()) {
                            if (gameTeam != playerData.getTeam()) {
                                info.append("  &7|").append("  &6Team ").append(gameTeam.getName()).append("&7: &0&l").append(gameTeam.getScore());
                            }
                        }
                        MessageCreator.sendActionbar(player, info.toString());
                    }
                }

                //loop to display actionbar to all Spectators
                for (Player player : GameManager.getSpectators()) {
                    PlayerData playerData = PlayerDataManager.getPlayerData(player);
                    //create the actionBar String
                    StringBuilder actionBar = new StringBuilder();
                    actionBar.append("&6Spectating: &7 ").append(playerData.getMap().getName());
                    for (GameTeam gameTeam : playerData.getGame().getTeams()) {
                        actionBar.append("  &7|").append("  &6Team ").append(gameTeam.getName()).append("&7: &0&l").append(gameTeam.getScore());
                    }
                    //set the Actionbar
                    MessageCreator.sendActionbar(player, actionBar.toString());

                    //Prevent Spectator from falling to void/tp him back to lobby spawn if he/she falls to low
                    if(player.getLocation().getY() < 70) {
                        player.teleport(playerData.getMap().getSpecSpawn().getTpLocation());
                    }
                }
            }
        }.runTaskTimer(MLGRush.getInstance(), 0, 10);
    }


    //Player die teleport back, save death, give items and so on
    private void playerDie(Player player) {
        player.setFallDistance(0);
        player.teleport(PlayerDataManager.getPlayerData(player).getTeam().getSpawn().getTpLocation());
        StatsManager statsManager = MLGRush.getStatsManager();
        statsManager.getPlayerStats(player).addDeath();
        statsManager.getGeneralStats().addTotalDeath();
        statsManager.getMapStats(PlayerDataManager.getPlayerData(player).getMap()).addTotalDeaths();
        Inventories.loadGameInv(player); //give the Player his items knockbackstick and so on
    }

    //bed destroyed gives points and stats to the destroyer also resets the Map
    private void bedDestroyed(Player player, GameTeam teamDestroyed) {
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        GameTeam playerTeam = playerData.getTeam();
        StatsManager statsManager = MLGRush.getStatsManager();
        Game game = playerData.getGame();

        playerTeam.addScore();
        statsManager.getPlayerStats(player).addBed();
        BoundingBoxActions.replace(Material.SANDSTONE, game.getMap().getBoundingBox());
        for (Player player1 : game.getPlayers()) {

            if (player1 != player) {
                player1.sendMessage(MessageCreator.prefix(player.getName() + " &cdestroyed&7 the Bed from Team " + teamDestroyed.getName()));
                MessageCreator.sendTitle(player1, "Team " + teamDestroyed.getName(), player.getName() + " &cdestroyed&f the Bed", 50, true);
            } else {
                player.sendMessage(MessageCreator.prefix("You &cdestroyed &7the Bed of Team " + teamDestroyed.getName()));
                MessageCreator.sendTitle(player, "You &cDestroyed&f the bed", "of Team " + teamDestroyed.getName(), 50, true);
            }
            player1.setFallDistance(0);
            player1.teleport(PlayerDataManager.getPlayerData(player1).getTeam().getSpawn().getTpLocation());
            Inventories.loadGameInv(player1);
            player1.playSound(player.getLocation(), Sound.ENTITY_BAT_DEATH, SoundCategory.MASTER, 1, 1);
        }
        if(playerTeam.getScore() >= game.getMap().getWinsNeeded()) {
            //the team of the player has won
            winGame(playerTeam, game);
        }
        statsManager.getPlayerStats(player).addBed();
        statsManager.getGeneralStats().addTotalBed();
        statsManager.getMapStats(game.getMap()).addTotalBed();

    }

    private void winGame(GameTeam team, Game game) {

        for(Player player: team.getPlayers()) {
            player.sendMessage(MessageCreator.prefix("You &6won&7 the Game || fritzibus ist ein L"));
            MessageCreator.sendTitle(player, "&6You Won!", "&acongratulations <the Invasion Devs>", 70, true);
            MLGRush.getStatsManager().getPlayerStats(player).addWin();
        }
        for(Player player: game.getPlayers()) {
            if (PlayerDataManager.getPlayerData(player).getTeam() != team) {
                player.sendMessage(MessageCreator.prefix("Team " + team.getName() + "&6won&7 the Game haha you're a noob"));
                MessageCreator.sendTitle(player, "Team " + team.getName(), "&6&lWon &7you noob <the Invasion Devs>", 70, true);
                MLGRush.getStatsManager().getPlayerStats(player).addLoos();
            }

        }
        game.endGame();
    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        Block block = event.getBlock();
        if (playerData.getState() == PlayerState.GAME) {
            //check if the Game is running
            if (playerData.getGame().isRunning()) {
                //check if the brokenBlock is inside the Map
                if (playerData.getMap().getBoundingBox().isInside(new SerializableLocation(block))) {
                    if (block.getType() != Material.SANDSTONE) {

                        if (block.getType() == Material.RED_BED) {
                            //bed destroy logic checks if the block destroyed was on a team bed
                            SerializableLocation blockLoc = new SerializableLocation(block);
                            GameTeam playerTeam = PlayerDataManager.getPlayerData(player).getTeam();

                            for (GameTeam team : playerData.getGame().getTeams()) {
                                Respawn bed = team.getBed();
                                if (bed.getLocation().equals(blockLoc)) {
                                    if (!team.equals(playerTeam)) {
                                        bedDestroyed(player, team);
                                    } else {
                                        player.sendMessage(MessageCreator.prefix("&cYou can't destroy your own bed!"));
                                    }
                                }
                                if (new SerializableLocation(bed.getLocation().getBlock().getRelative(bed.getRotation())).equals(blockLoc)) {
                                    if (!team.equals(playerTeam)) {
                                        bedDestroyed(player, team);
                                    } else {
                                        player.sendMessage(MessageCreator.prefix("&cYou can't destroy your own bed!"));
                                    }
                                }
                            }
                        }
                        event.setCancelled(true);
                    }
                } else {
                    event.setCancelled(true);
                }
            }
        } else if (playerData.getState() == PlayerState.SPECTATING) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        Block block = event.getBlock();
        if (playerData.getState() == PlayerState.GAME) {
            //check if the Game is running
            if (playerData.getGame().isRunning()) {
                //check if the brokenBlock is inside the Map
                if (playerData.getMap().getBoundingBox().isInside(new SerializableLocation(block))) {
                    if (block.getType() != Material.SANDSTONE) {
                        //block is not sandstone
                        event.setCancelled(true);
                    }
                } else {
                    //block is outside the map
                    event.setCancelled(true);
                }
            }
        }else if (playerData.getState() == PlayerState.SPECTATING) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.hasBlock()) {
            if (event.getClickedBlock().getType() == Material.RED_BED) {
                 event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public  void onDamage(EntityDamageEvent event) {
        //check if the damaged entity is a Player
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerData playerData = PlayerDataManager.getPlayerData(player);
            //check if the Player is inside a running Game
            if (playerData.getState() == PlayerState.GAME) {
                //get damage cause
                event.setDamage(0);
            }else if (playerData.getState() == PlayerState.SPECTATING) {
                event.setCancelled(true);
            }

        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        //check if both entites are players
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            //set a damger and a damaged player
            Player damaged = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();

            //cancel the event if the damager is a spectator
            if(PlayerDataManager.getPlayerData(damager).getState() == PlayerState.SPECTATING) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerHungerLevelChange(FoodLevelChangeEvent event) {
        //check if the entity is a Player
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            //reset food level
            player.setFoodLevel(20);
        }
    }

}
