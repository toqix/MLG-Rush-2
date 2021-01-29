package dev.invasion.plugins.games.mlgrush.Utils.BetterItem;

import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class ItemClickEvent {
    public Player getPlayer() {
        return player;
    }

    public boolean isRightClick() {
        return isRightClick;
    }

    public Block getClickedBlock() {
        return clickedBlock;
    }
    private final Player player;
    private final boolean isRightClick;
    private final BlockFace clickedFace;

    public BlockFace getClickedFace() {
        return clickedFace;
    }

    private final boolean isShiftClick;

    public boolean isShiftClick() {
        return isShiftClick;
    }
    public BlockFace getFace() {
        return clickedFace;
    }

    public ItemClickEvent(Player player, boolean isShiftClick, Block clickedBlock, EventType type, BlockFace face, boolean isRightClick, boolean playSound) {
        this.player = player;
        this.isRightClick = isRightClick;
        this.clickedBlock = clickedBlock;
        this.type = type;
        this.clickedFace = face;
        this.isShiftClick = isShiftClick;
        if(playSound) player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, (float) 1.25);
    }
    private Block clickedBlock;
    private EventType type;
    public EventType getType() {return type;}


}
