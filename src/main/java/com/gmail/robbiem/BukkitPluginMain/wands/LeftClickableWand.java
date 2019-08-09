package com.gmail.robbiem.BukkitPluginMain.wands;

import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.robbiem.BukkitPluginMain.Main;

public abstract class LeftClickableWand extends Wand {
	public LeftClickableWand(Main plugin) {
		super(plugin);
	}
	abstract public boolean useAlt(ItemStack item, Player player, World world, Server server);
	public long getAltItemCooldown() {
		return getAltPlayerCooldown();
	}
	abstract public long getAltPlayerCooldown();
}
