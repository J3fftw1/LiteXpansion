package dev.j3fftw.litexpansion.items;

import dev.j3fftw.litexpansion.Items;
import dev.j3fftw.litexpansion.LiteXpansion;
import dev.j3fftw.litexpansion.utils.Utils;
import io.github.thebusybiscuit.slimefun4.core.attributes.Rechargeable;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * The {@link PortableCharger} is an item that can
 * be clicked into any {@link Rechargeable} item to
 * transfer power to it.
 *
 * @author FluffyBear
 *
 */
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
        if (isItem(e.getCursor())) {
            Player p = (Player) e.getWhoClicked();
            ItemStack chargerItem = e.getCursor();
            Rechargeable charger = (Rechargeable) SlimefunItem.getByItem(chargerItem);
            ItemStack deviceItem = e.getCurrentItem();
            SlimefunItem sfItem = SlimefunItem.getByItem(deviceItem);

            if (sfItem instanceof Rechargeable) {
                Rechargeable device = (Rechargeable) sfItem;
                float neededCharge = device.getMaxItemCharge(deviceItem) - device.getItemCharge(deviceItem);
                float availableCharge = charger.getItemCharge(chargerItem);

                if (neededCharge > 0 && availableCharge > 0) {

                    if (availableCharge >= neededCharge) {
                        charger.removeItemCharge(chargerItem, neededCharge);
                        device.addItemCharge(deviceItem, neededCharge);

                    } else {
                        charger.removeItemCharge(chargerItem, availableCharge);
                        device.addItemCharge(deviceItem, availableCharge);
                    }

                    availableCharge = charger.getItemCharge(chargerItem);
                    Utils.lxPrefixMessage(p, ChatColor.GREEN, "Your item has been charged!");
                    Utils.lxPrefixMessage(p, ChatColor.YELLOW, "Your charger has " + availableCharge + "J left.");

                } else if (neededCharge == 0) {
                    Utils.lxPrefixMessage(p, ChatColor.RED, "This item is already full!");

                } else {
                    Utils.lxPrefixMessage(p, ChatColor.RED, "Your charger does not have enough power!");
                }
            }
        }
    }

    @Override
    public float getMaxItemCharge(ItemStack itemStack) {
        return 1000;
    }
}