package com.gmail.robbiem.BukkitPluginMain.wands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class WandOfPoison extends ParticleWand {
	
	@Override
	public void use(ItemStack item, Player player, World world, JavaPlugin plugin, Server server) {
		cast(player, plugin, (location) -> {
			location.getWorld().spawnParticle(Particle.SNEEZE, location, 100, 0.5, 0.5, 0.5, 0.2);
		}, (entity, spellLocation) -> {
			entity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20 * 5, 1));
		});
	}

	@Override
	public long getCooldown() {
		return 500l;
	}

	@Override
	public ShapedRecipe getCraftingRecipeFromResultingItem(ShapedRecipe startingRecipe) {
		return startingRecipe.shape("  e", " s ", "p  ").setIngredient('e', Material.SPIDER_EYE).setIngredient('s', Material.STICK).setIngredient('p', Material.ENDER_PEARL);
	}

	@Override
	public String getLore() {
		return "Poisons players that it hits\nfor 5 seconds";
	}

	@Override
	int getRange() {
		return 40;
	}

	@Override
	float getEffectRadius() {
		return 3.5f;
	}

	@Override
	void spawnWandParticle(Location particleLocation) {
		particleLocation.getWorld().spawnParticle(Particle.SNEEZE, particleLocation, 5, 0, 0, 0, 0.05);
	}
	
}
