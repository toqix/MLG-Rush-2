package dev.invasion.plugins.games.mlgrush.Utils;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

public class Inventories {
    public static Inventory BuildMode() {
        Inventory inv = InventoryHandler.createInventory("&6Build&7Mode");

        inv.setItem(20, InventoryHandler.createStack(Material.SANDSTONE, "&cCreate&7 a new Map", Arrays.asList("&7Start Building a brand new Map"), true));

        return inv;
    }
}
