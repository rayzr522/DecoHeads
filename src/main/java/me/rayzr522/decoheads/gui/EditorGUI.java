package me.rayzr522.decoheads.gui;

import me.rayzr522.decoheads.DecoHeads;
import me.rayzr522.decoheads.data.Head;
import me.rayzr522.decoheads.gui.system.Button;
import me.rayzr522.decoheads.gui.system.Dimension;
import me.rayzr522.decoheads.gui.system.GUI;
import me.rayzr522.decoheads.gui.system.Label;
import me.rayzr522.decoheads.util.Conversations;
import me.rayzr522.decoheads.util.NamePredicate;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class EditorGUI extends GUI {

    public EditorGUI(Player player) {
        super(player, DecoHeads.getInstance().tr(false, "gui.editor.title"), 1);

        init();
    }

    private void init() {
        DecoHeads plugin = DecoHeads.getInstance();

        addComponent(Label.background(0, 0, 9, 1));

        // TODO: Two buttons
        Button searchButton = new Button(
                new ItemStack(Material.COMPASS),
                Dimension.ONE,
                new Dimension(4, 0),
                e -> search(head -> new HeadEditorGUI(getPlayer(), head).render()),
                plugin.tr(false, "button.editor.search.name"),
                plugin.tr(false, "button.editor.search.lore").split("\n")
        );

        searchButton.setCloseOnClick(true);
        addComponent(searchButton);
    }

    private void search(Consumer<Head> callback) {
        DecoHeads plugin = DecoHeads.getInstance();

        Player player = getPlayer();

        Conversations.getString(
                player,
                plugin.tr("gui.search.prompt"),
                query -> {
                    player.sendMessage(plugin.tr("gui.search.searching", query));

                    new HeadsGUI(player, 1, new NamePredicate(query), null, (e, head) -> {
                        e.setShouldClose(true);
                        callback.accept(head);
                    }).render();
                }
        );
    }
}
