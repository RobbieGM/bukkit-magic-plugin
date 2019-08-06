package com.gmail.robbiem.BukkitPluginMain.scrolls;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Barrel;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.gmail.robbiem.BukkitPluginMain.Main;
import com.gmail.robbiem.BukkitPluginMain.ModdedItemManager;

public class ScrollOfOrganization extends Scroll implements Listener {
	
	public ScrollOfOrganization(Main plugin) {
		super(plugin);
	}

	List<Block> breakoutBlocks = new ArrayList<>();

	@Override
	public Material getCraftingRecipeCenterItem() {
		return Material.CRAFTING_TABLE;
	}

	@Override
	public boolean use(ItemStack item, Player player, World world, Server server) {
		Location l = player.getLocation();
		l.add(0, -4, 0);
		player.teleport(l.clone().add(0, 0.1875, 0)); // Trapdoor height
		SetBlockFunction set = (int x, int y, int z, Material type) -> {
			Block block = world.getBlockAt(l.clone().add(x, y, z));
			if (!ModdedItemManager.UNBREAKABLE_AND_SHULKERS.contains(block.getType())) {
				block.setType(type);
			}
		};
		set.run(0, 0, 0, Material.OAK_TRAPDOOR);
		set.run(0, 1, 0, Material.AIR);
		breakoutBlocks.add(world.getBlockAt(l.clone().add(0, 2, 0)));
		set.run(1, 0, 0, Material.BARREL);
		set.run(1, 1, 0, Material.BARREL);
		set.run(0, 0, -1, Material.ANVIL);
		set.run(0, 1, -1, Material.ENCHANTING_TABLE);
		set.run(-1, 0, 0, Material.SEA_LANTERN);
		set.run(-1, 1, 0, Material.GRINDSTONE);
		set.run(0, 0, 1, Material.FURNACE);
		set.run(0, 1, 1, Material.CRAFTING_TABLE);
		for (int i = -1; i >= -20; i--) {
			set.run(0, i, 0, Material.LADDER);
		}
		Barrel upperBarrel = (Barrel) world.getBlockAt(l.clone().add(1, 1, 0)).getState();
		upperBarrel.getInventory().addItem(new ItemStack(Material.LAPIS_LAZULI, 64));
		return true;
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if (breakoutBlocks.contains(e.getBlock()) && e.getPlayer() != null) {
			breakoutBlocks.remove(e.getBlock());
			e.getPlayer().setVelocity(new Vector(0, 1, 0));
			Block topBlock = e.getBlock().getLocation().add(0, 1, 0).getBlock();
			Material type = topBlock.getType();
			if (!ModdedItemManager.UNBREAKABLE_AND_SHULKERS.contains(type)) {
				topBlock.setType(Material.AIR);
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
					topBlock.setType(type);
				}, 10);
			}
		}
	}
	
	@Override
	public boolean isEventHandler() {
		return true;
	}

	@Override
	public long getPlayerCooldown() {
		return 0;
	}

	@Override
	public String getLore() {
		return "This scroll will teleport you\nunderground with everything you need\nto organize your inventory!";
	}

	@Override
	public String getName() {
		return "Scroll of Organization";
	}

}

interface SetBlockFunction {
	void run(int offsetX, int offsetY, int offsetZ, Material type);
}
