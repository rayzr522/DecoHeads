/**
 * 
 */
package com.rayzr522.decoheads;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import com.rayzr522.decoheads.gui.HeadsGui;

/**
 * @author Rayzr
 *
 */
public class HeadManager {

    private List<Head> heads = new ArrayList<Head>();

    private DecoHeads  plugin;

    /**
     * @param decoHeads
     */
    public HeadManager(DecoHeads plugin) {
        this.plugin = plugin;
    }

    /**
     * @return Whether anything was actually loaded
     */
    public boolean load() {
        heads.clear();

        FileConfiguration config = plugin.getConfigHandler().getConfig("heads.yml");
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
