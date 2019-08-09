package com.gmail.robbiem.BukkitPluginMain.wands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.gmail.robbiem.BukkitPluginMain.Main;
import com.gmail.robbiem.BukkitPluginMain.ModdedItemManager;

public class WandOfDestruction extends Wand {
	
	static final int RADIUS = 2;

	public WandOfDestruction(Main plugin) {
		super(plugin);
	}

	@Override
	public Material getWandTip() {
		return Material.IRON_PICKAXE;
	}

	@Override
	public boolean use(ItemStack item, Player player, World world, Server server) {
		Block targeted = player.getTargetBlockExact(40, FluidCollisionMode.ALWAYS);
		if (targeted == null) return false;
		List<Block> erased = new ArrayList<>();
		for (int x = -RADIUS; x <= RADIUS; x++) {
			for (int y = -RADIUS; y <= RADIUS; y++) {
				for (int z = -RADIUS; z <= RADIUS; z++) {
					if (new Vector(x, y, z).lengthSquared() <= RADIUS * RADIUS) {
						erased.add(targeted.getLocation().add(x, y, z).getBlock());
					}
				}
			}
		}
		for (Block b: erased) {
			int delay = (int) (Math.random() * 25);
//			int delay = 4 * (int) (RADIUS - b.getLocation().subtract(targeted.getLocation()).length());
			server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
				setBlock(b, Material.AIR);
//				if (setBlock(b, Material.AIR)) {
//					FallingBlock falling = world.spawnFallingBlock(b.getLocation().add(0.5, 0.5, 0.5), server.createBlockData(b.getType()));
//					Vector outFromCenter = falling.getLocation().subtract(targeted.getLocation()).toVector().multiply(0.4);
//					falling.setVelocity(outFromCenter);
//				}
			}, delay);
		}
		return true;
	}
	
	boolean setBlock(Block block, Material type) {
		boolean unbreakable = ModdedItemManager.UNBREAKABLE_AND_SHULKERS.contains(block.getType());
		if (!unbreakable) {
			block.setType(type);
			return true;
		}
		return false;
	}

	@Override
	public long getPlayerCooldown() {
		return 0;
	}
	
	@Override
	public long getItemCooldown() {
		return 400l;
	}

	@Override
	public String getName() {
		return "Wand of Destruction";
	}

	@Override
	public String getLore() {
		return "This wand takes out\nblocks at a rapid rate.";
	}

}
