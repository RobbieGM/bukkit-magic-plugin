package com.gmail.robbiem.BukkitPluginMain.scrolls;

import org.bukkit.Material;

import com.gmail.robbiem.BukkitPluginMain.Main;
import com.gmail.robbiem.BukkitPluginMain.UseableItem;

public abstract class Scroll extends UseableItem {
	public Scroll(Main plugin) {
		super(plugin);
	}

	public abstract Material getCraftingRecipeCenterItem();
}
