package me.bray.simplemagic.power;

import java.util.HashMap;
import java.util.Map;

public class PowerRegistry {

    private final Map<String, WandPower> powers = new HashMap<>();

    public void register(WandPower power) {
        powers.put(power.getId().toLowerCase(), power);
    }

    public WandPower getPower(String id) {
        return powers.get(id.toLowerCase());
    }

    public boolean exists(String id) {
        return powers.containsKey(id.toLowerCase());
    }
}