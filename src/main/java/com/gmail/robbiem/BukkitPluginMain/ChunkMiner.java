package com.gmail.robbiem.BukkitPluginMain;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.util.Vector;

public class ChunkMiner implements Listener {

	List<Material> unbreakable;
	
	public ChunkMiner(List<Material> unbreakable) {
		this.unbreakable = unbreakable;
	}
	
	@EventHandler
	public void onBlockDamage(BlockDamageEvent e) {
		if (e.getItemInHand().getItemMeta() != null && e.getItemInHand().getItemMeta().hasDisplayName() && e.getItemInHand().getItemMeta().getDisplayName().equals("Chunk Miner")) {
			for (int x = -1; x <= 1; x++) {
				for (int y = -1; y <= 1; y++) {
					for (int z = -1; z <= 1; z++) {
						Location locationAround = e.getBlock().getLocation().add(new Vector(x, y, z));
						Block block = e.getBlock().getWorld().getBlockAt(locationAround);
						if (!unbreakable.contains(block.getType())) {
							if (x == 0 && y == 0 && z == 0)
								block.breakNaturally();
							else
								block.setType(Material.AIR);
						}
					}
				}
			}
		}
	}
}
