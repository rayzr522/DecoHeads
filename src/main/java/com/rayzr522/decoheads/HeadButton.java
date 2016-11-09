/**
 * 
 */
package com.rayzr522.decoheads;

import com.rayzr522.decoheads.gui.ClickEvent;
import com.rayzr522.decoheads.gui.Component;
import com.rayzr522.decoheads.gui.Dimension;
import com.rayzr522.decoheads.util.ItemUtils;

/**
 * @author Rayzr
 *
 */
public class HeadButton extends Component {

    public HeadButton(Head head, Dimension position) {
        super(ItemUtils.setName(head.getItem(), DecoHeads.getInstance().tr("item.name", head.getName())), position, Dimension.ONE);
    }

    @Override
    public void onClick(ClickEvent e) {
        e.getPlayer().getInventory().addItem(ItemUtils.setLore(item.clone(), DecoHeads.getInstance().tr("item.lore").split("\n")));
    }

}
