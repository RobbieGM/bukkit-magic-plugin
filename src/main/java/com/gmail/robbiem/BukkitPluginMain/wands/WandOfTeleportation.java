package com.gmail.robbiem.BukkitPluginMain.wands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class WandOfTeleportation extends Wand {
	static final int TELEPORTER_STICK_RANGE = 100;
	
	@Override
	public void use(ItemStack wandItem, Player player, World world, JavaPlugin plugin, Server server) {
		Location previousLocation = player.getLocation();
		Location targetedLocation = player.getTargetBlock(null, TELEPORTER_STICK_RANGE).getLocation();
		Location highestBlockLocation = world.getHighestBlockAt(targetedLocation).getLocation();
		if (targetedLocation.getY() > highestBlockLocation.getY()) { // Avoid teleporting into air, but allow teleporting under trees.
			targetedLocation.setY(highestBlockLocation.getY());
		}
		targetedLocation.add(new Vector(0, 1, 0));
		targetedLocation.setDirection(previousLocation.getDirection());
		world.spawnParticle(Particle.PORTAL, previousLocation, 30, 1, 1, 1);
		Vector previousLocationVector = previousLocation.toVector();
		Vector locationDifference = targetedLocation.toVector().subtract(previousLocationVector);
		int vectorMag = (int) locationDifference.length();
		server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			for (int i = 1; i <= vectorMag; i++) {
				world.spawnParticle(Particle.PORTAL, previousLocationVector.clone().add(locationDifference.clone().multiply((double) i / vectorMag)).toLocation(world), 5, 0.5, 0.5, 0.5);
			}
		}, 1);
		player.teleport(targetedLocation);
		world.playSound(targetedLocation, Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1, 1);
	}
	
	public long getCooldown() {
		return 5000l;
	}

	@Override
	public ShapedRecipe getCraftingRecipeFromResultingItem(ShapedRecipe startingRecipe) {
		return startingRecipe.shape("  p", " s ", "p  ").setIngredient('p', Material.ENDER_PEARL).setIngredient('s', Material.STICK);
	}
}
