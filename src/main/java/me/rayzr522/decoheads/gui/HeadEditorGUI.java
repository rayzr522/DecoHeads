package me.rayzr522.decoheads.gui;

import me.rayzr522.decoheads.Category;
import me.rayzr522.decoheads.DecoHeads;
import me.rayzr522.decoheads.data.Head;
import me.rayzr522.decoheads.gui.system.*;
import me.rayzr522.decoheads.util.Conversations;
import me.rayzr522.decoheads.util.ItemUtils;
import me.rayzr522.decoheads.util.TextUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class HeadEditorGUI extends GUI {
    private final Head head;

    public HeadEditorGUI(Player player, Head head) {
        super(player, DecoHeads.getInstance().tr(false, "gui.head-editor.title", head.getName()), 5);
        this.head = head;

        init();
    }

    private void init() {
        DecoHeads plugin = DecoHeads.getInstance();
        addComponent(Label.background(0, 0, 9, 5));

        addComponent(
                new Label(
                        head.getItem(),
                        Dimension.ONE,
                        new Dimension(4, 3),
                        " "
                ) {
                    // We have to do this so it'll re-render properly
                    @Override
                    public ItemStack simpleRender(Player player, int offsetX, int offsetY) {
                        ItemStack item = super.simpleRender(player, offsetX, offsetY);
                        ItemUtils.setName(item, plugin.tr(
                                false,
                                "button.head-editor.current-settings.name",
                                head.getName()
                        ));
                        ItemUtils.setLore(item, plugin.tr(
                                false,
                                "button.head-editor.current-settings.lore",
                                head.getName(),
                                head.getUUID(),
                                head.getCategory().name(),
                                head.computeCostFor(player),
                                head.getCost()
                        ).split("\n"));
                        return item;
                    }
                }
        );

        addComponent(makeSettingButton("name", Material.SIGN, new Dimension(2, 1),
                e -> Conversations.getString(getPlayer(), getPrompt("name"), name -> {
                    head.setName(name);
                    plugin.save();
                    getPlayer().sendMessage(getSuccess("category", name));

                    render();
                }))
        );

        addComponent(makeSettingButton("category", Material.CHEST, new Dimension(4, 1),
                e -> Conversations.getString(getPlayer(), getPrompt("category", getCategoryOptions()), name -> {
                    Category category;
                    try {
                        category = Category.valueOf(name.toUpperCase().replaceAll("[^A-Z0-9]", "_"));
                    } catch (IllegalArgumentException ignore) {
                        getPlayer().sendMessage(plugin.tr("gui.head-editor.properties.category.invalid", name));
                        return;
                    }

                    head.setCategory(category);
                    plugin.save();
                    getPlayer().sendMessage(getSuccess("category", category.name()));

                    render();
                }))
        );

        addComponent(makeSettingButton("price", Material.GOLD_NUGGET, new Dimension(6, 1),
                e -> Conversations.getDouble(getPlayer(), getPrompt("price"), price -> {
                    head.setCost(price);
                    plugin.save();
                    getPlayer().sendMessage(getSuccess("price", TextUtils.formatPrice(price)));

                    render();
                }))
        );
    }

    private String getCategoryOptions() {
        return Arrays.stream(Category.values()).map(Category::name).collect(Collectors.joining(", "));
    }

    private String getPrompt(String id) {
        return getPrompt(id, null);
    }

    private String getPrompt(String id, Object extra) {
        return DecoHeads.getInstance().tr(String.format("gui.head-editor.properties.%s.prompt", id), head.getName(), extra);
    }

    private String getSuccess(String id, Object newValue) {
        return DecoHeads.getInstance().tr(String.format("gui.head-editor.properties.%s.success", id), newValue);
    }

    private Button makeSettingButton(String id, Material icon, Dimension position, Consumer<ClickEvent> clickHandler) {
        Button button = new Button(
                new ItemStack(icon),
                Dimension.ONE, position,
                clickHandler,
                DecoHeads.getInstance().tr(false, String.format("button.head-editor.%s", id))
        );

        button.setCloseOnClick(true);

        return button;
    }
}
