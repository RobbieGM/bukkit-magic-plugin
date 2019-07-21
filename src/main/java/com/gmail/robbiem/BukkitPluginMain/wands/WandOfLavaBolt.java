package com.gmail.robbiem.BukkitPluginMain.wands;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public class WandOfLavaBolt extends Wand {

	@Override
	public void use(ItemStack wandItem, Player player, World world, JavaPlugin plugin, Server server) {
		List<Block> blocks = player.getLineOfSight(null, 20);
		for (int i = 0; i < blocks.size() - 1; i++) {
			if (blocks.get(i).getLocation().distanceSquared(player.getLocation()) < 2 * 2)
				continue;
			final int innerI = i; // Otherwise Java throws an error
			server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
				blocks.get(innerI).setType(Material.LAVA);
				world.spawnParticle(Particle.LAVA, blocks.get(innerI).getLocation(), 3, 0.5, 0.5, 0.5);
				server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
					blocks.get(innerI).setType(Material.AIR);
				}, 20);
			}, i);
		}
	}

	@Override
	public long getCooldown() {
		// TODO Auto-generated method stub
		return 1000l;
	}

	@Override
	public ShapedRecipe getCraftingRecipeFromResultingItem(ShapedRecipe startingRecipe) {
		// TODO Auto-generated method stub
		return startingRecipe.shape("  l", " s ", "p  ").setIngredient('l', Material.LAVA_BUCKET).setIngredient('s', Material.STICK).setIngredient('p', Material.ENDER_PEARL);
	}

}
