package me.bray.simplemagic.message;

import me.bray.simplemagic.SimpleMagic;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class MessageManager {

    private final SimpleMagic plugin;
    private File file;
    private YamlConfiguration messages;

    public MessageManager(SimpleMagic plugin) {
        this.plugin = plugin;
        load();
    }

    public void load() {
        file = new File(plugin.getDataFolder(), "messages.yml");

        if (!file.exists()) {
            plugin.saveResource("messages.yml", false);
        }

        messages = YamlConfiguration.loadConfiguration(file);
    }

    public String get(String path, String... placeholders) {
        String message = messages.getString(path, "&cMissing message: " + path);
        String prefix = messages.getString("prefix", "");

        message = message.replace("%prefix%", prefix);

        for (int i = 0; i < placeholders.length; i += 2) {
            message = message.replace(placeholders[i], placeholders[i + 1]);
        }

        return color(message);
    }

    public List<String> getList(String path) {
        return messages.getStringList(path)
                .stream()
                .map(this::color)
                .toList();
    }

    private String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}