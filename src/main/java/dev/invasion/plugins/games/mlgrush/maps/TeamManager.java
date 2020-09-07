package dev.invasion.plugins.games.mlgrush.maps;

import java.util.ArrayList;

public class TeamManager {

    private ArrayList<Team> teams;
    private int teamSize;

    public TeamManager() {
        teamSize = 1;
        teams = new ArrayList<>();
        teams.add(new Team(TeamColor.RED));
        teams.add(new Team(TeamColor.BLUE));
    }


    public void setTeams(int amount) {
        if (amount <= 4 && amount >= 2) {
            teams.clear();
            for (int i = 1; i <= amount; i++) {
                TeamColor col;
                switch (i) {
                    case 1:
                        col = TeamColor.BLUE;
                        break;
                    case 2:
                        col = TeamColor.RED;
                        break;
                    case 3:
                        col = TeamColor.GREEN;
                        break;
                    case 4:
                        col = TeamColor.YELLOW;
                        break;
                    default:
                        col = null;
                        break;
                }
                teams.add(new Team(col));
            }
        }
    }

    public void addTeam(Team team) {
        teams.add(team);
    }

    public void addTeam() {
        if(teams.size() == 3) {
            teams.add(new Team(TeamColor.YELLOW));
        }else if(teams.size() == 2) {
            teams.add(new Team(TeamColor.GREEN));
        }
    }
    public void removeTeam() {
        if(teams.size() > 2) {
            teams.remove(teams.size()-1);
        }
    }

    public int getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(int teamSize) {
        if(teamSize >= 1) {
            this.teamSize = teamSize;
        }
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }


    public Team getTeam(TeamColor color) {
        Team toReturn = null;
        for (Team team : getTeams()) {
            if (team.getColor() == color) {
                toReturn = team;
            }
        }
        return toReturn;
    }

    public Team getTeam(int id) {
        if (id >= 0 && id < getTeams().size()) {
            return getTeams().get(id);
        } else {
            return null;
        }
    }
}