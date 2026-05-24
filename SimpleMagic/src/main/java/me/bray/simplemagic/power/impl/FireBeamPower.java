package me.bray.simplemagic.power.impl;

import me.bray.simplemagic.SimpleMagic;
import me.bray.simplemagic.power.WandPower;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;

public class FireBeamPower implements WandPower {

    private final SimpleMagic plugin;

    public FireBeamPower(SimpleMagic plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getId() {
        return "fire-beam";
    }

    @Override
    public void cast(Player player, ItemStack wand) {
        double range = plugin.getPowerFile().getConfig()
                .getDouble("fire-beam.range", 18);

        double damage = plugin.getPowerFile().getConfig()
                .getDouble("fire-beam.damage", 1.0);

        int fireTicks = plugin.getPowerFile().getConfig()
                .getInt("fire-beam.fire-ticks", 60);

        int duration = plugin.getPowerFile().getConfig()
                .getInt("fire-beam.duration", 60);

        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {

                if (!player.isOnline() || player.isDead() || ticks >= duration) {
                    cancel();
                    return;
                }

                Location eye = player.getEyeLocation();

                for (double i = 0; i < range; i += 0.5) {
                    Location point = eye.clone().add(eye.getDirection().multiply(i));
                    player.getWorld().spawnParticle(Particle.FLAME, point, 2, 0.03, 0.03, 0.03, 0.01);
                }

                RayTraceResult result = player.getWorld().rayTraceEntities(
                        eye,
                        eye.getDirection(),
                        range,
                        0.6,
                        entity -> entity instanceof LivingEntity && entity != player
                );

                if (result != null && result.getHitEntity() instanceof LivingEntity target) {
                    if (!target.isDead() && target.isValid() && target.getHealth() > 0) {
                        target.damage(damage, player);
                        target.setFireTicks(fireTicks);
                        target.getWorld().spawnParticle(Particle.LAVA, target.getLocation().add(0, 1, 0), 8, 0.3, 0.5, 0.3, 0);
                    }
                }

                player.getWorld().playSound(player.getLocation(), Sound.ITEM_FIRECHARGE_USE, 0.25F, 1.8F);
                ticks += 5;
            }
        }.runTaskTimer(plugin, 0L, 5L);
    }
}