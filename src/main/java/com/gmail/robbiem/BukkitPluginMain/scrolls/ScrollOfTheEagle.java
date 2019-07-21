package com.gmail.robbiem.BukkitPluginMain.scrolls;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ScrollOfTheEagle extends Scroll {
	
	static final int LENGTH_SECONDS = 15;

	@Override
	public void use(ItemStack wandItem, Player player, World world, JavaPlugin plugin, Server server) {
		Location originalLocation = player.getLocation();
		player.setGameMode(GameMode.SPECTATOR);
		int particleTaskId = server.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
			world.spawnParticle(Particle.SPELL, originalLocation, 2, 1, 2, 1);
			world.spawnParticle(Particle.SQUID_INK, player.getLocation(), 1, 0.5, 0.5, 0.5, 0);
		}, 0, 1);
		server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			server.getScheduler().cancelTask(particleTaskId);
			player.teleport(originalLocation);
			player.setGameMode(GameMode.SURVIVAL);
		}, 20 * LENGTH_SECONDS);
	}

	@Override
	public long getCooldown() {
		return LENGTH_SECONDS * 1000;
	}

	@Override
	public Material getCraftingRecipeCenterItem() {
		return Material.FEATHER;
	}

}
