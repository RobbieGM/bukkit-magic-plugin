package com.gmail.robbiem.BukkitPluginMain.wands;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.gmail.robbiem.BukkitPluginMain.Main;
import com.gmail.robbiem.BukkitPluginMain.ModdedItemManager;

public class WandOfPropulsion extends Wand {
	
public WandOfPropulsion(Main plugin) {
		super(plugin);
	}

//	static final double TERMINAL_VELOCITY = 2;

	@Override
	public boolean use(ItemStack item, Player player, World world, Server server) {
		if (player.getInventory().getChestplate() != null && player.getInventory().getChestplate().getType() == Material.ELYTRA) {
			player.setGliding(true);
		}
		Vector vel = player.getLocation().getDirection().multiply(2);
//		if (vel.length() > TERMINAL_VELOCITY) {
//			vel.multiply(TERMINAL_VELOCITY / vel.length());
//		}
		player.setVelocity(vel);
		return true;
	}

	@Override
	public long getPlayerCooldown() {
		return 0;
	}
	
	@Override
	public long getItemCooldown() {
		return 2500l;
	}
	
	@Override
	public boolean isWeapon() {
		return false;
	}

	@Override
	public String getLore() {
		return "With an elytra, propels you.";
	}

	@Override
	public Material getWandTip() {
		return Material.FIREWORK_ROCKET;
	}
	
	@Override
	public Material getWandBase() {
		return ModdedItemManager.LESSER_WAND_BASE;
	}

	@Override
	public String getName() {
		return "Wand of Propulsion";
	}

}
