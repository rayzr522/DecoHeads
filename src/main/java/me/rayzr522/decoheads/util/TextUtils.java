package me.rayzr522.decoheads.util;

import me.rayzr522.decoheads.DecoHeads;
import org.bukkit.ChatColor;

public class TextUtils {

    public static String colorize(String text) {
        if (text == null) {
            return null;
        }

        return ChatColor.translateAlternateColorCodes('&', text);
    }

    /**
     * Converts a string to proper capitalization.
     *
     * @param input The input string to capitalize.
     * @return The properly capitalized string.
     */
    public static String capitalize(String input) {
        StringBuilder builder = new StringBuilder();
        String[] split = input.toLowerCase().split(" ");
        for (String str : split) {
            if (str.length() <= 1) {
                builder.append(str.toUpperCase());
            } else {
                builder.append(str.substring(0, 1).toUpperCase()).append(str.substring(1));
            }
        }
        return builder.toString();
    }

    public static String formatDecimal(double number) {
        return String.format("%.2f", number);
    }

    public static String formatPrice(double price) {
        return DecoHeads.getInstance().tr(false, "economy.price-format", formatDecimal(price));
    }
}
