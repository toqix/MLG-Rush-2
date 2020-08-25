package dev.invasion.plugins.games.mlgrush.maps;

import java.util.UUID;

public class gameMap {

    private SerializableLocation secSpawn;
    private BoundingBox box;
    private String builder;
    private TeamManager teamManager;

    public gameMap(SerializableLocation specSpawn, BoundingBox box) {
        this.secSpawn = specSpawn;
        this.box = box;
    }

    public void setBuilder(String builder) {
        this.builder = builder;
    }

    public void setTeamManager() {
        teamManager = new TeamManager();
    }
}

