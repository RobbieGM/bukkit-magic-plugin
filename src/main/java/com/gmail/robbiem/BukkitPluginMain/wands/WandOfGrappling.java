package com.gmail.robbiem.BukkitPluginMain.wands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.gmail.robbiem.BukkitPluginMain.Main;

public class WandOfGrappling extends ParticleWand {
	
	public WandOfGrappling(Main plugin) {
		super(plugin);
	}

	static final double MAX_SPEED = 2;
	
	@Override
	public boolean use(ItemStack item, Player player, World world, Server server) {
		this.cast(player, plugin, (Location l) -> {
			int taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
				player.setFallDistance(0);
				try {
					l.checkFinite();
					Vector propelVector = l.clone().subtract(player.getLocation()).toVector().multiply(0.2);
					double mag = propelVector.length();
					if (mag > MAX_SPEED) {
						propelVector.multiply(MAX_SPEED / mag);
					}
					propelVector.setY(propelVector.getY() + 0.1);
					player.setVelocity(propelVector);
				} catch (IllegalArgumentException e) {}
			}, 0, 1);
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
				plugin.getServer().getScheduler().cancelTask(taskId);
			}, 15);
		}, (LivingEntity e, Location l) -> {});
		return true;
	}
	
	@Override
	float getSpeed() {
		return 2f;
	}

	@Override
	int getRange() {
		return 50;
	}

	@Override
	float getEffectRadius() {
		return 0;
	}

	@Override
	void spawnWandParticle(Location particleLocation) {
		particleLocation.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, particleLocation, 1);
	}

	@Override
	public long getPlayerCooldown() {
		return 1000l;
	}
	
	@Override
	public long getItemCooldown() {
		return 2000l;
	}
	
	@Override
	public boolean isWeapon() {
		return false;
	}

	@Override
	public String getLore() {
		return "This wand is like a wand of teleportation,\nbut not nearly as good.";
	}

	@Override
	public Material getWandTip() {
		return Material.STRING;
	}

	@Override
	public String getName() {
		return "Wand of Grappling";
	}

}
