package me.rayzr522.decoheads.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Compat {
    private static final boolean IS_1_9 = Reflector.getMinorVersion() >= 9;

    @SuppressWarnings("deprecation")
    public static ItemStack getItemInHand(Player player) {
        if (IS_1_9) {
            return player.getInventory().getItemInMainHand();
        } else {
            return player.getItemInHand();
        }
    }
}
