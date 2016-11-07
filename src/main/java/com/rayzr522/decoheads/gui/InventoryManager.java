
package com.rayzr522.decoheads.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.rayzr522.decoheads.DecoHeads;
import com.rayzr522.decoheads.util.ArrayUtils;
import com.rayzr522.decoheads.util.CustomHead;
import com.rayzr522.decoheads.util.ItemUtils;
import com.rayzr522.decoheads.util.TextUtils;

@SuppressWarnings("unused")
public class InventoryManager {

    private static List<ItemStack> heads;

    public static final int        WIDTH  = 7;
    public static final int        HEIGHT = 3;

    public static final int        SIZE   = WIDTH * HEIGHT;

    public static void loadHeads(DecoHeads plugin) {
        FileConfiguration config = plugin.getConfig();

        heads = new ArrayList<ItemStack>();

        ConfigurationSection headsSection = config.getConfigurationSection("heads");

        for (String name : headsSection.getKeys(false)) {
            if (!headsSection.isConfigurationSection(name)) {
                continue;
            }

            ConfigurationSection section = config.getConfigurationSection("heads." + name);
            if (!section.contains("texture") || !section.isString("texture")) {
                plugin.log(String.format("The head '%s' did not have a key of type '%s' named '%s'", name, "String", "texture"));
                continue;
            }
            if (!section.contains("uuid") || !section.isString("uuid")) {
                plugin.log(String.format("The head '%s' did not have a key of type '%s' named '%s'", name, "String", "texture"));
                continue;
            }
            heads.add(CustomHead.getHead(section.getString("texture"), section.getString("uuid"), plugin.tr("item.name", name)));
        }

        if (heads.size() < 1) {
            plugin.logger.err("No heads were found in the config!", true);
        }
    }

    private static final ItemStack EMPTY           = ItemUtils.setName(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15), " ");

    private static ItemStack       BUTTON_ENABLED  = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
    private static ItemStack       BUTTON_DISABLED = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);

    private static ItemStack BUTTON(String name, boolean enabled) {

        return ItemUtils.setName((enabled ? BUTTON_ENABLED : BUTTON_DISABLED).clone(), (enabled ? "&a" : "&c") + "&l" + name);

    }

    private static ItemStack BUTTON(String name) {

        return BUTTON(name, true);

    }

    private static List<ItemStack> searchHeads(String filter) {

        if (filter == null || filter.equals("")) {
            return heads;
        }

        filter = filter.toLowerCase();

        List<ItemStack> filteredHeads = new ArrayList<ItemStack>();

        for (ItemStack item : heads) {

            if (!ItemUtils.isValid(item) || item.getItemMeta() == null || !item.getItemMeta().hasDisplayName()) {
                break;
            }

            String name = TextUtils.stripColor(item.getItemMeta().getDisplayName()).toLowerCase();
            if (name.contains(filter)) {
                filteredHeads.add(item);
            }

        }

        return filteredHeads;
    }

    public static Inventory getInventory(Player player, String filter, int page) {

        List<ItemStack> filteredHeads = searchHeads(filter);

        if (filteredHeads == null || filteredHeads.size() < 1) {
            return null;
        }

        MenuHolder holder = new MenuHolder(player, page, filter);
        Inventory inv = holder.getInventory();

        ItemStack[] items = new ItemStack[54];

        for (int i = 0; i < 9; i++) {

            setItem(items, EMPTY, i, 5);

        }

        setItem(items, BUTTON("Previous Page", page > 1), 2, 5);
        setItem(items, BUTTON("Next Page", page < maxPages(filteredHeads)), 6, 5);

        int offset = (page - 1) * SIZE;

        for (int i = 0; i < SIZE; i++) {

            int pos = offset + i;
            if (pos >= filteredHeads.size()) {
                break;
            }

            setItem(items, filteredHeads.get(pos), 8 - WIDTH + i % WIDTH, 4 - HEIGHT + i / WIDTH);

        }

        inv.setContents(items);

        return inv;

    }

    public static void setItem(ItemStack[] inv, ItemStack item, int x, int y) {

        if (x + y * 9 > inv.length) {
            return;
        }

        inv[x + y * 9] = item;

    }

    /**
     * Checks if the given item is an enabled button
     * 
     * @param item the item
     * @return
     */
    public static boolean isButton(ItemStack item) {
        return ItemUtils.isValid(item) && (BUTTON_ENABLED.getType() == item.getType()) && (BUTTON_ENABLED.getAmount() == item.getAmount()) && (BUTTON_ENABLED.getDurability() == item.getDurability());
    }

    public static int maxPages() {
        return maxPages(heads);
    }

    public static int maxPages(List<ItemStack> heads) {
        return (int) Math.floor(heads.size() / SIZE);
    }

    /**
     * @param args the current args
     * @return a list of all the names of the heads
     */
    public static List<String> headsList(String[] args) {
        if (args.length < 2) {
            return Collections.emptyList();
        }
        String filter = ArrayUtils.concat(Arrays.copyOfRange(args, 1, args.length), " ");
        List<String> headsList = new ArrayList<String>();
        for (ItemStack item : heads) {
            String name = ChatColor.stripColor(item.getItemMeta().getDisplayName());
            if (name.equalsIgnoreCase(filter)) {
                return Collections.emptyList();
            }
            if (!name.startsWith(filter)) {
                continue;
            }
            headsList.add(name);
        }
        return headsList;
    }

}
