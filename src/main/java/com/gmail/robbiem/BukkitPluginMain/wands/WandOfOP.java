package com.gmail.robbiem.BukkitPluginMain.wands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import com.gmail.robbiem.BukkitPluginMain.Main;

public class WandOfOP extends Wand {

	public WandOfOP(Main plugin) {
		super(plugin);
	}

	@Override
	public boolean use(ItemStack wandItem, Player player, World world, Server server) {
		Location loc = player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(6)).toLocation(world, player.getLocation().getYaw(), player.getLocation().getPitch());
		LargeFireball fireball = world.spawn(loc, LargeFireball.class);
		fireball.setYield(10f);
		fireball.setShooter(player);
		return true;
	}

	@Override
	public long getPlayerCooldown() {
		return 250l;
	}

	@Override
	public String getLore() {
		return "It's pretty OP";
	}

	@Override
	public Material getWandTip() {
		return Material.BEDROCK;
	}

	@Override
	public String getName() {
		return "Wand of OP";
	}
	
}
