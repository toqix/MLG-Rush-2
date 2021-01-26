package dev.invasion.plugins.games.mlgrush.Game;

import dev.invasion.plugins.games.mlgrush.maps.SerializableLocation;
import dev.invasion.plugins.games.mlgrush.maps.Team;
import dev.invasion.plugins.games.mlgrush.maps.TeamColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class GameTeam extends Team {

    private int score;
    private ArrayList<Player> players = new ArrayList<> ();

    public GameTeam(TeamColor color) {
        super(color);
    }

    public GameTeam(Team team) {
        super(team.getColor());
        score = 0;
        setBed(team.getBed());
        setSpawn(team.getSpawn());
    }



    public int getScore() {
        return score;
    }
    public int playerCount() {
        return players.size();
    }

    public void addScore() {
        this.score++;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }
}
