
package com.rayzr522.decoheads;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class DHListener implements Listener {

	public DecoHeads plugin;

	public DHListener(DecoHeads plugin) {

		this.plugin = plugin;

	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {

		Inventory inv = e.getClickedInventory();

		if (!inv.getName().startsWith(InventoryManager.INV_NAME)) { return; }

		if (!(e.getWhoClicked() instanceof Player)) { return; }

		e.setCancelled(true);

		if (e.getCurrentItem().getType() == Material.SKULL_ITEM) {

			((Player) e.getWhoClicked()).getInventory().addItem(ItemUtils.setLore(e.getCurrentItem().clone(), "", "&7Made with &c&lDecoHeads", ""));

		}

	}

}
