package ru.n08i40k.buyer.gui;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.n08i40k.npluginlocale.LocaleRequestBuilder;

import java.util.List;
import java.util.Map;

public class ItemStackWithCost extends ItemStack {
    private final static LocaleRequestBuilder itemDescription =
            new LocaleRequestBuilder(null, "item.buyer_gui.item_description");

    public void setCost(int cost) {
        if (cost < 0) {
            throw new IllegalArgumentException("Cost must be greater than 0!");
        }

        this.cost = cost;
    }

    @Getter
    private int cost;
    private List<Component> originalLore;

    public ItemStackWithCost(ItemStack itemStack, int cost) {
        super(itemStack);

        ItemMeta meta = itemStack.getItemMeta();

        if (meta.hasLore()) {
            List<Component> lore = meta.lore();

            originalLore = lore == null ? List.of() : lore;
        }

        setCost(cost);
    }

    public void applyDescription() {
        ItemMeta meta = this.getItemMeta();
        if (meta == null)
            return;

        List<Component> newLore = itemDescription.get(null)
                .format(Map.of(
                        "item_cost", cost,
                        "total_cost", cost * this.getAmount()
                )).getMultiple().getC();

        meta.lore(newLore);
        this.setItemMeta(meta);
    }

    public void revertDescription() {
        ItemMeta meta = this.getItemMeta();
        if (meta == null)
            return;

        meta.lore(originalLore);
        this.setItemMeta(meta);
    }

    public int getTotalCost() {
        return cost * getAmount();
    }
}
