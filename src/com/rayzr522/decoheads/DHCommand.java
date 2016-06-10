
package com.rayzr522.decoheads;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DHCommand implements CommandExecutor {

	private DecoHeads plugin;

	public DHCommand(DecoHeads plugin) {

		this.plugin = plugin;

	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args) {

		if (!(sender instanceof Player)) {

			System.out.println("This command is only useable by players");
			return true;

		}

		if (cmd.equalsIgnoreCase("decoheads") || cmd.equalsIgnoreCase("dh") || cmd.equalsIgnoreCase("heads")) {

			Player p = (Player) sender;

			if (args.length > 0 && (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("reload"))) {

				if (!p.hasPermission("decoheads.reload")) {

					plugin.msg(p, "&cYou don't have permission to do that!");
					return true;

				}

				plugin.reloadConfig();
				plugin.msg(p, "Config reloaded!");

			} else {

				if (!p.hasPermission("decoheads.use")) {

					plugin.msg(p, "&cYou don't have permission to do that!");
					return true;

				}

				p.openInventory(InventoryManager.getInventory(1));

			}

		}

		return true;
	}

}
