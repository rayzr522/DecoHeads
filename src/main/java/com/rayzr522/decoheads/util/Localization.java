/**
 * 
 */
package com.rayzr522.decoheads.util;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @author Rayzr
 *
 */
public class Localization {

    /**
     * Matches any valid YAML path inside of double square brackets.
     * Example: [[some.path.here]]
     */
    private static final Pattern    VAR_SUBSTITUTION   = Pattern.compile("\\[\\[([a-z_.-]+)\\]\\]");
    /**
     * Matches any positional parameter
     * Example: {0}, {5}, {23}
     */
    private static final Pattern    POSITION_PARAMETER = Pattern.compile("{(\\d+)}");

    private HashMap<String, String> messages;

    /**
     * Loads all the messages from a config file
     * 
     * @param config the file to load all messages from
     */
    public void load(YamlConfiguration config) {
        messages = new HashMap<String, String>();

        HashMap<String, String> raw = new HashMap<String, String>();
        // First run: load all messages
        for (String key : config.getKeys(true)) {
            raw.put(key, config.get(key, "ERR").toString());
        }

        // Second run: parse them for special things
        for (Entry<String, String> entry : raw.entrySet()) {
            messages.put(entry.getKey(), parse(raw, entry));
        }
    }

    /**
     * @param raw the raw messages map before loading is completed
     * @param entry the current entry
     * @return the parsed string
     */
    private String parse(HashMap<String, String> raw, Entry<String, String> entry) {
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
     * @return the message
     */
    public String tr(String key, String... strings) {
        if (!messages.containsKey(key)) {
            return key;
        }
        String message = messages.get(key);
        Matcher matcher = POSITION_PARAMETER.matcher(message);
        while (matcher.find()) {
            message = message.replaceAll(Pattern.quote(matcher.group()), "" + Integer.parseInt(matcher.group(1)));
        }
        return TextUtils.colorize(message);
    }

}
