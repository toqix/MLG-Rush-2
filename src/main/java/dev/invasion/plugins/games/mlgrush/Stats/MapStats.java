package dev.invasion.plugins.games.mlgrush.Stats;

public class MapStats {

    private long dateCreated;
    private int playedGames;
    private int totalKills;
    private int totalDeaths;
    private int totalWins;
    private int totalLooses;
    private int totalBed;


    public int getTotalBed() {
        return totalBed;
    }
    public void addTotalBed() {
        totalBed++;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void addPlayedGames() {
        playedGames++;
    }
    public void addTotalKill() {
        totalKills++;
    }
    public void addTotalDeaths() {
        totalDeaths++;
    }


    public int getPlayedGames() {
        return playedGames;
    }

    public void setPlayedGames(int playedGames) {
        this.playedGames = playedGames;
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

    public int getTotalWins() {
        return totalWins;
    }

    public void setTotalWins(int totalWins) {
        this.totalWins = totalWins;
    }

    public int getTotalLooses() {
        return totalLooses;
    }

    public void setTotalLooses(int totalLooses) {
        this.totalLooses = totalLooses;
    }
}
