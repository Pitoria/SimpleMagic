package me.bray.simplemagic.power.impl;

import me.bray.simplemagic.SimpleMagic;
import me.bray.simplemagic.power.WandPower;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

public class IcePower implements WandPower {

    private final SimpleMagic plugin;

    public IcePower(SimpleMagic plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getId() {
        return "ice";
    }

    @Override
    public void cast(Player player, ItemStack wand) {
        double velocity = plugin.getPowerFile().getConfig().getDouble("ice.velocity", 1.7);

        player.getWorld().spawnParticle(
                Particle.SNOWFLAKE,
                player.getEyeLocation(),
                25,
                0.25,
                0.25,
                0.25,
                0.03
        );

        player.getWorld().playSound(
                player.getLocation(),
                Sound.BLOCK_GLASS_BREAK,
                0.7F,
                1.5F
        );

        Snowball snowball = player.launchProjectile(Snowball.class);
        snowball.setVelocity(player.getLocation().getDirection().multiply(velocity));
        snowball.setMetadata("simplemagic_ice", new FixedMetadataValue(plugin, true));
        new BukkitRunnable() {
            @Override
            public void run() {
                if (snowball.isDead() || snowball.isOnGround()) {
                    cancel();
                    return;
                }

                snowball.getWorld().spawnParticle(
                        Particle.SNOWFLAKE,
                        snowball.getLocation(),
                        20,
                        0.25,
                        0.25,
                        0.25,
                        0.02
                );

                snowball.getWorld().spawnParticle(
                        Particle.CLOUD,
                        snowball.getLocation(),
                        8,
                        0.15,
                        0.15,
                        0.15,
                        0.01
                );
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
}