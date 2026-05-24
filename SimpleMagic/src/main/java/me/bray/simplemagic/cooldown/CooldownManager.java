package me.bray.simplemagic.cooldown;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {

    private final Map<UUID, Map<String, Long>> cooldowns = new HashMap<>();

    public boolean isOnCooldown(UUID uuid, String wandId) {
        if (!cooldowns.containsKey(uuid)) {
            return false;
        }

        Map<String, Long> playerCooldowns = cooldowns.get(uuid);

        if (!playerCooldowns.containsKey(wandId)) {
            return false;
        }

        return playerCooldowns.get(wandId) > System.currentTimeMillis();
    }

    public long getRemaining(UUID uuid, String wandId) {
        if (!isOnCooldown(uuid, wandId)) {
            return 0;
        }

        return cooldowns.get(uuid).get(wandId) - System.currentTimeMillis();
    }

    public void setCooldown(UUID uuid, String wandId, int seconds) {
        cooldowns
                .computeIfAbsent(uuid, k -> new HashMap<>())
                .put(wandId, System.currentTimeMillis() + (seconds * 1000L));
    }
}