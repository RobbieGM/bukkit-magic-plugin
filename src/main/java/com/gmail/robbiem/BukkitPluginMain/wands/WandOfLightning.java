package com.gmail.robbiem.BukkitPluginMain.wands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import com.gmail.robbiem.BukkitPluginMain.Main;
import com.gmail.robbiem.BukkitPluginMain.ModdedItemManager;

public class WandOfLightning extends Wand {

	public WandOfLightning(Main plugin) {
		super(plugin);
	}

	@Override
	public boolean use(ItemStack wandItem, Player player, World world, Server server) {
		Location hit = Wand.getTarget(player, 80, false);
		if (hit != null) {
			world.strikeLightning(hit);
			server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
				world.strikeLightning(hit);
			}, 25);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public long getPlayerCooldown() {
		return 1500l;
	}
	
	@Override
	public long getItemCooldown() {
		return isBuffed ? 1500l : 2500l;
	}

	@Override
	public String getLore() {
		return "Makes lightning strike where\nyou're looking";
	}

	@Override
	public Material getWandTip() {
		return Material.TRIDENT;
	}
	
	@Override
	public Material getWandBase() {
		return ModdedItemManager.LESSER_WAND_BASE;
	}

	@Override
	public String getName() {
		return "Wand of Lightning";
	}

}
