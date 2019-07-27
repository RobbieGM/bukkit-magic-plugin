package com.gmail.robbiem.BukkitPluginMain.wands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class WandOfDecay extends ParticleWand {
	
	@Override
	public void use(ItemStack wandItem, Player player, World world, JavaPlugin plugin, Server server) {
		cast(player, plugin, (location) -> {
			location.getWorld().spawnParticle(Particle.SMOKE_LARGE, location, 50, 0, 0, 0, 0.15);
		}, (entity, spellLocation) -> {
			entity.damage(1, player);
			entity.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20 * 5, 4));
		});
	}

	@Override
	public long getCooldown() {
		return 750l;
	}

	@Override
	public String getLore() {
		return "This wand will apply the withering\nstatus effect to all it touches.";
	}

	@Override
	int getRange() {
		return 40;
	}

	@Override
	float getEffectRadius() {
		return 4f;
	}

	@Override
	void spawnWandParticle(Location particleLocation) {
		particleLocation.getWorld().spawnParticle(Particle.SMOKE_LARGE, particleLocation, 0, 0, -1, 0, 0.05);
	}

	@Override
	public ShapedRecipe getCraftingRecipeFromResultingItem(ShapedRecipe startingRecipe) {
		return startingRecipe.shape("  w", " s ", "p  ").setIngredient('w', Material.WITHER_ROSE).setIngredient('s', Material.STICK).setIngredient('p', Material.ENDER_PEARL);
	}

}
