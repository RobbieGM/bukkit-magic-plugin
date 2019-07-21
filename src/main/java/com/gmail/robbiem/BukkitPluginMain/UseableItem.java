package com.gmail.robbiem.BukkitPluginMain;

import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public interface UseableItem {
	public void use(ItemStack wandItem, Player player, World world, JavaPlugin plugin, Server server);
	public long getCooldown();
	public boolean isEventHandler();
}
