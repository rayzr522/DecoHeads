/**
 * 
 */
package com.rayzr522.decoheads.gui.system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    public Label(ItemStack item, Dimension size, Dimension position, String name, String... lore) {
        super(item, size, position);
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
    public void setLore(String... lore) {
        setLore(Arrays.asList(lore));
    }

    /**
     * @param lore the lore to set
     */
    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public void addLore(String... lore) {
        addLore(Arrays.asList(lore));
    }

    public void addLore(List<String> lore) {
        ArrayList<String> newLore = new ArrayList<String>();
        newLore.addAll(this.lore);
        newLore.addAll(lore);
        this.lore = Collections.unmodifiableList(newLore);
    }

}
