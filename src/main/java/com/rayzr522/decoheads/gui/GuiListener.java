
package com.rayzr522.decoheads.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

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

        if (!(e.getInventory().getHolder() instanceof Gui)) {
            return;
        }

        if (!ItemUtils.isValid(e.getCurrentItem())) {
            return;
        }

        e.setCancelled(true);

        if (e.getRawSlot() >= e.getInventory().getSize()) {
            return;
        }

        Gui holder = (Gui) e.getInventory().getHolder();

        holder.onClick(e);
    }

}
