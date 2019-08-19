package com.gmail.robbiem.BukkitPluginMain.scrolls;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.gmail.robbiem.BukkitPluginMain.Main;

public class ScrollOfTheHuntersVision extends Scroll {

	public ScrollOfTheHuntersVision(Main plugin) {
		super(plugin);
	}

	@Override
	public boolean use(ItemStack wandItem, Player player, World world, Server server) {
		for (Player p: world.getPlayers()) {
			if (!p.equals(player)) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20 * 10, 1));
			}
		}
		return true;
	}

	@Override
	public long getPlayerCooldown() {
		return 5000l;
	}

	@Override
	public Material getCraftingRecipeCenterItem() {
		return Material.GLOWSTONE;
	}

	@Override
	public String getLore() {
		return "Makes all other players glow\nfor 10 seconds";
	}

	@Override
	public String getName() {
		return "Scroll of the Hunter's Vision";
	}

}
