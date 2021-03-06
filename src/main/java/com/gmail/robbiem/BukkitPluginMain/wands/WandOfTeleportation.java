package com.gmail.robbiem.BukkitPluginMain.wands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.gmail.robbiem.BukkitPluginMain.Main;
import com.gmail.robbiem.BukkitPluginMain.ModdedItemManager;

public class WandOfTeleportation extends Wand {
	
	public WandOfTeleportation(Main plugin) {
		super(plugin);
	}

	static final int RANGE = 70;
	
	@Override
	public boolean use(ItemStack wandItem, Player player, World world, Server server) {
		Location previousLocation = player.getLocation();
		Location targetedLocation = Wand.getTarget(player, RANGE, true);
		clampToWorldBorder(targetedLocation);
		targetedLocation = moveUpUntilStandable(targetedLocation);
		Location highestBlockLocation = world.getHighestBlockAt(targetedLocation).getLocation();
		if (targetedLocation.getY() > highestBlockLocation.getY()) { // Avoid teleporting into air, but allow teleporting under trees.
			targetedLocation.setY(highestBlockLocation.getY());
		}
		targetedLocation.add(new Vector(0, 1, 0));
		targetedLocation.setDirection(previousLocation.getDirection());
		world.spawnParticle(Particle.PORTAL, previousLocation, 30, 1, 1, 1);
		Vector previousLocationVector = previousLocation.toVector();
		Vector locationDifference = targetedLocation.toVector().subtract(previousLocationVector);
		int vectorMag = (int) locationDifference.length();
		server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			for (int i = 1; i <= vectorMag; i++) {
				world.spawnParticle(Particle.PORTAL, previousLocationVector.clone().add(locationDifference.clone().multiply((double) i / vectorMag)).toLocation(world), 10, 0.5, 0.5, 0.5);
			}
		}, 1);
		player.teleport(targetedLocation);
		world.playSound(targetedLocation, Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1, 1);
		return true;
	}
	
	Location moveUpUntilStandable(Location orig) {
		Location l = orig.clone();
		for (int i = 0; i < 10; i++) {
			if (l.getBlock().isPassable()) return l;
			l.add(0, 1, 0);
		}
		l.setY(orig.getWorld().getHighestBlockYAt(orig));
		return l;
	}
	
	void clampToWorldBorder(Location orig) {
		WorldBorder border = orig.getWorld().getWorldBorder();
		if (orig.getX() < border.getCenter().getX() - border.getSize() / 2)
			orig.setX(border.getCenter().getX() - border.getSize() / 2);
		if (orig.getX() > border.getCenter().getX() + border.getSize() / 2)
			orig.setX(border.getCenter().getX() + border.getSize() / 2);
		if (orig.getZ() < border.getCenter().getZ() - border.getSize() / 2)
			orig.setZ(border.getCenter().getZ() - border.getSize() / 2);
		if (orig.getZ() > border.getCenter().getZ() + border.getSize() / 2)
			orig.setZ(border.getCenter().getZ() + border.getSize() / 2);
	}
	
	@Override
	public long getPlayerCooldown() {
		return 2000l;
	}
	
	@Override
	public long getItemCooldown() {
		return isBuffed ? 5500l : 7000l;
	}

	@Override
	public boolean isWeapon() {
		return false;
	}

	@Override
	public String getLore() {
		return "Teleports you where you're looking,\nbut can't make you go into the sky";
	}

	@Override
	public Material getWandTip() {
		return Material.ENDER_PEARL;
	}

	@Override
	public Material getWandBase() {
		return ModdedItemManager.LESSER_WAND_BASE;
	}

	@Override
	public String getName() {
		return "Wand of Teleportation";
	}
}
