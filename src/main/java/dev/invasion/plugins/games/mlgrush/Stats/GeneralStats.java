package dev.invasion.plugins.games.mlgrush.Stats;

import org.bukkit.Bukkit;

public class GeneralStats {
    private int totalKills;
    private int totalDeaths;
    private int totalPlayedGames;
    private int totalPlayersPlayed;
    private int totalMaps;
    private int totalBeds;

    public void addTotalMap() {
        totalMaps++;
    }
    public void addTotalKills() {
        totalKills++;
    }
    public void addTotalPlayedGame() {
        totalPlayedGames++;
    }
    public void addTotalPlayerPlayed() {
        totalPlayersPlayed++;
    }
    public void addTotalDeath() {
        totalDeaths++;
    }
    public void addTotalBed() {
        totalBeds++;
    }

    public int getTotalBeds() {
        return totalBeds;
    }

    public int getTotalMaps() {
        return totalMaps;
    }

    public void setTotalMaps(int totalMaps) {
        this.totalMaps = totalMaps;
    }

    public int getTotalKills() {
        return totalKills;
    }

    public void setTotalKills(int totalKills) {
        this.totalKills = totalKills;
    }

    public int getTotalDeaths() {
        return totalDeaths;
    }

    public void setTotalDeaths(int totalDeaths) {
        this.totalDeaths = totalDeaths;
    }

    public int getTotalPlayedGames() {
        return totalPlayedGames;
    }

    public void setTotalPlayedGames(int totalPlayedGames) {
        this.totalPlayedGames = totalPlayedGames;
    }

    public int getTotalPlayersPlayed() {
        return totalPlayersPlayed;
    }

    public void setTotalPlayersPlayed(int totalPlayersPlayed) {
        this.totalPlayersPlayed = totalPlayersPlayed;
    }
}
