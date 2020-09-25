package dev.j3fftw.litexpansion;

import dev.j3fftw.litexpansion.machine.AdvancedSolarPanel;
import dev.j3fftw.litexpansion.machine.BasicSolarPanel;
import dev.j3fftw.litexpansion.machine.MassFabricator;
import dev.j3fftw.litexpansion.machine.RubberSynthesizer;
import dev.j3fftw.litexpansion.machine.Recycler;
import dev.j3fftw.litexpansion.utils.Constants;
import dev.j3fftw.litexpansion.utils.LoreBuilderDynamic;
import io.github.thebusybiscuit.slimefun4.core.attributes.Radioactivity;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import me.mrCookieSlime.Slimefun.cscorelib2.skull.SkullItem;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

public final class Items {

    // Category
    public static final Category LITEXPANSION = new Category(
        new NamespacedKey(LiteXpansion.getInstance(), "litexpansion"),
        new CustomItem(SkullItem.fromHash("3f87fc5cbb233743a82fb0fa51fe739487f29bcc01c9026621ecefad197f4fb1"),
            "&7LiteXpansion")
    );
    public static final SlimefunItemStack ELECTRIC_CHESTPLATE = new SlimefunItemStack(
        "ELECTRIC_CHESTPLATE",
        Material.LEATHER_CHESTPLATE, Color.TEAL,
        "&9Electric Chestplate",
        "",
        "&8\u21E8 &7Negates all the damage dealt to player.",
        "",
        "&c&o&8\u21E8 &e\u26A1 &70 / 250 J"
    );

    // Armor
    public static final SlimefunItemStack NANO_BLADE = new SlimefunItemStack(
        "NANO_BLADE",
        Material.DIAMOND_SWORD,
        "&2Nano Blade &c(Off)",
        "",
        "&fAn advanced piece of technology which can",
        "&fcut through organic tissue with ease.",
        "",
        "&fToggle: &aRight Click",
        "",
        "&8\u21E8 &7Consumes &e10J &7per hit",
        "",
        "&c&o&8\u21E8 &e\u26A1 &70 / 500 J"
    );

    // Weapon
    // Tools
    public static final SlimefunItemStack WRENCH = new SlimefunItemStack(
        "WRENCH",
        Material.GOLDEN_HOE,
        "&6Wrench"
    );
    public static final SlimefunItemStack GLASS_CUTTER = new SlimefunItemStack(
        "GLASS_CUTTER",
        Material.GHAST_TEAR,
        "&bGlass Cutter",
        "",
        "&7Cuts glass quickly",
        "",
        "&c&o&8\u21E8 &e\u26A1 &70 / 300 J"
    );
    public static final SlimefunItemStack TREETAP = new SlimefunItemStack(
        "TREETAP",
        Material.WOODEN_HOE,
        "&7Treetap"
    );
    public static final SlimefunItemStack CARGO_CONFIGURATOR = new SlimefunItemStack(
        "CARGO_CONFIGURATOR",
        Material.COMPASS,
        "&7Cargo Configurator",
        "",
        "&7> &eRight Click &7- Copy node configuration",
        "&7> &eLeft Click  &7- Apply node configuration",
        "&7> &eShift+Right Click &7- Clear node configuration"
    );
    // Items
    public static final SlimefunItemStack FOOD_SYNTHESIZER = new SlimefunItemStack(
        "FOOD_SYNTHESIZER",
        new CustomItem(SkullItem.fromHash("a11a2df7d37af40ed5ce442fd2d78cd8ebcdcdc029d2ae691a2b64395cdf"),
            "Food Synthesizer"),
        "&dFood Synthesizer",
        "",
        "&fKeeps you fed with artificial food.",
        "&fComes in the flavors!",
        "",
        "&c&o&8\u21E8 &e\u26A1 &70 / 100 J"
    );
    public static final SlimefunItemStack MAG_THOR = new SlimefunItemStack(
        "MAG_THOR",
        Material.IRON_INGOT,
        "&b&lMag-Thor",
        "",
        "&7&oAn extremely durable alloy used",
        "&7&oonly in the most advanced machines"
    );
    public static final SlimefunItemStack THORIUM = new SlimefunItemStack(
        "THORIUM",
        new CustomItem(SkullItem.fromHash("427d1a6184c62d4c4a67f862b8e19ec001abe4c7d889f23349e8dafe6d033"),
            "Thorium"),
        "&8Thorium",
        "",
        LoreBuilder.radioactive(Radioactivity.HIGH),
        LoreBuilder.HAZMAT_SUIT_REQUIRED
    );
    public static final SlimefunItemStack SCRAP = new SlimefunItemStack(
        "SCRAP",
        Material.DEAD_BUSH,
        "&8Scrap",
        "",
        "&7Can be used to create &5UU-Matter"
    );
    public static final SlimefunItemStack UU_MATTER = new SlimefunItemStack(
        "UU_MATTER",
        Material.PURPLE_DYE,
        "&5UU-Matter",
        "",
        "&7Can be used to create items or resources"
    );
    public static final SlimefunItemStack IRIDIUM = new SlimefunItemStack(
        "IRIDIUM",
        Material.WHITE_DYE,
        "&fIridium"
    );
    public static final SlimefunItemStack IRIDIUM_PLATE = new SlimefunItemStack(
        "IRIDIUM_PLATE",
        Material.PAPER,
        "&fIridium Plate",
        "",
        "&7Used to create Iridium Armor"
    );
    public static final SlimefunItemStack THORIUM_DUST = new SlimefunItemStack(
        "THORIUM_DUST",
        Material.BLACK_DYE,
        "&8Thorium Dust"
    );
    public static final SlimefunItemStack REFINED_IRON = new SlimefunItemStack(
        "REFINED_IRON",
        Material.IRON_INGOT,
        "&7Refined Iron"
    );
    public static final SlimefunItemStack MACHINE_BLOCK = new SlimefunItemStack(
        "MACHINE_BLOCK",
        Material.IRON_BLOCK,
        "&7Machine Block"
    );
    public static final SlimefunItemStack UNINSULATED_COPPER_CABLE = new SlimefunItemStack(
        "UNINSULATED_COPPER_CABLE",
        Material.STRING,
        "&7Uninsulated Copper Cable"
    );
    public static final SlimefunItemStack COPPER_CABLE = new SlimefunItemStack(
        "COPPER_CABLE",
        Material.STRING,
        "&7Copper Cable"
    );
    public static final SlimefunItemStack RUBBER = new SlimefunItemStack(
        "RUBBER",
        Material.INK_SAC,
        "&7Rubber"
    );
    public static final SlimefunItemStack ELECTRONIC_CIRCUIT = new SlimefunItemStack(
        "ELECTRONIC_CIRCUIT",
        Material.COBWEB,
        "&7Electronic Circuit"
    );
    public static final SlimefunItemStack ADVANCED_CIRCUIT = new SlimefunItemStack(
        "ADVANCED_CIRCUIT",
        Material.COBWEB,
        "&7Advanced Circuit"
    );
    ////////////////
    // CARBON CRAP
    public static final SlimefunItemStack COAL_DUST = new SlimefunItemStack(
        "COAL_DUST",
        Material.BLACK_DYE,
        "&7Coal Dust"
    );
    public static final SlimefunItemStack RAW_CARBON_FIBRE = new SlimefunItemStack(
        "RAW_CARBON_FIBRE",
        Material.BLACK_DYE,
        "&7Raw Carbon Fibre"
    );
    public static final SlimefunItemStack RAW_CARBON_MESH = new SlimefunItemStack(
        "RAW_CARBON_MESH",
        Material.BLACK_DYE,
        "&7Raw Carbon Mesh"
    );
    public static final SlimefunItemStack CARBON_PLATE = new SlimefunItemStack(
        "CARBON_PLATE",
        Material.BLACK_CARPET,
        "&7Carbon Plate"
    );
    public static final SlimefunItemStack ADVANCED_ALLOY = new SlimefunItemStack(
        "ADVANCED_ALLOY",
        Material.PAPER,
        "&7Advanced Alloy"
    );
    /////////
    public static final SlimefunItemStack ADVANCED_MACHINE_BLOCK = new SlimefunItemStack(
        "ADVANCED_MACHINE_BLOCK",
        Material.DIAMOND_BLOCK,
        "&7Advanced Machine Block"
    );
    public static final SlimefunItemStack ENERGY_CRYSTAl = new SlimefunItemStack(
        "ENERGY_CRYSTAL",
        Material.DIAMOND,
        "&7Energy Crystal"
    );
    //todo make it enchanted
    public static final SlimefunItemStack LAPOTRON_CRYSTAL = new SlimefunItemStack(
        "LAPOTRON_CRYSTAL",
        Material.DIAMOND,
        "&7Lapotron Crystal"
    );
    public static final SlimefunItemStack REINFORCED_STONE = new SlimefunItemStack(
        "REINFORCED_STONE",
        Material.STONE,
        "&7Reinforced Stone"
    );
    public static final SlimefunItemStack REINFORCED_DOOR = new SlimefunItemStack(
        "REINFORCED_DOOR",
        Material.IRON_DOOR,
        "&7Reinforced Door"
    );
    public static final SlimefunItemStack REINFORCED_GLASS = new SlimefunItemStack(
        "REINFORCED_GLASS",
        Material.GRAY_STAINED_GLASS,
        "&7Reinforced Glass"
    );
    public static final SlimefunItemStack MIXED_METAL_INGOT = new SlimefunItemStack(
        "MIXED_METAL_INGOT",
        Material.IRON_INGOT,
        "&7Mixed Metal Ingot"
    );
    // Machines
    public static final SlimefunItemStack RECYCLER = new SlimefunItemStack(
        "SCRAP_MACHINE",
        Material.BLACK_CONCRETE,
        "&8Recycler",
        "",
        "&fProduces &8Scrap &ffrom anything",
        "",
        LoreBuilderDynamic.powerBuffer(Recycler.CAPACITY),
        LoreBuilderDynamic.powerPerTick(Recycler.ENERGY_CONSUMPTION)
    );
    public static final SlimefunItemStack MASS_FABRICATOR_MACHINE = new SlimefunItemStack(
        "MASS_FABRICATOR_MACHINE",
        Material.PURPLE_CONCRETE,
        "&5Mass Fabricator",
        "",
        "&fConverts &8Scrap &fto &5UU-Matter",
        "",
        LoreBuilderDynamic.powerBuffer(MassFabricator.CAPACITY),
        LoreBuilderDynamic.powerPerTick(MassFabricator.ENERGY_CONSUMPTION)
    );
    public static final SlimefunItemStack RUBBER_SYNTHESIZER_MACHINE = new SlimefunItemStack(
        "RUBBER_SYNTHESIZER_MACHINE",
        Material.ORANGE_CONCRETE,
        "&6Rubber Synthesizer",
        "",
        "&fConverts Bucket of Oil to &7Rubber",
        "",
        LoreBuilderDynamic.powerBuffer(RubberSynthesizer.CAPACITY),
        LoreBuilderDynamic.powerPerTick(RubberSynthesizer.ENERGY_CONSUMPTION)
    );
    //// Solar panels
    public static final SlimefunItemStack BASIC_SOLAR_PANEL = new SlimefunItemStack(
        "BASIC_SOLAR_PANEL",
        Material.BLUE_GLAZED_TERRACOTTA,
        "&3&lBasic Solar Panel",
        "&6Only works during the day",
        "",
        LoreBuilderDynamic.powerBuffer(BasicSolarPanel.BASIC_STORAGE),
        LoreBuilderDynamic.powerPerTick(BasicSolarPanel.BASIC_DAY_RATE) + " (Day)"
    );
    public static final SlimefunItemStack LOW_VOLTAGE_SOLAR_PANEL = new SlimefunItemStack(
        "LOW_VOLTAGE_SOLAR_PANEL",
        Material.RED_GLAZED_TERRACOTTA,
        "&c&lLow Voltage Solar Panel",
        "&6Only works during the day",
        "",
        LoreBuilderDynamic.powerBuffer(BasicSolarPanel.LOW_VOLTAGE_STORAGE),
        LoreBuilderDynamic.powerPerTick(BasicSolarPanel.LOW_VOLTAGE_DAY_RATE) + " (Day)"
    );
    public static final SlimefunItemStack MEDIUM_VOLTAGE_SOLAR_PANEL = new SlimefunItemStack(
        "MEDIUM_VOLTAGE_SOLAR_PANEL",
        Material.ORANGE_GLAZED_TERRACOTTA,
        "&6&lMedium Voltage Solar Panel",
        "&6Only works during the day",
        "",
        LoreBuilderDynamic.powerBuffer(BasicSolarPanel.MEDIUM_VOLTAGE_STORAGE),
        LoreBuilderDynamic.powerPerTick(BasicSolarPanel.MEDIUM_VOLTAGE_DAY_RATE) + " (Day)"
    );
    public static final SlimefunItemStack HIGH_VOLTAGE_SOLAR_PANEL = new SlimefunItemStack(
        "HIGH_VOLTAGE_SOLAR_PANEL",
        Material.YELLOW_GLAZED_TERRACOTTA,
        "&e&lHigh Voltage Solar Panel",
        "&6Only works during the day",
        "",
        LoreBuilderDynamic.powerBuffer(BasicSolarPanel.HIGH_VOLTAGE_STORAGE),
        LoreBuilderDynamic.powerPerTick(BasicSolarPanel.HIGH_VOLTAGE_DAY_RATE) + " (Day)"
    );
    public static final SlimefunItemStack ADVANCED_SOLAR_PANEL = new SlimefunItemStack(
        "ADVANCED_SOLAR_PANEL",
        Material.BLACK_GLAZED_TERRACOTTA,
        "&7&lAdvanced Solar Panel",
        "&9Works at Night",
        "",
        LoreBuilderDynamic.powerBuffer(AdvancedSolarPanel.ADVANCED_STORAGE),
        LoreBuilderDynamic.powerPerTick(AdvancedSolarPanel.ADVANCED_DAY_RATE) + " (Day)",
        LoreBuilderDynamic.powerPerTick(AdvancedSolarPanel.ADVANCED_NIGHT_RATE) + " (Night)"
    );
    public static final SlimefunItemStack HYBRID_SOLAR_PANEL = new SlimefunItemStack(
        "HYBRID_SOLAR_PANEL",
        Material.GRAY_GLAZED_TERRACOTTA,
        "&b&lHybrid Solar Panel",
        "&9Works at Night",
        "",
        LoreBuilderDynamic.powerBuffer(AdvancedSolarPanel.HYBRID_STORAGE),
        LoreBuilderDynamic.powerPerTick(AdvancedSolarPanel.HYBRID_DAY_RATE) + " (Day + Nether)",
        LoreBuilderDynamic.powerPerTick(AdvancedSolarPanel.HYBRID_NIGHT_RATE) + " (Night + End)"
    );
    public static final SlimefunItemStack ULTIMATE_SOLAR_PANEL = new SlimefunItemStack(
        "ULTIMATE_SOLAR_PANEL",
        Material.PURPLE_GLAZED_TERRACOTTA,
        "&5&lUltimate Solar Panel",
        "&9Works at Night",
        "",
        LoreBuilderDynamic.powerBuffer(AdvancedSolarPanel.ULTIMATE_STORAGE),
        LoreBuilderDynamic.powerPerTick(AdvancedSolarPanel.ULTIMATE_DAY_RATE) + " (Day)",
        LoreBuilderDynamic.powerPerTick(AdvancedSolarPanel.ULTIMATE_NIGHT_RATE) + " (Night)"
    );
    //Basic Machines
    public static final SlimefunItemStack REFINED_SMELTERY = new SlimefunItemStack(
        "REFINED_SMELTERY",
        Material.BLAST_FURNACE,
        "&7Refined Smeltery"
    );

    public static final SlimefunItemStack METAL_FORGE = new SlimefunItemStack(
        "METAL_FORGE",
        Material.DISPENSER,
        "&7Metal Forge"
    );

    private static final Enchantment glowEnchant = Enchantment.getByKey(Constants.GLOW_ENCHANT);

    static {
        ADVANCED_CIRCUIT.addEnchantment(glowEnchant, 1);
        GLASS_CUTTER.addEnchantment(glowEnchant, 1);
    }

    private Items() {}
}
