package com.gmail.robbiem.BukkitPluginMain.wands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import com.gmail.robbiem.BukkitPluginMain.Main;

public class WandOfForceField extends Wand {
	
	public WandOfForceField(Main plugin) {
		super(plugin);
	}

	static final int RADIUS = 6;
	static final int THICKNESS = 3;

	@Override
	public boolean use(ItemStack item, Player player, World world, Server server) {
		List<Block> barriers = new ArrayList<>();
		Location lookedAt = player.getLocation().getDirection().multiply(5).toLocation(world).add(player.getLocation());
		for (int x = -RADIUS; x < RADIUS; x++) {
			for (int y = -RADIUS; y < RADIUS; y++) {
				for (int z = -RADIUS; z < RADIUS; z++) {
					Location l = player.getLocation().add(x, y, z);
					double distSquared = player.getLocation().distanceSquared(l);
					boolean isOnSphereSurface = distSquared < RADIUS * RADIUS && distSquared >= (RADIUS - THICKNESS) * (RADIUS - THICKNESS);
					boolean isOnSphericalCap = l.distanceSquared(lookedAt) < RADIUS * RADIUS;
					if (isOnSphereSurface && isOnSphericalCap) {
						barriers.add(l.getBlock());
					}
				}
			}
		}
		
		for (Block b: barriers) {
			if (Arrays.asList(Material.CAVE_AIR, Material.AIR).contains(b.getType())) {
				b.setType(Material.BARRIER);
				world.spawnParticle(Particle.REDSTONE, b.getLocation(), 2, 0.2, 0.2, 0.2, 0.1, new Particle.DustOptions(Color.AQUA, 1.5f));
			}
		}
		server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			for (Block b: barriers) {
				if (b.getType() == Material.BARRIER) {
					b.setType(Material.AIR);
				}
			}
		}, 30);
		return true;
	}

	@Override
	public long getPlayerCooldown() {
		// TODO Auto-generated method stub
		return 1000l;
	}
	
	@Override
	public boolean isWeapon() {
		return false;
	}

	@Override
	public String getLore() {
		return "This wand creates a temporary\ninvisible shield where you look.";
	}

	@Override
	public Material getWandTip() {
		return Material.SHIELD;
	}

	@Override
	public String getName() {
		return "Wand of Force Field";
	}

}
