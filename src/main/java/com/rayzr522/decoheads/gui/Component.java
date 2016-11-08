/**
 * 
 */
package com.rayzr522.decoheads.gui;

import java.util.Objects;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Rayzr
 *
 */
public abstract class Component {

    protected ItemStack item;
    protected Dimension size;

    /**
     * Constructs a new component with the given item icon and size
     * 
     * @param item the item to display
     * @param size the size of the component
     */
    public Component(ItemStack item, Dimension size) {
        Objects.requireNonNull(item);
        Objects.requireNonNull(size);
        if (item.getType() == Material.AIR) {
            throw new IllegalArgumentException("Item cannot be AIR!");
        }
        this.item = item;
        this.size = size;
    }

    /**
     * @return the item
     */
    public ItemStack getItem() {
        return item;
    }

    /**
     * @param item the item to set
     */
    public void setItem(ItemStack item) {
        this.item = item;
    }

    /**
     * @return the size
     */
    public Dimension getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(Dimension size) {
        this.size = size;
    }

    /**
     * Renders this component to the given position
     * 
     * @param player the player that owns the inventory
     * @param gui the gui itself
     * @param offset the offset of the component
     */
    public void render(Player player, Gui gui, Dimension offset) {
        for (int x = 0; x < size.getX(); x++) {
            for (int y = 0; y < size.getY(); y++) {
                gui.setItem(offset.getX() + x, offset.getY() + y, simpleRender(player, x, y));
            }
        }
    }

    /**
     * A simple rendering method to get an itemstack for a specific slot.
     * Override this if you don't want to have to re-write the rectangular
     * for-loop.
     * 
     * @param player the player that owns the inventory
     * @param offsetX the offset on the x axis
     * @param offsetY the offset on the y axis
     * @return the ItemStack for the given position
     */
    public ItemStack simpleRender(Player player, int offsetX, int offsetY) {
        return item;
    }

    public abstract void onClick(ClickEvent e);

}
