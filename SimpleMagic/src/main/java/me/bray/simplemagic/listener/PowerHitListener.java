package me.bray.simplemagic.listener;

import me.bray.simplemagic.SimpleMagic;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PowerHitListener implements Listener {

    private final SimpleMagic plugin;

    public PowerHitListener(SimpleMagic plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Snowball snowball)) {
            return;
        }

        if (!snowball.hasMetadata("simplemagic_ice")) {
            return;
        }

        if (!(event.getHitEntity() instanceof LivingEntity target)) {
            return;
        }

        double damage = plugin.getPowerFile().getConfig().getDouble("ice.damage", 3.0);
        int slowDuration = plugin.getPowerFile().getConfig().getInt("ice.slow-duration", 60);
        int slowAmplifier = plugin.getPowerFile().getConfig().getInt("ice.slow-amplifier", 1);

        target.damage(damage, snowball);

        target.addPotionEffect(new PotionEffect(
                PotionEffectType.SLOW,
                slowDuration,
                slowAmplifier
        ));

        target.getWorld().spawnParticle(
                Particle.SNOWFLAKE,
                target.getLocation().add(0, 1, 0),
                40,
                0.5,
                0.7,
                0.5,
                0.04
        );

        target.getWorld().playSound(
                target.getLocation(),
                Sound.BLOCK_GLASS_BREAK,
                0.8F,
                1.2F
        );
    }
}