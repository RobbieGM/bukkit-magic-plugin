package com.gmail.robbiem.BukkitPluginMain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EnchantedSnowball implements Listener {
	
	ItemStack snowballItem = new ItemStack(Material.SNOWBALL, 1);
	List<UUID> snowballIds = new ArrayList<>();
	
	public EnchantedSnowball() {
		ItemMeta meta = snowballItem.getItemMeta();
		meta.setDisplayName("Enchanted Snowball");
		snowballItem.setItemMeta(meta);
	}
	
	public void registerRecipe(JavaPlugin plugin) {
		ItemStack item = snowballItem.clone();
		item.setAmount(8);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, "enchanted_snowball"), item);
		recipe.shape("sss", "sts", "sss").setIngredient('s', Material.SNOWBALL).setIngredient('t', Material.GHAST_TEAR);
		plugin.getServer().addRecipe(recipe);
	}
	
	@EventHandler
	public void onSnowballThrow(ProjectileLaunchEvent e) {
		if (e.getEntity() instanceof Snowball && e.getEntity().getShooter() instanceof Player) {
			Player p = (Player) e.getEntity().getShooter();
			if (p.getInventory().getItemInMainHand().hasItemMeta() && p.getInventory().getItemInMainHand().getItemMeta().hasDisplayName() && p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("Enchanted Snowball")) {
				snowballIds.add(e.getEntity().getUniqueId());
			}
		}
	}
	
	@EventHandler
	public void onSnowballHit(ProjectileHitEvent e) {
		if (e.getEntity() instanceof Snowball && snowballIds.contains(e.getEntity().getUniqueId())) {
			snowballIds.remove(e.getEntity().getUniqueId());
			if (e.getHitEntity() instanceof LivingEntity) {
				LivingEntity hit = (LivingEntity) e.getHitEntity();
				if (!hit.hasPotionEffect(PotionEffectType.JUMP)) {
					hit.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20 * 1, 128)); // Jump boost 128 = no jumping to evade slowness
					hit.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 1, 255));
				}
				if (e.getEntity().getShooter() instanceof Entity) {
					hit.damage(4, (Entity) e.getEntity().getShooter());
				} else {
					hit.damage(4);
				}
				hit.getWorld().playSound(hit.getLocation(), Sound.BLOCK_CHORUS_FLOWER_DEATH, 1, 1.5f);
				hit.getWorld().spawnParticle(Particle.SNOWBALL, hit.getLocation(), 30, 0.25, 0.25, 0.25);
			}
		}
	}
}
