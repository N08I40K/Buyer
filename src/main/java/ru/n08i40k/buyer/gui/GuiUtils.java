package ru.n08i40k.buyer.gui;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.inventory.Inventory;
import ru.n08i40k.buyer.config.buyer.BuyerEntryAccessor;

public class GuiUtils {
    public static Inventory getInventory(String entryName) {
        TextComponent entryGameName = BuyerEntryAccessor.getDisplayName(entryName).getC();

        GuiHolder holder = new GuiHolder(entryGameName, entryName);
        holder.updateInventory();

        return holder.getInventory();
    }
}
