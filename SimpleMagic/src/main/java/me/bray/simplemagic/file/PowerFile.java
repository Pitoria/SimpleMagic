package me.bray.simplemagic.file;

import me.bray.simplemagic.SimpleMagic;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class PowerFile {

    private final SimpleMagic plugin;

    private File file;
    private YamlConfiguration config;

    public PowerFile(SimpleMagic plugin) {
        this.plugin = plugin;
        load();
    }

    public void load() {
        file = new File(plugin.getDataFolder(), "powers.yml");

        if (!file.exists()) {
            plugin.saveResource("powers.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public YamlConfiguration getConfig() {
        return config;
    }
}