package ru.n08i40k.buyer.gui;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.kyori.adventure.text.TextComponent;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.n08i40k.buyer.api.VaultEconomy;
import ru.n08i40k.npluginlocale.Locale;

import java.util.List;

@Getter
public class GuiHolder implements InventoryHolder {

    public void updateInventory() {
        inventory.clear();

        List<ItemStackWithCost> items = customInventory.getPage(page);
        pageControllerLine.updateItems(items);

        for (int i = 0; i < items.size(); ++i)
            inventory.setItem(i, items.get(i));

        pageControllerLine.updateLine();
        List<ItemStack> line = pageControllerLine.getLine();
        for (int i = 0; i < 9; ++i) {
            inventory.setItem(inventory.getSize() - 9 + i, line.get(i));
        }
    }

    private final Inventory inventory;
    private final CustomInventory customInventory;
    private final String buyerName;
    private final PageControllerLine pageControllerLine;

    @Setter
    private int page;

    public GuiHolder(TextComponent title, String buyerName) {
        this.inventory = Bukkit.createInventory(this, 54, title);
        this.buyerName = buyerName;
        customInventory = new CustomInventory();
        pageControllerLine = new PageControllerLine(this);
        page = 0;
    }

    public void sell(@NonNull Player player) {
        Economy economy = VaultEconomy.getEconomy();

        int totalCost = customInventory.getTotalCost();
        int count = customInventory.getCount();

        economy.depositPlayer(player, totalCost);

        inventory.clear();
        this.getCustomInventory().getInventory().clear();

        Locale.getInstance().get("inventory.close", count, totalCost).getSingle().sendMessage(player);
    }
}