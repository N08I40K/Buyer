package ru.n08i40k.buyer.api;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Server;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;
import ru.n08i40k.buyer.BuyerPlugin;

public class VaultEconomy {
    private static Economy economy = null;

    @NotNull
    public static Economy getEconomy() {
        if (economy == null) {
            Server server = BuyerPlugin.getINSTANCE().getServer();
            if (server.getPluginManager().getPlugin("Vault") == null)
                throw new RuntimeException("Plugin Vault is not found!");

            RegisteredServiceProvider<Economy> rsp = server.getServicesManager().getRegistration(Economy.class);
            if (rsp == null)
                throw new RuntimeException("RegisteredServiceProvider for Economy class is not found!");

            economy = rsp.getProvider();
        }

        return economy;
    }
}
