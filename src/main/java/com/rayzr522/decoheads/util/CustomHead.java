
package com.rayzr522.decoheads.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.rayzr522.decoheads.DecoHeads;

public class CustomHead {

    private static Class<?>       GAMEPROFILE;
    private static Class<?>       PROPERTY;
    private static Class<?>       PROPERTY_MAP;

    private static Method         GET_PROPERTIES;
    private static Method         INSERT_PROPERTY;

    private static Constructor<?> GAMEPROFILE_CONSTRUCTOR;
    private static Constructor<?> PROPERTY_CONSTRUCTOR;

    static {
        try {
            // I somewhat copied this from mrCookieSlime, but I only really used
            // his code as a base so I could see what classes I needed to access
            // via Reflection and all that fun stuff :P

            GAMEPROFILE = Class.forName("com.mojang.authlib.GameProfile");
            PROPERTY = Class.forName("com.mojang.authlib.properties.Property");
            PROPERTY_MAP = Class.forName("com.mojang.authlib.properties.PropertyMap");

            GAMEPROFILE_CONSTRUCTOR = Reflector.getConstructor(GAMEPROFILE, 2);
            PROPERTY_CONSTRUCTOR = Reflector.getConstructor(PROPERTY, 2);

            GET_PROPERTIES = Reflector.getMethod(GAMEPROFILE, "getProperties");
            INSERT_PROPERTY = Reflector.getMethod(PROPERTY_MAP, "put");

        } catch (Exception e) {
            e.printStackTrace();
            DecoHeads.getInstance().err("Failed to load CustomHead", true);
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

            Object profile = GAMEPROFILE_CONSTRUCTOR.newInstance(new Object[] {
                    UUID.fromString(id), name
            });
            Object properties = GET_PROPERTIES.invoke(profile, new Object[0]);
            INSERT_PROPERTY.invoke(properties, new Object[] {
                    "textures", PROPERTY_CONSTRUCTOR.newInstance(new Object[] {
                            "textures", texture
                    })
            });

            Reflector.setFieldValue(meta, "profile", profile);

        } catch (Exception e) {
            System.err.println("Failed to create fake GameProfile for custom player head:");
            e.printStackTrace();
        }

        item.setItemMeta(meta);

        return item;
    }
}
