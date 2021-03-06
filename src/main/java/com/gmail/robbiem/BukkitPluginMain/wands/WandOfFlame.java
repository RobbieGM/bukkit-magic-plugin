package com.gmail.robbiem.BukkitPluginMain.wands;

import com.gmail.robbiem.BukkitPluginMain.Main;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.inventory.ItemStack;

public class WandOfFlame extends LeftClickableWand {

	public WandOfFlame(Main plugin) {
		super(plugin);
	}

	@Override
	public boolean use(ItemStack wandItem, Player player, World world, Server server) {
		Location loc = player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(6))
				.toLocation(world, player.getLocation().getYaw(), player.getLocation().getPitch());
		LargeFireball fireball = world.spawn(loc, LargeFireball.class);
		fireball.setGravity(true);
		fireball.setYield(isBuffed ? 2.5f : 2.0f);
		fireball.setShooter(player);
		world.playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1, 1.4f);
		return true;
	}

	@Override
	public boolean useAlt(ItemStack item, Player player, World world, Server server) {
		Location loc = player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(2))
				.toLocation(world, player.getLocation().getYaw(), player.getLocation().getPitch());
		SmallFireball fireball = world.spawn(loc, SmallFireball.class);
		fireball.setGravity(true);
		fireball.setShooter(player);
		world.playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1, 1.7f);
		return true;
	}

	@Override
	public long getPlayerCooldown() {
		return isBuffed ? 800l : 1000l;
	}

	@Override
	public long getAltPlayerCooldown() {
		return 250l;
	}

	@Override
	public String getLore() {
		return "Launches a fireball in the\ndirection you're looking";
	}

	@Override
	public Material getWandTip() {
		return Material.BLAZE_ROD;
	}

	@Override
	public String getName() {
		return "Wand of Flame";
	}

}
