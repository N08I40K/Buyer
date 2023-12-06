package ru.n08i40k.buyer.commands.subcommands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.n08i40k.buyer.commands.SubCommand;
import ru.n08i40k.buyer.commands.subcommands.admin.ConfigReloadCommand;

public class AdminCommand extends SubCommand {
    public AdminCommand(@Nullable SubCommand parent) {
        super(parent);

        subcommands.put(new ConfigReloadCommand(this));
    }

    @Override
    public @NotNull String getName() {
        return "admin";
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        localeRequestBuilder.get("select-action", String.join(", ", subcommands.keySet()))
                .getSingle().sendMessage(sender);
        return false;
    }
}
