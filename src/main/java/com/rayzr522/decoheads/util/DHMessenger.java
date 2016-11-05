
package com.rayzr522.decoheads.util;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.rayzr522.decoheads.DecoHeads;

public class DHMessenger {

    private DecoHeads plugin;
    private Logger    logger;

    private String    prefix = "[DecoHeads] ";

    public DHMessenger(DecoHeads plugin) {

        this.plugin = plugin;
        this.logger = plugin.getLogger();

    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public void info(String msg) {
        logger.info(msg);
    }

    public void msg(Player p, String msg) {
        p.sendMessage(TextUtils.colorize(prefix + msg));
    }

    public void err(String err, boolean disable) {
        info("-------------------- ERROR --------------------");
        info("DecoHeads has encountered an error:");
        info(err);

        if (disable) {

            info("DecoHeads will now be disabled.");
            info("---------------------------");
            Bukkit.getPluginManager().disablePlugin(plugin);

        } else {

            info("---------------------------");

        }
    }
}
