package me.rayzr522.decoheads.command;

import me.rayzr522.decoheads.Category;
import me.rayzr522.decoheads.DecoHeads;
import me.rayzr522.decoheads.data.Head;
import me.rayzr522.decoheads.data.HeadManager;
import me.rayzr522.decoheads.gui.EditorGUI;
import me.rayzr522.decoheads.gui.SettingsGUI;
import me.rayzr522.decoheads.util.Compat;
import me.rayzr522.decoheads.util.CustomHead;
import me.rayzr522.decoheads.util.TextUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

public class CommandDecoHeadsAdmin implements CommandExecutor {
    private final DecoHeads plugin;

    public CommandDecoHeadsAdmin(DecoHeads plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.tr("command.only-players"));
            return true;
        }

        Player player = (Player) sender;

        if (!plugin.checkPermission("admin", player, true)) {
            return true;
        }

        if (args.length < 1) {
            showUsage(player);
            return true;
        }

        String sub = args[0].toLowerCase();

        if (sub.equals("reload") || sub.equals("rl")) {
            if (!plugin.checkPermission("admin.reload", player, true)) {
                return true;
            }

            plugin.reload();
            player.sendMessage(plugin.tr("command.decoheadsadmin.reloaded"));
        } else if (sub.equals("settings")) {
            if (!plugin.checkPermission("admin.settings", player, true)) {
                return true;
            }

            new SettingsGUI(player).render();
        } else if (sub.equals("editor")) {
            if (!plugin.checkPermission("admin.editor", player, true)) {
                return true;
            }

            new EditorGUI(player).render();
        } else if (sub.equals("add")) {
            if (!plugin.checkPermission("admin.add", player, true)) {
                return true;
            }

            // [1] = name, [2] = category [3] ?= cost
            if (args.length < 3) {
                player.sendMessage(plugin.tr("command.decoheadsadmin.add.usage"));
                return true;
            }

            HeadManager headManager = plugin.getHeadManager();

            String name = args[1];
            if (headManager.findByName(name).isPresent()) {
                player.sendMessage(plugin.tr("command.decoheadsadmin.add.already-exists", args[1]));
                return true;
            }

            Category category;
            try {
                category = Category.valueOf(args[2].toUpperCase());
            } catch (IllegalArgumentException e) {
                player.sendMessage(plugin.tr(
                        "command.decoheadsadmin.add.invalid-category",
                        args[2],
                        Arrays.stream(Category.values()).map(Category::name).collect(Collectors.joining(", "))
                ));
                return true;
            }

            double cost = -1.0;
            if (args.length > 3) {
                try {
                    cost = Double.parseDouble(args[3]);
                } catch (NumberFormatException e) {
                    player.sendMessage(plugin.tr("command.not-a-decimal", args[3]));
                    return true;
                }
            }

            ItemStack item = Compat.getItemInHand(player);
            if (item == null || item.getType() != Material.SKULL_ITEM) {
                player.sendMessage(plugin.tr("command.decoheadsadmin.add.must-hold-head"));
                return true;
            }

            ItemMeta meta = item.getItemMeta();
            String texture = CustomHead.getTexture(meta);
            if (texture == null) {
                player.sendMessage(plugin.tr("command.decoheadsadmin.add.failed-to-retrieve-texture"));
                return true;
            }

            headManager.addHead(new Head(name, category, texture, UUID.randomUUID(), cost));
            headManager.save();
            if (cost > 0.0) {
                player.sendMessage(plugin.tr("command.decoheadsadmin.add.added", name, category.name(), TextUtils.formatPrice(cost)));
            } else {
                player.sendMessage(plugin.tr("command.decoheadsadmin.add.added-free", name, category.name()));
            }
        }

        return true;
    }

    private void showUsage(CommandSender sender) {
        sender.sendMessage(plugin.tr("command.decoheadsadmin.usage"));
    }
}
