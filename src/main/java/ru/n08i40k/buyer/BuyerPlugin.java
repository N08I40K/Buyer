package ru.n08i40k.buyer;

import com.google.common.base.Preconditions;
import lombok.Getter;
import meteordevelopment.orbit.IEventBus;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import ru.n08i40k.buyer.commands.MainCommand;
import ru.n08i40k.buyer.config.BuyerConfig;
import ru.n08i40k.buyer.config.MainConfig;
import ru.n08i40k.buyer.config.buyer.BuyerEntry;
import ru.n08i40k.buyer.events.EventBusManager;
import ru.n08i40k.buyer.gui.GuiInteractListener;
import ru.n08i40k.npluginconfig.Config;
import ru.n08i40k.npluginlocale.Locale;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class BuyerPlugin extends JavaPlugin {
    public static final String PLUGIN_NAME = "Buyer";
    public static final String PLUGIN_NAME_LOWER = "buyer";

    @Getter
    private static BuyerPlugin INSTANCE;

    @Getter
    private Config<MainConfig> mainConfig;
    @Getter
    private Config<BuyerConfig> buyerConfig;

    @Getter
    private BukkitScheduler scheduler;

    @Getter
    private boolean isUnloading;

    private final List<Command> commands = new ArrayList<>();

    @Override
    public void onLoad() {
        INSTANCE = this;

        IEventBus bus = EventBusManager.initEventBus(); // TODO: Make event listeners
    }

    @Override
    public void onEnable() {
        scheduler = getServer().getScheduler();

        if (!getDataFolder().exists()) {
            if (!getDataFolder().mkdir()) {
                getSLF4JLogger().error("Cannot create plugin data dir!");
                disable();
            }
        }

        // Load configs

        mainConfig = new Config<>(this, EventBusManager.getEventBus(), "main", MainConfig.class, null);
        buyerConfig = new Config<>(this, EventBusManager.getEventBus(), "buyer", BuyerConfig.class, Set.of(
                    BuyerEntry.class.getName(),
                    Material.class.getName()));

        Preconditions.checkState(mainConfig.isLoaded(),
                "Main config is not loaded!");
        Preconditions.checkState(buyerConfig.isLoaded(),
                "Buyer config is not loaded!");

        // Load locale

        new Locale(this, EventBusManager.getEventBus(), mainConfig.getData().getLang());

        // Register events handler

        this.getServer().getPluginManager().registerEvents(new EventsListener(), this);
        this.getServer().getPluginManager().registerEvents(new GuiInteractListener(), this);

        // Register commands

        commands.add(new MainCommand());

        getServer().getCommandMap().registerAll(PLUGIN_NAME_LOWER, commands);
    }

    public MainConfig getMainConfigData() {
        return mainConfig.getData();
    }

    public BuyerConfig getBuyerConfigData() {
        return buyerConfig.getData();
    }

    @Override
    public void onDisable() {
        isUnloading = true;

        for (Command command : commands) {
            command.unregister(getServer().getCommandMap());
        }
    }

    public void disable() {
        getPluginLoader().disablePlugin(this);
    }
}
