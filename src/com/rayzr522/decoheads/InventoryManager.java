
package com.rayzr522.decoheads;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

public class InventoryManager {

	public static final String	INV_NAME	= TextUtils.colorize("&d&e&c&o" + "&c&l&nDecoHeads");
	public static final String	DIVIDER		= " &8||&e ";

	private static List<ItemStack> heads;

	public static void loadHeads(DecoHeads plugin) {

		FileConfiguration config = plugin.getConfig();

		heads = new ArrayList<ItemStack>();

		for (String name : config.getConfigurationSection("heads").getKeys(false)) {

			ConfigurationSection section = config.getConfigurationSection("heads." + name);
			System.out.println("Adding head: " + name);
			heads.add(ItemUtils.setLore(CustomHead.getHead(section.getString("texture"), section.getString("uuid"), "&e&n" + name), "", "&7Made with &c&lDecoHeads", ""));

		}

	}

	private static final ItemStack EMPTY = ItemUtils.setName(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15), " ");

	private static ItemStack BUTTON(String name) {

		return ItemUtils.setName(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5), "&a&l" + name);

	}

	public static ItemStack[] getInventory(int page) {

		ItemStack[] items = new ItemStack[54];

		for (int i = 0; i < 9; i++) {

			setItem(items, EMPTY, i, 5);

		}

		setItem(items, BUTTON("Previous Page"), 2, 5);
		setItem(items, BUTTON("Next Page"), 6, 5);

		int offset = page * 24;

		for (int i = 0; i < 24; i++) {

			int pos = offset + i;

			if (pos >= heads.size()) {

				break;

			}

			setItem(items, heads.get(pos), 1 + i % 8, 1 + i / 8);

		}

		return items;

	}

	public static void setItem(ItemStack[] inv, ItemStack item, int x, int y) {

		if (x + y * 9 > inv.length) { return; }

		inv[x + y * 9] = item;

	}

	public static int getPage(String invName) {

		try {
			return Integer.parseInt(TextUtils.uncolorize(invName).split(DIVIDER)[1].replace("Page ", ""));
		} catch (Exception e) {
			return -1;
		}

	}

}
