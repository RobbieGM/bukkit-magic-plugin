package com.gmail.robbiem.BukkitPluginMain.scrolls;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;
import com.gmail.robbiem.BukkitPluginMain.Main;

public class ScrollOfTheEagle extends Scroll {
	
	public ScrollOfTheEagle(Main plugin) {
		super(plugin);
	}

	static final int LENGTH_SECONDS = 20;

	@Override
	public boolean use(ItemStack wandItem, Player player, World world, Server server) {
		Location originalLocation = player.getLocation();
		player.setGameMode(GameMode.SPECTATOR);
		int particleTaskId = server.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
			world.spawnParticle(Particle.SPELL, originalLocation, 2, 1, 2, 1);
			world.spawnParticle(Particle.SQUID_INK, player.getLocation(), 1, 0.5, 0.5, 0.5, 0);
		}, 0, 1);
		server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			server.getScheduler().cancelTask(particleTaskId);
			TNTPrimed tnt = (TNTPrimed) world.spawnEntity(player.getLocation(), EntityType.PRIMED_TNT);
			tnt.setYield(3f);
			tnt.setIsIncendiary(true);
			tnt.setFuseTicks(0);
			player.teleport(originalLocation);
			player.setGameMode(GameMode.SURVIVAL);
		}, 20 * LENGTH_SECONDS);
		return true;
	}

	@Override
	public long getPlayerCooldown() {
		return LENGTH_SECONDS * 1000;
	}

	@Override
	public Material getCraftingRecipeCenterItem() {
		return Material.FEATHER;
	}

	@Override
	public String getLore() {
		return "Lets you fly around in spectator mode for 15 seconds,\nthen puts you back where you were";
	}

	@Override
	public String getName() {
		return "Scroll of the Eagle";
	}

}
