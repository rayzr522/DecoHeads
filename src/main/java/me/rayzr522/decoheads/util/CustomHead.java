package me.rayzr522.decoheads.util;

import me.rayzr522.decoheads.DecoHeads;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

public class CustomHead {
    private static Method GET_PROPERTIES;
    private static Method INSERT_PROPERTY;

    private static Constructor<?> GAME_PROFILE_CONSTRUCTOR;
    private static Constructor<?> PROPERTY_CONSTRUCTOR;

    static {
        try {
            // I somewhat copied this from mrCookieSlime, but I only really used
            // his code as a base so I could see what classes I needed to access
            // via Reflection and all that fun stuff :P

            Class<?> gameProfile = Class.forName("com.mojang.authlib.GameProfile");
            Class<?> property = Class.forName("com.mojang.authlib.properties.Property");
            Class<?> propertyMap = Class.forName("com.mojang.authlib.properties.PropertyMap");

            GAME_PROFILE_CONSTRUCTOR = Reflector.getConstructor(gameProfile, 2);
            PROPERTY_CONSTRUCTOR = Reflector.getConstructor(property, 2);

            GET_PROPERTIES = Reflector.getMethod(gameProfile, "getProperties");
            INSERT_PROPERTY = Reflector.getMethod(propertyMap, "put");

        } catch (Exception e) {
            DecoHeads.getInstance().getLogger().log(Level.SEVERE, "Failed to load CustomHead:", e);
            Bukkit.getPluginManager().disablePlugin(DecoHeads.getInstance());
        }
    }

    public static ItemStack getHead(String texture, String id, String name) {
        Objects.requireNonNull(texture);
        Objects.requireNonNull(id);
        Objects.requireNonNull(name);

        String colorName = TextUtils.colorize(name);

        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(colorName);

        try {

            Object profile = GAME_PROFILE_CONSTRUCTOR.newInstance(UUID.fromString(id), name);
            Object properties = GET_PROPERTIES.invoke(profile);
            INSERT_PROPERTY.invoke(properties, "textures", PROPERTY_CONSTRUCTOR.newInstance("textures", texture));

            Reflector.setFieldValue(meta, "profile", profile);

        } catch (Exception e) {
            System.err.println("Failed to create fake GameProfile for custom player head:");
            e.printStackTrace();
        }

        item.setItemMeta(meta);

        return item;
    }
}
