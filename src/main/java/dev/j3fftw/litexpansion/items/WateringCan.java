package dev.j3fftw.litexpansion.items;

import dev.j3fftw.litexpansion.Items;
import dev.j3fftw.litexpansion.LiteXpansion;
import dev.j3fftw.litexpansion.utils.Utils;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.cscorelib2.chat.ChatColors;
import org.bukkit.Color;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.RayTraceResult;

import java.util.List;

import static java.lang.String.valueOf;

public class WateringCan extends SimpleSlimefunItem<ItemUseHandler> {

    private static final int USE_INDEX = 8;
    private static final int MAX_USES = 9;
    private static final NamespacedKey usageKey = new NamespacedKey(LiteXpansion.getInstance(), "watering_can_usage");

    public WateringCan() {
        super(Items.LITEXPANSION, Items.WATERING_CAN, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
            Items.REFINED_IRON, null, Items.REFINED_IRON,
            Items.REFINED_IRON, new ItemStack(Material.BUCKET), Items.REFINED_IRON,
            null, Items.REFINED_IRON, null
        });
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            e.cancel();

            Player p = e.getPlayer();
            RayTraceResult rayResult = p.rayTraceBlocks(5d, FluidCollisionMode.SOURCE_ONLY);

            if (rayResult != null) {

                Block b = rayResult.getHitBlock();
                Location blockLocation = b.getLocation();
                ItemStack item = e.getItem();
                BlockData blockData = b.getBlockData();

                // Fill if it hits water
                if (b.getType() == Material.WATER) {
                    updateUses(p, item, 2);

                } else {

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

                            if (!updateUses(p, item, 1))
                                return;
                            above.setType(Material.SUGAR_CANE);

                        } else {
                            Utils.send(p, "&cThe sugar cane is obstructed!");
                        }

                    } else if (blockData instanceof Ageable) {

                        Ageable crop = (Ageable) blockData;
                        int currentAge = crop.getAge();
                        int maxAge = crop.getMaximumAge();

                        if (currentAge < maxAge && updateUses(p, item, 1)) {
                            crop.setAge(currentAge + 1);
                        } else {
                            Utils.send(p, "&cThis crop is already ready for harvest!");
                            return;
                        }

                        b.setBlockData(blockData);
                    } else if (Tag.SAPLINGS.isTagged(b.getType())) {

                        if (BlockStorage.check(b) != null) {
                            Utils.send(p, "&cSorry, this is a Slimefun plant!");

                        } else {

                            TreeType treeType = TreeType.TREE;
                            String parseSapling = b.getType().toString()
                                .replace("_SAPLING", "");

                            if (!parseSapling.equals("OAK")) {
                                if (parseSapling.equals("JUNGLE")){
                                    parseSapling = "SMALL_JUNGLE";
                                }
                                treeType = TreeType.valueOf(parseSapling);
                            }

                            if (!updateUses(p, item, 1))
                                return;
                            b.setType(Material.AIR);
                            blockLocation.getWorld().generateTree(blockLocation, treeType);
                        }
                    }
                }

            }
        };
    }

    public static boolean updateUses(Player p, ItemStack item, int updateType) {

        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        int usesLeft = meta.getPersistentDataContainer().getOrDefault(usageKey, PersistentDataType.INTEGER, 0);

        if (updateType == 1) {

            if (usesLeft == 0) {
                Utils.send(p, "&cYou need to refill your Watering Can!");
                return false;
            }
            p.playSound(p.getLocation(), Sound.ENTITY_DROWNED_AMBIENT_WATER, 0.5F, 1F);
            usesLeft--;

        } else if (updateType == 2){
            item.setType(Material.POTION);
            p.playSound(p.getLocation(), Sound.ENTITY_DROWNED_DEATH_WATER, 0.5F, 1F);
            Utils.send(p, "&aYou have filled your Watering Can");
            usesLeft = MAX_USES;
            // Need to get this again because material changed
            PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
            potionMeta.setColor(Color.AQUA);
            item.setItemMeta(potionMeta);
            meta = item.getItemMeta();
            meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);

        } else if (updateType == 3) {
            if (usesLeft == 0) {
                Utils.send(p, "&cYou need to refill your Watering Can!");
                return false;
            }
            usesLeft = 0;
            p.playSound(p.getLocation(), Sound.ITEM_BUCKET_EMPTY, 0.5F, 1F);;
        } else {
            p.sendMessage("Error");
        }

        lore.set(USE_INDEX, ChatColors.color("&aUses Left: &e" + usesLeft));
        meta.setLore(lore);
        meta.getPersistentDataContainer().set(usageKey, PersistentDataType.INTEGER, usesLeft);
        item.setItemMeta(meta);

        if (usesLeft == 0) {
            item.setType(Material.GLASS_BOTTLE);
        }

        return true;
    }
}
