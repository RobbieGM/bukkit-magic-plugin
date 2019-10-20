package com.gmail.robbiem.BukkitPluginMain.wands;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public interface ParticleWand {

	public default void cast(Player player, JavaPlugin plugin, WandBlastCollideEvent onWandBlastCollide,
			EntityHitBySpellEvent onEntityHit, boolean explodeByDefault) {
		new Spell(player, plugin, this, onWandBlastCollide, this::spawnWandParticle, onEntityHit, explodeByDefault);
	}

	public default void cast(Player player, JavaPlugin plugin, WandBlastCollideEvent onWandBlastCollide,
			EntityHitBySpellEvent onEntityHit) {
		new Spell(player, plugin, this, onWandBlastCollide, this::spawnWandParticle, onEntityHit, false);
	}

	int getRange();

	default float getSpeed() {
		return 1f;
	}

	default float getSpread() {
		return 0;
	}

	float getEffectRadius();

	void spawnWandParticle(Location particleLocation);
}

class Spell {
	static final boolean ALLOW_SELF_HIT = false;
	static final Vector ACCELERATION = new Vector(0, -0.01, 0);

	WandBlastCollideEvent onWandBlastCollide, makeParticleEffect;
	EntityHitBySpellEvent onEntityHit;
	int repeatingTaskId;
	int eventualCollisionTaskId;
	int tick = 1;
	// final Vector difference;
	Location location;
	Vector velocity;
	ParticleWand wand;
	BukkitScheduler scheduler;
	Player caster;
	boolean hasExploded = false;

	Spell(Player player, JavaPlugin plugin, ParticleWand wand, WandBlastCollideEvent onWandBlastCollide,
			WandBlastCollideEvent makeParticleEffect, EntityHitBySpellEvent onEntityHit, boolean explodeByDefault) {
		this.onWandBlastCollide = onWandBlastCollide;
		this.onEntityHit = onEntityHit;
		this.makeParticleEffect = makeParticleEffect;
		this.caster = player;
		this.wand = wand;
		location = caster.getEyeLocation();
		scheduler = plugin.getServer().getScheduler();
		Vector randomVec = new Vector(-1 + 2 * Math.random(), -1 + 2 * Math.random(), -1 + 2 * Math.random());
		randomVec.multiply(wand.getSpread());
		// Location targeted = eyeLocation.clone()
		// .add(player.getLocation().getDirection().add(randomVec).multiply(wand.getRange()));
		// difference = targeted.toVector().subtract(eyeLocation.toVector());
		// double distance = difference.length();
		velocity = caster.getLocation().getDirection().add(randomVec).multiply(wand.getSpeed()); // .add(caster.getVelocity());
		repeatingTaskId = scheduler.scheduleSyncRepeatingTask(plugin, () -> {
			doTick();
		}, 0, 1);
		long projectileLifetime = (long) (wand.getRange() / wand.getSpeed());
		eventualCollisionTaskId = scheduler.scheduleSyncDelayedTask(plugin, () -> {
			if (explodeByDefault && !hasExploded) {
				explode(location);
			} else {
				plugin.getServer().getScheduler().cancelTask(repeatingTaskId);
			}
		}, projectileLifetime);
	}

	boolean rayIsObstructed(Location start, Location end) {
		Vector direction = end.clone().subtract(start).toVector().normalize();
		RayTraceResult result = start.getWorld().rayTrace(start, direction, end.distance(start), FluidCollisionMode.NEVER,
				true, 0, entity -> !entity.equals(caster));
		return result != null && !(result.getHitBlock() != null && blockIsPassableForCaster(result.getHitBlock()));
	}

	boolean blockIsPassableForCaster(Block block) {
		for (MetadataValue value : block.getMetadata("placerId")) {
			if (value.value().equals(caster.getUniqueId())) {
				return true;
			}
		}
		return false;
	}

	void doTick() {
		Location lastLocation = location.clone();
		velocity.add(ACCELERATION);
		location.add(velocity);
		boolean hit = rayIsObstructed(lastLocation, location);
		if (hit) {
			explode(location);
		} else {
			makeParticleEffect.run(location);
		}
		tick++;
	}

	void explode(Location location) {
		hasExploded = true;
		scheduler.cancelTask(repeatingTaskId);
		scheduler.cancelTask(eventualCollisionTaskId);
		onWandBlastCollide.run(location);
		for (LivingEntity e : location.getWorld().getLivingEntities()) {
			if (e.getLocation().distanceSquared(location) < Math.pow(wand.getEffectRadius(), 2)
					&& (!e.equals(caster) || ALLOW_SELF_HIT)) {
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