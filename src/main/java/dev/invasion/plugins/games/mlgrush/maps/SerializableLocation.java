package dev.invasion.plugins.games.mlgrush.maps;

import dev.invasion.plugins.games.mlgrush.MLGRush;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;


public class SerializableLocation {

    private double x;
    private double y;
    private double z;
    private int yaw = 0;
    private int pitch = 0;

    public SerializableLocation(Location loc) {
        x = loc.getX();
        y = loc.getY();
        z = loc.getZ();
    }
    public SerializableLocation(Player ploc) {
        Location loc = ploc.getLocation();
        x = loc.getX();
        y = loc.getY();
        z = loc.getZ();
        yaw = Math.round(loc.getYaw());
        pitch = Math.round(loc.getPitch());
    }


    public SerializableLocation(int xc, int yc,  int zc) {
        x = xc;
        y = yc;
        z = zc;
    }
    public SerializableLocation(int xc, int yc, int zc, int ya, int pi) {
        x = xc;
        y = yc;
        z = zc;
        yaw = ya;
        pitch = pi;
    }

    public SerializableLocation(double v, double v1, double v2) {
        x = v;
        y = v1;
        z = v2;
    }
    public SerializableLocation(double v, double v1, double v2, int ya, int pi) {
        x = v;
        y = v1;
        z = v2;
        yaw = ya;
        pitch = pi;
    }
    public SerializableLocation(Block block) {
        x = block.getX();
        y = block.getY();
        z = block.getZ();
    }

    public boolean isTpAble() {
        return (getBlock().getType() != Material.AIR && getCopy().add(0, 1, 0).getBlock().getType() != Material.AIR);
    }
    public SerializableLocation add(double xp, double yp, double zp) {
        x = x + xp;
        y = y + yp;
        z = z + zp;
        return this;
    }

    public int getPitch() {
        return pitch;
    }

    public int getYaw() {
        return yaw;
    }

    public SerializableLocation setPitch(int pitch) {
        this.pitch = pitch;
        return this;
    }

    public SerializableLocation setYaw(int yaw) {
        this.yaw = yaw;
        return this;
    }

    public SerializableLocation setZ(int z) {
        this.z = z;
        return this;
    }
    public void teleport(Player player) {
        player.teleport(getTpLocation());
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public SerializableLocation setY(int y) {
        this.y = y; return this;
    }

    public SerializableLocation setX(int x) {
        this.x = x;return this;
    }
    public SerializableLocation setY(double y) {
        this.y = y;return this;
    }

    public SerializableLocation setX(double x) {
        this.x = x;return this;
    }

    public SerializableLocation setZ(double z) { this.z = z; return this;}
    public Location getLocationYP() {
        return new Location(MLGRush.getWorld(), x, y, z, yaw, pitch);
    }
    public Location getLocation() {
        return new Location(MLGRush.getWorld(), x, y, z);
    }
    public SerializableLocation setLocation(Location loc) {
        x = (int) loc.getX();
        y = (int) loc.getY();
        z = (int) loc.getZ();
        return this;
    }
    public Location getTpLocation() {
        return new Location(MLGRush.getWorld(), Math.round(x) + 0.500, Math.round(y), Math.round(z) + 0.500, Math.round(yaw), Math.round(pitch));
    }
    public boolean equals(SerializableLocation loc) {
        return (x == loc.getX() && y == loc.getY() && z == loc.getZ());
    }
    public SerializableLocation getCopy() {
        return new SerializableLocation(x, y, z, yaw, pitch);
    }
    public Block getBlock() {
        return MLGRush.getWorld().getBlockAt(getLocation());
    }

    public boolean compare(SerializableLocation to) { return (to.getX() == x && to.getY() == y && to.getZ() == z); }
}
