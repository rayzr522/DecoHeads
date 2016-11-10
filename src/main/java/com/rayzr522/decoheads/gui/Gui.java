/**
 * 
 */
package com.rayzr522.decoheads.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.rayzr522.decoheads.DecoHeads;
import com.rayzr522.decoheads.util.TextUtils;

/**
 * @author Rayzr
 *
 */
public class Gui implements InventoryHolder {

    private List<Component> components = new ArrayList<Component>();
    private Player          player;
    private Inventory       inventory;
    private Dimension       size;

    public Gui(Player player, String title, int rows) {
        if (rows < 1 || rows > 6) {
            throw new IllegalArgumentException("Rows must be between 1 and 6!");
        }

        this.player = player;
        inventory = Bukkit.createInventory(this, rows * 9, TextUtils.colorize(title));
        size = new Dimension(9, rows * 9);
    }

    public void render() {

        for (Component comp : components) {
            Dimension position = comp.getPosition();
            if (!position.add(comp.getSize()).fitsInside(size)) {
                DecoHeads.getInstance().log("Invalid component at " + position.toString());
                continue;
            }
            comp.render(player, this, position);
        }

        player.closeInventory();
        player.openInventory(getInventory());

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.bukkit.inventory.InventoryHolder#getInventory()
     */
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * @param x the x position of the item
     * @param y the y position of the item
     * @param item the item itself
     */
    public void setItem(int x, int y, ItemStack item) {
        Dimension pos = new Dimension(x, y);
        if (!pos.fitsInside(size)) {
            throw new IllegalArgumentException("Can't set item at position " + pos.toString() + ": position excedes GUI bounds!");
        }
        inventory.setItem(x + y * 9, item);
    }

    /**
     * Handles an InventoryClickEvent
     * 
     * @param e the event itself
     */
    public void onClick(InventoryClickEvent e) {
        int x = e.getRawSlot() % 9;
        int y = e.getRawSlot() / 9;
        Dimension pos = new Dimension(x, y);
        components.stream().filter(comp -> {
            return new Dimension(x, y).subtract(comp.getPosition()).fitsInside(comp.getSize());
        }).reduce((a, b) -> b).ifPresent(comp -> {
            ClickEvent event = new ClickEvent((Player) e.getWhoClicked(), e.getCurrentItem(), e.getClick(), pos.subtract(comp.getPosition()));
            comp.onClick(event);
            e.setCancelled(event.isCancelled());
            if (event.shouldClose()) {
                player.closeInventory();
            }
        });
    }

    /**
     * Adds a {@link Component} to this GUI
     * 
     * @param comp the component to add
     * @return If the component was added
     */
    public boolean addComponent(Component comp) {
        if (!comp.getSize().add(comp.getPosition()).fitsInside(size)) {
            return false;
        }
        components.add(comp);
        return true;
    }

}
