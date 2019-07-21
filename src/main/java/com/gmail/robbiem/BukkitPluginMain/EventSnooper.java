package com.gmail.robbiem.BukkitPluginMain;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class EventSnooper implements Listener {
	
	JavaPlugin plugin;
	
	public EventSnooper(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerCraftWand(CraftItemEvent e) {
		if (e.getInventory().getResult().getItemMeta() != null && e.getInventory().getResult().getItemMeta().hasDisplayName()) {
			plugin.getLogger().info(e.getWhoClicked().getName() + " crafted " + e.getInventory().getResult().getItemMeta().getDisplayName());
		} else {
			plugin.getLogger().info(e.getWhoClicked().getName() + " crafted " + e.getInventory().getResult().getType());
		}
	}
}
