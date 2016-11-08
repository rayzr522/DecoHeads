/**
 * 
 */
package com.rayzr522.decoheads.gui;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.rayzr522.decoheads.util.ArrayUtils;
import com.rayzr522.decoheads.util.TextUtils;

/**
 * @author Rayzr
 *
 */
public class Label extends Component {

    private String       name;
    private List<String> lore;

    /**
     * @param item
     * @param size
     */
    public Label(ItemStack item, Dimension size, String name, String... lore) {
        super(item, size);
        this.name = name;
        this.lore = Arrays.asList(lore);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.rayzr522.decoheads.gui.Component#simpleRender(org.bukkit.entity.
     * Player, int, int)
     */
    @Override
    public ItemStack simpleRender(Player player, int offsetX, int offsetY) {
        ItemStack newItem = item.clone();
        ItemMeta meta = newItem.getItemMeta();
        meta.setDisplayName(TextUtils.colorize(name));
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
    public void setLore(String... lore) {
        this.lore = Arrays.asList(lore);
    }

    /**
     * @param lore the lore to set
     */
    public void setLore(List<String> lore) {
        this.lore = lore;
    }

}
