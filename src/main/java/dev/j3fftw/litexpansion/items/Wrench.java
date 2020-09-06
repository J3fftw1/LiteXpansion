package dev.j3fftw.litexpansion.items;

import dev.j3fftw.litexpansion.Items;
import dev.j3fftw.litexpansion.LiteXpansion;
import dev.j3fftw.litexpansion.utils.Constants;
import io.github.thebusybiscuit.slimefun4.core.attributes.DamageableItem;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.core.networks.cargo.CargoNet;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.InventoryBlock;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * This {@link SimpleSlimefunItem} allows players to
 * instantly break any {@link EnergyNetComponent} block
 * or {@link CargoNet} block.
 *
 * @author FluffyBear
 *
 */
public class Wrench extends SimpleSlimefunItem<ItemUseHandler> implements Listener,DamageableItem {

    private static final int[] cargoSlots = { 19, 20, 21, 28, 29, 30, 37, 38, 39 };
    private static final String[] specialBlockIds = { SlimefunItems.CARGO_INPUT_NODE.getItemId(), SlimefunItems.CARGO_OUTPUT_NODE.getItemId(), SlimefunItems.CARGO_OUTPUT_NODE_2.getItemId(), SlimefunItems.CARGO_CONNECTOR_NODE.getItemId(), SlimefunItems.SMALL_CAPACITOR.getItemId(), SlimefunItems.MEDIUM_CAPACITOR.getItemId(), SlimefunItems.LARGE_CAPACITOR.getItemId(), SlimefunItems.BIG_CAPACITOR.getItemId(), SlimefunItems.CARBONADO_EDGED_CAPACITOR.getItemId(), SlimefunItems.TRASH_CAN.getItemId() };

    public Wrench() {
        super(Items.LITEXPANSION, Items.WRENCH, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Items.REFINED_IRON, null, Items.REFINED_IRON,
                null, Items.REFINED_IRON, null,
                null, Items.REFINED_IRON, null
        });

        Bukkit.getPluginManager().registerEvents(this, LiteXpansion.getInstance());
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> e.setUseBlock(Event.Result.DENY);
    }

    @EventHandler
    public void onWrenchInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        // Check if player has wrench and is left clicking block
        // Can't use offhand because a player can offhand the wrench to escape the event
        if (isItem(e.getItem()) && !isItem(p.getInventory().getItemInOffHand())
            && e.getAction().toString().endsWith("_BLOCK")
        ) {
            e.setCancelled(true);
            Block block = e.getClickedBlock();
            SlimefunItem slimefunBlock = BlockStorage.check(block);

            // Check if slimefunBlock is not a machine or a cargo component
            if (slimefunBlock == null
                || (!(slimefunBlock instanceof EnergyNetComponent)
                && !slimefunBlock.getID().startsWith("CARGO_NODE")
                && !slimefunBlock.getID().equals(SlimefunItems.TRASH_CAN.getItemId()))
            ) {
                p.sendMessage(ChatColor.RED + "Hey buddy boy, this can only be electric machines, generators, capacitors, and cargo nodes!");
                return;
            }

            // Check if the config states that player needs wrench
            if (Constants.MACHINE_BREAK_REQUIRES_WRENCH) {
                double failChance = Math.random();
                if (failChance <= Constants.WRENCH_FAIL_CHANCE) {
                    dropBlock(e, p, block, slimefunBlock, true);
                } else {
                    dropBlock(e, p, block, slimefunBlock, false);
                }
            } else {
                dropBlock(e, p, block, slimefunBlock, false);
            }
            damageItem(p, e.getItem());
        }
    }

    public static void dropBlock(Event e, Player p, Block block, SlimefunItem slimefunBlock, boolean failed){
        ItemStack slimefunBlockDrop = slimefunBlock.getItem();
        BlockStorage.clearBlockInfo(block);
        block.setType(Material.AIR);

        // Special cases, capacitors should not turn into machine blocks and cargo nodes do not have input slots
        if (Arrays.asList(specialBlockIds).contains(slimefunBlock.getID())) {

            // These blocks can not fail
            block.getLocation().getWorld().dropItemNaturally(block.getLocation(), slimefunBlockDrop);

            if (slimefunBlock.getID().equals(SlimefunItems.CARGO_INPUT_NODE.getItemId()) || slimefunBlock.getID().equals(SlimefunItems.CARGO_OUTPUT_NODE_2.getItemId())) {
                for (int slot : cargoSlots) {
                    ItemStack cargoDrop = BlockStorage.getInventory(block).getItemInSlot(slot);
                    if (cargoDrop != null) {
                        block.getLocation().getWorld().dropItemNaturally(block.getLocation(), cargoDrop);
                    }
                }
            }

        // All other blocks will be EnergyNetComponents
        } else {
            if (failed) {
                block.getLocation().getWorld().dropItemNaturally(block.getLocation(), Items.MACHINE_BLOCK);
                p.sendMessage(ChatColor.RED + "Oh no! Your wrench failed!");
            } else {
                block.getLocation().getWorld().dropItemNaturally(block.getLocation(), slimefunBlockDrop);
            }

            if (BlockStorage.hasInventory(block)) {
                int[] inputSlots = ((InventoryBlock) slimefunBlock).getInputSlots();
                for (int slot : inputSlots) {
                    if (BlockStorage.getInventory(block).getItemInSlot(slot) != null) {
                        block.getLocation().getWorld().dropItemNaturally(block.getLocation(), BlockStorage.getInventory(block).getItemInSlot(slot));
                    }
                }
                int[] outputSlots = ((InventoryBlock) slimefunBlock).getOutputSlots();
                for (int slot : outputSlots) {
                    if (BlockStorage.getInventory(block).getItemInSlot(slot) != null) {
                        block.getLocation().getWorld().dropItemNaturally(block.getLocation(), BlockStorage.getInventory(block).getItemInSlot(slot));
                    }
                }
            }
        }
    }

    @Override
    public boolean isDamageable() {
        return true;
    }
}
