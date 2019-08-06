package com.gmail.robbiem.BukkitPluginMain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class ExplosiveBow implements Listener {
	JavaPlugin plugin;
	
	public ExplosiveBow(JavaPlugin p) {
		plugin = p;
	}
	
	ArrayList<UUID> explosiveArrowIDs = new ArrayList<UUID>();
	
	@EventHandler
	public void onArrowShoot(EntityShootBowEvent e) {
		if (Arrays.asList("Explosive Bow", "Explosive Crossbow").contains(e.getBow().getItemMeta().getDisplayName())) {
			explosiveArrowIDs.add(e.getProjectile().getUniqueId());
			LivingEntity entity = e.getEntity();
			if (entity instanceof Player)
				plugin.getServer().getPluginManager().callEvent(new ModdedWeaponUsedEvent((Player) e.getEntity()));
		}
	}
	
	@EventHandler
	public void onArrowLand(ProjectileHitEvent e) {
		if ((e.getEntityType() == EntityType.ARROW || e.getEntityType() == EntityType.SPECTRAL_ARROW) && explosiveArrowIDs.contains(e.getEntity().getUniqueId())) {
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
				TNTPrimed tnt = (TNTPrimed) e.getEntity().getWorld().spawnEntity(e.getEntity().getLocation(), EntityType.PRIMED_TNT);
				tnt.setFuseTicks(0);
				tnt.setYield(1.5f);
				explosiveArrowIDs.remove(e.getEntity().getUniqueId());
				e.getEntity().remove();
			}, (int) (20 * 0.5));
		}
	}
	
	public static void registerCraftingRecipe(JavaPlugin plugin) {
		ItemStack explosiveBow = new ItemStack(Material.BOW, 1);
		ItemMeta bowMeta = explosiveBow.getItemMeta();
		bowMeta.setDisplayName("Explosive Bow");
		explosiveBow.setItemMeta(bowMeta);
		ShapelessRecipe bowRecipe = new ShapelessRecipe(new NamespacedKey(plugin, "explosive_bow"), explosiveBow);
		bowRecipe.addIngredient(Material.BOW);
		bowRecipe.addIngredient(Material.TNT);
		plugin.getServer().addRecipe(bowRecipe);
		
		ItemStack explosiveCrossbow = new ItemStack(Material.CROSSBOW, 1);
		ItemMeta crossbowMeta = explosiveBow.getItemMeta();
		crossbowMeta.setDisplayName("Explosive Crossbow");
		explosiveCrossbow.setItemMeta(crossbowMeta);
		ShapelessRecipe crossbowRecipe = new ShapelessRecipe(new NamespacedKey(plugin, "explosive_crossbow"), explosiveCrossbow);
		crossbowRecipe.addIngredient(Material.CROSSBOW);
		crossbowRecipe.addIngredient(Material.TNT);
		plugin.getServer().addRecipe(crossbowRecipe);
	}
}
