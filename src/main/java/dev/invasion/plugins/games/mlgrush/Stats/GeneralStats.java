package dev.invasion.plugins.games.mlgrush.Stats;

public class GeneralStats {
    private int totalKills;
    private int totalDeaths;
    private int totalPlayedGames;
    private int totalPlayersPlayed;

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