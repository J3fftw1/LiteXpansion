package me.fluffybear.helpfultools;

import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import me.mrCookieSlime.Slimefun.cscorelib2.skull.SkullItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public final class Items {

    // Category
    public static final Category HELPFULTOOLS = new Category(
        new NamespacedKey(HelpfulTools.getInstance(), "helpfultools"),
        new CustomItem(new ItemStack(Material.IRON_PICKAXE),
            "&4Helpful Tools")
    );

    // Tools
    public static final SlimefunItemStack WRENCH = new SlimefunItemStack(
        "WRENCH",
        Material.IRON_HOE,
        "&f&lWrench"
    );

    private Items() {}
}
