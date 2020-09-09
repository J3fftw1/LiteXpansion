package dev.j3fftw.litexpansion.items;

import dev.j3fftw.litexpansion.Items;
import dev.j3fftw.litexpansion.LiteXpansion;
import dev.j3fftw.litexpansion.machine.MassFabricator;
import dev.j3fftw.litexpansion.utils.Utils;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.core.attributes.Rechargeable;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.EnergyRegulator;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.holograms.SimpleHologram;
import lombok.NonNull;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.InventoryBlock;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemHandler;
import me.mrCookieSlime.Slimefun.cscorelib2.chat.ChatColors;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.GlassPane;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * The {@link PortableCharger} is an item that can
 * be clicked into any {@link Rechargeable} item to
 * transfer power to it.
 *
 * @author FluffyBear
 *
 */
public class PortableCharger extends SimpleSlimefunItem<ItemUseHandler> implements Listener, InventoryBlock, Rechargeable {

    private final int[] border = { 5, 6, 7, 14, 16, 23, 24, 25 };

    private int powerSlot = 11;

    private int chargeSlot = 15;

    private BukkitTask task;

    Plugin plugin = LiteXpansion.getInstance();

    public PortableCharger() {
        super(Items.LITEXPANSION, Items.PORTABLE_CHARGER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
            Items.REFINED_IRON, null, Items.REFINED_IRON,
            null, Items.ADVANCED_CIRCUIT, null,
            null, Items.ADVANCED_CIRCUIT, null
        });

        Bukkit.getPluginManager().registerEvents(this, LiteXpansion.getInstance());
    }

    public static class Ticker implements Runnable{
        public void run(){
        }
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            e.cancel();

            // Get variables
            final Player p = e.getPlayer();
            final ItemStack chargerItem = e.getItem();
            final Rechargeable charger = (Rechargeable) SlimefunItem.getByItem(chargerItem);

            // Create GUI
            Inventory inventory = Bukkit.createInventory(null, 27, ChatColor.GOLD + "Portable Charger");

            ItemStack backgroundItem = buildNonInteractable(Material.GRAY_STAINED_GLASS_PANE, null);
            ItemStack borderItem = buildNonInteractable(Material.YELLOW_STAINED_GLASS_PANE, null);
            ItemStack powerItem = buildNonInteractable(Material.GLOWSTONE, "&4Power");

            for (int i = 0; i < 27; i++)
                inventory.setItem(i, backgroundItem);

            for (int slot : border)
                inventory.setItem(slot, borderItem);

            inventory.setItem(powerSlot, powerItem);
            inventory.clear(chargeSlot);

            e.cancel();
            p.openInventory(inventory);

            boolean lockCharger = true;

            // Task that triggers every second
            task = plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
                ItemStack deviceItem = inventory.getItem(chargeSlot);
                SlimefunItem sfItem = SlimefunItem.getByItem(deviceItem);
                if (sfItem instanceof Rechargeable && !isItem(deviceItem)) {
                    Rechargeable device = (Rechargeable) sfItem;
                    float neededCharge = device.getMaxItemCharge(deviceItem) - device.getItemCharge(deviceItem);
                    float availableCharge = charger.getItemCharge(chargerItem);

                    if (neededCharge > 0 && availableCharge > 0) {

                        if (neededCharge >= 10 && availableCharge >= 10) {
                            charger.removeItemCharge(chargerItem, 10);
                            device.addItemCharge(deviceItem, 10);

                        } else if (neededCharge < availableCharge) {
                            charger.removeItemCharge(chargerItem, neededCharge);
                            device.addItemCharge(deviceItem, neededCharge);
                        } else {
                            charger.removeItemCharge(chargerItem, availableCharge);
                            device.addItemCharge(deviceItem, availableCharge);
                        }

                    } else if (neededCharge == 0) {
                        Utils.send(p, "&cThis item is already full!");

                    } else {
                        Utils.send(p, "&cYour charger does not have enough power!");
                    }
                    p.sendMessage("Tick");
                }

                // Check if GUI is no longer open
                if (p.getOpenInventory().getType() != InventoryType.CHEST) {
                    task.cancel();

                    ItemStack forgottenItem = inventory.getItem(chargeSlot);

                    // Check if player left an item inside
                    if (forgottenItem != null) {
                        Utils.send(p, "&cHey! You left something in the charger! Dropping it now...");
                        p.getWorld().dropItemNaturally(p.getLocation(), forgottenItem);
                    }

                }
            }, 0L, 20L);

        };
    }


    @EventHandler
    public void onNonClickableClick(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        if (item != null && item.getItemMeta().hasCustomModelData()
            && item.getItemMeta().getCustomModelData() == 6969) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onChargerClick(InventoryClickEvent e) {
        if (isItem(e.getCursor()) &&  = true) {

        }
    }

    /*
    @EventHandler
    public void onInventorySwap(InventoryClickEvent e) {
        // getCursor returns the item you clicked with, getCurrentItem returns the item that was in the slot.
        if (isItem(e.getCursor())) {
            final Player p = (Player) e.getWhoClicked();
            final ItemStack chargerItem = e.getCursor();
            final Rechargeable charger = (Rechargeable) SlimefunItem.getByItem(chargerItem);
            final ItemStack deviceItem = e.getCurrentItem();
            final SlimefunItem sfItem = SlimefunItem.getByItem(deviceItem);

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
                    Utils.send(p, "&aYour item has been charged!");
                    Utils.send(p, "&eYour charger has " + availableCharge + "J left.");

                } else if (neededCharge == 0) {
                    Utils.send(p, "&cThis item is already full!");

                } else {
                    Utils.send(p, "&cYour charger does not have enough power!");
                }
            }
        }
    }

     */

    public ItemStack buildNonInteractable(Material material, @Nullable String name, @Nullable String... lore) {
        ItemStack nonClickable = new ItemStack(material);
        ItemMeta NCMeta = nonClickable.getItemMeta();
        if (name != null) {
            NCMeta.setDisplayName(ChatColors.color(name));
        } else {
            NCMeta.setDisplayName(" ");
        }

        if (lore.length > 0) {
            List<String> lines = new ArrayList();
            String[] loreString = lore;
            int loreLength = lore.length;

            for(int i = 0; i < loreLength; ++i) {
                String line = loreString[i];
                lines.add(ChatColor.translateAlternateColorCodes('&', line));
            }
            NCMeta.setLore(lines);
        }
        NCMeta.setCustomModelData(6969);
        nonClickable.setItemMeta(NCMeta);
        return nonClickable;
    }

    public void updateSlot(Inventory inventory, int slot, String name, String... lore) {
        ItemStack item = inventory.getItem(slot);
        ItemMeta slotMeta = item.getItemMeta();
        if (name != null) {
            slotMeta.setDisplayName(ChatColors.color(name));
        } else {
            slotMeta.setDisplayName(" ");
        }

        if (lore.length > 0) {
            List<String> lines = new ArrayList();
            String[] loreString = lore;
            int loreLength = lore.length;

            for(int i = 0; i < loreLength; ++i) {
                String line = loreString[i];
                lines.add(ChatColor.translateAlternateColorCodes('&', line));
            }
            slotMeta.setLore(lines);
        }
        item.setItemMeta(slotMeta);
        inventory.setItem(slot, item);
        System.out.println(item);
    }

    @Override
    public float getMaxItemCharge(ItemStack itemStack) {
        return 1000;
    }

    @Override
    public int[] getInputSlots() {
        return new int[13];
    }

    @Override
    public int[] getOutputSlots() {
        return new int[16];
    }
}