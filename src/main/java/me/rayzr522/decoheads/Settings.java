package me.rayzr522.decoheads;

import me.rayzr522.decoheads.util.ConfigVersionChecker;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;

public class Settings {

    /**
     * The version to check in the config
     */
    private static final int CONFIG_VERSION = 1;

    private final DecoHeads plugin;
    private YamlConfiguration config;

    Settings(DecoHeads plugin) {
        this.plugin = plugin;
    }

    void load() throws IOException {
        config = ConfigVersionChecker.updateConfig("settings.yml", CONFIG_VERSION);
    }

    public boolean save() {
        try {
            plugin.getConfigHandler().saveConfig("settings.yml", config);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isEconomyEnabled() {
        return config.getBoolean("economy.enabled");
    }

    public void setEconomyEnabled(boolean economyEnabled) {
        config.set("economy.enabled", economyEnabled);
    }

    public boolean shouldShowFreeHeads() {
        return config.getBoolean("economy.show-free-heads");
    }

    public void setShowFreeHeads(boolean showFreeHeads) {
        config.set("economy.show-free-heads", showFreeHeads);
    }

    public double getDefaultHeadCost() {
        return config.getDouble("economy.default-cost");
    }

    public void setDefaultHeadCost(double headCost) {
        config.set("economy.default-cost", headCost);
    }
}
