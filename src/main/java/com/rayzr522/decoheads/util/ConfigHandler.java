
package com.rayzr522.decoheads.util;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

import com.rayzr522.decoheads.DecoHeads;

public class ConfigHandler {

    private DecoHeads plugin;

    public ConfigHandler(DecoHeads plugin) {
        this.plugin = plugin;
    }

    public File getFile(String name) {
        return new File(plugin.getDataFolder(), name.replace("/", File.pathSeparator));
    }

    public YamlConfiguration getConfig(String name) {
        File file = getFile(name);

        if (!file.exists()) {
            if (plugin.getResource(name) == null) {
                plugin.log("Failed to load config file '" + name + "'");
                return null;
            }
            saveDefaultFile(name);
            file = getFile(name);
        }

        return YamlConfiguration.loadConfiguration(file);
    }

    public void saveDefaultFile(String name) {
        plugin.saveResource(name, false);
        plugin.log("Loaded default file for " + name);
    }

}
