package me.fluffybear.helpfultools;

import me.fluffybear.helpfultools.items.Wrench;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.UnplaceableBlock;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

final class ItemSetup {

    protected static final ItemSetup INSTANCE = new ItemSetup();
    private final ItemStack glass = new ItemStack(Material.GLASS);
    private boolean initialised;

    private ItemSetup() {}

    public void init() {
        if (initialised) return;

        initialised = true;

        registerTools();
        registerMachines();
        registerMiscItems();
        registerEndgameItems();
        registerSolarPanels();
    }

    private void registerTools() {
        new Wrench().register(HelpfulTools.getInstance());
    }

    private void registerMachines() {
    }

    private void registerMiscItems() {
    }

    private void registerEndgameItems() {
    }

    private void registerSolarPanels() {
    }

    ////////////////////////
    private void registerItem(@Nonnull SlimefunItemStack result, @Nonnull RecipeType type,
                              @Nonnull ItemStack... items) {
        ItemStack[] recipe;
        if (items.length == 1) {
            recipe = new ItemStack[] {
                null, null, null,
                null, items[0], null,
                null, null, null
            };
            new SlimefunItem(Items.HELPFULTOOLS, result, type, recipe).register(HelpfulTools.getInstance());

            // make shapeless
            for (int i = 0; i < 9; i++) {
                if (i == 4) continue;
                final ItemStack[] recipe2 = new ItemStack[9];
                recipe2[i] = items[0];
                type.register(recipe2, result);
            }

            return;
        }

        if (items.length < 9) {
            recipe = new ItemStack[9];
            System.arraycopy(items, 0, recipe, 0, items.length);
        } else
            recipe = items;

        new SlimefunItem(Items.HELPFULTOOLS, result, type, recipe).register(HelpfulTools.getInstance());
    }

    private void registerNonPlaceableItem(@Nonnull SlimefunItemStack result, @Nonnull RecipeType type,
                                          @Nonnull ItemStack... items) {
        ItemStack[] recipe;
        if (items.length == 1) {
            recipe = new ItemStack[] {
                null, null, null,
                null, items[0], null,
                null, null, null
            };
            new UnplaceableBlock(Items.HELPFULTOOLS, result, type, recipe).register(HelpfulTools.getInstance());

            // make shapeless
            for (int i = 0; i < 9; i++) {
                if (i == 4) continue;
                final ItemStack[] recipe2 = new ItemStack[9];
                recipe2[i] = items[0];
                type.register(recipe2, result);
            }

            return;
        }

        if (items.length < 9) {
            recipe = new ItemStack[9];
            System.arraycopy(items, 0, recipe, 0, items.length);
        } else
            recipe = items;

        new UnplaceableBlock(Items.HELPFULTOOLS, result, type, recipe).register(HelpfulTools.getInstance());
    }

    // Haha shapeless recipe bitches!!!! <3 <3 <3
    // DEAL WITH IT KIDDOS HAHAHAHHAHAHAHAHAH
    private void registerRecipe(@Nonnull SlimefunItemStack result, @Nonnull SlimefunItemStack item) {
        for (int i = 0; i < 9; i++) {
            final ItemStack[] recipe = new ItemStack[9];
            recipe[i] = item;
            RecipeType.ENHANCED_CRAFTING_TABLE.register(recipe, result);
        }
    }
}
