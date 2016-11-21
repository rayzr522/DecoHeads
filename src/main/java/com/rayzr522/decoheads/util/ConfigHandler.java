
package com.rayzr522.decoheads.util;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

import com.rayzr522.decoheads.DecoHeads;

public class ConfigHandler {

    private DecoHeads plugin;

    public ConfigHandler(DecoHeads plugin) {
        this.plugin = plugin;
        if (!plugin.getDataFolder().exists()) {
            if (!plugin.getDataFolder().mkdirs()) {
                plugin.err("Failed to create the plugin data folder at " + plugin.getDataFolder().getAbsolutePath(), false);
            }
        }
    }

    public File getFile(String name) {
        return new File(plugin.getDataFolder(), name.replace("/", File.pathSeparator));
    }

    public YamlConfiguration getConfig(String name) {
        if (!getFile(name).exists()) {
            if (plugin.getResource(name) == null) {
                plugin.log("Failed to load config file '" + name + "'");
                return null;
            }
            saveDefaultFile(name);
        }

        return YamlConfiguration.loadConfiguration(getFile(name));
    }

    public void saveDefaultFile(String name) {
        plugin.saveResource(name, true);
        plugin.log("Loaded default file for " + name);
    }

}
