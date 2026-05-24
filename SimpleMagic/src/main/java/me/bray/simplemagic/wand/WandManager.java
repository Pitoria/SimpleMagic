package me.bray.simplemagic.wand;

import me.bray.simplemagic.SimpleMagic;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class WandManager {

    private final SimpleMagic plugin;
    private final Map<String, WandDefinition> wands = new HashMap<>();
    private final NamespacedKey wandKey;

    public WandManager(SimpleMagic plugin) {
        this.plugin = plugin;
        this.wandKey = new NamespacedKey(plugin, "wand_id");
    }

    public void loadWands() {
        wands.clear();

        ConfigurationSection section = plugin.getConfig().getConfigurationSection("wands");

        if (section == null) {
            plugin.getLogger().warning("No wands configured.");
            return;
        }

        for (String id : section.getKeys(false)) {
            String path = "wands." + id;

            int xpCost = plugin.getConfig().getInt(path + ".xp-cost", 0);

            int cooldown = plugin.getConfig().getInt(path + ".cooldown", 0);

            Material material = Material.matchMaterial(plugin.getConfig().getString(path + ".material", "STICK"));

            if (material == null) {
                plugin.getLogger().warning("Invalid material for wand: " + id);
                continue;
            }

            String name = plugin.getConfig().getString(path + ".name", "&fVarita");
            List<String> lore = plugin.getConfig().getStringList(path + ".lore");
            List<String> powers = plugin.getConfig().getStringList(path + ".powers");

            wands.put(id, new WandDefinition(id, material, name, lore, powers, cooldown, xpCost));
        }

        plugin.getLogger().info("Loaded " + wands.size() + " wands.");
    }

    public ItemStack createWand(String id) {
        WandDefinition definition = wands.get(id);

        if (definition == null) {
            return null;
        }

        ItemStack item = new ItemStack(definition.getMaterial());
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(color(definition.getName()));

        List<String> coloredLore = new ArrayList<>();
        for (String line : definition.getLore()) {
            coloredLore.add(color(line));
        }

        meta.setLore(coloredLore);

        meta.getPersistentDataContainer().set(
                wandKey,
                PersistentDataType.STRING,
                definition.getId()
        );

        item.setItemMeta(meta);
        return item;
    }

    public boolean isWand(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }

        return item.getItemMeta()
                .getPersistentDataContainer()
                .has(wandKey, PersistentDataType.STRING);
    }

    public String getWandId(ItemStack item) {
        if (!isWand(item)) {
            return null;
        }

        return item.getItemMeta()
                .getPersistentDataContainer()
                .get(wandKey, PersistentDataType.STRING);
    }

    public WandDefinition getWand(String id) {
        return wands.get(id);
    }

    public Collection<WandDefinition> getWands() {
        return wands.values();
    }

    private String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}