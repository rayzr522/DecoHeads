
package com.rayzr522.decoheads;

import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.rayzr522.decoheads.command.CommandDecoHeads;
import com.rayzr522.decoheads.gui.GuiListener;
import com.rayzr522.decoheads.gui.InventoryManager;
import com.rayzr522.decoheads.util.ConfigHandler;
import com.rayzr522.decoheads.util.DHMessenger;
import com.rayzr522.decoheads.util.Localization;
import com.rayzr522.decoheads.util.Metrics;
import com.rayzr522.decoheads.util.Reflector;

public class DecoHeads extends JavaPlugin {

    private static DecoHeads instance;

    public DHMessenger       logger;
    private GuiListener      listener;

    private ConfigHandler    configHandler;
    private Localization     localization;

    @Override
    public void onEnable() {
        instance = this;

        logger = new DHMessenger(this);

        if (Reflector.getMinorVersion() < 8) {
            err("DecoHeads is only compatible with Minecraft 1.8+", true);
            return;
        }

        listener = new GuiListener(this);
        getServer().getPluginManager().registerEvents(listener, this);

        // Create the config handler
        configHandler = new ConfigHandler(this);
        // Load all config stuff
        reload();

        setupCommands();

        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException e) {
            // Ignore this, metrics failed to start
        }

    }

    public void reload() {
        reloadConfig();
        // Just ensure that the config.yml file exists
        configHandler.getConfig("config.yml");
        // Load the localization from the config
        localization = Localization.load(configHandler.getConfig("messages.yml"));

        logger.setPrefix(localization.getMessagePrefix());

        InventoryManager.loadHeads(this);
    }

    private void setupCommands() {
        // Currently only one command, I'll probably add more in the future
        CommandDecoHeads commandDecoHeads = new CommandDecoHeads(this);
        getCommand("decoheads").setExecutor(commandDecoHeads);
        getCommand("decoHeads").setTabCompleter(commandDecoHeads);
    }

    /**
     * Logs a message to the console
     * 
     * @param msg the message to print
     */
    public void log(String msg) {
        logger.info(msg);
    }

    /**
     * Logs an error to the console with the option to disable the plugin after
     * logging. Generally this should only be used for extremely SEVERE errors!
     * 
     * @param err the error message
     * @param disable whether or not to disable the plugin after loggin
     */
    public void err(String err, boolean disable) {
        logger.err(err, disable);
    }

    public void msg(Player p, String string) {
        logger.msg(p, string);
    }

    public Localization getLocalization() {
        return localization;
    }

    /**
     * @param key the key of the message
     * @param strings the strings to use for substitution
     * @return The message, or the key itself if no message was found for that
     *         key
     * @see com.rayzr522.decoheads.util.Localization#tr(java.lang.String,
     *      java.lang.String[])
     */
    public String tr(String key, Object... strings) {
        return localization.tr(key, strings);
    }

    /**
     * @return the instance of this plugin
     */
    public static DecoHeads getInstance() {
        return instance;
    }

}
