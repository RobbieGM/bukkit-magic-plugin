package com.gmail.robbiem.BukkitPluginMain.wands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import com.gmail.robbiem.BukkitPluginMain.Main;

public class WandOfArrowStorm extends LeftClickableWand {

	public WandOfArrowStorm(Main plugin) {
		super(plugin);
	}

	@Override
	public boolean use(ItemStack wandItem, Player player, World world, Server server) {
		for (int tickDelay = 0; tickDelay <= 9; tickDelay += 3) {
			server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
				Location loc = player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(0.5)).toLocation(world, player.getLocation().getYaw(), player.getLocation().getPitch());
				Arrow arrow = (Arrow) world.spawnArrow(loc, loc.getDirection(), 1.8f, isBuffed ? 4 : 8);
				if (isBuffed)
					arrow.setKnockbackStrength(1);
				arrow.setShooter(player);
				arrow.setPickupStatus(PickupStatus.CREATIVE_ONLY);
				// if (Math.random() > 0.9)
				// 	arrow.setFireTicks(20 * 5);
			}, tickDelay);
		}
		return true;
	}
	
	@Override
	public boolean useAlt(ItemStack item, Player player, World world, Server server) {
		Location loc = player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(0.5)).toLocation(world, player.getLocation().getYaw(), player.getLocation().getPitch());
		Arrow arrow = (Arrow) world.spawnArrow(loc, loc.getDirection(), 3f, 0);
		// arrow.setGravity(false);
		arrow.setKnockbackStrength(1);
		arrow.setShooter(player);
		arrow.setPickupStatus(PickupStatus.CREATIVE_ONLY);
		return true;
	}

	@Override
	public long getPlayerCooldown() {
		return 300l;
	}
	
	@Override
	public long getItemCooldown() {
		return 500l;
	}
	
	@Override
	public long getAltItemCooldown() {
		return isBuffed ? 1250l : 1750l;
	}

	@Override
	public long getAltPlayerCooldown() {
		return 500l;
	}

	@Override
	public String getLore() {
		return "Spawns a stream of arrows in the\ndirection you're looking";
	}

	@Override
	public Material getWandTip() {
		return Material.ARROW;
	}

	@Override
	public String getName() {
		return "Wand of Arrowstorm";
	}

}
