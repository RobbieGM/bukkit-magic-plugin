package com.gmail.robbiem.BukkitPluginMain.wands;

import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import com.gmail.robbiem.BukkitPluginMain.Main;

public abstract class ParticleWand extends Wand {

	public ParticleWand(Main plugin) {
		super(plugin);
	}

	public void cast(Player player, JavaPlugin plugin, WandBlastCollideEvent onWandBlastCollide, EntityHitBySpellEvent onEntityHit, boolean explodeByDefault) {
		new Spell(player, plugin, getEffectRadius(), getRange(), getSpeed(), onWandBlastCollide, this::spawnWandParticle, onEntityHit, explodeByDefault);
	}
	
	public void cast(Player player, JavaPlugin plugin, WandBlastCollideEvent onWandBlastCollide, EntityHitBySpellEvent onEntityHit) {
		new Spell(player, plugin, getEffectRadius(), getRange(), getSpeed(), onWandBlastCollide, this::spawnWandParticle, onEntityHit, false);
	}
	
	abstract int getRange();
	float getSpeed() {
		return 1f;
	}
	abstract float getEffectRadius();
	abstract void spawnWandParticle(Location particleLocation);
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
	boolean hasExploded = false;
	
	Spell(Player player, JavaPlugin plugin, float effectRadius, int spellRange, float speed, WandBlastCollideEvent onWandBlastCollide, WandBlastCollideEvent makeParticleEffect, EntityHitBySpellEvent onEntityHit, boolean explodeByDefault) {
		this.onWandBlastCollide = onWandBlastCollide;
		this.onEntityHit = onEntityHit;
		this.effectRadius = effectRadius;
		this.makeParticleEffect = makeParticleEffect;
		this.speed = speed;
		scheduler = plugin.getServer().getScheduler();
		Block targeted = player.getTargetBlock(null, spellRange);
		eyeLocation = player.getEyeLocation();
		difference = targeted.getLocation().toVector().subtract(eyeLocation.toVector());
		double distance = difference.length();
		totalTicks = (long) (distance / speed);
		repeatingTaskId = scheduler.scheduleSyncRepeatingTask(plugin, () -> {
			doTick();
		}, 0, 1);
		eventualCollisionTaskId = scheduler.scheduleSyncDelayedTask(plugin, () -> {
			if (explodeByDefault && !hasExploded) {
				explode(targeted.getLocation());
			} else {
				plugin.getServer().getScheduler().cancelTask(repeatingTaskId);
			}
		}, (long) totalTicks);
	}
	
	void doTick() {
		double fraction = (double) tick / totalTicks;
		Location particleLocation = eyeLocation.toVector().add(difference.clone().multiply(fraction)).toLocation(eyeLocation.getWorld());
		if (Arrays.asList(Material.AIR, Material.CAVE_AIR).contains(particleLocation.getBlock().getType())) {
			makeParticleEffect.run(particleLocation);
		} else {
			explode(particleLocation);
		}
		tick++;
	}
	
	void explode(Location location) {
		hasExploded = true;
		scheduler.cancelTask(repeatingTaskId);
		scheduler.cancelTask(eventualCollisionTaskId);
		onWandBlastCollide.run(location);
		for (LivingEntity e: location.getWorld().getLivingEntities()) {
			if (e.getLocation().distanceSquared(location) < Math.pow(effectRadius, 2)) {
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