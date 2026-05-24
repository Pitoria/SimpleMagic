package me.bray.simplemagic.power.impl;

import me.bray.simplemagic.SimpleMagic;
import me.bray.simplemagic.power.WandPower;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class HealingPower implements WandPower {

    private final SimpleMagic plugin;

    public HealingPower(SimpleMagic plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getId() {
        return "healing";
    }

    @Override
    public void cast(Player player, ItemStack wand) {
        double radius = plugin.getPowerFile().getConfig().getDouble("healing.radius", 5);
        int duration = plugin.getPowerFile().getConfig().getInt("healing.duration", 10);
        double healAmount = plugin.getPowerFile().getConfig().getDouble("healing.heal-amount", 2.0);

        Location center = player.getLocation();

        player.getWorld().playSound(center, Sound.BLOCK_BEACON_ACTIVATE, 0.7F, 1.4F);

        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (ticks >= duration * 20) {
                    cancel();
                    return;
                }

                center.getWorld().spawnParticle(
                        Particle.VILLAGER_HAPPY,
                        center.clone().add(0, 0.2, 0),
                        35,
                        radius / 2,
                        0.2,
                        radius / 2,
                        0.02
                );

                center.getWorld().spawnParticle(
                        Particle.TOTEM,
                        center.clone().add(0, 0.6, 0),
                        12,
                        radius / 2,
                        0.4,
                        radius / 2,
                        0.03
                );

                for (LivingEntity entity : center.getWorld().getNearbyLivingEntities(center, radius)) {

                    if (entity instanceof Monster) continue;
                    if (entity.isDead()) continue;
                    if (!entity.isValid()) continue;
                    if (entity.getHealth() <= 0) continue;

                    double newHealth = Math.min(
                            entity.getMaxHealth(),
                            entity.getHealth() + healAmount
                    );

                    entity.setHealth(newHealth);
                }

                ticks += 20;
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
}