package dev.invasion.plugins.games.mlgrush.Utils.BetterItem;

import dev.invasion.plugins.games.mlgrush.MLGRush;
import dev.invasion.plugins.games.mlgrush.Utils.KeyAssistant;
import dev.invasion.plugins.games.mlgrush.Utils.MessageCreator;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class BetterItem {
    private Material material;
    private String name = null;
    private int amount = 1;
    private ArrayList<Enchantment> enchantments = new ArrayList<>();
    private Function<ItemClickEvent, Boolean> onComplete;
    private List<EventType> listenTo;
    private ArrayList<String> lore = new ArrayList<>();
    private boolean glint = false;
    private boolean droppable = false;
    private boolean removeOnDrop = false;
    private boolean execute;
    private BetterItemDescription description;
    private boolean playSound = true;
    public UUID getUuid() {
        return uuid;
    }
    private Function<ItemMeta, ItemMeta> applyOnMeta;
    private boolean useDesc = false;

    //private ItemID id;
    private UUID uuid;

    public boolean listenTo(EventType type) {
        return listenTo.contains(type);
    }


    public BetterItem(BetterItemDescription description, boolean isMoveable) {
        useDesc = true;
        if(!isMoveable) {
            execute = true;
            onComplete = (itemClickEvent -> {return true;});
        } else {
            execute = false;
        }
        this.description = description;
        applyOnMeta = (meta) ->  {return meta;};
    }

    public BetterItem(Function<ItemClickEvent, Boolean> onComplete, Material material, List<EventType> listenTo) {
        this.onComplete = onComplete;
        this.material = material;
        this.listenTo = listenTo;
        execute = true;
        applyOnMeta = (meta) ->  {return meta;};
        uuid = BetterItemManager.registerItem(this);

    }
    public BetterItem(Function<ItemClickEvent, Boolean> onComplete, Material material) {
        this.onComplete = onComplete;
        this.material = material;
        execute = true;
        applyOnMeta = (meta) ->  {return meta;};
        this.listenTo = Arrays.asList(EventType.DROP, EventType.EXTERNAL, EventType.INVENTORY);
        uuid = BetterItemManager.registerItem(this);

    }

    public BetterItem setMaterial(Material material) {
        this.material = material;
        return this;
    }
    public BetterItem setName(String name) {
        this.name = name;
        return this;
    }
    public BetterItem modifyMeta(Function<ItemMeta, ItemMeta> func) {
        applyOnMeta = func;
        return this;
    }
    public BetterItem setRemoveOnDrop(boolean bool) {
        removeOnDrop = bool;
        return this;
    }
    public boolean getRemoveOnDrop() {
        return removeOnDrop;
    }
    public BetterItem setAmount(int amount) {
        this.amount = amount;
        return this;
    }
    public BetterItem addEnchant(Enchantment enchantment) {
        enchantments.add(enchantment);
        return this;
    }
    public BetterItem addEnchant(List<Enchantment> enchantment) {
        enchantments.addAll(enchantment);
        return this;
    }
    public BetterItem setGlint(boolean glint) {
        this.glint = glint;
        return this;
    }
    public BetterItem addLore(String toAdd) {
        lore.add(toAdd);
        return this;
    }
    public BetterItem addLore(List<String> toAdd) {
        lore.addAll(toAdd);
        return this;
    }
    public BetterItem setDroppable(boolean droppable) {
        this.droppable = droppable;
        return this;
    }
    public BetterItem setPlaySound(boolean playSound) {
        this.playSound = playSound;
        return this;
    }
    public boolean getPlaySound() {
        return playSound;
    }
    public Function<ItemClickEvent, Boolean> getOnComplete() {
        return onComplete;
    }

    public ItemStack create() {
        ItemStack stack = new ItemStack(material, amount);
        ArrayList<String> colorful_lore = new ArrayList<>();
        for (String item : lore) {
            colorful_lore.add(MessageCreator.t("&7" + item));
        }
        if(useDesc && description != null) {
            stack.setItemMeta(createMetadata(description.toLore(), stack.getItemMeta()));
            return stack;
        }
        stack.setItemMeta(createMetadata(colorful_lore, stack.getItemMeta()));
        return stack;
    }
    public ItemStack create(BetterItemDescription description) {
        ItemStack stack = new ItemStack(material, amount);
        ArrayList<String> colorful_lore = new ArrayList<>();
        stack.setItemMeta(createMetadata(description.toLore(), stack.getItemMeta()));
        return stack;
    }
    private ItemMeta createMetadata(List<String> lore, ItemMeta meta) {
        if (meta == null) return meta;
        if(name != null) {
            meta.setDisplayName(MessageCreator.t(name));
        }
        meta.getPersistentDataContainer().set(KeyAssistant.getKey("undroppable"), new PeresistentBoolean() , !droppable);
        if(uuid == null && execute) {
            meta.getPersistentDataContainer().set(KeyAssistant.getKey("timestamp"), PersistentDataType.LONG, MLGRush.getStartup() + 1);
            meta.getPersistentDataContainer().set(KeyAssistant.getKey("uuid"), new UUIDTagType(), BetterItemManager.getStaticUUID());
        }
        if(execute && uuid != null) meta.getPersistentDataContainer().set(KeyAssistant.getKey("timestamp"), PersistentDataType.LONG, Instant.now().getEpochSecond());
        if(uuid != null && execute) {
            meta.getPersistentDataContainer().set(KeyAssistant.getKey("uuid"), new UUIDTagType(), uuid);
        }
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DYE, ItemFlag.HIDE_ENCHANTS);
        if(glint) {
            meta.addEnchant(MLGRush.getGlow(), 1, true);
        }
        meta.setLore(lore);
        meta = applyOnMeta.apply(meta);
        return meta;
    }
}
