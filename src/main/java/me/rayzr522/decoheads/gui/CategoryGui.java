package me.rayzr522.decoheads.gui;

import me.rayzr522.decoheads.Category;
import me.rayzr522.decoheads.DecoHeads;
import me.rayzr522.decoheads.data.Head;
import me.rayzr522.decoheads.gui.system.Button;
import me.rayzr522.decoheads.gui.system.Dimension;
import me.rayzr522.decoheads.gui.system.Gui;
import me.rayzr522.decoheads.gui.system.Label;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author Rayzr
 */
public class CategoryGui extends Gui {

    private DecoHeads plugin = DecoHeads.getInstance();

    public CategoryGui(Player player) {
        super(player, DecoHeads.getInstance().tr("gui.categories.title"), 5);
        init();
    }

    private void init() {

        addComponent(new Label(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15), new Dimension(9, 5), new Dimension(0, 0), " "));

        for (Category category : Category.values()) {

            List<Head> heads = plugin.getHeadManager().searchHeads(h -> h.getCategory() == category);
            Head head = heads.get((int) (Math.random() * (heads.size() - 1)));

            Button button = new Button(head.getItem(), Dimension.ONE, category.getPosition(), e -> new HeadsGui(e.getPlayer(), 1, h -> h.getCategory() == category, this).render(), DecoHeads.getInstance().tr("button.categories.category.name", DecoHeads.getInstance().tr("category." + category.getKey())),
                    DecoHeads.getInstance().tr("button.categories.category.lore").split("\n"));

            addComponent(button);
        }

    }

}
