
package com.rayzr522.decoheads;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DHCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args) {

		if (!(sender instanceof Player)) {

			System.out.println("This command is only useable by players");
			return true;

		}

		if (cmd.equalsIgnoreCase("decoheads") || cmd.equalsIgnoreCase("dh") || cmd.equalsIgnoreCase("heads")) {

			Player p = (Player) sender;

			p.openInventory(InventoryManager.getInventory(0));

		}

		return true;
	}

}
