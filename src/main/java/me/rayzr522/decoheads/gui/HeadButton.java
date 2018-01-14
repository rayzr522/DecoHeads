/**
 *
 */
package me.rayzr522.decoheads.gui;

import me.rayzr522.decoheads.DecoHeads;
import me.rayzr522.decoheads.data.Head;
import me.rayzr522.decoheads.gui.system.ClickEvent;
import me.rayzr522.decoheads.gui.system.Component;
import me.rayzr522.decoheads.gui.system.Dimension;
import me.rayzr522.decoheads.util.ItemUtils;
import org.bukkit.inventory.ItemStack;

/**
 * @author Rayzr
 */
public class HeadButton extends Component {

    private Head head;

    public HeadButton(Head head, Dimension position) {
        super(ItemUtils.setName(head.getItem(), DecoHeads.getInstance().tr("item.name", head.getName())), Dimension.ONE, position);
        this.head = head;
    }

    @Override
    public void onClick(ClickEvent e) {
        ItemStack giveItem = head.getItem();
        ItemUtils.setName(giveItem, DecoHeads.getInstance().tr("item.name", head.getName()));
        ItemUtils.setLore(giveItem, DecoHeads.getInstance().tr("item.lore").split("\n"));
        e.getPlayer().getInventory().addItem(giveItem);
    }

}
