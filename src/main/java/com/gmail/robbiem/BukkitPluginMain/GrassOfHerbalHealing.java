package com.gmail.robbiem.BukkitPluginMain;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GrassOfHerbalHealing implements Listener {
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		Player player = e.getPlayer();
		if (e.getBlock().getType() == Material.GRASS &&
			player != null &&
			e.getItemInHand().getItemMeta().hasDisplayName() &&
			e.getItemInHand().getItemMeta().getDisplayName().equals("Grass of Herbal Healing")) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 10, 3), true);
		}
	}
}
