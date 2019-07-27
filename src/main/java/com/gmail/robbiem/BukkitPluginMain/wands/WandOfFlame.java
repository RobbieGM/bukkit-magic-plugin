package com.gmail.robbiem.BukkitPluginMain.wands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public class WandOfFlame extends Wand {

	@Override
	public void use(ItemStack wandItem, Player player, World world, JavaPlugin plugin, Server server) {
		Location loc = player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(6)).toLocation(world, player.getLocation().getYaw(), player.getLocation().getPitch());
		LargeFireball fireball = world.spawn(loc, LargeFireball.class);
		fireball.setYield(1.6f);
		fireball.setShooter(player);
		world.playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1, 1.5f);
	}

	@Override
	public long getCooldown() {
		return 1250l;
	}

	@Override
	public ShapedRecipe getCraftingRecipeFromResultingItem(ShapedRecipe startingRecipe) {
		return startingRecipe.shape("  t", " r ", "p  ").setIngredient('p', Material.ENDER_PEARL).setIngredient('r', Material.BLAZE_ROD).setIngredient('t', Material.TORCH);
	}

	@Override
	public String getLore() {
		return "Launches a fireball in the\ndirection you're looking";
	}

}
