package ru.n08i40k.buyer.utils;

import org.slf4j.Logger;
import ru.n08i40k.buyer.BuyerPlugin;
import ru.n08i40k.npluginlocale.Locale;

public class PluginUse {
    protected final BuyerPlugin plugin;
    protected final Locale locale;
    protected final Logger logger;

    public PluginUse() {
        plugin = BuyerPlugin.getINSTANCE();
        locale = Locale.getInstance();
        logger = plugin.getSLF4JLogger();
    }
}
