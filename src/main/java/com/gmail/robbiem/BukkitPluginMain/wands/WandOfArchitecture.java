package com.gmail.robbiem.BukkitPluginMain.wands;

import java.util.ArrayList;

import com.gmail.robbiem.BukkitPluginMain.Main;
import com.gmail.robbiem.BukkitPluginMain.ModdedItemManager;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class WandOfArchitecture extends LeftClickableWand {

	public WandOfArchitecture(Main plugin) {
		super(plugin);
	}

	@Override
	public Material getWandTip() {
		return Material.OAK_PLANKS;
	}

	@Override
	public boolean use(ItemStack item, Player player, World world, Server server) {
		float pitch = -player.getLocation().getPitch();
		Block standingOn = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
		if (-20 <= pitch && pitch <= 20) { // Planks going outward
			makePlanksOutward(standingOn, player.getLocation().getDirection().setY(0).normalize());
		} else if (-80 <= pitch && pitch < -20) {
			makeStairs(standingOn, player.getFacing(), false);
		} else if (20 < pitch && pitch <= 80) {
			makeStairs(standingOn, player.getFacing(), true);
		} else if (pitch < -80) {
			makeRoomAround(standingOn);
		} else if (pitch > 80) {
			makeLadder(player.getLocation().getBlock(), player.getFacing());
		}
		return true;
	}

	void makePlanksOutward(Block start, Vector direction) {
		for (float dist = 0; dist < 10; dist++) {
			final float d = dist;
			setTimeout(() -> {
				Location center = start.getLocation().add(direction.clone().multiply(d));
				for (int x = -1; x <= 1; x++) {
					for (int z = -1; z <= 1; z++) {
						setBlock(center.clone().add(x, 0, z).getBlock(), Material.OAK_PLANKS);
					}
				}
			}, (int) (dist * 15 / 10));
		}
	}

	void makeStairs(Block firstStairBlock, BlockFace facing, boolean up) {
		for (int i = 0; i < 7; i++) {
			final Block current = firstStairBlock.getRelative(facing, i).getRelative(up ? BlockFace.UP : BlockFace.DOWN, i);
			setTimeout(() -> {
				BlockFace[] adjacentFaces = getSideBlockFaces(facing);
				Block left = current.getRelative(adjacentFaces[0]);
				Block right = current.getRelative(adjacentFaces[1]);
				Block[] blocks = new Block[] { current, left, right };
				for (Block block : blocks) {
					block.setType(Material.OAK_STAIRS);
					Stairs stairData = ((Stairs) block.getBlockData());
					stairData.setFacing(up ? facing : facing.getOppositeFace());
					block.setBlockData(stairData);
				}
			}, i * 8 / 7);
		}
	}

	void makeLadder(Block bottomLadderBlock, BlockFace facing) {

	}

	void makeRoomAround(Block bottomCenter) {
		double FARTHEST_BLOCK_DIST = Math.sqrt(3 * 3 + 3 * 3 + 4 * 4);
		ArrayList<Block> blocks = new ArrayList<>();
		for (int x = -3; x <= 3; x++) {
			for (int y = 0; y <= 4; y++) {
				for (int z = -3; z <= 3; z++) {
					boolean isOnEdge = y == 0 || y == 4 || x == -3 || x == 3 || z == -3 || z == 3;
					boolean isWindow = y == 2 && (x == 0 || z == 0);
					if (isOnEdge && !isWindow) {
						blocks.add(bottomCenter.getLocation().add(x, y, z).getBlock());
					}
				}
			}
		}
		for (Block block : blocks) {
			double dist = block.getLocation().distanceSquared(bottomCenter.getLocation());
			setTimeout(() -> {
				setBlock(block, Material.OAK_PLANKS);
			}, (int) (40 * dist / (FARTHEST_BLOCK_DIST * FARTHEST_BLOCK_DIST)));
		}
	}

	@Override
	public boolean isWeapon() {
		return false;
	}

	@Override
	public long getItemCooldown() {
		return 1000l;
	}

	@Override
	public long getPlayerCooldown() {
		return 500l;
	}

	@Override
	public boolean useAlt(ItemStack item, Player player, World world, Server server) {
		Block b = player.getTargetBlockExact(10);
		boolean canBreak = b != null && (b.getType() == Material.OAK_PLANKS || b.getType() == Material.OAK_STAIRS
				|| (isBuffed && !ModdedItemManager.UNBREAKABLE_AND_SHULKERS.contains(b.getType())));
		if (canBreak) {
			b.setType(Material.AIR);
			return true;
		} else
			return false;
	}

	@Override
	public long getAltItemCooldown() {
		return 1000l;
	}

	@Override
	public long getAltPlayerCooldown() {
		return 500l;
	}

	@Override
	public String getLore() {
		return "Hold right-click on this wand to\nconstantly build in front\nof you.";
	}

	@Override
	public String getName() {
		return "Wand of Architecture";
	}

	void setTimeout(Runnable task, int tickDelay) {
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, task, tickDelay);
	}

	void setBlock(Block block, Material type) {
		if (!ModdedItemManager.UNBREAKABLE_AND_SHULKERS.contains(block.getType())) {
			block.setType(type);
		}
	}

	BlockFace[] getSideBlockFaces(BlockFace face) {
		switch (face) {
		case NORTH:
			return new BlockFace[] { BlockFace.WEST, BlockFace.EAST };
		case EAST:
			return new BlockFace[] { BlockFace.NORTH, BlockFace.SOUTH };
		case SOUTH:
			return new BlockFace[] { BlockFace.EAST, BlockFace.WEST };
		case WEST:
			return new BlockFace[] { BlockFace.SOUTH, BlockFace.NORTH };
		default:
			return new BlockFace[] {};
		}
	}

}

// class TemporaryBlockBuilder {

// Location lastLocation = null;
// Player player;
// Material material;
// JavaPlugin plugin;
// int taskId;
// List<Material> unbreakable;

// public TemporaryBlockBuilder(Player player, JavaPlugin plugin, Material
// material, long effectLength,
// List<Material> unbreakable) {
// Server server = plugin.getServer();
// this.player = player;
// this.material = material;
// this.unbreakable = unbreakable;
// this.plugin = plugin;
// taskId = server.getScheduler().scheduleSyncRepeatingTask(plugin,
// this::doTick, 0, 1);
// server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
// server.getScheduler().cancelTask(taskId);
// }, 20 * effectLength / 1000);
// }

// void stop() {
// player.getServer().getScheduler().cancelTask(taskId);
// }

// void doTick() {
// Vector offset = player.getLocation().getDirection().multiply(4);
// Location l = player.getLocation().add(offset);
// List<Block> blocks = new ArrayList<>();
// if (lastLocation != null) {
// blocks.addAll(getBlocksBetween(l, lastLocation));
// }
// blocks.add(l.getBlock());
// blocks.forEach(b -> {
// drawStar(b.getLocation(), material);
// });
// player.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
// blocks.forEach(b -> {
// drawStar(b.getLocation(), Material.AIR);
// });
// }, 20 * 20);
// lastLocation = l;
// }

// void drawStar(Location location, Material type) {
// setBlock(location.clone().add(0, 0, 0), type, true);
// // setBlock(location.clone().add(0, 1, 0), type, true);
// // setBlock(location.clone().add(0, -1, 0), type, true);
// setBlock(location.clone().add(1, 0, 0), type, true);
// setBlock(location.clone().add(-1, 0, 0), type, true);
// setBlock(location.clone().add(0, 0, 1), type, true);
// setBlock(location.clone().add(0, 0, -1), type, true);
// }

// boolean blockWouldSuffocatePlayer(Block block) {
// return block.getWorld().getPlayers().stream().anyMatch(player ->
// player.getEyeLocation().getBlock().equals(block));
// }

// void setBlock(Location location, Material type, boolean ignoreSolidBlocks) {
// Block block = location.getBlock();
// if (!unbreakable.contains(block.getType()) &&
// !blockWouldSuffocatePlayer(block)) {
// if (type == Material.AIR || !(block.getType().isSolid() &&
// ignoreSolidBlocks)) {
// block.setType(type);
// }
// }
// }

// List<Block> getBlocksBetween(Location from, Location to) {
// List<Block> blocks = new ArrayList<>();
// Vector difference = to.clone().subtract(from).toVector();
// for (int i = 0; i <= difference.length(); i++) {
// Location blockLoc =
// from.clone().add(difference.clone().normalize().multiply(i));
// blocks.add(blockLoc.getBlock());
// }
// // May not reach final block
// blocks.add(to.getBlock());
// return blocks;
// }
// }
