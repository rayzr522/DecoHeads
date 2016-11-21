/**
 * 
 */
package com.rayzr522.decoheads;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.rayzr522.decoheads.gui.HeadsGui;
import com.rayzr522.decoheads.util.ConfigVersionChecker;

/**
 * @author Rayzr
 *
 */
public class HeadManager {

    public static final int CONFIG_VERSION = 1;

    private List<Head>      heads          = new ArrayList<Head>();

    private DecoHeads       plugin;

    /**
     * @param decoHeads
     */
    public HeadManager(DecoHeads plugin) {
        this.plugin = plugin;
    }

    /**
     * @return Whether anything was actually loaded
     * @throws IOException
     */
    public boolean load() throws IOException {
        heads.clear();

        YamlConfiguration config = ConfigVersionChecker.updateConfig("heads.yml", CONFIG_VERSION,
                "--------------------------------------------------------------",
                "Upgraded Heads config to v" + CONFIG_VERSION,
                "Please take a look at the new heads.yml to see what changes",
                "have been made, and then feel free to copy your customized",
                "settings from your backed-up heads.yml over to the new one.",
                "",
                "This feature is in place so that the developer can change the",
                "config system without it breaking for people with old configs.",
                "--------------------------------------------------------------");
        
        ConfigurationSection headsSection = config.getConfigurationSection("heads");

        for (Category category : Category.values()) {

            ConfigurationSection categorySection = headsSection.getConfigurationSection(category.getKey());
            if (categorySection == null) {
                throw new IllegalStateException("Could not find config section for heads category '" + category.getKey() + "'");
            }

            heads.addAll(categorySection.getKeys(false).stream().filter(categorySection::isConfigurationSection)
                    .map(key -> Head.deserialize(key, category, categorySection.getConfigurationSection(key)))
                    .filter(head -> head != null).collect(Collectors.toList()));

        }

        if (heads.size() < 1) {
            plugin.log("No heads were found in heads.yml");
            return false;
        }

        return true;
    }

    /**
     * @param filter
     * @return
     */
    public List<Head> searchHeads(Predicate<Head> filter) {
        if (filter == null) {
            return heads;
        }

        return heads.stream()
                .filter(filter)
                .collect(Collectors.toList());
    }

    public int maxPages() {
        return maxPages(heads);
    }

    public int maxPages(List<Head> heads) {
        return (int) Math.ceil((double) heads.size() / (double) HeadsGui.SIZE);
    }

    /**
     * @param filter the current input
     * @return a list of all the names of the heads
     */
    public List<String> headsList(String filter) {
        List<String> headsList = new ArrayList<String>();
        for (Head head : heads) {
            if (!head.getName().startsWith(filter)) {
                continue;
            }
            headsList.add(head.getName());
        }
        return headsList;
    }

}
