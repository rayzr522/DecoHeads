
package com.rayzr522.decoheads;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class DecoHeads extends JavaPlugin {

	public static DecoHeads INSTANCE;

	public DHMessenger logger;

	private DHListener listener;

	private DHConfigHandler		configHandler;
	private YamlConfiguration	config;

	@Override
	public void onEnable() {

		INSTANCE = this;

		logger = new DHMessenger(this);

		listener = new DHListener(this);
		getServer().getPluginManager().registerEvents(listener, this);

		configHandler = new DHConfigHandler(this);
		config = configHandler.loadConfig("config.yml");

		logger.setPrefix(config.getString("prefix"));

		InventoryManager.loadHeads(this);

		DHCommand executor = new DHCommand(this);

		getCommand("decoheads").setExecutor(executor);

		log("DecoHeads v" + getDescription().getVersion() + " enabled!");

	}

	@Override
	public void onDisable() {

		log("DecoHeads v" + getDescription().getVersion() + " disabled!");

	}

	public void log(String msg) {

		logger.info(msg);

	}

	public void msg(Player p, String string) {

		logger.msg(p, string);

	}

}
