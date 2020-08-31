package me.fluffybear.helpfultools;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.core.researching.Research;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.bstats.bukkit.Metrics;
import me.mrCookieSlime.Slimefun.cscorelib2.config.Config;
import me.mrCookieSlime.Slimefun.cscorelib2.updater.GitHubBuildsUpdater;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class HelpfulTools extends JavaPlugin implements SlimefunAddon {

    private static HelpfulTools instance;

    @Override
    public void onEnable() {
        instance = this;

        if (!new File(getDataFolder(), "config.yml").exists())
            saveDefaultConfig();

        final Metrics metrics = new Metrics(this, 7111);
        setupCustomMetrics(metrics);

        if (getConfig().getBoolean("options.auto-update") && getDescription().getVersion().startsWith("DEV - ")) {
            new GitHubBuildsUpdater(this, getFile(), "J3fftw1/LiteXpansion/master").start();
        }

        getServer().getPluginManager().registerEvents(new Events(), this);

        // Category
        Items.HELPFULTOOLS.register();

        ItemSetup.INSTANCE.init();

        setupResearches();
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    private void setupResearches() {
        new Research(new NamespacedKey(this, "machine_removal"),
            37210, "Machine Removal", 10)
            .addItems(Items.WRENCH)
            .register();
    }

    private void setupCustomMetrics(@Nonnull Metrics metrics) {
        metrics.addCustomChart(new Metrics.AdvancedPie("blocks_placed", () -> {
            final Map<String, Integer> data = new HashMap<>();
            try {
                Class<?> blockStorage = Class.forName("me.mrCookieSlime.Slimefun.api.BlockStorage");

                for (World world : Bukkit.getWorlds()) {
                    final BlockStorage storage = BlockStorage.getStorage(world);
                    if (storage == null) continue;

                    final Field f = blockStorage.getDeclaredField("storage");
                    f.setAccessible(true);
                    @SuppressWarnings("unchecked") final Map<Location, Config> blocks =
                        (Map<Location, Config>) f.get(storage);

                    for (Map.Entry<Location, Config> entry : blocks.entrySet()) {
                        final SlimefunItem item = SlimefunItem.getByID(entry.getValue().getString("id"));
                        if (item == null || !(item.getAddon() instanceof HelpfulTools)) continue;

                        data.merge(item.getID(), 1, Integer::sum);
                    }
                }
            } catch (ReflectiveOperationException e) {
                getLogger().log(Level.WARNING, "Failed to load placed blocks", e);
            }
            return data;
        }));
    }

    public JavaPlugin getJavaPlugin() {
        return this;
    }

    public String getBugTrackerURL() {
        return "https://github.com/J3fftw1/LiteXpansion/issues";
    }

    public static HelpfulTools getInstance() {
        return instance;
    }
}
