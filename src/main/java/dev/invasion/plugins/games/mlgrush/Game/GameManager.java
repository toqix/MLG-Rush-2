package dev.invasion.plugins.games.mlgrush.Game;

import dev.invasion.plugins.games.mlgrush.MLGRush;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerData;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerDataManager;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerState;
import dev.invasion.plugins.games.mlgrush.Stats.MapStats;
import dev.invasion.plugins.games.mlgrush.maps.gameMap;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class GameManager {
    private static ArrayList<Game> games = new ArrayList<>();

    public static ArrayList<Game> getGames() {
        return games;
    }

    public static Game createGame(Player player, gameMap map) {
        if (map.finished()) {
            Game game = new Game(player, map);
            MLGRush.getStatsManager().getGeneralStats().addTotalPlayedGame();
            MapStats mapStats = MLGRush.getStatsManager().getMapStats(game.getMap());
            mapStats.addPlayedGames();

            games.add(game);
            return game;
        }
        return null;
    }

    public static void joinGame(Player player, Game game) {
        if (!game.join(player)) {
            //game is full or already running
        }
    }

    public static void joinGame(Player player, gameMap map) {
        for (Game game : getGames()) {
            if (game.getMap() == map) {
                game.join(player);
                return;
            }
        }
    }

    public static void finishGame(Game game) {
        for (Player player : game.getPlayers()) {
            leaveGame(player);
        }
        games.remove(game);
    }

    public static void leaveGame(Player player) {
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData.getState() == PlayerState.GAME) {
            Game game = playerData.getGame();
            game.leave(player);
        }
    }


    public static ArrayList<Player> getAllPlayers() {
        ArrayList<Player> toReturn = new ArrayList<>();
        for (Game game : getGames()) {
            toReturn.addAll(game.getPlayers());
        }
        return toReturn;
    }


}
