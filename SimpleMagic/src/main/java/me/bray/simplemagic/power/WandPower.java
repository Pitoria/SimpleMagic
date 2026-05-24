package me.bray.simplemagic.power;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface WandPower {

    String getId();

    void cast(Player player, ItemStack wand);
}