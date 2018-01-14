package me.rayzr522.decoheads;

import me.rayzr522.decoheads.command.CommandDecoHeads;
import me.rayzr522.decoheads.data.HeadManager;
import me.rayzr522.decoheads.gui.system.GuiListener;
import me.rayzr522.decoheads.util.ConfigHandler;
import me.rayzr522.decoheads.util.Localization;
import me.rayzr522.decoheads.util.Reflector;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class DecoHeads extends JavaPlugin {
    private static DecoHeads instance;

    private ConfigHandler configHandler;
    private Localization localization;

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

        reload();

        CommandDecoHeads commandDecoHeads = new CommandDecoHeads(this);
        getCommand("decoheads").setExecutor(commandDecoHeads);
        getCommand("decoheads").setTabCompleter(commandDecoHeads);

        getServer().getPluginManager().registerEvents(new GuiListener(), this);
    }

    @Override
    public void onDisable() {
        save();

        instance = null;
    }

    public void reload() {
        try {
            localization = Localization.load("messages.yml");
            headManager.load();
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Failed to load config files:", e);
        }
    }

    private void save() {
        headManager.save();
    }

    public String tr(String key, Object... strings) {
        return localization.tr(key, strings);
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }

    public HeadManager getHeadManager() {
        return headManager;
    }

}
