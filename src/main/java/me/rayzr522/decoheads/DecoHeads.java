package me.rayzr522.decoheads;

import me.rayzr522.decoheads.command.CommandDecoHeads;
import me.rayzr522.decoheads.command.CommandDecoHeadsAdmin;
import me.rayzr522.decoheads.compat.EconomyWrapper;
import me.rayzr522.decoheads.config.Localization;
import me.rayzr522.decoheads.config.Settings;
import me.rayzr522.decoheads.data.HeadManager;
import me.rayzr522.decoheads.event.PlayerListener;
import me.rayzr522.decoheads.gui.system.GUIListener;
import me.rayzr522.decoheads.util.ConfigHandler;
import me.rayzr522.decoheads.util.Reflector;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.update.spiget.SpigetUpdate;

import java.util.logging.Level;

public class DecoHeads extends JavaPlugin {
    private static DecoHeads instance;

    private ConfigHandler configHandler;
    private Settings settings;
    private Localization localization;
    private HeadManager headManager;

    private EconomyWrapper economy;
    private SpigetUpdate updater;

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

        if (!Bukkit.getPluginManager().isPluginEnabled("Vault") || !(economy = new EconomyWrapper()).setup()) {
            getLogger().warning("Failed to find an economy plugin! Disabling economy mode, please re-enable once you have installed an economy plugin.");
            if (settings.isEconomyEnabled()) {
                settings.setEconomyEnabled(false);
                settings.save();
            }
        }

        CommandDecoHeads commandDecoHeads = new CommandDecoHeads(this);
        getCommand("decoheads").setExecutor(commandDecoHeads);
        getCommand("decoheads").setTabCompleter(commandDecoHeads);

        CommandDecoHeadsAdmin commandDecoHeadsAdmin = new CommandDecoHeadsAdmin(this);
        getCommand("decoheadsadmin").setExecutor(commandDecoHeadsAdmin);
        getCommand("decoheadsadmin").setTabCompleter(commandDecoHeadsAdmin);

        getServer().getPluginManager().registerEvents(new GUIListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        Metrics metrics = new Metrics(this);
        metrics.addCustomChart(new Metrics.SimplePie("economy_mode", () -> settings.isEconomyEnabled() ? "enabled" : "disabled"));
        metrics.addCustomChart(new Metrics.SimplePie("custom_heads", () -> settings.isCustomHeadsEnabled() ? "enabled" : "disabled"));
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

            if (settings.isUpdaterEnabled()) {
                updater = new SpigetUpdate(this, 24655);
            } else {
                updater = null;
            }
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

    public EconomyWrapper getEconomy() {
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

    public SpigetUpdate getUpdater() {
        return updater;
    }
}
