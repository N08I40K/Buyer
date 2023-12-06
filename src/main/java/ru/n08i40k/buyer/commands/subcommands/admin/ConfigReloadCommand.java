package ru.n08i40k.buyer.commands.subcommands.admin;

import com.google.common.base.Preconditions;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.n08i40k.buyer.commands.SubCommand;

public class ConfigReloadCommand extends SubCommand {
    public ConfigReloadCommand(@Nullable SubCommand parentCommand) {
        super(parentCommand);
    }

    @Override
    public @NotNull String getName() {
        return "reload";
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        Preconditions.checkState(plugin.getMainConfig().load(),
                "Can't reload main config!");
        Preconditions.checkState(plugin.getBuyerConfig().load(),
                "Can't reload buyer config!");

        localeRequestBuilder.get("has-been-reloaded")
                .getSingle().sendMessage(sender);

        return true;
    }
}
