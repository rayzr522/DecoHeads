/**
 *
 */
package me.rayzr522.decoheads.gui;

import me.rayzr522.decoheads.DecoHeads;
import me.rayzr522.decoheads.data.Head;
import me.rayzr522.decoheads.gui.system.Button;
import me.rayzr522.decoheads.gui.system.Dimension;
import me.rayzr522.decoheads.gui.system.Gui;
import me.rayzr522.decoheads.gui.system.Label;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Rayzr
 */
public class HeadsGui extends Gui {

    /**
     * How many heads are on a page
     */
    private static final int WIDTH = 7;
    private static final int HEIGHT = 3;
    public static final int SIZE = WIDTH * HEIGHT;
    private static final int OFFSET_X = 1;
    private static final int OFFSET_Y = 1;
    private static ItemStack CURRENT_PAGE = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 9);

    private static ItemStack BUTTON_ENABLED = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
    private static ItemStack BUTTON_DISABLED = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);

    private DecoHeads plugin;
    private int page;
    private Predicate<Head> filter;
    private Gui lastGui;

    /**
     * @param player the player to open the GUI for
     * @param page   the page of the heads GUI
     * @param filter the filter
     */
    public HeadsGui(Player player, int page, Predicate<Head> filter) {
        this(player, page, filter, null);
    }

    public HeadsGui(Player player, int page, Predicate<Head> filter, Gui lastGui) {
        super(player, DecoHeads.getInstance().tr("gui.heads.title", page), 6);
        this.plugin = DecoHeads.getInstance();
        this.page = page;
        this.filter = filter;
        this.lastGui = lastGui;

        init();
    }

    private Button makeButton(String label, Dimension position, boolean enabled) {
        return new Button((enabled ? BUTTON_ENABLED : BUTTON_DISABLED).clone(), Dimension.ONE, position, e -> {
        }, (enabled ? "&a" : "&c") + "&l" + label);
    }

    private void refresh() {
        setComponents(new ArrayList<>());
        init();
    }

    public List<Head> getHeads() {
        return plugin.getHeadManager().getHeads().stream().filter(filter).collect(Collectors.toList());
    }

    /**
     *
     */
    private void init() {
        List<Head> filteredHeads = getHeads();

        addComponent(new Label(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15), new Dimension(9, 1), new Dimension(0, 5), " "));

        Button currentPage = new Button(CURRENT_PAGE.clone(), Dimension.ONE, new Dimension(4, 5), e -> {
            if (lastGui != null) {
                lastGui.render();
            }
        }, plugin.tr("button.heads.current-page.name", page), plugin.tr("button.heads.current-page.lore").split("\n"));
        if (lastGui != null) {
            currentPage.addLore(plugin.tr("button.heads.current-page.back").split("\n"));
        }

        addComponent(currentPage);

        Button previousPage = makeButton(plugin.tr("button.heads.previous-page"), new Dimension(2, 5), page > 1);
        Button nextPage = makeButton(DecoHeads.getInstance().tr("button.heads.next-page"), new Dimension(6, 5), page < plugin.getHeadManager().maxPages(filteredHeads));

        previousPage.setClickHandler(e -> {
            if (page <= 1) {
                return;
            }
            page--;
            refresh();
            render();
        });

        nextPage.setClickHandler(e -> {
            if (page >= plugin.getHeadManager().maxPages(filteredHeads)) {
                return;
            }
            page++;
            refresh();
            render();
        });

        addComponent(nextPage);
        addComponent(previousPage);

        if (filteredHeads.size() < 1) {
            return;
        }

        int offset = (page - 1) * SIZE;

        for (int i = 0; i < SIZE; i++) {
            int pos = offset + i;
            if (pos >= filteredHeads.size()) {
                break;
            }

            Head head = filteredHeads.get(pos);
            addComponent(new HeadButton(head, new Dimension(OFFSET_X + i % WIDTH, OFFSET_Y + i / WIDTH)));
        }
    }

}
