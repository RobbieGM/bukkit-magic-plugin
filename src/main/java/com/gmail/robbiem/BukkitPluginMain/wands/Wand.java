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

	public boolean isBuffed = false;

	public Wand(Main plugin) {
		super(plugin);
	}

	@Nullable
	public static Location getTarget(Player player, int maxDistance, boolean alwaysHit) {
		RayTraceResult result = player.getWorld().rayTrace(player.getEyeLocation(), player.getLocation().getDirection(),
				maxDistance, FluidCollisionMode.NEVER, true, 0, entity -> !entity.equals(player));
		if (result == null) {
			if (alwaysHit)
				return player.getEyeLocation().add(player.getLocation().getDirection().multiply(maxDistance));
			else
				return null;
		}
		return result.getHitPosition().toLocation(player.getWorld());
	}

	@Nullable
	public static Location getBlockTarget(Player player, int maxDistance, boolean alwaysHit) {
		RayTraceResult result = player.getWorld().rayTraceBlocks(player.getEyeLocation(),
				player.getLocation().getDirection(), maxDistance);
		if (result == null) {
			if (alwaysHit)
				return player.getEyeLocation().add(player.getLocation().getDirection().multiply(maxDistance));
			else
				return null;
		}
		return result.getHitPosition().toLocation(player.getWorld());
	}

	// public abstract ShapedRecipe getCraftingRecipeFromResultingItem(ShapedRecipe
	// startingRecipe);
	public abstract Material getWandTip();

	public Material getWandBase() {
		return Material.ENDER_PEARL;
	}

	public boolean isWeapon() {
		return true;
	}
}
