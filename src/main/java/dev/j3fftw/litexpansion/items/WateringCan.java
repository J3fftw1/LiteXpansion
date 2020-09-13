package dev.j3fftw.litexpansion.items;

import dev.j3fftw.litexpansion.Items;
import dev.j3fftw.litexpansion.LiteXpansion;
import dev.j3fftw.litexpansion.utils.Utils;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.cscorelib2.chat.ChatColors;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.player.PlayerBucketEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Cauldron;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

import static java.lang.String.valueOf;

public class WateringCan extends SimpleSlimefunItem<ItemUseHandler> {

    private static final int USE_INDEX = 7;

    public WateringCan() {
        super(Items.LITEXPANSION, Items.WATERING_CAN, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
            Items.REFINED_IRON, null, Items.REFINED_IRON,
            Items.REFINED_IRON, null, Items.REFINED_IRON,
            null, Items.REFINED_IRON, null
        });
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            e.cancel();
            Optional<Block> bOpt = e.getClickedBlock();

            if (bOpt.isPresent()) {
                Block b = bOpt.get();
                ItemStack item = e.getItem();
                Player p = e.getPlayer();
                BlockData blockData = b.getBlockData();
                ItemMeta meta = item.getItemMeta();
                List<String> lore = meta.getLore();

                // Filling
                if (b.getType() == Material.CAULDRON) {

                    if (blockData.getAsString().contains("level=3")) {

                        item.setType(Material.WATER_BUCKET);
                        Utils.send(p, "&aYou have filled your Watering Can");
                        lore.set(7, ChatColors.color("&aUses Left: &e5"));

                    } else {
                        Utils.send(p, "&cYou can only fill a Watering Can using a full cauldron!");
                    }

                    // Growing crops
                } else {

                    int uses = Integer.parseInt(String.valueOf(lore.get(USE_INDEX).charAt(15)));

                    if (uses == 0) {
                        Utils.send(p, "&cYou need to refill your Watering Can!");
                        return;
                    }

                    if (b.getType() == Material.SUGAR_CANE) {

                        int distance = 2;
                        Block above = b.getRelative(BlockFace.UP);

                        while (above.getType() == Material.SUGAR_CANE) {

                            // Failsafe
                            if (distance >= 10) {
                                Utils.send(p, "&cThis sugar cane is too tall!");
                                return;
                            }

                            above = b.getRelative(BlockFace.UP, distance);
                            distance++;
                        }

                        if (above.getType() == Material.AIR) {

                            above.setType(Material.SUGAR_CANE);
                            uses--;

                        } else {
                            Utils.send(p, "&cThe sugar cane is obstructed!");
                        }

                    } else if (blockData instanceof Ageable) {

                        Ageable crop = (Ageable) blockData;
                        int currentAge = crop.getAge();
                        int maxAge = crop.getMaximumAge();

                        if (currentAge < maxAge) {
                            crop.setAge(currentAge + 1);
                            uses--;

                        } else {
                            Utils.send(p, "&cThis crop is already ready for harvest!");
                        }

                        b.setBlockData(blockData);
                    } else if (b.getType().toString().endsWith("SAPLING")) {

                        if (BlockStorage.check(b) != null) {
                            Utils.send(p, "&cSorry, this is a Slimefun plant!");

                        } else {

                            TreeType treeType = TreeType.TREE;
                            String parseSapling = b.getType().toString()
                                .replace("Material.", "").replace("_SAPLING", "");

                            if (!parseSapling.equals("OAK")) {
                                if (parseSapling.equals("JUNGLE")){
                                    parseSapling = "SMALL_JUNGLE";
                                }
                                treeType = TreeType.valueOf(parseSapling);
                            }

                            b.setType(Material.AIR);
                            b.getLocation().getWorld().generateTree(b.getLocation(), treeType);
                            uses--;
                        }
                    }

                    lore.set(USE_INDEX, ChatColors.color("&aUses Left: &e" + uses));

                    if (uses == 0) {
                        item.setType(Material.BUCKET);
                    }
                }

                meta.setLore(lore);
                item.setItemMeta(meta);
            }

        };
    }
}
