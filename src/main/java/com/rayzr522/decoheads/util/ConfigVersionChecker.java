package com.rayzr522.decoheads.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Date;

import org.bukkit.configuration.file.YamlConfiguration;

import com.rayzr522.decoheads.DecoHeads;

/**
 * @author Rayzr
 *
 */
public class ConfigVersionChecker {

    public static YamlConfiguration updateConfig(String path, int targetVersion, String... updateMessage) throws IOException {
        DecoHeads plugin = DecoHeads.getInstance();
        ConfigHandler ch = plugin.getConfigHandler();

        YamlConfiguration config = ch.getConfig(path);

        Path current = ch.getFile(path).toPath();
        Path backup = ch.getFile(path + ".ERR.backup").toPath();

        Files.copy(current, backup, StandardCopyOption.REPLACE_EXISTING);

        int tries = 0;
        while (!config.contains("version") || !config.isInt("version") || config.getInt("version") != targetVersion) {
            tries++;
            if (tries > 10) {
                throw new IllegalStateException("Caught in an infinite loop while trying to update a config! Please restore using the " + path + ".ERR.backup file!");
            }

            Path file = ch.getFile(path).toPath();
            Path newPath = ch.getFile(path + "." + new DateCodeFormat().format(new Date()) + ".backup").toPath();
            Files.move(file, newPath, StandardCopyOption.REPLACE_EXISTING);
            Files.deleteIfExists(file);

            config = ch.getConfig(path);
        }

        Files.deleteIfExists(backup);

        if (tries > 0) {
            Arrays.stream(updateMessage).forEach(plugin::log);
        }

        return config;
    }

}
