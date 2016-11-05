
package com.rayzr522.decoheads.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.rayzr522.decoheads.util.TextUtils;

public class MenuHolder implements InventoryHolder {

    private Inventory inventory;
    private String    filter;
    private int       page;

    public MenuHolder(Player player, int page, String filter) {
        // TODO: load the title from the Localization
        String title = "Decoheads inventory";
        this.inventory = Bukkit.createInventory(this, 54, TextUtils.colorize(title));
        this.filter = filter;
        this.page = page;
    }

    public static Inventory makeInv(Player player, int page) {
        MenuHolder holder = new MenuHolder(player, page, null);
        return holder.getInventory();
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getFilter() {
        return filter == null ? "" : filter;
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
