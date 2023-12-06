package ru.n08i40k.buyer;

import org.bukkit.event.Listener;

public class EventsListener implements Listener {
    private final BuyerPlugin plugin;

    public EventsListener() {
        plugin = BuyerPlugin.getINSTANCE();
    }
}
