package me.rayzr522.decoheads.gui.system;

import me.rayzr522.decoheads.DecoHeads;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

class MenuHolder implements InventoryHolder {

    private Inventory inventory;
    private String filter;
    private int page;

    private MenuHolder(Player player, int page, String filter) {
        this.inventory = Bukkit.createInventory(this, 54, DecoHeads.getInstance().tr("gui.title", page));
        this.filter = filter;
        this.page = page;
    }

    public static Inventory makeInv(Player player, int page) {
        MenuHolder holder = new MenuHolder(player, page, null);
        return holder.getInventory();
    }

    public String getFilter() {
        return filter == null ? "" : filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * @return the page
     */
    public int getPage() {
        return page;
    }

}
