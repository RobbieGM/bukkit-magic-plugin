package com.gmail.robbiem.BukkitPluginMain;

import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class UseableItem {
	protected Main plugin;
	public UseableItem(Main plugin) {
		this.plugin = plugin;
	}
	abstract public boolean use(ItemStack item, Player player, World world, Server server);
	abstract public long getPlayerCooldown();
	public abstract String getName();
	public long getItemCooldown() {
		return getPlayerCooldown();
	}
	public boolean isEventHandler() {
		return false;
	}
	abstract public String getLore();
}
