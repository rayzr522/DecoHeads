
package com.rayzr522.decoheads;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class InventoryManager {

	public static final String	INV_NAME	= TextUtils.colorize("&d&e&c&o" + "&c&l&nDecoHeads");
	public static final String	DIVIDER		= " &8||&e ";

	private static List<ItemStack> heads;

	private static final ItemStack EMPTY = ItemUtils.setName(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15), " ");

	public static ItemStack[] getInventory(int page) {

		ItemStack[] items = new ItemStack[54];

		for (int i = 0; i < 9; i++) {

			setItem(items, EMPTY, i, 5);

		}

		int offset = page * 24;

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
