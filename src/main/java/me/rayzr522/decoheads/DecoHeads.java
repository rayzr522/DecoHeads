package me.rayzr522.decoheads;

import me.rayzr522.decoheads.command.CommandDecoHeads;
import me.rayzr522.decoheads.command.CommandDecoHeadsAdmin;
import me.rayzr522.decoheads.config.Localization;
import me.rayzr522.decoheads.config.Settings;
import me.rayzr522.decoheads.data.HeadManager;
import me.rayzr522.decoheads.gui.system.GUIListener;
import me.rayzr522.decoheads.util.ConfigHandler;
import me.rayzr522.decoheads.util.Reflector;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class DecoHeads extends JavaPlugin {
    private static DecoHeads instance;

    private ConfigHandler configHandler;
    private Settings settings;
    private Localization localization;

    private Economy economy;
    private HeadManager headManager;

    public static DecoHeads getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        if (Reflector.getMinorVersion() < 8) {
            getLogger().severe("DecoHeads is only compatible with Minecraft 1.8+");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        headManager = new HeadManager(this);
        configHandler = new ConfigHandler(this);
        settings = new Settings(this);

        reload();

        if (!setupEconomy()) {
            getLogger().severe("Failed to find an economy plugin! Disabling economy mode, please re-enable once you have installed an economy plugin.");
            settings.setEconomyEnabled(false);
            settings.save();
        }

        CommandDecoHeads commandDecoHeads = new CommandDecoHeads(this);
        getCommand("decoheads").setExecutor(commandDecoHeads);
        getCommand("decoheads").setTabCompleter(commandDecoHeads);

        getCommand("decoheadsadmin").setExecutor(new CommandDecoHeadsAdmin(this));

        getServer().getPluginManager().registerEvents(new GUIListener(), this);
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
        return rsp != null && (economy = rsp.getProvider()) != null;
    }

    @Override
    public void onDisable() {
        save();

        instance = null;
    }

    public void reload() {
        try {
            settings.load();
            localization = Localization.load("messages.yml");
            headManager.load();
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Failed to load config files:", e);
        }
    }

    public void save() {
        headManager.save();
    }

    public String tr(String key, Object... strings) {
        return localization.tr(key, strings);
    }

    public String tr(boolean usePrefix, String key, Object... strings) {
        return localization.tr(usePrefix, key, strings);
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }

    public Settings getSettings() {
        return settings;
    }

    public HeadManager getHeadManager() {
        return headManager;
    }

    public Economy getEconomy() {
        return economy;
    }

    public boolean checkPermission(String permission, CommandSender sender, boolean sendMessage) {
        String fullPermission = String.format("%s.%s", getName(), permission);
        if (!sender.hasPermission(fullPermission)) {
            if (sendMessage) {
                sender.sendMessage(tr("command.no-permission", fullPermission));
            }
            return false;
        }
        return true;
    }
}
