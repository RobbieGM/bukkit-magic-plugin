package com.gmail.robbiem.BukkitPluginMain.runes;

import org.bukkit.Material;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import com.gmail.robbiem.BukkitPluginMain.Main;

public class RuneOfVengeance extends Rune {

	public RuneOfVengeance(Main plugin) {
		super(plugin);
	}

	@EventHandler
	public void onPlayerDamagedByEntity(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && playerHasRune((Player) e.getEntity())
				&& !(e.getDamager() instanceof Creeper)) {
			Entity damager = getDamageSource(e.getDamager());
			if (damager instanceof Damageable && !(damager instanceof Player)) {
				Player damaged = (Player) e.getEntity();
				consumeRune(damaged);
				playRuneEffect(damaged);
				((Damageable) damager).damage(10000.0, damaged);
			}
		}
	}

	@Override
	public String getName() {
		return "Rune of Vengeance";
	}

	@Override
	public int getCraftingYield() {
		return 6;
	}

	@Override
	public Material getCraftingRecipeCenterItem() {
		return Material.BONE;
	}

	@Override
	public String getLore() {
		return "When attacked by a mob,\nkill it.";
	}

}
