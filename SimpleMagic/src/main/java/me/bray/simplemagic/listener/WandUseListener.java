package me.bray.simplemagic.listener;

import me.bray.simplemagic.SimpleMagic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class WandUseListener implements Listener {

    private final SimpleMagic plugin;

    public WandUseListener(SimpleMagic plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onWandUse(PlayerInteractEvent event) {
        Action action = event.getAction();

        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (!plugin.getWandManager().isWand(item)) {
            return;
        }

        event.setCancelled(true);

        String wandId = plugin.getWandManager().getWandId(item);

        var wand = plugin.getWandManager().getWand(wandId);

        if (wand == null) {
            return;
        }

        if (wand.getXpCost() > 0 && player.getLevel() < wand.getXpCost()) {
            player.sendActionBar(plugin.getMessageManager().get(
                    "not-enough-xp",
                    "%cost%", String.valueOf(wand.getXpCost())
            ));
            return;
        }

        if (wand.getXpCost() > 0) {
            player.setLevel(player.getLevel() - wand.getXpCost());
        }

        if (plugin.getCooldownManager().isOnCooldown(player.getUniqueId(), wandId)) {
            long remaining = plugin.getCooldownManager().getRemaining(player.getUniqueId(), wandId) / 1000;

            player.sendActionBar(plugin.getMessageManager().get(
                    "wand-cooldown",
                    "%time%", String.valueOf(remaining)
            ));
            return;
        }

        for (String powerId : wand.getPowers()) {

            var power = plugin.getPowerRegistry().getPower(powerId);

            if (power == null) {
                continue;
            }

            power.cast(player, item);
        }

        if (wand.getCooldown() > 0) {
            plugin.getCooldownManager().setCooldown(
                    player.getUniqueId(),
                    wandId,
                    wand.getCooldown()
            );
        }
    }
}