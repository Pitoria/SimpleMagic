package me.bray.simplemagic.power.impl;

import me.bray.simplemagic.SimpleMagic;
import me.bray.simplemagic.power.WandPower;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class FirePower implements WandPower {

    private final SimpleMagic plugin;

    public FirePower(SimpleMagic plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getId() {
        return "fire";
    }

    @Override
    public void cast(Player player, ItemStack wand) {
        double velocity = plugin.getPowerFile().getConfig()
                .getDouble("fire.velocity", 1.5);

        float yield = (float) plugin.getPowerFile().getConfig()
                .getDouble("fire.explosion-power", 1.5);

        boolean incendiary = plugin.getPowerFile().getConfig()
                .getBoolean("fire.incendiary", false);

        player.getWorld().spawnParticle(
                Particle.FLAME,
                player.getEyeLocation(),
                20,
                0.2,
                0.2,
                0.2,
                0.03
        );

        player.getWorld().playSound(
                player.getLocation(),
                Sound.ITEM_FIRECHARGE_USE,
                0.7F,
                1.2F
        );

        Fireball fireball = player.launchProjectile(Fireball.class);
        fireball.setVelocity(player.getLocation().getDirection().multiply(velocity));
        fireball.setYield(yield);
        fireball.setIsIncendiary(incendiary);
        fireball.setShooter(player);
        fireball.setMetadata(
                "simplemagic_fire",
                new FixedMetadataValue(plugin, true)
        );
    }
}