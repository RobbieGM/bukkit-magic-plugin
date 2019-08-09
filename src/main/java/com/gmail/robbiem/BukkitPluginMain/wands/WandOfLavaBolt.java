package com.gmail.robbiem.BukkitPluginMain.wands;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import com.gmail.robbiem.BukkitPluginMain.Main;
import com.gmail.robbiem.BukkitPluginMain.ModdedItemManager;

public class WandOfLavaBolt extends Wand {

	public WandOfLavaBolt(Main plugin) {
		super(plugin);
	}

	@Override
	public boolean use(ItemStack wandItem, Player player, World world, Server server) {
		int maxDist = (int) player.getTargetBlock(null, 20).getLocation().distance(player.getEyeLocation());
		List<Block> blocks = player.getLineOfSight(null, maxDist);
		for (int i = 0; i < blocks.size() - 1; i++) {
			if (blocks.get(i).getLocation().distanceSquared(player.getLocation()) < 2 * 2)
				continue;
			final int innerI = i; // Otherwise Java throws an error
			server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
				setBlock(blocks.get(innerI), Material.LAVA);
				if (Math.random() > 0.8) // Particles can be laggy
					world.spawnParticle(Particle.LAVA, blocks.get(innerI).getLocation(), 2, 0.5, 0.5, 0.5);
				server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
					setBlock(blocks.get(innerI), Material.AIR);
				}, 5);
			}, (int) (i * 0.5));
		}
		return true;
	}
	
	void setBlock(Block block, Material type) {
		if (!ModdedItemManager.UNBREAKABLE_AND_SHULKERS.contains(type))
			block.setType(type);
	}

	@Override
	public long getPlayerCooldown() {
		// TODO Auto-generated method stub
		return 2000l;
	}

	@Override
	public String getLore() {
		return "Creates a stream of lava in the\ndirection you look, lasting 1 second";
	}

	@Override
	public Material getWandTip() {
		return Material.LAVA_BUCKET;
	}

	@Override
	public String getName() {
		return "Wand of Lava Bolt";
	}

}
