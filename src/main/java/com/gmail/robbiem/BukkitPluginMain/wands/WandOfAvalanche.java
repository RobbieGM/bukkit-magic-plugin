package com.gmail.robbiem.BukkitPluginMain.wands;

import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import com.gmail.robbiem.BukkitPluginMain.Main;
import com.gmail.robbiem.BukkitPluginMain.ModdedItemManager;

public class WandOfAvalanche extends Wand implements Listener {
	public WandOfAvalanche(Main plugin) {
		super(plugin);
	}

	static final double RADIUS = 5;

	@Override
	public boolean use(ItemStack wandItem, Player player, World world, Server server) {
		Location playerLocation = player.getLocation();
		world.playSound(playerLocation, Sound.BLOCK_ENDER_CHEST_OPEN, 0.5f, 0.5f);
		ArrayList<Block> fallingBlocks = new ArrayList<>();
		for (int x = (int) -RADIUS; x <= RADIUS; x++) {
			for (int z = (int) -RADIUS; z <= RADIUS; z++) {
				Location l = playerLocation.clone().add(x, 0, z);
				double edgeJaggedness = Math.random();
				double distance = l.distance(playerLocation);
				if (distance > RADIUS - edgeJaggedness) continue;
				l.setY(world.getHighestBlockYAt(l) - 1);
				Material previousMaterial = l.getBlock().getType();
				if (previousMaterial == Material.AIR) continue;
				fallingBlocks.add(l.getBlock());
			}
		}
		int particleSpawnerTaskId = server.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
			for (int i = 0; i < 5; i++) {
				playerLocation.clone().add(new Vector(-RADIUS + Math.random() * RADIUS * 2, 0, -RADIUS + Math.random() * RADIUS * 2));
				Block randomBlock = fallingBlocks.get((int) (fallingBlocks.size() * Math.random()));
				world.spawnParticle(Particle.BLOCK_CRACK, randomBlock.getLocation(), 3, 0.2, 0.2, 0.2, server.createBlockData(randomBlock.getType()));
			}
		}, 0, 1);
		BukkitScheduler scheduler = server.getScheduler();
		scheduler.scheduleSyncDelayedTask(plugin, () -> {
			server.getScheduler().cancelTask(particleSpawnerTaskId);
			for (Block block: fallingBlocks) {
				Location blockLocation = block.getLocation().add(0.5, 0, 0.5);
				double distance = taxicabDistance(playerLocation, blockLocation);
				double delay = distance / 3;
				scheduler.scheduleSyncDelayedTask(plugin, () -> {
					Material previousMaterial = block.getType();
					createColumnOfAirTo(blockLocation);
					world.spawnParticle(Particle.BLOCK_CRACK, blockLocation, 3, 0.2, 0.2, 0.2, server.createBlockData(previousMaterial));
					scheduler.scheduleSyncDelayedTask(plugin, () -> {
						FallingBlock fallingBlock = world.spawnFallingBlock(blockLocation, server.createBlockData(previousMaterial));
						fallingBlock.setVelocity(new Vector(0, -1 + 0.6 * Math.random(), 0));
						fallingBlock.setMetadata("shouldNotSolidify", new FixedMetadataValue(plugin, true));
					}, 4);
				}, (long) (20 * delay));
			}
		}, (long) (20 * 0.5));
		return true;
	}

	@Override
	public long getPlayerCooldown() {
		return 2500l;
	}
	
	double taxicabDistance(Location l1, Location l2) {
		return 0.707 * (Math.abs(l1.getX() - l2.getX()) + Math.abs(l1.getZ() - l2.getZ()));
	}
	
	void createColumnOfAirTo(Location location) {
		for (int y = 0; y <= location.getY(); y++) {
			Location currentLocation = location.clone();
			currentLocation.setY(y);
			Block block = currentLocation.getBlock();
			if (!ModdedItemManager.UNBREAKABLE_AND_SHULKERS.contains(block.getType()))
				block.setType(Material.AIR);
		}
	}
	
	/**
	 * To fix bug where blocks seem to land by sticking to the sides of other blocks.
	 * @param e
	 */
	@EventHandler
	public void onEntityChangeBlock(EntityChangeBlockEvent e) {
		if (e.getEntity() instanceof FallingBlock && e.getEntity().hasMetadata("shouldNotSolidify")) {
			e.setCancelled(true);
		}
	}
	
	@Override
	public boolean isEventHandler() {
		return true;
	}

	@Override
	public String getLore() {
		return "Waits half a second, then opens a\ngaping hole in the ground";
	}

	@Override
	public Material getWandTip() {
		return Material.COBBLESTONE;
	}

	@Override
	public String getName() {
		return "Wand of Avalanche";
	}

}
