package me.bray.simplemagic.power.impl;

import me.bray.simplemagic.SimpleMagic;
import me.bray.simplemagic.power.WandPower;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class LightningPower implements WandPower {

    private final SimpleMagic plugin;

    public LightningPower(SimpleMagic plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getId() {
        return "lightning";
    }

    @Override
    public void cast(Player player, ItemStack wand) {

        double damage = plugin.getPowerFile().getConfig()
                .getDouble("lightning.damage", 8.0);

        double range = plugin.getPowerFile().getConfig()
                .getDouble("lightning.range", 30);

        boolean strikeEffect = plugin.getPowerFile().getConfig()
                .getBoolean("lightning.strike-effect", true);

        RayTraceResult trace = player.getWorld().rayTraceEntities(
                player.getEyeLocation(),
                player.getEyeLocation().getDirection(),
                range,
                0.5,
                entity -> entity instanceof LivingEntity && entity != player
        );

        Location endLocation;

        if (trace != null && trace.getHitEntity() instanceof LivingEntity target) {

            target.damage(damage, player);

            endLocation = target.getLocation();

            if (strikeEffect) {
                player.getWorld().strikeLightningEffect(endLocation);
            }

        } else {

            endLocation = player.getEyeLocation().add(
                    player.getLocation().getDirection().multiply(range)
            );
        }

        drawLightning(player.getEyeLocation(), endLocation);

        player.getWorld().playSound(
                player.getLocation(),
                Sound.ENTITY_LIGHTNING_BOLT_THUNDER,
                0.7F,
                1.5F
        );
    }

    private void drawLightning(Location start, Location end) {

        Vector direction = end.toVector().subtract(start.toVector());
        double length = direction.length();

        direction.normalize();

        for (double i = 0; i < length; i += 0.5) {

            Location point = start.clone().add(direction.clone().multiply(i));

            start.getWorld().spawnParticle(
                    Particle.ELECTRIC_SPARK,
                    point,
                    3,
                    0.05,
                    0.05,
                    0.05,
                    0.01
            );
        }
    }
}