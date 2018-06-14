package me.rayzr522.decoheads.gui;

import me.rayzr522.decoheads.DecoHeads;
import me.rayzr522.decoheads.compat.EconomyWrapper;
import me.rayzr522.decoheads.data.Head;
import me.rayzr522.decoheads.gui.system.*;
import me.rayzr522.decoheads.util.ItemUtils;
import me.rayzr522.decoheads.util.TextUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Rayzr
 */
public class HeadsGUI extends GUI {

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
    private GUI lastGUI;
    private BiConsumer<ClickEvent, Head> clickCallback;

    /**
     * @param player the player to open the GUI for
     * @param page   the page of the heads GUI
     * @param filter the filter
     */
    public HeadsGUI(Player player, int page, Predicate<Head> filter) {
        this(player, page, filter, null);
    }

    public HeadsGUI(Player player, int page, Predicate<Head> filter, GUI lastGUI) {
        this(player, page, filter, lastGUI, null);
    }

    public HeadsGUI(Player player, int page, Predicate<Head> filter, GUI lastGUI, BiConsumer<ClickEvent, Head> clickCallback) {
        super(player, DecoHeads.getInstance().tr(false, "gui.heads.title", page), 6);

        this.plugin = DecoHeads.getInstance();
        this.page = page;
        this.filter = filter;
        this.lastGUI = lastGUI;
        this.clickCallback = clickCallback;

        init();
    }

    private void init() {
        List<Head> filteredHeads = getHeads();

        addComponent(Label.background(0, 5, 9, 1));

        Button currentPage = new Button(CURRENT_PAGE.clone(), Dimension.ONE, new Dimension(4, 5), e -> {
            if (lastGUI != null) {
                lastGUI.render();
            }
        }, plugin.tr(false, "button.heads.current-page.name", page), plugin.tr(false, "button.heads.current-page.lore").split("\n"));
        if (lastGUI != null) {
            currentPage.addLore(plugin.tr(false, "button.heads.current-page.back").split("\n"));
        }

        addComponent(currentPage);

        Button previousPage = makeButton(plugin.tr(false, "button.heads.previous-page"), new Dimension(2, 5), page > 1);
        Button nextPage = makeButton(plugin.tr(false, "button.heads.next-page"), new Dimension(6, 5), page < plugin.getHeadManager().maxPages(filteredHeads));

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
            addComponent(makeHeadButton(head, new Dimension(OFFSET_X + i % WIDTH, OFFSET_Y + i / WIDTH)));
        }
    }

    private Button makeButton(String label, Dimension position, boolean enabled) {
        return new Button((enabled ? BUTTON_ENABLED : BUTTON_DISABLED).clone(), Dimension.ONE, position, e -> {
        }, (enabled ? "&a" : "&c") + "&l" + label);
    }

    private Component makeHeadButton(Head head, Dimension position) {
        Component button = new Component(ItemUtils.setName(head.getItem(), DecoHeads.getInstance().tr(false, "item.name", head.getName())), Dimension.ONE, position) {
            @Override
            public void onClick(ClickEvent e) {
                if (clickCallback == null) {
                    onClickHead(e, head);
                } else {
                    clickCallback.accept(e, head);
                }
            }
        };

        DecoHeads plugin = DecoHeads.getInstance();

        if (plugin.getSettings().isEconomyEnabled()) {
            if (head.hasCost() || plugin.getSettings().shouldShowFreeHeads()) {
                String[] costLore = plugin.tr(false, "item.cost", head.hasCost()
                        ? TextUtils.formatPrice(head.computeCost())
                        : plugin.tr(false, "economy.free")).split("\n");

                ItemUtils.setLore(button.getItem(), costLore);
            }
        }

        return button;
    }

    private void refresh() {
        setComponents(new ArrayList<>());
        init();
    }

    public List<Head> getHeads() {
        return plugin.getHeadManager().getHeadsFor(getPlayer()).stream()
                .filter(filter != null ? filter : head -> true)
                .collect(Collectors.toList());
    }

    private void onClickHead(ClickEvent e, Head head) {
        DecoHeads plugin = DecoHeads.getInstance();

        if (head.hasCost() && plugin.getSettings().isEconomyEnabled()) {
            if (plugin.getEconomy().getBalance(e.getPlayer()) < head.computeCost()) {
                e.setShouldClose(true);
                e.getPlayer().sendMessage(plugin.tr("economy.not-enough-money", TextUtils.formatPrice(head.computeCost())));
                return;
            }

            EconomyWrapper.EconomyResponseWrapper response = plugin.getEconomy().withdrawPlayer(e.getPlayer(), head.computeCost());
            if (!response.transactionSuccess()) {
                e.setShouldClose(true);
                e.getPlayer().sendMessage(plugin.tr("economy.failed", TextUtils.formatPrice(head.computeCost())));
                return;
            }
        }

        ItemStack giveItem = head.getItem();
        ItemUtils.setName(giveItem, plugin.tr(false, "item.name", head.getName()));
        ItemUtils.setLore(giveItem, plugin.tr(false, "item.lore").split("\n"));
        e.getPlayer().getInventory().addItem(giveItem);
    }

}
