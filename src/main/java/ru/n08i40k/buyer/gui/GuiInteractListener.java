package ru.n08i40k.buyer.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.n08i40k.buyer.BuyerPlugin;
import ru.n08i40k.buyer.config.buyer.BuyerEntryAccessor;
import ru.n08i40k.npluginlocale.Locale;

import java.util.Map;

public class GuiInteractListener implements Listener {
    static class UpdateInventoryTask implements Runnable {
        private final GuiHolder holder;

        public UpdateInventoryTask(GuiHolder holder) {
            this.holder = holder;
        }

        @Override
        public void run() {
            holder.updateInventory();
        }
    }

    private final BuyerPlugin plugin;
    private final Locale locale;

    public GuiInteractListener() {
        plugin = BuyerPlugin.getINSTANCE();
        locale = Locale.getInstance();
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();

        if (inventory.getHolder() != null && inventory.getHolder() instanceof GuiHolder holder) {
            holder.sell((Player) event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDrag(InventoryDragEvent event) {
        Inventory inventory = event.getInventory();

        if (inventory.getHolder() != null && inventory.getHolder() instanceof GuiHolder holder) {
            CustomInventory customInventory = holder.getCustomInventory();
            Map<Material, Integer> materialCosts = BuyerEntryAccessor.getMaterialCosts(holder.getBuyerName());

            for (Integer slot : event.getNewItems().keySet()) {
                if (slot > 9 * 5 && slot < 9 * 6) {
                    event.setCancelled(true);
                    return;
                }

                if (slot > 9 * 5)
                    continue;

                ItemStack itemStack = event.getNewItems().get(slot);
                if (!materialCosts.containsKey(itemStack.getType())) {
                    locale.get("inventory.cant-sell-item", itemStack.getType()).getSingle()
                            .sendMessage(event.getWhoClicked());
                    event.setCancelled(true);
                    return;
                }
            }

            event.getNewItems().forEach((slot, itemStack) -> {
                if (slot > 9 * 5)
                    return;

                customInventory.addItem(new ItemStackWithCost(itemStack, materialCosts.get(itemStack.getType())));
            });

            plugin.getScheduler().runTaskLater(plugin, new UpdateInventoryTask(holder), 1);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();

        if (inventory.getHolder() != null && inventory.getHolder() instanceof GuiHolder holder) {

            CustomInventory customInventory = holder.getCustomInventory();

            event.setCancelled(true);

            if (event.getClickedInventory() == inventory) {

                if (event.getSlot() >= inventory.getSize() - 9) {

                    int slot = event.getSlot() - inventory.getSize() + 9;

                    switch (slot) {
                        case 0, 1, 2, 6, 7, 8: {
                            break;
                        }
                        case 3: {
                            holder.setPage(Math.max(0, holder.getPage() - 1));
                            break;
                        }
                        case 4: {
                            event.getWhoClicked().closeInventory(InventoryCloseEvent.Reason.PLAYER);
                            break;
                        }
                        case 5: {
                            holder.setPage(holder.getPage() + 1);
                            break;
                        }
                        default: {
                            throw new IllegalArgumentException("Illegal item slot in gui!");
                        }
                    }

                    holder.updateInventory();
                    return;
                }

                int slot = event.getSlot();

                if (event.getAction() == InventoryAction.PICKUP_ALL) {
                    removeItemFromBuyerMenu(event, holder, customInventory, slot);
                    return;
                }

                if (event.getAction() == InventoryAction.PLACE_ALL) {
                    ItemStack itemStack = event.getCursor();
                    if (addItemToBuyerMenu(event, holder, customInventory, itemStack)) return;

                    event.setCancelled(false);
                    plugin.getScheduler().runTaskLater(plugin, new UpdateInventoryTask(holder), 1);

                    return;
                }
                if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                    removeItemFromBuyerMenu(event, holder, customInventory, slot);
                    return;
                }
                return;
            }

            if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                event.setCancelled(true);

                ItemStack itemStack = event.getCurrentItem();

                if (addItemToBuyerMenu(event, holder, customInventory, itemStack)) return;

                assert event.getClickedInventory() != null;
                event.getClickedInventory().setItem(event.getSlot(), new ItemStack(Material.AIR));

                plugin.getScheduler().runTaskLater(plugin, new UpdateInventoryTask(holder), 1);
            } else if (event.getAction() != InventoryAction.HOTBAR_SWAP) {
                event.setCancelled(false);
            }
        }
    }

    private boolean addItemToBuyerMenu(InventoryClickEvent event, GuiHolder holder, CustomInventory customInventory, ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR)
            return true;

        Map<Material, Integer> materialCosts = BuyerEntryAccessor.getMaterialCosts(holder.getBuyerName());
        if (!materialCosts.containsKey(itemStack.getType())) {
            locale.get("inventory.cant-sell-item", itemStack.getType()).getSingle()
                    .sendMessage(event.getWhoClicked());
            return true;
        }
        customInventory.addItem(new ItemStackWithCost(itemStack, materialCosts.get(itemStack.getType())));
        return false;
    }

    private void removeItemFromBuyerMenu(InventoryClickEvent event, GuiHolder holder, CustomInventory customInventory, int slot) {
        if (slot > customInventory.getPage(holder.getPage()).size() - 1)
            return;

        ItemStackWithCost itemStackWithCost = customInventory.getItem(holder.getPage(), slot);
        itemStackWithCost.revertDescription();
        event.setCurrentItem(itemStackWithCost);

        event.setCancelled(false);
        customInventory.removeItem(holder.getPage(), slot);

        plugin.getScheduler().runTaskLater(plugin, new UpdateInventoryTask(holder), 2);
    }
}
