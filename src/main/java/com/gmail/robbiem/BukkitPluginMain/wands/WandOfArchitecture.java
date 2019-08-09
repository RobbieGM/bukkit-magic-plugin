package com.gmail.robbiem.BukkitPluginMain.wands;

import java.util.ArrayList;
import java.util.List;
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

import net.md_5.bungee.api.ChatColor;

public class WandOfArchitecture extends LeftClickableWand {
	
	static final Material MATERIAL = Material.WHITE_CONCRETE;
	
	public WandOfArchitecture(Main plugin) {
		super(plugin);
	}

//	List<TemporaryBlockBuilder> activeWands = new ArrayList<>();

	@Override
	public Material getWandTip() {
		return Material.OAK_PLANKS;
	}

	@Override
	public boolean use(ItemStack item, Player player, World world, Server server) {
//		OptionalInt blockBuilderIndex = IntStream.range(0, activeWands.size()).filter(i -> activeWands.get(i).player.getUniqueId().equals(player.getUniqueId())).findFirst();
//		if (blockBuilderIndex.isPresent()) {
//			TemporaryBlockBuilder wand = activeWands.get(blockBuilderIndex.getAsInt());
//			activeWands.remove(wand);
//			wand.stop();
//		} else {
//		if (!blockBuilderIndex.isPresent()) {
			new TemporaryBlockBuilder(player, plugin, MATERIAL, getItemCooldown(), ModdedItemManager.UNBREAKABLE_AND_SHULKERS);
//			activeWands.add(tbb);
//		}
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
	public long getItemCooldown() {
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

	@Override
	public boolean useAlt(ItemStack item, Player player, World world, Server server) {
		Block b = player.getTargetBlockExact(10);
		if (b != null && b.getType() == MATERIAL) {
			b.setType(Material.AIR);
			return true;
		} else return false;
	}

	@Override
	public long getAltItemCooldown() {
		return 50l;
	}

	@Override
	public long getAltPlayerCooldown() {
		return 0;
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
		server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			server.getScheduler().cancelTask(taskId);
		}, 20 * effectLength / 1000);
	}
	
	void stop() {
		player.getServer().getScheduler().cancelTask(taskId);
	}

	void doTick() {
		Vector offset = player.getLocation().getDirection().multiply(4);
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
	
	void drawStar(Location location, Material type) {
		setBlock(location.clone().add(0, 0, 0), type, true);
		setBlock(location.clone().add(0, 1, 0), type, true);
		setBlock(location.clone().add(0, -1, 0), type, true);
		setBlock(location.clone().add(1, 0, 0), type, true);
		setBlock(location.clone().add(-1, 0, 0), type, true);
		setBlock(location.clone().add(0, 0, 1), type, true);
		setBlock(location.clone().add(0, 0, -1), type, true);
	}
	
	boolean blockWouldSuffocatePlayer(Block block) {
		return block.getWorld().getPlayers().stream().anyMatch(player -> player.getEyeLocation().getBlock().equals(block));
	}
	
	void setBlock(Location location, Material type, boolean ignoreSolidBlocks) {
		Block block = location.getBlock();
		if (!unbreakable.contains(block.getType()) && !blockWouldSuffocatePlayer(block)) {
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
