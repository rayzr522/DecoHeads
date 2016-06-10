package com.rayzr522.decoheads;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomHead {

	private static Class<?> GAMEPROFILE;
	private static Class<?> PROPERTY;
	private static Class<?> PROPERTY_MAP;

	private static Method GET_PROPERTIES;
	private static Method INSERT_PROPERTY;

	private static Constructor<?> GAMEPROFILE_CONSTRUCTOR;
	private static Constructor<?> PROPERTY_CONSTRUCTOR;

	static {

		try {

			// Might look familiar to mrCookieSlime :P
			// If you (mrCookieSlime) have a problem with this, just let me know...
			// I guess I could try to find a different way, although yours was pretty good
			if (Reflector.getVersion().startsWith("v1_7_")) {
				GAMEPROFILE = Class.forName("net.minecraft.util.com.mojang.authlib.GameProfile");
				PROPERTY = Class.forName("net.minecraft.util.com.mojang.authlib.properties.Property");
				PROPERTY_MAP = Class.forName("com.mojang.authlib.properties.PropertyMap");
			} else {
				GAMEPROFILE = Class.forName("com.mojang.authlib.GameProfile");
				PROPERTY = Class.forName("com.mojang.authlib.properties.Property");
				PROPERTY_MAP = Class.forName("com.mojang.authlib.properties.PropertyMap");
			}

			GAMEPROFILE_CONSTRUCTOR = Reflector.getConstructor(GAMEPROFILE, 2);
			PROPERTY_CONSTRUCTOR = Reflector.getConstructor(PROPERTY, 2);

			GET_PROPERTIES = Reflector.getMethod(GAMEPROFILE, "getProperties");
			INSERT_PROPERTY = Reflector.getMethod(PROPERTY_MAP, "put");

		} catch (Exception e) {
			
			e.printStackTrace();
			System.err.println("Failed to load CustomHead. Disabling DecoHeads.");
			Bukkit.getServer().getPluginManager().disablePlugin(DecoHeads.INSTANCE);

		}

	}

	public static ItemStack getHead(String texture, String id, String name) {

		String colorName = TextUtils.colorize(name);

		ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);

		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(colorName);

		try {

			Object profile = GAMEPROFILE_CONSTRUCTOR
					.newInstance(new Object[] { UUID.fromString(id), TextUtils.stripColor(colorName) });
			Object properties = GET_PROPERTIES.invoke(profile, new Object[0]);
			INSERT_PROPERTY.invoke(properties, new Object[] { "textures",
					PROPERTY_CONSTRUCTOR.newInstance(new Object[] { "textures", texture }) });

			Reflector.setFieldValue(meta, "profile", profile);

		} catch (Exception e) {

			System.err.println("Failed to get create profile for custom player head:");
			e.printStackTrace();

		}

		item.setItemMeta(meta);

		return item;

	}

}
