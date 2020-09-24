package me.rayzr522.decoheads.util;

import com.google.common.collect.Multimap;
import me.rayzr522.decoheads.DecoHeads;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
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

    public static ItemStack getHead(String texture, String id) {
        Objects.requireNonNull(texture);
        Objects.requireNonNull(id);

        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        ItemMeta meta = item.getItemMeta();

        try {

            Object profile = GAME_PROFILE_CONSTRUCTOR.newInstance(UUID.fromString(id), null);
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

    @SuppressWarnings("unchecked")
    public static String getTexture(ItemMeta meta) {
        try {
            Object profile = Reflector.getFieldValue(meta, "profile");
            Multimap<String, Object> properties = (Multimap<String, Object>) Reflector.getFieldValue(profile, "properties");
            List<Object> textures = new ArrayList<>(properties.get("textures"));
            if (textures.size() < 1) {
                return null;
            }

            Object property = textures.get(0);
            return (String) Reflector.getFieldValue(property, "value");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
