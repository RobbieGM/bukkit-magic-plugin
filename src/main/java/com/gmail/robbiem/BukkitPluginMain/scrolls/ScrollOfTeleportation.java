package com.gmail.robbiem.BukkitPluginMain.scrolls;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.gmail.robbiem.BukkitPluginMain.Main;

public class ScrollOfTeleportation extends Scroll {

	public ScrollOfTeleportation(Main plugin) {
		super(plugin);
	}

	@Override
	public boolean use(ItemStack wandItem, Player player, World world, Server server) {
		Location teleportLocation = null;
		while (teleportLocation == null || teleportLocation.distance(player.getLocation()) < world.getWorldBorder().getSize() * 0.5) {
			teleportLocation = getRandomLocationInBorder(world);
		}
		final Location finalTpLocation = teleportLocation;
		float walkSpeed = player.getWalkSpeed();
		player.setWalkSpeed(0);
		player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 7, 1));
		server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			player.setWalkSpeed(walkSpeed);
		}, 20 * 2);
		server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			world.spawnParticle(Particle.PORTAL, player.getLocation(), 30, 1, 1, 1);
			player.teleport(finalTpLocation);
			world.playSound(finalTpLocation, Sound.BLOCK_PORTAL_TRAVEL, 0.5f, 1);
		}, 20 * 1);
		return true;
	}
	
	Location getRandomLocationInBorder(World world) {
		Location teleportLocation = world.getWorldBorder().getCenter();
		double diameter = world.getWorldBorder().getSize();
		double radius = diameter / 2;
		teleportLocation.add(-radius + Math.random() * diameter, 0, -radius + Math.random() * diameter);
		teleportLocation.setY(world.getHighestBlockYAt(teleportLocation));
		return teleportLocation;
	}

	@Override
	public long getPlayerCooldown() {
		return 2500l;
	}

	@Override
	public Material getCraftingRecipeCenterItem() {
		return Material.IRON_BOOTS;
	}

	@Override
	public String getLore() {
		return "Puts you in a random place\ninside the world border";
	}

	@Override
	public String getName() {
		return "Scroll of Teleportation";
	}

}
