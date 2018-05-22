package me.rayzr522.decoheads.util;

import me.rayzr522.decoheads.DecoHeads;
import org.bukkit.Bukkit;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;

public class Reflector {

    private static String version = "";

    static {
        try {
            String[] split = Bukkit.getServer().getClass().getPackage().getName().split("\\.");
            version = split[split.length - 1];

            Class<?> craftPlayer = getCraftBukkitClass("entity.CraftPlayer");
            Class<?> craftEntity = getCraftBukkitClass("entity.CraftEntity");
            Class<?> craftServer = getCraftBukkitClass("CraftServer");
            Class<?> craftWorld = getCraftBukkitClass("CraftWorld");

            // Just some quick hacks, I should probably actually uhh... check this?
            assert craftPlayer != null;
            assert craftEntity != null;
            assert craftServer != null;
            assert craftWorld != null;
        } catch (Exception e) {
            DecoHeads.getInstance().getLogger().log(Level.SEVERE, "Failed to load Reflector", e);
            Bukkit.getPluginManager().disablePlugin(DecoHeads.getInstance());
        }
    }

    private static String getVersion() {
        return version;
    }

    public static int getMinorVersion() {
        return Integer.parseInt(getVersion().substring(1).split("_")[1]);
    }

    private static Class<?> getCraftBukkitClass(String name) throws ClassNotFoundException {
        return Class.forName("org.bukkit.craftbukkit." + version + "." + name);
    }

    public static Method getMethod(Class<?> clazz, String name) {
        for (Method m : clazz.getMethods()) {
            if (m.getName().equals(name)) {
                return m;
            }
        }

        return null;
    }

    private static Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        return clazz.getDeclaredField(fieldName);
    }

    public static void setFieldValue(Object object, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field f = getField(object.getClass(), fieldName);
        f.setAccessible(true);
        f.set(object, value);
    }

    public static Constructor<?> getConstructor(Class<?> clazz, int numParams) {
        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (constructor.getParameterTypes().length == numParams) {
                return constructor;
            }
        }

        return null;
    }

    public static Object getFieldValue(Object object, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field f = getField(object.getClass(), fieldName);
        f.setAccessible(true);
        return f.get(object);
    }
}
