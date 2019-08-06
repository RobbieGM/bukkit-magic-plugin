package com.gmail.robbiem.BukkitPluginMain.wands;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.gmail.robbiem.BukkitPluginMain.Main;
import com.gmail.robbiem.BukkitPluginMain.ModdedItemManager;

public class WandOfArchitecture extends Wand {
	
	public WandOfArchitecture(Main plugin) {
		super(plugin);
	}

	List<TemporaryBlockBuilder> activeWands = new ArrayList<>();

	@Override
	public Material getWandTip() {
		return Material.OAK_PLANKS;
	}

	@Override
	public boolean use(ItemStack item, Player player, World world, Server server) {
		OptionalInt blockBuilderIndex = IntStream.range(0, activeWands.size()).filter(i -> activeWands.get(i).player.getUniqueId().equals(player.getUniqueId())).findFirst();
		if (blockBuilderIndex.isPresent()) {
			TemporaryBlockBuilder wand = activeWands.get(blockBuilderIndex.getAsInt());
			activeWands.remove(wand);
			wand.stop();
		} else {
			TemporaryBlockBuilder tbb = new TemporaryBlockBuilder(player, plugin, Material.WHITE_CONCRETE, getPlayerCooldown(), ModdedItemManager.UNBREAKABLE_AND_SHULKERS);
			activeWands.add(tbb);
		}
		return true;
	}
	
	@Override
	public boolean isWeapon() {
		return false;
	}

	@Override
	public long getPlayerCooldown() {
		return 0;
	}

	@Override
	public String getLore() {
		return "Right-click this wand to toggle its building\nability--the hotbar slot controls distance,\nbut the first slot erases.";
	}

	@Override
	public String getName() {
		return "Wand of Architecture";
	}

}

class TemporaryBlockBuilder {
	
	Location lastLocation = null;
	Player player;
	Material material;
	int taskId;
	List<Material> unbreakable;
	
	public TemporaryBlockBuilder(Player player, JavaPlugin plugin, Material material, long effectLength, List<Material> unbreakable) {
		Server server = plugin.getServer();
		this.player = player;
		this.material = material;
		this.unbreakable = unbreakable;
		taskId = server.getScheduler().scheduleSyncRepeatingTask(plugin, this::doTick, 0, 1);
//		server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
//			server.getScheduler().cancelTask(taskId);
//		}, 20 * effectLength / 1000);
	}
	
	void stop() {
		player.getServer().getScheduler().cancelTask(taskId);
	}
	
	void doTick() {
		int maxDist = 30;
		double fraction = player.getInventory().getHeldItemSlot() / 8.0;
		boolean isFirstSlot = fraction == 0.0;
		if (isFirstSlot) {
			Block b = player.getTargetBlockExact(maxDist);
			if (b != null && b.getType() == material) {
				setBlock(b.getLocation(), Material.AIR, false);
			}
			lastLocation = null;
		} else {
			Vector offset = player.getLocation().getDirection().multiply(maxDist * fraction);
			Location l = player.getLocation().add(offset);
			List<Block> blocks = new ArrayList<>();
			if (lastLocation != null) {
				blocks.addAll(getBlocksBetween(l, lastLocation));
			}
			blocks.add(l.getBlock());
			blocks.forEach(b -> {
				drawStar(b.getLocation(), material);
			});
			lastLocation = l;
		}
	}
	
	void drawStar(Location location, Material type) {
		setBlock(location.clone().add(0, 0, 0), type, true);
		setBlock(location.clone().add(0, 1, 0), type, true);
		setBlock(location.clone().add(0, -1, 0), type, true);
		setBlock(location.clone().add(1, 0, 0), type, true);
		setBlock(location.clone().add(-1, 0, 0), type, true);
		setBlock(location.clone().add(0, 0, 1), type, true);
		setBlock(location.clone().add(0, 0, -1), type, true);
	}
	
	void setBlock(Location location, Material type, boolean ignoreSolidBlocks) {
		Block block = location.getBlock();
		if (!unbreakable.contains(block.getType())) {
			if (type == Material.AIR || !(block.getType().isSolid() && ignoreSolidBlocks)) {
				block.setType(type);
			}
		}
	}
	
	List<Block> getBlocksBetween(Location from, Location to) {
		List<Block> blocks = new ArrayList<>();
		Vector difference = to.clone().subtract(from).toVector();
		for (int i = 0; i <= difference.length(); i++) {
			Location blockLoc = from.clone().add(difference.clone().normalize().multiply(i));
			blocks.add(blockLoc.getBlock());
		}
		// May not reach final block
		blocks.add(to.getBlock());
		return blocks;
	}
}
