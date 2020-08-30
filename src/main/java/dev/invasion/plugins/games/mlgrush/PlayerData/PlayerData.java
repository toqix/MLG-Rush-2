package dev.invasion.plugins.games.mlgrush.PlayerData;

import dev.invasion.plugins.games.mlgrush.maps.gameMap;

public class PlayerData {

    private PlayerState state;
    private int page;
    private boolean debugOutput;
    private gameMap map;

    public PlayerData() {
        state = PlayerState.LOBBY;page=0;
    }

    public void setDebugOutput(boolean debugOutput) {
        this.debugOutput = debugOutput;
    }

    public boolean isDebugOutput() {
        return debugOutput;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public PlayerState getState() {
        return state;
    }

    public void setState(PlayerState playerState) {
        this.state = playerState;
    }

    public gameMap getMap() {
        return map;
    }

    public void setMap(gameMap map) {
        this.map = map;
    }
}
