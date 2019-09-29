package me.rayzr522.decoheads.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ItemUtils {
    public static boolean isInvalid(ItemStack item) {
        return item == null || item.getType() == Material.AIR;
    }

    public static ItemStack setName(ItemStack item, String name) {
        if (isInvalid(item)) {
            return item;
        }

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(TextUtils.colorize(name));
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack setLore(ItemStack item, String... lore) {
        if (isInvalid(item) || lore == null) {
            return item;
        }

        ItemMeta meta = item.getItemMeta();
        List<String> loreList = Arrays.stream(lore)
                .map(TextUtils::colorize)
                .collect(Collectors.toList());

        meta.setLore(loreList);
        item.setItemMeta(meta);

        return item;
    }

}
