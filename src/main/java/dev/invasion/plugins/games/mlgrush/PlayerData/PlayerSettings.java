package dev.invasion.plugins.games.mlgrush.PlayerData;

import org.bukkit.entity.Player;

public class PlayerSettings {

    private int stick;
    private int pick;
    private int blocks;

    public PlayerSettings() {
        stick = 0;
        pick = 1;
        blocks = 2;
    }

    public int getStick() {
        return stick;
    }

    public void setStick(int stick) {
        this.stick = stick;
    }

    public int getPick() {
        return pick;
    }

    public void setPick(int pick) {
        this.pick = pick;
    }

    public int getBlocks() {
        return blocks;
    }

    public void setBlocks(int blocks) {
        this.blocks = blocks;
    }
}
