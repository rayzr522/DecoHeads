
package com.rayzr522.decoheads;

import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

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

			if (args.length > 0) {

				String arg = args[0].toLowerCase();

				if (arg.equals("reload") || arg.equals("rl")) {

					if (!p.hasPermission("decoheads.reload")) {

						plugin.msg(p, "&cYou don't have permission to do that!");
						return true;

					}

					plugin.reloadConfig();
					plugin.msg(p, "Config reloaded!");

				} else if (arg.equals("search") || arg.equals("find")) {

					if (args.length < 2) {

						plugin.msg(p, "&cYou must specify what to search for!");
						plugin.msg(p, "&e/decoheads find|search <text>");
						return true;

					}

					String search = ArrayUtils.concat(Arrays.copyOfRange(args, 1, args.length), " ");
					Inventory inv = InventoryManager.getInventory(p, search, 1);

					if (inv == null) {
						plugin.msg(p, "No heads were found that matched '" + search + "'");
					} else {
						p.openInventory(inv);
					}

				} else {

					try {

						int page = Integer.parseInt(args[0]);

						if (page >= 1 && page <= InventoryManager.maxPages()) {

							if (!p.hasPermission("decoheads.use")) {

								plugin.msg(p, "&cYou don't have permission to do that!");
								return true;

							}

							p.openInventory(InventoryManager.getInventory(p, "", page));

						} else {

							plugin.msg(p, "&cNo such page (min: 1, max: " + InventoryManager.maxPages() + ")");

						}

					} catch (Exception e) {

					}

				}

			} else {

				if (!p.hasPermission("decoheads.use")) {

					plugin.msg(p, "&cYou don't have permission to do that!");
					return true;

				}

				p.openInventory(InventoryManager.getInventory(p, "", 1));

			}

		}

		return true;

	}

}
