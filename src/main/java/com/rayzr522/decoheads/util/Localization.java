/**
 * 
 */
package com.rayzr522.decoheads.util;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.configuration.file.YamlConfiguration;

import com.rayzr522.decoheads.DecoHeads;

/**
 * @author Rayzr
 *
 */
public class Localization {

    /**
     * The version to check in the config
     */
    public static final int           CONFIG_VERSION          = 1;

    /**
     * Matches any valid YAML path inside of double square brackets.
     * Example: [[some.path.here]]
     */
    private static final Pattern      VAR_SUBSTITUTION = Pattern.compile("\\[\\[([a-z_.-]+)\\]\\]");

    private CompatMap<String, String> messages;

    private Localization(DecoHeads plugin, String path) throws IOException {

        YamlConfiguration config = ConfigVersionChecker.updateConfig(path, CONFIG_VERSION,
                "--------------------------------------------------------------",
                "Upgraded Localization config to v" + CONFIG_VERSION,
                "Please take a look at the new messages.yml to see what changes",
                "have been made, and then feel free to copy your customized",
                "settings from your backed-up messages.yml over to the new one.",
                "",
                "This feature is in place so that the developer can add new",
                "messages, and have it update in everyone else's config files.",
                "--------------------------------------------------------------");

        messages = new CompatMap<String, String>();

        CompatMap<String, String> raw = new CompatMap<String, String>();
        // First run: load all messages
        for (String key : config.getKeys(true)) {
            if (config.isConfigurationSection(key)) {
                continue;
            }
            raw.put(key, config.get(key, "ERR").toString());
        }

        // Second run: parse them for special things
        for (Entry<String, String> entry : raw.entrySet()) {
            messages.put(entry.getKey(), parse(raw, entry));
        }
    }

    /**
     * Loads all the messages from a config file
     * 
     * @param config the file to load all messages from
     */
    public static Localization load(DecoHeads plugin, String path) throws IOException {
        return new Localization(plugin, path);
    }

    /**
     * @param raw the raw messages map before loading is completed
     * @param entry the current entry
     * @return the parsed string
     */
    private String parse(CompatMap<String, String> raw, Entry<String, String> entry) {
        // 1. Color the text
        String output = TextUtils.colorize(entry.getValue());
        Matcher matcher;

        // 2. Variable substitution
        matcher = VAR_SUBSTITUTION.matcher(output);
        while (matcher.find()) {
            output.replaceAll(matcher.group(), raw.getOrDefault(matcher.group(1), matcher.group(1)));
        }

        return output;
    }

    /**
     * Gets a message prefix if specified in the loaded config file under the
     * <code>prefix</code> path
     * 
     * @return the prefix, or an empty String if none was specified
     */
    public String getMessagePrefix() {
        return messages.getOrDefault("prefix", "");
    }

    /**
     * 
     * @param key the key of the message
     * @param strings the strings to use for substitution
     * @return The message, or the key itself if no message was found for that
     *         key
     */
    public String tr(String key, Object... strings) {
        if (!messages.containsKey(key)) {
            return key;
        }
        String message = messages.get(key);
        for (int i = 0; i < strings.length; i++) {
            message = message.replace("{" + i + "}", strings[i].toString());
        }
        return TextUtils.colorize(message);
    }

}
