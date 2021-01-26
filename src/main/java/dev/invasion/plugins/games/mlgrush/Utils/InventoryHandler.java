package dev.invasion.plugins.games.mlgrush.Utils;

import com.mojang.brigadier.Message;
import dev.invasion.plugins.games.mlgrush.BuildMode.BuildMode;
import dev.invasion.plugins.games.mlgrush.BuildMode.BuildModeInvs;
import dev.invasion.plugins.games.mlgrush.BuildMode.BuildModeManager;
import dev.invasion.plugins.games.mlgrush.Game.GameManager;
import dev.invasion.plugins.games.mlgrush.MLGRush;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerData;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerDataManager;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerState;
import dev.invasion.plugins.games.mlgrush.Utils.MessageCreator;

import dev.invasion.plugins.games.mlgrush.maps.*;
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
import org.bukkit.util.Vector;

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
    s: spawnpoints
    z: beds
    p: switch pages
    c: create a game + map id
    d: set direction
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
                        case "++":
                            PlayerDataManager.getPlayerData(player).setPage(PlayerDataManager.getPlayerData(player).getPage() + 1);
                            InvOpener.openDelay(player, Inventories.CreateMap(player));
                            break;
                        case "--":
                            if (PlayerDataManager.getPlayerData(player).getPage() > 0) {
                                PlayerDataManager.getPlayerData(player).setPage(PlayerDataManager.getPlayerData(player).getPage() - 1);
                                InvOpener.openDelay(player, Inventories.CreateMap(player));
                            }
                            break;
                    }
                case 'z':
                    if (PlayerDataManager.getPlayerData(player).getState() == PlayerState.BUILD) {
                        TeamColor color = TeamColor.valueOf(arguments);
                        if (clicked == null) {
                            Team team = PlayerDataManager.getPlayerData(player).getMap().getTeamManager().getTeam(color);
                            InvOpener.closeDelay(player);
                            player.getInventory().setItem(0, InventoryHandler.createStack(Material.RED_BED, "&7Team " + team.getName(), Arrays.asList("&7Set the Bed from", team.getName()), "z(" + team.getColor().name() + ")"));
                            return true;
                        }
                        gameMap map = PlayerDataManager.getPlayerData(player).getMap();
                        Respawn bed = map.getTeamManager().getTeam(color).getBed();
                        if (bed != null) {
                            Block block = bed.getLocation().getBlock();
                            block.setType(Material.AIR);
                            block.getRelative(bed.getRotation()).setType(Material.AIR);
                        }
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                BlockFace facing = null;
                                for (BlockFace face : BlockFace.values()) {
                                    if (face == BlockFace.SELF)
                                        continue;
                                    if (clicked.getRelative(face, 1).getType().toString().endsWith("BED")) {
                                        facing = face;
                                        break;
                                    }
                                }
                                if (facing == null) {
                                    player.sendMessage(MessageCreator.t("&7[&aBuildMode&7] Error: could not find the correct direction, make sure no beds are nearby!"));
                                    MessageCreator.sendTitle(player, "&cFailure!", "&7Please look in the chat", 75);
                                } else {
                                    map.getTeamManager().getTeam(color).setBed(new Respawn(new SerializableLocation(clicked), facing));
                                    clicked.setType(Material.AIR);
                                    clicked.getRelative(facing).setType(Material.AIR);
                                    MessageCreator.sendTitle(player, "&6Bed set", "&asuccessfully", 40, true);
                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            map.getTeamManager().getTeam(color).getBed().setBlock();
                                            player.getInventory().setItem(0, new ItemStack(Material.AIR));
                                            InvOpener.openDelay(player, BuildModeInvs.TeamsInv(map));
                                        }
                                    }.runTaskLater(MLGRush.getInstance(), 5);
                                }
                            }
                        }.runTaskLater(MLGRush.getInstance(), 1);
                        return false;

                    }
                case 's':
                    PlayerData playerData = PlayerDataManager.getPlayerData(player);
                    if (playerData.getState() == PlayerState.BUILD) {
                        TeamColor color = TeamColor.valueOf(arguments);
                        Team team = playerData.getMap().getTeamManager().getTeam(color);
                        if (clicked == null) {
                            InvOpener.closeDelay(player);
                            player.getInventory().setItem(0, InventoryHandler.createStack(Material.NETHER_STAR, "&7Team " + team.getName() + " &7Spawn", Arrays.asList("&7Set the Spawn from", team.getName()), "s(" + team.getColor().name() + ")"));
                            return true;
                        }
                        SerializableLocation spawn = new SerializableLocation(clicked);
                        //get direction is still missing

                        team.setSpawn(spawn);
                        player.getInventory().setItem(0, new ItemStack(Material.AIR));
                        player.sendMessage(MessageCreator.prefix("MLG-Rush-Build", "&7Spawnpoint of Team " + team.getName() + "&7 successfully set"));
                        MessageCreator.sendTitle(player, team.getName(), "&6Spawnpoint set", 50, true);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                player.teleport(team.getSpawn().getTpLocation());
                                MessageCreator.sendTitle(player, "Set direction", "look to the opposite bed", 30);
                                player.sendMessage(MessageCreator.prefix("MLG-Rush-Build", "&7Set the Spawn-direction, look to into the direction where the Spawn should look"));
                                player.getInventory().setItem(0, InventoryHandler.createStack(Material.NETHER_STAR, "&7Set " + team.getName() + " Direction", Arrays.asList("&7Right click", "&7to set the Spawn direction", "&7to the curren facing direction"), "d(" + team.getColor().name() + ")", "d(" + team.getColor().name() + ")"));
                            }
                        }.runTaskLater(MLGRush.getInstance(), 10);
                        return true;
                    }
                case 'c':
                    GameManager.createGame(player, MLGRush.getMapManager().getMap(Integer.parseInt(arguments)));
                    InvOpener.closeDelay(player);
                    break;
                case 'd':
                    BuildModeManager.setSpawnDirection(player, TeamColor.valueOf(arguments));
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
            case 'd':
                BuildModeManager.setSpawnDirection(player, TeamColor.valueOf(arguments));
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
    public static ItemStack createStack(Material material, String name, List<String> lore, String
            command, String commandRightClick, boolean enchanted, int amount, boolean unmoveable) {
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
    public static ItemStack createStack(Material material, String name, List<String> lore, String
            command, String rightCommand, boolean ench, int amount) {
        return createStack(material, name, lore, command, rightCommand, ench, amount, true);
    }

    public static ItemStack createStack(Material material, String name, List<String> lore, String
            command, String rightCommand) {
        return createStack(material, name, lore, command, rightCommand, false, 1);
    }

    public static ItemStack createStack(Material material, String name) {
        return createStack(material, name, Collections.emptyList(), "", "", false, 1);
    }


    public static ItemStack createStack(Material material, String name, List<String> lore, String
            command, String rightCommand, boolean enchanted) {
        return createStack(material, name, lore, command, rightCommand, enchanted, 1);
    }

    public static ItemStack createStack(Material material, String name, List<String> lore, String command,
                                        boolean enchanted) {
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

    public static ItemStack createStack(Material material, String name, List<String> lore, String command,
                                        int amount) {
        return createStack(material, name, lore, command, "", false, amount);
    }

    public static ItemStack createStack(Material material, String name, List<String> lore) {
        return createStack(material, name, lore, "", "", false, 1);
    }

    public static ItemStack createStack(Material material, String name, List<String> lore, boolean ench,
                                        boolean moveable) {
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
