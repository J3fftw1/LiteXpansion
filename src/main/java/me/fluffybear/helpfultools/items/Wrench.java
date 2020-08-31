package me.fluffybear.helpfultools.items;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.fluffybear.helpfultools.Items;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;

public class Wrench extends SimpleSlimefunItem<ItemUseHandler> implements Listener {

    public Wrench() {
        super(Items.HELPFULTOOLS, Items.WRENCH, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
            new ItemStack(Material.IRON_INGOT), SlimefunItems.REINFORCED_PLATE, new ItemStack(Material.IRON_INGOT),
            SlimefunItems.REINFORCED_PLATE, new ItemStack(Material.OAK_LOG), SlimefunItems.REINFORCED_PLATE,
            null, new ItemStack(Material.OAK_LOG), null
        });
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            Player p = e.getPlayer();
            Block b 

            if (p.getFoodLevel() >= 2) {
                if (p.getInventory().getItemInMainHand().getType() != Material.SHEARS && p.getGameMode() != GameMode.CREATIVE) {
                    FoodLevelChangeEvent event = new FoodLevelChangeEvent(p, p.getFoodLevel() - 2);
                    Bukkit.getPluginManager().callEvent(event);
                    p.setFoodLevel(event.getFoodLevel());
                }

                p.setVelocity(p.getEyeLocation().getDirection().multiply(4));
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_TNT_PRIMED, 1, 1);
                p.getWorld().playEffect(p.getLocation(), Effect.SMOKE, 1);
                p.setFallDistance(0F);
            }
            else {
                SlimefunPlugin.getLocalization().sendMessage(p, "messages.hungry", true);
            }
        };
    }


}