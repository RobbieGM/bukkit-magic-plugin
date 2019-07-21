package com.gmail.robbiem.BukkitPluginMain.wands;

import org.bukkit.inventory.ShapedRecipe;

import com.gmail.robbiem.BukkitPluginMain.UseableItem;

public abstract class Wand implements UseableItem {
	public abstract ShapedRecipe getCraftingRecipeFromResultingItem(ShapedRecipe startingRecipe);
	
	public boolean isEventHandler() {
		return false;
	}
	
	public boolean isWeapon() {
		return true;
	}
}
