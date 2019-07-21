package com.gmail.robbiem.BukkitPluginMain.runes;

import org.bukkit.entity.Creeper;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class RuneOfVengeance extends Rune {

	public RuneOfVengeance(JavaPlugin plugin) {
		super(plugin);
		// TODO Auto-generated constructor stub
	}

	@EventHandler
	public void onPlayerDamagedByEntity(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && playerHasRune((Player) e.getEntity()) && !(e.getDamager() instanceof Creeper)) {
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
	String getRuneName() {
		return "Rune of Vengeance";
	}

}
