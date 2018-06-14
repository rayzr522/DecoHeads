package me.rayzr522.decoheads.config;

import me.rayzr522.decoheads.DecoHeads;
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

    public Settings(DecoHeads plugin) {
        this.plugin = plugin;
    }

    public void load() throws IOException {
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
        // Ensure economy is not enabled when the economy wrapper is not available
        return plugin.getEconomy() != null && config.getBoolean("economy.enabled");
    }

    public void setEconomyEnabled(boolean economyEnabled) {
        // Ensure economy is not enabled when the economy wrapper is not available
        config.set("economy.enabled", plugin.getEconomy() != null && economyEnabled);
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

    public boolean isCustomHeadsEnabled() {
        return config.getBoolean("custom-heads.enabled");
    }

    public void setCustomHeadsEnabled(boolean customHeadsEnabled) {
        config.set("custom-heads.enabled", customHeadsEnabled);
    }

    public double getCustomHeadsCost() {
        return config.getDouble("custom-heads.cost");
    }

    public void setCustomHeadsCost(double customHeadsCost) {
        config.set("custom-heads.cost", customHeadsCost);
    }

    public boolean isUpdaterEnabled() {
        return config.getBoolean("updater-enabled");
    }

    public void setUpdaterEnabled(boolean updaterEnabled) {
        config.set("updater-enabled", updaterEnabled);
    }
}
