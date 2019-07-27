package com.gmail.robbiem.BukkitPluginMain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;
import org.bukkit.util.Vector;

public class GoCommand implements CommandExecutor {
	JavaPlugin plugin;
	static final int CHESTS_PER_PLAYER = 12;
	static final int BORDER_WIDTH = 200;
	static final int BORDER_RANDOMIZATION_RADIUS = 30;
	static final int MIN_CHEST_DISTANCE = 10;
	static final List<Material> TREE_COVER_BLOCKS = Arrays.asList(Material.BIRCH_LEAVES, Material.ACACIA_LEAVES, Material.DARK_OAK_LEAVES, Material.JUNGLE_LEAVES, Material.OAK_LEAVES, Material.SPRUCE_LEAVES, Material.RED_MUSHROOM_BLOCK, Material.BROWN_MUSHROOM_BLOCK);
	static final double X_CENTER = 933.5;
	static final double Z_CENTER = 1505.5;
	static final List<Vector> PEDESTALS = Arrays.asList(
			new Vector(X_CENTER, 74, Z_CENTER + 7),
			new Vector(X_CENTER, 74, Z_CENTER - 7),
			new Vector(X_CENTER - 7, 74, Z_CENTER),
			new Vector(X_CENTER + 7, 74, Z_CENTER),
			new Vector(X_CENTER + 6, 74, Z_CENTER + 6),
			new Vector(X_CENTER - 6, 74, Z_CENTER + 6),
			new Vector(X_CENTER + 6, 74, Z_CENTER - 6),
			new Vector(X_CENTER - 6, 74, Z_CENTER - 6)
			);
	static final double CHANCE_OF_CHEST_STAYING_ON_TREE = 0;
	int enableFriendlyFireTask = -1;
	int shrinkBorderTask = -1;
	long commandLastUsed = -1;
	
	public GoCommand(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (commandLastUsed != -1 && new Date().getTime() - commandLastUsed < 1000 * 5) {
			sender.sendMessage("This command has already been used and has a 5 second cooldown to prevent it from being accidentally used twice.");
			return false;
		}
		commandLastUsed = new Date().getTime();
		World world;
		if (sender instanceof BlockCommandSender) {
			world = ((BlockCommandSender) sender).getBlock().getWorld();
		} else if (sender instanceof Player) {
			world = ((Player) sender).getWorld();
		} else {
			world = sender.getServer().getWorld(Main.HUNGER_GAMES_WORLD);
		}
		for (Player p: world.getPlayers()) {
			p.getInventory().clear();
			p.setGameMode(GameMode.SURVIVAL);
			p.setTotalExperience(10000);
			p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 40, 3), true);
			p.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 40, 3), true);
		}
		world.getWorldBorder().setSize(BORDER_WIDTH);
		initTeams(sender.getServer(), world);
		world.setPVP(false);
		initPlayerPositions(PEDESTALS.stream().map(v -> v.toLocation(world)).collect(Collectors.toList()), world.getPlayers());
		world.setTime(0);
		execute("tellraw @a {\"text\": \"May the odds be ever in your favor!\", \"bold\": true, \"color\": \"red\"}");
		double borderCenterX = 933.5 - BORDER_RANDOMIZATION_RADIUS + 2 * BORDER_RANDOMIZATION_RADIUS * Math.random();
		double borderCenterZ = 1505.5 - BORDER_RANDOMIZATION_RADIUS + 2 * BORDER_RANDOMIZATION_RADIUS * Math.random();
		world.getWorldBorder().setCenter(borderCenterX, borderCenterZ);
		int onlinePlayers = world.getPlayers().size();
		placeChestsRandomly(world, CHESTS_PER_PLAYER * onlinePlayers, borderCenterX, borderCenterZ, BORDER_WIDTH);
		BukkitScheduler scheduler = plugin.getServer().getScheduler();
		
		if (enableFriendlyFireTask != -1) scheduler.cancelTask(enableFriendlyFireTask);
		if (shrinkBorderTask != -1) scheduler.cancelTask(shrinkBorderTask);
		enableFriendlyFireTask = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			world.setPVP(true);
			for (Player p: world.getPlayers()) {
				List<Material> freeItems = Arrays.asList(Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS, Material.ENDER_PEARL);
				p.getInventory().addItem(freeItems.stream().map((Material m) -> new ItemStack(m, 1)).toArray(ItemStack[]::new));
			}
		}, 20 * 60 * 2);
		shrinkBorderTask = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			world.getWorldBorder().setSize(1, 1000);
			plugin.getServer().broadcastMessage("The world border is shrinking!");
		}, 20 * 60 * 5);
		return true;
	}
	
	void initTeams(Server server, World world) {
		Scoreboard s = server.getScoreboardManager().getMainScoreboard();
		for (Player p: world.getPlayers()) {
			String name = p.getName();
			Team newTeam = s.registerNewTeam(name);
			newTeam.addEntry(name);
			newTeam.setOption(Option.NAME_TAG_VISIBILITY, OptionStatus.NEVER);
		}
	}
	
	void initPlayerPositions(List<Location> pedestals, List<Player> players) {
		Collections.shuffle(pedestals);
		for (int i = 0; i < Math.min(pedestals.size(), players.size()); i++) {
			players.get(i).teleport(pedestals.get(i));
		}
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
