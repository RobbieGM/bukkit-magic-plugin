package com.gmail.robbiem.BukkitPluginMain.wands;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public class WandOfOP extends Wand {

	@Override
	public void use(ItemStack wandItem, Player player, World world, JavaPlugin plugin, Server server) {
		Location loc = player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(6)).toLocation(world, player.getLocation().getYaw(), player.getLocation().getPitch());
		LargeFireball fireball = world.spawn(loc, LargeFireball.class);
		fireball.setYield(10f);
		fireball.setShooter(player);
	}

	@Override
	public long getCooldown() {
		// TODO Auto-generated method stub
		return 250l;
	}

	@Override
	public ShapedRecipe getCraftingRecipeFromResultingItem(ShapedRecipe startingRecipe) {
		// TODO Auto-generated method stub
		return null;
	}

}
