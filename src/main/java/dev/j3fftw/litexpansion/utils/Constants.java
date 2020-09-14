package dev.j3fftw.litexpansion.utils;

import dev.j3fftw.litexpansion.LiteXpansion;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import org.bukkit.NamespacedKey;

public final class Constants {

    public static final int SERVER_TICK_RATE = 20;

    public static final int CUSTOM_TICKER_DELAY = SlimefunPlugin.getCfg().getInt("URID.custom-ticker-delay");

    public static final double SUGAR_CANE_WATERING_SUCCESS_CHANCE = LiteXpansion.getCfg().getDouble("options.sugar-cane-watering-success-chance");

    public static final double CROP_WATERING_SUCCESS_CHANCE = LiteXpansion.getCfg().getDouble("options.crop-watering-success-chance");

    public static final double TREE_WATERING_SUCCESS_CHANCE = LiteXpansion.getCfg().getDouble("options.tree-watering-success-chance");

    public static final NamespacedKey GLOW_ENCHANT = new NamespacedKey(LiteXpansion.getInstance(),
        "glow_enchant");

    private Constants() {}

}
