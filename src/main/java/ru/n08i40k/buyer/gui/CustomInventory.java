package ru.n08i40k.buyer.gui;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class CustomInventory {
    public static final int PAGE_SIZE = 9*5;

    @Setter
    private List<ItemStackWithCost> inventory;

    public CustomInventory() {
        inventory = new ArrayList<>();
    }

    public List<ItemStackWithCost> getPage(int pageNumber) {
        List<ItemStackWithCost> page = new ArrayList<>();

        int pageMin = pageNumber * PAGE_SIZE;
        int pageMax = pageMin + PAGE_SIZE;

        for (int i = pageMin; i < pageMax; ++i) {
            if (i >= inventory.size()) break;

            page.add(inventory.get(i));
        }

        return page;
    }

    public void removeItem(int pageNumber, int slot) {
        List<ItemStackWithCost> page = getPage(pageNumber);

        if (slot >= page.size())
            return;

        inventory.remove(PAGE_SIZE * pageNumber + slot);
    }

    public void addItem(ItemStackWithCost itemStackWithCost) {
        inventory.add(itemStackWithCost);
    }

    public void addNewItem(ItemStack itemStack, int cost) {
        ItemStackWithCost itemStackWithCost = new ItemStackWithCost(itemStack, cost);

        inventory.add(itemStackWithCost);
    }

    public ItemStackWithCost getItem(int page, int slot) {
        int index = PAGE_SIZE * page + slot;

        if (inventory.size() <= index)
            throw new IndexOutOfBoundsException();

        return inventory.get(index);
    }

    public int getCount() {
        AtomicInteger count = new AtomicInteger();

        inventory.forEach(itemStackWithCost -> count.addAndGet(itemStackWithCost.getAmount()));

        return count.get();
    }

    public int getTotalCost() {
        AtomicInteger totalCost = new AtomicInteger();

        inventory.forEach(itemStackWithCost -> totalCost.addAndGet(itemStackWithCost.getTotalCost()));

        return totalCost.get();
    }
}
