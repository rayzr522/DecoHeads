package me.rayzr522.decoheads.gui;

import me.rayzr522.decoheads.DecoHeads;
import me.rayzr522.decoheads.config.Settings;
import me.rayzr522.decoheads.gui.system.Button;
import me.rayzr522.decoheads.gui.system.Dimension;
import me.rayzr522.decoheads.gui.system.GUI;
import me.rayzr522.decoheads.gui.system.Label;
import me.rayzr522.decoheads.util.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class SettingsGUI extends GUI {
    private static final List<Setting> SETTINGS = Arrays.asList(
            Setting.bool("economy", settings -> settings.setEconomyEnabled(DecoHeads.getInstance().getEconomy() != null && !settings.isEconomyEnabled()), Settings::isEconomyEnabled),
            Setting.bool("show-free-heads", settings -> settings.setShowFreeHeads(!settings.shouldShowFreeHeads()), Settings::shouldShowFreeHeads)
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
                                setting.onClick();
                                render();
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
        private final Consumer<Settings> clickHandler;
        private final Supplier<ItemSettings> itemSettingsSupplier;

        Setting(String id, Consumer<Settings> clickHandler, Supplier<ItemSettings> itemSettingsSupplier) {
            this.id = id;
            this.clickHandler = clickHandler;
            this.itemSettingsSupplier = itemSettingsSupplier;
        }

        private static Setting bool(String id, Consumer<Settings> clickHandler, Function<Settings, Boolean> stateProvider) {
            return new Setting(id, clickHandler, () -> stateProvider.apply(DecoHeads.getInstance().getSettings()) ? new ItemSettings(ChatColor.GREEN, Material.REDSTONE_BLOCK) : new ItemSettings(ChatColor.RED, Material.COAL_BLOCK));
        }

        void onClick() {
            Settings settings = DecoHeads.getInstance().getSettings();
            clickHandler.accept(settings);
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
