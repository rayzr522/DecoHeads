package me.rayzr522.decoheads.command;

import me.rayzr522.decoheads.DecoHeads;
import me.rayzr522.decoheads.gui.CategoryGui;
import me.rayzr522.decoheads.gui.HeadsGui;
import me.rayzr522.decoheads.util.ArrayUtils;
import me.rayzr522.decoheads.util.NamePredicate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CommandDecoHeads implements CommandExecutor, TabCompleter {

    private DecoHeads plugin;

    public CommandDecoHeads(DecoHeads plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.tr("command.only-players"));
            return true;
        }

        Player p = (Player) sender;

        if (!p.hasPermission("decoheads.use")) {
            p.sendMessage(plugin.tr("command.no-permission"));
            return true;
        }

        if (args.length < 1) {
            // new HeadsGui(p, 1, null).render();
            new CategoryGui(p).render();
            return true;
        }

        String arg = args[0].toLowerCase();

        if (arg.equals("reload") || arg.equals("rl")) {
            if (!p.hasPermission("decoheads.reload")) {
                p.sendMessage(plugin.tr("no-permission"));
                return true;
            }

            // Load all config stuff
            plugin.reload();
            p.sendMessage(plugin.tr("command.decoheads.reloaded"));
        } else if (arg.equals("search") || arg.equals("find")) {
            if (args.length < 2) {
                p.sendMessage(plugin.tr("command.decoheads.find.no-search"));
                p.sendMessage(plugin.tr("command.decoheads.find.usage"));
                return true;
            }

            String search = ArrayUtils.concat(Arrays.copyOfRange(args, 1, args.length), " ");
            HeadsGui gui = new HeadsGui(p, 1, new NamePredicate(search));

            if (gui.getHeads().size() < 1) {
                p.sendMessage(plugin.tr("command.decoheads.find.no-heads-found", search));
            } else {
                gui.render();
            }
        } else if (arg.matches("\\d+")) {
            int page;
            try {
                page = Integer.parseInt(arg);
            } catch (NumberFormatException e) {
                // Will basically only happen if the number is too big
                p.sendMessage(plugin.tr("command.decoheads.invalid-page", arg, plugin.getHeadManager().maxPages()));
                return true;
            }

            if (page < 1 || page > plugin.getHeadManager().maxPages()) {
                p.sendMessage(plugin.tr("command.decoheads.invalid-page", page, plugin.getHeadManager().maxPages()));
            } else {
                new HeadsGui(p, page, null).render();
            }
        } else {
            p.sendMessage(plugin.tr("command.decoheads.usage"));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return sort(Arrays.asList("reload", "rl", "search", "find", "#"), args[0]);
        } else if (args.length > 1 && (args[0].equalsIgnoreCase("search") || args[0].equalsIgnoreCase("find"))) {
            String filter = ArrayUtils.concat(Arrays.copyOfRange(args, 1, args.length), " ");
            return sort(plugin.getHeadManager().matchHeads(filter), filter);
        }
        return Collections.emptyList();
    }

    private List<String> sort(List<String> list, String filter) {
        return list.stream()
                .sorted(new MatchComparator(filter))
                .collect(Collectors.toList());
    }

}
