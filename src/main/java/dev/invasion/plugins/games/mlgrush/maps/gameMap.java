package dev.invasion.plugins.games.mlgrush.maps;

import java.util.UUID;

public class gameMap {

    private SerializableLocation specSpawn;



    private BoundingBox box;



    private String builder;
    private TeamManager teamManager;
    private boolean isAvailable;
    private boolean isFinished;

    public gameMap(SerializableLocation specSpawn, BoundingBox box) {
        this.specSpawn = specSpawn;
        isAvailable = true;
        this.box = box;
        this.teamManager = new TeamManager();
        builder = "Unknown";
    }

    public SerializableLocation getSpecSpawn() {
        return specSpawn;
    }

    public BoundingBox getBoundingBox() {
        return box;
    }

    public String getBuilder() {
        return builder;
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public void setBuilder(String builder) {
        this.builder = builder;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public boolean available() {
        return isAvailable;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public boolean finished() {
        return isFinished;
    }
}