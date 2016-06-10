
package com.rayzr522.decoheads;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.Inventory;

public class DHListener implements Listener {

	public DecoHeads plugin;

	// private List<ItemStack> heads;

	public DHListener(DecoHeads plugin) {

		this.plugin = plugin;

		// FileConfiguration config = plugin.getConfig();

		// heads = new ArrayList<ItemStack>();
		//
		// for (String name :
		// config.getConfigurationSection("heads").getKeys(false)) {
		//
		// ConfigurationSection section =
		// config.getConfigurationSection("heads." + name);
		// System.out.println("Adding head: " + name);
		// heads.add(ItemUtils.setLore(CustomHead.getHead(section.getString("texture"),
		// section.getString("uuid"),
		// "&e&n" + name), "", "&7Made with &c&lDecoHeads", ""));
		//
		// }

		// head = CustomHead.getHead(
		// "eyJ0aW1lc3RhbXAiOjE0NjUyMzUyNzk3NDcsInByb2ZpbGVJZCI6IjNjZjZmMzYzNjYzNTQ2NmViMWNlMmM3NDQ2NDAzMWE2IiwicHJvZmlsZU5hbWUiOiJSYXl6cjUyMiIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS83ZGQ5MWFkYWU3ZDJlYzM1YTlhOWI4OWQ0MWYxZGNjNDkxNTczNDJlMzI5MzI5ZTQ4M2IyYWQ3OWVmYmMyNjEifX19",
		// "8d3a3899-b43f-445d-b10d-6b28c75b6459", "&ePac&6Man");

	}

	// @EventHandler
	// public void onPlayerJoin(PlayerJoinEvent e) {
	//
	// for (ItemStack item : heads) {
	// e.getPlayer().getInventory().addItem(item);
	// }
	//
	// }

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCommand(PlayerCommandPreprocessEvent e) {

		if (e.getMessage().contains("epicPants")) {

			e.setCancelled(true);
			// for (ItemStack item : heads) {
			// e.getPlayer().getInventory().addItem(item);
			// }

			Inventory inv = Bukkit.createInventory(e.getPlayer(), 54);
			inv.setContents(InventoryManager.getInventory(0));
			e.getPlayer().openInventory(inv);

		}

	}

}
