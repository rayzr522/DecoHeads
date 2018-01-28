package me.rayzr522.decoheads.command;

import me.rayzr522.decoheads.DecoHeads;
import me.rayzr522.decoheads.gui.EditorGUI;
import me.rayzr522.decoheads.gui.SettingsGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        }

        return true;
    }

    private void showUsage(CommandSender sender) {
        sender.sendMessage(plugin.tr("command.decoheadsadmin.usage"));
    }
}
