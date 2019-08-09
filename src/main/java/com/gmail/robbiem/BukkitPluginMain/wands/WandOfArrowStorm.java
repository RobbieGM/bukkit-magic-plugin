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
import com.gmail.robbiem.BukkitPluginMain.ModdedItemManager;

public class WandOfArrowStorm extends LeftClickableWand {

	public WandOfArrowStorm(Main plugin) {
		super(plugin);
	}

	@Override
	public boolean use(ItemStack wandItem, Player player, World world, Server server) {
		for (int tickDelay = 0; tickDelay < 10; tickDelay += 2) {
			server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
				Location loc = player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(0.5)).toLocation(world, player.getLocation().getYaw(), player.getLocation().getPitch());
				Arrow arrow = (Arrow) world.spawnArrow(loc, loc.getDirection(), 2.5f, 8);
				arrow.setKnockbackStrength(1);
				arrow.setShooter(player);
				arrow.setPickupStatus(PickupStatus.CREATIVE_ONLY);
//				if (Math.random() > 0.9)
//					arrow.setFireTicks(20 * 5);
			}, tickDelay);
		}
		return true;
	}
	
	@Override
	public boolean useAlt(ItemStack item, Player player, World world, Server server) {
		Location loc = player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(0.5)).toLocation(world, player.getLocation().getYaw(), player.getLocation().getPitch());
		Arrow arrow = (Arrow) world.spawnArrow(loc, loc.getDirection(), 5f, 0);
		arrow.setKnockbackStrength(3);
		arrow.setShooter(player);
		arrow.setFireTicks(20 * 3);
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
		return 1500l;
	}

	@Override
	public long getAltPlayerCooldown() {
		return 0;
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
	public Material getWandBase() {
		return ModdedItemManager.LESSER_WAND_BASE;
	}

	@Override
	public String getName() {
		return "Wand of Arrowstorm";
	}

}
