package dev.j3fftw.litexpansion;

import dev.j3fftw.litexpansion.armor.ElectricChestplate;
import dev.j3fftw.litexpansion.items.FoodSynthesizer;
import dev.j3fftw.litexpansion.items.WateringCan;
import dev.j3fftw.litexpansion.utils.Constants;
import dev.j3fftw.litexpansion.weapons.NanoBlade;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Events implements Listener {

    @EventHandler
    public void onHunger(FoodLevelChangeEvent e) {
        Player p = (Player) e.getEntity();
        if (e.getFoodLevel() < p.getFoodLevel()) {
            checkAndConsume(p, e);
        }
    }

    @EventHandler
    public void onPlayerDamageDeal(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            ItemStack itemInHand = p.getInventory().getItemInMainHand();
            final NanoBlade nanoBlade = (NanoBlade) SlimefunItem.getByID(Items.NANO_BLADE.getItemId());
            if (nanoBlade.isItem(itemInHand)
                && itemInHand.containsEnchantment(Enchantment.getByKey(Constants.GLOW_ENCHANT))
                && nanoBlade.removeItemCharge(itemInHand, 10)
            ) {
                e.setDamage(e.getDamage() * 1.75);
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player && ((Player) e.getEntity()).getEquipment() != null) {
            Player p = (Player) e.getEntity();
            ItemStack chestplate = p.getEquipment().getChestplate();
            final ElectricChestplate electricChestplate = (ElectricChestplate)
                SlimefunItem.getByID(Items.ELECTRIC_CHESTPLATE.getItemId());
            if (chestplate != null
                && electricChestplate.isItem(chestplate)
                && electricChestplate.removeItemCharge(chestplate, (float) (e.getDamage() / 1.75))
            ) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onHungerDamage(EntityDamageEvent e) {
        if (Items.FOOD_SYNTHESIZER == null || Items.FOOD_SYNTHESIZER.getItem().isDisabled()
            || !(e.getEntity() instanceof Player)) {
            return;
        }

        if (e.getCause() == EntityDamageEvent.DamageCause.STARVATION) {
            checkAndConsume((Player) e.getEntity(), null);
        }
    }

    @EventHandler
    public void onWateringCanSplash(PlayerInteractEntityEvent e) {
        e.setCancelled(true);
        Player p = e.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();
        WateringCan wateringCan = (WateringCan) Items.WATERING_CAN.getItem();
        if (wateringCan.isItem(item)) {
            Entity target = e.getRightClicked();
            if (target instanceof Player) {
                ((Player) target).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 3, 1));
                WateringCan.updateUses(p, item, 3);
            }
        }
    }

    @EventHandler
    public void onWateringCanEmpty(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack item = e.getItem();
        WateringCan wateringCan = (WateringCan) Items.WATERING_CAN.getItem();
        if ((e.getAction() == Action.LEFT_CLICK_AIR
            || e.getAction() == Action.LEFT_CLICK_BLOCK)
            && wateringCan.isItem(item)) {
            e.setCancelled(true);
            WateringCan.updateUses(p, item, 3);
        }
    }

    public void checkAndConsume(Player p, FoodLevelChangeEvent e) {
        FoodSynthesizer foodSynth = (FoodSynthesizer) Items.FOOD_SYNTHESIZER.getItem();
        for (ItemStack item : p.getInventory().getContents()) {
            if (foodSynth.isItem(item) && foodSynth.removeItemCharge(item, 3F)) {
                p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EAT, 1.5F, 1F);
                p.setFoodLevel(20);
                p.setSaturation(5);
                if (e != null) {
                    e.setFoodLevel(20);
                }
            }
        }
    }
}
