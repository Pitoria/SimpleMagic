package me.bray.simplemagic.wand;

import org.bukkit.Material;

import java.util.List;

public class WandDefinition {

    private final String id;
    private final Material material;
    private final String name;
    private final List<String> lore;
    private final List<String> powers;
    private final int cooldown;
    private final int xpCost;

    public WandDefinition(
            String id,
            Material material,
            String name,
            List<String> lore,
            List<String> powers,
            int cooldown,
            int xpCost
    ) {
        this.id = id;
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.powers = powers;
        this.cooldown = cooldown;
        this.xpCost = xpCost;
    }

    public int getXpCost() {
        return xpCost;
    }

    public String getId() {
        return id;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        return name;
    }

    public List<String> getLore() {
        return lore;
    }

    public List<String> getPowers() {
        return powers;
    }

    public int getCooldown() {
        return cooldown;
    }

}