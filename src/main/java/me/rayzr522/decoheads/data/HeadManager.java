package me.rayzr522.decoheads.data;

import me.rayzr522.decoheads.Category;
import me.rayzr522.decoheads.DecoHeads;
import me.rayzr522.decoheads.gui.HeadsGUI;
import me.rayzr522.decoheads.util.ConfigVersionChecker;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Rayzr
 */
public class HeadManager {

    /**
     * The version to check in the config
     */
    private static final int CONFIG_VERSION = 2;

    private DecoHeads plugin;
    private Set<Head> heads;

    public HeadManager(DecoHeads plugin) {
        this.plugin = plugin;
    }

    public void load() throws IOException {
        YamlConfiguration config = ConfigVersionChecker.updateConfig("heads.yml", CONFIG_VERSION);
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
                .collect(Collectors.toSet());
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

    public Collection<Head> getHeads() {
        return heads;
    }

    public List<Head> getHeadsFor(CommandSender sender) {
        return heads.stream()
                .filter(head -> head.getCategory().hasPermission(sender))
                .filter(head -> head.isUseableBy(sender))
                .sorted(Comparator.comparing(Head::getName, String::compareToIgnoreCase))
                .collect(Collectors.toList());
    }

    public int maxPages(Collection<Head> heads) {
        return (int) Math.ceil((double) heads.size() / (double) HeadsGUI.SIZE);
    }

    public int maxPages() {
        return maxPages(heads);
    }

    public Optional<Head> findByName(String name) {
        return getHeads().stream().filter(head -> head.getName().toLowerCase().equals(name.toLowerCase())).findFirst();
    }

    public void addHead(Head head) {
        if (findByName(head.getName()).isPresent()) {
            throw new IllegalArgumentException("The head '" + head.getName() + "' already exists!");
        }
        heads.add(head);
    }

    public void removeHead(Head head) {
        heads.remove(head);
    }
}
