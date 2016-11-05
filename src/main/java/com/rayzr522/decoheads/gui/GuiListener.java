
package com.rayzr522.decoheads.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.rayzr522.decoheads.DecoHeads;
import com.rayzr522.decoheads.util.ItemUtils;

public class GuiListener implements Listener {

    public DecoHeads plugin;

    public GuiListener(DecoHeads plugin) {

        this.plugin = plugin;

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }

        if (!(e.getInventory().getHolder() instanceof MenuHolder)) {
            return;
        }

        if (!ItemUtils.isValid(e.getCurrentItem())) {
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

            int page = holder.getPage();

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
