
package com.rayzr522.decoheads;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("unused")
public class InventoryManager {

	public static final String	INV_NAME	= TextUtils.colorize("&d&e&c&o" + "&c&l&nDecoHeads");
	public static final String	DIVIDER		= TextUtils.colorize("&8 |#|&e ");

	private static List<ItemStack> heads;
	
	public static final int WIDTH = 7;
	public static final int HEIGHT = 3;
	
	public static final int SIZE = WIDTH * HEIGHT;

	public static void loadHeads(DecoHeads plugin) {

		FileConfiguration config = plugin.getConfig();

		heads = new ArrayList<ItemStack>();

		for (String name : config.getConfigurationSection("heads").getKeys(false)) {

			ConfigurationSection section = config.getConfigurationSection("heads." + name);
			System.out.println("Adding head: " + name);
			heads.add(CustomHead.getHead(section.getString("texture"), section.getString("uuid"), "&e&n" + name));

		}

	}

	private static final ItemStack EMPTY = ItemUtils.setName(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15), " ");

	private static ItemStack BUTTON(String name) {

		return ItemUtils.setName(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5), "&a&l" + name);

	}

	public static Inventory getInventory(int page) {

		Inventory inv = Bukkit.createInventory(null, 54, INV_NAME + DIVIDER + "Page " + (page + 1));

		ItemStack[] items = new ItemStack[54];

		for (int i = 0; i < 9; i++) {

			setItem(items, EMPTY, i, 5);

		}

		// Re-enable this when multi-page support is set up
		// setItem(items, BUTTON("Previous Page"), 2, 5);
		// setItem(items, BUTTON("Next Page"), 6, 5);

		int offset = page * SIZE;

		for (int i = 0; i < SIZE; i++) {

			int pos = offset + i;

			if (pos >= heads.size()) {

				break;

			}

			setItem(items, heads.get(pos), 8 - WIDTH + i % WIDTH, 4 - HEIGHT + i / WIDTH);

		}

		inv.setContents(items);

		return inv;

	}

	public static void setItem(ItemStack[] inv, ItemStack item, int x, int y) {

		if (x + y * 9 > inv.length) { return; }

		inv[x + y * 9] = item;

	}

	public static int getPage(String invName) {

		try {
			return Integer.parseInt(invName.split(DIVIDER)[1].replace("Page ", ""));
		} catch (Exception e) {
			return -1;
		}

	}

}
