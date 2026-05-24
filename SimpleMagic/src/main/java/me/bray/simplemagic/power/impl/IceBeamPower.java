package me.bray.simplemagic.power.impl;

import me.bray.simplemagic.SimpleMagic;
import me.bray.simplemagic.power.WandPower;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;

public class IceBeamPower implements WandPower {

    private final SimpleMagic plugin;

    public IceBeamPower(SimpleMagic plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getId() {
        return "ice-beam";
    }

    @Override
    public void cast(Player player, ItemStack wand) {

        double range = plugin.getPowerFile().getConfig()
                .getDouble("ice-beam.range", 18);

        double damage = plugin.getPowerFile().getConfig()
                .getDouble("ice-beam.damage", 0.5);

        int slowAmplifier = plugin.getPowerFile().getConfig()
                .getInt("ice-beam.slow-amplifier", 2);

        int slowDuration = plugin.getPowerFile().getConfig()
                .getInt("ice-beam.slow-duration", 40);

        int duration = plugin.getPowerFile().getConfig()
                .getInt("ice-beam.duration", 60);

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
                            Particle.SNOWFLAKE,
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
                        entity -> entity instanceof LivingEntity && entity != player
                );

                if (result != null && result.getHitEntity() instanceof LivingEntity target) {

                    if (!target.isDead() && target.isValid() && target.getHealth() > 0) {

                        target.damage(damage, player);

                        target.addPotionEffect(
                                new PotionEffect(
                                        PotionEffectType.SLOW,
                                        slowDuration,
                                        slowAmplifier
                                )
                        );

                        target.getWorld().spawnParticle(
                                Particle.SNOWBALL,
                                target.getLocation().add(0, 1, 0),
                                10,
                                0.3,
                                0.5,
                                0.3,
                                0
                        );
                    }
                }

                player.getWorld().playSound(
                        player.getLocation(),
                        Sound.BLOCK_GLASS_BREAK,
                        0.25F,
                        1.8F
                );

                ticks += 5;
            }

        }.runTaskTimer(plugin, 0L, 5L);
    }
}