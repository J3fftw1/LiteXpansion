package dev.j3fftw.litexpansion.items;

import dev.j3fftw.litexpansion.Items;
import dev.j3fftw.litexpansion.LiteXpansion;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.core.attributes.Rechargeable;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PortableCharger extends SimpleSlimefunItem<ItemUseHandler> implements Listener, Rechargeable {

    public PortableCharger() {
        super(Items.LITEXPANSION, Items.PORTABLE_CHARGER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
            Items.REFINED_IRON, null, Items.REFINED_IRON,
            null, Items.ADVANCED_CIRCUIT, null,
            null, Items.ADVANCED_CIRCUIT, null
        });

        Bukkit.getPluginManager().registerEvents(this, LiteXpansion.getInstance());
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> e.setUseBlock(Event.Result.DENY);
    }

    @EventHandler
    public void onInventorySwap(InventoryClickEvent e) {
        // getCursor returns the item you clicked with, getCurrentItem returns the item that was in the slot.
        Player p = (Player) e.getWhoClicked();
        if (isItem(e.getCursor())) {
            p.sendMessage("You have used the charger.");
            if (e.getCurrentItem() instanceof SlimefunItemStack) {
                if (((SlimefunItemStack) e.getCurrentItem()).getItem() != null) {
                    return;
                } else {
                    e.setCancelled(true);
                }
            }
        }
    }

    @Override
    public float getMaxItemCharge(ItemStack itemStack) {
        return 10;
    }
}