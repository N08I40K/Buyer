package ru.n08i40k.buyer.gui;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.n08i40k.npluginlocale.Locale;
import ru.n08i40k.npluginlocale.LocaleRequestBuilder;

import java.util.List;
import java.util.Map;

public class PageControllerLine {
    @Getter
    private List<ItemStack> line;
    private final GuiHolder holder;

    public PageControllerLine(GuiHolder guiHolder) {
        holder = guiHolder;
    }

    public void updateLine() {
        Locale locale = Locale.getInstance();

        ItemStack air = new ItemStack(Material.AIR);

        ItemStack back = new ItemStack(Material.SEA_LANTERN);
        ItemStack sell = new ItemStack(Material.EMERALD);
        ItemStack front = new ItemStack(Material.SEA_LANTERN);

        ItemMeta meta = new ItemStack(Material.STONE).getItemMeta();
        ItemMeta pageMeta = new ItemStack(Material.STONE).getItemMeta();

        LocaleRequestBuilder buyerGUI = new LocaleRequestBuilder(null, "item.buyer_gui");

        // Set chance description

        List<Component> pageLore = List.of(buyerGUI.get("page_select.description", holder.getPage())
                .getSingle().getC());
        pageMeta.lore(pageLore);

        List<Component> sellLore = buyerGUI.get("sell.description")
                        .format(Map.of(
                                "total_cost", holder.getCustomInventory().getTotalCost(),
                                "item_count", holder.getCustomInventory().getCount()
                        )).getMultiple().getC();
        meta.lore(sellLore);

        // Set names
        pageMeta.displayName(buyerGUI.get("page_select.back.name").getSingle().getC()); back.setItemMeta(pageMeta);
        meta.displayName(buyerGUI.get("sell.name").getSingle().getC()); sell.setItemMeta(meta);
        pageMeta.displayName(buyerGUI.get("page_select.front.name").getSingle().getC()); front.setItemMeta(pageMeta);

        line = List.of(air, air, air, back, sell, front, air, air, air);
    }

    public void updateItems(List<ItemStackWithCost> page) {
        for (ItemStackWithCost itemStackWithCost : page) {
            itemStackWithCost.applyDescription();
        }
    }
}
