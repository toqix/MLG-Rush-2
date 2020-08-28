package dev.invasion.plugins.games.mlgrush.PlayerData;

public class PlayerData {

    private PlayerState state;
    private int page;
    private boolean debugOutput;

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

}
