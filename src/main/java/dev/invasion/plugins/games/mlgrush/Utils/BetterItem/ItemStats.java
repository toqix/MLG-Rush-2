package dev.invasion.plugins.games.mlgrush.Utils.BetterItem;

public class ItemStats {

    private int interactions;
    private int clicks;
    private int drops;

    public ItemStats() {
        interactions = 0;
        clicks = 0;
        drops = 0;
    }

    public void add_interaction() {
        interactions++;
    }
    public void add_click() {
        clicks++;
    }
    public void add_drop() {
        drops++;
    }

}
