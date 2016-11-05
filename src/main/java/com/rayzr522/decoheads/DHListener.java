
package com.rayzr522.decoheads;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class DHListener implements Listener {

    public DecoHeads plugin;

    public DHListener(DecoHeads plugin) {

        this.plugin = plugin;

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {

        Inventory inv = e.getInventory();

        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }

        if (!(e.getInventory().getHolder() instanceof MenuHolder)) {
            return;
        }

        e.setCancelled(true);

        if (e.getRawSlot() >= 54) {
            return;
        }

        ItemStack clicked = e.getCurrentItem();
        Player p = (Player) e.getWhoClicked();
        MenuHolder holder = (MenuHolder) e.getInventory().getHolder();

        if (clicked.getType() == Material.SKULL_ITEM) {

            p.getInventory().addItem(ItemUtils.setLore(clicked.clone(), "", "&7Made with &c&lDecoHeads", ""));

        } else if (InventoryManager.isButton(clicked)) {

            boolean next = clicked.getItemMeta().getDisplayName().contains("Next");

            int page = InventoryManager.getPage(inv.getName());

            if (next) {

                p.closeInventory();
                p.openInventory(InventoryManager.getInventory(p, holder.getFilter(), page + 1));

            } else {

                p.closeInventory();
                p.openInventory(InventoryManager.getInventory(p, holder.getFilter(), page - 1));

            }

        }

    }

}
