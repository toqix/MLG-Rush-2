package dev.invasion.plugins.games.mlgrush.Utils.BetterItem;

import dev.invasion.plugins.games.mlgrush.MLGRush;
import dev.invasion.plugins.games.mlgrush.Utils.File.FileManager;
import dev.invasion.plugins.games.mlgrush.Utils.KeyAssistant;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import sun.util.BuddhistCalendar;

import java.io.IOException;
import java.security.Key;
import java.util.HashMap;
import java.util.UUID;

public class BetterItemManager implements Listener {

    private static HashMap<UUID, BetterItem> data_storage;
    private static ItemStats stats;
    private static UUID no_uuid;

    public static UUID getStaticUUID() {
        return no_uuid;
    }

    public BetterItemManager() {

        data_storage = new HashMap<>();
        no_uuid = UUID.randomUUID();
        MLGRush.getInstance().getLogger().info("Items initialisation phase complete.");
    }

    public static void save() {
        try {
            FileManager.save(stats, "/item_stats.json");
        } catch (IOException e) {
            MLGRush.getInstance().getLogger().warning("Could not save item stats, cancelled saving");
            e.printStackTrace();
        }
        if(data_storage == null) {
            MLGRush.getInstance().getLogger().info("Could not save ItemStats.");
            return;
        }
        data_storage.clear();
        MLGRush.getInstance().getLogger().info("Item stats saved");
    }

    public static BetterItem getItem(UUID uuid) {
        if (data_storage.containsKey(uuid)) return data_storage.get(uuid);
        return null;
    }

    private static UUID getUUID() {
        UUID uuid = UUID.randomUUID();
        if(data_storage.containsKey(uuid)) {
            return getUUID();
        }
        return uuid;
    }

    public static UUID registerItem(BetterItem toRegister) {
        /*if(data_storage.containsValue(toRegister)) {
            if(Game.getDev()) Bukkit.broadcastMessage("&7[" + Game.getGameName() + "&7| &bDEV &7] Item same");
            for (BetterItem item : data_storage.values()) {
                if(item.equals(toRegister)) {
                    if(Game.getDev()) Bukkit.broadcastMessage("&7[" + Game.getGameName() + "&7| &bDEV &7] Reused UUID");
                    return item.getUuid();
                }
            }
        }
        *
        * This could one day be useful if the performance of the plugin would be HORRIBLE but i don't think that it's
        * gonna get that way (hopefully).
        *
        */
        UUID uuid = getUUID();
        data_storage.put(uuid, toRegister);
        return uuid;
    }
    public static boolean isValid(UUID uuid) {
        return data_storage.containsKey(uuid);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        EventType eventType = EventType.INVENTORY;
        ItemStack item = event.getCurrentItem();
        if(item != null) {
            if(item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                if(meta != null) {
                    //Bukkit.broadcastMessage("Checking time");
                    PersistentDataContainer container = meta.getPersistentDataContainer();
                    if(container.has(KeyAssistant.getKey("timestamp"), PersistentDataType.LONG)) {
                        //Bukkit.broadcastMessage("Checking time: LONG_item" + container.get(KeyAssistant.getKey("timestamp"), PersistentDataType.LONG).longValue() + " - startup long: " + Game.getStartup());
                        if (container.get(KeyAssistant.getKey("timestamp"), PersistentDataType.LONG).longValue() > MLGRush.getStartup()) {
                            //parse

                            if(!container.has(KeyAssistant.getKey("uuid"), new UUIDTagType())) {
                               // ErrorHandeler.error(new ErrorCode(ErrorEffect.CORE, ErrorSeverity.LOWEST, ErrorRelation.ITEM, "BeIt:Click:noUUID"));
                            }
                            UUID uuid = container.get(KeyAssistant.getKey("uuid"), new UUIDTagType());
                            if(uuid.equals(no_uuid)) {
                                event.setCancelled(true);
                            } else {
                                if (BetterItemManager.isValid(uuid)) {
                                    BetterItem betterItem = BetterItemManager.getItem(uuid);
                                    //Bukkit.broadcastMessage("Checking listenTo");
                                    if (betterItem.listenTo(eventType)) {
                                        event.setCancelled(betterItem.getOnComplete().apply(new ItemClickEvent((Player) event.getWhoClicked(), event.isShiftClick(), null, eventType, null, event.getAction() == InventoryAction.PICKUP_HALF, betterItem.getPlaySound())));
                                    }
                                }
                            }
                        } else {
                            item.setType(Material.AIR);
                        }
                    }
                }
            }
        }

    }
    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        EventType eventType = EventType.INVENTORY;
        ItemStack item = event.getCursor();
        if(item != null) {
            if(item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                if(meta != null) {
                    PersistentDataContainer container = meta.getPersistentDataContainer();
                    if(container.has(KeyAssistant.getKey("timestamp"), PersistentDataType.LONG)) {
                        if (container.get(KeyAssistant.getKey("timestamp"), PersistentDataType.LONG) > MLGRush.getStartup()) {
                            //parse
                            UUID uuid = container.get(KeyAssistant.getKey("uuid"), new UUIDTagType());

                            if(uuid.equals(no_uuid)) {
                                event.setCancelled(true);
                            } else {
                                if (BetterItemManager.isValid(uuid)) {
                                    BetterItem betterItem = BetterItemManager.getItem(uuid);
                                    if (betterItem.listenTo(eventType)) {
                                        event.setCancelled(betterItem.getOnComplete().apply(new ItemClickEvent((Player) event.getWhoClicked(), false, null, eventType, null, false, betterItem.getPlaySound())));
                                    }
                                }
                            }
                        } else {
                            item.setType(Material.AIR);
                        }
                    }
                }
            }
        }
    }
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        EventType eventType = EventType.EXTERNAL;
        ItemStack item = event.getItem();
        if(item != null) {
            if(item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                if(meta != null) {
                    PersistentDataContainer container = meta.getPersistentDataContainer();
                    if(container.has(KeyAssistant.getKey("timestamp"), PersistentDataType.LONG)) {
                        if (container.get(KeyAssistant.getKey("timestamp"), PersistentDataType.LONG) > MLGRush.getStartup()) {
                            //parse
                            UUID uuid = container.get(KeyAssistant.getKey("uuid"), new UUIDTagType());
                            //Bukkit.broadcastMessage(uuid.toString());
                            if(uuid.equals(no_uuid)) {
                                event.setCancelled(true);
                            } else {
                                if (BetterItemManager.isValid(uuid)) {
                                    BetterItem betterItem = BetterItemManager.getItem(uuid);
                                    if (betterItem.listenTo(eventType)) {
                                        event.setCancelled(betterItem.getOnComplete().apply(new ItemClickEvent((Player) event.getPlayer(), event.getPlayer().isSneaking(), event.getClickedBlock(), eventType, event.getBlockFace(), false, betterItem.getPlaySound())));
                                    }
                                }
                            }
                        } else {
                            item.setType(Material.AIR);
                        }
                    }
                }
            }
        }
    }
    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        EventType eventType = EventType.INVENTORY;
        ItemStack item = event.getItemDrop().getItemStack();
        if(item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if(meta != null) {
                PersistentDataContainer container = meta.getPersistentDataContainer();
                if(container.has(KeyAssistant.getKey("undroppable"), new PeresistentBoolean()) && container.has(KeyAssistant.getKey("uuid"), new UUIDTagType())) {
                    event.setCancelled(container.get(KeyAssistant.getKey("undroppable"), new PeresistentBoolean()));
                }
                if(container.has(KeyAssistant.getKey("timestamp"), PersistentDataType.LONG)) {
                    if (container.get(KeyAssistant.getKey("timestamp"), PersistentDataType.LONG) > MLGRush.getStartup()) {
                        //parse

                        UUID uuid = container.get(KeyAssistant.getKey("uuid"), new UUIDTagType());
                        if(uuid.equals(no_uuid)) {
                            event.setCancelled(true);
                        } else {
                            if (BetterItemManager.isValid(uuid)) {
                                BetterItem betterItem = BetterItemManager.getItem(uuid);
                                if (betterItem.listenTo(eventType)) {
                                    if (betterItem.getRemoveOnDrop()) {
                                        event.getItemDrop().remove();
                                        return;
                                    }
                                    boolean shift = event.getItemDrop().getItemStack().getAmount() > 1 || event.getPlayer().isSneaking();
                                    event.setCancelled(betterItem.getOnComplete().apply(new ItemClickEvent((Player) event.getPlayer(), shift, null, eventType, null, false, betterItem.getPlaySound())));
                                }
                            }
                        }
                    } else {
                        item.setType(Material.AIR);
                    }
                }
            }
        }
    }
}
