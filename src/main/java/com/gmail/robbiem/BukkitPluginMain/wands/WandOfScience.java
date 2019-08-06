package com.gmail.robbiem.BukkitPluginMain.wands;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.robbiem.BukkitPluginMain.Main;

public class WandOfScience extends Wand {

	public WandOfScience(Main plugin) {
		super(plugin);
	}

	@Override
	public Material getWandTip() {
		return Material.POTION;
	}

	@Override
	public boolean use(ItemStack item, Player player, World world, Server server) {
		return true;
	}

	@Override
	public long getPlayerCooldown() {
		return 500l;
	}

	@Override
	public String getName() {
		return "Wand of Science";
	}

	@Override
	public String getLore() {
		return "For testing only";
	}

}
