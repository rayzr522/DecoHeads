package me.rayzr522.decoheads.util;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Rayzr
 */
public class Localization {

    /**
     * The version to check in the config
     */
    private static final int CONFIG_VERSION = 2;

    /**
     * Matches any valid YAML path inside of double square brackets.
     * Example: [[some.path.here]]
     */
    private static final Pattern VAR_SUBSTITUTION = Pattern.compile("\\[\\[([a-z_.-]+)]]");

    private Map<String, String> messages;

    private Localization(String path) throws IOException {
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

        // First run: load all messages
        Map<String, String> raw = config.getKeys(true).stream()
                .filter(config::isString)
                .collect(Collectors.toMap(key -> key, config::getString));

        // Second run: parse them for special things
        messages = raw.entrySet().stream()
                .collect(Collectors.toMap(Entry::getKey, entry -> parse(raw, entry)));
    }

    public static Localization load(String path) throws IOException {
        return new Localization(path);
    }

    private String parse(Map<String, String> raw, Entry<String, String> entry) {
        // 1. Color the text
        String output = TextUtils.colorize(entry.getValue());
        Matcher matcher;

        // 2. Variable substitution
        matcher = VAR_SUBSTITUTION.matcher(output);
        while (matcher.find()) {
            output = output.replaceAll(matcher.group(), raw.getOrDefault(matcher.group(1), matcher.group(1)));
        }

        return output;
    }

    /**
     * @param key     the key of the message
     * @param strings the strings to use for substitution
     * @return The message, or the key itself if no message was found for that
     * key
     */
    public String tr(String key, Object... strings) {
        if (!messages.containsKey(key)) {
            return key;
        }
        String message = messages.get(key);
        for (int i = 0; i < strings.length; i++) {
            message = message.replace("{" + i + "}", Objects.toString(strings[i]));
        }
        return TextUtils.colorize(message);
    }

}
