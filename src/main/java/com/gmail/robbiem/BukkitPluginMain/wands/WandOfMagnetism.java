package com.gmail.robbiem.BukkitPluginMain.wands;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.gmail.robbiem.BukkitPluginMain.Main;

public class WandOfMagnetism extends Wand {
	
	public WandOfMagnetism(Main plugin) {
		super(plugin);
	}

	@Override
	public boolean use(ItemStack wandItem, Player player, World world, Server server) {
		Location center = player.getTargetBlock(null, 40).getLocation();
		for (int i = 0; i < 150; i++) {
			makeSuctionParticle(center, 20);
		}
		world.playSound(center, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1, 1.5f);
		int taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
			for (LivingEntity entity: world.getLivingEntities()) {
				if (entity.getLocation().distance(center) > 20) continue;
				entity.setFallDistance(0);
				if (entity.equals(player)) continue;
				Vector propelVector = center.clone().subtract(entity.getLocation()).toVector().multiply(0.2);
				propelVector.setY(propelVector.getY() + 0.1);
				entity.setVelocity(propelVector);
			}
		}, 0, 1);
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			plugin.getServer().getScheduler().cancelTask(taskId);
		}, 10);
		return true;
	}
	
	void makeSuctionParticle(Location center, float radius) {
		Random r = new Random();
		double x = r.nextGaussian();
		double y = r.nextGaussian();
		double z = r.nextGaussian();
		Vector relative = new Vector(x, y, z).normalize().multiply(radius);
		Location particleLocation = center.clone().add(relative);
		Vector vel = relative.clone().multiply(-1);
		center.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, particleLocation, 0, vel.getX(), vel.getY(), vel.getZ(), 0.07);
	}

	@Override
	public long getPlayerCooldown() {
		return 500l;
	}
	
	@Override
	public long getItemCooldown() {
		return 2000l;
	}

	@Override
	public String getLore() {
		return "This wand pulls all players and mobs\ntowards its epicenter.";
	}

	@Override
	public Material getWandTip() {
		return Material.COMPASS;
	}

	@Override
	public String getName() {
		return "Wand of Magnetism";
	}

}
