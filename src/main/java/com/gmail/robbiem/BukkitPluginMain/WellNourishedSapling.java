package com.gmail.robbiem.BukkitPluginMain;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class WellNourishedSapling implements Listener {
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		Player player = e.getPlayer();
		
		if (e.getBlock().getType() == Material.OAK_SAPLING &&
			e.getItemInHand().getItemMeta().hasDisplayName() &&
			player != null &&
			e.getItemInHand().getItemMeta().getDisplayName().equals("Well-Nourished Sapling")) {
			e.getBlock().setType(Material.AIR);
			TreeType[] treeTypes = new TreeType[] {TreeType.ACACIA, TreeType.BIRCH, TreeType.COCOA_TREE, TreeType.MEGA_REDWOOD, TreeType.TALL_BIRCH, TreeType.DARK_OAK};
			TreeType randomTreeType = treeTypes[(int) (treeTypes.length * Math.random())];
			boolean successful = e.getBlock().getWorld().generateTree(e.getBlock().getLocation(), randomTreeType);
			if (successful) {
				Location playerLocation = player.getLocation();
				playerLocation.setY(e.getBlock().getWorld().getHighestBlockYAt(playerLocation));
				player.teleport(playerLocation);
			}
		}
	}
}
