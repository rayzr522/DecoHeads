package me.rayzr522.decoheads;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;

public class Settings {
    private final DecoHeads plugin;
    private YamlConfiguration config;

    Settings(DecoHeads plugin) {
        this.plugin = plugin;
    }

    void load() {
        config = plugin.getConfigHandler().getConfig("settings.yml");
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
