package ru.n08i40k.buyer.commands.subcommands;

import com.google.common.collect.ImmutableList;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.n08i40k.buyer.commands.SubCommand;
import ru.n08i40k.buyer.config.buyer.BuyerEntryAccessor;
import ru.n08i40k.buyer.gui.GuiUtils;

import java.util.List;
import java.util.Set;

public class OpenCommand extends SubCommand {
    public OpenCommand(@Nullable SubCommand parentCommand) {
        super(parentCommand);
    }

    @Override
    public @NotNull String getName() {
        return "open";
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        Set<String> entries = BuyerEntryAccessor.getBuyerCatalog().keySet();

        // Get chest name

        if (args.length == 0) {
            localeRequestBuilder.get("entry-is-not-present", String.join(", ", entries))
                    .getSingle().sendMessage(sender);
            return false;
        }

        String entryName = args[0];

        if (!entries.contains(entryName)) {
            localeRequestBuilder.get("incorrect-entry", String.join(", ", entries))
                    .getSingle().sendMessage(sender);
            return false;
        }

        Player player = (Player) sender;
        Inventory inventory = GuiUtils.getInventory(entryName);

        player.openInventory(inventory);
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1)
            return getAutocompletion(args[0], BuyerEntryAccessor.getBuyerCatalog().keySet());

        return ImmutableList.of();
    }
}
