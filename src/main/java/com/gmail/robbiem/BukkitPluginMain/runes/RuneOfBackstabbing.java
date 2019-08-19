package com.gmail.robbiem.BukkitPluginMain.runes;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import com.gmail.robbiem.BukkitPluginMain.Main;

public class RuneOfBackstabbing extends Rune {

	public RuneOfBackstabbing(Main plugin) {
		super(plugin);
	}

	@EventHandler
	public void onPlayerHurtByPlayer(EntityDamageByEntityEvent e) {
		Entity damager = getDamageSource(e.getDamager());
		if (!e.isCancelled() && e.getEntity() instanceof Player && damager instanceof Player
				&& playerHasRune((Player) e.getEntity())) {
			Player damaged = (Player) e.getEntity();
			consumeRune(damaged);
			playRuneEffect(damaged);
			e.setCancelled(true);
			((Player) damager).damage(8, damaged);
			Location newLoc = damager.getLocation();
			newLoc.subtract(newLoc.getDirection().setY(0).normalize().multiply(2));
			if (newLoc.getBlock().getType().isSolid())
				newLoc.setY(damaged.getWorld().getHighestBlockYAt(newLoc));
			damaged.teleport(newLoc);
		}
	}

	@Override
	public String getName() {
		return "Rune of Backstabbing";
	}

	@Override
	int getCraftingYield() {
		return 4;
	}

	@Override
	public Material getCraftingRecipeCenterItem() {
		return Material.IRON_SWORD;
	}

	@Override
	public String getLore() {
		return "When attacked by a player,\nteleport behind them.";
	}
}
