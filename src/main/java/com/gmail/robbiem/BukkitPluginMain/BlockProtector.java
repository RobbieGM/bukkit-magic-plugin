package com.gmail.robbiem.BukkitPluginMain;

import java.util.ArrayList;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class BlockProtector implements Listener {
	
	boolean isProtected(Material material) {
		return material.toString().contains("SHULKER");
	}
	
	@EventHandler(priority=EventPriority.HIGH)
	public void onBlockDamage(BlockDamageEvent e) {
		if (isProtected(e.getBlock().getType())) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority=EventPriority.HIGH)
	public void onBlockBreak(BlockBreakEvent e) {
		boolean isCreative = e.getPlayer() != null && e.getPlayer().getGameMode() == GameMode.CREATIVE;
		if (isProtected(e.getBlock().getType()) && !isCreative) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority=EventPriority.HIGH)
	public void onEntityExplode(EntityExplodeEvent e) {
		for (Block block: new ArrayList<Block>(e.blockList())) {
			if (isProtected(block.getType())) {
				e.blockList().remove(block);
			}
		}
	}
}
