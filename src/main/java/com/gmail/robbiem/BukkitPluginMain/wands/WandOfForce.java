package com.gmail.robbiem.BukkitPluginMain.wands;

import java.util.List;

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

public class WandOfForce extends Wand {

	@Override
	public void use(ItemStack wandItem, Player player, World world, JavaPlugin plugin, Server server) {
		List<LivingEntity> mobs = world.getLivingEntities();
		world.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1, 1.5f);
		for (LivingEntity mob: mobs) {
			Vector mobLocation = mob.getLocation().toVector();
			Vector playerLocation = player.getLocation().toVector();
			if (mobLocation.isInSphere(playerLocation, 10) && !mobLocation.equals(playerLocation)) {
				double distance = mobLocation.clone().subtract(playerLocation).length();
				double force = 10 - 1 * distance; // Same location: 10, 10 blocks away: 0
				Vector propelVector = mobLocation.subtract(playerLocation).normalize().multiply(force);
				propelVector.setY(1);
				world.spawnParticle(Particle.SPELL_INSTANT, mob.getLocation(), 5, 1, 1, 1);
				mob.setVelocity(propelVector);
				mob.damage(4, player);
			}
		}
	}

	@Override
	public long getCooldown() {
		return 2500l; 
	}

	@Override
	public ShapedRecipe getCraftingRecipeFromResultingItem(ShapedRecipe startingRecipe) {
		return startingRecipe.shape("  g", " s ", "p  ").setIngredient('p', Material.ENDER_PEARL).setIngredient('s', Material.STICK).setIngredient('g', Material.GUNPOWDER);
	}

}
