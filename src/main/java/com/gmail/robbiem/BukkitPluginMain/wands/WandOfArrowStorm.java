package com.gmail.robbiem.BukkitPluginMain.wands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public class WandOfArrowStorm extends Wand {

	@Override
	public void use(ItemStack wandItem, Player player, World world, JavaPlugin plugin, Server server) {
		for (int tickDelay = 0; tickDelay < 10; tickDelay += 2) {
			server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
				Location loc = player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(0.5)).toLocation(world, player.getLocation().getYaw(), player.getLocation().getPitch());
				Arrow arrow = (Arrow) world.spawnArrow(loc, loc.getDirection(), 1.5f, 10);
				arrow.setKnockbackStrength(0);
				arrow.setShooter(player);
				arrow.setPickupStatus(PickupStatus.CREATIVE_ONLY);
				if (Math.random() > 0.9)
					arrow.setFireTicks(20 * 5);
			}, tickDelay);
		}
	}

	@Override
	public long getCooldown() {
		return 900l;
	}

	@Override
	public ShapedRecipe getCraftingRecipeFromResultingItem(ShapedRecipe startingRecipe) {
		return startingRecipe.shape("  a", " s ", "p  ").setIngredient('p', Material.ENDER_PEARL).setIngredient('s', Material.STICK).setIngredient('a', Material.ARROW);
	}

	@Override
	public String getLore() {
		return "Spawns a stream of arrows in the\ndirection you're looking";
	}

}
