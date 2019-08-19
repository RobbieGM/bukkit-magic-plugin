package com.gmail.robbiem.BukkitPluginMain.runes;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import com.gmail.robbiem.BukkitPluginMain.Main;

public class RuneOfInvincibility extends Rune {
	
	public RuneOfInvincibility(Main plugin) {
		super(plugin);
	}

	@EventHandler(priority=EventPriority.HIGH)
	public void onPlayerDamagedByEntity(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && playerHasRune((Player) e.getEntity()) && ((Player) e.getEntity()).getHealth() - e.getFinalDamage() <= 6) {
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
	public String getName() {
		return "Rune of Invincibility";
	}

	@Override
	public Material getCraftingRecipeCenterItem() {
		return Material.DIAMOND_CHESTPLATE;
	}

	@Override
	public String getLore() {
		return "When attacked by a player and your\nhealth goes below 3 hearts,\nbecome invincible for 5 seconds.";
	}

}
