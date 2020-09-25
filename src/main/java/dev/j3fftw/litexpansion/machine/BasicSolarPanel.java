package dev.j3fftw.litexpansion.machine;

import dev.j3fftw.litexpansion.Items;
import dev.j3fftw.litexpansion.utils.Utils;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetProvider;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.InventoryBlock;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BasicSolarPanel extends SlimefunItem implements InventoryBlock, EnergyNetProvider {

    private static final int PROGRESS_SLOT = 4;
    private static final CustomItem generatingItem = new CustomItem(Material.ORANGE_STAINED_GLASS_PANE,
        "&cNot Generating..."
    );
    public static int BASIC_DAY_RATE = 10;
    public static int BASIC_STORAGE = 10_000;
    public static int LOW_VOLTAGE_DAY_RATE = 100;
    public static int LOW_VOLTAGE_STORAGE = 25_000;
    public static int MEDIUM_VOLTAGE_DAY_RATE = 200;
    public static int MEDIUM_VOLTAGE_STORAGE = 50_000;
    public static int HIGH_VOLTAGE_DAY_RATE = 400;
    public static int HIGH_VOLTAGE_STORAGE = 100_000;
    private final Basic type;

    public BasicSolarPanel(Basic type) {
        super(Items.LITEXPANSION, type.getItem(), RecipeType.ENHANCED_CRAFTING_TABLE, type.getRecipe());
        this.type = type;

        createPreset(this, type.getItem().getImmutableMeta().getDisplayName().orElse("&7Solar Panel"),
            blockMenuPreset -> {
                for (int i = 0; i < 9; i++)
                    blockMenuPreset.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());

                blockMenuPreset.addItem(PROGRESS_SLOT, generatingItem);
            });
    }

    @Override
    public int getGeneratedOutput(@Nonnull Location l, @Nonnull Config data) {
        @Nullable final BlockMenu inv = BlockStorage.getInventory(l);
        if (inv == null) return 0;

        final int stored = getCharge(l);
        final boolean canGenerate = stored < getCapacity();
        final int rate = canGenerate ? getGeneratingAmount(inv.getBlock(), l.getWorld()) : 0;

        String generationType = "&4Unknown";

        if (l.getWorld().getEnvironment() == World.Environment.NETHER) {
            generationType = "&cNether &e(Day)";
        } else if (rate == this.type.getDayGenerationRate()) {
            generationType = "&aOverworld &e(Day)";
        } else {
            generationType = "&aNot Generating";
        }

        if (inv.toInventory() != null && !inv.toInventory().getViewers().isEmpty()) {
            inv.replaceExistingItem(PROGRESS_SLOT,
                canGenerate ? new CustomItem(Material.GREEN_STAINED_GLASS_PANE, "&aGenerating",
                    "", "&bRate: " + generationType,
                    "&7Generating at &6" + Utils.powerFormatAndFadeDecimals(Utils.perTickToPerSecond(rate)) + " J/s " +
                        "&8(" + rate + " J/t)",
                    "", "&7Stored: &6" + Utils.powerFormatAndFadeDecimals(stored + rate) + " J"
                )
                    : new CustomItem(Material.ORANGE_STAINED_GLASS_PANE, "&cNot Generating",
                    "", "&7Generator has reached maximum capacity.",
                    "", "&7Stored: &6" + Utils.powerFormatAndFadeDecimals(stored) + " J")
            );
        }

        return rate;
    }

    @Override
    public boolean willExplode(@Nonnull Location l, @Nonnull Config data) {
        return false;
    }

    private int getGeneratingAmount(@Nonnull Block b, @Nonnull World world) {
        if (world.getEnvironment() == World.Environment.NETHER) return this.type.getDayGenerationRate();
        if (world.getEnvironment() == World.Environment.THE_END) return 0;

        // Note: You need to get the block above for the light check, the block itself is always 0
        if (world.isThundering() || world.hasStorm() || world.getTime() >= 13000
            || b.getLocation().add(0, 1, 0).getBlock().getLightFromSky() != 15
        ) {
            return 0;
        } else {
            return this.type.getDayGenerationRate();
        }
    }

    @Nonnull
    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.GENERATOR;
    }

    @Override
    public int getCapacity() {
        return this.type.getStorage();
    }

    @Override
    public int[] getInputSlots() {
        return new int[0];
    }

    @Override
    public int[] getOutputSlots() {
        return new int[0];
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public enum Basic {

        BASIC(Items.BASIC_SOLAR_PANEL, BASIC_DAY_RATE, BASIC_STORAGE, new ItemStack[] {
                Items.CARBON_PLATE, Items.CARBON_PLATE, Items.CARBON_PLATE,
                Items.REINFORCED_GLASS, Items.REINFORCED_GLASS, Items.REINFORCED_GLASS,
                Items.ELECTRONIC_CIRCUIT, Items.MACHINE_BLOCK, Items.ELECTRONIC_CIRCUIT
        }),
        LOW_VOLTAGE(Items.LOW_VOLTAGE_SOLAR_PANEL, LOW_VOLTAGE_DAY_RATE, LOW_VOLTAGE_STORAGE, new ItemStack[] {
                Items.REINFORCED_GLASS, Items.REINFORCED_GLASS, Items.REINFORCED_GLASS,
                Items.ADVANCED_ALLOY, Items.BASIC_SOLAR_PANEL, Items.ADVANCED_ALLOY,
                Items.ADVANCED_CIRCUIT, Items.ADVANCED_MACHINE_BLOCK, Items.ADVANCED_CIRCUIT
        }),
        MEDIUM_VOLTAGE(Items.MEDIUM_VOLTAGE_SOLAR_PANEL, MEDIUM_VOLTAGE_DAY_RATE, MEDIUM_VOLTAGE_STORAGE, new ItemStack[] {
                Items.REINFORCED_GLASS, Items.REINFORCED_GLASS, Items.REINFORCED_GLASS,
                Items.ADVANCED_ALLOY, Items.LOW_VOLTAGE_SOLAR_PANEL, Items.ADVANCED_ALLOY,
                Items.ADVANCED_CIRCUIT, Items.ADVANCED_MACHINE_BLOCK, Items.ADVANCED_CIRCUIT
        }),
        HIGH_VOLTAGE(Items.HIGH_VOLTAGE_SOLAR_PANEL, HIGH_VOLTAGE_DAY_RATE, HIGH_VOLTAGE_STORAGE, new ItemStack[] {
                Items.REINFORCED_GLASS, Items.REINFORCED_GLASS, Items.REINFORCED_GLASS,
                SlimefunItems.REINFORCED_PLATE, Items.MEDIUM_VOLTAGE_SOLAR_PANEL, SlimefunItems.REINFORCED_PLATE,
                Items.ADVANCED_CIRCUIT, Items.ADVANCED_MACHINE_BLOCK, Items.ADVANCED_CIRCUIT
        });

        @Nonnull
        private final SlimefunItemStack item;
        private final int dayGenerationRate;
        private final int storage;

        @Nonnull
        private final ItemStack[] recipe;
    }
}
