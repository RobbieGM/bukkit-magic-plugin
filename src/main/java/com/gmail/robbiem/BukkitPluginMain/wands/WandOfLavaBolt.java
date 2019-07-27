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
	
	List<Material> unbreakable;
	
	public WandOfLavaBolt(List<Material> unbreakable) {
		this.unbreakable = unbreakable;
	}

	@Override
	public void use(ItemStack wandItem, Player player, World world, JavaPlugin plugin, Server server) {
		List<Block> blocks = player.getLineOfSight(null, 40);
		for (int i = 0; i < blocks.size() - 1; i++) {
			if (blocks.get(i).getLocation().distanceSquared(player.getLocation()) < 2 * 2)
				continue;
			final int innerI = i; // Otherwise Java throws an error
			server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
				setBlock(blocks.get(innerI), Material.LAVA);
				if (Math.random() > 0.5) // Particles can be laggy
					world.spawnParticle(Particle.LAVA, blocks.get(innerI).getLocation(), 3, 0.5, 0.5, 0.5);
				server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
					setBlock(blocks.get(innerI), Material.AIR);
				}, 20);
			}, i);
		}
	}
	
	void setBlock(Block block, Material type) {
		if (!unbreakable.contains(type))
			block.setType(type);
	}

	@Override
	public long getCooldown() {
		// TODO Auto-generated method stub
		return 600l;
	}

	@Override
	public ShapedRecipe getCraftingRecipeFromResultingItem(ShapedRecipe startingRecipe) {
		// TODO Auto-generated method stub
		return startingRecipe.shape("  l", " s ", "p  ").setIngredient('l', Material.LAVA_BUCKET).setIngredient('s', Material.STICK).setIngredient('p', Material.ENDER_PEARL);
	}

	@Override
	public String getLore() {
		return "Creates a stream of lava in the\ndirection you look, lasting 1 second";
	}

}
