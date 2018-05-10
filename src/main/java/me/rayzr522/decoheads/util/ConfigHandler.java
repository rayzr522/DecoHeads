package me.rayzr522.decoheads.util;

import me.rayzr522.decoheads.DecoHeads;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

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

        YamlConfiguration config = YamlConfiguration.loadConfiguration(getFile(name));
        if (plugin.getResource(name) != null) {
            config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(plugin.getResource(name))));
        }

        return config;
    }

    public void saveConfig(String name, FileConfiguration config) throws IOException {
        config.save(getFile(name));
    }
}
