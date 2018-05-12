package me.rayzr522.decoheads.util;

import me.rayzr522.decoheads.DecoHeads;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Date;

/**
 * @author Rayzr
 */
public class ConfigVersionChecker {

    public static YamlConfiguration updateConfig(String path, int targetVersion) throws IOException {
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
            plugin.getLogger().info(
                    String.format(
                            "--------------------------------------------------------------\n" +
                                    "Upgraded %1$s config to v%2$d\n" +
                                    "Please take a look at the new %1$s to see what changes\n" +
                                    "have been made, and then feel free to copy your customized\n" +
                                    "settings from your backed-up %1$s over to the new one.\n" +
                                    "--------------------------------------------------------------",
                            path,
                            targetVersion
                    )
            );
        }

        return config;
    }

}
