package com.gmail.robbiem.BukkitPluginMain;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ChunkPlacer implements Listener {
	List<Material> unbreakable;
	
	public ChunkPlacer(List<Material> unbreakable) {
		this.unbreakable = unbreakable;
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		Player player = e.getPlayer();
		
		ItemStack offHandItem = player.getInventory().getItemInOffHand();
		if (player != null && offHandItem != null && offHandItem.getItemMeta() != null && offHandItem.getItemMeta().hasDisplayName() && offHandItem.getItemMeta().getDisplayName().equals("Chunk Placer")) {
			player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 3);
			for (int x = -1; x <= 1; x++) {
				for (int y = -1; y <= 1; y++) {
					for (int z = -1; z <= 1; z++) {
						Location locationAround = e.getBlock().getLocation().add(new Vector(x, y, z));
						Block block = e.getBlock().getWorld().getBlockAt(locationAround);
						if (!unbreakable.contains(block.getType())) {
							block.setType(e.getBlock().getType());
						}
					}
				}
			}
		}
	}
}
