package me.rayzr522.decoheads.gui;

import me.rayzr522.decoheads.DecoHeads;
import me.rayzr522.decoheads.config.Settings;
import me.rayzr522.decoheads.gui.system.*;
import me.rayzr522.decoheads.util.Conversations;
import me.rayzr522.decoheads.util.ItemUtils;
import me.rayzr522.decoheads.util.TextUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class SettingsGUI extends GUI {
    private static final List<Setting> SETTINGS = Arrays.asList(
            Setting.bool("custom-heads", settings -> settings.setCustomHeadsEnabled(!settings.isCustomHeadsEnabled()), Settings::isCustomHeadsEnabled),
            Setting.price("custom-heads-cost", Settings::setCustomHeadsCost, Settings::getCustomHeadsCost),
            Setting.bool("economy", settings -> settings.setEconomyEnabled(!settings.isEconomyEnabled()), Settings::isEconomyEnabled),
            Setting.bool("show-free-heads", settings -> settings.setShowFreeHeads(!settings.shouldShowFreeHeads()), Settings::shouldShowFreeHeads),
            Setting.price("economy-default-cost", Settings::setDefaultHeadCost, Settings::getDefaultHeadCost),
            Setting.bool("updater-enabled", settings -> settings.setUpdaterEnabled(!settings.isUpdaterEnabled()), Settings::isUpdaterEnabled)
    );

    public SettingsGUI(Player player) {
        super(player, DecoHeads.getInstance().tr(false, "gui.settings.title"), 1);

        init();
    }

    private void init() {
        // Background
        addComponent(Label.background(0, 0, 9, 1));

        // Setting buttons
        for (int i = 0; i < SETTINGS.size(); i++) {
            final Setting setting = SETTINGS.get(i);

            addComponent(
                    new Button(
                            new ItemStack(Material.STONE),
                            Dimension.ONE,
                            new Dimension(i, 0),
                            e -> {
                                setting.onClick(e);
                                if (!e.shouldClose()) {
                                    render();
                                }
                            },
                            ""
                    ) {
                        @Override
                        public ItemStack simpleRender(Player player, int offsetX, int offsetY) {
                            return setting.getItem();
                        }
                    }
            );
        }
    }

    private static class Setting {
        private final String id;
        private final BiConsumer<Settings, ClickEvent> clickHandler;
        private final Supplier<ItemSettings> itemSettingsSupplier;

        Setting(String id, BiConsumer<Settings, ClickEvent> clickHandler, Supplier<ItemSettings> itemSettingsSupplier) {
            this.id = id;
            this.clickHandler = clickHandler;
            this.itemSettingsSupplier = itemSettingsSupplier;
        }

        private static Setting bool(String id, Consumer<Settings> clickHandler, Function<Settings, Boolean> stateProvider) {
            return new Setting(id, (settings, e) -> clickHandler.accept(settings), () -> stateProvider.apply(DecoHeads.getInstance().getSettings()) ? new ItemSettings(ChatColor.GREEN, Material.REDSTONE_BLOCK) : new ItemSettings(ChatColor.RED, Material.COAL_BLOCK));
        }

        private static Setting price(String id, BiConsumer<Settings, Double> clickHandler, Function<Settings, Double> stateProvider) {
            DecoHeads plugin = DecoHeads.getInstance();
            return new Setting(
                    id,
                    (settings, e) -> {
                        e.setShouldClose(true);
                        e.getPlayer().closeInventory();
                        Conversations.getDouble(e.getPlayer(), plugin.tr(String.format("gui.settings.%s.prompt", id)), value -> {
                            clickHandler.accept(settings, value);
                            settings.save();
                            e.getGui().render();
                        });
                    },
                    () -> new ItemSettings(
                            ChatColor.DARK_AQUA,
                            Material.REDSTONE_COMPARATOR,
                            plugin.tr(
                                    false,
                                    String.format("gui.settings.%s.format", id),
                                    TextUtils.formatPrice(stateProvider.apply(plugin.getSettings()))
                            )
                    )
            );
        }

        void onClick(ClickEvent e) {
            Settings settings = DecoHeads.getInstance().getSettings();
            clickHandler.accept(settings, e);
            settings.save();
        }

        ItemStack getItem() {
            DecoHeads plugin = DecoHeads.getInstance();
            ItemSettings settings = itemSettingsSupplier.get();

            ItemStack item = new ItemStack(settings.type);
            ItemUtils.setName(item, settings.nameColor + plugin.tr(false, String.format("gui.settings.%s.name", id)));
            ItemUtils.setLore(item, settings.lore);

            return item;
        }
    }

    private static class ItemSettings {
        private ChatColor nameColor;
        private Material type;
        private String[] lore;

        public ItemSettings(ChatColor nameColor, Material type, String... lore) {
            this.nameColor = nameColor;
            this.type = type;
            this.lore = lore;
        }
    }
}
