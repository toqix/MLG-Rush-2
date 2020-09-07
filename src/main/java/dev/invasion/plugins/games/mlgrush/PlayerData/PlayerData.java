package dev.invasion.plugins.games.mlgrush.PlayerData;


import dev.invasion.plugins.games.mlgrush.Game.Game;
import dev.invasion.plugins.games.mlgrush.Game.GameTeam;
import dev.invasion.plugins.games.mlgrush.MLGRush;
import dev.invasion.plugins.games.mlgrush.maps.Team;
import dev.invasion.plugins.games.mlgrush.maps.gameMap;
import org.bukkit.entity.Player;

public class PlayerData {

    private PlayerState state;
    private int page;
    private boolean debugOutput;
    private PlayerSettings playerSettings;
    private gameMap map; //is null if player is in Lobby
    //null whilst not in a Game:
    private GameTeam team;
    private Game game;




    public PlayerData(Player player) {
        state = PlayerState.LOBBY;
        page=0;
        playerSettings = MLGRush.getPlayerSettingsManager().getPlayerSettings(player);
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public PlayerSettings getSettings() {
        return playerSettings;
    }

    public void setPlayerSettings(PlayerSettings playerSettings) {
        this.playerSettings = playerSettings;
    }

    public GameTeam getTeam() {
        return team;
    }

    public void setTeam(GameTeam team) {
        this.team = team;
    }

    public void setDebugOutput(boolean debugOutput) {
        this.debugOutput = debugOutput;
    }

    public boolean isDebugOutput() {
        return debugOutput;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public PlayerState getState() {
        return state;
    }

    public void setState(PlayerState playerState) {
        this.state = playerState;
    }

    public gameMap getMap() {
        return map;
    }

    public void setMap(gameMap map) {
        this.map = map;
    }
}
