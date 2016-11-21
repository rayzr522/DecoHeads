package com.rayzr522.decoheads.util;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.io.Files;
import com.rayzr522.decoheads.DecoHeads;

/**
 * @author Rayzr
 *
 */
public class ConfigVersionChecker {

    public static YamlConfiguration updateConfig(String path, int targetVersion, String... updateMessage) throws IOException {
        DecoHeads plugin = DecoHeads.getInstance();

        YamlConfiguration config = plugin.getConfigHandler().getConfig(path);

        File current = plugin.getConfigHandler().getFile(path);
        File backup = plugin.getConfigHandler().getFile(path + ".ERR.backup");

        Files.copy(current, backup);

        int tries = 0;
        while (!config.contains("version") || !config.isInt("version") || config.getInt("version") != targetVersion) {
            tries++;
            if (tries > 10) {
                throw new IllegalStateException("Caught in an infinite loop while trying to update a config! Please restore using the " + path + ".ERR.backup file!");
            }

            File file = plugin.getConfigHandler().getFile(path);
            String newName = path + "." + new DateCodeFormat().format(new Date()) + ".backup";
            if (!file.renameTo(plugin.getConfigHandler().getFile(newName))) {
                throw new IOException("Failed to backup old files! Please restore using the " + path + ".ERR.backup file!");
            }
            if (!plugin.getConfigHandler().getFile(path).delete()) {
                throw new IOException("Failed to delete old files! Please restore using the " + path + ".ERR.backup file!");
            }
            config = plugin.getConfigHandler().getConfig(path);
        }

        if (!backup.delete()) {
            plugin.err("Failed to delete the temporary backup file at " + path + ".ERR.backup. Please delete this file manually.", false);
        }

        if (tries > 0) {
            Arrays.stream(updateMessage).forEach(plugin::log);
        }

        return config;
    }

}
