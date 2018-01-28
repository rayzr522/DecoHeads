/**
 *
 */
package me.rayzr522.decoheads.gui.system;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Rayzr
 */
public class ClickEvent {

    private Player player;
    private GUI gui;
    private ItemStack item;
    private ClickType type;
    private int offsetX;
    private int offsetY;
    private boolean cancelled = true;
    private boolean shouldClose = false;

    ClickEvent(Player player, GUI gui, ItemStack item, ClickType type, Dimension offset) {
        this.player = player;
        this.gui = gui;
        this.item = item;
        this.type = type;
        this.offsetX = offset.getX();
        this.offsetY = offset.getY();
    }

    /**
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return the item
     */
    public ItemStack getItem() {
        return item;
    }

    /**
     * @return the type
     */
    public ClickType getType() {
        return type;
    }

    /**
     * @return the offsetX
     */
    public int getOffsetX() {
        return offsetX;
    }

    /**
     * @return the offsetY
     */
    public int getOffsetY() {
        return offsetY;
    }

    /**
     * @return Whether or not the event is cancelled. By default this is
     * {@code true}.
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * @param cancelled whether or not the event should be cancelled
     */
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * @return Whether or not the GUI should close. By default this is
     * {@code false}.
     */
    public boolean shouldClose() {
        return shouldClose;
    }

    /**
     * @param shouldClose whether or not the GUI should close
     */
    public void setShouldClose(boolean shouldClose) {
        this.shouldClose = shouldClose;
    }

    /**
     * @return The GUI that this event is associated with
     */
    public GUI getGui() {
        return gui;
    }

}
