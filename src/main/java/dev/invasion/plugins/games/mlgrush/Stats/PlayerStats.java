package dev.invasion.plugins.games.mlgrush.Stats;

public class PlayerStats {
    private int kills;
    private int deaths;
    private int beds;
    private int looses;
    private int wins;

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setBeds(int beds) {
        this.beds = beds;
    }

    public void setLooses(int looses) {
        this.looses = looses;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getBeds() {
        return beds;
    }

    public int getLooses() {
        return looses;
    }

    public int getWins() {
        return wins;
    }

}
