package dev.invasion.plugins.games.mlgrush.Game;

import dev.invasion.plugins.games.mlgrush.MLGRush;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerData;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerDataManager;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerState;
import dev.invasion.plugins.games.mlgrush.Utils.Inventories;
import dev.invasion.plugins.games.mlgrush.Utils.MessageCreator;
import dev.invasion.plugins.games.mlgrush.maps.BoundingBoxActions;
import dev.invasion.plugins.games.mlgrush.maps.MapState;
import dev.invasion.plugins.games.mlgrush.maps.Team;

import dev.invasion.plugins.games.mlgrush.maps.gameMap;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Game {
    private ArrayList<Player> players = new ArrayList<>();
    private gameMap map;
    private int teamAmount;
    private int teamSize;
    private boolean joinAllowed;
    private boolean running;
    private boolean isStarting;
    private ArrayList<GameTeam> teams = new ArrayList<>();


    public Game(Player player, gameMap map) {
        this.map = map;
        //creating the new GameTeams
        for (Team team : map.getTeamManager().getTeams()) {
            teams.add(new GameTeam(team));
            team.getBed().setBlock();
        }

        teamAmount = teams.size();
        teamSize = map.getTeamManager().getTeamSize();
        //set the game states
        joinAllowed = true;
        running = false;
        isStarting = false;
        //adding the first player to the first team
        join(player);
        //setting the map to inGame so that it is blocked from build mode
        map.setMapState(MapState.GAME);
    }

    public boolean join(Player player) {
        if (!isFull() && !running && joinAllowed) {
            //check for already full teams
            ArrayList<GameTeam> notFullTeams = new ArrayList<>();
            for (GameTeam team : teams) {
                if (team.getPlayers().size() < teamSize) {
                    notFullTeams.add(team);
                }
            }
            //sort the not full teams to find the most empty one
            GameTeam smallestTeam;
            notFullTeams.sort(Comparator.comparingInt(GameTeam::playerCount));
            smallestTeam = notFullTeams.get(0);
            //send every player that is inside this Game a message that you joined
            for (Player player1 : PlayerDataManager.getPlayers(map)) {
                player1.sendMessage(MessageCreator.prefix("&6" + player.getName() + "&f joined the game"));
            }
            //get the PlayerData
            PlayerData playerData = PlayerDataManager.getPlayerData(player);
            //save everything into playerdata
            playerData.setMap(getMap());
            playerData.setState(PlayerState.GAME);
            playerData.setGame(this);
            playerData.setTeam(smallestTeam);
            addPlayer(smallestTeam, player);
            //output additional information for debug
            if (playerData.isDebugOutput()) {
                player.sendMessage(MessageCreator.prefix("Debug", "Your team: " + smallestTeam.getName()));
                player.sendMessage(MessageCreator.prefix("Debug", "------ Other Players waiting -----"));
                for (Player player1 : PlayerDataManager.getPlayers(map)) {
                    player.sendMessage(MessageCreator.prefix("Debug", player1.getName() + " in team: " + PlayerDataManager.getPlayerData(player1).getTeam().getName()));
                }
                player.sendMessage(MessageCreator.prefix("Debug", "--------------------------------"));
                player.sendMessage("");
                player.sendMessage(MessageCreator.prefix("Debug", "Game on map: " + map.getName()));
                player.sendMessage(MessageCreator.prefix("Debug", "Teams: "));
                for (GameTeam team : teams) {
                    player.sendMessage(MessageCreator.prefix("Debug", "Team " + team.getName() + " with " + teamSize + " players"));
                }
            }

            //set Player food level to full
            player.setFoodLevel(20);

            player.sendMessage(MessageCreator.prefix("&aYou joined a Game&7, please wait for it to fill or use &6/start&7"));
            MessageCreator.sendTitle(player, "&6Game &ajoined", "Playing: &6" + getMap().getName(), 50, true);

            if (isFull()) {
                start();
            }
            return true;
        }
        player.sendMessage(MessageCreator.prefix("&cCouldn't join Game!"));
        return false;
    }

    private void addPlayer(GameTeam team, Player player) {
        team.addPlayer(player);
        players.add(player);
    }

    public void leave(Player player) {
        if (running) {
            if (getPlayers().size() <= 2) {
                //this is not finished just to make debugging easier
                endGame();
            } else {
                for (Player player1 : getPlayers()) {
                    player1.sendMessage(MessageCreator.prefix("&6" + player.getName() + "&7from Team " + PlayerDataManager.getPlayerData(player).getTeam().getName() + " &cleft&7 the Game"));
                }
                leavePlayer(player);
                sendLeaveMessage(player);
            }
        } else {
            if (getPlayers().size() <= 2) {
                endGame();
            }else {
                leavePlayer(player);
                sendLeaveMessage(player);
            }
        }
    }

    public void endGame() {
            //all this complicated shit to prevent java.util.ConcurrentModificationException
            List<Player> players = new CopyOnWriteArrayList<>();
            for (Player player1 : getPlayers()) {
                players.add(player1);
            }
            //loop through the new List
            for (Player player1 : players) {
                player1.sendMessage(MessageCreator.prefix("&cGame end!"));
                leavePlayer(player1);
                player1.playSound(player1.getLocation(), Sound.ENTITY_WITHER_DEATH, SoundCategory.HOSTILE, 1,1);
            }
            map.setMapState(MapState.WAITING);
            GameManager.finishGame(this);
    }

    private void leavePlayer(Player player) {
        PlayerData playerData = PlayerDataManager.getPlayerData(player);

        players.remove(player);
        playerData.getTeam().getPlayers().remove(player);

        playerData.setMap(null);
        playerData.setGame(null);
        playerData.setTeam(null);
        playerData.setState(PlayerState.LOBBY);
        player.teleport(MLGRush.getLobbySpawn().getTpLocation());
    }

    private void sendLeaveMessage(Player player) {
        player.sendMessage(MessageCreator.prefix("&cYou left the Game!"));
        MessageCreator.sendTitle(player, "&6Game &cleft", "", 50, true);
    }

    public void start() {
        if (!running) {
            joinAllowed = false;
            isStarting = true;
            BoundingBoxActions.replace(Material.SANDSTONE, map.getBoundingBox());
            //show timer for Players in that Game
            countDown(5);
            new BukkitRunnable() {
                @Override
                public void run() {
                    countDown(4);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            countDown(3);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    countDown(2);
                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            countDown(1);
                                        }
                                    }.runTaskLater(MLGRush.getInstance(), 20);
                                }
                            }.runTaskLater(MLGRush.getInstance(), 20);
                        }
                    }.runTaskLater(MLGRush.getInstance(), 20);
                }
            }.runTaskLater(MLGRush.getInstance(), 20);

            //initiate game start after timer is finished
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (Player player : getPlayers()) {
                        //send a title that the game has started
                        MessageCreator.sendTitle(player, "let the &6Games &abegin&f&l!", "&fMap:&6 " + map.getName(), 50, true);
                        player.sendMessage(MessageCreator.prefix("the Game started, playing: &6" + map.getName()));

                        //get the Playerdata of every player
                        PlayerData playerData = PlayerDataManager.getPlayerData(player);
                        //teleport the Player to it's spawn
                        player.teleport(playerData.getTeam().getSpawn().getTpLocation());
                        //give the Player the Game inventory
                        Inventories.loadGameInv(player);
                        //set the game to running
                        running = true;
                        isStarting = false;
                    }

                }
            }.runTaskLater(MLGRush.getInstance(), 100);
        }
    }

    private void countDown(int time) {
        for (Player player : getPlayers()) {
            player.setLevel(time);
            MessageCreator.sendActionbar(player, "Starting in: &6" + time);
            MessageCreator.sendTitle(player, "&6Playing", "&f&l" + time + "  &fplaying: &6&l" + map.getName(), 40, true);
        }
    }

    public boolean isStarting() {
        return isStarting;
    }

    public boolean isRunning() {
        return running;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public gameMap getMap() {
        return map;
    }

    public int getTeamAmount() {
        return teamAmount;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public ArrayList<GameTeam> getTeams() {
        return teams;
    }

    public boolean isFull() {
        return teamSize * teamAmount < players.size();
    }
}
