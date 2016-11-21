
package com.rayzr522.decoheads.util;

import org.bukkit.ChatColor;

public class TextUtils {

    public static String colorize(String text) {

        return ChatColor.translateAlternateColorCodes('&', text);

    }

    public static String uncolorize(String text) {

        return text.replace(ChatColor.COLOR_CHAR, '&');

    }

    public static String stripColor(String text) {

        return ChatColor.stripColor(text);

    }

    /**
     * @param input the input to convert to camel case
     * @return
     */
    public static String capitalize(String input) {
        StringBuilder builder = new StringBuilder();
        String[] split = input.toLowerCase().split(" ");
        for (String str : split) {
            if (str.length() < 2) {
                builder.append(str.toUpperCase());
            } else {
                builder.append(str.substring(0, 1).toUpperCase()).append(str.substring(1));
            }
        }
        return builder.toString();
    }

}
