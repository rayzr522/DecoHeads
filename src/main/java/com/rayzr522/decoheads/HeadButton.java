/**
 * 
 */
package com.rayzr522.decoheads;

import org.bukkit.inventory.ItemStack;

import com.rayzr522.decoheads.gui.ClickEvent;
import com.rayzr522.decoheads.gui.Component;
import com.rayzr522.decoheads.gui.Dimension;
import com.rayzr522.decoheads.util.ItemUtils;

/**
 * @author Rayzr
 *
 */
public class HeadButton extends Component {

    private Head head;

    public HeadButton(Head head, Dimension position) {
        super(ItemUtils.setName(head.getItem(), DecoHeads.getInstance().tr("item.name", head.getName())), Dimension.ONE, position);
        this.head = head;
        System.out.println("The head: " + head.toString());
    }

    @Override
    public void onClick(ClickEvent e) {
        ItemStack giveItem = head.getItem();
        ItemUtils.setName(giveItem, DecoHeads.getInstance().tr("item.name", head.getName()));
        ItemUtils.setLore(giveItem, DecoHeads.getInstance().tr("item.lore").split("\n"));
        System.out.println("Head: " + head.toString());
        e.getPlayer().getInventory().addItem(giveItem);
        // e.getPlayer().getInventory().addItem(ItemUtils.setLore(item.clone(),
        // DecoHeads.getInstance().tr("item.lore").split("\n")));
    }

}
