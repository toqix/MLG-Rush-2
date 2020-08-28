package dev.invasion.plugins.games.mlgrush.maps;

import dev.invasion.plugins.games.mlgrush.maps.SerializableLocation;

import java.io.Serializable;

public class BoundingBox implements Serializable {
    private final double x1;
    private final double y1;
    private final double z1;
    private final double x2;
    private final double y2;
    private final double z2;
    public BoundingBox(int x1, int y1, int z1, int x2, int y2, int z2) {
        this.x2 = Math.max(x1, x2);
        this.x1 = Math.min(x1, x2);
        this.y2 = Math.max(y1, y2);
        this.y1 = Math.min(y1, y2);
        this.z2 = Math.max(z1, z2);
        this.z1 = Math.min(z1, z2);
    }
    public BoundingBox(SerializableLocation loc1, SerializableLocation loc2) {

        /*
        ~2 is always bigger than ~1
        */
        double x1 = loc1.getX();
        double x2 = loc2.getX();
        double y1 = loc1.getY();
        double y2 = loc2.getY();
        double z1 = loc1.getZ();
        double z2 = loc2.getZ();
        this.x2 = Math.max(x1, x2);
        this.x1 = Math.min(x1, x2);
        this.y2 = Math.max(y1, y2);
        this.y1 = Math.min(y1, y2);
        this.z2 = Math.max(z1, z2);
        this.z1 = Math.min(z1, z2);
    }

    public BoundingBox(SerializableLocation middle, int radius, int height) {
        /*
        Returns quadratic BoundingBox
         */
        y1 = 0;
        y2 = height;
        x1 = middle.getX() - radius;
        x2 = middle.getX() + radius;
        z1 = middle.getZ() - radius;
        z2 = middle.getZ() + radius;

    }

    public double getX1() {
        return this.x1;
    }
    public double getX2() {
        return this.x2;
    }
    public double getY1() {
        return this.y1;
    }
    public double getY2() {
        return this.y2;
    }
    public double getZ1() {
        return this.z1;
    }
    public double getZ2() {
        return this.z2;
    }
    public SerializableLocation getMiddle() {
        return new SerializableLocation(((x2 - x1) / 2) + x1, 0, ((z2 - z1) / 2) + z1);
    }
    public double getMaxRadiusFromMiddle() {
        return Math.abs(Math.max(((x2 - x1) / 2), ((z2 - z1) / 2))) + 5;
    }

    public boolean isInside(SerializableLocation loc) {
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();
        return ((x1 < x && x < x2) && (y1 < y && y < y2) && (z1 < z && z < z2));
    }

}
