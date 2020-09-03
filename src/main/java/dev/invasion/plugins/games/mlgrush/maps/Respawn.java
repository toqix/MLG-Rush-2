package dev.invasion.plugins.games.mlgrush.maps;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Bed;

public class Respawn {

    private SerializableLocation location;
    private BlockFace rotation;

    public Respawn(SerializableLocation location, BlockFace rotation) {
        this.rotation = rotation;
        this.location = location;
    }

    public void setBlock() {
        Block block = location.getBlock().getRelative(rotation);
        for(Bed.Part part : Bed.Part.values()) {
            block.setBlockData(Bukkit.createBlockData(Material.RED_BED, (data) -> {
                ((Bed) data).setPart(part);
                ((Bed) data).setFacing(rotation);
            }));
            block = block.getRelative(rotation.getOppositeFace());
        }
    }

    public SerializableLocation getLocation() {
        return location;
    }

    public BlockFace getRotation() {
        return rotation;
    }
}