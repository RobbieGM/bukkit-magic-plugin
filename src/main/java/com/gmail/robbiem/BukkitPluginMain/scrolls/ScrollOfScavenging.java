package com.gmail.robbiem.BukkitPluginMain.scrolls;

import java.util.Collection;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import com.gmail.robbiem.BukkitPluginMain.Main;

public class ScrollOfScavenging extends Scroll {

	public ScrollOfScavenging(Main plugin) {
		super(plugin);
	}

	@Override
	public boolean use(ItemStack wandItem, Player player, World world, Server server) {
		Collection<Item> items = world.getEntitiesByClass(Item.class);
		if (items.size() == 0)
			return false;
		items.forEach(item -> {
			item.teleport(player);
		});
		return true;
	}

	@Override
	public long getPlayerCooldown() {
		return 0;
	}

	@Override
	public Material getCraftingRecipeCenterItem() {
		return Material.HOPPER;
	}

	@Override
	public String getLore() {
		return "Teleports all items on\nthe ground to you";
	}

	@Override
	public String getName() {
		return "Scroll of Scavenging";
	}

}
