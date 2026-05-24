package me.bray.simplemagic.power.impl;

import me.bray.simplemagic.SimpleMagic;
import me.bray.simplemagic.power.WandPower;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;

public class LifeBeamPower implements WandPower {

    private final SimpleMagic plugin;

    public LifeBeamPower(SimpleMagic plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getId() {
        return "life-beam";
    }

    @Override
    public void cast(Player player, ItemStack wand) {

        double range = plugin.getPowerFile().getConfig()
                .getDouble("life-beam.range", 15);

        double healAmount = plugin.getPowerFile().getConfig()
                .getDouble("life-beam.heal-amount", 1.0);

        int duration = plugin.getPowerFile().getConfig()
                .getInt("life-beam.duration", 60);

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

                    player.getWorld().spawnParticle(
                            Particle.TOTEM,
                            point,
                            2,
                            0.03,
                            0.03,
                            0.03,
                            0
                    );
                }

                RayTraceResult result = player.getWorld().rayTraceEntities(
                        eye,
                        eye.getDirection(),
                        range,
                        0.6,
                        entity -> entity instanceof LivingEntity
                                && !(entity instanceof Monster)
                                && entity != player
                );

                if (result != null && result.getHitEntity() instanceof LivingEntity target) {

                    if (!target.isDead() && target.isValid() && target.getHealth() > 0) {

                        double maxHealth = target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();

                        double newHealth = Math.min(
                                maxHealth,
                                target.getHealth() + healAmount
                        );

                        target.setHealth(newHealth);

                        target.getWorld().spawnParticle(
                                Particle.HEART,
                                target.getLocation().add(0, 1, 0),
                                6,
                                0.3,
                                0.5,
                                0.3,
                                0
                        );
                    }
                }

                player.getWorld().playSound(
                        player.getLocation(),
                        Sound.BLOCK_BEACON_AMBIENT,
                        0.25F,
                        1.8F
                );

                ticks += 5;
            }

        }.runTaskTimer(plugin, 0L, 5L);
    }
}