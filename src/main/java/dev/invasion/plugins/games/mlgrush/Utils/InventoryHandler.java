package dev.invasion.plugins.games.mlgrush.Utils;

import dev.invasion.plugins.games.mlgrush.BuildMode.BuildMode;
import dev.invasion.plugins.games.mlgrush.BuildMode.BuildModeInvs;
import dev.invasion.plugins.games.mlgrush.BuildMode.BuildModeManager;
import dev.invasion.plugins.games.mlgrush.MLGRush;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerData;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerDataManager;
import dev.invasion.plugins.games.mlgrush.Utils.MessageCreator;

import dev.invasion.plugins.games.mlgrush.maps.MapManager;
import dev.invasion.plugins.games.mlgrush.maps.gameMap;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class InventoryHandler implements Listener {

    //main Inventory handeler

    /*
    Information:
    Please enter your command definitions here:
    b: BuildMode Commands
    e: edit command + map id

    */
    private List<String> others = Arrays.asList("BOW");

    private boolean handleLeftClick(char command, String arguments, Player player, boolean isRightClick, Block clicked) {
        if (isRightClick) {
            if (command == 'r') {
                char commandRightClick = arguments.charAt(0);
                StringBuilder argRight = new StringBuilder();
                IntStream.range(2, arguments.length() - 1).forEachOrdered(n -> argRight.append(arguments.charAt(n)));
                String argsRight = argRight.toString();
                //Bukkit.broadcastMessage("Total: " + arguments + " Command:" + commandRightClick + " args: " + argsRight);
                handleRightClick(commandRightClick, argsRight, player);
            }
        } else {
            switch (command) {
                case 'b':
                    BuildModeManager.handleClick(arguments, player);
                    break;
                case 'e':
                    BuildModeManager.editMap(player, MLGRush.getMapManager().getMap(Integer.parseInt(arguments)));
                    break;
                case 'p':
                    switch (arguments) {
                        case "+":
                            PlayerDataManager.getPlayerData(player).setPage(PlayerDataManager.getPlayerData(player).getPage() + 1);
                            InvOpener.openDelay(player, BuildModeInvs.EditMapMode(player));
                            break;
                        case "-":
                            if (PlayerDataManager.getPlayerData(player).getPage() > 0) {
                                PlayerDataManager.getPlayerData(player).setPage(PlayerDataManager.getPlayerData(player).getPage() - 1);
                                InvOpener.openDelay(player, BuildModeInvs.EditMapMode(player));
                            }
                            break;

                    }
            }
        }
        return true;
    }

    /*
    Information:
    Please enter your command definitions here:


    */

    private void handleRightClick(char command, String arguments, Player player) {
        switch (command) {

        }
    }

    private final List<Action> okAct = new ArrayList<>();

    public InventoryHandler() {
        okAct.add(Action.RIGHT_CLICK_AIR);
        okAct.add(Action.RIGHT_CLICK_BLOCK);
    }

    //static util methods

    public static Inventory createInventory(String name, int size) {
        Inventory inv = Bukkit.createInventory(null, size, MessageCreator.t(name));
        IntStream.range(0, size).forEachOrdered(n -> inv.setItem(n, getNothing()));
        return inv;
    }

    public static Inventory createInventory(String name) {
        return createInventory(name, 45);
    }

    //Main InventoryHandler method
    public static ItemStack createStack(Material material, String name, List<String> lore, String command, String commandRightClick, boolean enchanted, int amount, boolean unmoveable) {
        ItemStack stack = new ItemStack(material, amount);
        ItemMeta stack_meta = stack.getItemMeta();
        assert stack_meta != null;
        stack_meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        List<String> newlore = new java.util.ArrayList<>(Collections.emptyList());
        if (!lore.isEmpty()) {
            for (String i : lore) {
                newlore.add(ChatColor.translateAlternateColorCodes('&', i));
            }
        }
        if (!commandRightClick.equals("")) {
            newlore.add(ChatColor.translateAlternateColorCodes('&', "&0&or(" + commandRightClick + ")"));
        }
        if (!command.equals("")) {
            newlore.add(ChatColor.translateAlternateColorCodes('&', "&0&o" + command));
        }
        if (unmoveable) newlore.add(ChatColor.translateAlternateColorCodes('&', "&0&oMLG-Rush"));
        stack_meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS);
        stack_meta.setLore(newlore);
        stack.setItemMeta(stack_meta);
        if (enchanted) {
            stack.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        }
        return stack;
    }

    //Other StackCreators which access the method above
    public static ItemStack createStack(Material material, String name, List<String> lore, String command, String rightCommand, boolean ench, int amount) {
        return createStack(material, name, lore, command, rightCommand, ench, amount, true);
    }

    public static ItemStack createStack(Material material, String name, List<String> lore, String command, String rightCommand) {
        return createStack(material, name, lore, command, rightCommand, false, 1);
    }

    public static ItemStack createStack(Material material, String name) {
        return createStack(material, name, Collections.emptyList(), "", "", false, 1);
    }


    public static ItemStack createStack(Material material, String name, List<String> lore, String command, String rightCommand, boolean enchanted) {
        return createStack(material, name, lore, command, rightCommand, enchanted, 1);
    }

    public static ItemStack createStack(Material material, String name, List<String> lore, String command, boolean enchanted) {
        return createStack(material, name, lore, command, "", enchanted, 1);
    }

    public static ItemStack createStack(Material material, String name, String command) {
        return createStack(material, name, Collections.emptyList(), command, "", false, 1);
    }

    public static ItemStack createStack(Material material, String name, List<String> lore, boolean enchanted) {
        return createStack(material, name, lore, "", "", enchanted, 1);
    }

    public static ItemStack createStack(Material material, String name, List<String> lore, String command) {
        return createStack(material, name, lore, command, "", false, 1);
    }

    public static ItemStack createStack(Material material, String name, List<String> lore, String command, int amount) {
        return createStack(material, name, lore, command, "", false, amount);
    }

    public static ItemStack createStack(Material material, String name, List<String> lore) {
        return createStack(material, name, lore, "", "", false, 1);
    }

    public static ItemStack createStack(Material material, String name, List<String> lore, boolean ench, boolean moveable) {
        return createStack(material, name, lore, "", "", ench, 1, moveable);
    }

    public static ItemStack getNothing() {
        ItemStack nothing = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta nothing_meta = nothing.getItemMeta();
        assert nothing_meta != null;
        nothing_meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', " "));
        nothing_meta.setLore(Collections.singletonList(ChatColor.translateAlternateColorCodes('&', "&0&oMLG-Rush")));
        nothing.setItemMeta(nothing_meta);
        return nothing;
    }

    //actual handlers

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack clicked = event.getCurrentItem();
        if (clicked != null) {
            if (clicked.getType() != Material.AIR) {
                if (clicked.hasItemMeta()) {
                    ItemMeta meta = clicked.getItemMeta();
                    if (meta != null) {
                        if (meta.hasLore()) {
                            List<String> lore = meta.getLore();
                            if (lore != null) {
                                if (lore.size() > 0) {

                                    if (ChatColor.stripColor(lore.get(lore.size() - 1)).equals("MLG-Rush")) {
                                        event.setCancelled(true);
                                    }
                                    if (lore.size() > 1) {
                                        String commandraw = ChatColor.stripColor(lore.get(lore.size() - 2));
                                        HumanEntity player = event.getWhoClicked();
                                        Player playerP = Bukkit.getPlayer(player.getName());
                                        if (playerP != null) {
                                            playerP.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 2);
                                        }
                                        if (commandraw.length() > 0) {
                                            char command = commandraw.charAt(0);
                                            StringBuilder arg = new StringBuilder();
                                            IntStream.range(2, commandraw.length() - 1).forEachOrdered(n -> arg.append(commandraw.charAt(n)));
                                            //Bukkit.broadcastMessage("Command: " + command + " Action: " + arg);
                                            String args = arg.toString();
                                            if (event.getAction() != InventoryAction.PICKUP_HALF) {
                                                handleLeftClick(command, args, playerP, false, null);
                                            } else {
                                                if (lore.size() > 2) {
                                                    String commandrawRight = ChatColor.stripColor(lore.get(lore.size() - 3));
                                                    char commandRight = commandrawRight.charAt(0);
                                                    StringBuilder argRight = new StringBuilder();
                                                    IntStream.range(2, commandrawRight.length() - 1).forEachOrdered(n -> argRight.append(commandrawRight.charAt(n)));
                                                    //Bukkit.broadcastMessage("Command: " + command + " Action: " + arg);
                                                    String argsRight = argRight.toString();
                                                    //Bukkit.broadcastMessage("Command: " + commandRight + " Args: " + argsRight);
                                                    event.setCancelled(handleLeftClick(commandRight, argsRight, playerP, true, null));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        ItemStack clicked = event.getCursor();
        if (clicked != null) {
            if (clicked.getType() != Material.AIR) {
                if (clicked.hasItemMeta()) {
                    ItemMeta meta = clicked.getItemMeta();
                    if (meta != null) {
                        if (meta.hasLore()) {
                            List<String> lore = meta.getLore();
                            if (lore != null) {
                                if (lore.size() > 0) {

                                    if (ChatColor.stripColor(lore.get(lore.size() - 1)).equals("MLG-Rush")) {
                                        event.setCancelled(true);
                                    }
                                    if (lore.size() > 1) {
                                        String commandraw = ChatColor.stripColor(lore.get(lore.size() - 2));
                                        HumanEntity player = event.getWhoClicked();
                                        Player playerP = Bukkit.getPlayer(player.getName());
                                        if (playerP != null) {
                                            playerP.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 2);
                                        }
                                        if (commandraw.length() > 0) {
                                            char command = commandraw.charAt(0);
                                            StringBuilder arg = new StringBuilder();
                                            IntStream.range(2, commandraw.length() - 1).forEachOrdered(n -> arg.append(commandraw.charAt(n)));
                                            //Bukkit.broadcastMessage("Command: " + command + " Action: " + arg);
                                            String args = arg.toString();
                                            event.setCancelled(handleLeftClick(command, args, playerP, false, null));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        ItemStack clicked = event.getItem();
        Action action = event.getAction();
        if (okAct.contains(action)) {
            if (clicked != null) {
                if (clicked.getType() != Material.AIR) {
                    if (clicked.hasItemMeta()) {
                        ItemMeta meta = clicked.getItemMeta();
                        if (meta != null) {
                            if (meta.hasLore()) {
                                List<String> lore = meta.getLore();
                                if (lore != null) {
                                    if (lore.size() > 0) {

                                        if (ChatColor.stripColor(lore.get(lore.size() - 1)).equals("MLG-Rush")) {
                                            event.setCancelled(true);
                                        } else {
                                            return;
                                        }
                                        if (lore.size() > 1) {
                                            String commandraw = ChatColor.stripColor(lore.get(lore.size() - 2));
                                            HumanEntity player = event.getPlayer();
                                            Player playerP = Bukkit.getPlayer(player.getName());
                                            if (playerP != null) {
                                                playerP.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 2);
                                            }
                                            if (commandraw.length() > 0) {
                                                char command = commandraw.charAt(0);
                                                StringBuilder arg = new StringBuilder();
                                                IntStream.range(2, commandraw.length() - 1).forEachOrdered(n -> arg.append(commandraw.charAt(n)));
                                                //Bukkit.broadcastMessage("Command: " + command + " Action: " + arg);
                                                Block relative_block = null;
                                                String args = arg.toString();
                                                Block clicked_block = event.getClickedBlock();
                                                if (clicked_block != null && event.getBlockFace() != null) {
                                                    relative_block = clicked_block.getRelative(event.getBlockFace(), 1);
                                                } else {
                                                    relative_block = event.getClickedBlock();
                                                }
                                                event.setCancelled(handleLeftClick(command, args, playerP, false, relative_block));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        ItemStack clicked = event.getItemDrop().getItemStack();
        if (clicked.getType() != Material.AIR) {
            if (clicked.hasItemMeta()) {
                ItemMeta meta = clicked.getItemMeta();
                if (meta != null) {
                    if (meta.hasLore()) {
                        List<String> lore = meta.getLore();
                        if (lore != null) {
                            if (lore.size() > 0) {

                                if (ChatColor.stripColor(lore.get(lore.size() - 1)).equals("MLG-Rush")) {
                                    event.setCancelled(true);
                                }
                                if (lore.size() > 1) {
                                    String commandraw = ChatColor.stripColor(lore.get(lore.size() - 2));
                                    HumanEntity player = event.getPlayer();
                                    Player playerP = Bukkit.getPlayer(player.getName());
                                    if (playerP != null) {
                                        playerP.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 2);
                                    }
                                    if (commandraw.length() > 0) {
                                        char command = commandraw.charAt(0);
                                        StringBuilder arg = new StringBuilder();
                                        IntStream.range(2, commandraw.length() - 1).forEachOrdered(n -> arg.append(commandraw.charAt(n)));
                                        //Bukkit.broadcastMessage("Command: " + command + " Action: " + arg);
                                        String args = arg.toString();
                                        event.setCancelled(handleLeftClick(command, args, playerP, false, null));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
