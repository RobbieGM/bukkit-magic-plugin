package com.gmail.robbiem.BukkitPluginMain.scrolls;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ScrollOfProtection extends Scroll {
	
	static final int HEIGHT = 50;
	static final int OFFSET_Y = -5;
	static final int RADIUS = 10;
	static final int DURATION = 20 * 60;
	
	List<Material> unbreakable;
	
	public ScrollOfProtection(List<Material> unbreakable) {
		this.unbreakable = unbreakable;
	}

	@Override
	public void use(ItemStack wandItem, Player player, World world, JavaPlugin plugin, Server server) {
		Block l = player.getLocation().getBlock();
		List<Block> changedBlocks = new ArrayList<>();
		
		// +x
		for (int y = OFFSET_Y; y <= HEIGHT + OFFSET_Y; y++) {
			for (int z = -RADIUS; z <= RADIUS; z++) {
				changedBlocks.add(world.getBlockAt(l.getX() + RADIUS, l.getY() + y, l.getZ() + z));
			}
		}
		// -x
		for (int y = OFFSET_Y; y <= HEIGHT + OFFSET_Y; y++) {
			for (int z = -RADIUS; z <= RADIUS; z++) {
				changedBlocks.add(world.getBlockAt(l.getX() - RADIUS, l.getY() + y, l.getZ() + z));
			}
		}
		// +z
		for (int y = OFFSET_Y; y <= HEIGHT + OFFSET_Y; y++) {
			for (int x = -RADIUS; x <= RADIUS; x++) {
				changedBlocks.add(world.getBlockAt(l.getX() + x, l.getY() + y, l.getZ() + RADIUS));
			}
		}
		// -z
		for (int y = OFFSET_Y; y <= HEIGHT + OFFSET_Y; y++) {
			for (int x = -RADIUS; x <= RADIUS; x++) {
				changedBlocks.add(world.getBlockAt(l.getX() + x, l.getY() + y, l.getZ() - RADIUS));
			}
		}
		// +y
		for (int x = -RADIUS; x <= RADIUS; x++) {
			for (int z = -RADIUS; z <= RADIUS; z++) {
				changedBlocks.add(world.getBlockAt(l.getX() + x, l.getY() + OFFSET_Y + HEIGHT, l.getZ() + z));
			}
		}
		// -y
		for (int x = -RADIUS; x <= RADIUS; x++) {
			for (int z = -RADIUS; z <= RADIUS; z++) {
				changedBlocks.add(world.getBlockAt(l.getX() + x, l.getY() + OFFSET_Y, l.getZ() + z));
			}
		}
		
		for (Block b: changedBlocks) {
			if (!unbreakable.contains(b.getType()))
				b.setType(Material.BARRIER);
		}
		server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			for (Block b: changedBlocks) {
				if (!unbreakable.contains(b.getType()) || b.getType() == Material.BARRIER)
					b.setType(Material.AIR);
			}
		}, DURATION);
	}

	@Override
	public long getCooldown() {
		return 0;
	}

	@Override
	public Material getCraftingRecipeCenterItem() {
		return Material.SHIELD;
	}

	@Override
	public String getLore() {
		return "Constructs a large barrier cuboid\naround you that lasts for 1 minute";
	}

}
