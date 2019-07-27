package com.gmail.robbiem.BukkitPluginMain.wands;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class WandOfMagnetism extends Wand {
	
	@Override
	public void use(ItemStack wandItem, Player player, World world, JavaPlugin plugin, Server server) {
		Location center = player.getTargetBlock(null, 40).getLocation();
		for (int i = 0; i < 150; i++) {
			makeSuctionParticle(center, 20);
		}
		world.playSound(center, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1, 1.5f);
		int taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
			for (LivingEntity entity: world.getLivingEntities()) {
				if (entity.getLocation().distance(center) > 20) continue;
				entity.setFallDistance(0);
//				if (entity.equals(player)) continue;
				Vector propelVector = center.clone().subtract(entity.getLocation()).toVector().multiply(0.2);
				propelVector.setY(propelVector.getY() + 0.1);
				entity.setVelocity(propelVector);
			}
		}, 0, 1);
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			plugin.getServer().getScheduler().cancelTask(taskId);
		}, 10);
	}
	
	void makeSuctionParticle(Location center, float radius) {
		Random r = new Random();
		double x = r.nextGaussian();
		double y = r.nextGaussian();
		double z = r.nextGaussian();
		Vector relative = new Vector(x, y, z).normalize().multiply(radius);
		Location particleLocation = center.clone().add(relative);
		Vector vel = relative.clone().multiply(-1);
		center.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, particleLocation, 0, vel.getX(), vel.getY(), vel.getZ(), 0.07);
	}

	@Override
	public long getCooldown() {
		return 2000l;
	}

	@Override
	public String getLore() {
		return "This wand pulls all players and mobs\ntowards its epicenter.";
	}

	@Override
	public ShapedRecipe getCraftingRecipeFromResultingItem(ShapedRecipe startingRecipe) {
		return startingRecipe.shape("  c", " s ", "p  ").setIngredient('p', Material.ENDER_PEARL).setIngredient('s', Material.STICK).setIngredient('c', Material.COMPASS);
	}

}
