package dev.invasion.plugins.games.mlgrush.maps;
import java.util.ArrayList;

public class TeamManager {

    private ArrayList<Team> teams;
    private int teamSize;

    public TeamManager(){
        teamSize = 1;
        teams = new ArrayList<>();
        teams.add(new Team(TeamColor.RED));
        teams.add(new Team(TeamColor.BLUE));
    }

    public void setTeams(int amount) {
        teams.clear();
        for(int i = 1;i<=amount; i++) {
            TeamColor col;
            switch (i) {
                case 1: col = TeamColor.BLUE; break;
                case 2: col = TeamColor.RED; break;
                case 3: col = TeamColor.GREEN; break;
                case 4: col = TeamColor.YELLOW; break;
                default: col = null;break;
            }
            teams.add(new Team(col));
        }
    }
}