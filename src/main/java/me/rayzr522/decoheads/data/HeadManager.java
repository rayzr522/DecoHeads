/**
 *
 */
package me.rayzr522.decoheads.data;

import me.rayzr522.decoheads.Category;
import me.rayzr522.decoheads.DecoHeads;
import me.rayzr522.decoheads.gui.HeadsGUI;
import me.rayzr522.decoheads.util.NamePredicate;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Rayzr
 */
public class HeadManager {
    private DecoHeads plugin;
    private List<Head> heads;

    public HeadManager(DecoHeads plugin) {
        this.plugin = plugin;
    }

    public void load() {
        YamlConfiguration config = plugin.getConfigHandler().getConfig("heads.yml");
        ConfigurationSection headsSection = config.isConfigurationSection("heads")
                ? config.getConfigurationSection("heads")
                : config.createSection("heads");

        heads = Arrays.stream(Category.values())
                .filter(category -> headsSection.isConfigurationSection(category.getKey()))
                .flatMap(category -> {
                    ConfigurationSection categorySection = headsSection.getConfigurationSection(category.getKey());

                    return categorySection.getKeys(false).stream()
                            .filter(categorySection::isConfigurationSection)
                            .map(name -> Head.load(name, category, categorySection.getConfigurationSection(name)))
                            .filter(Objects::nonNull);
                })
                .collect(Collectors.toList());
    }

    public void save() {
        YamlConfiguration config = new YamlConfiguration();
        ConfigurationSection headsSection = config.createSection("heads");

        Arrays.stream(Category.values())
                .forEach(category -> {
                    ConfigurationSection categorySection = headsSection.createSection(category.getKey());

                    heads.stream().filter(head -> head.getCategory() == category)
                            .forEach(head -> categorySection.createSection(head.getName(), head.serialize()));
                });

        try {
            plugin.getConfigHandler().saveConfig("heads.yml", config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Head> getHeads() {
        return heads;
    }

    public List<Head> searchHeads(Predicate<Head> filter) {
        return heads.stream()
                .filter(filter)
                .collect(Collectors.toList());
    }

    public List<String> matchHeads(String query) {
        return searchHeads(new NamePredicate(query)).stream()
                .map(Head::getName)
                .collect(Collectors.toList());
    }

    public int maxPages(List<Head> heads) {
        return (int) Math.ceil((double) heads.size() / (double) HeadsGUI.SIZE);
    }

    public int maxPages() {
        return maxPages(heads);
    }
}
