package com.gmail.robbiem.BukkitPluginMain.scrolls;

import org.bukkit.Material;

import com.gmail.robbiem.BukkitPluginMain.UseableItem;

public abstract class Scroll implements UseableItem {
	public abstract Material getCraftingRecipeCenterItem();
	public boolean isEventHandler() {
		return false;
	}
}
