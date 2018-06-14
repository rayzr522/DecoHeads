package me.rayzr522.decoheads.compat;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * @author Rayzr522
 */
public class EconomyWrapper {
    private Economy economy;

    public double getBalance(OfflinePlayer offlinePlayer) {
        return economy.getBalance(offlinePlayer);
    }

    public EconomyResponseWrapper withdrawPlayer(OfflinePlayer offlinePlayer, double amount) {
        return new EconomyResponseWrapper(economy.withdrawPlayer(offlinePlayer, amount));
    }

    public boolean setup() {
        RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServicesManager().getRegistration(Economy.class);
        return economyProvider != null && (economy = economyProvider.getProvider()) != null;
    }

    public class EconomyResponseWrapper {
        private final EconomyResponse response;

        public EconomyResponseWrapper(EconomyResponse response) {
            this.response = response;
        }

        public boolean transactionSuccess() {
            return response.transactionSuccess();
        }
    }
}
