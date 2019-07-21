package com.gmail.robbiem.BukkitPluginMain.runes;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class RuneOfInvincibility extends Rune {
	
	public RuneOfInvincibility(JavaPlugin plugin) {
		super(plugin);
		// TODO Auto-generated constructor stub
	}

	@EventHandler(priority=EventPriority.HIGH)
	public void onPlayerDamagedByEntity(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && playerHasRune((Player) e.getEntity())) {
			Player p = (Player) e.getEntity();
			consumeRune(p);
			playRuneEffect(p);
			e.setCancelled(true);
			p.setInvulnerable(true);
			p.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
				p.setInvulnerable(false);
			}, 20 * 5);
		}
	}

	@Override
	String getRuneName() {
		return "Rune of Invincibility";
	}

}
