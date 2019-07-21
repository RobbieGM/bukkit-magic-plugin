package com.gmail.robbiem.BukkitPluginMain.scrolls;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ScrollOfTeleportation extends Scroll {

	@Override
	public void use(ItemStack wandItem, Player player, World world, JavaPlugin plugin, Server server) {
		Location teleportLocation = world.getWorldBorder().getCenter();
		double diameter = world.getWorldBorder().getSize();
		double radius = diameter / 2;
		teleportLocation.add(-radius + Math.random() * diameter, 0, -radius + Math.random() * diameter);
		teleportLocation.setY(world.getHighestBlockYAt(teleportLocation));
		float walkSpeed = player.getWalkSpeed();
		player.setWalkSpeed(0);
		player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 7, 1));
		server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			player.setWalkSpeed(walkSpeed);
		}, 20 * 2);
		server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			world.spawnParticle(Particle.PORTAL, player.getLocation(), 30, 1, 1, 1);
			player.teleport(teleportLocation);
			world.playSound(teleportLocation, Sound.BLOCK_PORTAL_TRAVEL, 0.5f, 1);
		}, 20 * 1);
	}

	@Override
	public long getCooldown() {
		return 2500l;
	}

	@Override
	public Material getCraftingRecipeCenterItem() {
		return Material.IRON_BOOTS;
	}

}