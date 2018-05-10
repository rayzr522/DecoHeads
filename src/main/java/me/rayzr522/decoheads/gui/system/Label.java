/**
 *
 */
package me.rayzr522.decoheads.gui.system;

import me.rayzr522.decoheads.util.ArrayUtils;
import me.rayzr522.decoheads.util.TextUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Rayzr
 */
public class Label extends Component {

    private String name;
    private List<String> lore;

    /**
     * Constructs a new {@link Label}.
     *
     * @param item     The {@link ItemStack item} to use.
     * @param size     The size of the label.
     * @param position The position of the label.
     * @param name     The name of the label.
     * @param lore     The lore for the label.
     */
    public Label(ItemStack item, Dimension size, Dimension position, String name, String... lore) {
        super(item, size, position);
        this.name = name;
        this.lore = Arrays.asList(lore);
    }

    public static Label background(int x, int y, int width, int height) {
        return new Label(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15), new Dimension(width, height), new Dimension(x, y), " ");
    }

    @Override
    public ItemStack simpleRender(Player player, int offsetX, int offsetY) {
        ItemStack newItem = item.clone();
        ItemMeta meta = newItem.getItemMeta();
        meta.setDisplayName(TextUtils.colorize("&r" + name));
        meta.setLore(ArrayUtils.colorize(lore));
        newItem.setItemMeta(meta);
        return newItem;
    }

    @Override
    public void onClick(ClickEvent e) {
        // IGNORE
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the lore
     */
    public List<String> getLore() {
        return lore;
    }

    /**
     * @param lore the lore to set
     */
    private void setLore(List<String> lore) {
        this.lore = lore;
    }

    /**
     * @param lore the lore to set
     */
    public void setLore(String... lore) {
        setLore(Arrays.asList(lore));
    }

    public void addLore(String... lore) {
        addLore(Arrays.asList(lore));
    }

    private void addLore(List<String> lore) {
        ArrayList<String> newLore = new ArrayList<>();
        newLore.addAll(this.lore);
        newLore.addAll(lore);
        this.lore = Collections.unmodifiableList(newLore);
    }

}
