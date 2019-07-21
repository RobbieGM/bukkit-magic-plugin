package com.gmail.robbiem.BukkitPluginMain.scrolls;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class ScrollOfFlight extends Scroll {

	@Override
	public void use(ItemStack wandItem, Player player, World world, JavaPlugin plugin, Server server) {
		player.setAllowFlight(true);
		player.setVelocity(new Vector(0, 3, 0));
		player.setFlying(true);
		int particleSpawnerTask = server.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
			world.spawnParticle(Particle.SPELL, player.getLocation(), 4, 1, 1, 1);
		}, 0, 2);
		server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			server.getScheduler().cancelTask(particleSpawnerTask);
			player.setAllowFlight(false);
			player.setFlying(false);
		}, 20 * 10);
	}

	@Override
	public long getCooldown() {
		return 1000l;
	}

	@Override
	public Material getCraftingRecipeCenterItem() {
		return Material.ELYTRA;
	}

}
