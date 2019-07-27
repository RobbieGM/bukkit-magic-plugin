package com.gmail.robbiem.BukkitPluginMain.scrolls;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ScrollOfJealousy extends Scroll implements Listener {

	@Override
	public void use(ItemStack item, Player player, World world, JavaPlugin plugin, Server server) {
		Player closest = null;
		for (Player p: world.getPlayers()) {
			if ((closest == null || p.getLocation().distanceSquared(player.getLocation()) < closest.getLocation().distanceSquared(player.getLocation())) && p.getGameMode() == GameMode.SURVIVAL && !p.equals(closest)) {
				closest = p;
			}
		}
		if (closest != null) {
			player.openInventory(closest.getInventory());
		} else {
			player.sendMessage("No other players were detected. Are they in survival mode?");
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		InventoryHolder holder = e.getClickedInventory().getHolder();
		if (holder instanceof Player && e.getWhoClicked() instanceof Player && !holder.equals(e.getWhoClicked())) {
			e.setCancelled(true);
		}
	}
	
	@Override
	public boolean isEventHandler() {
		return true;
	}

	@Override
	public long getCooldown() {
		return 0;
	}

	@Override
	public String getLore() {
		return "Just how screwed are you?\nFind out with this scroll that lets you\nsee the nearest player's inventory.";
	}

	@Override
	public Material getCraftingRecipeCenterItem() {
		return Material.BARREL;
	}

}
