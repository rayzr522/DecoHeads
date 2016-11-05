
package com.rayzr522.decoheads;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class MenuHolder implements InventoryHolder {

    private Inventory inventory;
    private String    filter;

    public MenuHolder(Inventory inventory, String filter) {
        this.inventory = inventory;
        this.filter = filter;
    }

    public static Inventory makeInv(Player player, String title, int rows) {

        MenuHolder holder = new MenuHolder(Bukkit.createInventory(player, rows * 9, title), "");

        return Bukkit.createInventory(holder, rows, title);

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

}
