package com.gmail.robbiem.BukkitPluginMain.wands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import com.gmail.robbiem.BukkitPluginMain.Main;

public class WandOfMagicMissile extends Wand {
	
	public WandOfMagicMissile(Main plugin) {
		super(plugin);
	}

	static final int RANGE = 100;
	
	Map<Player, MagicMissile> playerMissileMap = new HashMap<>();

	@Override
	public boolean use(ItemStack wandItem, Player player, World world, Server server) {
		if (playerMissileMap.containsKey(player)) {
			playerMissileMap.get(player).stopTask(server.getScheduler());
		}
		MagicMissile missile = new MagicMissile(player.getEyeLocation(), getPlayerLookTarget(player, RANGE), player);
		missile.startTaskScheduler(plugin, () -> {
			missile.setTarget(getPlayerLookTarget(player, RANGE));
			missile.move();
			if (missile.hasCollided()) {
				missile.explode();
				missile.stopTask(server.getScheduler());
			}
		});
		playerMissileMap.put(player, missile);
		return true;
	}
	
	Location getPlayerLookTarget(Player p, int range) {
		Block targetedBlock = p.getTargetBlockExact(range);
		if (targetedBlock != null)
			return targetedBlock.getLocation();
		else
			return p.getLocation().add(p.getLocation().getDirection().multiply(range));
	}

	@Override
	public long getPlayerCooldown() {
		return 2500l;
	}

	@Override
	public String getLore() {
		return "Creates an RPG-like magic missile that\nfollows your cursor until it explodes";
	}

	@Override
	public Material getWandTip() {
		return Material.NETHER_WART;
	}

	@Override
	public String getName() {
		return "Wand of Magic Missile";
	}
}

class MagicMissile {
	
	static final Particle PARTICLE = Particle.DRAGON_BREATH;
	
	Location location;
	Vector velocity;
	Location target;
	Player shooter;
	int taskId;
	
	MagicMissile(Location original, Location target, Player shooter) {
		location = original;
		velocity = new Vector();
		this.target = target;
		this.shooter = shooter;
	}
	
	void setTarget(Location newTarget) {
		target = newTarget;
	}
	
	void startTaskScheduler(JavaPlugin plugin, Runnable task) {
		taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, task, 0, 1);
	}
	
	void stopTask(BukkitScheduler scheduler) {
		scheduler.cancelTask(taskId);
	}
	
	void move() {
		Vector dir = target.toVector().subtract(location.toVector()).normalize();
		velocity.add(dir.clone().multiply(0.2));
		velocity.multiply(0.8); // Drag
		location.add(velocity);
		location.getWorld().spawnParticle(PARTICLE, location, 0, dir.getX(), dir.getY(), dir.getZ(), 0.4, null, true);
	}
	
	boolean hasCollided() {
		return !Arrays.asList(Material.AIR, Material.CAVE_AIR).contains(location.getBlock().getType());
	}
	
	void explode() {
		location.getWorld().spawnParticle(PARTICLE, location, 200, 0, 0, 0, 0.15, null, true);
		location.getWorld().spawnParticle(Particle.FLASH, location, 1);
		location.getWorld().playSound(location, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1, 1);
		for (LivingEntity entity: location.getWorld().getLivingEntities()) {
			if (entity.getLocation().distanceSquared(location) <= 4 * 4)
				entity.damage(10, shooter);
		}
	}

}