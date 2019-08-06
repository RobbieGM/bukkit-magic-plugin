package com.gmail.robbiem.BukkitPluginMain.scrolls;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.gmail.robbiem.BukkitPluginMain.Main;

public class ScrollOfFlight extends Scroll {

	public ScrollOfFlight(Main plugin) {
		super(plugin);
	}

	@Override
	public boolean use(ItemStack wandItem, Player player, World world, Server server) {
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
		return true;
	}

	@Override
	public long getPlayerCooldown() {
		return 1000l;
	}

	@Override
	public Material getCraftingRecipeCenterItem() {
		return Material.ELYTRA;
	}

	@Override
	public String getLore() {
		return "Lets you fly for 10 seconds\nas if in creative mode";
	}

	@Override
	public String getName() {
		return "Scroll of Flight";
	}

}
