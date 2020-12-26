package me.rayzr522.decoheads.command;

import me.rayzr522.decoheads.DecoHeads;
import me.rayzr522.decoheads.compat.EconomyWrapper;
import me.rayzr522.decoheads.data.Head;
import me.rayzr522.decoheads.gui.CategoryGUI;
import me.rayzr522.decoheads.gui.HeadsGUI;
import me.rayzr522.decoheads.util.ArrayUtils;
import me.rayzr522.decoheads.util.NamePredicate;
import me.rayzr522.decoheads.util.TextUtils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandDecoHeads implements CommandExecutor, TabCompleter {

    private DecoHeads plugin;

    public CommandDecoHeads(DecoHeads plugin) {
        this.plugin = plugin;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.tr("command.only-players"));
            return true;
        }

        Player p = (Player) sender;

        if (!plugin.checkPermission("use", p, true)) {
            return true;
        }

        if (args.length < 1) {
            // new HeadsGUI(p, 1, null).render();
            new CategoryGUI(p).render();
            return true;
        }

        String sub = args[0].toLowerCase();

        if (sub.equals("search") || sub.equals("find")) {
            if (args.length < 2) {
                p.sendMessage(plugin.tr("command.decoheads.find.no-search"));
                p.sendMessage(plugin.tr("command.decoheads.find.usage"));
                return true;
            }

            String search = ArrayUtils.concat(Arrays.copyOfRange(args, 1, args.length), " ");
            HeadsGUI gui = new HeadsGUI(p, 1, new NamePredicate(search));

            if (gui.getHeads().size() < 1) {
                p.sendMessage(plugin.tr("command.decoheads.find.no-heads-found", search));
            } else {
                gui.render();
            }
        } else if (sub.equals("give")) {
            if (args.length < 2) {
                p.sendMessage(plugin.tr("command.decoheads.find.usage"));
                return true;
            }

            Head head = plugin.getHeadManager().findByName(args[1]).orElse(null);
            if (head == null) {
                p.sendMessage(plugin.tr("command.decoheads.give.invalid-head", args[1]));
                return true;
            }

            Player target = args.length > 2 ? Bukkit.getPlayer(args[2]) : p;

            if (target == null) {
                p.sendMessage(plugin.tr("command.decoheads.give.invalid-player", args[2]));
                return true;
            }

            target.getInventory().addItem(head.getItem());
            p.sendMessage(plugin.tr("command.decoheads.give.given", target.getName(), head.getName()));
        } else if (sub.equals("get") && plugin.getSettings().isCustomHeadsEnabled()) {
            if (!plugin.checkPermission("use.custom", p, true)) {
                return true;
            }

            if (args.length < 2) {
                p.sendMessage(plugin.tr("command.decoheads.get.no-username"));
                p.sendMessage(plugin.tr("command.decoheads.get.usage"));
                return true;
            }

            String username = args[1];
            ItemStack skull = makeSkull(username);

            double cost = plugin.getSettings().getCustomHeadsCost();

            if (plugin.getSettings().isEconomyEnabled() && cost > 0.0) {
                EconomyWrapper eco = plugin.getEconomy();
                if (eco.getBalance(p) < cost) {
                    p.sendMessage(plugin.tr("economy.not-enough-money", TextUtils.formatPrice(cost)));
                    return true;
                }
                eco.withdrawPlayer(p, cost);
                p.sendMessage(plugin.tr("command.decoheads.get.given-cost", username, TextUtils.formatPrice(cost)));
            } else {
                p.sendMessage(plugin.tr("command.decoheads.get.given", username));
            }

            p.getInventory().addItem(skull);
        } else if (sub.matches("\\d+")) {
            int page;
            try {
                page = Integer.parseInt(sub);
            } catch (NumberFormatException e) {
                // Will basically only happen if the number is too big
                p.sendMessage(plugin.tr("command.decoheads.invalid-page", sub, plugin.getHeadManager().maxPages()));
                return true;
            }

            if (page < 1 || page > plugin.getHeadManager().maxPages()) {
                p.sendMessage(plugin.tr("command.decoheads.invalid-page", page, plugin.getHeadManager().maxPages()));
            } else {
                new HeadsGUI(p, page, null).render();
            }
        } else {
            p.sendMessage(plugin.tr("command.decoheads.usage"));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Stream.of("search", "find", "get", "give")
                    .filter(option -> option.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("search") || args[0].equalsIgnoreCase("find") || args[0].equalsIgnoreCase("give"))) {
            return plugin.getHeadManager().getHeadsFor(sender).stream()
                    .filter(head -> head.getInternalName().toLowerCase().startsWith(args[1].toLowerCase()))
                    .map(Head::getInternalName)
                    .collect(Collectors.toList());
        } else if (args.length == 3 && (args[0].equalsIgnoreCase("give"))) {
            return Bukkit.getOnlinePlayers().stream()
                    .filter(player -> player.getName().toLowerCase().startsWith(args[2].toLowerCase()))
                    .map(Player::getName)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @SuppressWarnings("deprecation")
    private ItemStack makeSkull(String username) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
        skullMeta.setOwner(username);
        head.setItemMeta(skullMeta);
        return head;
    }

}
