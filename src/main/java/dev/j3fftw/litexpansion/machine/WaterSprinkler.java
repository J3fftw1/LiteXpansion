package dev.j3fftw.litexpansion.machine;

import java.util.EnumSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.inventory.ItemStack;

import dev.j3fftw.litexpansion.Items;
import dev.j3fftw.litexpansion.utils.Constants;
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.AbstractGrowthAccelerator;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.CropGrowthAccelerator;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;

/**
 * The {@link WaterSprinkler} speeds up the growth of nearby crops
 * when water is under the machine
 * Essentially a modified {@link CropGrowthAccelerator}
 *
 * @author FluffyBear
 */
public class WaterSprinkler extends AbstractGrowthAccelerator {

    private final Set<Material> crops = EnumSet.noneOf(Material.class);
    public static final int ENERGY_CONSUMPTION = 16;
    public static final int CAPACITY = 64;
    private static final int RADIUS = 2;
    private static final int PROGRESS_SLOT = 4;
    private static final CustomItem noWaterItem = new CustomItem(Material.BUCKET,
        "&cNo water found",
        "",
        "&cPlease place water under the sprinkler!"
    );
    private static final CustomItem waterFoundItem = new CustomItem(Material.WATER_BUCKET,
        "&bWater detected"
    );

    public WaterSprinkler() {
        super(Items.LITEXPANSION, Items.WATER_SPRINKER, RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[] {
                Items.REFINED_IRON, SlimefunItems.ELECTRIC_MOTOR, Items.REFINED_IRON,
                new ItemStack(Material.BUCKET), Items.MACHINE_BLOCK, new ItemStack(Material.BUCKET),
                Items.REFINED_IRON, SlimefunItems.MEDIUM_CAPACITOR, Items.REFINED_IRON
            });

        createPreset(this, Items.WATER_SPRINKER.getImmutableMeta().getDisplayName().orElse("&eWater Sprinkler"),
            blockMenuPreset -> {
                for (int i = 0; i < 9; i++)
                    blockMenuPreset.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());

                blockMenuPreset.addItem(PROGRESS_SLOT, noWaterItem);
            });

        crops.add(Material.WHEAT);
        crops.add(Material.POTATOES);
        crops.add(Material.CARROTS);
        crops.add(Material.NETHER_WART);
        crops.add(Material.BEETROOTS);
        crops.add(Material.COCOA);

        if (SlimefunPlugin.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_14)) {
            crops.add(Material.SWEET_BERRY_BUSH);
        }
    }

    public int getEnergyConsumption() {
        return ENERGY_CONSUMPTION;
    }

    @Override
    public int getCapacity() {
        return CAPACITY;
    }

    public int getRadius() {
        return RADIUS;
    }

    public int[] getInputSlots() {
        return new int[0];
    }

    public int[] getOutputSlots() {
        return new int[0];
    }

    @Override
    protected void tick(Block b) {
        BlockMenu inv = BlockStorage.getInventory(b);

        if (b.getRelative(BlockFace.DOWN).getType() == Material.WATER) {
            inv.replaceExistingItem(PROGRESS_SLOT, waterFoundItem);
        } else {
            inv.replaceExistingItem(PROGRESS_SLOT, noWaterItem);
            return;
        }

        if (getCharge(b.getLocation()) >= getEnergyConsumption()) {
            for (int x = -getRadius(); x <= getRadius(); x++) {
                for (int z = -getRadius(); z <= getRadius(); z++) {
                    Block block = b.getRelative(x, 0, z);

                    if (Constants.SPRINKLER_PARTICLES)
                    block.getWorld().spawnParticle(Particle.WATER_SPLASH, block.getLocation().add(0.5D, 0.5D, 0.5D), 4, 0.1F, 0.1F, 0.1F);

                    if (crops.contains(block.getType())) {
                        grow(block);
                    }
                }
            }
        }
    }

    private void grow(Block crop) {
        Ageable ageable = (Ageable) crop.getBlockData();

        double random = Math.random();
        if (Constants.SPRINKLER_SUCCESS_CHANCE >= random) {
            if (ageable.getAge() < ageable.getMaximumAge()) {

                ageable.setAge(ageable.getAge() + 1);
                crop.setBlockData(ageable);

                crop.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, crop.getLocation().add(0.5D, 0.5D, 0.5D), 4, 0.1F, 0.1F, 0.1F);
            }
        }
    }

}
