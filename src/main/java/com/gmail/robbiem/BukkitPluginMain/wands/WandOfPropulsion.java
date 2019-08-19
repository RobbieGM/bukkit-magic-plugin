package com.gmail.robbiem.BukkitPluginMain.wands;

import static java.lang.Math.PI;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.gmail.robbiem.BukkitPluginMain.Main;
import com.gmail.robbiem.BukkitPluginMain.ModdedItemManager;

public class WandOfPropulsion extends Wand {
	
public WandOfPropulsion(Main plugin) {
		super(plugin);
	}

//	static final double TERMINAL_VELOCITY = 2;

	@Override
	public boolean use(ItemStack item, Player player, World world, Server server) {
		if (player.getInventory().getChestplate() != null && player.getInventory().getChestplate().getType() == Material.ELYTRA) {
			player.setGliding(true);
		}
		Vector vel = player.getLocation().getDirection().multiply(2);
//		if (vel.length() > TERMINAL_VELOCITY) {
//			vel.multiply(TERMINAL_VELOCITY / vel.length());
//		}
		player.setFallDistance(0);
		player.setVelocity(vel);
		int totalEffectTicks = 20;
		long startTime = world.getFullTime();
		int particleTaskId = server.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
			long ticks = world.getFullTime() - startTime;
			double fractionDone = (double) ticks / totalEffectTicks;
			Location currentLocation = player.getLocation();
			Vector v1 = new Vector(2, 3, 0).rotateAroundY(fractionDone * PI * 2);// .add(new Vector(0, 1 + fractionDone * 50, 0));
			v1.rotateAroundX(Math.toRadians(-currentLocation.getPitch() - 90));
			v1.rotateAroundY(Math.toRadians(-currentLocation.getYaw() + 180));
			Vector v2 = new Vector(-2, 3, 0).rotateAroundY(fractionDone * PI * 2);// .add(new Vector(0, 1 + fractionDone * 50, 0));
			v2.rotateAroundX(Math.toRadians(-currentLocation.getPitch() - 90));
			v2.rotateAroundY(Math.toRadians(-currentLocation.getYaw() + 180));
			world.spawnParticle(Particle.REDSTONE, v1.toLocation(world).add(currentLocation), 0, 0, 0, 0, 0, new Particle.DustOptions(Color.BLACK, 3f));
			world.spawnParticle(Particle.REDSTONE, v2.toLocation(world).add(currentLocation), 0, 0, 0, 0, 0, new Particle.DustOptions(Color.BLACK, 3f));
		}, 0, 1);
		server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			server.getScheduler().cancelTask(particleTaskId);
		}, totalEffectTicks);
		return true;
	}

	@Override
	public long getPlayerCooldown() {
		return 0;
	}
	
	@Override
	public long getItemCooldown() {
		return isBuffed ? 1000l : 2500l;
	}
	
	@Override
	public boolean isWeapon() {
		return false;
	}

	@Override
	public String getLore() {
		return "Propels you where\nyou are looking.";
	}

	@Override
	public Material getWandTip() {
		return Material.FIREWORK_ROCKET;
	}
	
	@Override
	public Material getWandBase() {
		return ModdedItemManager.LESSER_WAND_BASE;
	}

	@Override
	public String getName() {
		return "Wand of Propulsion";
	}

}
