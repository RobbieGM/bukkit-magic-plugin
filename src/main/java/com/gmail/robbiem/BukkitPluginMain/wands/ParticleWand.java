package com.gmail.robbiem.BukkitPluginMain.wands;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public interface ParticleWand {

	public default void cast(Player player, JavaPlugin plugin, WandBlastCollideEvent onWandBlastCollide, EntityHitBySpellEvent onEntityHit, boolean explodeByDefault) {
		new Spell(player, plugin, getEffectRadius(), getRange(), getSpeed(), onWandBlastCollide, this::spawnWandParticle, onEntityHit, explodeByDefault);
	}
	
	public default void cast(Player player, JavaPlugin plugin, WandBlastCollideEvent onWandBlastCollide, EntityHitBySpellEvent onEntityHit) {
		new Spell(player, plugin, getEffectRadius(), getRange(), getSpeed(), onWandBlastCollide, this::spawnWandParticle, onEntityHit, false);
	}
	
	int getRange();
	default float getSpeed() {
		return 1f;
	}
	float getEffectRadius();
	void spawnWandParticle(Location particleLocation);
}

class Spell {
	
	WandBlastCollideEvent onWandBlastCollide, makeParticleEffect;
	EntityHitBySpellEvent onEntityHit;
	int repeatingTaskId;
	int eventualCollisionTaskId;
	int tick = 1;
	float speed;
	final long totalTicks;
	final Location eyeLocation;
	final Vector difference;
	float effectRadius;
	BukkitScheduler scheduler;
	Player caster;
	boolean hasExploded = false;
	
	Spell(Player player, JavaPlugin plugin, float effectRadius, int spellRange, float speed, WandBlastCollideEvent onWandBlastCollide, WandBlastCollideEvent makeParticleEffect, EntityHitBySpellEvent onEntityHit, boolean explodeByDefault) {
		this.onWandBlastCollide = onWandBlastCollide;
		this.onEntityHit = onEntityHit;
		this.effectRadius = effectRadius;
		this.makeParticleEffect = makeParticleEffect;
		this.speed = speed;
		this.caster = player;
		scheduler = plugin.getServer().getScheduler();
		Location targeted = player.getLocation().add(player.getLocation().getDirection().multiply(spellRange));
		eyeLocation = player.getEyeLocation();
		difference = targeted.toVector().subtract(eyeLocation.toVector());
		double distance = difference.length();
		totalTicks = (long) (distance / speed);
		repeatingTaskId = scheduler.scheduleSyncRepeatingTask(plugin, () -> {
			doTick();
		}, 0, 1);
		eventualCollisionTaskId = scheduler.scheduleSyncDelayedTask(plugin, () -> {
			if (explodeByDefault && !hasExploded) {
				explode(targeted);
			} else {
				plugin.getServer().getScheduler().cancelTask(repeatingTaskId);
			}
		}, (long) totalTicks);
	}
	
	boolean rayIsObstructed(Location start, Location end) {
		Vector direction = end.clone().subtract(start).toVector().normalize();
		RayTraceResult result = start.getWorld().rayTrace(start, direction, end.distance(start), FluidCollisionMode.NEVER, true, 1, entity -> !entity.equals(caster));
		return result != null;
	}
	
	void doTick() {
		double fraction = (double) tick / totalTicks;
		double lastFraction = ((double) (tick - 1) / totalTicks);
		Location particleLocation = eyeLocation.toVector().add(difference.clone().multiply(fraction)).toLocation(eyeLocation.getWorld());
		Location lastLocation = eyeLocation.toVector().add(difference.clone().multiply(lastFraction)).toLocation(eyeLocation.getWorld());
		boolean hit = rayIsObstructed(lastLocation, particleLocation);
		if (hit) {
			explode(particleLocation);
		} else {
			makeParticleEffect.run(particleLocation);
		}
		tick++;
	}
	
	void explode(Location location) {
		hasExploded = true;
		scheduler.cancelTask(repeatingTaskId);
		scheduler.cancelTask(eventualCollisionTaskId);
		onWandBlastCollide.run(location);
		for (LivingEntity e: location.getWorld().getLivingEntities()) {
			if (e.getLocation().distanceSquared(location) < Math.pow(effectRadius, 2) && !e.equals(caster)) {
				onEntityHit.run(e, location);
			}
		}
	}
}

interface EntityHitBySpellEvent {
	public void run(LivingEntity hitEntity, Location spellLocation);
}

interface WandBlastCollideEvent {
	public void run(Location collisionPoint);
}