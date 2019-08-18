package com.gmail.robbiem.BukkitPluginMain.wands;

import javax.annotation.Nullable;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

import com.gmail.robbiem.BukkitPluginMain.Main;
import com.gmail.robbiem.BukkitPluginMain.UseableItem;

public abstract class Wand extends UseableItem {
	
	public Wand(Main plugin) {
		super(plugin);
	}
	
	public static Location getTarget(Player player, int maxDistance, boolean alwaysHit) {
		RayTraceResult result = player.getWorld().rayTrace(player.getEyeLocation(), player.getLocation().getDirection(), maxDistance, FluidCollisionMode.NEVER, false, 0, entity -> !entity.equals(player));
		if (result == null) {
			if (alwaysHit) {
				return player.getLocation().add(player.getLocation().getDirection().multiply(maxDistance));
			} else return null;
		}
		return result.getHitPosition().toLocation(player.getWorld());
	}
	
	@Nullable
	public static Location getBlockTarget(Player player, int maxDistance, boolean alwaysHit) {
		RayTraceResult result = player.getWorld().rayTraceBlocks(player.getLocation(), player.getLocation().getDirection(), maxDistance);
		if (result == null) {
			if (alwaysHit) {
				return player.getLocation().add(player.getLocation().getDirection().multiply(maxDistance));
			} else return null;
		}
		return result.getHitPosition().toLocation(player.getWorld());
	}
	
	//	public abstract ShapedRecipe getCraftingRecipeFromResultingItem(ShapedRecipe startingRecipe);
	public abstract Material getWandTip();
	
	public Material getWandBase() {
		return Material.ENDER_PEARL;
	}
	
	public boolean isWeapon() {
		return true;
	}
}
