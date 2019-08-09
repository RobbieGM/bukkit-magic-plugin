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
import com.gmail.robbiem.BukkitPluginMain.ModdedItemManager;

public class WandOfForceField extends LeftClickableWand {
	
	public WandOfForceField(Main plugin) {
		super(plugin);
	}

	static final int THICKNESS = 3;

	@Override
	public boolean use(ItemStack item, Player player, World world, Server server) {
		makeForceField(player, 6, 30, false);
		return true;
	}
	
	@Override
	public boolean useAlt(ItemStack item, Player player, World world, Server server) {
		makeForceField(player, 12, 20 * 5, true);
		return true;
	}
	
	public void makeForceField(Player player, int radius, int durationTicks, boolean reducedParticles) {
		World world = player.getWorld();
		Server server = player.getServer();
		List<Block> barriers = new ArrayList<>();
		Location lookedAt = player.getLocation().getDirection().multiply(5).toLocation(world).add(player.getLocation());
		for (int x = -radius; x < radius; x++) {
			for (int y = -radius; y < radius; y++) {
				for (int z = -radius; z < radius; z++) {
					Location l = player.getLocation().add(x, y, z);
					double distSquared = player.getLocation().distanceSquared(l);
					boolean isOnSphereSurface = distSquared < radius * radius && distSquared >= (radius - THICKNESS) * (radius - THICKNESS);
					boolean isOnSphericalCap = l.distanceSquared(lookedAt) < radius * radius;
					if (isOnSphereSurface && isOnSphericalCap) {
						barriers.add(l.getBlock());
					}
				}
			}
		}
		
		for (Block b: barriers) {
			if (Arrays.asList(Material.CAVE_AIR, Material.AIR).contains(b.getType())) {
				b.setType(Material.BARRIER);
				world.spawnParticle(Particle.REDSTONE, b.getLocation(), reducedParticles ? 1 : 2, 0.2, 0.2, 0.2, 0.1, new Particle.DustOptions(Color.AQUA, 1.5f));
			}
		}
		server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			for (Block b: barriers) {
				if (b.getType() == Material.BARRIER) {
					b.setType(Material.AIR);
				}
			}
		}, durationTicks);
	}

	@Override
	public long getItemCooldown() {
		return 2000l;
	}
	
	@Override
	public long getPlayerCooldown() {
		return 1000l;
	}
	
	@Override
	public long getAltItemCooldown() {
		return 6000l;
	}

	@Override
	public long getAltPlayerCooldown() {
		return 3000l;
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
	public Material getWandBase() {
		return ModdedItemManager.LESSER_WAND_BASE;
	}

	@Override
	public String getName() {
		return "Wand of Force Field";
	}

}
