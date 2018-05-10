/**
 *
 */
package me.rayzr522.decoheads.gui.system;

import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

/**
 * @author Rayzr
 */
public class Button extends Label {

    private Consumer<ClickEvent> clickHandler;
    private boolean closeOnClick = false;

    /**
     * @param item         the button icon
     * @param size         the button size
     * @param clickHandler the click handler
     * @param name         the button name
     * @param lore         the button lore
     */
    public Button(ItemStack item, Dimension size, Dimension position, Consumer<ClickEvent> clickHandler, String name, String... lore) {
        super(item, size, position, name, lore);
        this.clickHandler = clickHandler;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.rayzr522.decoheads.gui.Label#onClick(com.rayzr522.decoheads.gui.
     * ClickEvent)
     */
    @Override
    public void onClick(ClickEvent e) {
        clickHandler.accept(e);
        e.setShouldClose(closeOnClick);
    }

    /**
     * @return the clickHandler
     */
    public Consumer<ClickEvent> getClickHandler() {
        return clickHandler;
    }

    /**
     * @param clickHandler the clickHandler to set
     */
    public void setClickHandler(Consumer<ClickEvent> clickHandler) {
        this.clickHandler = clickHandler;
    }

    /**
     * @return whether the GUI will close on click
     */
    public boolean willCloseOnClick() {
        return closeOnClick;
    }

    /**
     * @param closeOnClick set whether or not to close on click. Defaults to
     *                     {@code false}
     */
    public void setCloseOnClick(boolean closeOnClick) {
        this.closeOnClick = closeOnClick;
    }

}
