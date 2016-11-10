/**
 * 
 */
package com.rayzr522.decoheads;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import com.rayzr522.decoheads.gui.HeadsGui;

/**
 * @author Rayzr
 *
 */
public class HeadManager {

    private List<Head> heads;

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
        FileConfiguration config = plugin.getConfigHandler().getConfig("heads.yml");

        ConfigurationSection headsSection = config.getConfigurationSection("heads");

        heads = headsSection.getKeys(false).stream().filter(headsSection::isConfigurationSection)
                .map(key -> Head.deserialize(key, headsSection.getConfigurationSection(key)))
                .filter(head -> head != null).collect(Collectors.toList());

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
    public List<Head> searchHeads(String filter) {
        if (filter == null || filter.equals("")) {
            return heads;
        }

        String filter2 = filter.toLowerCase();

        return heads.stream()
                .filter(head -> head.getName().toLowerCase().contains(filter2))
                .collect(Collectors.toList());
    }

    public int maxPages() {
        return maxPages(heads);
    }

    public int maxPages(List<Head> heads) {
        return heads.size() / HeadsGui.SIZE;
    }

    /**
     * @param args the current args
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
