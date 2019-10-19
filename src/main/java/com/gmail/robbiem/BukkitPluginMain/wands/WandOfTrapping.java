package com.gmail.robbiem.BukkitPluginMain.wands;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.gmail.robbiem.BukkitPluginMain.Main;
import com.gmail.robbiem.BukkitPluginMain.ModdedItemManager;

public class WandOfTrapping extends LeftClickableWand implements Listener {

	static final double TRAP_RANGE = 3;
	
	public WandOfTrapping(Main plugin) {
		super(plugin);
	}

	List<Trap> traps = new ArrayList<>();

	@Override
	public boolean use(ItemStack item, Player player, World world, Server server) {
		Block b = player.getTargetBlockExact(7);
		if (b != null) {
			if (b.getType() == Material.GRASS) {
				b = b.getLocation().add(0, -1, 0).getBlock();
			}
			world.spawnParticle(Particle.REDSTONE, b.getLocation().add(0.5, 1, 0.5), 0, 0, 1, 0, 1, new Particle.DustOptions(Color.WHITE, 2f));
			List<Trap> playerTraps = traps.stream().filter(trap -> trap.placer.equals(player)).collect(Collectors.toList());
			if (playerTraps.size() >= 4) {
				traps.remove(playerTraps.get(0));
			}
			traps.add(new Trap(b, player));
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean isEventHandler() {
		return true;
	}
	
	@EventHandler
	public void onPlayerStepOnBlock(PlayerMoveEvent e) {
		Trap trap = traps.stream().filter(t -> t.block.getWorld().equals(e.getTo().getWorld()) && t.block.getLocation().distanceSquared(e.getTo()) < TRAP_RANGE * TRAP_RANGE).findAny().orElse(null);
		if (trap != null && e.getPlayer().getGameMode() == GameMode.SURVIVAL && !e.getPlayer().equals(trap.placer)) {
			boolean isProtected = e.getPlayer().getInventory().getBoots() == null;
			e.getPlayer().getWorld().playSound(trap.block.getLocation(), Sound.BLOCK_WOODEN_PRESSURE_PLATE_CLICK_ON, 1, 1);
			e.getPlayer().damage(isProtected ? 2 : 6);
			e.getPlayer().setVelocity(new Vector(-0.5 + Math.random(), 0.5, -0.5 + Math.random()).multiply(isBuffed ? 2 : 1));
			trap.placer.sendMessage("One of your traps was activated!");;
			traps.remove(trap);
		}
	}

	@Override
	public long getPlayerCooldown() {
		return 0;
	}
	
	@Override
	public long getItemCooldown() {
		return 250l;
	}
	
	@Override
	public Material getWandTip() {
		return Material.TRIPWIRE_HOOK;
	}
	
	@Override
	public Material getWandBase() {
		return ModdedItemManager.LESSER_WAND_BASE;
	}

	@Override
	public String getLore() {
		return "Using this wand on a block\nwill cause the block to spawn\nfangs when stepped on.";
	}

	@Override
	public String getName() {
		return "Wand of Trapping";
	}

	@Override
	public boolean useAlt(ItemStack item, Player player, World world, Server server) {
		boolean foundTrap = false;
		for (Trap trap: traps) {
			if (trap.block.getLocation().getWorld().equals(player.getWorld()) && trap.block.getLocation().distanceSquared(player.getLocation()) < 10 * 10) {
				foundTrap = true;
				if (trap.placer.equals(player)) {
					world.spawnParticle(Particle.REDSTONE, trap.block.getLocation().add(0.5, 1, 0.5), 0, 0, 1, 0, 1,
							new Particle.DustOptions(Color.WHITE, 2f));
				} else {
					world.playSound(trap.block.getLocation(), Sound.BLOCK_WOODEN_PRESSURE_PLATE_CLICK_ON, 1, 1);
					traps.remove(trap);
				}
			}
		}
		return foundTrap;
	}

	@Override
	public long getAltPlayerCooldown() {
		return 3000l;
	}

}

class Trap {
	Block block;
	Player placer;
	public Trap(Block block, Player placer) {
		this.block = block;
		this.placer = placer;
	}
}
