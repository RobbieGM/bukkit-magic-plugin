package com.gmail.robbiem.BukkitPluginMain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class GoCommand implements CommandExecutor {
	JavaPlugin plugin;
	static final int CHESTS = 40;
	static final int BORDER_WIDTH = 200;
	static final int BORDER_RANDOMIZATION_RADIUS = 30;
	static final int MIN_CHEST_DISTANCE = 10;
	static final List<Material> TREE_COVER_BLOCKS = Arrays.asList(Material.BIRCH_LEAVES, Material.ACACIA_LEAVES, Material.DARK_OAK_LEAVES, Material.JUNGLE_LEAVES, Material.OAK_LEAVES, Material.SPRUCE_LEAVES, Material.RED_MUSHROOM_BLOCK, Material.BROWN_MUSHROOM_BLOCK);
	static final double CHANCE_OF_CHEST_STAYING_ON_TREE = 0;
	int enableFriendlyFireTask = -1;
	int shrinkBorderTask = -1;
	
	public GoCommand(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		World world;
		if (sender instanceof BlockCommandSender) {
			world = ((BlockCommandSender) sender).getBlock().getWorld();
		} else if (sender instanceof Player) {
			world = ((Player) sender).getWorld();
		} else {
			return false;
		}
		execute("clear @a");
		execute("gamemode survival @a");
		execute("worldborder set " + BORDER_WIDTH);
		execute("team modify everyone friendlyFire false");
		execute("tp RobbieGM 933.5 74 1512.5");
		execute("tp OinkMePapi 933.5 74 1498.5");
		execute("tp aNicePlayer 926.5 74 1505.5");
		execute("tp bc_epic_12 940.5 74 1505.5");
		execute("tp Putins_Mixtape_ 939.5 74 1511.5");
		execute("team join everyone @a");
		execute("xp set @a 100 levels");
		execute("time set 0");
		execute("advancement revoke @a everything");
		execute("effect give @a minecraft:instant_health 20");
		execute("effect give @a minecraft:saturation 20");
		execute("tellraw @a {\"text\": \"May the odds be ever in your favor!\", \"bold\": true, \"color\": \"red\"}");
		double borderCenterX = 933.5 - BORDER_RANDOMIZATION_RADIUS + 2 * BORDER_RANDOMIZATION_RADIUS * Math.random();
		double borderCenterZ = 1505.5 - BORDER_RANDOMIZATION_RADIUS + 2 * BORDER_RANDOMIZATION_RADIUS * Math.random();
		world.getWorldBorder().setCenter(borderCenterX, borderCenterZ);
		placeChestsRandomly(world, CHESTS, borderCenterX, borderCenterZ, BORDER_WIDTH);
		BukkitScheduler scheduler = plugin.getServer().getScheduler();
		
		if (enableFriendlyFireTask != -1) scheduler.cancelTask(enableFriendlyFireTask);
		if (shrinkBorderTask != -1) scheduler.cancelTask(shrinkBorderTask);
		enableFriendlyFireTask = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			execute("team modify everyone friendlyFire true");
		}, 20 * 60 * 1);
		shrinkBorderTask = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			execute("worldborder set 1 1000");
		}, 20 * 60 * 5);
		return true;
	}
	
	@SuppressWarnings("deprecation")
	void placeChestsRandomly(World world, int numChests, double borderCenterX, double borderCenterZ, int borderWidth) {
		List<Location> chestLocations = new ArrayList<>();
		int chestsPlaced = 0;
		while (chestsPlaced < numChests) {
			int x = (int) (borderCenterX + (-0.5 + Math.random()) * borderWidth);
			int z = (int) (borderCenterZ + (-0.5 + Math.random()) * borderWidth);
			int y = world.getHighestBlockYAt(x, z);
			boolean isOnTree = TREE_COVER_BLOCKS.contains(world.getBlockAt(x, y - 1, z).getType());
			boolean shouldLeaveItOnTree = Math.random() < CHANCE_OF_CHEST_STAYING_ON_TREE;
			if (isOnTree && !shouldLeaveItOnTree) { // Try to go below tree, if can't find a block within 20 down from original spot, then leave it
				for (int down = 2; down < 20; down++) {
					if (!world.getBlockAt(x, y - down, z).getType().isSolid() && world.getBlockAt(x, y - down - 1, z).getType().isSolid() && !TREE_COVER_BLOCKS.contains(world.getBlockAt(x, y - down - 1, z).getType())) {
						y = y - down;
						break;
					}
				}
			}
			Location loc = new Location(world, x, y, z);
			boolean chestIsInMiddle = x >= 923 && x <= 943 && z >= 1495 && z <= 1515;
			if (chestIsTooCloseToOthers(chestLocations, loc) || chestIsInMiddle) continue;
			Block chestBlock = world.getBlockAt(loc);
			chestBlock.setType(Material.CHEST);
			chestLocations.add(chestBlock.getLocation());
			Chest chest = (Chest) chestBlock.getState();
			chest.setLootTable(plugin.getServer().getLootTable(new NamespacedKey("hungergames", "chests/loot_table")));
			chest.update();
			chestsPlaced++;
		}
	}
	
	boolean chestIsTooCloseToOthers(List<Location> chestLocations, Location newLocation) {
		for (Location chestLocation: chestLocations) {
			if (newLocation.distanceSquared(chestLocation) < Math.pow(MIN_CHEST_DISTANCE, 2)) {
				return true;
			}
		}
		return false;
	}

	void execute(String command) {
		plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command);
	}
}
