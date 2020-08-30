package dev.invasion.plugins.games.mlgrush.maps;

import java.util.UUID;

public class gameMap {

    private SerializableLocation specSpawn;



    private BoundingBox box;
    private String name;
    private String builder;
    private TeamManager teamManager;
    private boolean isAvailable;
    private boolean isFinished;
    private MapState mapState;

    public gameMap(SerializableLocation specSpawn, BoundingBox box, String name) {
        this.specSpawn = specSpawn;
        isAvailable = true;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MapState getMapState() {
        return mapState;
    }

    public void setMapState(MapState mapState) {
        this.mapState = mapState;
    }
}