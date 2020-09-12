package dev.j3fftw.litexpansion.items;

import dev.j3fftw.litexpansion.Items;
import dev.j3fftw.litexpansion.LiteXpansion;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class WateringCan extends SimpleSlimefunItem<ItemUseHandler> {

    public WateringCan() {
        super(Items.LITEXPANSION, Items.WATERING_CAN, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
            Items.REFINED_IRON, Items.REFINED_IRON, Items.REFINED_IRON,
            new ItemStack(Material.SHEARS), Items.ADVANCED_CIRCUIT, new ItemStack(Material.SHEARS),
            null, Items.CARBON_PLATE, null
        });
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            Optional<Block> b = e.getClickedBlock();
            if (b.isPresent() && b.get().isLiquid()) {
                System.out.println("Its a liquid");
            }
        };
    }
}
