package me.rayzr522.decoheads;

import me.rayzr522.decoheads.gui.system.Dimension;
import me.rayzr522.decoheads.util.TextUtils;
import org.bukkit.command.CommandSender;

/**
 * @author Rayzr
 */
public enum Category {
    FOOD("food", new Dimension(1, 1)),
    TECHNOLOGY("tech", new Dimension(1, 2)),
    LETTERS("letters", new Dimension(1, 3)),
    MINI_BLOCKS("mini-blocks", new Dimension(3, 1)),
    HOUSEHOLD_ITEMS("household-items", new Dimension(3, 2)),
    COLORS("colors", new Dimension(3, 3)),
    MOBS("mobs", new Dimension(5, 1)),
    CHARACTERS("characters", new Dimension(5, 2)),
    POKEMON("pokemon", new Dimension(5, 3)),
    FLAGS("flags", new Dimension(7, 1)),
    EMOJI("emoji", new Dimension(7, 2)),
    MISC("misc", new Dimension(7, 3));

    private final String key;
    private final Dimension position;

    Category(String key, Dimension position) {
        this.key = key;
        this.position = position;
    }

    /**
     * @return the key in the heads.yml file that this Category is associated
     * with.
     */
    public String getKey() {
        return key;
    }

    /**
     * @return the position of this Category in the categories GUI
     */
    public Dimension getPosition() {
        return position;
    }

    /**
     * @param sender The {@link CommandSender} to check.
     * @return Whether or not the sender has permission to access this category.
     */
    public boolean hasPermission(CommandSender sender) {
        return DecoHeads.getInstance().checkPermission(String.format("category.%s", key), sender, false);
    }

    @Override
    public String toString() {
        return TextUtils.capitalize(name().replace('_', ' '));
    }
}
