package com.gmail.robbiem.BukkitPluginMain.scrolls;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import com.gmail.robbiem.BukkitPluginMain.Main;

public class ScrollOfAntiMagic extends Scroll {
	
	public ScrollOfAntiMagic(Main plugin) {
		super(plugin);
	}

	static final int LENGTH_SECONDS = 15;

	@Override
	public Material getCraftingRecipeCenterItem() {
		return Material.WOODEN_SWORD;
	}

	@Override
	public boolean use(ItemStack item, Player player, World world, Server server) {
		world.getPlayers().forEach(p -> {
			((Main) plugin).moddedItemManager.cooldownManager.setPlayerCooldown(p, 1000 * LENGTH_SECONDS);
		});
		return true;
	}

	@Override
	public long getPlayerCooldown() {
		return 0;
	}

	@Override
	public String getLore() {
		return "Disables everyone's ability to use wands and\nscrolls for " + LENGTH_SECONDS + " seconds.";
	}

	@Override
	public String getName() {
		return "Scroll of Anti-Magic";
	}

}
