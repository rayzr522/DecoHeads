package me.rayzr522.decoheads.gui.system;

import me.rayzr522.decoheads.DecoHeads;
import me.rayzr522.decoheads.util.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rayzr
 */
public class GUI implements InventoryHolder {
    private List<Component> components = new ArrayList<>();
    private Player player;
    private Inventory inventory;
    private Dimension size;

    protected GUI(Player player, String title, int rows) {
        if (rows < 1 || rows > 6) {
            throw new IllegalArgumentException("Rows must be between 1 and 6!");
        }

        this.player = player;
        inventory = Bukkit.createInventory(this, rows * 9, TextUtils.colorize(title));
        size = new Dimension(9, rows * 9);
    }

    public void render() {
        inventory.setContents(new ItemStack[0]);

        components.forEach(comp -> {
            Dimension position = comp.getPosition();
            if (!position.add(comp.getSize()).fitsInside(size)) {
                DecoHeads.getInstance().getLogger().warning("Invalid component at " + position.toString());
                return;
            }
            comp.render(player, this, position);
        });

        if (player.getOpenInventory().getTopInventory().getHolder() != this) {
            player.closeInventory();
            player.openInventory(getInventory());
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * @param x    The X position of the item.
     * @param y    The Y position of the item.
     * @param item The {@link ItemStack} to set.
     */
    protected void setItem(int x, int y, ItemStack item) {
        Dimension pos = new Dimension(x, y);
        if (!pos.fitsInside(size)) {
            throw new IllegalArgumentException("Can't set item at position " + pos.toString() + ": position exceeds GUI bounds!");
        }
        inventory.setItem(x + y * 9, item);
    }

    /**
     * Handles an InventoryClickEvent
     *
     * @param raw The raw event.
     */
    public void onClick(InventoryClickEvent raw) {
        int slot = raw.getRawSlot();

        Dimension pos = new Dimension(slot % 9, slot / 9);
        components.stream()
                .filter(comp -> pos.getX() >= comp.getPosition().getX() && pos.getX() < comp.getPosition().getX() + comp.getSize().getX()
                        && pos.getY() >= comp.getPosition().getY() && pos.getY() < comp.getPosition().getY() + comp.getSize().getY())
                .reduce((a, b) -> b)
                .ifPresent(comp -> {
                    ClickEvent event = new ClickEvent((Player) raw.getWhoClicked(), this, raw.getCurrentItem(), raw.getClick(), pos.subtract(comp.getPosition()));
                    comp.onClick(event);
                    raw.setCancelled(event.isCancelled());
                    if (event.shouldClose() && player.getOpenInventory().getTopInventory().getHolder() == this) {
                        player.closeInventory();
                    }
                });
    }

    /**
     * Adds a {@link Component} to this GUI
     *
     * @param comp the component to add
     */
    protected void addComponent(Component comp) {
        if (!comp.getSize().add(comp.getPosition()).fitsInside(size)) {
            return;
        }
        components.add(comp);
    }

    /**
     * @param components the components to set
     */
    protected void setComponents(List<Component> components) {
        this.components = new ArrayList<>(components);
    }
}
