package com.gmail.robbiem.BukkitPluginMain.wands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.RayTraceResult;

public class WandOfLightning extends Wand {

	@Override
	public void use(ItemStack wandItem, Player player, World world, JavaPlugin plugin, Server server) {
		RayTraceResult rayTrace = player.rayTraceBlocks(100);
		if (rayTrace != null && rayTrace.getHitBlock() != null) {
			Location loc = rayTrace.getHitPosition().toLocation(world);
			world.strikeLightning(loc);
		}
	}

	@Override
	public long getCooldown() {
		return 2000l;
	}

	@Override
	public ShapedRecipe getCraftingRecipeFromResultingItem(ShapedRecipe startingRecipe) {
		return startingRecipe.shape("  t", " s ", "p  ").setIngredient('t', Material.TRIDENT).setIngredient('s', Material.STICK).setIngredient('p', Material.ENDER_PEARL);
	}

	@Override
	public String getLore() {
		return "Makes lightning strike where\nyou're looking";
	}

}
