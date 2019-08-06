package com.gmail.robbiem.BukkitPluginMain.wands;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

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
import com.gmail.robbiem.BukkitPluginMain.Main;

public class WandOfTrapping extends Wand implements Listener {
	
	public WandOfTrapping(Main plugin) {
		super(plugin);
	}

	List<Trap> traps = new ArrayList<>();

	@Override
	public Material getWandTip() {
		return Material.TRIPWIRE_HOOK;
	}

	@Override
	public boolean use(ItemStack item, Player player, World world, Server server) {
		Block b = player.getTargetBlockExact(7);
		if (b != null) {
			world.spawnParticle(Particle.REDSTONE, b.getLocation().add(0.5, 1, 0.5), 0, 0, 1, 0, 1, new Particle.DustOptions(Color.WHITE, 2f));
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
		Block steppedOn = e.getTo().clone().subtract(0, 1, 0).getBlock();
		OptionalInt trapIndex = IntStream.range(0, traps.size()).filter(i -> traps.get(i).block.equals(steppedOn)).findFirst();
		if (trapIndex.isPresent() && e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
			e.getPlayer().getWorld().playSound(steppedOn.getLocation(), Sound.BLOCK_WOODEN_PRESSURE_PLATE_CLICK_ON, 1, 1);
			e.getPlayer().damage(7);
			traps.get(trapIndex.getAsInt()).placer.sendMessage("One of your traps was activated!");;
			traps.remove(trapIndex.getAsInt());
		}
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
		return "Using this wand on a block\nwill cause the block to spawn\nfangs when stepped on.";
	}

	@Override
	public String getName() {
		return "Wand of Trapping";
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
