package dev.invasion.plugins.games.mlgrush.Utils;

import dev.invasion.plugins.games.mlgrush.Game.Game;
import dev.invasion.plugins.games.mlgrush.Game.GameManager;
import dev.invasion.plugins.games.mlgrush.MLGRush;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerData;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerDataManager;
import dev.invasion.plugins.games.mlgrush.Stats.StatsManager;
import dev.invasion.plugins.games.mlgrush.Utils.BetterItem.BetterItem;
import dev.invasion.plugins.games.mlgrush.Utils.BetterItem.BetterItemDescription;
import dev.invasion.plugins.games.mlgrush.Utils.BetterItem.EventType;
import dev.invasion.plugins.games.mlgrush.maps.MapManager;
import dev.invasion.plugins.games.mlgrush.maps.MapState;
import dev.invasion.plugins.games.mlgrush.maps.gameMap;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Inventories {

    public static Inventory SpectateFindPlayerInv(Game game) {
        Inventory inv = InventoryHandler.createInventory("Teleport to");

        for(int i = 9; i < 45; i++) {
            inv.setItem(i, new ItemStack(Material.AIR));
        }
        int count = 0;
        for(Player player: game.getPlayers()) {
            count++;
            inv.setItem(count+9, new BetterItem((itemClickEvent -> {
                Player user = itemClickEvent.getPlayer();
                user.teleport(player.getLocation().clone().add(0, 3, 0));
                return true;
            }), Material.PLAYER_HEAD)
                    .setGlint(true)
                    .setName("&6" + player.getName())
                    .create(new BetterItemDescription("Teleport", "Statistics", Collections.singletonList("Teleports you to " + player.getName())))

            );
        }

        return inv;
    }

    public static Inventory SpectateGameInv() {
        Inventory inv = InventoryHandler.createInventory("Choose a §6Game §7to spectate");

        for (int i = 9; i < 36; i++) {
            inv.setItem(i, new ItemStack(Material.AIR));
        }

        for (int i = 0; i < 27; i++) {
            int slot = i + 9;
            if (GameManager.getGames().size() > i) {
                Game game = GameManager.getGames().get(i);
                if (game.isRunning()) {
                    ArrayList<String> playerNames = new ArrayList<>();
                    playerNames.add("&7&lPlayers");
                    for(Player player : game.getPlayers()) {
                        playerNames.add("&7" + player.getName());
                    }
                    inv.setItem(slot, InventoryHandler.createStack(Material.SANDSTONE, "Spectate Game " + i, playerNames, "v(" + game.getMap().getId() +")"));
                }else {
                    inv.setItem(slot, InventoryHandler.createStack(Material.COBBLESTONE, "&7Game has &cnot &7started yet"));
                }
            }
        }

        return inv;
    }

    public static Inventory CreateMap(Player player) {
        Inventory inv = InventoryHandler.createInventory("Choose a §6Map");

        StatsManager stats = MLGRush.getStatsManager();
        inv.setItem(0, InventoryHandler.createStack(Material.BOOK, "&6Stats", Arrays.asList("&7&fTotal Maps Created: &6" + stats.getGeneralStats().getTotalMaps(), "&7&fFritz ist gaga"), true));

        for (int i = 9; i < 36; i++) {
            inv.setItem(i, new ItemStack(Material.AIR));
        }
        for (int i = 0; i < 27; i++) {
            int slot = 9 + i;
            int mapId = PlayerDataManager.getPlayerData(player).getPage() * 27 + i;

            gameMap map = MLGRush.getMapManager().getMap(mapId);
            if (map != null) {
                if (map.getMapState() == MapState.GAME) {
                    inv.setItem(slot, InventoryHandler.createStack(Material.BARRIER, "&6" + map.getName(), Arrays.asList("&cThere's already an &aactive &c Game", "&con this Map")));
                } else if (map.getMapState() == MapState.BUILD) {
                    inv.setItem(slot, InventoryHandler.createStack(Material.BARRIER, "&6" + map.getName(), Arrays.asList("&cThis map is currently being", "&6edited &cplease try again later")));
                } else if (map.getMapState() == MapState.WAITING) {
                    if (map.finished()) {
                        inv.setItem(slot, InventoryHandler.createStack(Material.SANDSTONE, "&6" + map.getName(), Arrays.asList("&a&lStart&7 a &6Game", "&7on this nice Map"), "c(" + mapId + ")"));
                    } else {
                        inv.setItem(slot, InventoryHandler.createStack(Material.COBBLESTONE, "&6" + map.getName(), Arrays.asList("&7This map is &cunfinished", "&7go into BuildMode to finish it!")));
                    }
                }
            }

        }
        inv.setItem(36, InventoryHandler.createStack(Material.END_CRYSTAL, "&7Previous Page", Collections.singletonList("&7Current Page: " + PlayerDataManager.getPlayerData(player).getPage()), "p(--)", false));
        inv.setItem(44, InventoryHandler.createStack(Material.END_CRYSTAL, "&7Next Page", Collections.singletonList("&7Current Page: " + PlayerDataManager.getPlayerData(player).getPage()), "p(++)", false));

        return inv;
    }
    public static Inventory joinGameInv() {
        Inventory inv = InventoryHandler.createInventory("Choos a Game tp §6join§/spectate");

        return inv;
    }

    public static void loadGameInv(Player player) {
        player.getInventory().clear();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        //load the spot for each item
        int stickSlot = playerData.getSettings().getStick();
        int pickSlot = playerData.getSettings().getPick();
        int blocksSlot = playerData.getSettings().getBlocks();


        //create the blocks
        ItemStack blocks = new ItemStack(Material.SANDSTONE, 64);
        ItemMeta blocks_meta = blocks.getItemMeta();

        assert blocks_meta != null;
        blocks_meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&5Blocks"));
        blocks_meta.setLore(Collections.singletonList(ChatColor.translateAlternateColorCodes('&', "&7Use this to bridge around the Map")));
        blocks.setItemMeta(blocks_meta);


        //create the knock back stick
        ItemStack stick = new ItemStack(Material.STICK);
        ItemMeta stick_meta = stick.getItemMeta();

        assert stick_meta != null;
        stick_meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&4KnockBack-Stick"));
        stick_meta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', "&7Hit your enemys with this Stick")));
        // stick_meta.getItemFlags().add(ItemFlag.HIDE_ENCHANTS);
        // stick_meta.getItemFlags().add(ItemFlag.HIDE_ATTRIBUTES);
        stick.setItemMeta(stick_meta);
        stick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);


        //create the Pickaxt!
        ItemStack pick = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta pick_meta = pick.getItemMeta();

        assert pick_meta != null;
       // pick_meta.getItemFlags().add(ItemFlag.HIDE_ENCHANTS);
        pick_meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&4Picke"));
        pick_meta.setLore(Arrays.asList( ChatColor.translateAlternateColorCodes('&', "&7Use this great tool to break Blocks")));

        pick.setItemMeta(pick_meta);
        pick.addEnchantment(Enchantment.DIG_SPEED, 2);
        pick.addEnchantment(Enchantment.DURABILITY, 3);

        player.getInventory().setItem(stickSlot, stick);
        player.getInventory().setItem(pickSlot, pick);
        player.getInventory().setItem(blocksSlot, blocks);

    }

    public static void saveGameInv(Player player) {

        PlayerData playerData = PlayerDataManager.getPlayerData(player);

        int stickSlot = 0;
        int pickSlot = 1;
        int blocksSlot = 2;
        int sucess = 0;

        Inventory inv = player.getInventory();
        for (int id = 0; id <= 8; id++) {
            ItemStack item = inv.getItem(id);
            if(item != null) {
                if(item.getType() == Material.STICK) {
                    stickSlot = id;
                    sucess++;
                }else if(item.getType() == Material.WOODEN_PICKAXE) {
                    pickSlot = id;
                    sucess++;
                }else if(item.getType() == Material.SANDSTONE) {
                    blocksSlot = id;
                    sucess++;
                }
            }
        }
        if(sucess == 3) {
            player.sendMessage(MessageCreator.prefix("Your Inventory was saved &asuccessfully"));
            playerData.getSettings().setStick(stickSlot);
            playerData.getSettings().setPick(pickSlot);
            playerData.getSettings().setBlocks(blocksSlot);
        }else {
            player.sendMessage(MessageCreator.prefix("&cOops something went wrong Inventory couldn't be saved completely!"));
        }
    }
}
