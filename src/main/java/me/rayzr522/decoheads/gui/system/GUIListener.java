package me.rayzr522.decoheads.gui.system;

import me.rayzr522.decoheads.util.ItemUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getWhoClicked() == null || e.getInventory() == null) {
            return;
        }

        if (!(e.getWhoClicked() instanceof Player) || !(e.getInventory().getHolder() instanceof GUI)) {
            return;
        }

        if (ItemUtils.isInvalid(e.getCurrentItem())) {
            return;
        }

        e.setCancelled(true);

        // They clicked outside of the top inventory, in their own inventory.
        if (e.getRawSlot() >= e.getInventory().getSize()) {
            return;
        }

        ((GUI) e.getInventory().getHolder()).onClick(e);
    }

}
