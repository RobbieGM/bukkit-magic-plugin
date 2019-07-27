package com.gmail.robbiem.BukkitPluginMain.runes;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class RuneOfBounce extends Rune {
	
	List<Projectile> bouncingProjectiles = new ArrayList<>();

	public RuneOfBounce(JavaPlugin plugin) {
		super(plugin);
	}
	
	@EventHandler(priority=EventPriority.HIGH)
	public void onProjectileHitPlayer(ProjectileHitEvent e) {
		if (e.getHitEntity() instanceof Player && playerHasRune((Player) e.getHitEntity())) {
			Player p = (Player) e.getHitEntity();
			consumeRune(p);
			playRuneEffect(p);
			bouncingProjectiles.add(e.getEntity());
			Projectile newProjectile = (Projectile) e.getEntity().getWorld().spawnEntity(e.getEntity().getLocation(), e.getEntityType());
			newProjectile.setVelocity(e.getEntity().getVelocity().clone().multiply(-1));
		}
	}
	
	@EventHandler(priority=EventPriority.HIGH)
	public void onPlayerDamagedByEntity(EntityDamageByEntityEvent e) {
		if (bouncingProjectiles.contains(e.getDamager())) {
			e.setCancelled(true);
		}
	}

	@Override
	String getRuneName() {
		return "Rune of Bounce";
	}

}
