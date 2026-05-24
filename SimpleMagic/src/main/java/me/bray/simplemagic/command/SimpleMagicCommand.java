package me.bray.simplemagic.command;

import me.bray.simplemagic.SimpleMagic;
import me.bray.simplemagic.wand.WandDefinition;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.command.TabCompleter;
import java.util.ArrayList;
import java.util.List;

public class SimpleMagicCommand implements CommandExecutor, TabCompleter {

    private final SimpleMagic plugin;

    public SimpleMagicCommand(SimpleMagic plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            sender.sendMessage(plugin.getMessageManager().get("usage-main"));
            return true;
        }

        if (args[0].equalsIgnoreCase("give")) {
            handleGive(sender, args);
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            handleReload(sender, args);
            return true;
        }

        sender.sendMessage(plugin.getMessageManager().get("unknown-command"));
        return true;
    }

    private void handleGive(CommandSender sender, String[] args) {
        if (!sender.hasPermission("simplemagic.command.give")) {
            sender.sendMessage(plugin.getMessageManager().get("no-permission"));
            return;
        }

        if (args.length < 3) {
            sender.sendMessage(plugin.getMessageManager().get("usage-give"));
            return;
        }

        Player target = Bukkit.getPlayerExact(args[1]);

        if (target == null) {
            sender.sendMessage(plugin.getMessageManager().get("player-not-found"));
            return;
        }

        String wandId = args[2];
        ItemStack wand = plugin.getWandManager().createWand(wandId);

        if (wand == null) {
            sender.sendMessage(plugin.getMessageManager().get(
                    "wand-not-found",
                    "%wand%", wandId
            ));
            return;
        }

        target.getInventory().addItem(wand);

        sender.sendMessage(plugin.getMessageManager().get(
                "wand-given",
                "%wand%", wandId,
                "%player%", target.getName()
        ));
    }

    private void handleReload(CommandSender sender, String[] args) {
        if (!sender.hasPermission("simplemagic.command.reload")) {
            sender.sendMessage(plugin.getMessageManager().get("no-permission"));
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(plugin.getMessageManager().get("usage-reload"));
            return;
        }

        String type = args[1].toLowerCase();

        switch (type) {
            case "config" -> {
                plugin.reloadConfig();
                sender.sendMessage(plugin.getMessageManager().get("reload-config"));
            }

            case "messages" -> {
                plugin.getMessageManager().load();
                sender.sendMessage(plugin.getMessageManager().get("reload-messages"));
            }

            case "wands" -> {
                plugin.reloadConfig();
                plugin.getWandManager().loadWands();
                sender.sendMessage(plugin.getMessageManager().get("reload-wands"));
            }

            case "powers" -> {
                plugin.getPowerFile().load();
                sender.sendMessage(plugin.getMessageManager().get("reload-powers"));
            }

            case "all" -> {
                plugin.getPowerFile().load();
                plugin.reloadConfig();
                plugin.getMessageManager().load();
                plugin.getWandManager().loadWands();
                sender.sendMessage(plugin.getMessageManager().get("reload-all"));
            }

            default -> sender.sendMessage(plugin.getMessageManager().get("usage-reload"));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            if (sender.hasPermission("simplemagic.command.give")) {
                completions.add("give");
            }

            if (sender.hasPermission("simplemagic.command.reload")) {
                completions.add("reload");
            }

            return completions;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("simplemagic.command.reload")) {
                completions.add("config");
                completions.add("messages");
                completions.add("wands");
                completions.add("powers");
                completions.add("all");
            }

            return completions;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            if (sender.hasPermission("simplemagic.command.give")) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    completions.add(player.getName());
                }
            }

            return completions;
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
            if (sender.hasPermission("simplemagic.command.give")) {
                for (WandDefinition wand : plugin.getWandManager().getWands()) {
                    completions.add(wand.getId());
                }
            }

            return completions;
        }

        return completions;
    }

}
