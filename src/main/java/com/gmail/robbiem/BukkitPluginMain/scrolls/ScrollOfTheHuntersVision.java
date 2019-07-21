package com.gmail.robbiem.BukkitPluginMain.scrolls;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ScrollOfTheHuntersVision extends Scroll {

	@Override
	public void use(ItemStack wandItem, Player player, World world, JavaPlugin plugin, Server server) {
		for (Player p: world.getPlayers()) {
			if (!p.equals(player)) {
				p.setGlowing(true);
			}
		}
	}

	@Override
	public long getCooldown() {
		return 5000l;
	}

	@Override
	public Material getCraftingRecipeCenterItem() {
		return Material.GLOWSTONE;
	}

}
