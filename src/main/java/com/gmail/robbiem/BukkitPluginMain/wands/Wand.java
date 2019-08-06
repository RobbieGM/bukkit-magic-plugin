package com.gmail.robbiem.BukkitPluginMain.wands;

import org.bukkit.Material;

import com.gmail.robbiem.BukkitPluginMain.Main;
import com.gmail.robbiem.BukkitPluginMain.UseableItem;

public abstract class Wand extends UseableItem {
	public Wand(Main plugin) {
		super(plugin);
	}
	//	public abstract ShapedRecipe getCraftingRecipeFromResultingItem(ShapedRecipe startingRecipe);
	public abstract Material getWandTip();
	public boolean isWeapon() {
		return true;
	}
}
