package com.gmail.robbiem.BukkitPluginMain.wands;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public abstract class ParticleWand extends Wand {

	public void cast(Player player, JavaPlugin plugin, WandBlastCollideEvent onWandBlastCollide, EntityHitBySpellEvent onEntityHit) {
		Server server = plugin.getServer();
		World world = player.getWorld();
		Block targeted = player.getTargetBlock(null, getRange());
		Location eyeLocation = player.getEyeLocation();
		Vector difference = targeted.getLocation().toVector().subtract(eyeLocation.toVector());
		double distance = difference.length();
		for (int i = 1; i < distance; i++) {
			final int finalI = i;
			server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
				Location particleLocation = eyeLocation.toVector().add(difference.clone().multiply(finalI / distance)).toLocation(world);
				spawnWandParticle(particleLocation);
			}, i);
		}
		server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			onWandBlastCollide.run(targeted.getLocation());
			for (LivingEntity e: world.getLivingEntities()) {
				if (e.getLocation().distanceSquared(targeted.getLocation()) < Math.pow(getEffectRadius(), 2)) {
					onEntityHit.run(e, targeted.getLocation());
				}
			}
		}, (long) distance);
	}
	
	abstract int getRange();
	abstract float getEffectRadius();
	abstract void spawnWandParticle(Location particleLocation);
}

interface EntityHitBySpellEvent {
	public void run(LivingEntity hitEntity, Location spellLocation);
}

interface WandBlastCollideEvent {
	public void run(Location collisionPoint);
}