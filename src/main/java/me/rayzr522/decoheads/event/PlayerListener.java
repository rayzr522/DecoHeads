package me.rayzr522.decoheads.event;

import me.rayzr522.decoheads.DecoHeads;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.inventivetalent.update.spiget.UpdateCallback;

/**
 * @author Rayzr522
 */
public class PlayerListener implements Listener {
    private final DecoHeads plugin;

    public PlayerListener(DecoHeads plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        // If updates are enabled and this player should get update messages...
        if (!plugin.getSettings().isUpdaterEnabled() || !plugin.checkPermission("update-notify", e.getPlayer(), false)) {
            return;
        }

        plugin.getUpdater().checkForUpdate(new UpdateCallback() {
            @Override
            public void updateAvailable(String version, String downloadURL, boolean hasDirectDownload) {
                e.getPlayer().sendMessage(plugin.tr("update-available", version, downloadURL));
            }

            @Override
            public void upToDate() {
                // Ignored
            }
        });
    }
}
