package dev.j3fftw.litexpansion.items;

import dev.j3fftw.litexpansion.Items;
import dev.j3fftw.litexpansion.LiteXpansion;
import dev.j3fftw.litexpansion.utils.Constants;
import dev.j3fftw.litexpansion.utils.Utils;
import io.github.thebusybiscuit.slimefun4.core.attributes.DamageableItem;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.core.networks.cargo.CargoNet;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.items.cargo.TrashCan;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.InventoryBlock;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
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

    private static final ArrayList<String> specialBlockIds = new ArrayList<>(Arrays.asList( SlimefunItems.CARGO_INPUT_NODE.getItemId(), SlimefunItems.CARGO_OUTPUT_NODE.getItemId(), SlimefunItems.CARGO_OUTPUT_NODE_2.getItemId(), SlimefunItems.CARGO_CONNECTOR_NODE.getItemId(), SlimefunItems.TRASH_CAN.getItemId() ));

    public Wrench() {
        super(Items.LITEXPANSION, Items.WRENCH, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                SlimefunItems.COPPER_INGOT, null, SlimefunItems.COPPER_INGOT,
                null, SlimefunItems.COPPER_INGOT, null,
                null, SlimefunItems.COPPER_INGOT, null
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
                && !(slimefunBlock instanceof TrashCan))
            ) {
                Utils.lxPrefixMessage(p, ChatColor.RED, "Hey buddy boy, this can only be electric " +
                    "machines, generators, capacitors, and cargo nodes!" );
                return;
            }

            // Check if the config states that player needs wrench
            if (Constants.MACHINE_BREAK_REQUIRES_WRENCH) {
                double failChance = Math.random();
                dropBlock(e, p, block, slimefunBlock, failChance <= Constants.WRENCH_FAIL_CHANCE);
            } else {
                dropBlock(e, p, block, slimefunBlock, false);
            }
            damageItem(p, e.getItem());
        }
    }

    public static void dropBlock(Event e, Player p, Block block, SlimefunItem slimefunBlock, boolean failed){
        ItemStack slimefunBlockDrop = slimefunBlock.getItem().clone();
        Location blockLocation = block.getLocation();
        World blockWorld = block.getWorld();
        BlockMenu blockInventory = BlockStorage.getInventory(block);

        BlockStorage.clearBlockInfo(block);
        block.setType(Material.AIR);

        // Special cases, capacitors should not turn into machine blocks and cargo nodes do not have input slots
        if (specialBlockIds.contains(slimefunBlock.getID())) {

            // These blocks can not fail
            blockWorld.dropItemNaturally(blockLocation, slimefunBlockDrop);

            if (slimefunBlock.getID().equals(SlimefunItems.CARGO_INPUT_NODE.getItemId())
                || slimefunBlock.getID().equals(SlimefunItems.CARGO_OUTPUT_NODE_2.getItemId())
            ) {
                for (int slot : cargoSlots) {
                    ItemStack cargoDrop = blockInventory.getItemInSlot(slot);
                    if (cargoDrop != null) {
                        blockWorld.dropItemNaturally(blockLocation, cargoDrop);
                    }
                }
            }

        // All other blocks will be EnergyNetComponents
        } else {
            if (failed) {
                blockWorld.dropItemNaturally(blockLocation, Items.MACHINE_BLOCK.clone());
                Utils.lxPrefixMessage(p, ChatColor.RED, "Oh no! Your wrench failed!");
            } else {
                blockWorld.dropItemNaturally(blockLocation, slimefunBlockDrop);
            }

            if (BlockStorage.hasInventory(block)) {
                int[] inputSlots = ((InventoryBlock) slimefunBlock).getInputSlots();
                for (int slot : inputSlots) {

                    if (blockInventory.getItemInSlot(slot) != null) {
                        blockWorld.dropItemNaturally(blockLocation, blockInventory.getItemInSlot(slot));
                    }
                }
                int[] outputSlots = ((InventoryBlock) slimefunBlock).getOutputSlots();
                for (int slot : outputSlots) {
                    if (blockInventory.getItemInSlot(slot) != null) {
                        blockWorld.dropItemNaturally(blockLocation, blockInventory.getItemInSlot(slot));
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
