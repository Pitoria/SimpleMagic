package me.bray.simplemagic;

import me.bray.simplemagic.listener.PowerExplosionListener;
import me.bray.simplemagic.listener.PowerHitListener;
import me.bray.simplemagic.message.MessageManager;
import me.bray.simplemagic.power.PowerRegistry;
import me.bray.simplemagic.power.impl.*;
import me.bray.simplemagic.wand.WandManager;
import org.bukkit.plugin.java.JavaPlugin;
import me.bray.simplemagic.command.SimpleMagicCommand;
import me.bray.simplemagic.listener.WandUseListener;
import me.bray.simplemagic.cooldown.CooldownManager;
import me.bray.simplemagic.file.PowerFile;

public final class SimpleMagic extends JavaPlugin {

    private WandManager wandManager;
    private MessageManager messageManager;
    private PowerRegistry powerRegistry;
    private CooldownManager cooldownManager;
    private PowerFile powerFile;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.messageManager = new MessageManager(this);
        this.cooldownManager = new CooldownManager();

        this.powerRegistry = new PowerRegistry();
        this.powerRegistry.register(new FirePower(this));
        this.powerFile = new PowerFile(this);
        this.powerRegistry.register(new IcePower(this));
        this.powerRegistry.register(new LightningPower(this));
        this.powerRegistry.register(new HealingPower(this));

        powerRegistry.register(new FireBeamPower(this));
        powerRegistry.register(new IceBeamPower(this));
        powerRegistry.register(new LightningBeamPower(this));
        powerRegistry.register(new LifeBeamPower(this));

        this.wandManager = new WandManager(this);
        this.wandManager.loadWands();

        getServer().getPluginManager().registerEvents(
                new PowerHitListener(this), this
        );

        SimpleMagicCommand command = new SimpleMagicCommand(this);
        getCommand("simplemagic").setExecutor(command);
        getCommand("simplemagic").setTabCompleter(command);

        getServer().getPluginManager().registerEvents(
                new WandUseListener(this), this
        );

        getServer().getPluginManager().registerEvents(
                new PowerExplosionListener(),
                this
        );

        getLogger().info("SimpleMagic enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("SimpleMagic disabled.");
    }

    public WandManager getWandManager() {
        return wandManager;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public PowerRegistry getPowerRegistry() {
        return powerRegistry;
    }

    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }

    public PowerFile getPowerFile() {
        return powerFile;
    }

}