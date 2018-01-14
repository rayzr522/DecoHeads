package me.rayzr522.decoheads.util;

import me.rayzr522.decoheads.DecoHeads;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigHandler {

    private DecoHeads plugin;

    public ConfigHandler(DecoHeads plugin) {
        this.plugin = plugin;
        if (!plugin.getDataFolder().exists()) {
            if (!plugin.getDataFolder().mkdirs()) {
                plugin.getLogger().severe("Failed to create the plugin data folder at " + plugin.getDataFolder().getAbsolutePath());
            }
        }
    }

    public File getFile(String name) {
        return new File(plugin.getDataFolder(), name.replace('/', File.separatorChar));
    }

    public YamlConfiguration getConfig(String name) {
        if (!getFile(name).exists() && plugin.getResource(name) != null) {
            plugin.saveResource(name, false);
        }
        return YamlConfiguration.loadConfiguration(getFile(name));
    }

    public void saveConfig(String name, FileConfiguration config) throws IOException {
        config.save(getFile(name));
    }
}
