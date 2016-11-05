
package com.rayzr522.decoheads.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUtils {

    public static boolean isValid(ItemStack item) {
        return (item != null && item.getType() != Material.AIR);
    }

    public static ItemStack setName(ItemStack item, String name) {
        if (!isValid(item)) {
            return item;
        }

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(TextUtils.colorize(name));
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack setLore(ItemStack item, String... lore) {
        if (!isValid(item) || lore == null) {
            return item;
        }

        ItemMeta meta = item.getItemMeta();
        List<String> loreList = new ArrayList<String>();

        for (String str : lore) {

            loreList.add(TextUtils.colorize(str));

        }

        meta.setLore(loreList);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack addLore(ItemStack item, String lore) {
        if (!isValid(item)) {
            return item;
        }

        ItemMeta meta = item.getItemMeta();

        if (meta.getLore() == null) {
            setLore(item, new String[] { TextUtils.colorize(lore) });
        } else {
            meta.getLore().add(TextUtils.colorize(lore));
        }

        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack clearLore(ItemStack item) {
        if (!isValid(item)) {
            return item;
        }

        ItemMeta meta = item.getItemMeta();
        meta.setLore(null);
        item.setItemMeta(meta);

        return item;
    }

}
