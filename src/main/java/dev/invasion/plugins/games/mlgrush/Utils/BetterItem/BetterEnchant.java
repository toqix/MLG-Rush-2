package dev.invasion.plugins.games.mlgrush.Utils.BetterItem;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

public class BetterEnchant {
    public int getLevel() {
        return level;
    }
    public String getRawEnchant() {
        return enchant;
    }

    public Enchantment getEnchant() {
        return Enchantment.getByKey(NamespacedKey.minecraft(enchant));
    }
    private int level;
    private String enchant;
    public BetterEnchant(int lv, String ench) {
        level = lv;
        enchant = ench;
    }
}
