package dev.j3fftw.litexpansion.items;

import dev.j3fftw.litexpansion.Items;
import dev.j3fftw.litexpansion.LiteXpansion;
import dev.j3fftw.litexpansion.utils.Utils;
import io.github.thebusybiscuit.slimefun4.core.attributes.Rechargeable;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.cscorelib2.chat.ChatColors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@link PortableCharger} is an item that opens
 * a portable charging GUI that charges any
 * {@link Rechargeable} item.
 *
 * @author FluffyBear
 *
 */
public class PortableCharger extends SimpleSlimefunItem<ItemUseHandler> implements Listener, Rechargeable {

    private final int[] border = { 5, 6, 7, 14, 16, 23, 24, 25 };

    private final int powerSlot = 11;

    private final int chargeSlot = 15;

    private final int chargeSpeed = 20;

    Plugin plugin = LiteXpansion.getInstance();

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
        return e -> {
            e.cancel();

            // Get variables
            final Player p = e.getPlayer();
            final ItemStack chargerItem = e.getItem();
            final Rechargeable charger = (Rechargeable) SlimefunItem.getByItem(chargerItem);

            // Create GUI Items
            Inventory inventory = Bukkit.createInventory(null, 27, ChatColor.GOLD + "Portable Charger");

            ItemStack backgroundItem = buildNonInteractable(Material.GRAY_STAINED_GLASS_PANE, null);
            ItemStack borderItem = buildNonInteractable(Material.YELLOW_STAINED_GLASS_PANE, null);
            ItemStack powerItem = buildNonInteractable(Material.GLOWSTONE, "&4Power");

            // Build and open GUI
            for (int i = 0; i < 27; i++)
                inventory.setItem(i, backgroundItem);

            for (int slot : border)
                inventory.setItem(slot, borderItem);

            inventory.setItem(powerSlot, powerItem);
            updateSlot(inventory, powerSlot, "&6&lPower Remaining",
                "&e" + charger.getItemCharge(chargerItem) + "J");
            inventory.clear(chargeSlot);
            p.openInventory(inventory);

            // Task that triggers every second
            new BukkitRunnable(){
                public void run() {

                    ItemStack deviceItem = inventory.getItem(chargeSlot);
                    SlimefunItem sfItem = SlimefunItem.getByItem(deviceItem);

                    if (sfItem instanceof Rechargeable && !isItem(deviceItem)) {

                        Rechargeable device = (Rechargeable) sfItem;
                        float neededCharge = device.getMaxItemCharge(deviceItem)
                            - device.getItemCharge(deviceItem);
                        float availableCharge = charger.getItemCharge(chargerItem);

                        // Three different scenarios
                        if (neededCharge > 0 && availableCharge > 0) {

                            if (neededCharge >= chargeSpeed && availableCharge >= chargeSpeed) {
                                charger.removeItemCharge(chargerItem, chargeSpeed);
                                device.addItemCharge(deviceItem, chargeSpeed);

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

                        // The name of the powerItem NEEDS to be "Portable Charger" to cancel event
                        updateSlot(inventory, powerSlot, "&6&lPower Remaining",
                            "&e" + charger.getItemCharge(chargerItem) + "J");
                    }

                    // Check if GUI is no longer open
                    if (p.getOpenInventory().getTopInventory() != inventory) {
                        cancel();

                        ItemStack forgottenItem = inventory.getItem(chargeSlot);

                        // Check if player left an item inside
                        if (forgottenItem != null) {
                            Utils.send(p, "&cHey! You left something in the charger! Dropping it now...");
                            p.getWorld().dropItemNaturally(p.getLocation(), forgottenItem);
                        }
                    }
                }
            }.runTaskTimer(plugin, 0, 20);
        };
    }

    // This is used to make the items non clickable
    @EventHandler
    public void onNonClickableClick(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        System.out.println(item);
        if (item != null && item.getType() != Material.AIR && item.getItemMeta().hasCustomModelData()
            && item.getItemMeta().getCustomModelData() == 6969) {
            e.setCancelled(true);
        }
    }

    // Prevent the player from shoving the charger into itself or other chargers
    // Uses a kinda wonky way by looking for the power display in the specific slot
    @EventHandler
    public void onChargerClick(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        Player p = (Player) e.getWhoClicked();
        if (isItem(item) && p.getOpenInventory().getTopInventory().getItem(powerSlot) != null
            && p.getOpenInventory().getTopInventory().getItem(powerSlot).
            getItemMeta().getDisplayName().contains("Power Remaining")) {
            e.setCancelled(true);
        }
    }

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
    }

    @Override
    public float getMaxItemCharge(ItemStack itemStack) {
        return 1000;
    }
}