
package com.rayzr522.decoheads;

import java.util.logging.Logger;

import org.bukkit.entity.Player;

public class DHMessenger {

	// private DecoHeads plugin;
	private Logger logger;

	private String prefix = "[DecoHeads] ";

	public DHMessenger(DecoHeads plugin) {

		// this.plugin = plugin;
		this.logger = plugin.getLogger();

	}

	public void setPrefix(String prefix) {

		this.prefix = prefix;

	}

	public String getPrefix() {

		return prefix;

	}

	public void info(String msg) {

		logger.info(msg);

	}

	public void msg(Player p, String msg) {

		p.sendMessage(TextUtils.colorize(prefix + msg));

	}

}
