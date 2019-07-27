package com.gmail.robbiem.BukkitPluginMain.scrolls;

import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class ScrollOfTheOracle extends Scroll {

	@Override
	public void use(ItemStack wandItem, Player player, World world, JavaPlugin plugin, Server server) {
		Location center = world.getWorldBorder().getCenter();
		ItemStack heartOfTheSea = new ItemStack(Material.HEART_OF_THE_SEA);
		ItemMeta meta = heartOfTheSea.getItemMeta();
		meta.setDisplayName("Crystal Ball");
		meta.setLore(Arrays.asList("X: " + center.getBlockX(), "Z: " + center.getBlockZ()));
		heartOfTheSea.setItemMeta(meta);
		player.getInventory().addItem(heartOfTheSea);
		world.playSound(player.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1, 1);
	}

	@Override
	public long getCooldown() {
		return 0;
	}

	@Override
	public Material getCraftingRecipeCenterItem() {
		return Material.ENDER_EYE;
	}

	@Override
	public String getLore() {
		return "Gives you a crystal ball revealing the\nend coordinates of the world border";
	}

}
