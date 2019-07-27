package com.gmail.robbiem.BukkitPluginMain.scrolls;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ScrollOfBlindness extends Scroll {

	@Override
	public void use(ItemStack wandItem, Player player, World world, JavaPlugin plugin, Server server) {
		for (Player p: world.getPlayers()) {
			if (!p.equals(player)) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 10, 1));
			}
		}
	}

	@Override
	public long getCooldown() {
		return 1000l;
	}

	@Override
	public Material getCraftingRecipeCenterItem() {
		return Material.SPIDER_EYE;
	}

	@Override
	public String getLore() {
		return "Blinds all players\naround you";
	}

}
