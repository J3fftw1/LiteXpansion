package dev.j3fftw.litexpansion.machine;

import dev.j3fftw.litexpansion.Items;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.cscorelib2.blocks.BlockPosition;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems.ADVANCED_CIRCUIT_BOARD;
import static io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems.REINFORCED_PLATE;

public class WaterSprinkler extends AContainer implements RecipeDisplayItem {

    public static final int ENERGY_CONSUMPTION = 32;
    public static final int CAPACITY = 126;

    public WaterSprinkler() {
        super(Items.LITEXPANSION, Items.WATER_SPRINKER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
            REINFORCED_PLATE, ADVANCED_CIRCUIT_BOARD, REINFORCED_PLATE,
            ADVANCED_CIRCUIT_BOARD, Items.MACHINE_BLOCK, ADVANCED_CIRCUIT_BOARD,
            REINFORCED_PLATE, ADVANCED_CIRCUIT_BOARD, REINFORCED_PLATE
        });
    }

    @Override
    public void preRegister() {
        this.addItemHandler(new BlockTicker() {
            public void tick(Block b, SlimefunItem sf, Config data) {
                WaterSprinkler.this.tick(b);
            }

            public boolean isSynchronized() {
                return false;
            }
        });
    }

    public void tick(@Nonnull Block b) {
        if (!AContainer.progress.isEmpty()) {
            System.out.println("PROCESSING");
        }
    }

    @Override
    protected void registerDefaultRecipes() {

        registerRecipe(8, new ItemStack[] {new ItemStack(Material.WATER_BUCKET)},
            new ItemStack[] {new ItemStack(Material.BUCKET)});

    }

    @Override
    public List<ItemStack> getDisplayRecipes() {
        List<ItemStack> displayRecipes = new ArrayList<>(recipes.size() * 2);

        for (MachineRecipe recipe : recipes) {
            displayRecipes.add(recipe.getInput()[0]);
            displayRecipes.add(recipe.getOutput()[recipe.getOutput().length - 1]);
        }

        return displayRecipes;
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.WATER_BUCKET);
    }

    @Override
    public String getInventoryTitle() {
        return "&bWater Sprinkler";
    }

    @Override
    public String getMachineIdentifier() {
        return "WATER_SPRINKLER";
    }

    @Override
    public int getCapacity() {
        return CAPACITY;
    }

    @Override
    public int getEnergyConsumption() {
        return ENERGY_CONSUMPTION;
    }

    @Override
    public int getSpeed() {
        return 1;
    }
}