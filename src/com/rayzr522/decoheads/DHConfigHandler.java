
package com.rayzr522.decoheads;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

public class DHConfigHandler {

	private DecoHeads plugin;

	public DHConfigHandler(DecoHeads plugin) {

		this.plugin = plugin;

	}

	public File loadFile(String name) {

		return new File(plugin.getDataFolder() + File.separator + name);

	}

	public YamlConfiguration loadConfig(String name) {

		File f = loadFile(name);

		if (!f.exists()) {
			if (plugin.getResource(name) == null) {
				plugin.log("Failed to load config file '" + name + "'");
				return null;
			}
			createDefault(name);
			f = loadFile(name);
		}

		return YamlConfiguration.loadConfiguration(loadFile(name));

	}

	public void createDefault(String name) {

		plugin.saveResource(name, false);
		plugin.log("Loaded default file for " + name);

	}

}
