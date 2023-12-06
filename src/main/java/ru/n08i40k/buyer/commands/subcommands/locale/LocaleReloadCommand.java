package ru.n08i40k.buyer.commands.subcommands.locale;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import ru.n08i40k.buyer.commands.SubCommand;

public class LocaleReloadCommand extends SubCommand {
    public LocaleReloadCommand(@NotNull SubCommand parentCommand) {
        super(parentCommand);
    }

    @Override
    public @NotNull String getName() {
        return "reload";
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        locale.reload(null);

        localeRequestBuilder.get("has-been-reloaded")
                .getSingle().sendMessage(sender);

        return true;
    }
}
