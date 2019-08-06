package com.gmail.robbiem.BukkitPluginMain.runes;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import com.gmail.robbiem.BukkitPluginMain.Main;

public class RuneOfFeatherFalling extends Rune {

	public RuneOfFeatherFalling(Main plugin) {
		super(plugin);
	}
	
	@EventHandler
	public void onFallDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player && playerHasRune((Player) e.getEntity()) && e.getCause() == DamageCause.FALL && e.getFinalDamage() > 4) {
			e.setCancelled(true);
			Player player = (Player) e.getEntity();
			consumeRune(player);
			playRuneEffect(player);
		}
	}

	@Override
	String getRuneName() {
		return "Rune of Feather Falling";
	}

}
