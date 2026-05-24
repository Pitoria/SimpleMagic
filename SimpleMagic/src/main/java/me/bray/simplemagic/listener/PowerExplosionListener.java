package me.bray.simplemagic.listener;

import org.bukkit.entity.Fireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class PowerExplosionListener implements Listener {

    @EventHandler
    public void onExplosion(EntityExplodeEvent event) {

        if (!(event.getEntity() instanceof Fireball fireball)) {
            return;
        }

        if (!fireball.hasMetadata("simplemagic_fire")) {
            return;
        }

        event.blockList().clear();
    }
}