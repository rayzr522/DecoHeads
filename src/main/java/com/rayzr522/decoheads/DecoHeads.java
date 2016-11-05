
package com.rayzr522.decoheads;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.rayzr522.decoheads.command.CommandDecoHeads;
import com.rayzr522.decoheads.gui.GuiListener;
import com.rayzr522.decoheads.gui.InventoryManager;
import com.rayzr522.decoheads.util.ConfigHandler;
import com.rayzr522.decoheads.util.DHMessenger;
import com.rayzr522.decoheads.util.Reflector;

public class DecoHeads extends JavaPlugin {

    private static DecoHeads  instance;

    public DHMessenger        logger;
    private GuiListener       listener;

    private ConfigHandler     configHandler;
    private YamlConfiguration config;

    @Override
    public void onEnable() {

        instance = this;

        logger = new DHMessenger(this);

        if (Reflector.getMajorVersion() < 8) {
            err("DecoHeads is only compatible with Minecraft 1.8+", true);
            return;
        }

        listener = new GuiListener(this);
        getServer().getPluginManager().registerEvents(listener, this);

        configHandler = new ConfigHandler(this);
        config = configHandler.getConfig("config.yml");

        logger.setPrefix(config.getString("prefix"));

        InventoryManager.loadHeads(this);

        setupCommands();

        log("DecoHeads v" + getDescription().getVersion() + " enabled!");

    }

    private void setupCommands() {
        getCommand("decoheads").setExecutor(new CommandDecoHeads(this));
    }

    @Override
    public void onDisable() {

        log("DecoHeads v" + getDescription().getVersion() + " disabled!");

    }

    public void log(String msg) {

        logger.info(msg);

    }

    public void err(String err, boolean disable) {

        logger.err(err, disable);

    }

    public void msg(Player p, String string) {

        logger.msg(p, string);

    }

    /**
     * @return the instance of this plugin
     */
    public static DecoHeads getInstance() {
        return instance;
    }

}
